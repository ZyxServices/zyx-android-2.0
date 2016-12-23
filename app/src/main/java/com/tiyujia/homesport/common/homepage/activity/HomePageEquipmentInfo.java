package com.tiyujia.homesport.common.homepage.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.adapter.NGLAdapter;
import com.tiyujia.homesport.common.homepage.entity.EquipmentInfoModel;
import com.tiyujia.homesport.common.personal.activity.PersonalOtherHome;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.util.DeleteUtil;
import com.tiyujia.homesport.util.PicUtil;
import com.tiyujia.homesport.util.PicassoUtil;
import com.tiyujia.homesport.util.StringUtil;
import com.w4lle.library.NineGridlayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者: Cymbi on 2016/12/7 10:16.
 * 邮箱:928902646@qq.com
 */

public class HomePageEquipmentInfo extends ImmersiveActivity {
    @Bind(R.id.ivBack)
    ImageView ivBack;
    @Bind(R.id.ivMenu)    ImageView ivMenu;
    @Bind(R.id.ivAvatar)    ImageView ivAvatar;
    @Bind(R.id.tvOfficial)    TextView tvOfficial;
    @Bind(R.id.tvNickname)    TextView tvNickname;
    @Bind(R.id.tvTime)    TextView tvTime;
    @Bind(R.id.tvTitle)    TextView tvTitle;
    @Bind(R.id.tvContent)    TextView tvContent;
    @Bind(R.id.nineGrid)    NineGridlayout nineGrid;
    private int userId;
    String token;
    int equipId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipment_info);
        SharedPreferences share= getSharedPreferences("UserInfo",MODE_PRIVATE);
        token= share.getString("Token","");
        userId=share.getInt("UserId",0);
        equipId=getIntent().getIntExtra("id",0);
        OkGo.get(API.BASE_URL+"/v2/equip/queryOne")
                .tag(this)
                .params("token",token)
                .params("id",equipId)
                .execute(new LoadCallback<EquipmentInfoModel>(this) {
                    @Override
                    public void onSuccess(final EquipmentInfoModel Model, Call call, Response response) {
                    if(Model.state==200){
                        tvNickname.setText(Model.data.userIconVo.nickName);
                        tvTime.setText(API.simpleYear.format(Model.data.createTime));
                        PicassoUtil.handlePic(HomePageEquipmentInfo.this, PicUtil.getImageUrlDetail(HomePageEquipmentInfo.this, StringUtil.isNullAvatar(Model.data.userIconVo.avatar), 320, 320), ivAvatar, 320, 320);
                        tvTitle.setText(Model.data.title);
                        tvContent.setText(Model.data.content);
                        if (Model.data.imgUrl != null) {
                            String str = Model.data.imgUrl;
                            List<String> imgUrls = StringUtil.stringToList(str);;
                            NGLAdapter adapter = new NGLAdapter(HomePageEquipmentInfo.this, imgUrls);
                            nineGrid.setVisibility(View.VISIBLE);
                            nineGrid.setGap(6);
                            nineGrid.setAdapter(adapter);
                        }
                        if (Model.data.userIconVo.id!=userId){
                            ivMenu.setVisibility(View.GONE);
                            ivAvatar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i= new Intent(HomePageEquipmentInfo.this, PersonalOtherHome.class);
                                    i.putExtra("id",Model.data.userIconVo.id);
                                    startActivity(i);
                                }
                            });
                        }else {
                            ivMenu.setVisibility(View.VISIBLE);
                        }
                    }
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        showToast("网络连接错误");
                    }
                });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.share_dialog, null);
                final Dialog dialog = new Dialog(HomePageEquipmentInfo.this,R.style.Dialog_Fullscreen);
                TextView tvDelete=(TextView)view.findViewById(R.id.tvDelete);
                TextView tvCancel=(TextView)view.findViewById(R.id.tvCancel);
                //删除
                tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DeleteUtil.handleDeleteActiveTransaction(HomePageEquipmentInfo.this,token,equipId,userId);
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
            }
        });
    }
}
