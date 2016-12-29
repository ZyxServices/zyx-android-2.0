package com.tiyujia.homesport.common.homepage.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.NewBaseActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.community.activity.CommunityDynamicDetailActivity;
import com.tiyujia.homesport.common.homepage.adapter.HomePageCommentAdapter;
import com.tiyujia.homesport.common.homepage.adapter.HomePageVenueUserAdapter;
import com.tiyujia.homesport.common.homepage.adapter.NGLAdapter;
import com.tiyujia.homesport.common.homepage.entity.EquipmentInfoModel;
import com.tiyujia.homesport.common.homepage.entity.HomePageCommentEntity;
import com.tiyujia.homesport.common.personal.activity.PersonalLogin;
import com.tiyujia.homesport.common.personal.activity.PersonalOtherHome;
import com.tiyujia.homesport.entity.ImageUploadModel;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.entity.LzyResponse;
import com.tiyujia.homesport.util.DeleteUtil;
import com.tiyujia.homesport.util.EmojiFilterUtil;
import com.tiyujia.homesport.util.FollowUtil;
import com.tiyujia.homesport.util.KeyboardWatcher;
import com.tiyujia.homesport.util.PicUtil;
import com.tiyujia.homesport.util.PicassoUtil;
import com.tiyujia.homesport.util.PostUtil;
import com.tiyujia.homesport.util.RefreshUtil;
import com.tiyujia.homesport.util.StringUtil;
import com.tiyujia.homesport.widget.GlideImageLoader;
import com.tiyujia.homesport.widget.ImagePickerAdapter;
import com.w4lle.library.NineGridlayout;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者: Cymbi on 2016/12/7 10:16.
 * 邮箱:928902646@qq.com
 */

public class HomePageEquipmentInfo extends NewBaseActivity implements View.OnClickListener,KeyboardWatcher.OnKeyboardToggleListener,SwipeRefreshLayout.OnRefreshListener
        ,ImagePickerAdapter.OnRecyclerViewItemClickListener {
    @Bind(R.id.ivBack)                  ImageView ivBack;
    @Bind(R.id.ivMenu)                  ImageView ivMenu;
    @Bind(R.id.ivAvatar)                ImageView ivAvatar;
    @Bind(R.id.tvOfficial)              TextView tvOfficial;
    @Bind(R.id.tvNickname)              TextView tvNickname;
    @Bind(R.id.tvTime)                  TextView tvTime;
    @Bind(R.id.tvTitle)                 TextView tvTitle;
    @Bind(R.id.tvContent)               TextView tvContent;
    @Bind(R.id.tvPraise)                TextView tvPraise;
    @Bind(R.id.nineGrid)                NineGridlayout nineGrid;
    @Bind(R.id.llToTalk)                LinearLayout llToTalk;//橙色布局
    @Bind(R.id.llCancelAndSend)         LinearLayout llCancelAndSend;//输入框布局
    @Bind(R.id.srlRefresh)              SwipeRefreshLayout srlRefresh;
    @Bind(R.id.rvEquipComment)          RecyclerView rvEquipComment;
    public static HomePageCommentEntity.HomePage entity;
    public static EditText etToComment;
    TextView tvSend,tvCancel;
    HomePageCommentAdapter commentAdapter;
    private KeyboardWatcher keyboardWatcher;
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    private int maxImgCount = 9;               //允许选择图片最大数
    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private List<ImageItem> images=new ArrayList<>();
    public static RecyclerView rvAddPicture;
    public static boolean isComment=true;
    public static int replyToId=0;
    private int userId;
    String token;
    int equipId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipment_info);
        etToComment= (EditText) llCancelAndSend.findViewById(R.id.etToComment);
        tvCancel= (TextView) llCancelAndSend.findViewById(R.id.tvCancel);
        tvSend= (TextView) llCancelAndSend.findViewById(R.id.tvSend);
        commentAdapter=new HomePageCommentAdapter(null);
        commentAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        commentAdapter.isFirstOnly(false);
        rvEquipComment.setItemAnimator(new DefaultItemAnimator());
        rvEquipComment.setLayoutManager(new LinearLayoutManager(this));
        rvEquipComment.setAdapter(commentAdapter);
        View view= LayoutInflater.from(this).inflate(R.layout.normal_empty_view,null);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp2);
        TextView tvEmptyText= (TextView) view.findViewById(R.id.text_empty);
        tvEmptyText.setText("没有更多评论");
        commentAdapter.setEmptyView(view);
        RefreshUtil.refresh(srlRefresh,this);
        srlRefresh.setOnRefreshListener(this);
        getBaseInfo();
        onRefresh();
        initImagePicker();
        initWidget();
        setListeners();
    }

    private void initWidget() {
        rvAddPicture= (RecyclerView) findViewById(R.id.revImage);
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);
        rvAddPicture.setLayoutManager(new GridLayoutManager(this, 4));
        rvAddPicture.setHasFixedSize(true);
        rvAddPicture.setAdapter(adapter);
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

    private void getBaseInfo() {
        SharedPreferences share= getSharedPreferences("UserInfo",MODE_PRIVATE);
        token= share.getString("Token","");
        userId=share.getInt("UserId",0);
        equipId=getIntent().getIntExtra("id",0);
    }

    private void setListeners() {
        ivMenu.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        llToTalk.setOnClickListener(this);
        tvSend.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        tvPraise.setOnClickListener(this);
        keyboardWatcher = new KeyboardWatcher(this);
        keyboardWatcher.setListener(this);
    }
private int equipOwnerId=0;
    public void onRefresh() {
        OkGo.get(API.BASE_URL+"/v2/equip/queryOne")
                .tag(this)
                .params("token",token)
                .params("id",equipId)
                .execute(new LoadCallback<EquipmentInfoModel>(this) {
                    @Override
                    public void onSuccess(final EquipmentInfoModel Model, Call call, Response response) {
                        if(Model.state==200){
                            equipOwnerId=Model.data.accountId;
                            tvNickname.setText(Model.data.userIconVo.nickName);
                            tvTime.setText(API.simpleYear.format(Model.data.createTime));
                            PicassoUtil.handlePic(HomePageEquipmentInfo.this, PicUtil.getImageUrlDetail(HomePageEquipmentInfo.this, StringUtil.isNullAvatar(Model.data.userIconVo.avatar), 320, 320), ivAvatar, 320, 320);
                            tvTitle.setText(Model.data.title);
                            tvContent.setText(Model.data.content);
                            if (Model.data.imgUrl != null) {
                                String str = Model.data.imgUrl;
                                List<String> imgUrls = StringUtil.stringToList(str);;
                                NGLAdapter adapter = new NGLAdapter(HomePageEquipmentInfo.this, imgUrls);
                                nineGrid.setVisibility(View.VISIBLE);
                                nineGrid.setGap(6);
                                nineGrid.setAdapter(adapter);
                            }
                            if (Model.data.userIconVo.id!=userId){
                                ivMenu.setVisibility(View.GONE);
                                ivAvatar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent i= new Intent(HomePageEquipmentInfo.this, PersonalOtherHome.class);
                                        i.putExtra("id",Model.data.userIconVo.id);
                                        startActivity(i);
                                    }
                                });
                            }else {
                                ivMenu.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        showToast("服务器故障");
                    }
                });
        OkGo.get(API.BASE_URL+"/v2/comment/query/"+2+"/"+equipId)
                .tag(this)
                .execute(new LoadCallback<HomePageCommentEntity>(this) {
                    @Override
                    public void onSuccess(HomePageCommentEntity homePage, Call call, Response response) {
                        if (homePage.state==200) {
                            if (homePage.data != null) {
                                commentAdapter.setNewData(homePage.data);
                                commentAdapter.setOnItemClickListener(new HomePageCommentAdapter.OnItemClickListener() {
                                    @Override
                                    public void OnItemClick(HomePageCommentEntity.HomePage data, String backTo) {
                                        isComment = false;
                                        entity = data;
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        llToTalk.setVisibility(View.GONE);
                                        etToComment.requestFocus();
                                        etToComment.setHint("回复：" + backTo);
                                        llCancelAndSend.setVisibility(View.VISIBLE);
                                        imm.showSoftInput(etToComment, InputMethodManager.SHOW_FORCED);
                                    }
                                });
                            }else {

                            }
                        }
                    }
                    @Override
                    public void onAfter(@Nullable HomePageCommentEntity homePageCommentEntity, @Nullable Exception e) {
                        super.onAfter(homePageCommentEntity, e);
                        commentAdapter.removeAllFooterView();
                        setRefreshing(false);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        switch (v.getId()){
            case R.id.tvPraise:
                if (userId==0){
                    showToast("您还没有登陆呢，亲！");
                }else {
                    FollowUtil.goToPraise(this,token,equipId,2,equipOwnerId,userId);
                }
                break;
            case R.id.tvCancel:
                etToComment.setText("");
                llToTalk.setVisibility(View.VISIBLE);
                llCancelAndSend.setVisibility(View.GONE);
                imm.hideSoftInputFromWindow(etToComment.getWindowToken(), 0);
                break;
            case R.id.tvSend:
                if (TextUtils.isEmpty(etToComment.getText().toString())){
                    Toast.makeText(this,"请输入评论或回复内容！",Toast.LENGTH_LONG).show();
                }else {
                    writeToCallBack();
                }
                break;
            case R.id.llToTalk:
                llToTalk.setVisibility(View.GONE);
                etToComment.requestFocus();
                llCancelAndSend.setVisibility(View.VISIBLE);
                if (isComment){
                    rvAddPicture.setVisibility(View.VISIBLE);
                }else {
                    rvAddPicture.setVisibility(View.GONE);
                }
                imm.showSoftInput(etToComment,InputMethodManager.SHOW_FORCED);
                break;
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivMenu:
                View view = getLayoutInflater().inflate(R.layout.share_dialog, null);
                final Dialog dialog = new Dialog(HomePageEquipmentInfo.this,R.style.Dialog_Fullscreen);
                TextView tvDelete=(TextView)view.findViewById(R.id.tvDelete);
                TextView tvCancel=(TextView)view.findViewById(R.id.tvCancel);
                //删除
                tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OkGo.delete(API.BASE_URL+"/v2/equip/"+token+"/del/"+equipId+"/"+userId)
                                .tag(this)
                                .execute(new LoadCallback<LzyResponse>(HomePageEquipmentInfo.this) {
                                    @Override
                                    public void onSuccess(LzyResponse lzyResponse, Call call, Response response) {
                                        if(lzyResponse.state==200){
                                            showToast("删除成功");
                                            finish();
                                        }else {
                                            showToast("异常"+lzyResponse.state);
                                        }
                                    }
                                    @Override
                                    public void onError(Call call, Response response, Exception e) {
                                        super.onError(call, response, e);
                                        showToast("服务器故障");
                                    }
                                });
                        showToast("删除成功");
                        dialog.dismiss();
                    }
                });
                //取消
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showToast("取消");
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
                dialog.show();
                break;
        }
    }
    public void setRefreshing(final boolean refreshing) {
        srlRefresh.post(new Runnable() {
            @Override
            public void run() {
                srlRefresh.setRefreshing(refreshing);
            }
        });
    }
    private void writeToCallBack(){
        final String uri = API.BASE_URL + "/v2/comment/insert";
        String tempText = etToComment.getText().toString().trim();
        final String commentText = EmojiFilterUtil.filterEmoji(HomePageEquipmentInfo.this,tempText);
        if (userId==0){
            Toast.makeText(this,"您还没有登录呢，亲！请登录！",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(this, PersonalLogin.class);
            startActivity(intent);
        }else {
            if (isComment) {
                if (images.size()!=0) {
                    ArrayList<File> files = new ArrayList<>();
                    for (int i = 0; i < images.size(); i++) {
                        files.add(new File(images.get(i).path));
                    }
                    OkGo.post(API.IMAGE_URLS)
                            .tag(this)
                            .addFileParams("avatars", files)
                            .execute(new LoadCallback<ImageUploadModel>(this) {
                                @Override
                                public void onSuccess(ImageUploadModel imageUploadModel, Call call, Response response) {
                                    List<String> da = imageUploadModel.data;
                                    String[] str = (String[]) da.toArray(new String[da.size()]);
                                    String imgUrl = StringUtils.join(str, ",");
                                    OkGo.post(uri)
                                            .params("token", token)
                                            .params("comment_type", "2")
                                            .params("comment_id", equipId + "")
                                            .params("model_create_id", "-1")
                                            .params("comment_account", userId + "")
                                            .params("comment_content", commentText)
                                            .params("comment_img_path", imgUrl)
                                            .execute(new LoadCallback<LzyResponse>(HomePageEquipmentInfo.this) {
                                                @Override
                                                public void onSuccess(LzyResponse lzyResponse, Call call, Response response) {
                                                    if (lzyResponse.state == 200) {
                                                        showToast("评论成功");
                                                        etToComment.setText("");
                                                        llToTalk.setVisibility(View.VISIBLE);
                                                        llCancelAndSend.setVisibility(View.GONE);
                                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                        imm.hideSoftInputFromWindow(etToComment.getWindowToken(), 0);
                                                        images.clear();
                                                        onRefresh();
                                                    }
                                                }
                                                @Override
                                                public void onError(Call call, Response response, Exception e) {
                                                    super.onError(call, response, e);
                                                    showToast("失败");
                                                }
                                            });
                                }
                                @Override
                                public void onError(Call call, Response response, Exception e) {
                                    super.onError(call, response, e);
                                    showToast("onError");
                                    Log.i("tag","error============"+e.toString());
                                }
                            });
                }else {
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                HashMap<String, String> params = new HashMap<>();
                                params.put("token", token);
                                params.put("comment_type", "2");
                                params.put("comment_id", equipId + "");
                                params.put("model_create_id", "-1");
                                params.put("comment_account", userId + "");
                                params.put("comment_content", commentText);
                                String result = PostUtil.sendPostMessage(uri, params);
                                JSONObject obj = new JSONObject(result);
                                int state = obj.getInt("state");
                                if (state == 200) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(HomePageEquipmentInfo.this, "评论成功！", Toast.LENGTH_LONG).show();
                                            etToComment.setText("");
                                            llToTalk.setVisibility(View.VISIBLE);
                                            llCancelAndSend.setVisibility(View.GONE);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                            imm.hideSoftInputFromWindow(etToComment.getWindowToken(), 0);
                                            onRefresh();
                                        }
                                    });
                                }else if (state==800){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(HomePageEquipmentInfo.this, "登陆已过期，请重新登陆！", Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(HomePageEquipmentInfo.this,PersonalLogin.class);
                                            HomePageEquipmentInfo.this.startActivity(intent);
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
            }else {
                handleReply(replyToId,commentText);
            }
        }
    }
    private void handleReply(final int replyToId,final String replyContent){
        new Thread(){
            @Override
            public void run() {
                try{
                    String url=API.BASE_URL+"/v2/reply/addReply";
                    int replyParentId=entity.id;
                    int replyFromUser=userId;
                    int replyToUser=(replyToId==0)?entity.userVo.id:replyToId;
                    HashMap<String,String> params=new HashMap<String, String>();
                    params.put("token",""+token);
                    params.put("reply_parent_id",""+replyParentId);
                    params.put("reply_from_user",""+replyFromUser);
                    params.put("reply_to_user",""+replyToUser);
                    params.put("reply_content",""+replyContent);
                    params.put("reply_img_path","");
                    String result=PostUtil.sendPostMessage(url,params);
                    JSONObject object=new JSONObject(result);
                    if (object.getInt("state")==200){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(HomePageEquipmentInfo.this, "回复成功！", Toast.LENGTH_LONG).show();
                                etToComment.setText("");
                                llToTalk.setVisibility(View.VISIBLE);
                                llCancelAndSend.setVisibility(View.GONE);
                                isComment=true;
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(etToComment.getWindowToken(), 0);
                                onRefresh();
                            }
                        });
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }
    @Override
    protected void onDestroy() {
        keyboardWatcher.destroy();
        super.onDestroy();
    }
    @Override
    public void onKeyboardShown(int keyboardSize) {
        llCancelAndSend.setVisibility(View.VISIBLE);
        if (isComment){
            rvAddPicture.setVisibility(View.VISIBLE);
        }else {
            rvAddPicture.setVisibility(View.GONE);
        }
        llToTalk.setVisibility(View.GONE);
    }
    @Override
    public void onKeyboardClosed() {
        llCancelAndSend.setVisibility(View.GONE);
        llToTalk.setVisibility(View.VISIBLE);
        isComment=true;
        etToComment.setHint("你想说点什么？");
    }
    boolean isFirstIn=true;
    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirstIn&&isBackFromSelectPic){
            etToComment.requestFocus();
            Timer timer = new Timer();
            timer.schedule(new TimerTask(){
                @Override
                public void run(){
                    InputMethodManager m = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }, 300);
        }else {
            etToComment.clearFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etToComment.getWindowToken(), 0);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        isFirstIn=false;
        etToComment.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etToComment.getWindowToken(), 0);
    }
    boolean isBackFromSelectPic=false;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        isFirstIn=false;
        isBackFromSelectPic=true;
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
    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                Intent intent = new Intent(this, ImageGridActivity.class);
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

}
