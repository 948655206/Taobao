package com.zxyapp.taobaounion.ui.adatper;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.zxyapp.taobaounion.R;
import com.zxyapp.taobaounion.model.domain.IBaseInfo;
import com.zxyapp.taobaounion.model.domain.OnSellContent;
import com.zxyapp.taobaounion.utils.LogUtils;
import com.zxyapp.taobaounion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.http.Url;

public class OnSellContentAdapter extends RecyclerView.Adapter<OnSellContentAdapter.InnerAdapter> {
    private List<OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> mData=new ArrayList<>();
    private onSellPageItemClickListener mContentItemClickListener=null;

    @NonNull
    @Override
    public InnerAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_on_sell_content, parent, false);
        return new InnerAdapter(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerAdapter holder, int position) {
        //TODO:绑定数据
        OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean mapDataBean = mData.get(position);
        holder.setData(mapDataBean);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContentItemClickListener!=null){
                    mContentItemClickListener.onSellItemClick(mapDataBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        LogUtils.d(this,"size123----->"+mData.size());
        return mData.size();
    }

    public void setData(OnSellContent result) {
        this.mData.clear();
        this.mData.addAll(result.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data());
        LogUtils.d(this,"size456----->"+mData.size());
        notifyDataSetChanged();
    }

    public void onMoreLoaded(OnSellContent moreResult) {
        List<OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> moreData = moreResult.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data();
        //原数据长度
        int oldDataSize = this.mData.size();
        this.mData.addAll(moreData);
        notifyItemRangeChanged(oldDataSize-1,moreData.size());
    }

    public class InnerAdapter extends RecyclerView.ViewHolder {
        @BindView(R.id.on_sell_cover)
        public ImageView cover;

        @BindView(R.id.on_sell_content_title_tv)
        public TextView titleTv;

          @BindView(R.id.on_sell_origin_prise_tv)
        public TextView originPriseTv;

          @BindView(R.id.on_sell_off_prise_tv)
        public TextView offPriseTv;

        public InnerAdapter(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        public void setData(OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean data){
            titleTv.setText(data.getTitle());
//            LogUtils.d(this,"pic url---->"+data.getPict_url());
            String coverPath = UrlUtils.getCoverPath(data.getPict_url());
            Glide.with(cover.getContext()).load(coverPath).into(cover);
            String originPrise = data.getZk_final_price();
            originPriseTv.setText("￥"+originPrise+" ");
            originPriseTv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            int coupon_amount = data.getCoupon_amount();
            float originPriseFloat=Float.parseFloat(originPrise);
            float finalPrise = originPriseFloat - coupon_amount;
            offPriseTv.setText(String.format("券后价：%.2f",finalPrise));
        }
    }

    public void setonSellPageItemClickListener(onSellPageItemClickListener listener){
        this.mContentItemClickListener=listener;
    }


    public interface onSellPageItemClickListener{
        void onSellItemClick(IBaseInfo data);
    }
}
