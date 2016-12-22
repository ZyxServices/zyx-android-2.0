package com.tiyujia.homesport.common.homepage.activity;

import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.NewBaseActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.adapter.MorePeopleGoneAdapter;
import com.tiyujia.homesport.common.homepage.entity.WhomGoneEntity;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.entity.LzyResponse;
import com.tiyujia.homesport.util.RefreshUtil;
import java.util.List;
import okhttp3.Call;
import okhttp3.Response;

public class MorePeopleGoneActivity extends NewBaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    ImageView ivBack;
    SwipeRefreshLayout srlRefresh;
    RecyclerView recyclerView;
    MorePeopleGoneAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_people_gone);
        ivBack= (ImageView) findViewById(R.id.ivBack);
        srlRefresh= (SwipeRefreshLayout) findViewById(R.id.srlRefresh);
        recyclerView= (RecyclerView) findViewById(R.id.recyclerView);
        adapter =new MorePeopleGoneAdapter(null);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        adapter.isFirstOnly(false);
        recyclerView.setAdapter(adapter);
        RefreshUtil.refresh(srlRefresh,this);
        srlRefresh.setOnRefreshListener(this);
        onRefresh();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MorePeopleGoneActivity.this.finish();
            }
        });
    }
    @Override
    public void onRefresh() {
        int venueId=getIntent().getIntExtra("venueId",0);
        OkGo.post(API.BASE_URL+"/v2/record/users")
                .tag(this)
                .params("venueId",venueId)
                .params("pageSize",100)
                .params("pageNum",1)
                .execute(new LoadCallback<LzyResponse<List<WhomGoneEntity>>>(this) {
                    @Override
                    public void onSuccess(LzyResponse<List<WhomGoneEntity>> result, Call call, Response response) {
                        if (result.state==200){
                            adapter.setNewData(result.data);
                        }
                    }
                    @Override
                    public void onAfter(@Nullable LzyResponse<List<WhomGoneEntity>> listLzyResponse, @Nullable Exception e) {
                        super.onAfter(listLzyResponse, e);
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
