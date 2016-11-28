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
import com.tiyujia.homesport.common.homepage.activity.HomePageVenueSurveyActivity;
import com.tiyujia.homesport.common.homepage.adapter.HomePageRecentVenueAdapter;
import com.tiyujia.homesport.common.homepage.entity.HomePageBannerEntity;
import com.tiyujia.homesport.common.homepage.entity.HomePageRecentVenueEntity;
import com.tiyujia.homesport.util.RefreshUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zzqybyb19860112 on 2016/11/14.1
 */

public class AllVenueFragment extends BaseFragment implements  SwipeRefreshLayout.OnRefreshListener{
    private View view;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout srlRefresh;
    public static HomePageRecentVenueAdapter adapter;
    List<HomePageRecentVenueEntity> datas;
    int [] picAddress=new int[]{R.drawable.demo_05,R.drawable.demo_06,R.drawable.demo_09,R.drawable.demo_10};
    public static final int HANDLE_DATA=1;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HANDLE_DATA:
                    adapter=new HomePageRecentVenueAdapter(getActivity(),datas);
                    adapter.setFriends(datas);
                    adapter.getFilter().filter(HomePageVenueSurveyActivity.getSearchText());
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(layoutManager);
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
        datas=new ArrayList<>();
        String [] names={"成都体育馆","达州体育馆","北京体育馆","深圳体育馆","上海体育馆","天津体育馆","杭州体育馆","伦敦体育馆"};
        String []types={"室内","室外"};
        for (int i=0;i<30;i++){
            HomePageRecentVenueEntity entity=new HomePageRecentVenueEntity();
            entity.setBigPicUrl(picAddress[new Random().nextInt(4)]+"");
            entity.setVenueName(names[new Random().nextInt(8)]);
            entity.setDegreeNumber(new Random().nextInt(5)+1);
            entity.setNumberGone(new Random().nextInt(1200)+1);
            entity.setNumberTalk(new Random().nextInt(3200)+1);
            List<String> typeList=new ArrayList<>();
            if (i%2==0){
                typeList.add(types[0]);
            }else {
                typeList.add(types[1]);
            }
            typeList.add("抱石");
            entity.setVenueType(typeList);
            datas.add(entity);
        }
        handler.sendEmptyMessage(HANDLE_DATA);
    }

    @Override
    public void onRefresh() {
        setData();
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                // 停止刷新
                srlRefresh.setRefreshing(false);
            }
        }, 1000);
    }
}
