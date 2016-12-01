package com.tiyujia.homesport.common.personal.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.personal.adapter.DynamicAdapter;
import com.tiyujia.homesport.entity.ActiveModel;
import com.tiyujia.homesport.util.RefreshUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者: Cymbi on 2016/11/14 17:25.
 * 邮箱:928902646@qq.com
 */

public class PersonalDynamic extends ImmersiveActivity implements SwipeRefreshLayout.OnRefreshListener{
    @Bind(R.id.personal_back) ImageView personal_back;
    @Bind(R.id.srlRefresh)  SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.recyclerView)  RecyclerView recyclerView;
    @Bind(R.id.tv_title)    TextView tv_title;
    DynamicAdapter adapter;
    private String mToken;
    private int mUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_dynamic);
        ButterKnife.bind(this);
        setInfo();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter= new DynamicAdapter(this,null);
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        adapter.isFirstOnly(false);
        recyclerView.setAdapter(adapter);
        RefreshUtil.refresh(swipeRefresh,this);
        swipeRefresh.setOnRefreshListener(this);
        onRefresh();
    }

    private void setInfo() {
        SharedPreferences share = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        mToken=share.getString("Token","");
        mUserId=share.getInt("UserId",0);
        tv_title.setText("我的动态");
        personal_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @Override
    public void onRefresh() {

    }
    public void setRefreshing(final boolean refreshing) {
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(refreshing);
            }
        });
    }
}
