package com.zxyapp.taobaounion.presenter.impl;

import android.util.Log;

import com.zxyapp.taobaounion.model.Api;
import com.zxyapp.taobaounion.model.domain.OnSellContent;
import com.zxyapp.taobaounion.presenter.IOnSellPagePresenter;
import com.zxyapp.taobaounion.utils.LogUtils;
import com.zxyapp.taobaounion.utils.RetrofitManager;
import com.zxyapp.taobaounion.utils.UrlUtils;
import com.zxyapp.taobaounion.view.IOnSellCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OnSellPagePresenterImpl implements IOnSellPagePresenter {
    public static final int DEFAULT_PAGE=1;
    private final Api mApi;
    private int mCurrentPage=DEFAULT_PAGE;
    private IOnSellCallback mOnSellPageCallback=null;

    @Override
    public void registerViewCallback(IOnSellCallback callback) {
        this.mOnSellPageCallback=callback;
    }

    @Override
    public void unregisterViewCallback(IOnSellCallback callback) {
        this.mOnSellPageCallback=null;
    }

    public OnSellPagePresenterImpl(){
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
    }

    @Override
    public void getOnSellContent() {
//        if (mIsLoading){
//            return;
//        }
//        mIsLoading=true;
        //通知UI为加载中
        if (mOnSellPageCallback != null) {
            mOnSellPageCallback.onLoading();
        }
        //获取特惠内容
        String onSellPageUrl = UrlUtils.getOnSellPageUrl(mCurrentPage);
        LogUtils.d(this,"onSellPageUrl---->"+onSellPageUrl);
        Call<OnSellContent> task = mApi.getOnSellPageContent(onSellPageUrl);
        task.enqueue(new Callback<OnSellContent>() {
            @Override
            public void onResponse(Call<OnSellContent> call, Response<OnSellContent> response) {
                mIsLoading=false;
                int code = response.code();
                LogUtils.d(this,"code--->"+code);
                if (code== HttpURLConnection.HTTP_OK){
                    OnSellContent result = response.body();
                    onSuccess(result);
                }else{
                    onError();
                }
            }
            @Override
            public void onFailure(Call<OnSellContent> call, Throwable t) {
                LogUtils.d(this,"失败"+t);
                onError();
            }
        });
    }

    private void onSuccess(OnSellContent result) {
        if (mOnSellPageCallback != null) {
            try {
                if (isEmpty(result)){
                    onEmpty();
                }else {
                    mOnSellPageCallback.onContentLoadedSuccess(result);
                }
            }catch (Exception e){
                onEmpty();
            }

        }
    }

    private boolean isEmpty(OnSellContent content){
        int size;
        try {
           size = content.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size();
        }catch (Exception e){
            size=0;
        }
        return size==0;
    }

    private void onEmpty() {
        if (mOnSellPageCallback != null) {
            mOnSellPageCallback.onEmpty();
        }
    }

    private void onError() {
        mIsLoading=false;
        if (mOnSellPageCallback!=null){
            mOnSellPageCallback.onError();
        }
    }

    @Override
    public void reLoaded() {
        //重新加载
        this.getOnSellContent();

    }

    /**
     * 当前加载状态
     */
    private boolean mIsLoading=false;

    @Override
    public void loadedMore() {
        if (mIsLoading) {
            return;
        }
        mIsLoading=true;
        //加载更多
        mCurrentPage++;
        //去加载更多内容
        String onSellPageUrl = UrlUtils.getOnSellPageUrl(mCurrentPage);
        Call<OnSellContent> task = mApi.getOnSellPageContent(onSellPageUrl);
        task.enqueue(new Callback<OnSellContent>() {
            @Override
            public void onResponse(Call<OnSellContent> call, Response<OnSellContent> response) {
                mIsLoading=false;
                int code = response.code();
                LogUtils.d(this,"code--->"+code);
                if (code== HttpURLConnection.HTTP_OK){
                    OnSellContent result = response.body();
                    onMoreLoaded(result);
                }else{
                    onLoaderMoreError();
                }
            }

            @Override
            public void onFailure(Call<OnSellContent> call, Throwable t) {
                onLoaderMoreError();
            }
        });
    }


    private void onLoaderMoreError() {
        mIsLoading=false;
        mCurrentPage--;
        mOnSellPageCallback.onMoreLoadedError();
    }

    /**
     * 加载更多，通知UI更新
     */
    private void onMoreLoaded(OnSellContent result) {
        if (mOnSellPageCallback != null) {
            if (isEmpty(result)) {
                mCurrentPage--;
                mOnSellPageCallback.onMoreLoadedEmpty();
            }else {
                mOnSellPageCallback.onMoreLoaded(result);
            }
        }
    }
}
