package com.tiyujia.homesport.common.personal.activity;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.personal.adapter.EquipmentShowAdapter;
import com.tiyujia.homesport.common.personal.adapter.FansAdapter;
import com.tiyujia.homesport.common.personal.model.ActiveModel;
import com.tiyujia.homesport.common.personal.model.EquipmentShowModel;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.util.RefreshUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者: Cymbi on 2016/11/14 17:45.
 * 邮箱:928902646@qq.com
 */

public class PersonalEquipmentShow extends ImmersiveActivity implements SwipeRefreshLayout.OnRefreshListener{
    @Bind(R.id.personal_back)    ImageView personal_back;
    @Bind(R.id.srlRefresh)    SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.tv_title)    TextView tv_title;
    @Bind(R.id.recyclerView)RecyclerView recyclerView;
    private String mToken;
    private int mUserId;
    private EquipmentShowAdapter adapter;
    private int pageSize=100;
    private int page=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_equipment_show);
        setInfo();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new EquipmentShowAdapter(null);
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        adapter.isFirstOnly(false);
        recyclerView.setAdapter(adapter);
        View view= LayoutInflater.from(PersonalEquipmentShow.this).inflate(R.layout.normal_empty_image_view,null);
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
        tv_title.setText("装备秀");
        personal_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public void onRefresh() {
        OkGo.get(API.BASE_URL+"/v2/my/equipment/create/list")
                .tag(this)
                .params("token",mToken)
                .params("userId",mUserId)
                .params("page",page)
                .params("pageSize",pageSize)
                .execute(new LoadCallback<EquipmentShowModel>(this) {
                    @Override
                    public void onSuccess(EquipmentShowModel equipmentShowModel, Call call, Response response) {
                                if (equipmentShowModel.state==200){adapter.setNewData(equipmentShowModel.data);}
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        showToast("服务器故障");
                    }

                    @Override
                    public void onAfter(@Nullable EquipmentShowModel equipmentShowModel, @Nullable Exception e) {
                        super.onAfter(equipmentShowModel, e);
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
