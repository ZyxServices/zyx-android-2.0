package com.tiyujia.homesport.common.homepage.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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
import com.tiyujia.homesport.util.PicUtil;
import com.tiyujia.homesport.util.PicassoUtil;
import com.tiyujia.homesport.util.StringUtil;
import com.w4lle.library.NineGridlayout;

import java.util.ArrayList;

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
    private int UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipment_info);
        SharedPreferences share= getSharedPreferences("UserInfo",MODE_PRIVATE);
        String token= share.getString("Token","");
        UserId=share.getInt("UserId",0);
        int id=getIntent().getIntExtra("id",0);
        OkGo.get(API.BASE_URL+"/v2/equip/queryOne")
                .tag(this)
                .params("token",token)
                .params("id",id)
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
                            ArrayList<String> imgUrls = new ArrayList<>();
                            if (str.contains(",")) {
                                String[] s = str.split(",");
                                for (String s1 : s) {
                                    imgUrls.add(API.PICTURE_URL + s1);
                                }
                            }
                            NGLAdapter adapter = new NGLAdapter(HomePageEquipmentInfo.this, imgUrls);
                            nineGrid.setVisibility(View.VISIBLE);
                            nineGrid.setGap(6);
                            nineGrid.setAdapter(adapter);
                        }
                        if (Model.data.userIconVo.id!=UserId){
                            ivAvatar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i= new Intent(HomePageEquipmentInfo.this, PersonalOtherHome.class);
                                    i.putExtra("id",Model.data.userIconVo.id);
                                    startActivity(i);
                                }
                            });
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
    }
}
