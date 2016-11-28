package com.tiyujia.homesport.common.homepage.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.personal.adapter.TestAdapter;
import com.tiyujia.homesport.entity.ActiveModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者: Cymbi on 2016/11/17 15:03.1
 * 邮箱:928902646@qq.com
 */

public class HomePageDateActivity extends ImmersiveActivity implements View.OnClickListener ,SwipeRefreshLayout.OnRefreshListener{
    @Bind(R.id.ivMenu) ImageView ivMenu;
    @Bind(R.id.ivBack) ImageView ivBack;
    @Bind(R.id.tvTitle) TextView tvTitle;
    @Bind(R.id.cbDateBanner) ConvenientBanner cbDateBanner;
    @Bind(R.id.recyclerView) RecyclerView recyclerView;
    @Bind(R.id.srlRefresh)   SwipeRefreshLayout srlRefresh;
    private ArrayList<ActiveModel> mDatas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_date);
        ButterKnife.bind(this);
        setView();
        initData();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        //保证recycleView不卡顿
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.setAdapter(new TestAdapter(this,mDatas));
    }

    private void initData() {
            mDatas = new ArrayList<>();
            for (int i = 0; i < 10; i++)
            {
                ActiveModel activeModel=  new ActiveModel();
                mDatas.add(activeModel);
            }
    }

    private void setView() {
        ivMenu.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.ivBack:
                    finish();
                    break;
                case R.id.ivMenu:

                    break;
            }
    }

    @Override
    public void onRefresh() {

    }
}
