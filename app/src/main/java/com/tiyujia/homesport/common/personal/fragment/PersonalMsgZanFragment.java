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
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.BaseFragment;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.personal.adapter.MsgAdapter;
import com.tiyujia.homesport.common.personal.model.MsgModel;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.util.RefreshUtil;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者: Cymbi on 2016/11/16 10:53.
 * 邮箱:928902646@qq.com
 */

public class PersonalMsgZanFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
    private View view;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout srlRefresh;
    private String mToken;
    private int mUserId;
    private int msg_type=3;
    private MsgAdapter adapter;

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
        adapter=new MsgAdapter(null);
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        adapter.isFirstOnly(false);
        recyclerView.setAdapter(adapter);
        RefreshUtil.refresh(srlRefresh,getActivity());
        srlRefresh.setOnRefreshListener(this);
        onRefresh();
    }
    private void setInfo() {
        SharedPreferences share = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        mToken=share.getString("Token","");
        mUserId=share.getInt("UserId",0);
    }
    @Override
    public void onRefresh() {
        OkGo.get(API.BASE_URL+"/v2/msg/list")
                .tag(this)
                .params("token",mToken)
                .params("user_id",mUserId)
                .params("msg_type",msg_type)
                .execute(new LoadCallback<MsgModel>(getActivity()) {
                    @Override
                    public void onSuccess(MsgModel msgModel, Call call, Response response) {
                        if (msgModel.state==200){adapter.setNewData(msgModel.data);}
                        if(msgModel.state==401){showToast("token失效");}
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        showToast("网络连接错误");
                    }

                    @Override
                    public void onAfter(@Nullable MsgModel msgModel, @Nullable Exception e) {
                        super.onAfter(msgModel, e);
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
