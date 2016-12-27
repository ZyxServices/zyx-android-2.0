package com.tiyujia.homesport.common.homepage.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ff.imagezoomdrag.ImageDetailActivity;
import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.community.activity.CommunityDynamicDetailActivity;
import com.tiyujia.homesport.common.homepage.entity.DateInfoModel;
import com.tiyujia.homesport.common.personal.activity.PersonalLogin;
import com.tiyujia.homesport.common.personal.activity.PersonalOtherHome;
import com.tiyujia.homesport.entity.JsonCallback;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.entity.LzyResponse;
import com.tiyujia.homesport.util.DeleteUtil;
import com.tiyujia.homesport.util.LvUtil;
import com.tiyujia.homesport.util.PicUtil;
import com.tiyujia.homesport.util.PicassoUtil;
import com.tiyujia.homesport.util.RefreshUtil;
import com.tiyujia.homesport.util.StringUtil;
import com.tiyujia.homesport.util.TimeUtil;

import java.util.ArrayList;

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
    @Bind(R.id.tvPush)    TextView tvPush;
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
    private AlertDialog builder;
    private int mUserId;
    private String mToken;
    private int activityUserId;
    private long lastTime;
    private String imgUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_date_info);
        getInfo();
        activityId=getIntent().getIntExtra("id",0);
        RefreshUtil.refresh(srlRefresh,this);
        srlRefresh.setOnRefreshListener(this);
        onRefresh();
        ivBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> list = new ArrayList<String>();
                list.add(API.PICTURE_URL+imgUrls);
                startActivity(ImageDetailActivity.getMyStartIntent(HomePageDateInfo.this, list,0, ImageDetailActivity.url_path));
            }
        });
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(HomePageDateInfo.this, PersonalOtherHome.class);
                i.putExtra("id",activityUserId);
                startActivity(i);
            }
        });
        setView();
    }
    private void getInfo() {
        SharedPreferences share= getSharedPreferences("UserInfo",MODE_PRIVATE);
        mToken= share.getString("Token","");
        mUserId=share.getInt("UserId",0);
    }

    private void setView() {
        ivBack.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        tvPhone.setOnClickListener(this);
        tvPush.setOnClickListener(this);
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
                            imgUrls=dateInfoModel.data.imgUrls;
                            tvTitle.setText(dateInfoModel.data.title);
                            activityUserId=dateInfoModel.data.user.id;
                            lastTime=dateInfoModel.data.lastTime;
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
                            if (mUserId==activityUserId){
                                ivShare.setVisibility(View.VISIBLE);
                            }else {
                                ivShare.setVisibility(View.GONE);
                            }
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
                View view = getLayoutInflater().inflate(R.layout.share_dialog, null);
                final Dialog dialog = new Dialog(this,R.style.Dialog_Fullscreen);
//                TextView tvQQ=(TextView)view.findViewById(R.id.tvQQ);
//                TextView tvQQzone=(TextView)view.findViewById(R.id.tvQQzone);
//                TextView tvWeChat=(TextView)view.findViewById(R.id.tvWeChat);
//                TextView tvFriends=(TextView)view.findViewById(R.id.tvFriends);
//                TextView tvSina=(TextView)view.findViewById(R.id.tvSina);
                TextView tvDelete=(TextView)view.findViewById(R.id.tvDelete);
                TextView tvCancel=(TextView)view.findViewById(R.id.tvCancel);
//
//                //分享到QQ
//                tvQQ.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v)
//                    {
//                        showToast("分享到QQ");
//                        showDialog();
//                        dialog.dismiss();
//                    }
//                });
//                //分享到QQ空间
//                tvQQzone.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        showToast("分享到QQ空间");
//                        showDialog();
//                        dialog.dismiss();
//                    }
//                });
//                //分享到微信好友
//                tvWeChat.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        showToast("分享到微信好友");
//                        showDialog();
//                        dialog.dismiss();
//                    }
//                });
//                //分享到朋友圈
//                tvFriends.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        showToast("分享到朋友圈");
//                        showDialog();
//                        dialog.dismiss();
//                    }
//                });
//                //分享到新浪微博
//                tvSina.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        showToast("分享到新浪微博");
//                        showDialog();
//                        dialog.dismiss();
//                    }
//                });
                //删除
                tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DeleteUtil.handleDeleteActiveTransaction(HomePageDateInfo.this,mToken,activityId,mUserId);
                        showToast("删除活动成功");
                        dialog.dismiss();
                    }
                });
                //取消
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showToast("取消");
                        dialog.dismiss();
                    }
                });
                dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                Window window = dialog.getWindow();
                // 设置显示动画
                window.setWindowAnimations(R.style.main_menu_animstyle);
                WindowManager.LayoutParams wl = window.getAttributes();
                wl.x = 0;
                wl.y = getWindowManager().getDefaultDisplay().getHeight();
                // 以下这两句是为了保证按钮可以水平满屏
                wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
                wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                // 设置显示位置
                dialog.onWindowAttributesChanged(wl);
                // 设置点击外围解散
                dialog.show();
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
            case R.id.tvPush:
                long currentTime = System.currentTimeMillis();
                if (currentTime>lastTime){
                    showToast("报名时间已过，无法报名");
                }else{
                    OkGo.post(API.BASE_URL+"/v2/member/add")
                            .tag(this)
                            .params("token",mToken)
                            .params("userId",mUserId)
                            .params("activityId",activityId)
                            .params("activityUserId",activityUserId)
                            .execute(new LoadCallback<LzyResponse>(this) {
                                @Override
                                public void onSuccess(LzyResponse lzyResponse, Call call, Response response) {
                                    if(lzyResponse.state==200){
                                        AlertDialog  builder = new AlertDialog.Builder(HomePageDateInfo.this).create();
                                        builder.setView(HomePageDateInfo.this.getLayoutInflater().inflate(R.layout.share_succeed_dialog, null));
                                        builder.show();
                                        //去掉dialog四边的黑角
                                        builder.getWindow().setBackgroundDrawable(new BitmapDrawable());
                                        TextView tvTitle=(TextView)builder.getWindow().findViewById(R.id.tvTitle);
                                        tvTitle.setText("报名成功");
                                        TextView tvContent=(TextView)builder.getWindow().findViewById(R.id.tvContent);
                                        tvContent.setText("感谢您的报名，祝您玩愉快");
                                    }else if(lzyResponse.state==10005){
                                        showToast("亲，请勿重复报名哦");
                                    }else if(lzyResponse.state==800){
                                        showToast("亲，账户已失效或者未登录");
                                        Intent intent=new Intent(HomePageDateInfo.this, PersonalLogin.class);
                                        HomePageDateInfo.this.startActivity(intent);
                                    }else if(lzyResponse.state==10013){
                                        showToast("亲，报名人数已满，无法报名");
                                    }
                                }
                            });
                }
                break;
        }
    }
    private void showDialog(){
        builder = new AlertDialog.Builder(this).create();
        builder.setView(this.getLayoutInflater().inflate(R.layout.share_succeed_dialog, null));
        builder.show();
        //去掉dialog四边的黑角
        builder.getWindow().setBackgroundDrawable(new BitmapDrawable());
        TextView tvTitle=(TextView)builder.getWindow().findViewById(R.id.tvTitle);
        tvTitle.setText("分享成功");
        TextView tvContent=(TextView)builder.getWindow().findViewById(R.id.tvContent);
        tvContent.setText("感谢您的分享，祝您玩愉快");
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
