package com.tiyujia.homesport.common.homepage.activity;

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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
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
import com.tiyujia.homesport.NewBaseActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.adapter.HomePageCommentAdapter;
import com.tiyujia.homesport.common.homepage.entity.ArticleModel;
import com.tiyujia.homesport.common.homepage.entity.HomePageCommentEntity;
import com.tiyujia.homesport.common.personal.activity.PersonalLogin;
import com.tiyujia.homesport.common.personal.activity.PersonalOtherHome;
import com.tiyujia.homesport.common.personal.model.UserInfoModel;
import com.tiyujia.homesport.entity.ImageUploadModel;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.entity.LzyResponse;
import com.tiyujia.homesport.util.EmojiFilterUtil;
import com.tiyujia.homesport.util.KeyboardWatcher;
import com.tiyujia.homesport.util.LvUtil;
import com.tiyujia.homesport.util.PicUtil;
import com.tiyujia.homesport.util.PicassoUtil;
import com.tiyujia.homesport.util.PostUtil;
import com.tiyujia.homesport.util.RefreshUtil;
import com.tiyujia.homesport.util.StringUtil;
import com.tiyujia.homesport.widget.GlideImageLoader;
import com.tiyujia.homesport.widget.ImagePickerAdapter;
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
 * 作者: Cymbi on 2016/12/6 19:19.
 * 邮箱:928902646@qq.com
 */

public class HomePageArticleActivity extends NewBaseActivity implements View.OnClickListener,KeyboardWatcher.OnKeyboardToggleListener,SwipeRefreshLayout.OnRefreshListener,
        ImagePickerAdapter.OnRecyclerViewItemClickListener{
    @Bind(R.id.ivBack)                      ImageView ivBack;
    @Bind(R.id.ivAvatar)                    ImageView ivAvatar;
    @Bind(R.id.ivLv)                        ImageView ivLv;
    @Bind(R.id.tvNickname)                  TextView tvNickname;
    @Bind(R.id.tvTime)                      TextView tvTime;
    @Bind(R.id.tv_not)                      TextView tv_not;
    @Bind(R.id.tv_yes)                      TextView tv_yes;
    @Bind(R.id.tvTitle)                     TextView tvTitle;
    @Bind(R.id.tvPraise)                    TextView tvPraise;
    @Bind(R.id.webview)                     WebView webview;
    @Bind(R.id.llToTalk)                    LinearLayout llToTalk;//橙色布局
    @Bind(R.id.llCancelAndSend)             LinearLayout llCancelAndSend;//输入框布局
    @Bind(R.id.srlRefresh)                  SwipeRefreshLayout srlRefresh;//下拉刷新
    @Bind(R.id.rvHomePageActicleDetail)     RecyclerView rvHomePageActicleDetail;
    private String token="tiyujia2016";
    String userToken;
    int nowUserId;//当前用户ID
    int userId;//发布者ID
    int modelId;//文章ID
    private int maxImgCount = 9;               //允许选择图片最大数
    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private List<ImageItem> images=new ArrayList<>();
    public static RecyclerView rvAddPicture;
    public static HomePageCommentEntity.HomePage entity;
    public static boolean isComment=true;
    public static int replyToId=0;
    SharedPreferences share;
    public static EditText etToComment;
    TextView tvSend,tvCancel;
    HomePageCommentAdapter commentAdapter;
    private KeyboardWatcher keyboardWatcher;
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_info);
        getBaseData();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        modelId= getIntent().getIntExtra("id",0);
        OkGo.post(API.BASE_URL+"/v2/city/findCourseById")
                .tag(this)
                .params("courseId",modelId)
                .execute(new LoadCallback<ArticleModel>(this) {
                    @Override
                    public void onSuccess(ArticleModel articleModel, Call call, Response response) {
                        tvTitle.setText(articleModel.data.title);
                        WindowManager wm = (WindowManager) HomePageArticleActivity.this.getSystemService(Context.WINDOW_SERVICE);
                        int width = wm.getDefaultDisplay().getWidth();
                        if(width > 720){webview.setInitialScale(190);}
                        else if(width > 520){
                            webview.setInitialScale(160);
                        }else if(width > 450){
                            webview.setInitialScale(140);
                        }else if(width > 300){
                            webview.setInitialScale(120);
                        }else{
                            webview.setInitialScale(100);
                        }
                        DisplayMetrics dm = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(dm);
                        if (dm.densityDpi > 240 ) {
                            webview.getSettings().setDefaultFontSize(20); //可以取1-72之间的任意值，默认16
                        }
                        webview.loadDataWithBaseURL(null, articleModel.data.content, "text/html", "utf-8", null);
                        webview.getSettings().setJavaScriptEnabled(true);
                        // 设置启动缓存 ;
                        webview.getSettings().setAppCacheEnabled(true);
                        webview.setWebChromeClient(new WebChromeClient());
                        webview.getSettings().setJavaScriptEnabled(true);
                        userId=articleModel.data.userId;
                        final long createTime=articleModel.data.createTime;
                        OkGo.get(API.BASE_URL+"/v2/user/center_info")
                                .tag(this)
                                .params("token",token)
                                .params("account_id",userId)
                                .execute(new LoadCallback<UserInfoModel>(HomePageArticleActivity.this) {
                                    @Override
                                    public void onSuccess(final UserInfoModel userInfoModel, Call call, Response response) {
                                        if(userInfoModel.state==200){
                                            tvNickname.setText(userInfoModel.data.nickname);
                                            tvTime.setText(API.simpleYear.format(createTime));
                                            PicassoUtil.handlePic(HomePageArticleActivity.this, PicUtil.getImageUrlDetail(HomePageArticleActivity.this, StringUtil.isNullAvatar(userInfoModel.data.avatar), 320, 320), ivAvatar, 320, 320);
                                            if (userInfoModel.data.level!=null&&userInfoModel.data.level.equals("")){
                                                LvUtil.setLv(ivLv,userInfoModel.data.level.pointDesc);
                                            }else {
                                                LvUtil.setLv(ivLv,"初学乍练");
                                            }
                                            ivAvatar.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent=new Intent(HomePageArticleActivity.this,PersonalOtherHome.class);
                                                    intent.putExtra("id",userId);
                                                    HomePageArticleActivity.this.startActivity(intent);
                                                }
                                            });
                                        }
                                    }
                                    @Override
                                    public void onError(Call call, Response response, Exception e) {
                                        super.onError(call, response, e);
                                        showToast("用户信息查询失败");
                                    }
                                });
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        showToast("服务器故障");
                    }
                });
        etToComment= (EditText) llCancelAndSend.findViewById(R.id.etToComment);
        tvCancel= (TextView) llCancelAndSend.findViewById(R.id.tvCancel);
        tvSend= (TextView) llCancelAndSend.findViewById(R.id.tvSend);
        commentAdapter=new HomePageCommentAdapter(null);
        commentAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        commentAdapter.isFirstOnly(false);
        rvHomePageActicleDetail.setItemAnimator(new DefaultItemAnimator());
        rvHomePageActicleDetail.setLayoutManager(new LinearLayoutManager(this));
        rvHomePageActicleDetail.setAdapter(commentAdapter);
        View view= LayoutInflater.from(this).inflate(R.layout.normal_empty_view,null);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp2);
        TextView tvEmptyText= (TextView) view.findViewById(R.id.text_empty);
        tvEmptyText.setText("没有更多评论");
        commentAdapter.setEmptyView(view);
        RefreshUtil.refresh(srlRefresh,this);
        srlRefresh.setOnRefreshListener(this);
        onRefresh();
        initImagePicker();
        initWidget();
        setListeners();
        keyboardWatcher = new KeyboardWatcher(this);
        keyboardWatcher.setListener(this);
    }

    private void getBaseData() {
        share= getSharedPreferences("UserInfo", MODE_PRIVATE);
        nowUserId = share.getInt("UserId", 0);
        userToken=share.getString("Token","");
    }

    private void setListeners() {
        llToTalk.setOnClickListener(this);
        tvSend.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        ivAvatar.setOnClickListener(this);
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
    @Override
    public void onRefresh() {
        OkGo.get(API.BASE_URL+"/v2/comment/query/"+0+"/"+modelId)
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
    public void setRefreshing(final boolean refreshing) {
        srlRefresh.post(new Runnable() {
            @Override
            public void run() {
                srlRefresh.setRefreshing(refreshing);
            }
        });
    }
    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        switch (v.getId()) {
            case R.id.tvCancel:
                etToComment.setText("");
                llToTalk.setVisibility(View.VISIBLE);
                llCancelAndSend.setVisibility(View.GONE);
                imm.hideSoftInputFromWindow(etToComment.getWindowToken(), 0);
                break;
            case R.id.tvSend:
                if (TextUtils.isEmpty(etToComment.getText().toString())) {
                    Toast.makeText(this, "请输入评论或回复内容！", Toast.LENGTH_LONG).show();
                } else {
                    writeToCallBack();
                }
                break;
            case R.id.ivAvatar:
                Intent intent=new Intent(HomePageArticleActivity.this,PersonalOtherHome.class);
                intent.putExtra("id",userId);
                HomePageArticleActivity.this.startActivity(intent);
                break;
            case R.id.llToTalk:
                llToTalk.setVisibility(View.GONE);
                etToComment.requestFocus();
                llCancelAndSend.setVisibility(View.VISIBLE);
                if (isComment) {
                    rvAddPicture.setVisibility(View.VISIBLE);
                } else {
                    rvAddPicture.setVisibility(View.GONE);
                }
                imm.showSoftInput(etToComment, InputMethodManager.SHOW_FORCED);
                break;
        }
    }

    private void writeToCallBack(){
        final String uri = API.BASE_URL + "/v2/comment/insert";
        String tempText = etToComment.getText().toString().trim();
        final String commentText=EmojiFilterUtil.filterEmoji(HomePageArticleActivity.this,tempText);
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
                                            .params("token",userToken)
                                            .params("comment_type", "0")
                                            .params("comment_id", modelId + "")
                                            .params("model_create_id", "-1")
                                            .params("comment_account", nowUserId + "")
                                            .params("comment_content", commentText)
                                            .params("comment_img_path", imgUrl)
                                            .execute(new LoadCallback<LzyResponse>(HomePageArticleActivity.this) {
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
                                params.put("token", userToken);
                                params.put("comment_type", "0");
                                params.put("comment_id", modelId + "");
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
                                            Toast.makeText(HomePageArticleActivity.this, "评论成功！", Toast.LENGTH_LONG).show();
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
                    params.put("token",""+userToken);
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
                                Toast.makeText(HomePageArticleActivity.this, "回复成功！", Toast.LENGTH_LONG).show();
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
        OkGo.getInstance().cancelTag(this);
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
                llCancelAndSend.setVisibility(View.VISIBLE);
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                selImageList.clear();
                selImageList.addAll(images);
                adapter.setImages(selImageList);
                llCancelAndSend.setVisibility(View.VISIBLE);
            }
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(etToComment,InputMethodManager.SHOW_FORCED);
    }
    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etToComment.getWindowToken(), 0);
                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                Intent intent = new Intent(HomePageArticleActivity.this, ImageGridActivity.class);
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
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomePageArticleActivity.this, PersonalOtherHome.class);
                intent.putExtra("id",userId);
                startActivity(intent);
            }
        });

    }

}
