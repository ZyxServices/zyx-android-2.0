package com.tiyujia.homesport.common.personal.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.entity.JsonCallback;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.entity.LzyResponse;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者: Cymbi on 2016/11/22 18:02.
 * 邮箱:928902646@qq.com
 */

public class PersonalForgetRegister extends ImmersiveActivity implements View.OnClickListener{
    @Bind(R.id.ivBack)   ImageView ivBack;
    @Bind(R.id.tvProtocol)   TextView tvProtocol;
    @Bind(R.id.tvSucceed)   TextView tvSucceed;
    @Bind(R.id.tvVerCode)   TextView tvVerCode;
    @Bind(R.id.etPassword)   EditText etPassword;
    @Bind(R.id.etVerCode)   EditText etVerCode;
    @Bind(R.id.etPhone)   EditText etPhone;
    private int time=60;
    Handler handler = new Handler() {
        @Override public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (time >= 0) {
                        tvVerCode.setText(time + " 秒后重试");
                        time--;
                        tvVerCode.setClickable(false);
                        tvVerCode.setTextColor(getResources().getColor(R.color.day_edit_hit_color));
                        sendEmptyMessageDelayed(1, 1000);
                    } else {
                        removeMessages(1);
                        time = 60;
                        tvVerCode.setClickable(true);
                        tvVerCode.setText("获取验证码");
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_register_activcity);
        setView();
    }

    private void setView() {
        ivBack.setOnClickListener(this);
        tvSucceed.setOnClickListener(this);
        tvVerCode.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivBack:
                finish();
                break;
            case R.id.tvVerCode:
                final String phone = etPhone.getText().toString();
                if(TextUtils.isEmpty(phone)){
                    showToast("手机号不能为空");
                }else{ OkGo.post(API.BASE_URL+"/v2/sendCode")
                        .tag(this)
                        .params("phone",phone)
                        .execute(new LoadCallback<LzyResponse>(this) {
                            @Override
                            public void onSuccess(LzyResponse forgetModel, Call call, Response response) {
                                if(forgetModel.state==200){
                                    showToast("发送验证码");
                                    handler.sendEmptyMessage(1);
                                }
                            }
                        });}
                break;
            case R.id.tvSucceed:
                final  String Phone = etPhone.getText().toString();
                final String VerCode = etVerCode.getText().toString();
                final String Password = etPassword.getText().toString();
                if (TextUtils.isEmpty(Phone)){
                    showToast("手机号不能为空");
                }else if(TextUtils.isEmpty(VerCode)){
                    showToast("验证码不能为空");
                }else if(TextUtils.isEmpty(Password)){
                    showToast("新密码不能为空");
                }else {
                    OkGo.post(API.BASE_URL+"/v2/account/retrieve_secret")
                            .tag(this)
                            .params("phone",Phone)
                            .params("code",VerCode)
                            .params("pwd",Password)
                            .execute(new LoadCallback<LzyResponse>(PersonalForgetRegister.this) {
                                @Override
                                public void onSuccess(LzyResponse lzyResponse, Call call, Response response) {
                                    if (lzyResponse.state==200){
                                        showToast("密码修改成功");
                                        startActivity(new Intent(PersonalForgetRegister.this,PersonalLogin.class));
                                        finish();
                                    }else if (lzyResponse.state==40011){
                                        showToast("验证码错误");
                                    }
                                }
                                @Override
                                public void onError(Call call, Response response, Exception e) {
                                    super.onError(call, response, e);
                                    showToast("网络连接错误");
                                }
                            });
                }
                break;
        }
    }
}
