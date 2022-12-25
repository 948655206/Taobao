package com.zxyapp.taobaounion.utils;

import com.zxyapp.taobaounion.presenter.ICategoryPagerPresenter;
import com.zxyapp.taobaounion.presenter.IHomePresenter;
import com.zxyapp.taobaounion.presenter.IOnSellPagePresenter;
import com.zxyapp.taobaounion.presenter.ISearchPresenter;
import com.zxyapp.taobaounion.presenter.ISelectPagerPresenter;
import com.zxyapp.taobaounion.presenter.ITickPresenter;
import com.zxyapp.taobaounion.presenter.impl.CategoryPagePresenterImpl;
import com.zxyapp.taobaounion.presenter.impl.HomePresenterImpl;
import com.zxyapp.taobaounion.presenter.impl.OnSellPagePresenterImpl;
import com.zxyapp.taobaounion.presenter.impl.SearchPresenter;
import com.zxyapp.taobaounion.presenter.impl.SelectedPagePresenterImpl;
import com.zxyapp.taobaounion.presenter.impl.TicketPresenterImpl;

public class PresenterManager {
    private static final PresenterManager singleton = new PresenterManager();
    private final ICategoryPagerPresenter mCategoryPagePresenter;
    private final IHomePresenter mHomePresenter;
    private final ITickPresenter mTicketPresenter;
    private final ISelectPagerPresenter mSelectedPagePresenter;
    private final IOnSellPagePresenter mOnSellPagePresenter;
    private final ISearchPresenter mSearchPresenter;

    public ITickPresenter getTicketPresenter() {
        return mTicketPresenter;
    }

    public ICategoryPagerPresenter getCategoryPagePresenter(){
        return mCategoryPagePresenter;
    }

    public IHomePresenter getHomePresenter() {
        return mHomePresenter;
    }

    public IOnSellPagePresenter getOnSellPagePresenter() {
        return mOnSellPagePresenter;
    }

    public ISearchPresenter getSearchPresenter() {
        return mSearchPresenter;
    }

    private PresenterManager() {
        mCategoryPagePresenter = new CategoryPagePresenterImpl();
        mHomePresenter = new HomePresenterImpl();
        mTicketPresenter = new TicketPresenterImpl();
        mSelectedPagePresenter = new SelectedPagePresenterImpl();
        mOnSellPagePresenter = new OnSellPagePresenterImpl();
        mSearchPresenter = new SearchPresenter();
    }

    public ISelectPagerPresenter getSelectedPagePresenter() {
        return mSelectedPagePresenter;
    }

    public static PresenterManager getInstance() {
        return singleton;
    }


}
