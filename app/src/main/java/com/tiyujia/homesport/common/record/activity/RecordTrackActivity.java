package com.tiyujia.homesport.common.record.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.record.adapter.RecordTrackAdapter;
import com.tiyujia.homesport.common.personal.model.ActiveModel;
import com.tiyujia.homesport.common.record.model.OverViewModel;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.entity.LzyResponse;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者: Cymbi on 2016/11/22 09:44.
 * 邮箱:928902646@qq.com
 */

public class RecordTrackActivity extends ImmersiveActivity implements View.OnClickListener {
    @Bind(R.id.ivBack)    ImageView ivBack;
    @Bind(R.id.ivShare)   ImageView ivShare;
    @Bind(R.id.llTrack)   LinearLayout llTrack;
    @Bind(R.id.recyclerView)    RecyclerView recyclerView;
    @Bind(R.id.tvSportTimes)    TextView tvSportTimes;
    @Bind(R.id.tvTotalScore)    TextView tvTotalScore;
    private AlertDialog builder;
    private String mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_track);
        initView();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }
    private void initView() {
        SharedPreferences share=getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        mToken=share.getString("Token","");
        ivBack.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        llTrack.setOnClickListener(this);
        OkGo.post(API.BASE_URL+"/v2/record/overview")
                .tag(this)
                .params("token",mToken)
                .execute(new LoadCallback<LzyResponse<OverViewModel>>(this) {
                    @Override
                    public void onSuccess(LzyResponse<OverViewModel> model, Call call, Response response) {
                        if(model.state==200){
                            tvSportTimes.setText(model.data.sportTimes+"");
                            tvTotalScore.setText(model.data.totalScore+"");
                        }
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
                TextView tvQQ=(TextView)view.findViewById(R.id.tvQQ);
                TextView tvQQzone=(TextView)view.findViewById(R.id.tvQQzone);
                TextView tvWeChat=(TextView)view.findViewById(R.id.tvWeChat);
                TextView tvFriends=(TextView)view.findViewById(R.id.tvFriends);
                TextView tvSina=(TextView)view.findViewById(R.id.tvSina);
                TextView tvDelete=(TextView)view.findViewById(R.id.tvDelete);
                TextView tvCancel=(TextView)view.findViewById(R.id.tvCancel);

                //分享到QQ
                tvQQ.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        showToast("分享到QQ");
                        showDialog();
                        dialog.dismiss();
                    }
                });
                //分享到QQ空间
                tvQQzone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showToast("分享到QQ空间");
                        showDialog();
                        dialog.dismiss();
                    }
                });
                //分享到微信好友
                tvWeChat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showToast("分享到微信好友");
                        showDialog();
                        dialog.dismiss();
                    }
                });
                //分享到朋友圈
                tvFriends.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showToast("分享到朋友圈");
                        showDialog();
                        dialog.dismiss();
                    }
                });
                //分享到新浪微博
                tvSina.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showToast("分享到新浪微博");
                        showDialog();
                        dialog.dismiss();
                    }
                });
                //删除
                tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showToast("删除");
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
            case R.id.llTrack:
                showToast("调到地图界面");
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
}
