package com.zxyapp.taobaounion.presenter.impl;

import com.zxyapp.taobaounion.model.Api;
import com.zxyapp.taobaounion.model.domain.SelectPageCategory;
import com.zxyapp.taobaounion.model.domain.SelectedContent;
import com.zxyapp.taobaounion.presenter.ISelectPagerPresenter;
import com.zxyapp.taobaounion.utils.LogUtils;
import com.zxyapp.taobaounion.utils.RetrofitManager;
import com.zxyapp.taobaounion.utils.UrlUtils;
import com.zxyapp.taobaounion.view.ISelectedPageCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SelectedPagePresenterImpl implements ISelectPagerPresenter {
    private final Api mApi;
    private ISelectedPageCallback mViewCallback = null;

    public SelectedPagePresenterImpl() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
    }


    @Override
    public void registerViewCallback(ISelectedPageCallback callback) {
        this.mViewCallback = callback;
    }

    @Override
    public void unregisterViewCallback(ISelectedPageCallback callback) {
        this.mViewCallback = null;
    }

    @Override
    public void getCategories() {
        if (mViewCallback != null) {
            mViewCallback.onLoading();
        }
        //拿到retrofit
        Call<SelectPageCategory> task = mApi.getSelectPageCategory();
        task.enqueue(new Callback<SelectPageCategory>() {
            @Override
            public void onResponse(Call<SelectPageCategory> call, Response<SelectPageCategory> response) {
                int code = response.code();
                LogUtils.d(this, "code------>" + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    SelectPageCategory result = response.body();
                    //通知UI更新
                    if (mViewCallback != null) {
                        mViewCallback.onCategoriesLoaded(result);
                    }

                } else {
                    onLoadedError();
                }
            }

            @Override
            public void onFailure(Call<SelectPageCategory> call, Throwable t) {
                onLoadedError();
                LogUtils.e(this,"错误1==>"+t);
            }
        });
    }

    private void onLoadedError() {
        if (mViewCallback != null) {
            mViewCallback.onError();
        }
    }

    @Override
    public void getContentByCategory(SelectPageCategory.DataBean item) {
        int categoryId=item.getFavorites_id();
        LogUtils.d(this,"categoryId----->"+categoryId);
        String targetUrl=UrlUtils.getSelectedPageContentUrl(categoryId);
        Call<SelectedContent> task = mApi.getSelectedPageContent(targetUrl);
        task.enqueue(new Callback<SelectedContent>() {
            @Override
            public void onResponse(Call<SelectedContent> call, Response<SelectedContent> response) {
                int code = response.code();
                LogUtils.d(this, "code------>" + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    SelectedContent result = response.body();
                    LogUtils.d(this,"result------>"+result);
                    //通知UI更新
                    if (mViewCallback != null) {
                        mViewCallback.onContentLoaded(result);
                    }else {
                        onLoadedError();
                    }
                }
            }

            @Override
            public void onFailure(Call<SelectedContent> call, Throwable t) {
                onLoadedError();
            }
        });
    }

    @Override
    public void reloadContent() {
        this.getCategories();
    }
}
