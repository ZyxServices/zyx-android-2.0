package com.tiyujia.homesport.common.homepage.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.BaseFragment;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.adapter.CourseAdapter;
import com.tiyujia.homesport.common.homepage.adapter.HomePageEquipmentAdpter;
import com.tiyujia.homesport.common.homepage.entity.EquipmentModel;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.util.RefreshUtil;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者: Cymbi on 2016/12/6 14:12.
 * 邮箱:928902646@qq.com
 */

public class EquipmentAllFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private View view;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout srlRefresh;
    public HomePageEquipmentAdpter adapter;
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.recycleview_layout,null);
        return view;
    }
    @Override
    protected void initData() {
        recyclerView= (RecyclerView)view.findViewById(R.id.recyclerView);
        srlRefresh= (SwipeRefreshLayout)view.findViewById(R.id.srlRefresh);
        adapter=new HomePageEquipmentAdpter(getActivity(),null);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        adapter.isFirstOnly(false);
        recyclerView.setAdapter(adapter);
        RefreshUtil.refresh(srlRefresh,getActivity());
        srlRefresh.setOnRefreshListener(this);
        onRefresh();
    }
    @Override
    public void onRefresh() {
        OkGo.post(API.BASE_URL+"/v2/equip/query")
                .tag(this)
                .params("id",0)
                .cacheMode(CacheMode.IF_NONE_CACHE_REQUEST)
                .execute(new LoadCallback<EquipmentModel>(getActivity()) {
                    @Override
                    public void onSuccess(EquipmentModel equipmentModel, Call call, Response response) {
                        if(equipmentModel.state==200){adapter.setNewData(equipmentModel.data);}
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        showToast("网络连接错误");
                    }
                    @Override
                    public void onAfter(@Nullable EquipmentModel equipmentModel, @Nullable Exception e) {
                        super.onAfter(equipmentModel, e);
                        adapter.removeAllFooterView();
                        setRefreshing(false);
                    }
                });
    }
    public void setRefreshing(final boolean refreshing) {
        srlRefresh.post(new Runnable() {
            @Override
            public void run() {
                srlRefresh.setRefreshing(refreshing);
            }
        });
    }
}
