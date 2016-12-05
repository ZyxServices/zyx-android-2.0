package com.tiyujia.homesport.common.personal.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.personal.model.UserInfoModel;
import com.tiyujia.homesport.entity.JsonCallback;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.util.GetUtil;
import com.tiyujia.homesport.util.PicUtil;
import com.tiyujia.homesport.util.PicassoUtil;
import com.tiyujia.homesport.util.PostUtil;
import com.tiyujia.homesport.util.StorePhotosUtil;
import com.tiyujia.homesport.util.StringUtil;
import com.tiyujia.homesport.util.UploadUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    private String picAddress=null;

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
                finish();
                break;
            case R.id.tvAddress:
                dialog = new BottomDialog(PersonalSetInfo.this);
                dialog.setOnAddressSelectedListener(PersonalSetInfo.this);
                dialog.show();
                break;
            case R.id.ivAvatar:
                showDialogs();
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
                } else {
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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String uri= API.BASE_URL+"/v2/upload";
                            String result=UploadUtil.uploadFile2(uri, fileName);
                            JSONObject object= null;
                            object = new JSONObject(result);
                            JSONObject data=object.getJSONObject("data");
                            String newUrl = URI.create(data.getString("url")).getPath();
                            HashMap<String, String> params = new HashMap<>();
                            params.put("avatar", newUrl);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
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
            Thread thread1=new Thread(){
                @Override
                public void run() {
                    String path= UploadUtil.getImageAbsolutePath(PersonalSetInfo.this,originalUri);
                    picAddress=UploadUtil.getNetWorkImageAddress(path, PersonalSetInfo.this);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (picAddress!=null){
                                cameradialog.dismiss();
                            }
                        }
                    });
                }
            };
            thread1.setPriority(8);
            thread1.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
}
