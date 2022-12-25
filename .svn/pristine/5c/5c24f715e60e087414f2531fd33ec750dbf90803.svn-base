package com.zxyapp.taobaounion.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.zxyapp.taobaounion.R;
import com.zxyapp.taobaounion.model.domain.IBaseInfo;
import com.zxyapp.taobaounion.model.domain.OnSellContent;
import com.zxyapp.taobaounion.presenter.IOnSellPagePresenter;
import com.zxyapp.taobaounion.presenter.ITickPresenter;
import com.zxyapp.taobaounion.ui.activity.TicketActivity;
import com.zxyapp.taobaounion.ui.adatper.OnSellContentAdapter;
import com.zxyapp.taobaounion.utils.LogUtils;
import com.zxyapp.taobaounion.utils.PresenterManager;
import com.zxyapp.taobaounion.utils.SizeUtils;
import com.zxyapp.taobaounion.utils.TicketUtil;
import com.zxyapp.taobaounion.utils.ToastUtil;
import com.zxyapp.taobaounion.view.IOnSellCallback;

import butterknife.BindView;

public class OnSellFragment extends BaseFragment implements IOnSellCallback, OnSellContentAdapter.onSellPageItemClickListener {

  private IOnSellPagePresenter mOnSellPagePresenter;

  private static final int DEFAULT_SPAN_COUNT=2;



  @BindView(R.id.on_sell_content_list)
  public RecyclerView mContentRv;

  @BindView(R.id.on_sell_refresh_layout)
  public TwinklingRefreshLayout mTwinklingRefreshLayout;

  @BindView(R.id.fragment_bar_title_tv)
  public TextView barTitleTv;

  private OnSellContentAdapter mOnSellContentAdapter;

  @Override
  protected void initPresenter() {
    super.initPresenter();
    mOnSellPagePresenter = PresenterManager.getInstance().getOnSellPagePresenter();
    mOnSellPagePresenter.registerViewCallback(this);
    mOnSellPagePresenter.getOnSellContent();
  }

  @Override
  protected void release() {
    super.release();
    if (mOnSellPagePresenter!=null){
      mOnSellPagePresenter.unregisterViewCallback(this);
    }
  }

  @Override
  protected int getRootViewResId() {
    return R.layout.fragment_on_sell;
  }

  @Override
  protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
    return inflater.inflate(R.layout.fragment_with_bar_layout,container,false);
  }

  @Override
  protected void initView(View rootView) {
    setUpState (State.SUCCESS);
    mOnSellContentAdapter = new OnSellContentAdapter();
    //设置布局管理器
    GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),DEFAULT_SPAN_COUNT);
    mContentRv.setLayoutManager(gridLayoutManager);
    mContentRv.setAdapter(mOnSellContentAdapter);
    mContentRv.addItemDecoration(new RecyclerView.ItemDecoration() {
      @Override
      public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
          outRect.top= SizeUtils.dip2px(getContext(),2.5f);
          outRect.bottom= SizeUtils.dip2px(getContext(),2.5f);
          outRect.left=SizeUtils.dip2px(getContext(),2.5f);
          outRect.right=SizeUtils.dip2px(getContext(),2.5f);
      }
    });
    mTwinklingRefreshLayout.setEnableLoadmore(true);
    mTwinklingRefreshLayout.setEnableRefresh(false);
    mTwinklingRefreshLayout.setEnableOverScroll(true);
  }

  @Override
  protected void initListener() {
    super.initListener();
    mTwinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
      @Override
      public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
        //加载更多
        if (mOnSellPagePresenter!=null){
            mOnSellPagePresenter.loadedMore();
        }
      }
    });
    mOnSellContentAdapter.setonSellPageItemClickListener(this);
  }

  @Override
  public void onError() {

  }

  @Override
  public void onLoading() {
      setUpState(State.LOADING);
  }

  @Override
  public void onEmpty() {
    setUpState(State.EMPTY);
  }

  @Override
  public void onContentLoadedSuccess(OnSellContent result) {
      //数据回来了
      setUpState(State.SUCCESS);
      //更新UI
      LogUtils.d(this,"result----->"+result);
      mOnSellContentAdapter.setData(result);
  }

  @Override
  public void onMoreLoaded(OnSellContent moreResult) {
      //
      mTwinklingRefreshLayout.finishLoadmore();
      //添加内容到适配器里
      mOnSellContentAdapter.onMoreLoaded(moreResult);
  }

  @Override
  public void onMoreLoadedError() {
    mTwinklingRefreshLayout.finishLoadmore();
    ToastUtil.showToast("网络异常，请稍后重试");
  }

  @Override
  public void onMoreLoadedEmpty() {
    mTwinklingRefreshLayout.finishLoadmore();
    ToastUtil.showToast("没有更多内容");
  }

  @Override
  public void onSellItemClick(IBaseInfo item) {
    TicketUtil.toTicketPage(getContext(),item);

  }
}