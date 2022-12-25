package com.zxyapp.taobaounion.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxyapp.taobaounion.R;
import com.zxyapp.taobaounion.utils.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TextFlowLayout extends ViewGroup {

    public static final int DEFAULT_SPACE = 10;

    private float mItemHorizontalSpace = DEFAULT_SPACE;
    private List<String> mTextList = new ArrayList<>();
    private float mItemVerticalSpace = DEFAULT_SPACE;
    private int mSelfWidth;
    private int mItemHeight;
    private OnFlowtTextItemClickListener mItemClickListener=null;

    public int getContentSize(){
        return mTextList.size();
    }

    public float getItemHorizontalSpace() {
        return mItemHorizontalSpace;
    }

    public void setItemHorizontalSpace(float itemHorizontalSpace) {
        this.mItemHorizontalSpace = itemHorizontalSpace;
    }

    public float getItemVerticalSpace() {
        return mItemVerticalSpace;
    }

    public void setItemVerticalSpace(float itemVerticalSpace) {
        mItemVerticalSpace = itemVerticalSpace;
    }

    public TextFlowLayout(Context context) {
        this(context, null);
    }

    public TextFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //去拿到相关属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FlowLayoutStyle);

        mItemHorizontalSpace = ta.getDimension(R.styleable.FlowLayoutStyle_horizontalSpace, DEFAULT_SPACE);
        mItemVerticalSpace = ta.getDimension(R.styleable.FlowLayoutStyle_verticalSpace, DEFAULT_SPACE);
        ta.recycle();
        LogUtils.d(this, "mItemHorizontalSpace == >" + mItemHorizontalSpace);
        LogUtils.d(this, "mItemVerticalSpace == >" + mItemVerticalSpace);
    }

    public void setTextList(List<String> textList) {
        removeAllViews();
        this.mTextList.clear();
        this.mTextList.addAll(textList);
        Collections.reverse(mTextList);
        //遍历内容
        for (String text : mTextList) {
            //添加子View
            //LayoutInflater.from(getContext()).inflate(R.layout.flow_text_view,this,true);
            //等价于
            TextView item = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.flow_text_view, this, false);
            item.setText(text);
            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onFlowItemClick(text);
                    }
                }
            });
            addView(item);
        }
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        //摆放孩子
        LogUtils.d(this, "onLayout--->" + getChildCount());
        int topOffset= (int) mItemHorizontalSpace;
        for (List<View> views : lines) {
            //views是每一行
            int leftOffset=(int) mItemHorizontalSpace;
            for (View view : views) {
                //每一行的每个item
                view.layout(leftOffset,topOffset,leftOffset+view.getMeasuredWidth(),topOffset+view.getMeasuredHeight());
                //
                leftOffset+=view.getMeasuredWidth()+mItemHorizontalSpace;
            }
            topOffset+=mItemHeight+mItemHorizontalSpace;
        }
    }

    //这个是描述所有的行
    private List<List<View>> lines = new ArrayList<>();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount()==0){
            return;
        }
        //这个是描述单行
        List<View> line = null;
        lines.clear();
        mSelfWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
//        LogUtils.d(this, "mSelfWidth==>" + mSelfWidth);
        //测量
//        LogUtils.d(this, "omMessure---->" + getChildCount());
        //测量孩子
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View itemView = getChildAt(i);
            if (itemView.getVisibility()!=VISIBLE) {
                //不需要进行测量
                continue;
            }
            //测量前
            LogUtils.d(this, "height---->" + itemView.getMeasuredHeight());
            measureChild(itemView, widthMeasureSpec, heightMeasureSpec);
            //测量后
            LogUtils.d(this, "afer height-----》" + itemView.getMeasuredHeight());
            if (line == null) {
                //说明当前是空行，可以添加
                line=createNewline(itemView);
            } else {
                //判断是否可以在继续添加
                if (canBeAdd(itemView, line)) {
                    //可以添加
                    line.add(itemView);
                }else{
                    //新创建一行
                    line=createNewline(itemView);
                }
            }
        }
        mItemHeight = getChildAt(0).getMeasuredHeight();
        int selfHeight= (int) (lines.size()*mItemHeight+mItemVerticalSpace*(lines.size()+1)+0.5f);
        //测量自己
        setMeasuredDimension(mSelfWidth,selfHeight);
    }

    private List<View> createNewline(View itemView) {
        List<View> line = new ArrayList<>();
        line.add(itemView);
        lines.add(line);
        return line;
    }

    /**
     * 判断当前行是否可以再继续添加数据
     *
     * @param itemView
     * @param line
     */
    private boolean canBeAdd(View itemView, List<View> line) {
        //所有的已经添加的子View宽度相加+（line.size()+1）*mItemHorizontalSpace+itemView.getMeasureWidth()
        //条件：如果小于/等于当前控件的宽度，则可以添加，否则不能添加
        int totalWidth = itemView.getMeasuredWidth();
        for (View view : line) {
            //叠加所有已经添加控件的宽度
            totalWidth += view.getMeasuredWidth();
        }
        //水平间距的宽度
        totalWidth += mItemHorizontalSpace * (line.size() + 1);
//        LogUtils.d(this, "totalWidth-->" + totalWidth);
//        LogUtils.d(this, "mSelfWidth-->" + mSelfWidth);
        //如果小于/等于当前控件的宽度，则可以添加，否则不能添加
        return totalWidth<=mSelfWidth;
    }

    public void setOnFlowtTextItemClickListener(OnFlowtTextItemClickListener listener){
        this.mItemClickListener=listener;
    }

    public interface OnFlowtTextItemClickListener{
        void onFlowItemClick(String text);
    }
}
