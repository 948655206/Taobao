package com.zxyapp.taobaounion.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.zxyapp.taobaounion.R;
import com.zxyapp.taobaounion.utils.LogUtils;


/**
 * 自动轮播
 */
public class AutoLoopViewPager extends ViewPager {
    //间隔时长,单位毫秒
    public static final long DEFAULT_POSITION=3000;

    private long mDuration=DEFAULT_POSITION;

    public AutoLoopViewPager(@NonNull Context context) {
        this(context,null);
    }

    public AutoLoopViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //读取属性
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.AutoLooperStyle);
        //获取属性
        mDuration=t.getInteger(R.styleable.AutoLooperStyle_duration, (int) DEFAULT_POSITION);
//        LogUtils.d(this,"mDuration------>"+mDuration);
        //回收
        t.recycle();
    }

    private boolean isLoop=false;

    public void startLoop(){
        isLoop=true;
        post(mTask);
    }

    /**
     * 设置轮播图切换时长
     * @param duration 时长，单位：毫秒
     */
    public void setDuration(int duration){
        this.mDuration=duration;
    }

    private Runnable mTask=new Runnable() {
        @Override
        public void run() {
            //先拿到当前位置
            int currentItem=getCurrentItem();
            currentItem++;
            setCurrentItem(currentItem);
            if (isLoop==true){
                postDelayed(this,mDuration);
            }
        }
    };

    public void stopLoop(){
        removeCallbacks(mTask);
        isLoop=false;
    }
}
