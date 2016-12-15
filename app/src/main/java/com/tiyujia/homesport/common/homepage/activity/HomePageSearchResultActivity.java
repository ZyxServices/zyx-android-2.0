package com.tiyujia.homesport.common.homepage.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.NewBaseActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.adapter.HomePageDiscussAdapter;
import com.tiyujia.homesport.common.homepage.adapter.HomePageVenueUserAdapter;
import com.tiyujia.homesport.common.homepage.adapter.NGLAdapter;
import com.tiyujia.homesport.common.homepage.entity.CallBackDetailEntity;
import com.tiyujia.homesport.common.homepage.entity.HomePageDiscussEntity;
import com.tiyujia.homesport.common.homepage.entity.HomePageVenueWhomGoneEntity;
import com.tiyujia.homesport.common.homepage.entity.VenueWholeBean;
import com.tiyujia.homesport.util.CacheUtils;
import com.tiyujia.homesport.util.DegreeUtil;
import com.tiyujia.homesport.util.JSONParseUtil;
import com.tiyujia.homesport.util.PostUtil;
import com.tiyujia.homesport.util.StringUtil;
import com.w4lle.library.NineGridlayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import butterknife.Bind;
//1
public class HomePageSearchResultActivity extends NewBaseActivity implements View.OnClickListener{
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
    LinearLayout llCancelAndSend;//输入框布局
    EditText etToComment;
    TextView tvSend,tvCancel;
    List<HomePageVenueWhomGoneEntity> list;
    HomePageVenueUserAdapter userAdapter;
    List<HomePageDiscussEntity> mValue;
    HomePageDiscussAdapter discussAdapter;
    VenueWholeBean data;
    int venueId;//场馆ID
    int nowUserId;//当前用户ID
    Dialog dialog;
    public static final int HANDLE_BASE_DATA=1;
    public static final int HANDLE_BASE_VENUE_DATA=2;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HANDLE_BASE_DATA:
                    userAdapter=new HomePageVenueUserAdapter(HomePageSearchResultActivity.this,list);
                    LinearLayoutManager manager1 = new LinearLayoutManager(HomePageSearchResultActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    rvHomePageVenueDetailWhomGone.setLayoutManager(manager1);
                    rvHomePageVenueDetailWhomGone.setAdapter(userAdapter);
                    discussAdapter=new HomePageDiscussAdapter(HomePageSearchResultActivity.this,mValue);
                    LinearLayoutManager manager2 = new LinearLayoutManager(HomePageSearchResultActivity.this, LinearLayoutManager.VERTICAL, false);
                    rvHomePageVenueDetailSay.setLayoutManager(manager2);
                    rvHomePageVenueDetailSay.setAdapter(discussAdapter);
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
        llCancelAndSend= (LinearLayout) View.inflate(this,R.layout.comment_view,null);
        etToComment= (EditText) llCancelAndSend.findViewById(R.id.etToComment);
        tvCancel= (TextView) llCancelAndSend.findViewById(R.id.tvCancel);
        tvSend= (TextView) llCancelAndSend.findViewById(R.id.tvSend);
        setVenueData();
        setData();
        setListeners();
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
    private void setData() {
        list=new ArrayList<>();
        int [] pictures={R.drawable.demo_05,R.drawable.demo_06,R.drawable.demo_10,R.drawable.demo_10};
        String []userName={"土拨鼠","萌小妹","小美爱赵伟","萌小美美"};
        int [] levels={R.mipmap.img_lv1,R.mipmap.img_lv2,R.mipmap.img_lv3,R.mipmap.img_lv4,
                R.mipmap.img_lv5,R.mipmap.img_lv6,R.mipmap.img_lv7,R.mipmap.img_lv8,R.mipmap.img_lv9,
                R.mipmap.img_lv10,R.mipmap.img_lv11,R.mipmap.img_lv12,R.mipmap.img_lv13};
//        for (int i=0;i<4;i++){
//            HomePageVenueWhomGoneEntity entity=new HomePageVenueWhomGoneEntity();
//            entity.setUserPhotoUrl(pictures[i]+"");
//            entity.setUserName(userName[i]);
//            entity.setUserLevelUrl(levels[new Random().nextInt(13)]+"");
//            list.add(entity);
//        }
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
                        for (int i=0;i<array.length();i++){
                            JSONObject jsonObj=array.getJSONObject(i);
                            HomePageVenueWhomGoneEntity entity=new HomePageVenueWhomGoneEntity();
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
                    handler.sendEmptyMessage(HANDLE_BASE_DATA);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
        HomePageVenueWhomGoneEntity empty=new HomePageVenueWhomGoneEntity();
        list.add(empty);
        String [] contents={"今天天气真好，我们一起去打篮球吧","今天天气真好，我们一起去球场上打篮球吧，来pk一下撒！大家说，怎么样呢！。。。。","明天天气很好，我们到时一起去打篮球吧"};
        mValue=new ArrayList<>();
        for (int i=0;i<3;i++){
            HomePageDiscussEntity entity=new HomePageDiscussEntity();
            entity.setMainUserName(userName[i]);
            entity.setMainUserLevelUrl(levels[new Random().nextInt(13)]+"");
            entity.setMainUserPhotoUrl(pictures[3-i]+"");
            entity.setMainUserSendContent(contents[i]);
            entity.setMainUserSendTime("3分钟前");
            List<String> urls=new ArrayList<>();
            for (int j=0;j<=i;j++){
                urls.add(pictures[j]+"");
            }
            entity.setMainUserSendPicUrlList(urls);
            String [] discussTexts={"说走咱就走啊，一路看天不回头啊，水里火里一碗酒啊，路见不平一声吼啊，该出手时就出手啊！","走撒，那个SB不去！","打毛线，要下雨哈！"};
            List<CallBackDetailEntity> entityList=new ArrayList<>();
            if (i!=0){
                for (int k=0;k<3;k++){
                    CallBackDetailEntity entityDetail=new CallBackDetailEntity();
                    entityDetail.setCallFrom(userName[i%2==0?k:3-k]);
                    entityDetail.setCallTo(userName[i%2==1?k:3-k]);
                    entityDetail.setCallDetail(discussTexts[new Random().nextInt(3)]);
                    entityList.add(entityDetail);
                }
            }else {
                CallBackDetailEntity entityDetail=new CallBackDetailEntity();
                entityDetail.setCallFrom(userName[i]);
                entityDetail.setCallDetail(discussTexts[new Random().nextInt(3)]);
                entityList.add(entityDetail);
            }
            entity.setDiscussList(entityList);
            mValue.add(entity);
        }
        handler.sendEmptyMessage(HANDLE_BASE_DATA);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvCancel:
                etToComment.setText("");
                llToTalk.setVisibility(View.VISIBLE);
                dialog.dismiss();
                break;
            case R.id.tvSend:
                writeToCallBack();
                break;
            case R.id.llToTalk:
                showCommentView();
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
    private void showCommentView() {
        if (dialog==null) {
            dialog = new Dialog(this, R.style.Dialog_FS);
            dialog.setContentView(llCancelAndSend);
        }
        dialog.show();
        llToTalk.setVisibility(View.GONE);
        Window dialogWindow = this.getWindow();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialogWindow.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
        lp.x = 0;
        Rect r = new Rect();
        //获取当前界面可视部分
        dialogWindow.getDecorView().getWindowVisibleDisplayFrame(r);
        //获取屏幕的高度
        lp.y = r.bottom;
        lp.width= WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        Timer timer = new Timer();
        timer.schedule(new TimerTask(){
            @Override
            public void run(){
                InputMethodManager m = (InputMethodManager)etToComment.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                m.showSoftInput(etToComment,InputMethodManager.SHOW_FORCED);
            }
        }, 200);

    }
    private void writeToCallBack(){
        final String uri = API.BASE_URL + "/v2/comment/insert";
        final SharedPreferences share = getSharedPreferences("UserInfo", MODE_PRIVATE);
        new Thread(){
            @Override
            public void run() {
                try {
                    nowUserId = share.getInt("UserId", 0);
                    HashMap<String, String> params = new HashMap<>();
                    params.put("comment_type", "4");
                    params.put("comment_id", venueId + "");
                    params.put("model_create_id", "-1");
                    params.put("comment_account", nowUserId + "");
                    String commentText = etToComment.getText().toString().trim();
                    params.put("comment_content", commentText);
                    String result = PostUtil.sendPostMessage(uri, params);
                    JSONObject obj = new JSONObject(result);
                    int state=obj.getInt("state");
                    if (state==200){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                               Toast.makeText(HomePageSearchResultActivity.this,"评论成功！",Toast.LENGTH_LONG).show();
                                etToComment.setText("");
                                llToTalk.setVisibility(View.VISIBLE);
                                dialog.dismiss();
                            }
                        });
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();

    }
}
