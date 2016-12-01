package com.tiyujia.homesport.common.community.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.personal.activity.PersonalSetInfo;
import com.tiyujia.homesport.entity.ImageUploadModel;
import com.tiyujia.homesport.entity.JsonCallback;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.entity.LzyResponse;
import com.tiyujia.homesport.util.StringUtil;
import com.tiyujia.homesport.widget.GlideImageLoader;
import com.tiyujia.homesport.widget.ImagePickerAdapter;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;

import static com.loc.c.e;

/**
 * 作者: Cymbi on 2016/11/18 14:22.1
 * 邮箱:928902646@qq.com
 */

public class CommunityDynamicPublish extends ImmersiveActivity implements ImagePickerAdapter.OnRecyclerViewItemClickListener,View.OnClickListener{
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 9;               //允许选择图片最大数
    private Dialog dialog;
    private TextView tvVideo;
    private TextView tvGallery;
    @Bind(R.id.ivBack)  ImageView ivBack;
    @Bind(R.id.tvPush)  TextView tvPush;
    @Bind(R.id.etIssueContent)  EditText etIssueContent;
    private ArrayList<ImageItem> images;
    private String mToken;
    private int mUserId;
    private String imgUrl="";
    private String local="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_dynamic_publish);
        setInfo();
        initImagePicker();
        initWidget();

    }

    private void setInfo() {
        SharedPreferences share = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        mToken=share.getString("Token","");
        mUserId=share.getInt("UserId",0);
    }
    private void initWidget() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.revImage);
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        ivBack.setOnClickListener(this);
        tvPush.setOnClickListener(this);
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
                Intent intent = new Intent(CommunityDynamicPublish.this, CommunityNewVideoActivity.class);
                startActivity(intent);
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
                finish();
                break;
            case R.id.tvPush:
                String content=etIssueContent.getText().toString();
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
                                        String[] str=null;
                                        str = (String[])da.toArray(new String[da.size()]);
                                        imgUrl=  StringUtils.join(str,",");
                                        Log.i("imgUrl",imgUrl);
                                        showToast("daslsa");
                                    }
                                    @Override
                                    public void onError(Call call, Response response, Exception e) {
                                        super.onError(call, response, e);
                                        showToast("onError");
                                    }
                                });
                    }else {}

                    OkGo.post(API.BASE_URL+"/v2/cern/insert")
                            .params("userId",mUserId)
                            .params("content",content)
                            .params("imgUrl",imgUrl)
                            .params("visible",0)
                            .params("local",local)
                            .execute(new LoadCallback<LzyResponse>(this) {
                                @Override
                                public void onSuccess(LzyResponse lzyResponse, Call call, Response response) {
                                    if(lzyResponse.state==200){
                                        showToast("发布成功");
                                    }
                                }
                                @Override
                                public void onError(Call call, Response response, Exception e) {
                                    super.onError(call, response, e);
                                    showToast("失败");
                                }
                            });
                }else {
                    showToast("还没有填写内容哟~");
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
}
