package com.tiyujia.homesport.common.homepage.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ff.imagezoomdrag.ImageDetailActivity;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.NewBaseActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.adapter.HomePageCommentAdapter;
import com.tiyujia.homesport.common.homepage.adapter.HomePageVenueUserAdapter;
import com.tiyujia.homesport.common.homepage.entity.DateInfoModel;
import com.tiyujia.homesport.common.homepage.entity.HomePageCommentEntity;
import com.tiyujia.homesport.common.homepage.entity.HomePageVenueWhomGoneEntity;
import com.tiyujia.homesport.common.personal.activity.PersonalLogin;
import com.tiyujia.homesport.common.personal.activity.PersonalOtherHome;
import com.tiyujia.homesport.entity.ImageUploadModel;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.entity.LzyResponse;
import com.tiyujia.homesport.util.DeleteUtil;
import com.tiyujia.homesport.util.EmojiFilterUtil;
import com.tiyujia.homesport.util.KeyboardWatcher;
import com.tiyujia.homesport.util.LvUtil;
import com.tiyujia.homesport.util.PicUtil;
import com.tiyujia.homesport.util.PicassoUtil;
import com.tiyujia.homesport.util.PostUtil;
import com.tiyujia.homesport.util.RefreshUtil;
import com.tiyujia.homesport.util.StringUtil;
import com.tiyujia.homesport.util.TimeUtil;
import com.tiyujia.homesport.widget.GlideImageLoader;
import com.tiyujia.homesport.widget.ImagePickerAdapter;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
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
 * 作者: Cymbi on 2016/12/7 18:50.
 * 邮箱:928902646@qq.com
 */

public class HomePageDateInfo extends NewBaseActivity implements View.OnClickListener,KeyboardWatcher.OnKeyboardToggleListener,SwipeRefreshLayout.OnRefreshListener
                                                                    ,ImagePickerAdapter.OnRecyclerViewItemClickListener{
    @Bind(R.id.ivBack)                          ImageView ivBack;
    @Bind(R.id.ivShare)                         ImageView ivShare;
    @Bind(R.id.ivAvatar)                        ImageView ivAvatar;
    @Bind(R.id.ivBackground)                    ImageView ivBackground;
    @Bind(R.id.ivLv)                            ImageView ivLv;
    @Bind(R.id.tvPush)                          TextView tvPush;
    @Bind(R.id.tvTitle)                         TextView tvTitle;
    @Bind(R.id.tvTime)                          TextView tvTime;
    @Bind(R.id.tvContent)                       TextView tvContent;
    @Bind(R.id.tvAddress)                       TextView tvAddress;
    @Bind(R.id.tvNickname)                      TextView tvNickname;
    @Bind(R.id.tvSign)                          TextView tvSign;
    @Bind(R.id.tvStartTime)                     TextView tvStartTime;
    @Bind(R.id.tvPhone)                         TextView tvPhone;
    @Bind(R.id.tvCity)                          TextView tvCity;
    @Bind(R.id.tvSurplusNumber)                 TextView tvSurplusNumber;//剩余报名名额
    @Bind(R.id.llToTalk)                        LinearLayout llToTalk;//橙色布局
    @Bind(R.id.llCancelAndSend)                 LinearLayout llCancelAndSend;//输入框布局
    @Bind(R.id.srlRefresh)                      SwipeRefreshLayout srlRefresh;
    @Bind(R.id.rvActiveEnrolled)                RecyclerView rvActiveEnrolled;
    @Bind(R.id.rvActiveComment)                 RecyclerView rvActiveComment;
    public static HomePageCommentEntity.HomePage entity;
    public static EditText etToComment;
    TextView tvSend,tvCancel;
    HomePageVenueUserAdapter userAdapter;
    HomePageCommentAdapter commentAdapter;
    private KeyboardWatcher keyboardWatcher;
    private int activityId;
    private AlertDialog builder;
    private int mUserId;
    private String mToken;
    private int activityUserId;
    private long lastTime;
    private String imgUrls;
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
    public static final int HANDLE_BASE_DATA=1;
    List<HomePageVenueWhomGoneEntity> list;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==HANDLE_BASE_DATA){
                userAdapter=new HomePageVenueUserAdapter(HomePageDateInfo.this,list);
                LinearLayoutManager manager1 = new LinearLayoutManager(HomePageDateInfo.this, LinearLayoutManager.HORIZONTAL, false);
                rvActiveEnrolled.setLayoutManager(manager1);
                rvActiveEnrolled.setAdapter(userAdapter);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_date_info);
        llToTalk.setVisibility(View.VISIBLE);
        llCancelAndSend.setVisibility(View.GONE);
        etToComment= (EditText) llCancelAndSend.findViewById(R.id.etToComment);
        tvCancel= (TextView) llCancelAndSend.findViewById(R.id.tvCancel);
        tvSend= (TextView) llCancelAndSend.findViewById(R.id.tvSend);
        commentAdapter=new HomePageCommentAdapter(null);
        commentAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        commentAdapter.isFirstOnly(false);
        rvActiveComment.setItemAnimator(new DefaultItemAnimator());
        rvActiveComment.setLayoutManager(new LinearLayoutManager(this));
        rvActiveComment.setAdapter(commentAdapter);
        View view= LayoutInflater.from(this).inflate(R.layout.normal_empty_view,null);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp2);
        TextView tvEmptyText= (TextView) view.findViewById(R.id.text_empty);
        tvEmptyText.setText("没有更多评论");
        commentAdapter.setEmptyView(view);
        getInfo();
        onRefresh();
        initImagePicker();
        initWidget();
        setView();
        keyboardWatcher = new KeyboardWatcher(this);
        keyboardWatcher.setListener(this);
    }
    private void getInfo() {
        SharedPreferences share= getSharedPreferences("UserInfo",MODE_PRIVATE);
        mToken= share.getString("Token","");
        mUserId=share.getInt("UserId",0);
        activityId=getIntent().getIntExtra("id",0);
    }

    private void setView() {
        RefreshUtil.refresh(srlRefresh,this);
        srlRefresh.setOnRefreshListener(this);
        ivBack.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        tvPhone.setOnClickListener(this);
        tvPush.setOnClickListener(this);
        llToTalk.setOnClickListener(this);
        tvSend.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        ivBackground.setOnClickListener(this);
        ivAvatar.setOnClickListener(this);
    }

    @Override
    public void onRefresh() {
        OkGo.post(API.BASE_URL+"/v2/activity/activityById")
                .tag(this)
                .params("activityId",activityId)
                .execute(new LoadCallback<DateInfoModel>(this) {
                    @Override
                    public void onSuccess(DateInfoModel dateInfoModel, Call call, Response response) {
                        if(dateInfoModel.state==200){
                            int maxNumber=dateInfoModel.data.maxPeople;
                            int memberPeople=dateInfoModel.data.memberPeople;
                            if(maxNumber==0){
                                tvSurplusNumber.setText("已报名："+memberPeople+"人");
                            }else if(maxNumber>memberPeople) {
                                tvSurplusNumber.setText("剩余名额： "+(maxNumber-memberPeople)+"人");
                            }else if(maxNumber<=memberPeople){
                                tvSurplusNumber.setText("报名人数已满");
                            }
                            imgUrls=dateInfoModel.data.imgUrls;
                            tvTitle.setText(dateInfoModel.data.title);
                            activityUserId=dateInfoModel.data.user.id;
                            lastTime=dateInfoModel.data.lastTime;
                            String starttime=API.format.format(dateInfoModel.data.startTime);
                            String endtime=API.format.format(dateInfoModel.data.endTime);
                            tvTime.setText(starttime+"—"+endtime);
                            tvContent.setText(dateInfoModel.data.descContent);
                            tvAddress.setText(dateInfoModel.data.address);
                            tvNickname.setText(dateInfoModel.data.user.nickname);
                            if (!TextUtils.isEmpty(dateInfoModel.data.user.signature)){
                                tvSign.setText(dateInfoModel.data.user.signature);
                            }else {
                                tvSign.setText("这个人很懒，什么也没有留下");
                            }
                            if (dateInfoModel.data.user.level!=null||dateInfoModel.data.user.level.equals("")){
                                LvUtil.setLv(ivLv,dateInfoModel.data.user.level.pointDesc);
                            }else {
                                LvUtil.setLv(ivLv,"初学乍练");
                            }
                            String  create= TimeUtil.checkTime(dateInfoModel.data.createTime);
                            String  end= TimeUtil.checkTime(dateInfoModel.data.endTime);
                            tvStartTime.setText(create+"~"+end);
                            tvPhone.setText(dateInfoModel.data.user.phone);
                            tvCity.setText(dateInfoModel.data.city);
                            PicassoUtil.handlePic(HomePageDateInfo.this, PicUtil.getImageUrlDetail(HomePageDateInfo.this, StringUtil.isNullImage(dateInfoModel.data.imgUrls), 1280,720 ),ivBackground,1280,720);
                            PicassoUtil.handlePic(HomePageDateInfo.this, PicUtil.getImageUrlDetail(HomePageDateInfo.this, StringUtil.isNullImage(dateInfoModel.data.user.avatar), 320,320 ),ivAvatar,320,320);
                            if (mUserId==activityUserId){
                                ivShare.setVisibility(View.VISIBLE);
                            }else {
                                ivShare.setVisibility(View.GONE);
                            }
                        }
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        showToast("服务器故障");
                    }
                    @Override
                    public void onAfter(@Nullable DateInfoModel dateInfoModel, @Nullable Exception e) {
                        super.onAfter(dateInfoModel, e);
                        setRefreshing(false);
                    }
                });
        list=new ArrayList<>();
        final String url=API.BASE_URL+"/v2/member/findByActivityId";
        new Thread(){
            @Override
            public void run() {
                try{
                    HashMap<String, String> params = new HashMap<>();
                    params.put("activityId", ""+activityId);
                    params.put("number", "1000");
                    params.put("pageNumber", "1");
                    String result = PostUtil.sendPostMessage(url, params);
                    JSONObject obj = new JSONObject(result);
                    int state=obj.getInt("state");
                    if (state==200){
                        JSONArray array=obj.getJSONArray("data");
                        if (array.length()!=0){
                            int getDataNumber=0;
                            if (array.length()<=4){
                                getDataNumber=array.length();
                            }else {
                                getDataNumber=4;
                            }
                            for (int i=0;i<getDataNumber;i++){
                                JSONObject jsonObj=array.getJSONObject(i);
                                HomePageVenueWhomGoneEntity entity=new HomePageVenueWhomGoneEntity();
                                entity.setVenueId(activityId);
                                JSONArray user=jsonObj.getJSONArray("user");
                                JSONObject object=user.getJSONObject(0);
                                entity.setUserId(object.getInt("id"));
                                entity.setUserName(object.getString("nickname"));
                                entity.setUserPhotoUrl(StringUtil.isNullAvatar(object.getString("avatar")));
                                JSONObject level=object.getJSONObject("level");
                                entity.setUserLevelUrl(level.getString("pointDesc")==null?"初学乍练":level.getString("pointDesc"));
                                entity.setAuthenticate(object.getString("signature")==null?"":object.getString("signature"));
                                entity.setStep(level.getString("step")==null?"":level.getString("step"));
                                list.add(entity);
                            }
                        }
                    }
                    if (list.size()==4){
                        HomePageVenueWhomGoneEntity empty=new HomePageVenueWhomGoneEntity();
                        list.add(empty);
                    }
                    handler.sendEmptyMessage(HANDLE_BASE_DATA);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
        OkGo.get(API.BASE_URL+"/v2/comment/query/"+3+"/"+activityId)
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        etToComment.setText("");
        llToTalk.setVisibility(View.VISIBLE);
        llCancelAndSend.setVisibility(View.GONE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etToComment.getWindowToken(), 0);
        return super.onKeyDown(keyCode, event);

    }

    @Override
    public void onClick(View v) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            switch (v.getId()){
                case R.id.ivAvatar:
                    Intent i=new Intent(HomePageDateInfo.this, PersonalOtherHome.class);
                    i.putExtra("id",activityUserId);
                    startActivity(i);
                    break;
                case R.id.ivBackground:
                    ArrayList<String> list = new ArrayList<String>();
                    list.add(API.PICTURE_URL+imgUrls);
                    startActivity(ImageDetailActivity.getMyStartIntent(HomePageDateInfo.this, list,0, ImageDetailActivity.url_path));
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
            case R.id.ivShare:
                View view = getLayoutInflater().inflate(R.layout.share_dialog, null);
                final Dialog dialog = new Dialog(this,R.style.Dialog_Fullscreen);
//                TextView tvQQ=(TextView)view.findViewById(R.id.tvQQ);
//                TextView tvQQzone=(TextView)view.findViewById(R.id.tvQQzone);
//                TextView tvWeChat=(TextView)view.findViewById(R.id.tvWeChat);
//                TextView tvFriends=(TextView)view.findViewById(R.id.tvFriends);
//                TextView tvSina=(TextView)view.findViewById(R.id.tvSina);
                TextView tvDelete=(TextView)view.findViewById(R.id.tvDelete);
                TextView tvCancel=(TextView)view.findViewById(R.id.tvCancel);
//
//                //分享到QQ
//                tvQQ.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v)
//                    {
//                        showToast("分享到QQ");
//                        showDialog();
//                        dialog.dismiss();
//                    }
//                });
//                //分享到QQ空间
//                tvQQzone.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        showToast("分享到QQ空间");
//                        showDialog();
//                        dialog.dismiss();
//                    }
//                });
//                //分享到微信好友
//                tvWeChat.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        showToast("分享到微信好友");
//                        showDialog();
//                        dialog.dismiss();
//                    }
//                });
//                //分享到朋友圈
//                tvFriends.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        showToast("分享到朋友圈");
//                        showDialog();
//                        dialog.dismiss();
//                    }
//                });
//                //分享到新浪微博
//                tvSina.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        showToast("分享到新浪微博");
//                        showDialog();
//                        dialog.dismiss();
//                    }
//                });
                //删除
                tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DeleteUtil.handleDeleteActiveTransaction(HomePageDateInfo.this,mToken,activityId,mUserId);
                        showToast("删除活动成功");
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
            case R.id.tvPhone:
                final AlertDialog builder = new AlertDialog.Builder(HomePageDateInfo.this).create();
                builder.setView(getLayoutInflater().inflate(R.layout.call_phone_dialog, null));
                builder.show();
                //去掉dialog四边的黑角
                builder.getWindow().setBackgroundDrawable(new BitmapDrawable());
                TextView text=(TextView) builder.getWindow().findViewById(R.id.text);
                text.setText("直接拨打"+tvPhone.getText()+"?");
                builder.getWindow().findViewById(R.id.dialog_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();
                    }
                });
                TextView dialog_confirm=(TextView)builder.getWindow().findViewById(R.id.dialog_confirm);
                dialog_confirm.setText("拨打");
                dialog_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+tvPhone.getText().toString().trim()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        builder.dismiss();
                    }
                });
                break;
            case R.id.tvPush:
                long currentTime = System.currentTimeMillis();
                if (currentTime>lastTime){
                    showToast("报名时间已过，无法报名");
                }else{
                    OkGo.post(API.BASE_URL+"/v2/member/add")
                            .tag(this)
                            .params("token",mToken)
                            .params("userId",mUserId)
                            .params("activityId",activityId)
                            .params("activityUserId",activityUserId)
                            .execute(new LoadCallback<LzyResponse>(this) {
                                @Override
                                public void onSuccess(LzyResponse lzyResponse, Call call, Response response) {
                                    if(lzyResponse.state==200){
                                        AlertDialog  builder = new AlertDialog.Builder(HomePageDateInfo.this).create();
                                        builder.setView(HomePageDateInfo.this.getLayoutInflater().inflate(R.layout.share_succeed_dialog, null));
                                        builder.show();
                                        //去掉dialog四边的黑角
                                        builder.getWindow().setBackgroundDrawable(new BitmapDrawable());
                                        TextView tvTitle=(TextView)builder.getWindow().findViewById(R.id.tvTitle);
                                        tvTitle.setText("报名成功");
                                        TextView tvContent=(TextView)builder.getWindow().findViewById(R.id.tvContent);
                                        tvContent.setText("感谢您的报名，祝您玩得愉快");
                                    }else if(lzyResponse.state==10005){
                                        showToast("亲，请勿重复报名哦");
                                    }else if(lzyResponse.state==800){
                                        showToast("亲，账户已失效或者未登录");
                                        Intent intent=new Intent(HomePageDateInfo.this, PersonalLogin.class);
                                        HomePageDateInfo.this.startActivity(intent);
                                    }else if(lzyResponse.state==10013){
                                        showToast("亲，报名人数已满，无法报名");
                                    }
                                }
                            });
                }
                break;
        }
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
    private void initWidget() {
        rvAddPicture= (RecyclerView) findViewById(R.id.revImage);
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);
        rvAddPicture.setLayoutManager(new GridLayoutManager(this, 4));
        rvAddPicture.setHasFixedSize(true);
        rvAddPicture.setAdapter(adapter);
    }
    private void showDialog(){
        builder = new AlertDialog.Builder(this).create();
        builder.setView(this.getLayoutInflater().inflate(R.layout.share_succeed_dialog, null));
        builder.show();
        //去掉dialog四边的黑角
        builder.getWindow().setBackgroundDrawable(new BitmapDrawable());
        TextView tvTitle=(TextView)builder.getWindow().findViewById(R.id.tvTitle);
        tvTitle.setText("分享成功");
        TextView tvContent=(TextView)builder.getWindow().findViewById(R.id.tvContent);
        tvContent.setText("感谢您的分享，祝您玩愉快");
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
        final String commentText = EmojiFilterUtil.filterEmoji(HomePageDateInfo.this,tempText);
        if (mUserId==0){
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
                                            .params("token", mToken)
                                            .params("comment_type", "4")
                                            .params("comment_id", activityId + "")
                                            .params("model_create_id", "-1")
                                            .params("comment_account", mUserId + "")
                                            .params("comment_content", commentText)
                                            .params("comment_img_path", imgUrl)
                                            .execute(new LoadCallback<LzyResponse>(HomePageDateInfo.this) {
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
                                params.put("token", mToken);
                                params.put("comment_type", "3");
                                params.put("comment_id", activityId + "");
                                params.put("model_create_id", "-1");
                                params.put("comment_account", mUserId + "");
                                params.put("comment_content", commentText);
                                String result = PostUtil.sendPostMessage(uri, params);
                                JSONObject obj = new JSONObject(result);
                                int state = obj.getInt("state");
                                if (state == 200) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(HomePageDateInfo.this, "评论成功！", Toast.LENGTH_LONG).show();
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
                                            Toast.makeText(HomePageDateInfo.this, "登陆已过期，请重新登陆！", Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(HomePageDateInfo.this,PersonalLogin.class);
                                            HomePageDateInfo.this.startActivity(intent);
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
                    String token=mToken;
                    int replyParentId=entity.id;
                    int replyFromUser=mUserId;
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
                                Toast.makeText(HomePageDateInfo.this, "回复成功！", Toast.LENGTH_LONG).show();
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
