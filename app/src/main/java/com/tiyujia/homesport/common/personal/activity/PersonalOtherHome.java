package com.tiyujia.homesport.common.personal.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.personal.model.UserInfoModel;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.util.PicUtil;
import com.tiyujia.homesport.util.PicassoUtil;
import com.tiyujia.homesport.util.StringUtil;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者: Cymbi on 2016/11/23 14:30.
 * 邮箱:928902646@qq.com
 */

public class PersonalOtherHome extends ImmersiveActivity implements View.OnClickListener{
    @Bind(R.id.ivBack)    ImageView ivBack;
    @Bind(R.id.ivAvatar)    ImageView ivAvatar;
    @Bind(R.id.ivLv)    ImageView ivLv;
    @Bind(R.id.tvNickname)    TextView tvNickname;
    @Bind(R.id.tvAddAttention)    TextView tvAddAttention;
    @Bind(R.id.tvCcontent)    TextView tvCcontent;
    @Bind(R.id.tvAttention)    TextView tvAttention;
    @Bind(R.id.tvFans)    TextView tvFans;
    @Bind(R.id.tab)    TabLayout tab;
    @Bind(R.id.vp)    ViewPager vp;
    private String token="tiyujia2016";
    private int account_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_other_home);
        account_id=getIntent().getIntExtra("id",0);
        setView();
        setData();
    }
    private void setData() {
        OkGo.get(API.BASE_URL+"/v2/user/center_info")
                .tag(this)
                .params("token",token)
                .params("account_id",account_id)
                .execute(new LoadCallback<UserInfoModel>(this) {
                    @Override
                    public void onSuccess(UserInfoModel userInfoModel, Call call, Response response) {
                        if(userInfoModel.state==200){
                            PicassoUtil.handlePic(PersonalOtherHome.this, PicUtil.getImageUrlDetail(PersonalOtherHome.this, StringUtil.isNullAvatar(userInfoModel.data.avatar), 320, 320),ivAvatar,320,320);
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        showToast("网络连接错误");
                    }
                });
    }
    private void setView() {
        ivBack.setOnClickListener(this);
        tvAddAttention.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivBack:
                finish();
                break;
            case R.id.tvAddAttention:

                break;
        }
    }
}
