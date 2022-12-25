package com.zxyapp.taobaounion.view;

import com.zxyapp.taobaounion.base.IBaseCallback;
import com.zxyapp.taobaounion.model.domain.HomePagerContent;

import java.util.List;

public interface ICategoryPagerCallback extends IBaseCallback {
    /**
     * 数据加载回来
     * @param content
     */
     void onContentLoaded(List<HomePagerContent.DataDTO> content);

     int getCategoryId();
    /**
     * 加更多网络错误
     */
    void onLoaderMoreError();

    /**
     * 加到了更多内容
     */
    void onLoaderMoreEmpty();

    /**
     * 加到了更多内容
     * @param contents
     */
    void onLoaderMoreLoaded(List<HomePagerContent.DataDTO> contents);

    /**
     * 轮播图内容找到了
     * @param contents
     */
    void onLooperListLoaded(List<HomePagerContent.DataDTO> contents);




}
