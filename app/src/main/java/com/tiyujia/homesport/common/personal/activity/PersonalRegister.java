package com.tiyujia.homesport.common.personal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tiyujia.homesport.HomeActivity;
import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.entity.Result;
import com.tiyujia.homesport.entity.UserService;
import com.tiyujia.homesport.entity.UserServiceImpl;
import com.tiyujia.homesport.entity.VerifyCode;

import butterknife.Bind;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者: Cymbi on 2016/11/22 18:02.
 * 邮箱:928902646@qq.com
 */

public class PersonalRegister extends ImmersiveActivity implements View.OnClickListener{
    @Bind(R.id.ivBack)   ImageView ivBack;
    @Bind(R.id.tvProtocol)   TextView tvProtocol;
    @Bind(R.id.tvRegister)   TextView tvRegister;
    @Bind(R.id.tvNext)   TextView tvNext;
    @Bind(R.id.tvTitle)   TextView tvTitle;
    @Bind(R.id.tvVerCode)   TextView tvVerCode;
    @Bind(R.id.etPassword)   EditText etPassword;
    @Bind(R.id.etVerCode)   EditText etVerCode;
    @Bind(R.id.etPhone)   EditText etPhone;
    @Bind(R.id.etNickName)   EditText etNickName;
    ImageView ivAvatar;
    @Bind(R.id.llBasic)   LinearLayout llBasic;
    @Bind(R.id.llPerfect) LinearLayout llPerfect;
    private String phone,pwd;
    int time = 60;
    UserService userService;
    Handler handler = new Handler() {
        @Override public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (time >= 0) {
                        tvVerCode.setText(time + " 秒后重试");
                        time--;
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
        userService=new UserServiceImpl();
        setContentView(R.layout.register_activcity);
        ivAvatar=(ImageView)findViewById(R.id.ivAvatar);
        llPerfect.setVisibility(View.GONE);
        tvTitle.setText("注册");
        tvRegister.setText("注册");
        setView();
    }

    private void setView() {
        ivBack.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        tvNext.setOnClickListener(this);
        tvVerCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivBack:
                finish();
                break;
            case R.id.tvNext:
                String verifyCode=etVerCode.getText().toString();
                pwd= etPassword.getText().toString();
                phone= etPhone.getText().toString();
                if (verifyCode != null && !"".equals(verifyCode)) {
                    if(pwd!=null&&!pwd.equals("")){
                    if (phone != null && !"".equals(phone)) {
                        Subscription s = userService.verifyPhone(phone, verifyCode).subscribeOn(Schedulers.io())//
                                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Result<VerifyCode>>() {
                                    @Override public void onCompleted() {
                                        Log.i("fldy", "===>:onCompleted");
                                    }
                                    @Override public void onError(Throwable e) {

                                    }
                                    @Override public void onNext(Result<VerifyCode> result) {
                                        if (result.state == 200) {
                                            llBasic.setVisibility(View.GONE);
                                            llPerfect.setVisibility(View.VISIBLE);
                                            tvTitle.setText("资料完善");
                                        }else if(result.state == 40005){
                                            showToast("该账号已注册");
                                        }else if(result.state == 40011){
                                            etVerCode.setError("验证码不匹配");
                                            etVerCode.requestFocus();
                                        }
                                    }
                                });
                        mCompositeSubscription.add(s);
                    } else {showToast("手机号不能为空");}
                    }else {showToast("请输入密码");}
                } else {
                    showToast("请输入验证码");
                }
                break;
            case R.id.tvVerCode:
                phone=etPhone.getText().toString();
                if(TextUtils.isEmpty(phone)){
                    showToast("请先输入手机号");
                }else {
                    Subscription s = userService.getVerifyCode(phone).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Result>() {
                                @Override public void onCompleted() {
                                }
                                @Override public void onError(Throwable e) {
                                    showToast("获取验证码失败");
                                }
                                @Override public void onNext(Result result) {
                                    if (result.state==50005){
                                        showToast("手机号已注册");
                                        tvRegister.setClickable(false);
                                        tvRegister.setBackgroundColor(getResources().getColor(R.color.background));
                                    }else
                                    if (result.state == 200) {
                                        showToast("验证码已发送");
                                        tvRegister.setClickable(true);
                                    }else
                                    if (result.state == 500) {
                                        showToast("服务器无响应");
                                        tvRegister.setClickable(true);
                                    }
                                }
                            });
                    mCompositeSubscription.add(s);
                    tvVerCode.setClickable(true);
                    handler.sendEmptyMessage(1);
                }
                break;
            case  R.id.tvRegister:
                String avatar  = null;
                String nickname= etNickName.getText().toString();
                if(nickname.equals("")||nickname==null){
                    showToast("昵称不能为空");
                }else {
                    Subscription s =userService.register(phone, pwd, nickname,avatar).subscribeOn(Schedulers.io())//
                                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Result>() {
                                @Override public void onCompleted() {
                                    showToast("完成");
                                }
                                @Override public void onError(Throwable e) {
                                    showToast("注册失败");
                                    Log.e("onError==>>", e.getMessage());
                                }
                                @Override public void onNext(Result result) {
                                    if (result.state == 200) {
                                        Intent intent = new Intent(PersonalRegister.this, HomeActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        showToast("注册成功");
                                    }
                                    if(result.state == 40005){
                                        showToast("手机号码已注册");
                                    }
                                }
                            });
                    mCompositeSubscription.add(s);
                }


                break;
        }
    }
}
