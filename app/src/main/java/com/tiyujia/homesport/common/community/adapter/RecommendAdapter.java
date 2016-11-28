package com.tiyujia.homesport.common.community.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.ninegrid.NineGridView;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.entity.ActiveModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者: Cymbi on 2016/11/14 15:37.1
 * 邮箱:928902646@qq.com
 */

public class RecommendAdapter extends RecyclerView.Adapter {
    Context context;
    List<ActiveModel> mDatas;

    public RecommendAdapter(Context context, List<ActiveModel> mDatas) {
        if(mDatas.size()!=0){
            this.mDatas = mDatas;
        }else {
            this.mDatas=new ArrayList<>();
        }
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.personal_dynamic_item, null);
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
        @Bind(R.id.me_head) ImageView me_head;
        @Bind(R.id.iv_lv) ImageView iv_lv;
        @Bind(R.id.tv_time) TextView tv_time;
        @Bind(R.id.tv_yes) TextView tv_yes;
        @Bind(R.id.desc) TextView desc;
        @Bind(R.id.tv_address) TextView tv_address;
        @Bind(R.id.tv_msg) TextView tv_msg;
        @Bind(R.id.tv_zan) TextView tv_zan;
        @Bind(R.id.nineGrid) NineGridView nineGrid;
        public myholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            tv_yes.setVisibility(View.VISIBLE);
        }
    }
}
