package com.tiyujia.homesport.common.homepage.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.community.activity.CommunityDynamicPublish;
import com.tiyujia.homesport.common.personal.activity.PersonalSetInfo;
import com.tiyujia.homesport.util.PicUtil;
import com.tiyujia.homesport.util.StorePhotosUtil;
import com.tiyujia.homesport.util.UploadUtil;
import com.tiyujia.homesport.widget.GlideImageLoader;
import com.tiyujia.homesport.widget.ImagePickerAdapter;
import com.tiyujia.homesport.widget.picker.SlideDateTimeListener;
import com.tiyujia.homesport.widget.picker.SlideDateTimePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者: Cymbi on 2016/11/18 15:22.
 * 邮箱:928902646@qq.com
 */

public class HomePageActivePublishActivity extends ImmersiveActivity implements View.OnClickListener,ImagePickerAdapter.OnRecyclerViewItemClickListener{
    @Bind(R.id.personal_back)    ImageView personal_back;
    @Bind(R.id.tvPush)    TextView tvPush;
    @Bind(R.id.tvUploadImage)    TextView tvUploadImage;
    @Bind(R.id.tvStartTime)    TextView tvStartTime;
    @Bind(R.id.tvEndTime)    TextView tvEndTime;
    @Bind(R.id.tvApplyEndTime)    TextView tvApplyEndTime;
    @Bind(R.id.myRadioGroup)    RadioGroup myRadioGroup;
    @Bind(R.id.rbDate)    RadioButton rbDate;
    @Bind(R.id.rbLead)    RadioButton rbLead;
    @Bind(R.id.ivBackground)    ImageView ivBackground;
    @Bind(R.id.reStartTime)    RelativeLayout reStartTime;
    @Bind(R.id.reEndTime)    RelativeLayout reEndTime;
    @Bind(R.id.reApplyEndTiem)    RelativeLayout reApplyEndTiem;
    @Bind(R.id.revImage)    RecyclerView revImage;
    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private int type=0;//活动类型(1、求约 2、求带)
    private Dialog cameradialog;
    private String fileName;
    private final int PIC_FROM_CAMERA = 1;
    private Bitmap bitmap;
    private String  imageUrl;//封面图片
    private List<String > descimage;//内容图片
    private String picAddress;
    private int maxPeople=0;//报名人数限制(0 为不限制)
    private int paymentType=0;//付费类型(0奖励 1免费 2 AA)
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 9;               //允许选择图片最大数
    private ArrayList<ImageItem> images;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_active_publish);
        ButterKnife.bind(this);
        initview();
        initImagePicker();
        initWidget();
    }

    private void initWidget() {
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);
        revImage.setLayoutManager(new GridLayoutManager(this, 4));
        revImage.setHasFixedSize(true);
        revImage.setAdapter(adapter);
    }

    private void initview() {
        personal_back.setOnClickListener(this);
        tvPush.setOnClickListener(this);
        tvUploadImage.setOnClickListener(this);
        ivBackground.setOnClickListener(this);
        reStartTime.setOnClickListener(this);
        reEndTime.setOnClickListener(this);
        reApplyEndTiem.setOnClickListener(this);

        if(!myRadioGroup.isClickable()){
        type=0;
        }else{}
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(false);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(maxImgCount);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }
    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                //打开选择,本次允许选择的数量
                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                Intent intent = new Intent(HomePageActivePublishActivity.this, ImageGridActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SELECT);
                break;
            default:
                //打开预览
                Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.personal_back:
                finish();
                break;
            case  R.id.tvPush:

                break;
            case R.id.tvUploadImage:
                showDialogs();
                break;
            case R.id.ivBackground:
                showDialogs();
                break;
            case R.id.reStartTime:
                new SlideDateTimePicker.Builder(getSupportFragmentManager())
                        .setListener(start)
                        .setInitialDate(new Date())
                        .setIs24HourTime(true)
                        .build()
                        .show();
                break;
            case R.id.reEndTime:
                new SlideDateTimePicker.Builder(getSupportFragmentManager())
                        .setListener(end)
                        .setInitialDate(new Date())
                        .setIs24HourTime(true)
                        .build()
                        .show();
                break;
            case R.id.reApplyEndTiem:
                new SlideDateTimePicker.Builder(getSupportFragmentManager())
                        .setListener(applyend)
                        .setInitialDate(new Date())
                        .setIs24HourTime(true)
                        .build()
                        .show();
                break;

        }
    }
    //开始时间选择器的确定和取消按钮的返回操作
    SlideDateTimeListener start = new SlideDateTimeListener() {
        @Override
        public void onDateTimeSet(Date date){
            tvStartTime.setText(mFormatter.format(date) );
        }
        // Optional cancel listener
        @Override
        public void onDateTimeCancel() {

        }
    };
    //开始时间选择器的确定和取消按钮的返回操作
    SlideDateTimeListener applyend = new SlideDateTimeListener() {
        @Override
        public void onDateTimeSet(Date date){
            tvApplyEndTime.setText(mFormatter.format(date) );
        }
        // Optional cancel listener
        @Override
        public void onDateTimeCancel() {

        }
    };
    //开始时间选择器的确定和取消按钮的返回操作
    SlideDateTimeListener end = new SlideDateTimeListener() {
        @Override
        public void onDateTimeSet(Date date){
            tvEndTime.setText(mFormatter.format(date) );
        }
        // Optional cancel listener
        @Override
        public void onDateTimeCancel() {

        }
    };
    private void showDialogs() {
        View view = getLayoutInflater().inflate(R.layout.dialog_photo, null);
        cameradialog = new Dialog(this,R.style.Dialog_Fullscreen);
        TextView camera = (TextView) view.findViewById(R.id.camera);
        TextView gallery=(TextView)view.findViewById(R.id.gallery);
        TextView cancel=(TextView)view.findViewById(R.id.cancel);
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
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(HomePageActivePublishActivity.this, Manifest.permission.CAMERA);
                    if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(HomePageActivePublishActivity.this,new String[]{Manifest.permission.CAMERA},222);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PIC_FROM_CAMERA&&resultCode == Activity.RESULT_OK) {
            final Bitmap bitmap = PicUtil.getSmallBitmap(fileName);
            // 这里是先压缩质量，再调用存储方法
            new StorePhotosUtil(bitmap, fileName);
            ivBackground.setImageBitmap(bitmap);
            tvUploadImage.setVisibility(View.GONE);
            ivBackground.setVisibility(View.VISIBLE);
            if (bitmap!=null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String uri= API.IMAGE_URL;
                            String result= UploadUtil.uploadFile2(uri, fileName);
                            JSONObject object= null;
                            object = new JSONObject(result);
                            JSONObject data=object.getJSONObject("data");
                            String newUrl = URI.create(data.getString("url")).getPath();
                            imageUrl=newUrl;
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
                ivBackground.setImageBitmap(bitmap);
                tvUploadImage.setVisibility(View.GONE);
                ivBackground.setVisibility(View.VISIBLE);
            }catch (Exception e){
                e.printStackTrace();
            }
            Thread thread1=new Thread(){
                @Override
                public void run() {
                    String path= UploadUtil.getImageAbsolutePath(HomePageActivePublishActivity.this,originalUri);
                    imageUrl=UploadUtil.getNetWorkImageAddress(path, HomePageActivePublishActivity.this);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (imageUrl!=null){
                                cameradialog.dismiss();
                            }
                        }
                    });
                }
            };
            thread1.setPriority(8);
            thread1.start();
        }
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                selImageList.addAll(images);
                adapter.setImages(selImageList);
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                selImageList.clear();
                selImageList.addAll(images);
                adapter.setImages(selImageList);
            }
        }
    }
}
