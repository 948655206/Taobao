package com.zxyapp.taobaounion.presenter.impl;

import com.zxyapp.taobaounion.model.Api;
import com.zxyapp.taobaounion.model.domain.Categories;
import com.zxyapp.taobaounion.presenter.IHomePresenter;
import com.zxyapp.taobaounion.utils.LogUtils;
import com.zxyapp.taobaounion.utils.RetrofitManager;
import com.zxyapp.taobaounion.view.IHomeCallback;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomePresenterImpl implements IHomePresenter {
  private IHomeCallback mCallback=null;

  @Override
  public void getCategories() {
      if (mCallback!=null){
          mCallback.onLoading ();
      }
      //商品分类
    Retrofit retrofit= RetrofitManager.getInstance ().getRetrofit ();
    Api api=retrofit.create (Api.class);
    Call<Categories> task = api.getCategories ();
    task.enqueue (new Callback<Categories> () {
      @Override
      public void onResponse(Call<Categories> call, Response<Categories> response) {
          //数据结果
          //加载分类数据
          int code = response.code ();
          LogUtils.d (this,"code-------->"+code);

            if (code== HttpsURLConnection.HTTP_OK) {
                if (mCallback != null) {
                //请求成功
                Categories categories = response.body ();
                if (categories==null||categories.getData ().size ()==0){
                     mCallback.onEmpty ();
                }else {
                    mCallback.onCategoriesLoaded (categories);
                }
          }
        }else {
          LogUtils.d (this,"加载");
                if (mCallback != null) {
                    mCallback.onError ();
                }
        }
      }

      @Override
      public void onFailure(Call<Categories> call, Throwable t) {
        //加载失败
          if (mCallback != null) {
              mCallback.onError ();
          }
      }
    });
  }

      @Override
      public void registerViewCallback(IHomeCallback callback) {
          this.mCallback=callback;
      }

      @Override
      public void unregisterViewCallback(IHomeCallback callback) {
          mCallback=null;
      }

}