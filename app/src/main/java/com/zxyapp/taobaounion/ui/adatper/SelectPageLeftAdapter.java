package com.zxyapp.taobaounion.ui.adatper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zxyapp.taobaounion.R;
import com.zxyapp.taobaounion.model.domain.SelectPageCategory;

import java.util.ArrayList;
import java.util.List;

public class SelectPageLeftAdapter extends RecyclerView.Adapter<SelectPageLeftAdapter.InnerHolder> {

    private List<SelectPageCategory.DataBean> mData = new ArrayList<>();

    private int mCurrentSelectPosition = 0;
    private OnLeftItemClickListener mItemClickListener = null;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_page_left, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        TextView itemTv = holder.itemView.findViewById(R.id.left_category_tv);
        if (mCurrentSelectPosition == position) {
            itemTv.setBackgroundColor(itemTv.getResources().getColor(R.color.colorEFEEEE, null));
        } else {
            itemTv.setBackgroundColor(itemTv.getResources().getColor(R.color.white, null));

        }

        SelectPageCategory.DataBean dataBean = mData.get(position);
        itemTv.setText(dataBean.getFavorites_title());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null&&mCurrentSelectPosition != position) {
                        //修改当前选中的位置
                        mCurrentSelectPosition = position;
                        mItemClickListener.onLeftItemClick(dataBean);
                        notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * 设置数据
     *
     * @param categories
     */
    public void setData(SelectPageCategory categories) {
        List<SelectPageCategory.DataBean> data = categories.getData();
        if (data != null) {
            this.mData.clear();
            this.mData.addAll(data);
            notifyDataSetChanged();
        }
        if (mData.size()>0){
            mItemClickListener.onLeftItemClick(mData.get(mCurrentSelectPosition));
        }
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void setOnLeftClickListener(OnLeftItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public interface OnLeftItemClickListener {
        void onLeftItemClick(SelectPageCategory.DataBean item);
    }
}
