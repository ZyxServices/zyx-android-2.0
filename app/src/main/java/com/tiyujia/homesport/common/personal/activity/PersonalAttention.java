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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.personal.adapter.AttentionAdapter;
import com.tiyujia.homesport.common.personal.model.AttentionModel;
import com.tiyujia.homesport.entity.JsonCallback;
import com.tiyujia.homesport.util.RefreshUtil;
import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;
/**
 * 作者: Cymbi on 2016/11/15 11:44.
 * 邮箱:928902646@qq.com
 */

public class PersonalAttention extends ImmersiveActivity implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener{
    @Bind(R.id.ivBack)ImageView ivBack;
    @Bind(R.id.ivSearch) ImageView iv_search;
    @Bind(R.id.srlRefresh)SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.recyclerView)RecyclerView recyclerView;
    @Bind(R.id.tvTitle)TextView tv_title;
    private AttentionAdapter adapter;
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
        adapter=new AttentionAdapter(this,null);
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        adapter.isFirstOnly(false);
        recyclerView.setAdapter(adapter);
        View view= LayoutInflater.from(PersonalAttention.this).inflate(R.layout.normal_empty_image_view,null);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(lp2);
        TextView tvEmptyText= (TextView) view.findViewById(R.id.text_empty);
        tvEmptyText.setText("暂无数据");
        adapter.setEmptyView(view);
        RefreshUtil.refresh(swipeRefresh,this);
        swipeRefresh.setOnRefreshListener(this);
        onRefresh();
    }
    private void setInfo() {
        SharedPreferences share = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        mToken=share.getString("Token","");
        mUserId=share.getInt("UserId",0);
        tv_title.setText("我的关注");
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
    /** 下拉刷新 */
    @Override
    public void onRefresh() {
        OkGo.get(API.BASE_URL+"/v2/my/attention/from")
                .tag(this)
                .params("token",mToken)
                .params("accountId",mUserId)
                .execute(new JsonCallback<AttentionModel>() {
                    @Override
                    public void onSuccess(AttentionModel attentionModel, Call call, Response response) {
                        if(attentionModel.state==200){
                            adapter.setNewData(attentionModel.data);
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
