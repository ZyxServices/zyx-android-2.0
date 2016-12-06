package com.tiyujia.homesport.common.homepage.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tiyujia.homesport.BaseFragment;
import com.tiyujia.homesport.R;

/**
 * Created by zzqybyb19860112 on 2016/11/14.1
 */

public class WholeSearchFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
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
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_search_all,null);
        return view;
    }

    @Override
    protected void initData() {
        srlRefreshSearchAll= (SwipeRefreshLayout) view.findViewById(R.id.srlRefreshSearchAll);
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
        setNetData();
    }
    private void setNetData() {
    }

    @Override
    public void onRefresh() {

    }
}
