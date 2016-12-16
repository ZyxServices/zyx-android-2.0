package com.tiyujia.homesport.common.personal.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.personal.adapter.FansAdapter;
import com.tiyujia.homesport.common.personal.model.AttentionModel;
import com.tiyujia.homesport.entity.JsonCallback;
import com.tiyujia.homesport.util.RefreshUtil;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者: Cymbi on 2016/11/15 11:50.
 * 邮箱:928902646@qq.com
 */

public class PersonalFans extends ImmersiveActivity implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener{
    @Bind(R.id.ivBack)ImageView ivBack;
    @Bind(R.id.ivSearch) ImageView iv_search;
    @Bind(R.id.srlRefresh)SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.recyclerView)RecyclerView recyclerView;
    @Bind(R.id.tvTitle)TextView tv_title;
    private FansAdapter adapter;
    private String mToken;
    private int mUserId;

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attention);
        setInfo();
        ivBack.setOnClickListener(this);
        iv_search.setOnClickListener(this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new FansAdapter(this,null);
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        adapter.isFirstOnly(false);
        recyclerView.setAdapter(adapter);
        RefreshUtil.refresh(swipeRefresh,this);
        swipeRefresh.setOnRefreshListener(this);
        onRefresh();
    }

    private void setInfo() {
        SharedPreferences share = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        mToken=share.getString("Token","");
        mUserId=share.getInt("UserId",0);
        tv_title.setText("我的粉丝");
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivSearch:

                break;
        }
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        OkGo.get(API.BASE_URL+"/v2/my/attention/to")
                .tag(this)
                .params("token",mToken)
                .params("accountId",mUserId)
                .execute(new JsonCallback<AttentionModel>() {
                    @Override
                    public void onSuccess(AttentionModel attentionModel, Call call, Response response) {
                        if(attentionModel.state==200){
                            if (attentionModel.data.size()==0){
                            adapter.setNewData(attentionModel.data);}
                        }
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Log.i("onError",e.getMessage());
                    }

                    @Override
                    public void onAfter(@Nullable AttentionModel attentionModel, @Nullable Exception e) {
                        super.onAfter(attentionModel, e);
                        adapter.removeAllFooterView();
                        setRefreshing(false);
                    }
                });
    }
    public void setRefreshing(final boolean refreshing) {
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(refreshing);
            }
        });
    }
}
