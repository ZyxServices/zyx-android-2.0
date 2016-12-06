package com.tiyujia.homesport.common.community.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.community.adapter.AddAttentionAdapter;
import com.tiyujia.homesport.common.personal.model.AttentionModel;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.util.RefreshUtil;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者: Cymbi on 2016/11/16 17:38.1
 * 邮箱:928902646@qq.com
 */

public class CommunityAddAttention extends ImmersiveActivity implements View.OnClickListener ,SwipeRefreshLayout.OnRefreshListener{

    @Bind(R.id.ivBack)ImageView personal_back;
    @Bind(R.id.ivSearch) ImageView iv_search;
    @Bind(R.id.srlRefresh)SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.recyclerView)RecyclerView recyclerView;
    @Bind(R.id.tvTitle)TextView tv_title;
    @Bind(R.id.tv_tuijian)TextView tv_tuijian;
    private AddAttentionAdapter adapter;
    private String mToken;
    private int mUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attention);
        setInfo();
        personal_back.setOnClickListener(this);
        iv_search.setOnClickListener(this);
        adapter =new AddAttentionAdapter(this,null);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
        tv_title.setText("添加关注");
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

    @Override
    public void onRefresh() {
        OkGo.post(API.BASE_URL+"/v2/follow/list")
                .tag(this)
                .params("token",mToken)
                .params("loginUserId",mUserId)
                .execute(new LoadCallback<AttentionModel>(this) {
                    @Override
                    public void onSuccess(AttentionModel attentionModel, Call call, Response response) {
                        if(attentionModel.state==200){
                            adapter.setNewData(attentionModel.data);
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
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
