package com.tiyujia.homesport.common.homepage.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.entity.EquipmentModel;
import com.tiyujia.homesport.entity.ImageUploadModel;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.entity.LzyResponse;
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
 * 作者: Cymbi on 2016/12/6 16:52.
 * 邮箱:928902646@qq.com
 */

public class HomePageEquipmentShowActivity extends ImmersiveActivity implements View.OnClickListener,ImagePickerAdapter.OnRecyclerViewItemClickListener{
    @Bind(R.id.ivBack)    ImageView ivBack;
    @Bind(R.id.tvPush)    TextView tvPush;
    @Bind(R.id.etTitle)   EditText etTitle;
    @Bind(R.id.etContent)   EditText etContent;
    @Bind(R.id.revImage)    RecyclerView revImage;
    @Bind(R.id.cbHelm)    RadioButton cbHelm;
    @Bind(R.id.cbShoes)    RadioButton cbShoes;
    @Bind(R.id.cbDynamicRope)    RadioButton cbDynamicRope;
    @Bind(R.id.cbLock)    RadioButton cbLock;
    @Bind(R.id.radioGroup)    RadioGroup radioGroup;
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 9;               //允许选择图片最大数
    private ArrayList<ImageItem> images;
    private String mToken;
    private int mUserId;
    private int labelId=1;//主锁 4，头盔3，动力绳2 ，鞋子 1
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_equipment_show);
        setView();
        setInfo();
        initImagePicker();
        initWidget();
    }
    private void setInfo() {
        SharedPreferences share = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        mToken=share.getString("Token","");
        mUserId=share.getInt("UserId",0);
    }
    private void setView() {
        ivBack.setOnClickListener(this);
        tvPush.setOnClickListener(this);
        revImage.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(cbHelm.getId()==checkedId){
                    cbHelm.setTextColor(getResources().getColor(R.color.white));
                }else {
                    cbHelm.setTextColor(getResources().getColor(R.color.day_edit_hit_color));
                }
                if(cbShoes.getId()==checkedId){
                    cbShoes.setTextColor(getResources().getColor(R.color.white));
                }else {
                    cbShoes.setTextColor(getResources().getColor(R.color.day_edit_hit_color));
                }
                if(cbDynamicRope.getId()==checkedId){
                    cbDynamicRope.setTextColor(getResources().getColor(R.color.white));
                }else {
                    cbDynamicRope.setTextColor(getResources().getColor(R.color.day_edit_hit_color));
                }
                if(cbLock.getId()==checkedId){
                    cbLock.setTextColor(getResources().getColor(R.color.white));
                }else {
                    cbLock.setTextColor(getResources().getColor(R.color.day_edit_hit_color));
                }

            }
        });
    }
    private void initWidget() {
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);
        revImage.setLayoutManager(new GridLayoutManager(this, 4));
        revImage.setHasFixedSize(true);
        revImage.setAdapter(adapter);
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
                Intent intent = new Intent(HomePageEquipmentShowActivity.this, ImageGridActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SELECT);
                break;
            default:
                //打开预览
                Intent intentPreview = new Intent(HomePageEquipmentShowActivity.this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivBack:
                finish();
                break;
            case R.id.tvPush:
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (cbShoes.getId()==checkedId){
                            labelId=1;
                        }if (cbDynamicRope.getId()==checkedId){
                            labelId=2;
                        }if (cbHelm.getId()==checkedId){
                            labelId=3;
                        }if (cbLock.getId()==checkedId){
                            labelId=4;
                        }
                    }
                });
                final String title =etTitle.getText().toString();
                final String content =etContent.getText().toString();
                ArrayList<File> files=new ArrayList<>();
                if(TextUtils.isEmpty(title)||TextUtils.isEmpty(content)||images == null || images.size() <= 0){
                    showToast("信息填写不完整");
                }else {
                    if (images != null && images.size() > 0){
                        for (int i = 0; i < images.size(); i++) {
                            files.add(new File(images.get(i).path));
                        }
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
                                    OkGo.post(API.BASE_URL+"/v2/equip/insert")
                                            .tag(this)
                                            .params("title",title)
                                            .params("content",content)
                                            .params("accountId",mUserId)
                                            .params("labelId",labelId)
                                            .params("imgUrls",imgUrl)
                                            .execute(new LoadCallback<LzyResponse>(HomePageEquipmentShowActivity.this) {
                                                @Override
                                                public void onSuccess(LzyResponse equipment, Call call, Response response) {
                                                    if(equipment.state==200){showToast("发布成功");}
                                                }

                                                @Override
                                                public void onError(Call call, Response response, Exception e) {
                                                    super.onError(call, response, e);
                                                    showToast("网络连接错误");
                                                }
                                            });
                                }
                            });
                }
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
}
