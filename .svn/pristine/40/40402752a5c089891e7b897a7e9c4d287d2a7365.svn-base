package com.zxyapp.taobaounion.presenter.impl;

import com.zxyapp.taobaounion.model.Api;
import com.zxyapp.taobaounion.model.domain.Histories;
import com.zxyapp.taobaounion.model.domain.SearchRecommend;
import com.zxyapp.taobaounion.model.domain.SearchResult;
import com.zxyapp.taobaounion.presenter.ISearchPresenter;
import com.zxyapp.taobaounion.utils.JsonCacheUtil;
import com.zxyapp.taobaounion.utils.LogUtils;
import com.zxyapp.taobaounion.utils.RetrofitManager;
import com.zxyapp.taobaounion.view.ISearchViewCallback;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchPresenter implements ISearchPresenter {

    public static final int DEFAULT_PAGE = 0;
    private final JsonCacheUtil mJsonCacheUtil;
    private int mCurrentPage = DEFAULT_PAGE;
    private final Api mApi;
    private ISearchViewCallback mSearchViewCallback = null;
    private String mCurrentKeyword;

    public SearchPresenter() {
        RetrofitManager instance = RetrofitManager.getInstance();
        Retrofit retrofit = instance.getRetrofit();
        mApi = retrofit.create(Api.class);
        mJsonCacheUtil = JsonCacheUtil.getInstance();
    }

    @Override
    public void registerViewCallback(ISearchViewCallback callback) {
        this.mSearchViewCallback = callback;
    }

    @Override
    public void unregisterViewCallback(ISearchViewCallback callback) {
        this.mSearchViewCallback = null;
    }

    @Override
    public void getHistories() {
        Histories histories = mJsonCacheUtil.getValue(KEY_HISTORIES, Histories.class);
        if (mSearchViewCallback !=null) {
            mSearchViewCallback.onHistoryLoaded(histories);
        }
    }

    @Override
    public void delHistories() {
        mJsonCacheUtil.delCache(KEY_HISTORIES);
        if (mSearchViewCallback != null) {
            mSearchViewCallback.onHistoriesDeleted();
        }
    }


    public static final String KEY_HISTORIES = "key_histories";
    public static final int DEFAULT_HISTORIES_SIZE = 5;
    private int mHistoriesMaxSize = DEFAULT_HISTORIES_SIZE;

    /**
     * 添加历史记录
     *
     * @param history
     */
    private void saveHistory(String history) {
        Histories histories = mJsonCacheUtil.getValue(KEY_HISTORIES, Histories.class);
        //如果说已经有了，就干掉，然后添加
        List<String> historiesList = null;
        if (histories != null && histories.getHistories() != null) {
            historiesList = histories.getHistories();
            if (historiesList.contains(history)) {
                historiesList.remove(history);
            }
        }
        //去重完成
        //处理没有数据的情况
        if (historiesList == null) {
            historiesList = new ArrayList<>();
        }
        if (histories == null) {
            histories = new Histories();
        }
        histories.setHistories(historiesList);
        //对个数限制
        if (historiesList.size() > mHistoriesMaxSize) {
            historiesList = historiesList.subList(0, mHistoriesMaxSize);
        }
        //添加记录
        historiesList.add(history);
        //保存记录
        mJsonCacheUtil.saveCache(KEY_HISTORIES, histories);
    }

    @Override
    public void doSearch(String keyword) {
        if (mCurrentKeyword == null || !mCurrentKeyword.equals(keyword)) {
            this.saveHistory(keyword);
            this.mCurrentKeyword = keyword;
        }
        //更新UI状态
        if (mSearchViewCallback != null) {
            mSearchViewCallback.onLoading();
        }
        Call<SearchResult> task = mApi.doSearch(mCurrentPage, keyword);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                int code = response.code();
                LogUtils.d(this, "code----->" + code);
                if (code == HttpsURLConnection.HTTP_OK) {
                    handleSearchResult(response.body());
                } else {
                    onError();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                onError();
            }
        });
    }

    private void onError() {
        if (mSearchViewCallback != null) {
            mSearchViewCallback.onError();
        }
    }

    private void handleSearchResult(SearchResult result) {
        if (mSearchViewCallback != null) {
            if (isResultEmpty(result)) {
                //数据为空
                mSearchViewCallback.onEmpty();
            } else {
                mSearchViewCallback.onSearchSuccess(result);
            }
        }

    }

    private boolean isResultEmpty(SearchResult result) {
        try {
            return result == null || result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data().size() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void reserach() {
        if (mCurrentKeyword == null) {
            if (mSearchViewCallback != null) {
                mSearchViewCallback.onEmpty();
            }
        } else {
            //可以重新搜索
            this.doSearch(mCurrentKeyword);
        }
    }

    @Override
    public void loadMore() {
        mCurrentPage++;
        //进行搜索
        if (mCurrentKeyword == null) {
            //判断是否为空
            if (mSearchViewCallback != null) {
                mSearchViewCallback.onEmpty();
            }
        } else {
            //做搜索的事情
            doSearchMore();
        }
    }

    private void doSearchMore() {
        Call<SearchResult> task = mApi.doSearch(mCurrentPage, mCurrentKeyword);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                int code = response.code();
                LogUtils.d(this, "code----->" + code);
                if (code == HttpsURLConnection.HTTP_OK) {
                    handleMoreSearchResult(response.body());
                } else {
                    onLoaderMoreError();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                t.printStackTrace();
                onLoaderMoreError();
            }
        });
    }

    /**
     * 处理加载更多的结果
     *
     * @param result
     */
    private void handleMoreSearchResult(SearchResult result) {
        if (mSearchViewCallback != null) {
            if (isResultEmpty(result)) {
                mSearchViewCallback.onMoreLoadedEmpty();
            } else {
                mSearchViewCallback.onMoreLoad(result);
            }
        }
    }

    /**
     * 加载更多内容失败
     */
    private void onLoaderMoreError() {
        mCurrentPage--;
        if (mSearchViewCallback != null) {

            mSearchViewCallback.onMoreLoadedError();
        }
    }

    @Override
    public void getRecommendWords() {
        Call<SearchRecommend> task = mApi.getRecommendWords();
        task.enqueue(new Callback<SearchRecommend>() {
            @Override
            public void onResponse(Call<SearchRecommend> call, Response<SearchRecommend> response) {
                int code = response.code();
                LogUtils.d(this, "code---->" + code);
                if (code == HttpsURLConnection.HTTP_OK) {
                    //处理结果
                    if (mSearchViewCallback != null) {
                        mSearchViewCallback.onRecommendWordLoaded(response.body().getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchRecommend> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
