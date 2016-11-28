package com.tiyujia.homesport.common.homepage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.entity.HomePageSearchEntity;
import com.tiyujia.homesport.common.homepage.entity.WholeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzqybyb19860112 on 2016/11/11.1
 */

public class WholeSearchRecordAdapter extends RecyclerView.Adapter  {
    Context context;
    List<WholeBean> values;
    OnItemClickListener listener;
    public WholeSearchRecordAdapter(Context context, List<WholeBean> values) {
        if (values.size()!=0){
            this.values = values;
        }else {
            this.values=new ArrayList<>();
        }
        this.context = context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_search_record, null);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        viewItem.setLayoutParams(lp1);
        return new WholeSearchHolder(viewItem);
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof WholeSearchHolder) {
            final WholeSearchHolder holder = (WholeSearchHolder) viewHolder;
            WholeBean data = values.get(position);
            holder.tvSearchText.setText(data.getText());
            if (listener!=null){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    listener.onItemClick(holder.tvSearchText.getText().toString());
                    }
                });
            }
        }
    }
    @Override
    public int getItemCount() {
        return values.size() ;
    }
    public interface OnItemClickListener{
        void onItemClick(String searchText);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    class WholeSearchHolder extends RecyclerView.ViewHolder{
        TextView tvSearchText;
        public WholeSearchHolder(View itemView) {
            super(itemView);
            tvSearchText= (TextView) itemView.findViewById(R.id.tvSearchText);
        }
    }
}
