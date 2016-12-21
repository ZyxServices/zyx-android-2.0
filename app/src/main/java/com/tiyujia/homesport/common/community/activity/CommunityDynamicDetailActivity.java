package com.tiyujia.homesport.common.community.activity;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.ninegrid.NineGridView;
import com.lzy.okgo.OkGo;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.NewBaseActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.community.model.DynamicDetailEntity;
import com.tiyujia.homesport.common.homepage.activity.HomePageSearchResultActivity;
import com.tiyujia.homesport.common.homepage.adapter.HomePageCommentAdapter;
import com.tiyujia.homesport.common.homepage.adapter.NGLAdapter;
import com.tiyujia.homesport.common.homepage.entity.HomePageCommentEntity;
import com.tiyujia.homesport.common.homepage.entity.HomePageVenueWhomGoneEntity;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.entity.LzyResponse;
import com.tiyujia.homesport.util.KeyboardWatcher;
import com.tiyujia.homesport.util.LvUtil;
import com.tiyujia.homesport.util.PostUtil;
import com.tiyujia.homesport.util.StringUtil;
import com.tiyujia.homesport.widget.ImagePickerAdapter;
import com.w4lle.library.NineGridlayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;

public class CommunityDynamicDetailActivity extends NewBaseActivity implements View.OnClickListener,KeyboardWatcher.OnKeyboardToggleListener,SwipeRefreshLayout.OnRefreshListener,
        ImagePickerAdapter.OnRecyclerViewItemClickListener {
    private int recommendId;
    private int nowUserId;
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
    @Bind(R.id.nglDynamicDetailImages)      NineGridlayout nglDynamicDetailImages;//用户的动态的图片
    @Bind(R.id.rvDynamicDetailLove)         RecyclerView rvDynamicDetailLove;//喜欢该条动态的用户列表
    @Bind(R.id.rvDynamicDetailSay)          RecyclerView rvDynamicDetailSay;//评论列表
    @Bind(R.id.llToTalk)                    LinearLayout llToTalk;//橙色布局
    @Bind(R.id.llCancelAndSend)             LinearLayout llCancelAndSend;//输入框布局
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
    public static HomePageCommentEntity.HomePage entity;
    public static boolean isComment=true;
    public static int replyToId=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_dynamic_detail);
        setBaseData();
        setListeners();
    }
    private void setListeners() {
        ivDynamicDetailBack.setOnClickListener(this);
    }
    private void setBaseData() {
        recommendId=getIntent().getIntExtra("recommendId",0);
        SharedPreferences share=getSharedPreferences("UserInfo",MODE_PRIVATE);
        nowUserId=share.getInt("UserId",0);
        OkGo.get(API.BASE_URL+"/v2/concern/getOne")
        .tag(this)
        .params("concernId",recommendId)
        .params("accountId",nowUserId)
        .execute(new LoadCallback<LzyResponse<DynamicDetailEntity>>(this) {
            @Override
            public void onSuccess(LzyResponse<DynamicDetailEntity> info, Call call, Response response) {
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
                tvDynamicDetailText.setText(info.data.concern.topicContent);
                List<String> imageUrls=new ArrayList<String>();
                String urlTemp=info.data.concern.imgUrl;
                if (!urlTemp.equals("")&&urlTemp!=null&&!urlTemp.equals("null")){
                    if (urlTemp.contains(",")){
                        String[] urls=urlTemp.split(",");
                        for (String s:urls){
                            imageUrls.add(API.PICTURE_URL+s);
                        }
                    }else {
                        imageUrls.add(API.PICTURE_URL+urlTemp);
                    }
                }
                NGLAdapter adapter = new NGLAdapter(CommunityDynamicDetailActivity.this, imageUrls);
                nglDynamicDetailImages.setGap(6);
                nglDynamicDetailImages.setAdapter(adapter);
            }
        });
        setWhoLove();
    }

    private void setWhoLove() {
        final String url=API.BASE_URL+"GET /v2/concern/getConcernZanUser";

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivDynamicDetailBack:
                finish();
                break;
        }
    }

    @Override
    public void onKeyboardShown(int keyboardSize) {

    }

    @Override
    public void onKeyboardClosed() {

    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
