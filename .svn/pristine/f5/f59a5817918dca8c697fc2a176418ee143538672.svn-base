package com.lcodecore.tkrefreshlayout.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

public class TbNestedScrollView extends NestedScrollView {

    private int originScroll=0;
    private int mHeaderHeight=0;
    private RecyclerView mRecycleView;


    public TbNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public TbNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TbNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void setHeaderHeight(int headerHeight) {
        this.mHeaderHeight = headerHeight;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed, @ViewCompat.NestedScrollType int type) {
        if (target instanceof RecyclerView){
            this.mRecycleView=(RecyclerView)target;
        }
        if (originScroll<mHeaderHeight){
            scrollBy(dx,dy);
            consumed[0]=dx;
            consumed[1]=dy;
        }
        super.onNestedPreScroll(target, dx, dy, consumed,type);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        this .originScroll=t;
        super.onScrollChanged(l, t, oldl, oldt);
    }

    /**
     * 判断子类，是否已经滑动到了底部
     * @return
     */
    public boolean isInBottom() {
        if (mRecycleView != null) {
            boolean isBottom = !mRecycleView.canScrollVertically(1);
            return isBottom;
        }
        return false;
    }
}
