package com.zxyapp.taobaounion.ui.fragment;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.views.TbNestedScrollView;
import com.zxyapp.taobaounion.R;
import com.zxyapp.taobaounion.model.domain.Categories;
import com.zxyapp.taobaounion.model.domain.HomePagerContent;
import com.zxyapp.taobaounion.model.domain.IBaseInfo;
import com.zxyapp.taobaounion.presenter.ICategoryPagerPresenter;
import com.zxyapp.taobaounion.ui.adatper.LinearItemContentAdapter;
import com.zxyapp.taobaounion.ui.adatper.LooperPagerAdapter;
import com.zxyapp.taobaounion.ui.custom.AutoLoopViewPager;
import com.zxyapp.taobaounion.utils.Constants;
import com.zxyapp.taobaounion.utils.LogUtils;
import com.zxyapp.taobaounion.utils.PresenterManager;
import com.zxyapp.taobaounion.utils.SizeUtils;
import com.zxyapp.taobaounion.utils.TicketUtil;
import com.zxyapp.taobaounion.utils.ToastUtil;
import com.zxyapp.taobaounion.view.ICategoryPagerCallback;

import java.util.List;

import butterknife.BindView;

public class HomePagerFragment extends BaseFragment implements ICategoryPagerCallback, LinearItemContentAdapter.OnListItemClickListener, LooperPagerAdapter.OnLooperPageItemClickListener {

    private ICategoryPagerPresenter mCategoryPagePresenter;
    private int mMaterialId;
    private LinearItemContentAdapter mContentAdapter;
    private LooperPagerAdapter mLooperPagerAdapter;

    public static HomePagerFragment newInstance(Categories.DataDTO category) {
        HomePagerFragment homePagerFragment = new HomePagerFragment ();
        //
        Bundle bundle = new Bundle ();
        bundle.putString (Constants.KEY_HOME_PAGER_TITLE, category.getTitle ());
        bundle.putInt (Constants.KEY_HOME_PAGER_MATERIAL_ID, category.getId ());
        homePagerFragment.setArguments (bundle);
        return homePagerFragment;
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home_pager;
    }

    @Override
    protected void initListener() {
        mContentAdapter.setOnListItemOnClickItemListener(this);
        mLooperPagerAdapter.setOnLooperPageItemClickListener(this);
        //动态设置高度
        homePagerParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (homeHeaderContainer==null){
                    return;
                }
                int headerHeight = homeHeaderContainer.getMeasuredHeight();
//                LogUtils.d(this,"headerHeight--->"+headerHeight);
                homePagerNestedView.setHeaderHeight(headerHeight);
                //
                int measuredHeight = homePagerParent.getMeasuredHeight();
//                LogUtils.d(this,"measuredHeight------->"+measuredHeight);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mContentList.getLayoutParams();
                layoutParams.height=measuredHeight;
                mContentList.setLayoutParams(layoutParams);
                if (measuredHeight!=0){
                    homePagerParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
        currentCategoryTitleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo:
                int measuredHeight = mContentList.getMeasuredHeight();
//                LogUtils.d(HomePagerFragment.this,"measuredHeight------>"+measuredHeight);

            }
        });
        looperPager.addOnPageChangeListener (new ViewPager.OnPageChangeListener () {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mLooperPagerAdapter.getDataSize ()==0){
                    return;
                }
                int targetPosition=position%mLooperPagerAdapter.getDataSize ();
                //切换指示器
                updateLooperIndicator(targetPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTwinklingRefreshLayout.setOnRefreshListener (new RefreshListenerAdapter () {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                LogUtils.d (HomePagerFragment.this,"触发了Loader more...");
                //TODO:去加载更多内容
                if (mCategoryPagePresenter != null) {
                    mCategoryPagePresenter.loaderMore (mMaterialId);
                }
            }

            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh (refreshLayout);
            }
        });
    }

    /**
     * 切换指示器
     * @param targetPosition
     */
    private void updateLooperIndicator(int targetPosition) {
        for (int i = 0; i < looperPointContainer.getChildCount (); i++) {
            View point=looperPointContainer.getChildAt (i);
            if (i==targetPosition){
                point.setBackgroundResource (R.drawable.shape_indicator_point_selected);
            }else {
                point.setBackgroundResource (R.drawable.shape_indicator_point_normal);
            }
        }

    }

    @BindView (R.id.home_pager_content_list)
    public RecyclerView mContentList;

    @BindView (R.id.looper_pager)
    public AutoLoopViewPager looperPager;

    @BindView (R.id.home_pager_title)
    public TextView currentCategoryTitleTv;

    @BindView (R.id.looper_point_container)
    public LinearLayout looperPointContainer;

    @BindView (R.id.home_pager_refresh)
    public TwinklingRefreshLayout mTwinklingRefreshLayout;

    @BindView (R.id.home_pager_parent)
    public LinearLayout homePagerParent;

    @BindView (R.id.home_pager_header_container)
    public LinearLayout homeHeaderContainer;

    @BindView (R.id.home_pager_nested_scroller)
    public TbNestedScrollView homePagerNestedView;

    @Override
    public void onResume() {
        super.onResume();
        //可见我们去调用开始轮播
        looperPager.startLoop();
        LogUtils.d(this,"onResume....");
    }

    @Override
    public void onPause() {
        super.onPause();
        //不可见的时候暂停
        looperPager.stopLoop();
        LogUtils.d(this,"onPause....");

    }

    @Override
    protected void initView(View rootView) {
        //设置布局管理器
        mContentList.setLayoutManager (new LinearLayoutManager (getContext ()));
        mContentList.addItemDecoration (new RecyclerView.ItemDecoration () {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top=8;
                outRect.bottom=8;
            }
        });
        //创建适配器
        mContentAdapter = new LinearItemContentAdapter();
        //设置适配器
        mContentList.setAdapter (mContentAdapter);
        //创建适配器
        mLooperPagerAdapter = new LooperPagerAdapter ();
        //设置适配器
        looperPager.setAdapter (mLooperPagerAdapter);
        //设置Refresh相关内容
        mTwinklingRefreshLayout.setEnableRefresh (false);
        mTwinklingRefreshLayout.setEnableLoadmore (true);
    }

    @Override
    protected void initPresenter() {
        mCategoryPagePresenter = PresenterManager.getInstance().getCategoryPagePresenter();
        mCategoryPagePresenter.registerViewCallback (this);
    }

    @Override
    protected void loadData() {
        Bundle arguments = getArguments ();
        String title = arguments.getString (Constants.KEY_HOME_PAGER_TITLE);
        mMaterialId = arguments.getInt (Constants.KEY_HOME_PAGER_MATERIAL_ID);
        LogUtils.d (this, "title---->" + title);
        LogUtils.d (this, "materialId---->" + mMaterialId);
        //TODO:加载数据
        if (mCategoryPagePresenter != null) {
            mCategoryPagePresenter.getContentByCategoryId (mMaterialId);
        }
        if (currentCategoryTitleTv != null) {
            currentCategoryTitleTv.setText (title);
        }
    }


    @Override
    public void onContentLoaded(List<HomePagerContent.DataDTO> contents) {
        //数据列表加载到了
        mContentAdapter.setData(contents);
        setUpState (State.SUCCESS);
    }

    @Override
    public int getCategoryId() {
        return mMaterialId;
    }

    @Override
    public void onLoading() {
        setUpState (State.LOADING);
    }

    @Override
    public void onError() {
        //网络错误
        setUpState (State.ERROR);
    }

    @Override
    public void onEmpty() {
        setUpState (State.EMPTY);
    }

    @Override
    public void onLoaderMoreError() {
        ToastUtil.showToast ("网络异常，请稍后重试");
        if (mTwinklingRefreshLayout != null) {
            mTwinklingRefreshLayout.finishLoadmore ();
        }
    }

    @Override
    public void onLoaderMoreEmpty() {
        ToastUtil.showToast ("没有了更多商品");
        if (mTwinklingRefreshLayout != null) {
            mTwinklingRefreshLayout.finishLoadmore ();
        }
    }

    @Override
    public void onLoaderMoreLoaded(List<HomePagerContent.DataDTO> contents) {
        //添加到适配器数据的底部
        mContentAdapter.addData(contents);
        if (mTwinklingRefreshLayout != null) {
            mTwinklingRefreshLayout.finishLoadmore ();
        }
        ToastUtil.showToast ("加载了"+contents.size ()+"条数据");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onLooperListLoaded(List<HomePagerContent.DataDTO> contents) {
        LogUtils.d (this,"size----->"+contents.size ());
        mLooperPagerAdapter.setData(contents);
        //中间点%数据的Size不一定为0,所以就不是第一个
        //处理一下
        //第0个开始
        int dx = (Integer.MAX_VALUE / 2) % contents.size ();
        int targetCenterPosition= (Integer.MAX_VALUE / 2) - dx;
        //设置到中间点
        if (looperPager!=null){
            looperPager.setCurrentItem (targetCenterPosition);
        }
//        LogUtils.d (this,"url------->"+contents.get (0).getPictUrl ());
        if (looperPointContainer==null){
            return;
        }
        looperPointContainer.removeAllViews ();
        //添加点
        for (int i = 0; i < contents.size (); i++) {
            View point=new View (getContext ());
            int size=SizeUtils.dip2px (getContext (),8);
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams (size,size);
            layoutParams.leftMargin =SizeUtils.dip2px (getContext (),5);
            layoutParams.rightMargin =SizeUtils.dip2px (getContext (),5);
            point.setLayoutParams (layoutParams);
            if (i==0){
                point.setBackgroundResource (R.drawable.shape_indicator_point_selected);
            }else {
                point.setBackgroundResource (R.drawable.shape_indicator_point_normal);
            }
            if (looperPointContainer != null) {
                looperPointContainer.addView (point);
            }
        }
    }

    @Override
    protected void release() {
        mCategoryPagePresenter.unregisterViewCallback (this);
    }

    @Override
    public void onItemClick(IBaseInfo item) {
        //列表内容被点击了
        LogUtils.d(this,"item click ---->"+item.getTitle());
        handleItemClick(item);
    }

    /**
     * 点击跳转到对应的activity
     * @param item
     */
    private void handleItemClick(IBaseInfo item) {
        TicketUtil.toTicketPage(getContext(),item);
    }

    @Override
    public void onLooperItemClick(IBaseInfo item) {
        //轮播图内容被点击了
        LogUtils.d(this,"item click ---->"+item.getTitle());
        handleItemClick(item);
    }
}