package com.zxyapp.taobaounion.view;

import com.zxyapp.taobaounion.base.IBaseCallback;
import com.zxyapp.taobaounion.model.domain.Histories;
import com.zxyapp.taobaounion.model.domain.SearchRecommend;
import com.zxyapp.taobaounion.model.domain.SearchResult;

import java.util.List;

public interface ISearchViewCallback extends IBaseCallback {
    /**
     * 搜索历史内容
     * @param histories
     */
    void onHistoryLoaded(Histories histories);

    /**
     * 历史记录删除完成
     */
    void onHistoriesDeleted();

    /**
     * 搜索结果：成功 有数据
     * @param result 数据
     */
    void onSearchSuccess(SearchResult result);

    /**
     * 加载到了更多内容
     * @param result
     */
    void onMoreLoad(SearchResult result);

    /**
     * 加载更多内容失败
     */
    void onMoreLoadedError();

    /**
     * 没有更多内容
     */
    void onMoreLoadedEmpty();

    /**
     * 获取推荐词
     * @param recommendWords
     */
    void onRecommendWordLoaded(List<SearchRecommend.DataBean> recommendWords);
}
