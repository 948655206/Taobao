package com.zxyapp.taobaounion.ui.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.zxyapp.taobaounion.R;
import com.zxyapp.taobaounion.model.domain.Categories;
import com.zxyapp.taobaounion.presenter.IHomePresenter;
import com.zxyapp.taobaounion.ui.activity.IMainActivity;
import com.zxyapp.taobaounion.ui.activity.MainActivity;
import com.zxyapp.taobaounion.ui.activity.ScanQrCodeActivity;
import com.zxyapp.taobaounion.ui.adatper.HomePagerAdapter;
import com.zxyapp.taobaounion.utils.LogUtils;
import com.zxyapp.taobaounion.utils.PresenterManager;
import com.zxyapp.taobaounion.view.IHomeCallback;

import butterknife.BindView;

public class HomeFragment extends BaseFragment implements IHomeCallback {

    private IHomePresenter mHomePresenter;

    @BindView (R.id.home_indicator)
    public TabLayout mTabLayout;

    @BindView (R.id.home_pager)
    public ViewPager homePager;

    @BindView(R.id.home_search_input_box)
    public View mSearchInputBox;

    @BindView(R.id.scan_icon)
    public View scanIcon;

    private HomePagerAdapter mHomePagerAdapter;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home;
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate (R.layout.base_home_fragment,container,false);
    }

    @Override
    protected void loadData() {
        //加载数据
        mHomePresenter.getCategories ();
    }

    @Override
    protected void initPresenter() {
        //创建Presenter
        mHomePresenter = PresenterManager.getInstance().getHomePresenter();
        mHomePresenter.registerViewCallback (this);
        mSearchInputBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到搜索界面
                FragmentActivity activity = getActivity();
                if (activity instanceof MainActivity){
                    ((IMainActivity)activity).switch2Search();
                }
            }
        });
    }

    @Override
    protected void initView(View rootView) {
        mTabLayout.setupWithViewPager (homePager);
        //给ViewPager设置适配器

        mHomePagerAdapter = new HomePagerAdapter (getChildFragmentManager ());
        homePager.setAdapter (mHomePagerAdapter);
    }

    @Override
    protected void initListener() {
        scanIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到扫码界面
                startActivity(new Intent(getContext(), ScanQrCodeActivity.class));
            }
        });
    }

    @Override
    public void onCategoriesLoaded(Categories categories) {
        setUpState (State.SUCCESS);
        LogUtils.d (this,"onCategoriesLoaded.....");
        //加载的数据就会从这里回来
        if (mHomePagerAdapter != null) {
            //这个用来全部数据加载，一般淘宝是只用加载第一页
//            homePager.setOffscreenPageLimit (categories.getData ().size ());
            mHomePagerAdapter.setCategories (categories);
        }
    }

    @Override
    public void onLoading() {
        setUpState (State.LOADING);
    }

    @Override
    public void onError() {
        setUpState (State.ERROR);

    }

    @Override
    public void onEmpty() {
        setUpState (State.EMPTY);
    }

    @Override
    protected void release() {
        //取消回调注册
        if (mHomePresenter != null) {
            mHomePresenter.unregisterViewCallback (this);
        }
    }


    @Override
    protected void onRetryClick() {
        //网络错误
        //重新加载分类内容
        if (mHomePresenter != null) {
            LogUtils.d (this,"123");
            mHomePresenter.getCategories ();
        }
    }
}