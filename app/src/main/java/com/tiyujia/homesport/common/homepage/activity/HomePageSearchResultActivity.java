package com.tiyujia.homesport.common.homepage.activity;

import android.app.AlertDialog;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
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
import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.NewBaseActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.adapter.CommentListAdapter;
import com.tiyujia.homesport.common.homepage.adapter.HomePageCommentAdapter;
import com.tiyujia.homesport.common.homepage.adapter.HomePageVenueUserAdapter;
import com.tiyujia.homesport.common.homepage.adapter.NGLAdapter;
import com.tiyujia.homesport.common.homepage.entity.HomePageCommentEntity;
import com.tiyujia.homesport.common.homepage.entity.HomePageVenueWhomGoneEntity;
import com.tiyujia.homesport.common.homepage.entity.VenueWholeBean;
import com.tiyujia.homesport.common.personal.activity.PersonalLogin;
import com.tiyujia.homesport.common.personal.model.MyDynamicModel;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.util.CacheUtils;
import com.tiyujia.homesport.util.DegreeUtil;
import com.tiyujia.homesport.util.JSONParseUtil;
import com.tiyujia.homesport.util.KeyboardWatcher;
import com.tiyujia.homesport.util.PostUtil;
import com.tiyujia.homesport.util.RefreshUtil;
import com.tiyujia.homesport.util.StringUtil;
import com.w4lle.library.NineGridlayout;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;

//1
public class HomePageSearchResultActivity extends NewBaseActivity implements View.OnClickListener,KeyboardWatcher.OnKeyboardToggleListener,SwipeRefreshLayout.OnRefreshListener{
    @Bind(R.id.rvHomePageVenueDetailWhomGone) RecyclerView rvHomePageVenueDetailWhomGone;
    @Bind(R.id.rvHomePageVenueDetailSay) RecyclerView rvHomePageVenueDetailSay;
    @Bind(R.id.ivVenueDetailBack)       ImageView ivVenueDetailBack;//左上角返回按钮
    @Bind(R.id.ivVenueDetailMore)       ImageView ivVenueDetailMore;//右上角更多选择按钮
    @Bind(R.id.nglVenueDetail)          NineGridlayout nglVenueDetail;//顶部大图片集合
    @Bind(R.id.tvVenueDetailName)       TextView tvVenueDetailName;//攀岩馆名称
    @Bind(R.id.tvVenueTypeA)            TextView tvVenueTypeA;//类型A
    @Bind(R.id.tvVenueTypeB)            TextView tvVenueTypeB;//类型B
    @Bind(R.id.ivDegree1)               ImageView ivDegree1;//难度显示图片1
    @Bind(R.id.ivDegree2)               ImageView ivDegree2;
    @Bind(R.id.ivDegree3)               ImageView ivDegree3;
    @Bind(R.id.ivDegree4)               ImageView ivDegree4;
    @Bind(R.id.ivDegree5)               ImageView ivDegree5;//难度显示图片5
    @Bind(R.id.tvVenueDetailAddress)    TextView tvVenueDetailAddress;//场馆详细地址
    @Bind(R.id.tvMeGone)                TextView tvMeGone;//我去过标签
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
    public static final int HANDLE_BASE_DATA=1;
    public static final int HANDLE_BASE_VENUE_DATA=2;
    public static final  int REPLY_TYPE_ONE=3;
    public static final  int REPLY_TYPE_TWO=4;
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
                        if(width > 520){
                            wvVenueDetail.setInitialScale(160);
                        }else if(width > 450){
                            wvVenueDetail.setInitialScale(140);
                        }else if(width > 300){
                            wvVenueDetail.setInitialScale(120);
                        }else{
                            wvVenueDetail.setInitialScale(100);
                        }
                        wvVenueDetail.loadDataWithBaseURL(null, bean.getVenueDescription(), "text/html", "utf-8", null);
                        wvVenueDetail.getSettings().setJavaScriptEnabled(true);
                        // 设置启动缓存 ;
                        wvVenueDetail.getSettings().setAppCacheEnabled(true);
                        wvVenueDetail.setWebChromeClient(new WebChromeClient());
                        wvVenueDetail.getSettings().setJavaScriptEnabled(true);
                        List<String> picUrls=bean.getVenueImages();
                        if (picUrls!=null&&picUrls.size()!=0){

                        }else {
                            String url=API.PICTURE_URL+"group1/M00/00/11/dz1CN1g_lDSACHFEAAH4y2kp7rw197.jpg";
                            picUrls.add(url);
                            picUrls.add(url);
                            picUrls.add(url);
                            picUrls.add(url);
                            picUrls.add(url);
                        }
                        NGLAdapter adapter = new NGLAdapter(HomePageSearchResultActivity.this, picUrls);
                        nglVenueDetail.setGap(6);
                        nglVenueDetail.setAdapter(adapter);
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
        RefreshUtil.refresh(srlRefresh,this);
        srlRefresh.setOnRefreshListener(this);
        setVenueData();
        onRefresh();
        setListeners();
        keyboardWatcher = new KeyboardWatcher(this);
        keyboardWatcher.setListener(this);
    }
    private void setListeners() {
        ivVenueDetailBack.setOnClickListener(this);
        ivVenueDetailMore.setOnClickListener(this);
        tvVenuePhone.setOnClickListener(this);
        llToTalk.setOnClickListener(this);
        tvSend.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }
    private void setVenueData() {
        venueId=getIntent().getIntExtra("venueId",0);
        ArrayList<String> cacheData= (ArrayList<String>) CacheUtils.readJson(HomePageSearchResultActivity.this, HomePageSearchResultActivity.this.getClass().getName() + "_1.json");
        if (cacheData==null||cacheData.size()==0) {
            new Thread() {
                @Override
                public void run() {
                    String uri = API.BASE_URL + "/v2/venue/findVenueById";
                    HashMap<String, String> params = new HashMap<>();
                    params.put("venueId", ""+venueId);
                    String result = PostUtil.sendPostMessage(uri, params);
                    JSONParseUtil.parseNetDataVenueDetail(HomePageSearchResultActivity.this,result, HomePageSearchResultActivity.this.getClass().getName()+"_1.json",data, handler, HANDLE_BASE_VENUE_DATA);
                }
            }.start();
        }else {
            JSONParseUtil.parseLocalDataVenueDetail(HomePageSearchResultActivity.this, HomePageSearchResultActivity.this.getClass().getName()+"_1.json",data, handler, HANDLE_BASE_VENUE_DATA);
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
        final int [] pictures={R.drawable.demo_05,R.drawable.demo_06,R.drawable.demo_10,R.drawable.demo_10};
        final String []userName={"土拨鼠","萌小妹","小美爱赵伟","萌小美美"};
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
                        }else {
                            for (int i=0;i<4;i++){
                                HomePageVenueWhomGoneEntity entity=new HomePageVenueWhomGoneEntity();
                                entity.setUserPhotoUrl(pictures[i]+"");
                                entity.setUserName(userName[i]);
                                entity.setUserLevelUrl("初学乍练");
                                list.add(entity);
                            }
                        }
                    }
                    HomePageVenueWhomGoneEntity empty=new HomePageVenueWhomGoneEntity();
                    list.add(empty);
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
                imm.showSoftInput(etToComment,InputMethodManager.SHOW_FORCED);
                break;
            case R.id.ivVenueDetailBack:
                finish();
                break;
            case R.id.ivVenueDetailMore:
                Toast.makeText(HomePageSearchResultActivity.this,"吐司",Toast.LENGTH_LONG).show();
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
        nowUserId = share.getInt("UserId", 0);
        if (nowUserId==0){
            Toast.makeText(this,"您还没有登录呢，亲！请登录！",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(this, PersonalLogin.class);
            startActivity(intent);
        }else {
            if (isComment) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("comment_type", "4");
                            params.put("comment_id", venueId + "");
                            params.put("model_create_id", "-1");
                            params.put("comment_account", nowUserId + "");
                            String commentText = etToComment.getText().toString().trim();
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
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }else {
                handleReply(replyToId);
            }
        }
    }
    private void handleReply(final int replyToId){
        new Thread(){
            @Override
            public void run() {
                try{
                    String url=API.BASE_URL+"/v2/reply/addReply";
                    String token=share.getString("Token","");
                    int replyParentId=entity.id;
                    int replyFromUser=nowUserId;
                    int replyToUser=(replyToId==0)?entity.userVo.id:replyToId;
                    String replyContent=etToComment.getText().toString().trim();
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
        llToTalk.setVisibility(View.GONE);
    }
    @Override
    public void onKeyboardClosed() {
        llCancelAndSend.setVisibility(View.GONE);
        llToTalk.setVisibility(View.VISIBLE);
    }
}
