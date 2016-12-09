package com.tiyujia.homesport.common.homepage.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.App;
import com.tiyujia.homesport.BaseFragment;
import com.tiyujia.homesport.BootLoaderActivity;
import com.tiyujia.homesport.HomeActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.activity.HomePageVenueSurveyActivity;
import com.tiyujia.homesport.common.homepage.adapter.HomePageRecentVenueAdapter;
import com.tiyujia.homesport.common.homepage.entity.HomePageRecentVenueEntity;
import com.tiyujia.homesport.util.CacheUtils;
import com.tiyujia.homesport.util.JSONParseUtil;
import com.tiyujia.homesport.util.PostUtil;
import com.tiyujia.homesport.util.RefreshUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zzqybyb19860112 on 2016/11/14.1
 */

    public class NearVenueFragment extends BaseFragment implements  SwipeRefreshLayout.OnRefreshListener{
        private View view;
        private RecyclerView recyclerView;
        private SwipeRefreshLayout srlRefresh;
        public static HomePageRecentVenueAdapter adapter;
        List<HomePageRecentVenueEntity> datas=new ArrayList<>();
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
            AMapLocationClient client = BootLoaderActivity.client;
            AMapLocation location = client.getLastKnownLocation();
            final double latitude= location.getLatitude();//纬度
            final double longitude=location.getLongitude();//经度
            ArrayList<String> cacheData= (ArrayList<String>) CacheUtils.readJson(getActivity(), NearVenueFragment.this.getClass().getName() + ".json");
            if (cacheData==null||cacheData.size()==0) {
                new Thread() {
                    @Override
                    public void run() {
                        String uri = API.BASE_URL + "/v2/venue/findVenue";
                        HashMap<String, String> params = new HashMap<>();
                        params.put("type", "2");
                        params.put("lng", longitude + "");
                        params.put("lat", latitude + "");
                        params.put("number", "10");
                        params.put("pageNumber", "1");
                        String result = PostUtil.sendPostMessage(uri, params);
                        JSONParseUtil.parseNetDataVenue(getActivity(),result, NearVenueFragment.this.getClass().getName()+".json",datas, handler, HANDLE_DATA);
                    }
                }.start();
            }else {
                JSONParseUtil.parseLocalDataVenue(getActivity(), NearVenueFragment.this.getClass().getName()+".json",datas, handler, HANDLE_DATA);
            }
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
