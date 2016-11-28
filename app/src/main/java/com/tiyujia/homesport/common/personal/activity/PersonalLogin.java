package com.tiyujia.homesport.common.personal.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.HomeActivity;
import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.personal.model.LoginInfoModel;
import com.tiyujia.homesport.entity.LoginCallback;
import com.tiyujia.homesport.entity.LzyResponse;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者: Cymbi on 2016/11/22 16:00.
 * 邮箱:928902646@qq.com
 */

public class PersonalLogin extends ImmersiveActivity implements View.OnClickListener{
    @Bind(R.id.tvRegister)    TextView tvRegister;
    @Bind(R.id.tvLogin)    TextView tvLogin;
    @Bind(R.id.ivBack)    ImageView ivBack;
    @Bind(R.id.etPhone)    EditText etPhone;
    @Bind(R.id.etPassword)    EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        setView();
    }

    private void setView() {
        ivBack.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        tvLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvRegister:
                startActivity(new Intent(this,PersonalRegister.class));
                break;
            case R.id.ivBack:
                finish();
                break;
            case R.id.tvLogin:
                final String phone=etPhone.getText().toString();
                final String pwd=etPassword.getText().toString();
                if(!TextUtils.isEmpty(phone)){
                    if(!TextUtils.isEmpty(pwd)){
                        OkGo.post(API.BASE_URL+"/v2/account/log_in")
                                .tag(this)
                                .params("phone",phone)
                                .params("pwd",pwd)
                                .execute(new LoginCallback<LzyResponse<LoginInfoModel>>(this){
                                    @Override
                                    public void onSuccess(LzyResponse<LoginInfoModel> Response, Call call, Response response) {
                                        if(Response.state==200){
                                            SharedPreferences share = getSharedPreferences("UserInfo",MODE_PRIVATE);
                                            SharedPreferences.Editor etr=share.edit();
                                            etr.clear().apply();
                                            String Token=Response.data.getToken().toString();
                                            String Nickname=Response.data.getNickname().toString();
                                            String Phone=Response.data.getPhone().toString();
                                          //  String Avatar=Response.data.getAvatar().toString();
                                            int UserId=Response.data.getId();
                                            etr.putString("Token",Token);
                                            etr.putString("NickName",Nickname);
                                            etr.putString("Phone",Phone);
                                           // etr.putString("Avatar",Avatar);
                                            etr.putInt("UserId",UserId);
                                            etr.apply();
                                            Intent i=new Intent(PersonalLogin.this, HomeActivity.class);
                                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(i);
                                            showToast("登录成功");
                                            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                            im.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onError(Call call, Response response, Exception e) {
                                        super.onError(call, response, e);
                                        showToast("onError");
                                    }
                                });
                    }else {showToast("密码不能为空");}
                }else { showToast("账号不能为空");}



                break;
        }
    }
}
