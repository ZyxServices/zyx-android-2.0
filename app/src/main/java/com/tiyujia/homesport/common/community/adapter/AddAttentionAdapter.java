package com.tiyujia.homesport.common.community.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tiyujia.homesport.R;
import com.tiyujia.homesport.entity.ActiveModel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者: Cymbi on 2016/11/15 11:58.1
 * 邮箱:928902646@qq.com
 */

public class AddAttentionAdapter extends  RecyclerView.Adapter {
    Context context;
    List<ActiveModel> mDatas;

    public AddAttentionAdapter(Context context, List<ActiveModel> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.personal_attention_item, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new myholder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
    public class myholder extends RecyclerView.ViewHolder{
        @Bind(R.id.tv_not) TextView tv_not;
        public myholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            tv_not.setVisibility(View.VISIBLE);
        }
    }
}
