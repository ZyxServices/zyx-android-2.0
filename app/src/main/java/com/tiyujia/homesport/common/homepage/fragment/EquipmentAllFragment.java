package com.tiyujia.homesport.common.homepage.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tiyujia.homesport.BaseFragment;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.adapter.HomePageEquipmentAdpter;
import com.tiyujia.homesport.common.personal.adapter.TestAdapter;
import com.tiyujia.homesport.entity.ActiveModel;
import com.tiyujia.homesport.util.RefreshUtil;

import java.util.ArrayList;

/**
 * 作者: Cymbi on 2016/11/17 17:50.1
 * 邮箱:928902646@qq.com
 */

public class EquipmentAllFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
    private View view;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout srlRefresh;
    private ArrayList<ActiveModel> mDatas;
    public static final int HANDLE_DATA=1;
    public HomePageEquipmentAdpter adapter;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HANDLE_DATA:
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(layoutManager);
                    adapter=new HomePageEquipmentAdpter(getActivity(),mDatas);
//                    adapter.setFriends(mDatas);
//                    adapter.getFilter().filter(HomePageVenueSurveyActivity.getSearchText());
                    recyclerView.setAdapter(adapter);
                    srlRefresh.setRefreshing(false);
                    break;
            }
        }
    };

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.recycleview_layout,null);
        return view;
    }

    @Override
    protected void initData() {
        setData();
        recyclerView= (RecyclerView)view.findViewById(R.id.recyclerView);
        srlRefresh= (SwipeRefreshLayout)view.findViewById(R.id.srlRefresh);
        RefreshUtil.refresh(srlRefresh, getActivity());
        srlRefresh.setOnRefreshListener(this);
    }

    private void setData() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 10; i++)
        {
            ActiveModel activeModel=  new ActiveModel();
            mDatas.add(activeModel);
        }
        handler.sendEmptyMessage(HANDLE_DATA);
    }

    @Override
    public void onRefresh() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 20; i++)
        {
            ActiveModel activeModel=  new ActiveModel();
            mDatas.add(activeModel);
        }
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                // 停止刷新
                srlRefresh.setRefreshing(false);
            }
        }, 1000);
    }
}
