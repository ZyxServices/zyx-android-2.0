package com.tiyujia.homesport.common.homepage.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import android.text.InputFilter;
import android.text.Spanned;
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
import com.tiyujia.homesport.common.homepage.adapter.NGLAdapter;
import com.tiyujia.homesport.common.homepage.entity.HomePageCommentEntity;
import com.tiyujia.homesport.common.homepage.entity.HomePageVenueWhomGoneEntity;
import com.tiyujia.homesport.common.homepage.entity.VenueWholeBean;
import com.tiyujia.homesport.common.personal.activity.PersonalLogin;
import com.tiyujia.homesport.entity.ImageUploadModel;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.entity.LzyResponse;
import com.tiyujia.homesport.util.CacheUtils;
import com.tiyujia.homesport.util.DegreeUtil;
import com.tiyujia.homesport.util.EmojiFilterUtil;
import com.tiyujia.homesport.util.JSONParseUtil;
import com.tiyujia.homesport.util.KeyboardWatcher;
import com.tiyujia.homesport.util.PicUtil;
import com.tiyujia.homesport.util.PicassoUtil;
import com.tiyujia.homesport.util.PictureUtil;
import com.tiyujia.homesport.util.PostUtil;
import com.tiyujia.homesport.util.RefreshUtil;
import com.tiyujia.homesport.util.StringUtil;
import com.tiyujia.homesport.widget.GlideImageLoader;
import com.tiyujia.homesport.widget.ImagePickerAdapter;
import com.w4lle.library.NineGridlayout;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;

//1
public class HomePageSearchResultActivity extends NewBaseActivity implements View.OnClickListener,KeyboardWatcher.OnKeyboardToggleListener,SwipeRefreshLayout.OnRefreshListener,
         ImagePickerAdapter.OnRecyclerViewItemClickListener{
    @Bind(R.id.rvHomePageVenueDetailWhomGone) RecyclerView rvHomePageVenueDetailWhomGone;
    @Bind(R.id.rvHomePageVenueDetailSay) RecyclerView rvHomePageVenueDetailSay;
    @Bind(R.id.ivVenueDetailBack)       ImageView ivVenueDetailBack;//左上角返回按钮
    @Bind(R.id.nglVenueDetail)          ImageView nglVenueDetail;//顶部大图片集合
    @Bind(R.id.tvVenueDetailName)       TextView tvVenueDetailName;//攀岩馆名称
    @Bind(R.id.tvVenueTypeA)            TextView tvVenueTypeA;//类型A
    @Bind(R.id.tvVenueTypeB)            TextView tvVenueTypeB;//类型B
    @Bind(R.id.ivDegree1)               ImageView ivDegree1;//难度显示图片1
    @Bind(R.id.ivDegree2)               ImageView ivDegree2;
    @Bind(R.id.ivDegree3)               ImageView ivDegree3;
    @Bind(R.id.ivDegree4)               ImageView ivDegree4;
    @Bind(R.id.ivDegree5)               ImageView ivDegree5;//难度显示图片5
    @Bind(R.id.tvVenueDetailAddress)    TextView tvVenueDetailAddress;//场馆详细地址
    @Bind(R.id.tvVenuePhone)            TextView tvVenuePhone;//联系电话
    @Bind(R.id.wvVenueDetail)           WebView wvVenueDetail;//场馆基本介绍
    @Bind(R.id.llToTalk)                LinearLayout llToTalk;//橙色布局
    @Bind(R.id.llCancelAndSend)         LinearLayout llCancelAndSend;//输入框布局
    @Bind(R.id.srlRefresh)              SwipeRefreshLayout srlRefresh;//下拉刷新
    public static EditText etToComment;
    TextView tvSend,tvCancel;
    List<HomePageVenueWhomGoneEntity> list;
    HomePageVenueUserAdapter userAdapter;
    HomePageCommentAdapter commentAdapter;
    private KeyboardWatcher keyboardWatcher;
    VenueWholeBean data;
    int venueId;//场馆ID
    int nowUserId;//当前用户ID
    String token;
    public static final int HANDLE_BASE_DATA=1;
    public static final int HANDLE_BASE_VENUE_DATA=2;
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
     SharedPreferences share;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HANDLE_BASE_DATA:
                    userAdapter=new HomePageVenueUserAdapter(HomePageSearchResultActivity.this,list);
                    LinearLayoutManager manager1 = new LinearLayoutManager(HomePageSearchResultActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    rvHomePageVenueDetailWhomGone.setLayoutManager(manager1);
                    rvHomePageVenueDetailWhomGone.setAdapter(userAdapter);
                    break;
                case HANDLE_BASE_VENUE_DATA:
                    VenueWholeBean bean= (VenueWholeBean) msg.obj;
                    if (bean!=null){
                        tvVenueDetailName.setText(bean.getVenueName());
                        tvVenuePhone.setText(bean.getVenuePhone());
                        tvVenueDetailAddress.setText(bean.getVenueAddress());
                        tvVenueTypeA.setText((bean.getVenueType()==1)?"室内":"室外");
                        int degree=bean.getVenueDegree();
                        DegreeUtil.handleDegrees(degree,ivDegree1,ivDegree2,ivDegree3,ivDegree4,ivDegree5);
                        WindowManager wm = (WindowManager) HomePageSearchResultActivity.this.getSystemService(Context.WINDOW_SERVICE);
                        int width = wm.getDefaultDisplay().getWidth();
                        if(width > 720){wvVenueDetail.setInitialScale(190);}
                        else if(width > 520){
                            wvVenueDetail.setInitialScale(160);
                        }else if(width > 450){
                            wvVenueDetail.setInitialScale(140);
                        }else if(width > 300){
                            wvVenueDetail.setInitialScale(120);
                        }else{
                            wvVenueDetail.setInitialScale(100);
                        }
                        DisplayMetrics dm = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(dm);
                        if (dm.densityDpi > 240 ) {
                            wvVenueDetail.getSettings().setDefaultFontSize(20); //可以取1-72之间的任意值，默认16
                        }
                        wvVenueDetail.loadDataWithBaseURL(null, bean.getVenueDescription(), "text/html", "utf-8", null);
                        wvVenueDetail.getSettings().setJavaScriptEnabled(true);
                        // 设置启动缓存 ;
                        wvVenueDetail.getSettings().setAppCacheEnabled(true);
                        wvVenueDetail.setWebChromeClient(new WebChromeClient());
                        wvVenueDetail.getSettings().setJavaScriptEnabled(true);
                        final String picUrls= bean.getVenueImages();
                        if (picUrls==null){
                            nglVenueDetail.setVisibility(View.GONE);
                        }else {
                            PicassoUtil.handlePic(HomePageSearchResultActivity.this, PicUtil.getImageUrlDetail(HomePageSearchResultActivity.this, StringUtil.isNullAvatar(picUrls), 1280, 720),nglVenueDetail,1280,720);
                            final ArrayList<String>imagelist =new ArrayList<String>();
                            imagelist.add(API.PICTURE_URL+picUrls);
                            nglVenueDetail.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(ImageDetailActivity.getMyStartIntent(HomePageSearchResultActivity.this, imagelist,0, ImageDetailActivity.url_path));
                                }
                            });
                        }
                    }else {
                        Toast.makeText(HomePageSearchResultActivity.this,"服务器异常，请等待",Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_search_result);
        etToComment= (EditText) llCancelAndSend.findViewById(R.id.etToComment);
        tvCancel= (TextView) llCancelAndSend.findViewById(R.id.tvCancel);
        tvSend= (TextView) llCancelAndSend.findViewById(R.id.tvSend);
        commentAdapter=new HomePageCommentAdapter(null);
        commentAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        commentAdapter.isFirstOnly(false);
        rvHomePageVenueDetailSay.setItemAnimator(new DefaultItemAnimator());
        rvHomePageVenueDetailSay.setLayoutManager(new LinearLayoutManager(this));
        rvHomePageVenueDetailSay.setAdapter(commentAdapter);
        View view= LayoutInflater.from(HomePageSearchResultActivity.this).inflate(R.layout.normal_empty_view,null);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp2);
        TextView tvEmptyText= (TextView) view.findViewById(R.id.text_empty);
        tvEmptyText.setText("没有更多评论");
        commentAdapter.setEmptyView(view);
        RefreshUtil.refresh(srlRefresh,this);
        srlRefresh.setOnRefreshListener(this);
        setVenueData();
        onRefresh();
        initImagePicker();
        initWidget();
        setListeners();
        keyboardWatcher = new KeyboardWatcher(this);
        keyboardWatcher.setListener(this);
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
    private void setListeners() {
        ivVenueDetailBack.setOnClickListener(this);
        tvVenuePhone.setOnClickListener(this);
        llToTalk.setOnClickListener(this);
        tvSend.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }
    private void setVenueData() {
        venueId=getIntent().getIntExtra("venueId",0);
        ArrayList<String> cacheData= (ArrayList<String>) CacheUtils.readJson(HomePageSearchResultActivity.this, HomePageSearchResultActivity.this.getClass().getName() + "_"+venueId+".json");
        if (cacheData==null||cacheData.size()==0) {
            new Thread() {
                @Override
                public void run() {
                    String uri = API.BASE_URL + "/v2/venue/findVenueById";
                    HashMap<String, String> params = new HashMap<>();
                    params.put("venueId", ""+venueId);
                    String result = PostUtil.sendPostMessage(uri, params);
                    JSONParseUtil.parseNetDataVenueDetail(HomePageSearchResultActivity.this,result, HomePageSearchResultActivity.this.getClass().getName()+"_"+venueId+".json",data, handler, HANDLE_BASE_VENUE_DATA);
                }
            }.start();
        }else {
            JSONParseUtil.parseLocalDataVenueDetail(HomePageSearchResultActivity.this, HomePageSearchResultActivity.this.getClass().getName()+"_"+venueId+".json",data, handler, HANDLE_BASE_VENUE_DATA);
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
    @Override
    public void onRefresh()  {
        list=new ArrayList<>();
        final String url=API.BASE_URL+"/v2/record/users";
        new Thread(){
            @Override
            public void run() {
                try{
                    HashMap<String, String> params = new HashMap<>();
                    params.put("venueId", ""+venueId);
                    params.put("pageSize", "4");
                    params.put("pageNum", "1");
                    String result = PostUtil.sendPostMessage(url, params);
                    JSONObject obj = new JSONObject(result);
                    int state=obj.getInt("state");
                    if (state==200){
                        JSONArray array=obj.getJSONArray("data");
                        if (array.length()!=0){
                            for (int i=0;i<array.length();i++){
                                JSONObject jsonObj=array.getJSONObject(i);
                                HomePageVenueWhomGoneEntity entity=new HomePageVenueWhomGoneEntity();
                                entity.setVenueId(venueId);
                                entity.setUserId(jsonObj.getInt("id"));
                                entity.setUserName(jsonObj.getString("nickName"));
                                entity.setUserPhotoUrl(StringUtil.isNullAvatar(jsonObj.getString("avatar")));
                                entity.setUserLevelUrl(jsonObj.getString("levelName")==null?"初学乍练":jsonObj.getString("levelName"));
                                entity.setAuthenticate(jsonObj.getString("authenticate")==null?"":jsonObj.getString("authenticate"));
                                entity.setStep(jsonObj.getString("step")==null?"":jsonObj.getString("step"));
                                entity.setLevel(jsonObj.getString("level")==null?"":jsonObj.getString("level"));
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
        OkGo.get(API.BASE_URL+"/v2/comment/query/"+4+"/"+venueId)
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
            case R.id.ivVenueDetailBack:
                finish();
                break;
            case R.id.tvVenuePhone:
                final AlertDialog builder = new AlertDialog.Builder(HomePageSearchResultActivity.this).create();
                builder.setView(getLayoutInflater().inflate(R.layout.call_phone_dialog, null));
                builder.show();
                //去掉dialog四边的黑角
                builder.getWindow().setBackgroundDrawable(new BitmapDrawable());
                TextView text=(TextView) builder.getWindow().findViewById(R.id.text);
                text.setText("直接拨打"+tvVenuePhone.getText()+"?");
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
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+tvVenuePhone.getText().toString().trim()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        builder.dismiss();
                    }
                });
                break;

        }
    }
    private void writeToCallBack(){
        final String uri = API.BASE_URL + "/v2/comment/insert";
        share= getSharedPreferences("UserInfo", MODE_PRIVATE);
        token= share.getString("Token","");
        nowUserId = share.getInt("UserId", 0);
        String tempText = etToComment.getText().toString().trim();
        final String commentText = EmojiFilterUtil.filterEmoji(HomePageSearchResultActivity.this,tempText);
        if (nowUserId==0){
            Toast.makeText(this,"您还没有登录呢，亲！请登录！",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(this, PersonalLogin.class);
            startActivity(intent);
        }else {
            if (isComment) {
                if (images.size()!=0) {
                    ArrayList<File> files = new ArrayList<>();
                    for (int i = 0; i < images.size(); i++) {
                        Bitmap bitmap= PictureUtil.getSmallBitmap(images.get(i).path);
                        File fil=PictureUtil.saveBitmapFile(bitmap,images.get(i).path);
                        files.add(fil);
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
                                            .params("comment_type", "4")
                                            .params("comment_id", venueId + "")
                                            .params("model_create_id", "-1")
                                            .params("comment_account", nowUserId + "")
                                            .params("comment_content", commentText)
                                            .params("comment_img_path", imgUrl)
                                            .execute(new LoadCallback<LzyResponse>(HomePageSearchResultActivity.this) {
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
                                params.put("comment_type", "4");
                                params.put("comment_id", venueId + "");
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
                                            Toast.makeText(HomePageSearchResultActivity.this, "评论成功！", Toast.LENGTH_LONG).show();
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
                                            Toast.makeText(HomePageSearchResultActivity.this, "登陆已过期，请重新登陆！", Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(HomePageSearchResultActivity.this,PersonalLogin.class);
                                            HomePageSearchResultActivity.this.startActivity(intent);
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
                    String token=share.getString("Token","");
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
                                Toast.makeText(HomePageSearchResultActivity.this, "回复成功！", Toast.LENGTH_LONG).show();
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
                Intent intent = new Intent(HomePageSearchResultActivity.this, ImageGridActivity.class);
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
