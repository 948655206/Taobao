package com.zxyapp.taobaounion.presenter;

import com.zxyapp.taobaounion.base.IBasePresenter;
import com.zxyapp.taobaounion.view.ICategoryPagerCallback;

public interface ICategoryPagerPresenter extends IBasePresenter<ICategoryPagerCallback> {
    /**
     * 根据分类Id获取商品
     * @param categoryId
     */
    void getContentByCategoryId(int categoryId);

    /**
     * 加载更多
     * @param categoryId
     */
    void loaderMore(int categoryId);


    void reload(int categoryId);



}
