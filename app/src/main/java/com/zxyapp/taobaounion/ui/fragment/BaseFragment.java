package com.zxyapp.taobaounion.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zxyapp.taobaounion.R;
import com.zxyapp.taobaounion.utils.LogUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {

    private Unbinder mBind;
    public FrameLayout mBaseContainer;
    private View mSuccessView;
    private View mLoadingView;
    private View mErrorView;
    private View mEmptyView;

    public enum State{
        NONE,LOADING,SUCCESS,ERROR,EMPTY
    }
    private State currentState=State.NONE;

    @OnClick(R.id.network_error_tips)
    public void retry(){
        //点击了重现加载内容
        LogUtils.d (this,"on retry.....");
        onRetryClick ();
    }

    /**
     * 如果子线程点击了重新加载
     */
    protected void onRetryClick(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = loadRootView(inflater,container);
        mBaseContainer=rootView.findViewById (R.id.base_container);
        //加载全部View
        loadStateView(inflater,container);

        mBind = ButterKnife.bind (this, rootView);

        initView(rootView);
        initListener();
        //注册接口
        initPresenter();
        //加载数据
        loadData();
        return rootView;
    }

    /**
     * 如果子类需要设置相关的监听,覆盖此方法
     */
    protected void initListener() {
    }

    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate (R.layout.base_fragment_layout,container,false);
    }

    private void loadStateView(LayoutInflater inflater, ViewGroup container) {
        //成功的View
        mSuccessView = loadSuccessView (inflater,container);
        mBaseContainer.addView (mSuccessView);
        //加载中的View
        mLoadingView = loadLoadingView (inflater, container);
        mBaseContainer.addView (mLoadingView);

        //错误页面
        mErrorView = loadErrorView (inflater, container);
        mBaseContainer.addView (mErrorView);

        //内容为空
        mEmptyView = loadEmptyView (inflater, container);
        mBaseContainer.addView (mEmptyView);

        setUpState (State.NONE);
    }

    protected View loadEmptyView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate (R.layout.fragment_empty,container,false);
    }

    protected View loadErrorView(LayoutInflater inflater, ViewGroup container) {
        return  inflater.inflate (R.layout.fragment_error,container,false);
    }

    /**
     * 子类通过这个方法切换状态
     * @param state
     */
    public void setUpState(State state){
        this.currentState=state;
        mSuccessView.setVisibility (currentState==State.SUCCESS?View.VISIBLE:View.GONE);
        mLoadingView.setVisibility (currentState==State.LOADING?View.VISIBLE:View.GONE);
        mErrorView.setVisibility (currentState==State.ERROR?View.VISIBLE:View.GONE);
        mEmptyView.setVisibility (currentState==State.EMPTY?View.VISIBLE:View.GONE);
    }

    /**
     * 在家Loding中的view
     * @param inflater
     * @param container
     * @return
     */
    protected View loadLoadingView(LayoutInflater inflater, ViewGroup container) {
        View loadingView = inflater.inflate (R.layout.fragment_loading, container, false);
        return loadingView;
    }

    protected void initView(View rootView) {

    }

    protected void initPresenter() {
        //创建presenter

    }

    protected void loadData() {
        //加载数据
    }

    protected View loadSuccessView(LayoutInflater inflater, ViewGroup container) {
      int resId=getRootViewResId();
      return inflater.inflate (resId,container,false);
    }

    protected abstract int getRootViewResId();

    @Override
    public void onDestroy() {
        super.onDestroy ();
        release ();
    }

    protected void release() {
        //释放资源

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView ();
        if (mBind != null) {
            mBind.unbind ();
        }
    }
}