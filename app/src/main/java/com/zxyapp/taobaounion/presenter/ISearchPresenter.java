package com.zxyapp.taobaounion.presenter;

import com.zxyapp.taobaounion.base.IBasePresenter;
import com.zxyapp.taobaounion.view.ISearchViewCallback;

public interface ISearchPresenter extends IBasePresenter<ISearchViewCallback> {
    /**
     * 获取搜索历史
     */
    void getHistories();

    /**
     * 删除搜索历史
     */
    void delHistories();

    /**
     *搜索
     */
    void doSearch(String keyword);

    /**
     * 重新搜索
     */
    void reserach();

    /**
     * 获取更多的搜索结果
     */
    void loadMore();

    /**
     * 获取推荐词
     */
    void getRecommendWords();
}
