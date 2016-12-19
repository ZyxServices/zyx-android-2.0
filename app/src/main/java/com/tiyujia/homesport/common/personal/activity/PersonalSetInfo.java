package com.tiyujia.homesport.common.personal.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;
import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.personal.model.AvatarModel;
import com.tiyujia.homesport.common.personal.model.UserInfoModel;
import com.tiyujia.homesport.entity.JsonCallback;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.entity.LzyResponse;
import com.tiyujia.homesport.util.DialogUtil;
import com.tiyujia.homesport.util.GetUtil;
import com.tiyujia.homesport.util.PicUtil;
import com.tiyujia.homesport.util.PicassoUtil;
import com.tiyujia.homesport.util.PostUtil;
import com.tiyujia.homesport.util.StorePhotosUtil;
import com.tiyujia.homesport.util.StringUtil;
import com.tiyujia.homesport.util.UploadUtil;
import com.tiyujia.homesport.widget.TimePicker.WheelView;
import com.tiyujia.homesport.widget.TimePicker.wheeladapter.NumericWheelAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
    @Bind(R.id.tvBirthday)  TextView tvBirthday;
    @Bind(R.id.tvSex)  TextView tvSex;
    @Bind(R.id.personal_back)    ImageView personal_back;
    @Bind(R.id.ivAvatar)    ImageView ivAvatar;
    @Bind(R.id.etNickName)  EditText etNickName;
    @Bind(R.id.etSignature)  EditText etSignature;
    private String mToken;
    private int mUserId;
    private BottomDialog dialog;
    private Dialog cameradialog;
    private TextView camera,cancel,gallery;
    private String fileName;
    private final int PIC_FROM_CAMERA = 1;
    private Bitmap bitmap;
    private String pickaddress;
    private WheelView day,year,month;

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
        ivAvatar.setOnClickListener(this);
        tvAddress.setOnClickListener(this);
        tvBirthday.setOnClickListener(this);
        tvSex.setOnClickListener(this);
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
                            String sex=userInfoModel.data.sex;
                            long birthday=userInfoModel.data.birthday;
                            String address=userInfoModel.data.address;
                            String signature=userInfoModel.data.signature;
                            etNickName.setText(nickname);
                            if(!TextUtils.isEmpty(signature)){
                                etSignature.setText(signature);
                            }else {
                                etSignature.setText("输入您的签名");
                            }
                            if(!TextUtils.isEmpty(sex)){
                                if(sex.equals("1")){
                                    tvSex.setText("男");
                                }else if(sex.equals("0")){
                                    tvSex.setText("女");
                                }
                            }else {
                                tvSex.setText("您的性别");
                            }
                            if(!TextUtils.isEmpty(address)){
                                tvAddress.setText(address);
                            }else {
                                tvAddress.setText("您的所在地");
                            }
                            if(birthday!=0){
                                String s= API.simpleDateFormat.format(birthday);
                                String birthdays =s.substring(0,10);
                                tvBirthday.setText(birthdays);
                            }else {
                                tvBirthday.setText("您的生日");
                            }
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
        final String address =(province == null ? "" : province.name) +" "+
                (city == null ? "" :  city.name) +" "+
                (county == null ? "" :  county.name) +" "+
                (street == null ? "" :street.name);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String uri= API.BASE_URL+"/v2/user/info";
                    Map<String, String> params=new HashMap<>();
                    params.put("token",mToken);
                    params.put("account_id",mUserId+"");
                    params.put("address",address);
                    String result= PostUtil.sendPostMessage(uri,params);
                    Log.e("result",result);
                    JSONObject json =  new JSONObject(result);
                    if(json.getInt("state")==200){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvAddress.setText(address);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        dialog.dismiss();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.personal_back:
                String nickname=etNickName.getText().toString();
                String signtrue=etSignature.getText().toString();
                OkGo.post(API.BASE_URL+"/v2/user/info")
                        .tag(this)
                        .params("token",mToken)
                        .params("account_id",mUserId)
                        .params("nickname",nickname)
                        .params("signature",signtrue)
                        .execute(new LoadCallback<LzyResponse>(this) {
                            @Override
                            public void onSuccess(LzyResponse lzyResponse, Call call, Response response) {
                                if(lzyResponse.state==200){
                                    finish();
                                }
                            }
                        });
                break;
            case R.id.tvAddress:
                dialog = new BottomDialog(PersonalSetInfo.this);
                dialog.setOnAddressSelectedListener(PersonalSetInfo.this);
                dialog.show();
                break;
            case R.id.ivAvatar:
                showDialogs();
                break;
            case R.id.tvBirthday:
                showDateDialog();
                break;
            case R.id.tvSex:
                AlertDialog.Builder builder = new AlertDialog.Builder(PersonalSetInfo.this);
                builder.setTitle("请选择性别");
                final String[] sexList = {"女", "男"};
                //    设置一个单项选择下拉框
                /**
                 * 第一个参数指定我们要显示的一组下拉单选框的数据集合
                 * 第二个参数代表索引，指定默认哪一个单选框被勾选上
                 * 第三个参数给每一个单选项绑定一个监听器
                 */
                builder.setSingleChoiceItems(sexList, 0, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, final int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String uri= API.BASE_URL+"/v2/user/info";
                                    Map<String, String> params=new HashMap<>();
                                    params.put("token",mToken);
                                    params.put("account_id",mUserId+"");
                                    params.put("sex",which+"");
                                    String result= PostUtil.sendPostMessage(uri,params);
                                    Log.e("result",result);
                                    JSONObject json =  new JSONObject(result);
                                    if(json.getInt("state")==200){
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                tvSex.setText(sexList[which]);
                                                showToast("您的性别是:"+sexList[which]);
                                            }
                                        });
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            String nickname=etNickName.getText().toString();
            String signtrue=etSignature.getText().toString();
            OkGo.post(API.BASE_URL+"/v2/user/info")
                    .tag(this)
                    .params("token",mToken)
                    .params("account_id",mUserId)
                    .params("nickname",nickname)
                    .params("signature",signtrue)
                    .execute(new LoadCallback<LzyResponse>(this) {
                        @Override
                        public void onSuccess(LzyResponse lzyResponse, Call call, Response response) {
                            if(lzyResponse.state==200){
                                finish();
                            }
                        }
                    });
        }
        return super.onKeyDown(keyCode, event);
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
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(PersonalSetInfo.this, Manifest.permission.CAMERA);
                    if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(PersonalSetInfo.this,new String[]{Manifest.permission.CAMERA},222);
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
                /*else {
                    fileName = getPhotopath();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                    File out = new File(fileName);
                    Uri uri = Uri.fromFile(out);
                    // 获取拍照后未压缩的原图片，并保存在uri路径中
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(intent,PIC_FROM_CAMERA);
                    cameradialog.dismiss();
                }*/
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
                        .execute(new LoadCallback<LzyResponse<AvatarModel>>(PersonalSetInfo.this) {
                            @Override
                            public void onSuccess(LzyResponse<AvatarModel> avatar, Call call, Response response) {
                                if(avatar.state==200){
                                    String newUrl = avatar.data.url;
                                    OkGo.post(API.BASE_URL+"/v2/user/info")
                                            .tag(this)
                                            .params("token",mToken)
                                            .params("account_id",mUserId)
                                            .params("avatar",newUrl)
                                            .execute(new LoadCallback<LzyResponse>(PersonalSetInfo.this) {
                                                @Override
                                                public void onSuccess(LzyResponse lzyResponse, Call call, Response response) {
                                                    if (lzyResponse.state==200){
                                                        showToast("头像修改成功");
                                                    }
                                                }
                                                @Override
                                                public void onError(Call call, Response response, Exception e) {
                                                    super.onError(call, response, e);
                                                    showToast("网络故障");
                                                }
                                            });
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
                bitmap= PicUtil.compress(bitmap1, 720, 480);
                ivAvatar.setImageBitmap(bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String path= UploadUtil.getImageAbsolutePath(PersonalSetInfo.this,originalUri);
                    pickaddress=UploadUtil.getNetWorkImageAddress(path, PersonalSetInfo.this);
                    if(pickaddress!=null){
                        String url=API.BASE_URL+"/v2/user/info";
                        HashMap<String,String> map=new HashMap<>();
                        map.put("token",mToken);
                        map.put("account_id",mUserId+"");
                        map.put("avatar",pickaddress);
                        String result=PostUtil.sendPostMessage(url,map);
                        try {
                            final JSONObject   obj = new JSONObject(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }) .start();
        }
    }

    /**
     * @param year
     * @param month
     * @return
     */
    private int getDay(int year, int month) {
        int day = 30;
        boolean flag = false;
        switch (year % 4) {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                day = flag ? 29 : 28;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }
    /**
     * 初始化年
     */
    private void initYear(int curYear) {
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(this,1950, curYear);
        numericWheelAdapter.setLabel(" 年");
        //		numericWheelAdapter.setTextSize(15);  设置字体大小
        year.setViewAdapter(numericWheelAdapter);
        year.setCyclic(true);
    }
    /**
     * 初始化月
     */
    private void initMonth() {
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(this,1, 12, "%02d");
        numericWheelAdapter.setLabel(" 月");
        //		numericWheelAdapter.setTextSize(15);  设置字体大小
        month.setViewAdapter(numericWheelAdapter);
        month.setCyclic(true);
    }
    /**
     * 初始化天
     */
    private void initDay(int arg1, int arg2) {
        NumericWheelAdapter numericWheelAdapter=new NumericWheelAdapter(this,1, getDay(arg1, arg2), "%02d");
        numericWheelAdapter.setLabel(" 日");
        //		numericWheelAdapter.setTextSize(15);  设置字体大小
        day.setViewAdapter(numericWheelAdapter);
        day.setCyclic(true);
    }
    /**
     * 显示日期
     */
    private void showDateDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(PersonalSetInfo.this)
                .create();
        dialog.show();
        Window window = dialog.getWindow();
        // 设置布局
        window.setContentView(R.layout.datapick);
        // 设置宽高
        //   window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        // 设置弹出的动画效果
        window.setWindowAnimations(R.style.log_out_dialog);
        dialog.setCanceledOnTouchOutside(true);
        Calendar c = Calendar.getInstance();
        int curYear = c.get(Calendar.YEAR);
        int curMonth = c.get(Calendar.MONTH) + 1;//通过Calendar算出的月数要+1
        int curDate = c.get(Calendar.DATE);
            year = (WheelView) window.findViewById(R.id.year);
        initYear(curYear);
             month = (WheelView) window.findViewById(R.id.month);
        initMonth();
             day = (WheelView) window.findViewById(R.id.day);
        initDay(curYear,curMonth);
        year.setCurrentItem(curYear - 1950);
        month.setCurrentItem(curMonth - 1);
        day.setCurrentItem(curDate - 1);
        year.setVisibleItems(7);
        month.setVisibleItems(7);
        day.setVisibleItems(7);
        // 设置监听
        Button ok = (Button) window.findViewById(R.id.set);
        Button cancel = (Button) window.findViewById(R.id.cancel);
        ok.setOnClickListener(new View.OnClickListener() {
            public String str;
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            str = (year.getCurrentItem()+1950) + "-"+ (month.getCurrentItem()+1)+"-"+(day.getCurrentItem()+1);
                            Log.e("str",str);
                            String uri= API.BASE_URL+"/v2/user/info";
                            Map<String, String> params=new HashMap<>();
                            SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd");
                            Date date=simpleDateFormat.parse(str);
                            Long birthday_time= date.getTime();
                            Log.e("birthday_time",birthday_time+"");
                            params.put("token",mToken);
                            params.put("account_id",mUserId+"");
                            params.put("birthday",birthday_time+"");
                            String result= PostUtil.sendPostMessage(uri,params);
                            JSONObject json =  new JSONObject(result);
                            if(json.getInt("state")==200){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tvBirthday.setText(str);
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                dialog.cancel();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }

}
