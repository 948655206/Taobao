package com.zxyapp.taobaounion.ui.fragment;

import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.zxyapp.taobaounion.R;
import com.zxyapp.taobaounion.model.domain.Histories;
import com.zxyapp.taobaounion.model.domain.IBaseInfo;
import com.zxyapp.taobaounion.model.domain.SearchRecommend;
import com.zxyapp.taobaounion.model.domain.SearchResult;
import com.zxyapp.taobaounion.presenter.ISearchPresenter;
import com.zxyapp.taobaounion.ui.adatper.LinearItemContentAdapter;
import com.zxyapp.taobaounion.ui.custom.TextFlowLayout;
import com.zxyapp.taobaounion.utils.KeyboardUtils;
import com.zxyapp.taobaounion.utils.LogUtils;
import com.zxyapp.taobaounion.utils.PresenterManager;
import com.zxyapp.taobaounion.utils.SizeUtils;
import com.zxyapp.taobaounion.utils.TicketUtil;
import com.zxyapp.taobaounion.utils.ToastUtil;
import com.zxyapp.taobaounion.view.ISearchViewCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SearchFragment extends BaseFragment implements ISearchViewCallback, TextFlowLayout.OnFlowtTextItemClickListener {

    @BindView(R.id.search_history_view)
    public TextFlowLayout mHistoriesView;

    @BindView(R.id.search_recommend_view)
    public TextFlowLayout mRecommendView;

    @BindView(R.id.search_recommend_container)
    public View mRecommendContainer;

    @BindView(R.id.search_history_container)
    public View mHistoryContainer;

    @BindView(R.id.search_history_delete)
    public View mHistoryDelete;

    @BindView(R.id.search_result_list)
    public RecyclerView mSearchList;

    @BindView(R.id.search_btn)
    public TextView mSearchBtn;

    @BindView(R.id.search_clean_btn)
    public ImageView mCleanInputBtn;

    @BindView(R.id.search_input_box)
    public EditText mSearchInputBox;

    @BindView(R.id.search_result_container)
    public TwinklingRefreshLayout mRefreshLayoutContainer;

    private ISearchPresenter mSearchPresenter;
    private LinearItemContentAdapter mSearchResultAdapter;

    @Override
    protected void initPresenter() {
        mSearchPresenter = PresenterManager.getInstance().getSearchPresenter();
        mSearchPresenter.registerViewCallback(this);
        //获取搜索推荐词语
        mSearchPresenter.getRecommendWords();
//        mSearchPresenter.doSearch("键盘");
        mSearchPresenter.getHistories();
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_search_layout, container, false);
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initListener() {
        mHistoriesView.setOnFlowtTextItemClickListener(this);
        mRecommendView.setOnFlowtTextItemClickListener(this);
        //发起搜索
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //如果有内容搜索
                //如果没有内容取消
                if (hasInput(false)) {
                    //发起搜索
                    if (mSearchPresenter!=null) {
//                        mSearchPresenter.doSearch(mSearchInputBox.getText().toString().trim());
                        toSearch(mSearchInputBox.getText().toString().trim());
                        KeyboardUtils.hide(getContext(),view);
                    }
                }else {
                    //隐藏键盘
                    KeyboardUtils.hide(getContext(),view);
                }
            }
        });
        //清除输入框中的内容
        mCleanInputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchInputBox.setText("");
                //回到历史记录界面
                switch2HistoryPage();
            }
        });

        //监听输入框的内容变化
        mSearchInputBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                //变化的时候
                LogUtils.d(this,"input text----->"+charSequence.toString().trim());
                //如果长度不为0，显示删除按钮
                //否则因此
                mCleanInputBtn.setVisibility(hasInput(true)?View.VISIBLE:View.GONE);
                mSearchBtn.setText(hasInput(false)?"搜索":"取消");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mSearchInputBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
//                LogUtils.d(this,"actionId----->"+actionId);
                if (actionId == EditorInfo.IME_ACTION_SEARCH && mSearchPresenter != null) {
                    String keyWord = textView.getText().toString();
                    //判断拿到的内容是否为空
                    if (TextUtils.isEmpty(keyWord)) {
                        return false;
                    }
                    LogUtils.d(this,"input text---->"+keyWord);
                    //发起搜索
                    toSearch(keyWord);
//                    mSearchPresenter.doSearch(keyWord);
                }
                return false;
            }
        });
        mHistoryDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo:删除历史
                mSearchPresenter.delHistories();
            }
        });
        mRefreshLayoutContainer.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                //去加载更多内容
                if (mSearchPresenter != null) {
                    mSearchPresenter.loadMore();
                }
            }
        });
        mSearchResultAdapter.setOnListItemOnClickItemListener(new LinearItemContentAdapter.OnListItemClickListener() {
            @Override
            public void onItemClick(IBaseInfo item) {
                //搜索列表内容被点击了
                TicketUtil.toTicketPage(getContext(), item);
            }
        });
    }

    /**
     * 切换到历史和推荐界面
     */
    private void switch2HistoryPage() {
        if (mSearchPresenter != null) {
            mSearchPresenter.getHistories();
        }
        if (mRecommendView.getContentSize()!=0) {
            mRecommendContainer.setVisibility(View.VISIBLE);
        }else {
            mRecommendContainer.setVisibility(View.GONE);
        }
        //内容要隐藏
        mRefreshLayoutContainer.setVisibility(View.GONE);
    }

    private boolean hasInput(boolean containSpace){
        if (containSpace){
            return mSearchInputBox.getText().toString().length()>0;
        }else {
            return mSearchInputBox.getText().toString().trim().length()>0;
        }
    }


    @Override
    protected void onRetryClick() {
        //重新加载内容
        if (mSearchPresenter != null) {
            mSearchPresenter.reserach();
        }
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
        //设置布局
        mSearchList.setLayoutManager(new LinearLayoutManager(getContext()));
        //设置适配器
        mSearchResultAdapter = new LinearItemContentAdapter();
        mSearchList.setAdapter(mSearchResultAdapter);
        //设置刷下控件
        mRefreshLayoutContainer.setEnableLoadmore(true);
        mRefreshLayoutContainer.setEnableRefresh(false);
        mRefreshLayoutContainer.setEnableOverScroll(true);
        mSearchList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = SizeUtils.dip2px(getContext(), 1.5f);
                outRect.bottom = SizeUtils.dip2px(getContext(), 1.5f);
            }
        });
    }

    @Override
    protected void release() {
        if (mSearchPresenter != null) {
            mSearchPresenter.unregisterViewCallback(this);
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
        setUpState(State.EMPTY);
    }

    @Override
    public void onHistoryLoaded(Histories histories) {
        LogUtils.d(this, "history----->" + histories);
        if (histories == null || histories.getHistories().size() == 0) {
            mHistoryContainer.setVisibility(View.GONE);
        } else {
            mHistoryContainer.setVisibility(View.VISIBLE);
            mHistoriesView.setTextList(histories.getHistories());
        }
    }

    @Override
    public void onHistoriesDeleted() {
        //删除历史记录
        if (mSearchPresenter != null) {
            mSearchPresenter.getHistories();
        }
    }

    @Override
    public void onSearchSuccess(SearchResult result) {
        setUpState(State.SUCCESS);
//        LogUtils.d(this, "result-----》" + result);
        //隐藏掉历史记录和推荐
        mRecommendContainer.setVisibility(View.GONE);
        mHistoryContainer.setVisibility(View.GONE);
        //显示搜索结果
        mRefreshLayoutContainer.setVisibility(View.VISIBLE);
        //设置数据
        try {
            mSearchResultAdapter.setData(result.getData()
                    .getTbk_dg_material_optional_response()
                    .getResult_list()
                    .getMap_data());
        } catch (Exception e) {
            e.printStackTrace();
            //切换到为内容为空
            setUpState(State.EMPTY);
        }


    }

    @Override
    public void onMoreLoad(SearchResult result) {
        mRefreshLayoutContainer.finishLoadmore();
        //加载更多的结果
        //拿到结果，添加到适配器的尾部
        try {
            List<SearchResult.DataBean.TbkDgMaterialOptionalResponseDataBean.ResultListDataBean.MapDataDataBean> moreData = result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data();
            mSearchResultAdapter.addData(moreData);
            //提示用户加载到的内容
            ToastUtil.showToast("加载到了" + moreData.size() + "条记录");
        }catch (Exception e){
            onMoreLoadedEmpty();
        }
    }

    @Override
    public void onMoreLoadedError() {
        mRefreshLayoutContainer.finishLoadmore();
        ToastUtil.showToast("网络异常,请稍后重试");
    }

    @Override
    public void onMoreLoadedEmpty() {
        mRefreshLayoutContainer.finishLoadmore();
        ToastUtil.showToast("没有更多数据");
    }

    @Override
    public void onRecommendWordLoaded(List<SearchRecommend.DataBean> recommendWords) {
        LogUtils.d(this, "recommendSize---->" + recommendWords.size());
        List<String> recommendKeyWords = new ArrayList<>();
        for (SearchRecommend.DataBean item : recommendWords) {
            recommendKeyWords.add(item.getKeyword());
        }
        if (recommendWords == null || recommendWords.size() == 0) {
            mRecommendContainer.setVisibility(View.GONE);
        } else {
            mRecommendView.setTextList(recommendKeyWords);
            mRecommendContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFlowItemClick(String text) {
        //发起搜索
        toSearch(text);
    }

    private void toSearch(String text) {
        if (mSearchPresenter != null) {
            mSearchList.scrollToPosition(0);
            mSearchInputBox.setText(text);
            mSearchInputBox.setFocusable(true);
            mSearchInputBox.requestFocus();
            mSearchInputBox.setSelection(text.length(),text.length());
            mSearchPresenter.doSearch(text);
        }
    }
}