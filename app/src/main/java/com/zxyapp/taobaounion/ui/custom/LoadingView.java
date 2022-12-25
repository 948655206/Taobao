package com.zxyapp.taobaounion.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.zxyapp.taobaounion.R;
import com.zxyapp.taobaounion.utils.LogUtils;

public class LoadingView extends AppCompatImageView {
    private float mDegrees=0;
    private boolean mNeedRotate=true;
    public LoadingView(Context context) {
        this(context,null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImageResource(R.mipmap.loading);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startRotate();
    }

    private void startRotate() {
        mNeedRotate=true;
        post(new Runnable() {
            @Override
            public void run() {
                mDegrees+=10;
                if (mDegrees>=360){
                    mDegrees=0;
                }
                //重绘
                invalidate();
//                LogUtils.d(this,"Loading....");
                //判断是否要继续旋转
                //如果不可见，或者已经DetachFormWindow就不在转动了
                //getVisibility()中，0代表可见
                if (getVisibility()!=VISIBLE&& !mNeedRotate){
                    removeCallbacks(this);
                }else {
                    postDelayed(this,10);
                }
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopRotate();
    }

    private void stopRotate() {
        mNeedRotate=false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(mDegrees,getWidth()/2,getHeight()/2);
        super.onDraw(canvas);
    }
}
