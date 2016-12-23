package com.tiyujia.homesport.common.homepage.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.personal.adapter.AttendAdapter;
import com.tiyujia.homesport.common.personal.model.ActiveModel;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.util.RefreshUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者: Cymbi on 2016/11/17 15:03.1
 * 邮箱:928902646@qq.com
 */

public class HomePageDateActivity extends ImmersiveActivity implements View.OnClickListener ,SwipeRefreshLayout.OnRefreshListener,RadioGroup.OnCheckedChangeListener {
    @Bind(R.id.ivMenu) ImageView ivMenu;
    @Bind(R.id.ivBack) ImageView ivBack;
    @Bind(R.id.ivPush) ImageView ivPush;
    @Bind(R.id.tvTitle) TextView tvTitle;
    @Bind(R.id.tvline)    TextView tvline;
    @Bind(R.id.cbDateBanner) ConvenientBanner cbDateBanner;
    @Bind(R.id.recyclerView) RecyclerView recyclerView;
    @Bind(R.id.srlRefresh)   SwipeRefreshLayout srlRefresh;
    private Dialog dialog;
    private String mToken;
    private int mUserId;
    private AttendAdapter adapter;
    private int state=0;//状态（0、全部 1、正在报名 2、已结束）
    private int type=0;//类型（0、全部 1、求约 2、求带）
    private int number=100;//每页显示条数
    private int pageNumber=1;//当前第几页
    private int RgType;//手选类型
    private int RgState;//手选状态
    private String city="";//城市
    private boolean isInitCache = false;
    private RadioGroup rgType,rgState;
    private View view;
    private TextView tvComplete;
    private RadioButton rbTypeAll,rbDateType,rbLeadType,rbStateAll,rbStart,rbEnd,rbApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_date);
        setInfo();
        adapter=new AttendAdapter(this,null);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        //保证recycleView不卡顿
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        adapter.isFirstOnly(false);
        recyclerView.setAdapter(adapter);
        RefreshUtil.refresh(srlRefresh,this);
        srlRefresh.setOnRefreshListener(this);
        //adapter.setOnLoadMoreListener(this);
        onRefresh();
    }
    private void setInfo() {
        SharedPreferences share = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        mToken=share.getString("Token","");
        mUserId=share.getInt("UserId",0);
        city=share.getString("City","");
        ivMenu.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivPush.setOnClickListener(this);
        view = getLayoutInflater().inflate(R.layout.homepage_type_dialog, null);
        rgType=(RadioGroup) view.findViewById(R.id.rgType);
        rgState=(RadioGroup) view.findViewById(R.id.rgState);
        dialog = new Dialog(this,R.style.dialog);
        ImageView ivBack=(ImageView) view.findViewById(R.id.ivBack);
        ImageView ivClose=(ImageView) view.findViewById(R.id.ivClose);
        rbTypeAll=(RadioButton) view.findViewById(R.id.rbTypeAll);
        rbDateType=(RadioButton) view.findViewById(R.id.rbDateType);
        rbLeadType=(RadioButton) view.findViewById(R.id.rbLeadType);
        rbStateAll=(RadioButton) view.findViewById(R.id.rbStateAll);
        rbStart=(RadioButton) view.findViewById(R.id.rbStart);
        rbEnd=(RadioButton) view.findViewById(R.id.rbEnd);
        rbApply=(RadioButton) view.findViewById(R.id.rbApply);
        tvComplete=(TextView) view.findViewById(R.id.tvComplete);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tvComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
                dialog.dismiss();
            }
        });
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = tvline.getWidth();
        wl.y = tvline.getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        window.setGravity(Gravity.TOP);
        //加下面代码可以去掉状态栏
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivMenu:
                dialog.show();
                rgState.setOnCheckedChangeListener(this);
                rgType.setOnCheckedChangeListener(this);
                break;
            case R.id.ivPush:
                startActivity(new Intent(this,HomePageActivePublishActivity.class));
                break;
        }
    }

    @Override
    public void onRefresh() {
        OkGo.post(API.BASE_URL+"/v2/activity/query")
                .tag(this)
                .params("state",state)
                .params("type",type)
                .params("city",city)
                .params("number",number)
                .params("pageNumber",pageNumber)
                .execute(new LoadCallback<ActiveModel>(this) {
                    @Override
                    public void onSuccess(ActiveModel activeModel, Call call, Response response) {
                        if(activeModel.state==200){adapter.setNewData(activeModel.data);}
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        showToast("网络连接错误");
                    }
                    @Override
                    public void onAfter(@Nullable ActiveModel activeModel, @Nullable Exception e) {
                        super.onAfter(activeModel, e);
                        if (adapter!=null) {
                            adapter.removeAllFooterView();
                            setRefreshing(false);
                        }
                    }
                });
    }
    public void setRefreshing(final boolean refreshing) {
        if (srlRefresh!=null) {
            srlRefresh.post(new Runnable() {
                @Override
                public void run() {
                    srlRefresh.setRefreshing(refreshing);
                }
            });
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getId()){
            case R.id.rgType:
                if(rbTypeAll.getId()==checkedId){
                    type=0;
                }
                if(rbDateType.getId()==checkedId){
                    type=1;
                }
                if(rbLeadType.getId()==checkedId){
                    type=2;
                }
                break;
            case R.id.rgState:
                if(rbStateAll.getId()==checkedId){
                    state=0;
                }
                if(rbStart.getId()==checkedId){
                    state=0;
                }
                if(rbEnd.getId()==checkedId){
                    state=2;
                }
                if(rbApply.getId()==checkedId){
                    state=1;
                }
                break;
        }
    }
}
