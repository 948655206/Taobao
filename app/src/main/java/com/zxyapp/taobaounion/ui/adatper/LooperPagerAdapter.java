package com.zxyapp.taobaounion.ui.adatper;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.zxyapp.taobaounion.model.domain.HomePagerContent;
import com.zxyapp.taobaounion.model.domain.IBaseInfo;
import com.zxyapp.taobaounion.utils.LogUtils;
import com.zxyapp.taobaounion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

public class LooperPagerAdapter extends PagerAdapter {

  private List<HomePagerContent.DataDTO> mData=new ArrayList<> ();
  private OnLooperPageItemClickListener mItemClickListener=null;

  @NonNull
  @Override
  public Object instantiateItem(@NonNull ViewGroup container, int position) {
    //处理越界问题
    int realPosition=position%mData.size ();
    HomePagerContent.DataDTO dataBean = mData.get (realPosition);

    int measuredWidth = container.getMeasuredWidth();
    int measuredHeight = container.getMeasuredHeight();
//    LogUtils.d(this,"measuredWidth------>"+measuredWidth);
//    LogUtils.d(this,"measuredHeight------>"+measuredHeight);
    int ivSize=(measuredWidth>measuredHeight?measuredWidth:measuredHeight)/2;
    String coverUrl= UrlUtils.getCoverPath (dataBean.getPictUrl (),ivSize);

    ImageView iv=new ImageView (container.getContext ());
    ViewGroup.LayoutParams layoutParams=new ViewGroup.LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
    iv.setLayoutParams (layoutParams);
    iv.setScaleType (ImageView.ScaleType.CENTER_CROP);
    Glide.with (container.getContext ()).load (coverUrl).into (iv);
    iv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (mItemClickListener != null) {
          HomePagerContent.DataDTO item = mData.get(realPosition);
          mItemClickListener.onLooperItemClick(item);
        }
      }
    });
    container.addView (iv);
    return iv;
  }


  public int getDataSize(){
    return mData.size ();
  }

  @Override
  public int getCount() {
    return Integer.MAX_VALUE;
  }

  @Override
  public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
    return view==object;
  }

  @Override
  public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    container.removeView ((View) object);
  }

  public void setData(List<HomePagerContent.DataDTO> contents) {
      mData.clear ();
      mData.addAll (contents);
      notifyDataSetChanged ();
  }

  public void setOnLooperPageItemClickListener(OnLooperPageItemClickListener listener){
    this.mItemClickListener=listener;
  }

  public interface OnLooperPageItemClickListener{
    void onLooperItemClick(IBaseInfo item);
  }
}