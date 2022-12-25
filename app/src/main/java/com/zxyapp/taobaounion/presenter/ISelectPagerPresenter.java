package com.zxyapp.taobaounion.presenter;

import com.zxyapp.taobaounion.base.IBasePresenter;
import com.zxyapp.taobaounion.model.domain.SelectPageCategory;
import com.zxyapp.taobaounion.view.ISelectedPageCallback;

public interface ISelectPagerPresenter extends IBasePresenter<ISelectedPageCallback> {
    /**
     * 获取分类
     */
    void getCategories();

    /**
     * 根据分类获取内容
     * @param item
     */
    void getContentByCategory(SelectPageCategory.DataBean item);

    /**
     * 重新加载内容
     */
    void reloadContent();
}
