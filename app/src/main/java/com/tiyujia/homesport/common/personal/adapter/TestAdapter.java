package com.tiyujia.homesport.common.personal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tiyujia.homesport.R;
import com.tiyujia.homesport.entity.ActiveModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者: Cymbi on 2016/11/14 15:37.
 * 邮箱:928902646@qq.com
 */

public class TestAdapter extends RecyclerView.Adapter {
    Context context;
    List<ActiveModel> mDatas;

    public TestAdapter(Context context, List<ActiveModel> mDatas) {
        if(mDatas.size()!=0){
            this.mDatas = mDatas;
        }else {
            this.mDatas=new ArrayList<>();
        }
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attend_fragment_item, null);
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
        @Bind(R.id.iv_background) ImageView iv_background;
        @Bind(R.id.tv_time) TextView tv_time;
        @Bind(R.id.tv_apply_lable) TextView tv_apply_lable;
        @Bind(R.id.tv_active_lable) TextView tv_active_lable;
        @Bind(R.id.tv_title) TextView tv_title;
        @Bind(R.id.tv_award) TextView tv_award;
        @Bind(R.id.tv_address) TextView tv_address;
        @Bind(R.id.tv_msg) TextView tv_msg;
        @Bind(R.id.tv_zan) TextView tv_zan;
        @Bind(R.id.tv_apply) TextView tv_apply;
        public myholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
