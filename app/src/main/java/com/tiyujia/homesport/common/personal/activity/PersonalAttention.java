package com.tiyujia.homesport.common.personal.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.personal.adapter.AttentionAdapter;
import com.tiyujia.homesport.entity.ActiveModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者: Cymbi on 2016/11/15 11:44.
 * 邮箱:928902646@qq.com
 */

public class PersonalAttention extends ImmersiveActivity implements View.OnClickListener{
    @Bind(R.id.ivBack)ImageView personal_back;
    @Bind(R.id.ivSearch) ImageView iv_search;
    @Bind(R.id.srlRefresh)SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.recyclerView)RecyclerView recycle;
    @Bind(R.id.tvTitle)TextView tv_title;
    private ArrayList<ActiveModel> mDatas;
    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attention);
        ButterKnife.bind(this);
        initdata();
        personal_back.setOnClickListener(this);
        iv_search.setOnClickListener(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recycle.setLayoutManager(layoutManager);
        recycle.setAdapter(new AttentionAdapter(this,mDatas));

    }
    private void initdata() {
        mDatas = new ArrayList<>();
        tv_title.setText("我的关注");
        for (int i = 0; i < 10; i++)
        {
            ActiveModel activeModel=  new ActiveModel();
            mDatas.add(activeModel);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.personal_back:
                finish();
                break;
            case R.id.ivSearch:

                break;
        }
    }
}
