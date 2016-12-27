package com.tiyujia.homesport.common.community.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClient;
import com.bumptech.glide.Glide;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.lzy.okgo.OkGo;
import com.tencent.upload.Const;
import com.tencent.upload.UploadManager;
import com.tencent.upload.task.ITask;
import com.tencent.upload.task.IUploadTaskListener;
import com.tencent.upload.task.VideoAttr;
import com.tencent.upload.task.data.FileInfo;
import com.tencent.upload.task.impl.VideoUploadTask;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.HomeActivity;
import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.community.fragment.AttentionFragment;
import com.tiyujia.homesport.common.community.fragment.RecommendFragment;
import com.tiyujia.homesport.entity.ImageUploadModel;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.entity.LzyResponse;
import com.tiyujia.homesport.util.EmojiFilterUtil;
import com.tiyujia.homesport.util.GetSignTask;
import com.tiyujia.homesport.widget.GlideImageLoader;
import com.tiyujia.homesport.widget.ImagePickerAdapter;
import org.apache.commons.lang3.StringUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;


/**
 * 作者: Cymbi on 2016/11/18 14:22.1
 * 邮箱:928902646@qq.com
 */

public class CommunityDynamicPublish extends ImmersiveActivity implements ImagePickerAdapter.OnRecyclerViewItemClickListener,View.OnClickListener{
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    public static final int REQUEST_CODE_VIDEO = 102;
    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 9;               //允许选择图片最大数
    private Dialog dialog;
    private TextView tvVideo;
    private TextView tvGallery;
    @Bind(R.id.ivBack)  ImageView ivBack;
    @Bind(R.id.tvPush)  TextView tvPush;
    @Bind(R.id.tvIssueEdit)  TextView tvIssueEdit;
    @Bind(R.id.tvCity)  TextView tvCity;
    @Bind(R.id.etIssueContent)  EditText etIssueContent;
    @Bind(R.id.rlVideo)    RelativeLayout rlVideo;
    @Bind(R.id.framelayoutvideo)    FrameLayout framelayoutvideo;
    private ArrayList<ImageItem> images;
    private String mToken;
    private int mUserId;
    private String local="";
    private String videoPath;
    private String framePicPath;
    private String sign;
    private Integer recordId=null;
    private String appid;
    private String persistenceId = null;
    private ImageView imageviewvideo;
    private RecyclerView recyclerView;
    private String videoSaveName;
    private String bucket;
    private Handler mMainHandler = new Handler(Looper.getMainLooper());
    private UploadManager mFileUploadManager=null;
    private VideoUploadTask videoUploadTask=null;
    private FileInfo mCurrPhotoInfo;
    private ProgressDialog m_pDialog = null;
    private String VideoUrl=null;//服务器返回的网络视频地址


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_dynamic_publish);
        setInfo();
        initImagePicker();
        initWidget();
        appid = "299201";
        bucket = "vincentsu";
        // 去用户的业务务器获取签名
        GetSignTask task = new GetSignTask(this, appid, Const.FileType.Video, bucket,
                null, null,new GetSignTask.OnGetSignListener(){

            @Override
            public void onSign(String result) {
                // TODO Auto-generated method stub
                sign = result;
            }});
        task.execute();

        persistenceId = "videPersistenceId";
        // 1，创建一个上传容器 需要1.appid 2.上传文件类型3.上传缓存（类型字符串，要全局唯一否则）
        mFileUploadManager = new UploadManager(this, appid, Const.FileType.Video,persistenceId);
    }
    private void setInfo() {
        SharedPreferences share = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        mToken=share.getString("Token","");
        mUserId=share.getInt("UserId",0);
        local=share.getString("City","");
        tvCity.setText(local);
        recordId=getIntent().getIntExtra("recordId",0);
    }
    private void initWidget() {
        recyclerView = (RecyclerView) findViewById(R.id.revImage);
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        ivBack.setOnClickListener(this);
        tvPush.setOnClickListener(this);
        tvCity.setOnClickListener(this);
        framelayoutvideo.setOnClickListener(this);
        // 创建ProgressDialog对象
        m_pDialog = new ProgressDialog(CommunityDynamicPublish.this);
        m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_pDialog.setIndeterminate(false);
        m_pDialog.setCancelable(false);
        etIssueContent.addTextChangedListener(textWatcher);
    }
    TextWatcher textWatcher=new TextWatcher(){
        private CharSequence temp;
        private int editStart ;
        private int editEnd ;
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp=s;
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
        @Override
        public void afterTextChanged(Editable s) {
            editStart = etIssueContent.getSelectionStart();
            editEnd = etIssueContent.getSelectionEnd();
            tvIssueEdit.setText(temp.length()+"/"+"500");
            if (temp.length() > 500) {
                Toast.makeText(CommunityDynamicPublish.this,
                        "你输入的字数已经超过了限制！", Toast.LENGTH_SHORT)
                        .show();
                s.delete(editStart-1, editEnd);
                int tempSelection = editStart;
                etIssueContent.setText(s);
                etIssueContent.setSelection(tempSelection);
            }
        }
    };
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
                showDialogs();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
        }else if(requestCode==REQUEST_CODE_VIDEO&&resultCode==12){
            rlVideo.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            //视频地址
            videoPath = data.getStringExtra("videoPath");
            //封面图片地址
            framePicPath = data.getStringExtra("framePicPath");
            imageviewvideo = (ImageView) findViewById(R.id.imageviewvideo);
            Glide.with(CommunityDynamicPublish.this).load(framePicPath).error(R.drawable.jc_play_normal).into(imageviewvideo);
            upLoadVideo(true);
        }else if (requestCode==101&&resultCode==111){
            local=data.getStringExtra("cityTitle");
            tvCity.setText(local);
        }
    }
    private void showDialogs() {
        View view = getLayoutInflater().inflate(R.layout.dynamic_dialog, null);
        dialog = new Dialog(this,R.style.Dialog_Fullscreen);
        tvVideo=(TextView)view.findViewById(R.id.tvVideo);
        tvGallery=(TextView)view.findViewById(R.id.tvGallery);
        //从相册获取
        tvGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //打开选择,本次允许选择的数量
                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                Intent intent = new Intent(CommunityDynamicPublish.this, ImageGridActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SELECT);
                dialog.dismiss();
            }
        });
        //小视频
        tvVideo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(CommunityDynamicPublish.this, Manifest.permission.CAMERA);
                    if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(CommunityDynamicPublish.this,new String[]{Manifest.permission.CAMERA},222);
                        return;
                    }else{
                        Intent intent = new Intent(CommunityDynamicPublish.this, YWRecordVideoActivity.class);
                        startActivityForResult(intent,REQUEST_CODE_VIDEO);
                    }
                }
                dialog.dismiss();
            }
        });
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivBack:
                if (!TextUtils.isEmpty(videoPath)) {
                    File file = new File(videoPath);
                    file.delete();
                }
                if (!TextUtils.isEmpty(framePicPath)) {
                    File file = new File(framePicPath);
                    file.delete();
                }
                finish();
                break;
            case R.id.tvCity:
                Intent inten=new Intent(CommunityDynamicPublish.this,CityAddressSelect.class);
                startActivityForResult(inten,101);
                break;
            case R.id.tvPush:
                String tempText = etIssueContent.getText().toString().trim();
                final String content= EmojiFilterUtil.filterEmoji(this,tempText);
                ArrayList<File> files=new ArrayList<>();
                if(!TextUtils.isEmpty(content)){
                    if (images != null && images.size() > 0) {
                        for (int i = 0; i < images.size(); i++) {
                            files.add(new File(images.get(i).path));
                        }
                        OkGo.post(API.IMAGE_URLS)
                                .tag(this)
                                .addFileParams("avatars", files)
                                .execute(new LoadCallback<ImageUploadModel>(this) {
                                    @Override
                                    public void onSuccess(ImageUploadModel imageUploadModel, Call call, Response response) {
                                        List<String> da =imageUploadModel.data;
                                        String[] str = (String[])da.toArray(new String[da.size()]);
                                        String imgUrl=  StringUtils.join(str,",");
                                        if(recordId==null){
                                            OkGo.post(API.BASE_URL+"/v2/cern/insert")
                                                    .params("token",mToken)
                                                    .params("userId",mUserId)
                                                    .params("content",content)
                                                    .params("imgUrl",imgUrl)
                                                    .params("visible",0)
                                                    .params("local",local)
                                                    .execute(new LoadCallback<LzyResponse>(CommunityDynamicPublish.this) {
                                                        @Override
                                                        public void onSuccess(LzyResponse lzyResponse, Call call, Response response) {
                                                            if(lzyResponse.state==200){
                                                                showToast("发布成功");
                                                                AttentionFragment.isFirstIn=false;
                                                                RecommendFragment.isFirstIn=false;
                                                                finish();
                                                            }
                                                        }
                                                        @Override
                                                        public void onError(Call call, Response response, Exception e) {
                                                            super.onError(call, response, e);
                                                            showToast("失败");
                                                        }
                                                    });
                                        }else {
                                            OkGo.post(API.BASE_URL+"/v2/cern/insert")
                                                    .params("token",mToken)
                                                    .params("userId",mUserId)
                                                    .params("content",content)
                                                    .params("imgUrl",imgUrl)
                                                    .params("visible",0)
                                                    .params("local",local)
                                                    .params("recordId",recordId)
                                                    .execute(new LoadCallback<LzyResponse>(CommunityDynamicPublish.this) {
                                                        @Override
                                                        public void onSuccess(LzyResponse lzyResponse, Call call, Response response) {
                                                            if(lzyResponse.state==200){
                                                                showToast("发布成功");
                                                                finish();
                                                            }
                                                        }
                                                        @Override
                                                        public void onError(Call call, Response response, Exception e) {
                                                            super.onError(call, response, e);
                                                            showToast("失败");
                                                        }
                                                    });
                                        }

                                    }
                                    @Override
                                    public void onError(Call call, Response response, Exception e) {
                                        super.onError(call, response, e);
                                        showToast("onError");
                                    }
                                });
                    }else if(!TextUtils.isEmpty(VideoUrl)){
                        OkGo.post(API.BASE_URL+"/v2/cern/insert")
                                .params("token",mToken)
                                .params("userId",mUserId)
                                .params("content",content)
                                .params("visible",0)
                                .params("videoUrl",VideoUrl)
                                .params("local",local)
                                .execute(new LoadCallback<LzyResponse>(CommunityDynamicPublish.this) {
                                    @Override
                                    public void onSuccess(LzyResponse lzyResponse, Call call, Response response) {
                                        if(lzyResponse.state==200){
                                            showToast("发布成功");
                                            finish();
                                        }
                                    }
                                    @Override
                                    public void onError(Call call, Response response, Exception e) {
                                        super.onError(call, response, e);
                                        showToast("失败");
                                    }
                                });
                    }  else {  if(recordId==null){
                        OkGo.post(API.BASE_URL+"/v2/cern/insert")
                                .params("token",mToken)
                                .params("userId",mUserId)
                                .params("content",content)
                                .params("visible",0)
                                .params("local",local)
                                .execute(new LoadCallback<LzyResponse>(CommunityDynamicPublish.this) {
                                    @Override
                                    public void onSuccess(LzyResponse lzyResponse, Call call, Response response) {
                                        if(lzyResponse.state==200){
                                            showToast("发布成功");
                                            finish();
                                        }
                                    }
                                    @Override
                                    public void onError(Call call, Response response, Exception e) {
                                        super.onError(call, response, e);
                                        showToast("失败");
                                    }
                                });
                    }else{
                        OkGo.post(API.BASE_URL+"/v2/cern/insert")
                                .params("token",mToken)
                                .params("userId",mUserId)
                                .params("content",content)
                                .params("visible",0)
                                .params("local",local)
                                .params("recordId",recordId)
                                .execute(new LoadCallback<LzyResponse>(CommunityDynamicPublish.this) {
                                    @Override
                                    public void onSuccess(LzyResponse lzyResponse, Call call, Response response) {
                                        if(lzyResponse.state==200){
                                            showToast("发布成功");
                                            finish();
                                        }
                                    }
                                    @Override
                                    public void onError(Call call, Response response, Exception e) {
                                        super.onError(call, response, e);
                                        showToast("失败");
                                    }
                                });
                    }
                    }
                }else {
                    showToast("还没有填写内容哟~");
                }
                break;
            case R.id.framelayoutvideo:
                if (!TextUtils.isEmpty(videoPath)) {
                    Intent intent = new Intent(CommunityDynamicPublish.this, WidthMatchVideo.class);
                    intent.putExtra("videoPath", videoPath);
                    startActivity(intent);
                } else {
                    Toast.makeText(CommunityDynamicPublish.this, "视频路径无效", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Activity销毁时，取消网络请求
        OkGo.getInstance().cancelTag(this);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            //退出时删除录制的视频和图片
            if (!TextUtils.isEmpty(videoPath)) {
                File file = new File(videoPath);
                file.delete();
            }
            if (!TextUtils.isEmpty(framePicPath)) {
                File file = new File(framePicPath);
                file.delete();
            }

            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // 上传视频
    public void upLoadVideo(boolean to_over_write) {
        if(videoPath == null){
            Toast.makeText(this, "请选择文件", Toast.LENGTH_SHORT).show();
            return;
        }
        VideoAttr videoAttr = new VideoAttr();
        videoAttr.isCheck = false;
        videoAttr.title = "red-1";
        videoAttr.desc = "cos-video-desc" + videoPath;

        String[] strarray = videoPath.split("[/]");
        videoSaveName = strarray[strarray.length - 1];
        m_pDialog.show();
        // 构建要上传的任务
        videoUploadTask = new VideoUploadTask(bucket, videoPath, "/"+ videoSaveName, "", videoAttr, to_over_write,new IUploadTaskListener(){
            @Override
            public void onUploadSucceed(final FileInfo result) {
                mMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        mCurrPhotoInfo = result;
                        VideoUrl=result.url;
                        m_pDialog.hide();
                    }
                });
            }
            @Override
            public void onUploadStateChange(ITask.TaskState state) {
            }
            @Override
            public void onUploadProgress(final long totalSize,
                                         final long sendSize) {
                long p = (long) ((sendSize * 100) / (totalSize * 1.0f));
                Log.i("jia", "上传进度: " + p + "%");
                mMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        long p = (long) ((sendSize * 100) / (totalSize * 1.0f));
                        m_pDialog.setMessage("短视频加载中: " + p + "%");
                    }
                });
            }
            @Override
            public void onUploadFailed(final int errorCode,
                                       final String errorMsg) {
                mMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        m_pDialog.hide();
                        Toast.makeText(getApplicationContext(),
                                errorCode + " msg:" + errorMsg,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        videoUploadTask.authIsEmpty();
        videoUploadTask.setAuth(sign);
        mFileUploadManager.upload(videoUploadTask); // 开始上传
    }
}
