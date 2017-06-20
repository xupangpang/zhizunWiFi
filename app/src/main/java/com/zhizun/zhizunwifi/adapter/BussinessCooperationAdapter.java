package com.zhizun.zhizunwifi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhizun.zhizunwifi.R;
import com.zhizun.zhizunwifi.bean.BussCooper;

import java.util.List;

/**
 * @author xupp
 * @date 2017/5/9
 */

public class BussinessCooperationAdapter extends RecyclerView.Adapter<BussinessCooperationAdapter.ViewHolder>{
    private List<BussCooper>  mData;
    private Context mContext;
    private MyItemClickListener mItemClickListener;

    public BussinessCooperationAdapter(Context context) {
        this.mContext = context;
    }

    public void  setList(List<BussCooper> list){
        this.mData = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.buss_cooper_item, parent, false);
        return new ViewHolder(view,mItemClickListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.buss_ssid.setText(mData.get(position).getSsid());
        if (mData.get(position).getStatus().equals("审核成功")){
            holder.buss_store_type.setText(mData.get(position).getBusiness().name+" • "+mData.get(position).getBusiness().type);
        }else {
            holder.buss_store_type.setText(mData.get(position).getStatus());
        }

    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        private MyItemClickListener mListener;
        TextView buss_ssid;
        TextView buss_store_type;
        public ViewHolder(View itemView,MyItemClickListener myItemClickListener) {
            super(itemView);
            buss_ssid = (TextView) itemView.findViewById(R.id.buss_ssid);
            buss_store_type = (TextView)itemView.findViewById(R.id.buss_store_type);
            this.mListener = myItemClickListener;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onItemClick(view, getPosition());
                    }
                }
            });
        }
    }

    /**
     * 创建一个回调接口
     */
    public interface MyItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * 在activity里面adapter就是调用的这个方法,将点击事件监听传递过来,并赋值给全局的监听
     *
     * @param myItemClickListener
     */
    public void setItemClickListener(MyItemClickListener myItemClickListener) {
        this.mItemClickListener = myItemClickListener;
    }
}
