package com.tiyujia.homesport.common.personal.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.App;
import com.tiyujia.homesport.BaseFragment;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.personal.activity.PersonalActive;
import com.tiyujia.homesport.common.personal.activity.PersonalAttention;
import com.tiyujia.homesport.common.personal.activity.PersonalDynamic;
import com.tiyujia.homesport.common.personal.activity.PersonalEquipmentShow;
import com.tiyujia.homesport.common.personal.activity.PersonalFans;
import com.tiyujia.homesport.common.personal.activity.PersonalLogin;
import com.tiyujia.homesport.common.personal.activity.PersonalMsg;
import com.tiyujia.homesport.common.personal.activity.PersonalPanyanGold;
import com.tiyujia.homesport.common.personal.activity.PersonalSystemSetting;
import com.tiyujia.homesport.common.personal.model.UserInfoModel;
import com.tiyujia.homesport.entity.JsonCallback;
import com.tiyujia.homesport.entity.LzyResponse;
import com.tiyujia.homesport.util.PicUtil;
import com.tiyujia.homesport.util.PicassoUtil;
import com.tiyujia.homesport.util.StatusBarUtil;
import com.tiyujia.homesport.util.StringUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by zzqybyb19860112 on 2016/10/18.
 */
public class PersonalFragment extends BaseFragment implements View.OnClickListener{

    @Bind(R.id.iv_msg) ImageView iv_msg;
    @Bind(R.id.iv_setting) ImageView iv_setting;
    @Bind(R.id.ivAvatar) ImageView ivAvatar;
    @Bind(R.id.tvLv) TextView tvLv;
    @Bind(R.id.tvGz) TextView tvGz;
    @Bind(R.id.tvFs) TextView tvFs;
    @Bind(R.id.tvCoin) TextView tvCoin;
    @Bind(R.id.tvName) TextView tvName;
    @Bind(R.id.tv_intro) TextView tv_intro;
    @Bind(R.id.ll_attention) LinearLayout ll_attention;
    @Bind(R.id.ll_fans) LinearLayout ll_fans;
    @Bind(R.id.ll_gold) LinearLayout ll_gold;
    @Bind(R.id.ll_user) LinearLayout ll_user;
    @Bind(R.id.re_active) RelativeLayout re_active;
    @Bind(R.id.re_dynamic) RelativeLayout re_dynamic;
    @Bind(R.id.re_show) RelativeLayout re_show;
    @Bind(R.id.re_login) RelativeLayout re_login;
    @Bind(R.id.btn_login)    Button btn_login;
    private SharedPreferences mShare;
    private String mToken;
    private int mUserId;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.personal_home_fragment,null);
        ButterKnife.bind(this, view);
        isLogin();
        return view;
    }
    private void isLogin() {
        SharedPreferences share = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        mToken=share.getString("Token","");
        mUserId=share.getInt("UserId",0);
        if(!TextUtils.isEmpty(mToken)){
            re_login.setVisibility(View.GONE);
            setData();
        }else {
            ll_user.setVisibility(View.GONE);

        }
    }
    public void setData() {
        OkGo.get(API.BASE_URL+"/v2/user/center_info")
                .tag(this)
                .params("token",mToken)
                .params("account_id",mUserId)
                .execute(new JsonCallback<UserInfoModel>() {
                    @Override
                    public void onSuccess(UserInfoModel userInfoModel, Call call, Response response) {
                             if(userInfoModel.state==200){
                                 PicassoUtil.handlePic(getActivity(), PicUtil.getImageUrlDetail(getActivity(),StringUtil.isNullAvatar(userInfoModel.data.avatar), 320, 320),ivAvatar,320,320);
                                 String nickname=userInfoModel.data.nickname.toString();
                                 String level =userInfoModel.data.level.pointDesc.toString();
                                 String signature=userInfoModel.data.signature.toString();
                                 int fs=userInfoModel.data.fs;
                                 int gz=userInfoModel.data.gz;
                                 int coin=userInfoModel.data.coin;
                                 if(TextUtils.isEmpty(signature)){
                                     tv_intro.setText("个人简介: 这个人很懒，什么也没有留下");
                                 }else {
                                     tv_intro.setText("个人简介："+signature);
                                 }
                                 tvName.setText(nickname);
                                 tvLv.setText(level);
                                 tvGz.setText(gz+"");
                                 tvFs.setText(fs+"");
                                 tvCoin.setText(coin+"");
                             }
                        if (userInfoModel.state==401){
                            showToast("Token失效");
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);

                    }
                });

    }
    @Override
    protected void initData() {
        iv_msg.setOnClickListener(this);
        iv_setting.setOnClickListener(this);
        ivAvatar.setOnClickListener(this);
        ll_attention.setOnClickListener(this);
        ll_fans.setOnClickListener(this);
        ll_gold.setOnClickListener(this);
        re_active.setOnClickListener(this);
        re_dynamic.setOnClickListener(this);
        re_show.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_msg:
                getActivity().startActivity(new Intent(getActivity(), PersonalMsg.class));
                break;
            case R.id.iv_setting:
                getActivity().startActivity(new Intent(getActivity(), PersonalSystemSetting.class));
                break;
            case R.id.ivAvatar:
                Toast.makeText(getActivity(),"hdksajhdksja",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_attention:
                getActivity().startActivity(new Intent(getActivity(), PersonalAttention.class));
                break;
            case R.id.ll_fans:
                getActivity().startActivity(new Intent(getActivity(), PersonalFans.class));
                break;
            case R.id.ll_gold:
                getActivity().startActivity(new Intent(getActivity(), PersonalPanyanGold.class));
                break;
            case R.id.re_active:
                getActivity().startActivity(new Intent(getActivity(), PersonalActive.class));
                break;
            case R.id.re_dynamic:
                getActivity().startActivity(new Intent(getActivity(), PersonalDynamic.class));
                break;
            case R.id.re_show:
                getActivity().startActivity(new Intent(getActivity(), PersonalEquipmentShow.class));
                break;
            case R.id.btn_login:
                getActivity().startActivity(new Intent(getActivity(), PersonalLogin.class));
                break;
        }
    }
}
