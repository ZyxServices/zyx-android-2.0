package com.tiyujia.homesport.common.personal.activity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ff.imagezoomdrag.ImageDetailActivity;
import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.personal.fragment.ActiveFragment;
import com.tiyujia.homesport.common.personal.fragment.OtherActiveFragment;
import com.tiyujia.homesport.common.personal.fragment.OtherDynamicFragment;
import com.tiyujia.homesport.common.personal.fragment.OtherEquipmentShowFragment;
import com.tiyujia.homesport.common.personal.model.UserInfoModel;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.entity.LzyResponse;
import com.tiyujia.homesport.util.FastBlurUtil;
import com.tiyujia.homesport.util.LvUtil;
import com.tiyujia.homesport.util.PicUtil;
import com.tiyujia.homesport.util.PicassoUtil;
import com.tiyujia.homesport.util.StringUtil;
import com.tiyujia.homesport.widget.TablayoutVPAdapter;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者: Cymbi on 2016/11/23 14:30.
 * 邮箱:928902646@qq.com
 */

public class PersonalOtherHome extends ImmersiveActivity implements View.OnClickListener{
    @Bind(R.id.ivBack)    ImageView ivBack;
    @Bind(R.id.ivAvatar)    ImageView ivAvatar;
    @Bind(R.id.ivLv)    ImageView ivLv;
    @Bind(R.id.tvNickname)    TextView tvNickname;
    @Bind(R.id.tvAddAttention)    TextView tvAddAttention;
    @Bind(R.id.tvContent)    TextView tvContent;
    @Bind(R.id.tvAttention)    TextView tvAttention;
    @Bind(R.id.tvFans)    TextView tvFans;
    @Bind(R.id.personal_name_title)    TextView title;
    @Bind(R.id.tab)    TabLayout tab;
    @Bind(R.id.vp)    ViewPager vp;
    @Bind(R.id.personal_appbar)    AppBarLayout appbar;
    @Bind(R.id.ivBackground)    ImageView ivBackground;
    private State state;
    private List<String> mTitle=new ArrayList<String>();
    private List<Fragment> mFragment = new ArrayList<Fragment>();
    private String token="tiyujia2016";
    private int account_id;
    private String avatarUrl="";
    private static final int HANDLE_IMAGE=1;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HANDLE_IMAGE:
                    Bitmap bitmap= (Bitmap) msg.obj;
                    if (bitmap==null){
                    }else {Bitmap blur = FastBlurUtil.blurBitmap(bitmap);
                        ivBackground.setImageBitmap(blur);}
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_other_home);
        account_id=getIntent().getIntExtra("id",0);
        setView();
        setData();
        initView();
        TablayoutVPAdapter adapter=new TablayoutVPAdapter(getSupportFragmentManager(),mFragment,mTitle);
        vp.setAdapter(adapter);
        //tablayout和viewpager关联
        tab.setupWithViewPager(vp);
        vp.setOffscreenPageLimit(3);
        tab.setTabsFromPagerAdapter(adapter);
        tab.setTabGravity(TabLayout.GRAVITY_FILL);
        tab.setTabMode(TabLayout.MODE_FIXED);
    }

    private void initView() {
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    if (state != State.EXPANDED) {
                        state = State.EXPANDED;//修改状态标记为展开
                        title.setVisibility(View.GONE);
                    }
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (state != State.COLLAPSED) {
                        title.setVisibility(View.VISIBLE);//显示title控件
                        state = State.COLLAPSED;//修改状态标记为折叠
                        title.setText(tvNickname.getText() );//设置title为用户名
                    }
                } else {
                    if (state != State.INTERNEDIATE) {
                        if (state == State.COLLAPSED) {
                            title.setVisibility(View.GONE);//由折叠变为中间状态时隐藏播放按钮
                        }
                        state = State.INTERNEDIATE;//修改状态标记为中间
                    }
                }
            }
        });
    }
    private void setData() {
        OkGo.get(API.BASE_URL+"/v2/user/center_info")
                .tag(this)
                .params("token",token)
                .params("account_id",account_id)
                .execute(new LoadCallback<UserInfoModel>(this) {
                    @Override
                    public void onSuccess(final UserInfoModel userInfoModel, Call call, Response response) {
                        if(userInfoModel.state==200){
                            avatarUrl=userInfoModel.data.avatar;
                            PicassoUtil.handlePic(PersonalOtherHome.this, PicUtil.getImageUrlDetail(PersonalOtherHome.this, StringUtil.isNullAvatar(avatarUrl), 320, 320),ivAvatar,320,320);
                            tvNickname.setText(userInfoModel.data.nickname);
                            tvContent.setText("个人简介:"+userInfoModel.data.signature);
                            if (userInfoModel.data.level!=null&&userInfoModel.data.level.equals("")){
                                LvUtil.setLv(ivLv,userInfoModel.data.level.pointDesc);
                            }else {
                                LvUtil.setLv(ivLv,"初学乍练");
                            }
                            tvAttention.setText(userInfoModel.data.gz+"");
                            tvFans.setText(userInfoModel.data.fs+"");
                            if(userInfoModel.data.avatar!=null){
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        final Bitmap blur = getBitmap(API.PICTURE_URL+userInfoModel.data.avatar);
                                        Message message=new Message();
                                        message.what=HANDLE_IMAGE;
                                        message.obj=blur;
                                        handler.sendMessage(message);
                                    }
                                }).start();
                            }
                        }

                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        showToast("网络连接错误");
                    }
                });
    }
    private void setView() {
        ivBack.setOnClickListener(this);
        tvAddAttention.setOnClickListener(this);
        ivAvatar.setOnClickListener(this);
        mTitle.add("他的动态");
        mTitle.add("他的活动");
        mTitle.add("他的装备");
        OtherDynamicFragment otherDynamicFragment= new OtherDynamicFragment();
        OtherActiveFragment otherActiveFragment= new OtherActiveFragment();
        OtherEquipmentShowFragment otherEquipmentShowFragment= new OtherEquipmentShowFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("account_id",account_id);
        otherActiveFragment.setArguments(bundle);
        otherDynamicFragment.setArguments(bundle);
        otherEquipmentShowFragment.setArguments(bundle);
        mFragment.add(otherDynamicFragment);
        mFragment.add(otherActiveFragment);
        mFragment.add(otherEquipmentShowFragment);
    }

    //枚举出Toolbar的三种状态
    private enum State{
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }
    /**
     * 通过枚举修改AppBarLyout状态
     */

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivAvatar:
                if(!TextUtils.isEmpty(avatarUrl)){
                    ArrayList<String> str=new ArrayList<>();
                    str.add(API.PICTURE_URL+avatarUrl);
                    startActivity(ImageDetailActivity.getMyStartIntent(this, str,0, ImageDetailActivity.url_path));
                }else {
                    ivAvatar.setClickable(false);
                }
                break;
            case R.id.tvAddAttention:
               SharedPreferences share= getSharedPreferences("UserInfo",MODE_PRIVATE);
                String  Token= share.getString("Token","");
                int  userid= share.getInt("UserId",0);
                OkGo.post(API.BASE_URL+"/v2/follow/add")
                        .tag(this)
                        .params("token",Token)
                        .params("fromUserId",userid)
                        .params("toUserId",account_id)
                        .execute(new LoadCallback<LzyResponse>(this) {
                            @Override
                            public void onSuccess(LzyResponse lzyResponse, Call call, Response response) {
                                if(lzyResponse.state==200){
                                    showToast("添加成功");
                                }if(lzyResponse.state==70003) {
                                    showToast("您已关注，无法重复关注");
                                }
                            }
                        });
                break;
        }
    }
    public Bitmap getBitmap(String url) {
        Bitmap bm = null;
        try {
            URL iconUrl = new URL(url);
            URLConnection conn = iconUrl.openConnection();
            HttpURLConnection http = (HttpURLConnection) conn;
            int length = http.getContentLength();
            conn.connect();
            // 获得图像的字符流
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is, length);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();// 关闭流
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }
}