package com.zxyapp.taobaounion.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zxyapp.taobaounion.R;
import com.zxyapp.taobaounion.model.domain.IBaseInfo;
import com.zxyapp.taobaounion.model.domain.SelectPageCategory;
import com.zxyapp.taobaounion.model.domain.SelectedContent;
import com.zxyapp.taobaounion.presenter.ISelectPagerPresenter;
import com.zxyapp.taobaounion.presenter.ITickPresenter;
import com.zxyapp.taobaounion.ui.activity.TicketActivity;
import com.zxyapp.taobaounion.ui.adatper.SelectPageLeftAdapter;
import com.zxyapp.taobaounion.ui.adatper.SelectedPageContentAdapter;
import com.zxyapp.taobaounion.utils.LogUtils;
import com.zxyapp.taobaounion.utils.PresenterManager;
import com.zxyapp.taobaounion.utils.SizeUtils;
import com.zxyapp.taobaounion.utils.TicketUtil;
import com.zxyapp.taobaounion.view.ISelectedPageCallback;

import java.util.List;

import butterknife.BindView;

public class SelectedFragment extends BaseFragment
        implements ISelectedPageCallback,
        SelectPageLeftAdapter.OnLeftItemClickListener,
        SelectedPageContentAdapter.OnSelectedPageContentItemClickListener {

    @BindView(R.id.left_category_list)
    public RecyclerView leftCategoryList;

    @BindView(R.id.right_content_list)
    public RecyclerView rightContentList;

    @BindView(R.id.fragment_bar_title_tv)
    public TextView barTitleTv;

    private ISelectPagerPresenter mSelectedPagePresenter;
    private SelectPageLeftAdapter mLeftAdapter;
    private SelectedPageContentAdapter mRightAdapter;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_select;
    }

    @Override
    protected void initView(View rootView) {
        barTitleTv.setText("精选宝贝");
        setUpState(State.SUCCESS);
        leftCategoryList.setLayoutManager(new LinearLayoutManager(getContext()));
        //创建适配器
        mLeftAdapter = new SelectPageLeftAdapter();
        //设置适配器
        leftCategoryList.setAdapter(mLeftAdapter);

        rightContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        mRightAdapter = new SelectedPageContentAdapter();
        rightContentList.setAdapter(mRightAdapter);
        //设置间距
        rightContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int topAndBottom = SizeUtils.dip2px(getContext(), 4);
                int rightAndLeft = SizeUtils.dip2px(getContext(), 6);
                outRect.top=topAndBottom ;
                outRect.bottom=topAndBottom;
                outRect.left=rightAndLeft;
                outRect.right=rightAndLeft;
            }
        });
    }

    @Override
    protected void initListener() {
        super.initListener();
        mLeftAdapter.setOnLeftClickListener(this);
        mRightAdapter.setOnSelectedPageContentItemClickListener(this);
    }

    @Override
    protected void initPresenter() {
        super.initPresenter();
        mSelectedPagePresenter = PresenterManager.getInstance().getSelectedPagePresenter();
        mSelectedPagePresenter.registerViewCallback(this);
        mSelectedPagePresenter.getCategories();
    }

    @Override
    protected void onRetryClick() {
        //重试
        if (mSelectedPagePresenter != null) {
            mSelectedPagePresenter.reloadContent();
        }
    }

    @Override
    protected void release() {
        super.release();
        if (mSelectedPagePresenter != null) {
            mSelectedPagePresenter.unregisterViewCallback(this);
        }
    }

    @Override
    public void onError() {
        setUpState(State.ERROR);
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onEmpty() {

    }
    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_with_bar_layout,container,false);
    }
    @Override
    public void onCategoriesLoaded(SelectPageCategory categories) {
        setUpState(State.SUCCESS);
        mLeftAdapter.setData(categories);
        //分类内容
//        LogUtils.d(this, "onCategoriesLoaded------>" + categories);
        //更新UI
        //根据当前选中的分类，获取分类详情内容
//        List<SelectPageCategory.DataBean> data = categories.getData();
//        mSelectedPagePresenter.getContentByCategory(data.get(0));
    }

    @Override
    public void onContentLoaded(SelectedContent content) {
        mRightAdapter.setData(content);
        rightContentList.scrollToPosition(0);
    }

    @Override
    public void onLeftItemClick(SelectPageCategory.DataBean item) {
        //左边分类点击
        mSelectedPagePresenter.getContentByCategory(item);
        LogUtils.d(this, "current selected item---->" + item.getFavorites_title());
    }

    @Override
    public void onClickItemClick(IBaseInfo item) {
        TicketUtil.toTicketPage(getContext(),item);

    }
}