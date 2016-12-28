package com.tiyujia.homesport.common.personal.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.HomeActivity;
import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.personal.model.AvatarModel;
import com.tiyujia.homesport.common.personal.model.LoginInfoModel;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.entity.LoginCallback;
import com.tiyujia.homesport.entity.LzyResponse;
import com.tiyujia.homesport.entity.Result;
import com.tiyujia.homesport.entity.UserService;
import com.tiyujia.homesport.entity.UserServiceImpl;
import com.tiyujia.homesport.entity.VerifyCode;
import com.tiyujia.homesport.util.PicUtil;
import com.tiyujia.homesport.util.PostUtil;
import com.tiyujia.homesport.util.StorePhotosUtil;
import com.tiyujia.homesport.util.UploadUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;
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
    private final int PIC_FROM_CAMERA = 1;
    private String fileName;
    private String phone,pwd;
    private Dialog cameradialog;
    private TextView camera,cancel,gallery;
    int time = 60;
    private Bitmap bitmap;
    private String avatar=null;
    UserService userService;
    Handler handler = new Handler() {
        @Override public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (time >= 0) {
                        tvVerCode.setText(time + " 秒后重试");
                        time--;
                        tvVerCode.setClickable(false);
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
        ivAvatar.setOnClickListener(this);
        tvProtocol.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivBack:
                finish();
                break;
            case R.id.tvProtocol:
                Intent a=new Intent(this,ProtocolAcitvity.class);
                a.putExtra("title","趣攀岩软件许可协议");
                a.putExtra("Url","/phone/agreement/permit");
                startActivity(a);
                break;
            case R.id.ivAvatar:
                showDialogs();
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
                                        Log.e("验证码:",""+result.state);
                                    }else
                                    if (result.state == 500) {
                                        showToast("服务器无响应");
                                        tvRegister.setClickable(true);
                                    }else if(result.state==40100){
                                        showToast("手机号码格式错误");
                                        Log.e("VerifyCode==","手机号码格式错误");
                                    }
                                }
                            });
                    mCompositeSubscription.add(s);
                    tvVerCode.setClickable(true);
                    handler.sendEmptyMessage(1);
                }
                break;
            case  R.id.tvRegister:
                String nickname= etNickName.getText().toString();
                if(nickname.equals("")||nickname==null){
                    showToast("昵称不能为空");
                }else {
                    if(TextUtils.isEmpty(avatar)){
                        OkGo.post(API.BASE_URL+"/v2/account/register2")
                                .params("phone",phone)
                                .params("pwd",pwd)
                                .params("nickname",nickname)
                                .execute(new LoadCallback<LzyResponse>(this) {
                                    @Override
                                    public void onSuccess(LzyResponse lzyResponse, Call call, Response response) {
                                        if(lzyResponse.state==200){
                                            showToast("注册成功");
                                            OkGo.post(API.BASE_URL+"/v2/account/log_in")
                                                    .tag(this)
                                                    .params("phone",phone)
                                                    .params("pwd",pwd)
                                                    .execute(new LoginCallback<LzyResponse<LoginInfoModel>>(PersonalRegister.this){
                                                        @Override
                                                        public void onSuccess(LzyResponse<LoginInfoModel> Response, Call call, Response response) {
                                                            if(Response.state==200){
                                                                SharedPreferences share = getSharedPreferences("UserInfo",MODE_PRIVATE);
                                                                SharedPreferences.Editor etr=share.edit();
                                                                etr.clear().apply();
                                                                String Token=Response.data.getToken().toString();
                                                                String Nickname=Response.data.getNickname().toString();
                                                                String Phone=Response.data.getPhone().toString();
                                                                int UserId=Response.data.getId();
                                                                etr.putString("Token",Token);
                                                                etr.putString("NickName",Nickname);
                                                                etr.putString("Phone",Phone);
                                                                etr.putInt("UserId",UserId);
                                                                etr.apply();
                                                                Intent i=new Intent(PersonalRegister.this, HomeActivity.class);
                                                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                startActivity(i);
                                                                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                                im.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                                                            }
                                                        }

                                                        @Override
                                                        public void onError(Call call, Response response, Exception e) {
                                                            super.onError(call, response, e);
                                                            showToast("onError");
                                                        }
                                                    });
                                        }
                                    }
                                });
                    }else {
                        OkGo.post(API.BASE_URL+"/v2/account/register2")
                            .params("phone",phone)
                            .params("pwd",pwd)
                            .params("nickname",nickname)
                            .params("avatar",avatar)
                            .execute(new LoadCallback<LzyResponse>(this) {
                                @Override
                                public void onSuccess(LzyResponse lzyResponse, Call call, Response response) {
                                    if(lzyResponse.state==200){
                                        showToast("注册成功");
                                        OkGo.post(API.BASE_URL+"/v2/account/log_in")
                                                .tag(this)
                                                .params("phone",phone)
                                                .params("pwd",pwd)
                                                .execute(new LoginCallback<LzyResponse<LoginInfoModel>>(PersonalRegister.this){
                                                    @Override
                                                    public void onSuccess(LzyResponse<LoginInfoModel> Response, Call call, Response response) {
                                                        if(Response.state==200){
                                                            SharedPreferences share = getSharedPreferences("UserInfo",MODE_PRIVATE);
                                                            SharedPreferences.Editor etr=share.edit();
                                                            etr.clear().apply();
                                                            String Token=Response.data.getToken().toString();
                                                            String Nickname=Response.data.getNickname().toString();
                                                            String Phone=Response.data.getPhone().toString();
                                                            int UserId=Response.data.getId();
                                                            etr.putString("Token",Token);
                                                            etr.putString("NickName",Nickname);
                                                            etr.putString("Phone",Phone);
                                                            etr.putInt("UserId",UserId);
                                                            etr.apply();
                                                            Intent i=new Intent(PersonalRegister.this, HomeActivity.class);
                                                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(i);
                                                            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                            im.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                                                        }
                                                    }

                                                    @Override
                                                    public void onError(Call call, Response response, Exception e) {
                                                        super.onError(call, response, e);
                                                        showToast("onError");
                                                    }
                                                });
                                    }
                                }
                            });}

                }


                break;
        }
    }
    private void showDialogs() {
        View view = getLayoutInflater().inflate(R.layout.dialog_photo, null);
        cameradialog = new Dialog(this,R.style.Dialog_Fullscreen);
        camera=(TextView)view.findViewById(R.id.camera);
        gallery=(TextView)view.findViewById(R.id.gallery);
        cancel=(TextView)view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameradialog.dismiss();
            }
        });
        //从相册获取
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 1000);
                cameradialog.dismiss();
            }
        });
        //拍照
        camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(PersonalRegister.this, Manifest.permission.CAMERA);
                    if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(PersonalRegister.this,new String[]{Manifest.permission.CAMERA},222);
                        return;
                    }else{
                        fileName = getPhotopath();
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                        File out = new File(fileName);
                        Uri uri = Uri.fromFile(out);
                        // 获取拍照后未压缩的原图片，并保存在uri路径中
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(intent,PIC_FROM_CAMERA);
                        cameradialog.dismiss();
                    }
                }
            }
        });
        cameradialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = cameradialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        cameradialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        cameradialog.setCanceledOnTouchOutside(true);
        cameradialog.show();
    }
    /**
     * 路径
     * @return
     */
    private String getPhotopath() {
        // 照片全路径
        String fileName = "";
        // 文件夹路径
        String pathUrl = Environment.getExternalStorageDirectory()+"/Zyx/";
        //照片名
        String name = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".png";
        File file = new File(pathUrl);
        if (!file.exists()) {
            Log.e("TAG", "第一次创建文件夹");
            file.mkdirs();// 如果文件夹不存在，则创建文件夹
        }
        fileName=pathUrl+name;
        return fileName;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PIC_FROM_CAMERA&&resultCode == Activity.RESULT_OK) {
            final Bitmap bitmap = PicUtil.getSmallBitmap(fileName);
            // 这里是先压缩质量，再调用存储方法
            new StorePhotosUtil(bitmap, fileName);
            ivAvatar.setImageBitmap(bitmap);
            if (bitmap!=null) {
                OkGo.post(API.IMAGE_URL)
                        .tag(this)
                        .params("avatar",new File(fileName))
                        .execute(new LoadCallback<LzyResponse<AvatarModel>>(PersonalRegister.this) {
                            @Override
                            public void onSuccess(LzyResponse<AvatarModel> avatars, Call call, Response response) {
                                if(avatars.state==200){
                                    avatar = avatars.data.url;
                                }else {
                                    showToast("服务器异常");
                                }
                            }
                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                super.onError(call, response, e);
                                showToast("网络连接错误");
                            }
                        });
            }
        }
        if (requestCode == 1000  &&  data != null){
            // 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
            final ContentResolver resolver = getContentResolver();
            final   Uri originalUri = data.getData(); // 获得图片的uri
            try {
                Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                bitmap= PicUtil.compress(bitmap1, 320, 320);
                ivAvatar.setImageBitmap(bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String path= UploadUtil.getImageAbsolutePath(PersonalRegister.this,originalUri);
                    avatar=UploadUtil.getNetWorkImageAddress(path, PersonalRegister.this);
                }
            }) .start();
        }
    }
}
