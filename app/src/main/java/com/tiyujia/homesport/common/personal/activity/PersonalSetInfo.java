package com.tiyujia.homesport.common.personal.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
import chihane.jdaddressselector.AddressSelector;
import chihane.jdaddressselector.BottomDialog;
import chihane.jdaddressselector.OnAddressSelectedListener;
import chihane.jdaddressselector.model.City;
import chihane.jdaddressselector.model.County;
import chihane.jdaddressselector.model.Province;
import chihane.jdaddressselector.model.Street;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者: Cymbi on 2016/11/10 17:29.
 * 邮箱:928902646@qq.com
 */

public class PersonalSetInfo extends ImmersiveActivity  implements View.OnClickListener,OnAddressSelectedListener {
    @Bind(R.id.tv_title)    TextView tv_title;
    @Bind(R.id.tvAddress)  TextView tvAddress;
    @Bind(R.id.personal_back)    ImageView personal_back;
    @Bind(R.id.ivAvatar)    ImageView ivAvatar;
    @Bind(R.id.etNickName)  EditText etNickName;
    @Bind(R.id.etSignature)  EditText etSignature;
    private String mToken;
    private int mUserId;
    private BottomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_set_info);
        setInfo();
        setData();
    }

    private void setInfo() {
        tv_title.setText("个人资料");
        SharedPreferences share = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        mToken=share.getString("Token","");
        mUserId=share.getInt("UserId",0);
        personal_back.setOnClickListener(this) ;
        tvAddress.setOnClickListener(this);
        AddressSelector selector = new AddressSelector(this);
        selector.setOnAddressSelectedListener(this);
        assert tvAddress != null;
    }

    private void setData() {
        OkGo.get(API.BASE_URL+"/v2/user/center_info")
                .tag(this)
                .params("token",mToken)
                .params("account_id",mUserId)
                .execute(new LoadCallback<UserInfoModel>(this) {
                    @Override
                    public void onSuccess(UserInfoModel userInfoModel, Call call, Response response) {
                        if(userInfoModel.state==200){
                            PicassoUtil.handlePic(PersonalSetInfo.this, PicUtil.getImageUrlDetail(PersonalSetInfo.this, StringUtil.isNullAvatar(userInfoModel.data.avatar), 320, 320),ivAvatar,320,320);
                            String nickname=userInfoModel.data.nickname.toString();
                            String sex=userInfoModel.data.sex.toString();
                            String address=userInfoModel.data.address.toString();
                            String signature=userInfoModel.data.signature.toString();
                            etNickName.setText(nickname);
                            etSignature.setText(signature);

                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                    }
                });
    }


    @Override
    public void onAddressSelected(Province province, City city, County county, Street street) {

        //这里发请求修改城市
        String s =(province == null ? "" : province.name) +" "+
                (city == null ? "" :  city.name) +" "+
                (county == null ? "" :  county.name) +" "+
                (street == null ? "" :street.name);
        tvAddress.setText(s);
        dialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.personal_back:
                finish();
                break;
            case R.id.tvAddress:
                dialog = new BottomDialog(PersonalSetInfo.this);
                dialog.setOnAddressSelectedListener(PersonalSetInfo.this);
                dialog.show();
                break;
        }
    }
}
