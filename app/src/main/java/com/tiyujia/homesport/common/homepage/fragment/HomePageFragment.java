package com.tiyujia.homesport.common.homepage.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.BaseFragment;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.activity.HomePageArticleActivity;
import com.tiyujia.homesport.common.homepage.activity.HomePageCourseActivity;
import com.tiyujia.homesport.common.homepage.activity.HomePageDateActivity;
import com.tiyujia.homesport.common.homepage.activity.HomePageEquipmentActivity;
import com.tiyujia.homesport.common.homepage.activity.HomePageSetCityActivity;
import com.tiyujia.homesport.common.homepage.activity.HomePageVenueSurveyActivity;
import com.tiyujia.homesport.common.homepage.activity.HomePageWholeSearchActivity;
import com.tiyujia.homesport.common.homepage.adapter.HomePageRecentVenueAdapter;
import com.tiyujia.homesport.common.homepage.entity.HomePageBannerEntity;
import com.tiyujia.homesport.common.homepage.entity.HomePageRecentVenueEntity;
import com.tiyujia.homesport.util.DialogUtil;
import com.tiyujia.homesport.util.JSONParseUtil;
import com.tiyujia.homesport.util.NetworkUtil;
import com.tiyujia.homesport.util.PicassoUtil;
import com.tiyujia.homesport.util.PostUtil;
import com.tiyujia.homesport.util.RefreshUtil;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zzqybyb19860112 on 2016/10/18.44441
 */
public class HomePageFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener{
    @Bind(R.id.srlHomePage)             SwipeRefreshLayout swipeContainer;
    @Bind(R.id.cbHomePage)              ConvenientBanner cbHomePage;
    @Bind(R.id.rvRecentVenue)           RecyclerView rvRecentVenue;
    @Bind(R.id.ivHomePageAllVenue)      ImageView ivHomePageAllVenue;
    @Bind(R.id.tvCourse)                TextView tvCourse;
    @Bind(R.id.tvEquipment)             TextView tvEquipment;
    @Bind(R.id.tvDate)                  TextView tvDate;
    @Bind(R.id.tvSearchCity)            TextView tvSearchCity;
    @Bind(R.id.tvSearchDetail)          TextView tvSearchDetail;
    HomePageRecentVenueAdapter adapter;
    List<HomePageRecentVenueEntity> datas=new ArrayList<>();
    private Toolbar tb;
    private AppBarLayout appbar;
    private State state;
    String selectCity;
    private List<HomePageBannerEntity> banners = new ArrayList<>();
    public static final int HANDLE_DATA=1;
    public static final int HANDLE_BANNER_DATA=2;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HANDLE_DATA:
                    adapter=new HomePageRecentVenueAdapter(getActivity(),datas);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    rvRecentVenue.setLayoutManager(layoutManager);
                    rvRecentVenue.setAdapter(adapter);
                    break;
                case HANDLE_BANNER_DATA:
                    cbHomePage.setPages(new CBViewHolderCreator<ImageHolderView>() {
                        @Override public ImageHolderView createHolder() {
                            return new ImageHolderView();
                        }
                    }, banners).setPageIndicator(
                            new int[] { R.drawable.dot_normal, R.drawable.dot_selected})
                            .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
                    cbHomePage.postInvalidate();
                    break;
            }
        }
    };
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.homepage_fragment,null);
        ButterKnife.bind(this,view);
        tb=(Toolbar)view.findViewById(R.id.toolbar);
        appbar=(AppBarLayout)view.findViewById(R.id.personal_appbar);
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    if (state != State.EXPANDED) {
                        state = State.EXPANDED;//修改状态标记为展开
                        swipeContainer.setEnabled(true);//展开时才可使用下拉刷新
                    }
                    swipeContainer.setEnabled(true);
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (state != State.COLLAPSED) {
                        state = State.COLLAPSED;//修改状态标记为折叠
                    }
                } else {
                    if (state != State.INTERNEDIATE) {
                        if (state == State.COLLAPSED) {
                        }
                        state = State.INTERNEDIATE;//修改状态标记为中间
                    }
                    swipeContainer.setEnabled(false);
                }
            }
        });
        ((AppCompatActivity)getActivity()).setSupportActionBar(tb);
        dialog=DialogUtil.createDialog(getActivity(),"请等待。。。。。。","         正在定位");
        return view;
    }
    @Override
    protected void initData() {
        setDatas();
        swipeContainer.setOnRefreshListener(this);
        RefreshUtil.refresh(swipeContainer,getActivity());
        ivHomePageAllVenue.setOnClickListener(this);
        tvCourse.setOnClickListener(this);
        tvEquipment.setOnClickListener(this);
        tvDate.setOnClickListener(this);
        tvSearchCity.setOnClickListener(this);
        tvSearchDetail.setOnClickListener(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 停止刷新
                swipeContainer.setRefreshing(false);
            }
        }, 500);
    }
    Dialog dialog;
    private void setDatas() {
        try {
            if (selectCity==null){
                if (city==null){
                    tvSearchCity.setText("定位中");
                }else {
                    tvSearchCity.setText(city);
                }
            }else {
                tvSearchCity.setText(selectCity);
            }
            tvSearchCity.invalidate();
            if (tvSearchCity.getText().toString().equals("定位中")){
                dialog.show();
            }else {
                boolean isNetEnable= NetworkUtil.isNetworkEnable(getActivity());
                if (isNetEnable){
                    new Thread() {
                        @Override
                        public void run() {
                            String uri = API.BASE_URL + "/v2/venue/findVenue";
                            HashMap<String, String> params = new HashMap<>();
                            params.put("type", "2");
                            params.put("lng", jingDu + "");
                            params.put("lat", weiDu + "");
                            if (selectCity!=null){
                                params.put("city", selectCity);
                            }
                            params.put("number", "10");
                            params.put("pageNumber", "1");
                            String result = PostUtil.sendPostMessage(uri, params);
                            JSONParseUtil.parseNetDataVenue(getActivity(), result, HomePageFragment.this.getClass().getName()+"_" +selectCity+ "_.1.json", datas, handler, HANDLE_DATA);
                        }
                    }.start();
                }else {
                    JSONParseUtil.parseLocalDataVenue(getActivity(), HomePageFragment.this.getClass().getName()+"_" +selectCity+ "_.1.json", datas, handler, HANDLE_DATA);
                }
                if (isNetEnable) {
                    new Thread() {
                        @Override
                        public void run() {
                            String uri = API.BASE_URL + "/v2/search/deva";
                            HashMap<String, String> params = new HashMap<>();
                            params.put("model", "1");
                            String result = PostUtil.sendPostMessage(uri, params);
                            JSONParseUtil.parseNetDataHomeBanner(getActivity(), result, HomePageFragment.this.getClass().getName() + "2.json", banners, handler, HANDLE_BANNER_DATA);
                        }
                    }.start();
                } else {
                    JSONParseUtil.parseLocalDataHomeBanner(getActivity(), HomePageFragment.this.getClass().getName() + ".2.json", banners, handler, HANDLE_BANNER_DATA);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private String city;
    private double jingDu;
    private double weiDu;
    MyLocationReceiver myReceiver;
    private class MyLocationReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            city=intent.getStringExtra("city");
            jingDu=intent.getDoubleExtra("jingDu",0.0);
            weiDu=intent.getDoubleExtra("weiDu",0.0);
            dialog.dismiss();
            setDatas();
        }
    }
    @Override public void onResume() {
        super.onResume();
        setDatas();
        cbHomePage.startTurning(2500);
        myReceiver=new MyLocationReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("GOT_LOCATION");
        getActivity().registerReceiver(myReceiver,filter);
    }
    @Override public void onPause() {
        super.onPause();
        cbHomePage.stopTurning();
        getActivity().unregisterReceiver(myReceiver);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivHomePageAllVenue://更多岩场
                getActivity().startActivity(new Intent(getActivity(), HomePageVenueSurveyActivity.class));
                break;
            case R.id.tvCourse://教程
                getActivity().startActivity(new Intent(getActivity(), HomePageCourseActivity.class));
                break;
            case R.id.tvEquipment://装备控
                getActivity().startActivity(new Intent(getActivity(), HomePageEquipmentActivity.class));
                break;
            case R.id.tvDate://求约
                getActivity().startActivity(new Intent(getActivity(), HomePageDateActivity.class));
                break;
            case R.id.tvSearchDetail://全局搜索框
                getActivity().startActivity(new Intent(getActivity(), HomePageWholeSearchActivity.class));
                break;
            case R.id.tvSearchCity://城市定位按钮
                Intent intent=new Intent(getActivity(), HomePageSetCityActivity.class);
                startActivityForResult(intent,10001);
                break;
        }
    }
    private static String GAODEAPI = "http://restapi.amap.com/v3/geocode/geo?key=<key>&s=rsv3&address=<address>";
    private static String KEY = "41b06df45821e2e8780540298badbd71";
    private static Pattern pattern = Pattern.compile("\"location\":\"(\\d+\\.\\d+),(\\d+\\.\\d+)\"");
    private void resetJingWeiDu(String cityName){
        GAODEAPI = GAODEAPI.replaceAll("<key>", KEY);
        try {
            String requestUrl = GAODEAPI.replaceAll("<address>", URLEncoder.encode(cityName, "UTF-8"));
            if (requestUrl != null ) {
                Matcher matcher = pattern.matcher(requestUrl);
                if (matcher.find() && matcher.groupCount() == 2) {
                    jingDu = Double.valueOf(matcher.group(1));
                    weiDu = Double.valueOf(matcher.group(2));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode==10002&&requestCode==10001){
            selectCity=data.getStringExtra("SelectCity");
            resetJingWeiDu(selectCity);
            setDatas();
        }
    }
    public class ImageHolderView implements Holder<HomePageBannerEntity> {
        private ImageView iv;
        int pos=0;
        HomePageBannerEntity data;
        @Override public View createView(Context context) {
            iv = new ImageView(context);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), HomePageArticleActivity.class);
                    intent.putExtra("id",data.getModelId());
                    getActivity().startActivity(intent);
                }
            });
            return iv;
        }
        @Override public void UpdateUI(Context context, final int position, HomePageBannerEntity data) {
            this.data=data;
            pos=position;
            Rect rect = new Rect();
            ((Activity)context).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            int x = rect.width();
            PicassoUtil.handlePic(context, data.imageUrl, iv, x, 720);
        }
    }
    @Override public void onRefresh() {
        resetJingWeiDu(tvSearchCity.getText().toString());
        setDatas();
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                // 停止刷新
                swipeContainer.setRefreshing(false);
            }
        }, 500); // 5秒后发送消息，停止刷新
    }
    private enum State{
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }
}
