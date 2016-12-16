package com.tiyujia.homesport.common.personal.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.tiyujia.homesport.common.personal.adapter.FansAdapter;
import com.tiyujia.homesport.common.personal.adapter.PanyanGoldInfoAdapter;
import com.tiyujia.homesport.common.personal.model.PanyanGoldInfoModel;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.entity.LzyResponse;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者: Cymbi on 2016/11/15 16:47.
 * 邮箱:928902646@qq.com
 */

public class PersonalPanyanGoldInfo extends ImmersiveActivity {
    @Bind(R.id.tv_title)TextView tv_title;
    @Bind(R.id.personal_back)ImageView personal_back;
    @Bind(R.id.recyclerView)RecyclerView recyclerView;
    private PanyanGoldInfoAdapter adapter;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_panyanbi_info);
        tv_title.setText("攀岩币明细");
        personal_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new PanyanGoldInfoAdapter(null);
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        adapter.isFirstOnly(false);
        recyclerView.setAdapter(adapter);
        SharedPreferences share=getSharedPreferences("UserInfo",MODE_PRIVATE);
        userId=share.getInt("UserId",0);
        setData();
    }

    private void setData() {
        OkGo.post(API.BASE_URL+"/v2/coin/log")
                .tag(this)
                .params("userId",userId)
                .execute(new LoadCallback<LzyResponse<List<PanyanGoldInfoModel>>>(this) {

                    @Override
                    public void onSuccess(LzyResponse<List<PanyanGoldInfoModel>> Model, Call call, Response response) {
                        if(Model.state==200){
                            adapter.setNewData(Model.data);
                        }
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        showToast("网络连接错误");
                    }
                });
    }
}
