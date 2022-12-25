package com.zxyapp.taobaounion.presenter.impl;

import android.util.Log;

import com.zxyapp.taobaounion.model.Api;
import com.zxyapp.taobaounion.model.domain.HomePagerContent;
import com.zxyapp.taobaounion.presenter.ICategoryPagerPresenter;
import com.zxyapp.taobaounion.utils.LogUtils;
import com.zxyapp.taobaounion.utils.RetrofitManager;
import com.zxyapp.taobaounion.utils.UrlUtils;
import com.zxyapp.taobaounion.view.ICategoryPagerCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoryPagePresenterImpl implements ICategoryPagerPresenter {

    private Map<Integer,Integer> pagesInfo=new HashMap<> ();
    public static final int DEFAULT_PAGE=1;
    private Integer mCurrentPage;

    private List<ICategoryPagerCallback> callbacks=new ArrayList<> ();

    @Override
    public void registerViewCallback(ICategoryPagerCallback callback) {
        if (!callbacks.contains (callback)){
            callbacks.add (callback);
        }
    }

    @Override
    public void unregisterViewCallback(ICategoryPagerCallback callback) {

    }

    @Override
    public void  getContentByCategoryId(int categoryId) {
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId ()==categoryId) {
                callback.onLoading ();
            }
        }
      //去加载分类Id去加载内容

        Integer targetPage = pagesInfo.get (categoryId);
        if (targetPage == null) {
            targetPage=DEFAULT_PAGE;
            pagesInfo.put (categoryId,targetPage);
        }
        Call<HomePagerContent> task = createTask (categoryId, targetPage);
        task.enqueue (new Callback<HomePagerContent> () {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                int code = response.code ();
                if (code== HttpsURLConnection.HTTP_OK) {
                    HomePagerContent pagerContent = response.body ();
                    LogUtils.d (this,"pageContent"+pagerContent);
                    //更新UI
                    handleHomePageContentResult(pagerContent,categoryId);
                }else {
                    //TODO:
                    handleNetworkError (categoryId);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                handleNetworkError (categoryId);
            }
        });

    }

    private Call<HomePagerContent> createTask(int categoryId, Integer targetPage) {
        String homePagerUrl = UrlUtils.createHomePagerUrl (categoryId, targetPage);
        LogUtils.d (this,"home pager-->"+homePagerUrl);
        Retrofit retrofit= RetrofitManager.getInstance ().getRetrofit ();
        Api api=retrofit.create (Api.class);
        Call<HomePagerContent> task = api.getHomePagerContent (homePagerUrl);
        return task;
    }

    private void handleNetworkError(int categoryId){
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId ()==categoryId) {
                callback.onError ();
            }
        }
    }

    private void handleHomePageContentResult(HomePagerContent pagerContent, int categoryId) {
        //通知UI层更新数据
        List<HomePagerContent.DataDTO> data = pagerContent.getData ();
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId ()==categoryId) {
                if (pagerContent==null||pagerContent.getData ().size ()==0) {
                    callback.onEmpty ();
                }else{
                    List<HomePagerContent.DataDTO> looperData = data.subList (data.size () - 5, data.size ());
                    callback.onLooperListLoaded (looperData);
                    callback.onContentLoaded (data);
                }
            }
        }
    }

    @Override
    public void loaderMore(int categoryId) {
        //加载更多内容数据
        //1、拿到当前页面
        if (mCurrentPage == null) {
            mCurrentPage =1;
        }
        //2、页码++
        mCurrentPage++;
        //3、加载数据
        Call<HomePagerContent> task=createTask (categoryId, mCurrentPage);
        //4、处理数据结果
        task.enqueue (new Callback<HomePagerContent> () {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                //结果
                int code = response.code ();
                LogUtils.d (this,"result---->"+code);
                if (code== HttpsURLConnection.HTTP_OK) {
                    HomePagerContent result = response.body ();
//                    LogUtils.d (this,"result------->"+result.toString ());
                    handleLoaderResult(result,categoryId);
                }else {
                    handleLoaderMoreError(categoryId);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                //请求失败
                LogUtils.d (CategoryPagePresenterImpl.this,t.toString ());
                handleLoaderMoreError(categoryId);
            }
        });
    }

    private void handleLoaderResult(HomePagerContent result, int categoryId) {
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId ()==categoryId) {
                if (result==null||result.getData ().size ()==0){
                    callback.onLoaderMoreEmpty ();
                }else {
                    callback.onLoaderMoreLoaded (result.getData ());
                }
            }
        }
    }

    private void handleLoaderMoreError(int categoryId) {
        mCurrentPage--;
        pagesInfo.put (categoryId,mCurrentPage);
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId ()==categoryId) {
                callback.onLoaderMoreError ();
            }
        }
    }

    @Override
    public void reload(int categoryId) {

    }
}