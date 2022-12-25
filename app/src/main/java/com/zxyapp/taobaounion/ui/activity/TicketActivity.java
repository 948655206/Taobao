package com.zxyapp.taobaounion.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zxyapp.taobaounion.R;
import com.zxyapp.taobaounion.model.domain.TicketResult;
import com.zxyapp.taobaounion.presenter.ITickPresenter;
import com.zxyapp.taobaounion.utils.LogUtils;
import com.zxyapp.taobaounion.utils.PresenterManager;
import com.zxyapp.taobaounion.utils.ToastUtil;
import com.zxyapp.taobaounion.utils.UrlUtils;
import com.zxyapp.taobaounion.view.ITicketPagerCallback;

import butterknife.BindView;

public class TicketActivity extends BaseActivity implements ITicketPagerCallback {

    private ITickPresenter mTicketPresenter;

    private boolean mHasTaobaoApp=false;

    @BindView(R.id.ticket_cover)
    public ImageView mCover;

    @BindView(R.id.ticket_code)
    public EditText mTicketCode;

    @BindView(R.id.ticket_copy_or_open_btn)
    public TextView mOpenOrCopyBtn;

    @BindView(R.id.ticket_load_retry)
    public TextView retryLoadText;

    @BindView(R.id.ticket_back_press)
    public ImageView backPress;

    @BindView(R.id.ticket_cover_loading)
    public View loadingView;

    @Override
    protected void initPresenter() {
        mTicketPresenter = PresenterManager.getInstance().getTicketPresenter();
        if (mTicketPresenter != null) {
            mTicketPresenter.registerViewCallback(this);
        }
        //判断是否有安装淘宝
        //act=android.intent.action.MAIN
        //cat=[android.intent.category.LAUNCHER]
        //flg=0x10200000
        //cmp=com.taobao.taobao/com.taobao.tao.welcome.Welcome
        //包名是这个：com.taobao.taobao
        //检查是否有安装淘宝应用
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo("com.taobao.taobao", PackageManager.MATCH_UNINSTALLED_PACKAGES);
            mHasTaobaoApp=packageInfo!=null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            mHasTaobaoApp=false;
        }
        LogUtils.d(this,"mHasTaobaoApp------>"+mHasTaobaoApp);
        //根据这个值去修改UI
        mOpenOrCopyBtn.setText(mHasTaobaoApp?"打开淘宝":"复制淘口令");
    }

    @Override
    protected void release() {
        if (mTicketPresenter != null) {
            mTicketPresenter.unregisterViewCallback(this);
        }
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initEvent() {
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mOpenOrCopyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //复制淘口令
                //拿到内容
                String ticketCode = mTicketCode.getText().toString().trim();
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                LogUtils.d(TicketActivity.this,"ticketCode-------->"+ticketCode);
                //复制到粘贴板
                ClipData clipdata = ClipData.newPlainText("sob_taobao_ticket_code", ticketCode);
                cm.setPrimaryClip(clipdata);
                //判断有没有淘宝
                if (mHasTaobaoApp){
                    //如果有就打开淘宝
                    Intent taobaoIntent=new Intent();
                    ComponentName componentName=new ComponentName("com.taobao.taobao","com.taobao.tao.welcome.Welcome");
                    taobaoIntent.setComponent(componentName);
                    startActivity(taobaoIntent);
                }else {
                    //没有提示复制成功
                    ToastUtil.showToast("已经复制成功，粘贴分享，或打开淘宝");
                }
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_ticket;
    }

    @Override
    public void onError() {
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
        if (retryLoadText != null) {
            retryLoadText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoading() {
        if (loadingView != null) {
            loadingView.setVisibility(View.VISIBLE);
        }
        if (retryLoadText != null) {
            retryLoadText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onTicketLoaded(String cover, TicketResult result) {
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
        //设置图片
        if (mCover != null && !TextUtils.isEmpty(cover)) {
           ViewGroup.LayoutParams layoutParams=mCover.getLayoutParams();
//            int targetWidth = layoutParams.width/2;
            String coverPath = UrlUtils.getCoverPath(cover);
            LogUtils.d(this,"coverPath----->"+coverPath);
            Glide.with(this).load(coverPath).into(mCover);
        }
        if (TextUtils.isEmpty(cover)){
            mCover.setImageResource(R.mipmap.no_image);
        }
        //设置淘口令
        if (result!=null&&result.getData().getTbk_tpwd_create_response()!=null){
            String model = result.getData().getTbk_tpwd_create_response().getData().getModel();
            model=model.substring(2,15);
            LogUtils.d(this,"model------>"+model);
            mTicketCode.setText(model);
        }
    }

}
