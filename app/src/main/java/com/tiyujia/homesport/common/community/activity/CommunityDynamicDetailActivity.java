package com.tiyujia.homesport.common.community.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
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
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.NewBaseActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.community.adapter.CommunityLoveAdapter;
import com.tiyujia.homesport.common.community.model.CommunityLoveEntity;
import com.tiyujia.homesport.common.community.model.DynamicDetailEntity;
import com.tiyujia.homesport.common.homepage.activity.HomePageEquipmentInfo;
import com.tiyujia.homesport.common.homepage.adapter.HomePageCommentAdapter;
import com.tiyujia.homesport.common.homepage.adapter.NGLAdapter;
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
import com.tiyujia.homesport.util.LvUtil;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;

public class CommunityDynamicDetailActivity extends NewBaseActivity implements View.OnClickListener,KeyboardWatcher.OnKeyboardToggleListener,SwipeRefreshLayout.OnRefreshListener,
        ImagePickerAdapter.OnRecyclerViewItemClickListener {
    private int recommendId;
    private int nowUserId;
    private int dynamicOwnerId;
    @Bind(R.id.ivDynamicDetailBack)         ImageView ivDynamicDetailBack;//回退按钮
    @Bind(R.id.ivDynamicDetailMore)         ImageView ivDynamicDetailMore;//更多操作（删除）
    @Bind(R.id.srlRefresh)                  SwipeRefreshLayout srlRefresh;//下拉刷新
    @Bind(R.id.rivDynamicDetailAvatar)      RoundedImageView rivDynamicDetailAvatar;//圆形头像
    @Bind(R.id.tvDynamicDetailName)         TextView tvDynamicDetailName;//用户昵称
    @Bind(R.id.ivDynamicDetailLevel)        ImageView ivDynamicDetailLevel;//用户等级
    @Bind(R.id.tvDynamicDetailTime)         TextView tvDynamicDetailTime;//用户创建动态的时间
    @Bind(R.id.tvDynamicDetailCancel)       TextView tvDynamicDetailCancel;//当前用户取消对该用户的关注
    @Bind(R.id.tvDynamicDetailConcern)      TextView tvDynamicDetailConcern;//当前用户关注该用户
    @Bind(R.id.tvDynamicDetailText)         TextView tvDynamicDetailText;//用户的动态文字详情
    @Bind(R.id.tvPraise)                    TextView tvPraise;//用户点赞
    @Bind(R.id.nglDynamicDetailImages)      NineGridlayout nglDynamicDetailImages;//用户的动态的图片
    @Bind(R.id.rvDynamicDetailLove)         RecyclerView rvDynamicDetailLove;//喜欢该条动态的用户列表
    @Bind(R.id.rvDynamicDetailSay)          RecyclerView rvDynamicDetailSay;//评论列表
    @Bind(R.id.llToTalk)                    LinearLayout llToTalk;//橙色布局
    @Bind(R.id.llCancelAndSend)             LinearLayout llCancelAndSend;//输入框布局
    @Bind(R.id.llLastLover)                 LinearLayout llLastLover;//喜欢的人的最后一个布局
    public static EditText etToComment;
    TextView tvSend,tvCancel;
    private KeyboardWatcher keyboardWatcher;
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    private int maxImgCount = 9;               //允许选择图片最大数
    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private List<ImageItem> images=new ArrayList<>();
    public static RecyclerView rvAddPicture;
    public static HomePageCommentEntity.HomePage entity;
    public static boolean isComment=true;
    public static int replyToId=0;
    CommunityLoveAdapter loveAdapter;
    HomePageCommentAdapter commentAdapter;
    String token;
    private String avatarImage;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_dynamic_detail);
        etToComment= (EditText) llCancelAndSend.findViewById(R.id.etToComment);
        tvCancel= (TextView) llCancelAndSend.findViewById(R.id.tvCancel);
        tvSend= (TextView) llCancelAndSend.findViewById(R.id.tvSend);
        setBaseData();
        onRefresh();
        loveAdapter=new CommunityLoveAdapter(null);
        loveAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        loveAdapter.isFirstOnly(false);
        rvDynamicDetailLove.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager manager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvDynamicDetailLove.setLayoutManager(manager1);
        rvDynamicDetailLove.setAdapter(loveAdapter);
        commentAdapter=new HomePageCommentAdapter(null);
        commentAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        commentAdapter.isFirstOnly(false);
        rvDynamicDetailSay.setItemAnimator(new DefaultItemAnimator());
        rvDynamicDetailSay.setLayoutManager(new LinearLayoutManager(this));
        rvDynamicDetailSay.setAdapter(commentAdapter);
        View view= LayoutInflater.from(this).inflate(R.layout.normal_empty_view,null);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(lp2);
        TextView tvEmptyText= (TextView) view.findViewById(R.id.text_empty);
        tvEmptyText.setText("没有更多评论");
        commentAdapter.setEmptyView(view);
        initImagePicker();
        initWidget();
        RefreshUtil.refresh(srlRefresh,this);
        srlRefresh.setOnRefreshListener(this);
        setListeners();
        keyboardWatcher = new KeyboardWatcher(this);
        keyboardWatcher.setListener(this);
    }

    private void setBaseData() {
        SharedPreferences share=getSharedPreferences("UserInfo",MODE_PRIVATE);
        token=share.getString("Token","");
        recommendId=getIntent().getIntExtra("recommendId",0);
        nowUserId=share.getInt("UserId",0);
    }
    private void setListeners() {
        ivDynamicDetailBack.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        tvSend.setOnClickListener(this);
        llToTalk.setOnClickListener(this);
        ivDynamicDetailMore.setOnClickListener(this);
        llLastLover.setOnClickListener(this);
        rivDynamicDetailAvatar.setOnClickListener(this);
        tvDynamicDetailConcern.setOnClickListener(this);
        tvDynamicDetailCancel.setOnClickListener(this);
        tvPraise.setOnClickListener(this);
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
    private void getCommentList() {
        OkGo.get(API.BASE_URL+"/v2/comment/query/"+1+"/"+recommendId)
                .tag(this)
                .execute(new LoadCallback<HomePageCommentEntity>(this) {
                    @Override
                    public void onSuccess(HomePageCommentEntity homePage, Call call, Response response) {
                        if (homePage.state==200){
                            commentAdapter.setNewData(homePage.data);
                            commentAdapter.setOnItemClickListener(new HomePageCommentAdapter.OnItemClickListener() {
                                @Override
                                public void OnItemClick(HomePageCommentEntity.HomePage data,String backTo) {
                                    isComment=false;
                                    entity=data;
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    llToTalk.setVisibility(View.GONE);
                                    etToComment.requestFocus();
                                    etToComment.setHint("回复："+backTo);
                                    llCancelAndSend.setVisibility(View.VISIBLE);
                                    imm.showSoftInput(etToComment,InputMethodManager.SHOW_FORCED);
                                }
                            });
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
    private void getWhoLove() {
        OkGo.get(API.BASE_URL+"/v2/concern/getConcernZanUser")
                .tag(this)
                .params("token",token)
                .params("concernId",recommendId)
                .params("max",5)
                .execute(new LoadCallback<CommunityLoveEntity>(CommunityDynamicDetailActivity.this) {
                    @Override
                    public void onSuccess(CommunityLoveEntity loveUser, Call call, Response response) {
                        List<CommunityLoveEntity.LoveData.LoveUser> list=loveUser.data.userList;
                        if (list.size()==5){
                            llLastLover.setVisibility(View.VISIBLE);
                            list.remove(4);
                        }else {
                            llLastLover.setVisibility(View.GONE);
                        }
                        loveAdapter.setNewData(list);
                    }
                });
    }
    public void setRefreshing(final boolean refreshing) {
        srlRefresh.post(new Runnable() {
            @Override
            public void run() {
                srlRefresh.setRefreshing(refreshing);
            }
        });
    }
    @Override
    public void onRefresh() {
        OkGo.get(API.BASE_URL+"/v2/concern/getOne")
                .tag(this)
                .params("concernId",recommendId)
                .params("accountId",nowUserId)
                .execute(new LoadCallback<LzyResponse<DynamicDetailEntity>>(this) {
                    @Override
                    public void onSuccess(LzyResponse<DynamicDetailEntity> info, Call call, Response response) {
                        dynamicOwnerId=info.data.concern.userIconVo.id;
                        avatarImage=info.data.concern.userIconVo.avatar;
                        userId=info.data.concern.userIconVo.id;
                        String photoUrl=API.PICTURE_URL+info.data.concern.userIconVo.avatar;
                        Picasso.with(CommunityDynamicDetailActivity.this).load(photoUrl).into(rivDynamicDetailAvatar);
                        tvDynamicDetailName.setText(info.data.concern.userIconVo.nickName+"");
                        Object level=info.data.concern.userIconVo.level;
                        if (level.equals("")||level==null){
                            LvUtil.setLv(ivDynamicDetailLevel,"初学乍练");
                        }else {
                            LvUtil.setLv(ivDynamicDetailLevel,info.data.concern.userIconVo.level.pointDesc);
                        }
                        String time=API.simpleDateFormat.format(new Date(info.data.concern.createTime));
                        tvDynamicDetailTime.setText(time);

                        int follow=info.data.concern.follow;
                        boolean isFollowed=(follow==1)?true:false;
                        if (isFollowed){
                            tvDynamicDetailCancel.setVisibility(View.VISIBLE);
                            tvDynamicDetailConcern.setVisibility(View.GONE);
                        }else {
                            tvDynamicDetailConcern.setVisibility(View.VISIBLE);
                            tvDynamicDetailCancel.setVisibility(View.GONE);
                        }
                        if(nowUserId==info.data.concern.userId){
                            tvDynamicDetailCancel.setVisibility(View.GONE);
                            tvDynamicDetailConcern.setVisibility(View.GONE);
                        }
                        tvDynamicDetailText.setText(info.data.concern.topicContent);
                        if(info.data.concern.imgUrl!=null){
                            final ArrayList<String> imageUrls=(ArrayList<String>) StringUtil.stringToList(info.data.concern.imgUrl);
                            NGLAdapter adapter = new NGLAdapter(CommunityDynamicDetailActivity.this, imageUrls);
                            nglDynamicDetailImages.setGap(6);
                            nglDynamicDetailImages.setAdapter(adapter);
                            nglDynamicDetailImages.setOnItemClickListerner(new NineGridlayout.OnItemClickListerner() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    startActivity(ImageDetailActivity.getMyStartIntent(CommunityDynamicDetailActivity.this, imageUrls,position, ImageDetailActivity.url_path));
                                }
                            });
                        }else {
                            nglDynamicDetailImages.setVisibility(View.GONE);
                        }
                        if (nowUserId==dynamicOwnerId){
                            ivDynamicDetailMore.setVisibility(View.VISIBLE);
                        }else {
                            ivDynamicDetailMore.setVisibility(View.GONE);
                        }
                    }
                });

        getWhoLove();
        getCommentList();
    }
    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        switch (v.getId()){
            case R.id.tvPraise:
                if (userId==0){
                    showToast("您还没有登陆呢，亲！");
                }else {
                    FollowUtil.goToPraise(this,token,recommendId,1,dynamicOwnerId,userId);
                }
                break;
            case R.id.tvDynamicDetailConcern:
                FollowUtil.handleFollowTransaction(CommunityDynamicDetailActivity.this,API.BASE_URL+"/v2/follow/add",token,
                        nowUserId,dynamicOwnerId,tvDynamicDetailConcern,tvDynamicDetailCancel);
                break;
            case R.id.tvDynamicDetailCancel:
                FollowUtil.handleFollowTransaction(CommunityDynamicDetailActivity.this,API.BASE_URL+"/v2/follow/unfollow",token,
                        nowUserId,dynamicOwnerId,tvDynamicDetailCancel,tvDynamicDetailConcern);
                break;
            case R.id.tvCancel:
                etToComment.setText("");
                llToTalk.setVisibility(View.VISIBLE);
                llCancelAndSend.setVisibility(View.GONE);
                imm.hideSoftInputFromWindow(etToComment.getWindowToken(), 0);
                break;
            case R.id.rivDynamicDetailAvatar:
                Intent i= new Intent(this, PersonalOtherHome.class);
                i.putExtra("id",userId);
                startActivity(i);
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
            case R.id.ivDynamicDetailMore:
                View view = getLayoutInflater().inflate(R.layout.share_dialog, null);
                final Dialog dialog = new Dialog(CommunityDynamicDetailActivity.this,R.style.Dialog_Fullscreen);
                TextView tvDelete=(TextView)view.findViewById(R.id.tvDelete);
                TextView tvCancel=(TextView)view.findViewById(R.id.tvCancel);
                //删除
                tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DeleteUtil.handleDeleteOtherTransaction(CommunityDynamicDetailActivity.this,"/v2/concern/del/",token,recommendId,userId);
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
            case R.id.ivDynamicDetailBack:
                finish();
                break;
            case R.id.llLastLover:
                Intent intent=new Intent(this, CommunityMoreLoveActivity.class);
                intent.putExtra("token",token);
                intent.putExtra("concernId",recommendId);
                startActivity(intent);
                break;
        }
    }
    private void writeToCallBack(){
        final String uri = API.BASE_URL + "/v2/comment/insert";
        String tempText = etToComment.getText().toString().trim();
        final String commentText= EmojiFilterUtil.filterEmoji(CommunityDynamicDetailActivity.this,tempText);
        if (nowUserId==0){
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
                                            .params("comment_type", "1")
                                            .params("comment_id", recommendId + "")
                                            .params("model_create_id", "-1")
                                            .params("comment_account", nowUserId + "")
                                            .params("comment_content", commentText)
                                            .params("comment_img_path", imgUrl)
                                            .execute(new LoadCallback<LzyResponse>(CommunityDynamicDetailActivity.this) {
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
                                params.put("comment_type", "1");
                                params.put("comment_id", recommendId + "");
                                params.put("model_create_id", "-1");
                                params.put("comment_account", nowUserId + "");
                                params.put("comment_content", commentText);
                                String result = PostUtil.sendPostMessage(uri, params);
                                JSONObject obj = new JSONObject(result);
                                int state = obj.getInt("state");
                                if (state == 200) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(CommunityDynamicDetailActivity.this, "评论成功！", Toast.LENGTH_LONG).show();
                                            etToComment.setText("");
                                            llToTalk.setVisibility(View.VISIBLE);
                                            llCancelAndSend.setVisibility(View.GONE);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                            imm.hideSoftInputFromWindow(etToComment.getWindowToken(), 0);
                                            onRefresh();
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
                    int replyFromUser=nowUserId;
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
                                Toast.makeText(CommunityDynamicDetailActivity.this, "回复成功！", Toast.LENGTH_LONG).show();
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
        Log.i("tag","1111111111111111111BBBBBBBBBBB");
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
        Log.i("tag","22222222222222222BBBBBBBBBBBBB");
        llCancelAndSend.setVisibility(View.GONE);
        llToTalk.setVisibility(View.VISIBLE);
        isComment=true;
        etToComment.setHint("你想说点什么？");
    }
    boolean isFirstIn=true;
    @Override
    protected void onResume() {
        super.onResume();
        Log.i("tag","33333333333333333333BBBBBBBBBBBBBB");
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
        Log.i("tag","55555555555555555BBBBBBBBBBBBBBBBB");
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
