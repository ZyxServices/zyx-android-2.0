package com.tiyujia.homesport.common.homepage.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.entity.DateInfoModel;
import com.tiyujia.homesport.entity.JsonCallback;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.util.LvUtil;
import com.tiyujia.homesport.util.PicUtil;
import com.tiyujia.homesport.util.PicassoUtil;
import com.tiyujia.homesport.util.RefreshUtil;
import com.tiyujia.homesport.util.StringUtil;
import com.tiyujia.homesport.util.TimeUtil;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者: Cymbi on 2016/12/7 18:50.
 * 邮箱:928902646@qq.com
 */

public class HomePageDateInfo extends ImmersiveActivity implements SwipeRefreshLayout.OnRefreshListener,View.OnClickListener{
    @Bind(R.id.ivBack)    ImageView ivBack;
    @Bind(R.id.ivShare)    ImageView ivShare;
    @Bind(R.id.ivAvatar)    ImageView ivAvatar;
    @Bind(R.id.ivBackground)    ImageView ivBackground;
    @Bind(R.id.ivLv)    ImageView ivLv;
    @Bind(R.id.tvTitle)    TextView tvTitle;
    @Bind(R.id.tvTime)    TextView tvTime;
    @Bind(R.id.tvContent)    TextView tvContent;
    @Bind(R.id.tvAddress)    TextView tvAddress;
    @Bind(R.id.tvNickname)    TextView tvNickname;
    @Bind(R.id.tvSign)    TextView tvSign;
    @Bind(R.id.tvStartTime)    TextView tvStartTime;
    @Bind(R.id.tvPhone)    TextView tvPhone;
    @Bind(R.id.tvCity)    TextView tvCity;
    @Bind(R.id.srlRefresh)    SwipeRefreshLayout srlRefresh;
    private int activityId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_date_info);
        setView();
        activityId=getIntent().getIntExtra("id",0);
        RefreshUtil.refresh(srlRefresh,this);
        srlRefresh.setOnRefreshListener(this);
        onRefresh();
    }

    private void setView() {
        ivBack.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        tvPhone.setOnClickListener(this);
    }
    @Override
    public void onRefresh() {
        OkGo.post(API.BASE_URL+"/v2/activity/activityById")
                .tag(this)
                .params("activityId",activityId)
                .execute(new LoadCallback<DateInfoModel>(this) {
                    @Override
                    public void onSuccess(DateInfoModel dateInfoModel, Call call, Response response) {
                        if(dateInfoModel.state==200){
                            tvTitle.setText(dateInfoModel.data.title);
                            String starttime=API.format.format(dateInfoModel.data.startTime);
                            String endtime=API.format.format(dateInfoModel.data.endTime);
                            tvTime.setText(starttime+"—"+endtime);
                            tvContent.setText(dateInfoModel.data.descContent);
                            tvAddress.setText(dateInfoModel.data.address);
                            tvNickname.setText(dateInfoModel.data.user.nickname);
                            if (!TextUtils.isEmpty(dateInfoModel.data.user.signature)){
                                tvSign.setText(dateInfoModel.data.user.signature);
                            }else {
                                tvSign.setText("这个人很懒，什么也没有留下");
                            }
                            if (dateInfoModel.data.user.level!=null||dateInfoModel.data.user.level.equals("")){
                                LvUtil.setLv(ivLv,dateInfoModel.data.user.level.pointDesc);
                            }else {
                                LvUtil.setLv(ivLv,"初学乍练");
                            }
                            String  create= TimeUtil.checkTime(dateInfoModel.data.createTime);
                            String  end= TimeUtil.checkTime(dateInfoModel.data.endTime);
                            tvStartTime.setText(create+"~"+end);

                            tvPhone.setText(dateInfoModel.data.user.phone);
                            tvCity.setText(dateInfoModel.data.city);
                            PicassoUtil.handlePic(HomePageDateInfo.this, PicUtil.getImageUrlDetail(HomePageDateInfo.this, StringUtil.isNullImage(dateInfoModel.data.imgUrls), 1280,720 ),ivBackground,1280,720);
                            PicassoUtil.handlePic(HomePageDateInfo.this, PicUtil.getImageUrlDetail(HomePageDateInfo.this, StringUtil.isNullImage(dateInfoModel.data.user.avatar), 320,320 ),ivAvatar,320,320);
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        showToast("网络连接错误");
                    }

                    @Override
                    public void onAfter(@Nullable DateInfoModel dateInfoModel, @Nullable Exception e) {
                        super.onAfter(dateInfoModel, e);
                        setRefreshing(false);
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivShare:

                break;
            case R.id.tvPhone:
                final AlertDialog builder = new AlertDialog.Builder(HomePageDateInfo.this).create();
                builder.setView(getLayoutInflater().inflate(R.layout.call_phone_dialog, null));
                builder.show();
                //去掉dialog四边的黑角
                builder.getWindow().setBackgroundDrawable(new BitmapDrawable());
                TextView text=(TextView) builder.getWindow().findViewById(R.id.text);
                text.setText("直接拨打"+tvPhone.getText()+"?");
                builder.getWindow().findViewById(R.id.dialog_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();
                    }
                });
                TextView dialog_confirm=(TextView)builder.getWindow().findViewById(R.id.dialog_confirm);
                dialog_confirm.setText("拨打");
                dialog_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+tvPhone.getText().toString().trim()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        builder.dismiss();
                    }
                });
                break;
        }
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
