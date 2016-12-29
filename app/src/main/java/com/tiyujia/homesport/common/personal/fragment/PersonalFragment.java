package com.tiyujia.homesport.common.personal.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.ff.imagezoomdrag.ImageDetailActivity;
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
import com.tiyujia.homesport.util.FastBlurUtil;
import com.tiyujia.homesport.util.LvUtil;
import com.tiyujia.homesport.util.PicUtil;
import com.tiyujia.homesport.util.PicassoUtil;
import com.tiyujia.homesport.util.StatusBarUtil;
import com.tiyujia.homesport.util.StringUtil;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

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
    @Bind(R.id.ivBackground) ImageView ivBackground;
    @Bind(R.id.ivLv) ImageView ivLv;
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
    private static final int HANDLE_IMAGE=1;
    private String avatarUrl="";
     Handler handler=new Handler(){
         @Override
         public void handleMessage(Message msg) {
             switch (msg.what){
                 case HANDLE_IMAGE:
                     Bitmap bitmap= (Bitmap) msg.obj;
                     if(bitmap!=null){
                         Bitmap blur = FastBlurUtil.blurBitmap(bitmap);
                         ivBackground.setImageBitmap(blur);
                     }else {}
                     break;
             }
         }
     };
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
        Log.e("token",mToken);
        Log.e("userid",mUserId+"");
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
                    public void onSuccess(final UserInfoModel userInfoModel, Call call, Response response) {
                        if(userInfoModel.state==200){
                            avatarUrl=userInfoModel.data.avatar;
                            PicassoUtil.handlePic(getActivity(), PicUtil.getImageUrlDetail(getActivity(),StringUtil.isNullAvatar(avatarUrl), 320, 320),ivAvatar,320,320);
                            String nickname=userInfoModel.data.nickname.toString();
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
                            tvGz.setText(gz+"");
                            tvFs.setText(fs+"");
                            tvCoin.setText(coin+"");
                            if(userInfoModel.data.level!=null){
                                LvUtil.setLv(ivLv,userInfoModel.data.level.pointDesc);
                            }else {
                                LvUtil.setLv(ivLv,"初学乍练");
                            }
                            if(userInfoModel.data.avatar!=null){
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        final Bitmap blur = getBitmap(API.PICTURE_URL+userInfoModel.data.avatar);
                                        Message message=new Message();
                                        message.what=HANDLE_IMAGE;
                                        message.obj=blur;
                                        handler.sendMessage(message);
                                    }
                                }).start();
                            }
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
                if(avatarUrl==null){
                    ArrayList<String> str=new ArrayList<>();
                    str.add("http://image.tiyujia.com/group1/M00/00/00/052YyFfXxLKARvQWAAAbNiA-OGw444.png");
                    getActivity().startActivity(ImageDetailActivity.getMyStartIntent(getActivity(), str,0, ImageDetailActivity.url_path));
                }else {ArrayList<String> str=new ArrayList<>();
                    str.add(API.PICTURE_URL+avatarUrl);
                    getActivity().startActivity(ImageDetailActivity.getMyStartIntent(getActivity(), str,0, ImageDetailActivity.url_path));}
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

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
    public Bitmap getBitmap(String url) {
        Bitmap bm = null;
        try {
            URL iconUrl = new URL(url);
            URLConnection conn = iconUrl.openConnection();
            HttpURLConnection http = (HttpURLConnection) conn;
            int length = http.getContentLength();
            conn.connect();
            // 获得图像的字符流
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is, length);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();// 关闭流
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }
}
