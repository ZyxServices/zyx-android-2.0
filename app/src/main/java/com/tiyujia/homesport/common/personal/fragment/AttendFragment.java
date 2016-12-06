package com.tiyujia.homesport.common.personal.fragment;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.tiyujia.homesport.common.personal.adapter.AttendAdapter;
import com.tiyujia.homesport.common.personal.adapter.FansAdapter;
import com.tiyujia.homesport.common.personal.model.ActiveModel;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.util.RefreshUtil;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者: Cymbi on 2016/10/20 16:51.
 * 邮箱:928902646@qq.com
 */

public class AttendFragment extends BaseFragment implements  SwipeRefreshLayout.OnRefreshListener,BaseQuickAdapter.RequestLoadMoreListener{
    private View view;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout srlRefresh;
    private AttendAdapter adapter;
    private String mToken;
    private int mUserId;
    private int page=1;
    private int pageSize=10;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.recycleview_layout,null);
        return view;
    }
    @Override
    protected void initData() {
        setInfo();
        recyclerView= (RecyclerView)view.findViewById(R.id.recyclerView);
        srlRefresh= (SwipeRefreshLayout)view.findViewById(R.id.srlRefresh);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter=new AttendAdapter(getActivity(),null);
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        adapter.isFirstOnly(false);
        recyclerView.setAdapter(adapter);
        RefreshUtil.refresh(srlRefresh,getActivity());
        srlRefresh.setOnRefreshListener(this);
        //adapter.setOnLoadMoreListener(this);
        onRefresh();
    }
    private void setInfo() {
        SharedPreferences share = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        mToken=share.getString("Token","");
        mUserId=share.getInt("UserId",0);
    }

    @Override
    public void onRefresh() {
        OkGo.get(API.BASE_URL+"/v2/my/activity/create/list")
                .tag(this)
                .params("token",mToken)
                .params("accountId",mUserId)
                .params("page",page)
                .params("pageSize",pageSize)
                .execute(new LoadCallback<ActiveModel>(getActivity()) {
                    @Override
                    public void onSuccess(ActiveModel activeModel, Call call, Response response) {
                        if(activeModel.state==200){adapter.setNewData(activeModel.data);}
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        showToast("网络连接错误");
                    }
                    @Override
                    public void onAfter(@Nullable ActiveModel activeModel, @Nullable Exception e) {
                        super.onAfter(activeModel, e);
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

    @Override
    public void onLoadMoreRequested() {
        OkGo.get(API.BASE_URL+"/v2/my/activity/create/list")
                .tag(this)
                .params("token",mToken)
                .params("accountId",mUserId)
                .params("page",page+1)
                .params("pageSize",pageSize)
                .execute(new LoadCallback<ActiveModel>(getActivity()) {
                    @Override
                    public void onSuccess(ActiveModel activeModel, Call call, Response response) {
                        if(activeModel.state==200){adapter.setNewData(activeModel.data);}
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        showToast("网络连接错误");
                    }
                    @Override
                    public void onAfter(@Nullable ActiveModel activeModel, @Nullable Exception e) {
                        super.onAfter(activeModel, e);
                        adapter.removeAllFooterView();
                        setRefreshing(false);
                    }
                });
    }
}
