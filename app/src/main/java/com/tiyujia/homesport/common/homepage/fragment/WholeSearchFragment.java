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
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tiyujia.homesport.API;
import com.tiyujia.homesport.BaseFragment;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.activity.HomePageWholeSearchActivity;
import com.tiyujia.homesport.common.homepage.adapter.SearchActiveAdapter;
import com.tiyujia.homesport.common.homepage.adapter.SearchCourseAdapter;
import com.tiyujia.homesport.common.homepage.adapter.SearchDynamicAdapter;
import com.tiyujia.homesport.common.homepage.adapter.SearchEquipAdapter;
import com.tiyujia.homesport.common.homepage.adapter.SearchUserAdapter;
import com.tiyujia.homesport.common.homepage.adapter.SearchVenueAdapter;
import com.tiyujia.homesport.common.homepage.entity.WholeSearchEntity;
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

public class WholeSearchFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,View.OnClickListener{
    View view;
    SwipeRefreshLayout srlRefreshSearchAll;
    RecyclerView rvSearchActive;
    TextView tvMoreActive;
    RecyclerView rvSearchEquip;
    TextView tvMoreEquip;
    RecyclerView rvSearchDynamic;
    TextView tvMoreDynamic;
    RecyclerView rvSearchCourse;
    TextView tvMoreCourse;
    RecyclerView rvSearchVenue;
    TextView tvMoreVenue;
    RecyclerView rvSearchUser;
    TextView tvMoreUser;
    WholeSearchEntity wholeSearchEntity;
    public static List<RecyclerView.Adapter> adapterList=new ArrayList<>();
    private static final int HANDLE_WHOLE_DATA=1;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HANDLE_WHOLE_DATA:
                    wholeSearchEntity= (WholeSearchEntity) msg.obj;
                    setViews(wholeSearchEntity);
                    srlRefreshSearchAll.setRefreshing(false);
                    break;
            }
        }
    };
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_search_all,null);
        return view;
    }
    private void setViews(WholeSearchEntity entity){
        //教程
        SearchCourseAdapter courseAdapter=new SearchCourseAdapter(getActivity(),entity.getCourseList());
        courseAdapter.setFriends(entity.getCourseList());
        courseAdapter.getFilter().filter(HomePageWholeSearchActivity.getSearchText());
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        rvSearchCourse.setLayoutManager(layoutManager1);
        rvSearchCourse.setAdapter(courseAdapter);
        adapterList.add(courseAdapter);
        //活动
        SearchActiveAdapter activeAdapter=new SearchActiveAdapter(getActivity(),entity.getActiveList());
        activeAdapter.setFriends(entity.getActiveList());
        activeAdapter.getFilter().filter(HomePageWholeSearchActivity.getSearchText());
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        rvSearchActive.setLayoutManager(layoutManager2);
        rvSearchActive.setAdapter(activeAdapter);
        adapterList.add(activeAdapter);
        //动态
        SearchDynamicAdapter dynamicAdapter=new SearchDynamicAdapter(getActivity(),entity.getDynamicList());
        dynamicAdapter.setFriends(entity.getDynamicList());
        dynamicAdapter.getFilter().filter(HomePageWholeSearchActivity.getSearchText());
        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(getActivity());
        rvSearchDynamic.setLayoutManager(layoutManager3);
        rvSearchDynamic.setAdapter(dynamicAdapter);
        adapterList.add(dynamicAdapter);
        //装备
        SearchEquipAdapter equipAdapter=new SearchEquipAdapter(getActivity(),entity.getEquipList());
        equipAdapter.setFriends(entity.getEquipList());
        equipAdapter.getFilter().filter(HomePageWholeSearchActivity.getSearchText());
        RecyclerView.LayoutManager layoutManager4 = new LinearLayoutManager(getActivity());
        rvSearchEquip.setLayoutManager(layoutManager4);
        rvSearchEquip.setAdapter(equipAdapter);
        adapterList.add(equipAdapter);
        //场馆
        SearchVenueAdapter venueAdapter=new SearchVenueAdapter(getActivity(),entity.getVenueList());
        venueAdapter.setFriends(entity.getVenueList());
        venueAdapter.getFilter().filter(HomePageWholeSearchActivity.getSearchText());
        RecyclerView.LayoutManager layoutManager5 = new LinearLayoutManager(getActivity());
        rvSearchVenue.setLayoutManager(layoutManager5);
        rvSearchVenue.setAdapter(venueAdapter);
        adapterList.add(venueAdapter);
        //用户
        SearchUserAdapter userAdapter=new SearchUserAdapter(getActivity(),entity.getUserList());
        userAdapter.setFriends(entity.getUserList());
        userAdapter.getFilter().filter(HomePageWholeSearchActivity.getSearchText());
        RecyclerView.LayoutManager layoutManager6 = new LinearLayoutManager(getActivity());
        rvSearchUser.setLayoutManager(layoutManager6);
        rvSearchUser.setAdapter(userAdapter);
        adapterList.add(userAdapter);
        tvMoreActive.setOnClickListener(this);
        tvMoreEquip.setOnClickListener(this);
        tvMoreDynamic.setOnClickListener(this);
        tvMoreVenue.setOnClickListener(this);
        tvMoreCourse.setOnClickListener(this);
        tvMoreUser.setOnClickListener(this);
    }
    @Override
    protected void initData() {
        srlRefreshSearchAll= (SwipeRefreshLayout) view.findViewById(R.id.srlRefreshSearchAll);
        RefreshUtil.refresh(srlRefreshSearchAll, getActivity());
        srlRefreshSearchAll.setOnRefreshListener(this);
        rvSearchActive= (RecyclerView) view.findViewById(R.id.rvSearchActive);
        tvMoreActive= (TextView) view.findViewById(R.id.tvMoreActive);
        rvSearchEquip= (RecyclerView) view.findViewById(R.id.rvSearchEquip);
        tvMoreEquip= (TextView) view.findViewById(R.id.tvMoreEquip);
        rvSearchDynamic= (RecyclerView) view.findViewById(R.id.rvSearchDynamic);
        tvMoreDynamic= (TextView) view.findViewById(R.id.tvMoreDynamic);
        rvSearchCourse= (RecyclerView) view.findViewById(R.id.rvSearchCourse);
        tvMoreCourse= (TextView) view.findViewById(R.id.tvMoreCourse);
        rvSearchVenue= (RecyclerView) view.findViewById(R.id.rvSearchVenue);
        tvMoreVenue= (TextView) view.findViewById(R.id.tvMoreVenue);
        rvSearchUser= (RecyclerView) view.findViewById(R.id.rvSearchUser);
        tvMoreUser= (TextView) view.findViewById(R.id.tvMoreUser);
        setNetData();
    }
    private void setNetData() {
        wholeSearchEntity=new WholeSearchEntity();
        ArrayList<String> cacheData= (ArrayList<String>) CacheUtils.readJson(getActivity(), WholeSearchFragment.this.getClass().getName() + ".json");
        if (cacheData==null||cacheData.size()==0) {
            new Thread() {
                @Override
                public void run() {
                    String uri = API.BASE_URL + "/v2/search/modelAll";
                    HashMap<String, String> params = new HashMap<>();
                    params.put("number", "3");
                    params.put("pageNumber", "1");
                    String result = PostUtil.sendPostMessage(uri, params);
                    JSONParseUtil.parseNetDataSearchAll(getActivity(),result, WholeSearchFragment.this.getClass().getName()+".json",wholeSearchEntity, handler, HANDLE_WHOLE_DATA);
                }
            }.start();
        }else {
            JSONParseUtil.parseLocalDataSearchAll(getActivity(), WholeSearchFragment.this.getClass().getName()+".json",wholeSearchEntity, handler, HANDLE_WHOLE_DATA);
        }
    }
    @Override
    public void onRefresh() {
        setNetData();
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                // 停止刷新
                srlRefreshSearchAll.setRefreshing(false);
            }
        }, 1000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvMoreActive:
                HomePageWholeSearchActivity.vp.setCurrentItem(1);
                break;
            case R.id.tvMoreEquip:
                HomePageWholeSearchActivity.vp.setCurrentItem(2);
                break;
            case R.id.tvMoreDynamic:
                HomePageWholeSearchActivity.vp.setCurrentItem(3);
                break;
            case R.id.tvMoreCourse:
                HomePageWholeSearchActivity.vp.setCurrentItem(5);
                break;
            case R.id.tvMoreUser:
                HomePageWholeSearchActivity.vp.setCurrentItem(6);
                break;
            case R.id.tvMoreVenue:
                HomePageWholeSearchActivity.vp.setCurrentItem(4);
                break;
        }
    }
}
