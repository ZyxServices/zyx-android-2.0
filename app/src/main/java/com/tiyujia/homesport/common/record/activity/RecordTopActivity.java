package com.tiyujia.homesport.common.record.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.gms.common.api.Api;
import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.record.adapter.RecordTopAdapter;
import com.tiyujia.homesport.common.record.model.TopModel;
import com.tiyujia.homesport.common.record.model.UserTopModel;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.entity.LzyResponse;
import com.tiyujia.homesport.util.LvUtil;
import com.tiyujia.homesport.util.PicUtil;
import com.tiyujia.homesport.util.PicassoUtil;
import com.tiyujia.homesport.util.StringUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者: Cymbi on 2016/11/21 14:36.
 * 邮箱:928902646@qq.com
 */

public class RecordTopActivity extends ImmersiveActivity implements View.OnClickListener{
    @Bind(R.id.ivBack)    ImageView ivBack;
    @Bind(R.id.ivAvatar)    ImageView ivAvatar;
    @Bind(R.id.ivLv)    ImageView ivLv;
    @Bind(R.id.tvUserNumber)    TextView tvUserNumber;
    @Bind(R.id.tvNickname)    TextView tvNickname;
    @Bind(R.id.tvTotalScore)    TextView tvTotalScore;
    @Bind(R.id.recyclerView)    RecyclerView recyclerView;
    @Bind(R.id.llUserinfo)    LinearLayout llUserinfo;
    private RecordTopAdapter adapter;
    private String mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_top);
        setView();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new RecordTopAdapter(null);
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        adapter.isFirstOnly(false);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivBack:
                finish();
                break;
        }
    }
    private void setView() {
        SharedPreferences share = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        mToken=share.getString("Token","");
        ivBack.setOnClickListener(this);
        OkGo.post(API.BASE_URL+"/v2/record/rank/list")
                .tag(this)
                .params("token",mToken)
                .params("pageSize",100)
                .params("pageNum",1)
                .execute(new LoadCallback<TopModel>(this) {
                    @Override
                    public void onSuccess(TopModel topModel, Call call, Response response) {
                        if(topModel.state==200){
                            adapter.setNewData(topModel.data);
                        }
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        showToast("服务器故障");
                    }
                });
        OkGo.post(API.BASE_URL+"/v2/record/rank/self")
                .tag(this)
                .params("token",mToken)
                .execute(new LoadCallback<LzyResponse<UserTopModel>>(this) {
                    @Override
                    public void onSuccess(LzyResponse<UserTopModel> user, Call call, Response response) {
                        if(user.state==200){
                            if(user.data!=null){
                                tvNickname.setText(user.data.userIconVo.nickName);
                                tvUserNumber.setText("  "+user.data.rankNum+"");
                                tvTotalScore.setText(user.data.totalScore+"");
                                if (user.data.userIconVo.levelName!=null&&user.data.userIconVo.levelName.equals("")){
                                    LvUtil.setLv(ivLv,user.data.userIconVo.levelName);
                                }else {
                                    LvUtil.setLv(ivLv,"初学乍练");
                                }
                                PicassoUtil.handlePic(RecordTopActivity.this, PicUtil.getImageUrlDetail(RecordTopActivity.this, StringUtil.isNullAvatar(user.data.userIconVo.avatar), 320, 320),ivAvatar,320,320);
                            }else {
                                llUserinfo.setVisibility(View.GONE);
                            }

                        }
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        showToast("服务器故障");
                    }
                });
    }
}
