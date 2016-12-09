package com.tiyujia.homesport.common.homepage.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.squareup.picasso.Picasso;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.BaseFragment;
import com.tiyujia.homesport.BootLoaderActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.activity.HomePageCourseActivity;
import com.tiyujia.homesport.common.homepage.activity.HomePageDateActivity;
import com.tiyujia.homesport.common.homepage.activity.HomePageEquipmentActivity;
import com.tiyujia.homesport.common.homepage.activity.HomePageSetCityActivity;
import com.tiyujia.homesport.common.homepage.activity.HomePageVenueSurveyActivity;
import com.tiyujia.homesport.common.homepage.activity.HomePageWholeSearchActivity;
import com.tiyujia.homesport.common.homepage.adapter.HomePageRecentVenueAdapter;
import com.tiyujia.homesport.common.homepage.entity.HomePageBannerEntity;
import com.tiyujia.homesport.common.homepage.entity.HomePageData;
import com.tiyujia.homesport.common.homepage.entity.HomePageRecentVenueEntity;
import com.tiyujia.homesport.common.homepage.net.HomePageDataManager;
import com.tiyujia.homesport.entity.Result;
import com.tiyujia.homesport.common.homepage.service.HomePageService;
import com.tiyujia.homesport.util.CacheUtils;
import com.tiyujia.homesport.util.JSONParseUtil;
import com.tiyujia.homesport.util.PostUtil;
import com.tiyujia.homesport.util.RefreshUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zzqybyb19860112 on 2016/10/18.44441
 */
public class HomePageFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener{
    HomePageService homePageService;
    @Bind(R.id.srlHomePage)             SwipeRefreshLayout swipeContainer;
    @Bind(R.id.cbHomePage)              ConvenientBanner cbHomePage;
    @Bind(R.id.rvRecentVenue)           RecyclerView rvRecentVenue;
    @Bind(R.id.ivHomePageAllVenue)      ImageView ivHomePageAllVenue;
    @Bind(R.id.tvCourse)                TextView tvCourse;
    @Bind(R.id.tvEquipment)             TextView tvEquipment;
    @Bind(R.id.tvDate)                  TextView tvDate;
    @Bind(R.id.tvSearchCity)            TextView tvSearchCity;
    @Bind(R.id.tvSearchDetail)            TextView tvSearchDetail;
    HomePageRecentVenueAdapter adapter;
    List<HomePageRecentVenueEntity> datas=new ArrayList<>();
    private Toolbar tb;
    private AppBarLayout appbar;
    private State state;
    private HomePageFragmentReceiver mReceiver;
    String selectCity;
    private List<HomePageBannerEntity> banners = new ArrayList<>();
    int [] picAddress=new int[]{R.drawable.demo_05,R.drawable.demo_06,R.drawable.demo_09,R.drawable.demo_10};
    public static final int HANDLE_DATA=1;
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
            }
        }
    };
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.homepage_fragment,null);
        ButterKnife.bind(this,view);
        homePageService = HomePageDataManager.getService(HomePageService.class);
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
        return view;
    }
    @Override
    protected void initData() {
        banners.add(new HomePageBannerEntity(picAddress[0]));
        banners.add(new HomePageBannerEntity(picAddress[1]));
        banners.add(new HomePageBannerEntity(picAddress[2]));
        banners.add(new HomePageBannerEntity(picAddress[3]));
        setDatas();
        cbHomePage.setPages(new CBViewHolderCreator<ImageHolderView>() {
            @Override public ImageHolderView createHolder() {
                return new ImageHolderView();
            }
        }, banners).setPageIndicator(
                new int[] { R.drawable.dot_normal, R.drawable.dot_selected})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        Subscription hotInfo = homePageService.getAllHotInfo().subscribeOn(Schedulers.io())//
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Result<HomePageData>>() {
                    @Override public void onCompleted() {
                    }
                    @Override public void onError(Throwable e) {
                        Toast.makeText(getActivity(), "刷新失败", Toast.LENGTH_SHORT).show();
                    }
                    @Override public void onNext(Result<HomePageData> result) {
                        if (result.state == 200) {
                            refresh(result.data);
                        } else {
                            Toast.makeText(getActivity(), "诶~网络好像有问题啊", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        mCompositeSubscription.add(hotInfo);
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
    private void setDatas() {
        AMapLocationClient client = BootLoaderActivity.client;
        AMapLocation location = client.getLastKnownLocation();
        final double latitude= location.getLatitude();//纬度
        final double longitude=location.getLongitude();//经度
        ArrayList<String> cacheData= (ArrayList<String>) CacheUtils.readJson(getActivity(), HomePageFragment.this.getClass().getName() + ".json");
        if (cacheData==null||cacheData.size()==0) {
            new Thread() {
                @Override
                public void run() {
                    String uri = API.BASE_URL + "/v2/venue/findVenue";
                    HashMap<String, String> params = new HashMap<>();
                    params.put("type", "2");
                    params.put("lng", longitude + "");
                    params.put("lat", latitude + "");
                    params.put("number", "10");
                    params.put("pageNumber", "1");
                    String result = PostUtil.sendPostMessage(uri, params);
                    JSONParseUtil.parseNetDataVenue(getActivity(),result,HomePageFragment.this.getClass().getName()+".json",datas, handler, HANDLE_DATA);
                }
            }.start();
        }else {
            JSONParseUtil.parseLocalDataVenue(getActivity(),HomePageFragment.this.getClass().getName()+".json",datas, handler, HANDLE_DATA);
        }
    }
    @Override public void onResume() {
        super.onResume();
        cbHomePage.startTurning(2500);
        mReceiver=new HomePageFragmentReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("GET_LOCATION");
        getActivity().registerReceiver(mReceiver,filter);
    }
    @Override public void onPause() {
        super.onPause();
        cbHomePage.stopTurning();
        getActivity().unregisterReceiver(mReceiver);
    }
    private boolean isFirstReceive=true;
    private class HomePageFragmentReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isFirstReceive) {
                String city = intent.getStringExtra("CITY");
                Log.i("tag","now------fragment----"+city);
                tvSearchCity.setText(city);
                tvSearchCity.postInvalidate();
                isFirstReceive=false;
            }
        }
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode==10002&&requestCode==10001){
            selectCity=data.getStringExtra("SelectCity");
            tvSearchCity.setText(selectCity);
            tvSearchCity.invalidate();
        }
    }

    public class ImageHolderView implements Holder<HomePageBannerEntity> {
        private ImageView iv;
        int pos=0;
        @Override public View createView(Context context) {
            iv = new ImageView(context);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            return iv;
        }
        @Override public void UpdateUI(Context context, final int position, HomePageBannerEntity data) {
            pos=position;
//            Rect rect = new Rect();
//            ((Activity)context).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
//            int x = rect.width();
//            PicassoUtil.handlePic(context, data.bmpUrl, iv, x, 720);
            Picasso.with(getActivity()).load(data.picAddress).into(iv);
        }
    }
    @Override public void onRefresh() {
        setDatas();
        updateData();
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                // 停止刷新
                swipeContainer.setRefreshing(false);
            }
        }, 500); // 5秒后发送消息，停止刷新
    }
    public void updateData() {
        Subscription hotInfo = homePageService.getAllHotInfo().subscribeOn(Schedulers.io())//
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Result<HomePageData>>() {
                    @Override public void onCompleted() {
                        swipeContainer.setRefreshing(false);
                    }
                    @Override public void onError(Throwable e) {
                        swipeContainer.setRefreshing(false);
                        Toast.makeText(getActivity(), "刷新内容失败", Toast.LENGTH_SHORT).show();
                    }
                    @Override public void onNext(Result<HomePageData> result) {
                        swipeContainer.setRefreshing(false);
                        if (result.state == 200) {
                            refresh(result.data);
                        } else {
                            Toast.makeText(getActivity(), "当前网络状态不佳，请检查您的网络设置！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        mCompositeSubscription.add(hotInfo);
    }
    private void refresh(HomePageData data) {
        //banner
        if (data.homeBannerEntities != null && data.homeBannerEntities.size() > 0) {
            banners.clear();
            data.homeBannerEntities.add(new HomePageBannerEntity(picAddress[0]));
            data.homeBannerEntities.add(new HomePageBannerEntity(picAddress[1]));
            data.homeBannerEntities.add(new HomePageBannerEntity(picAddress[2]));
            data.homeBannerEntities.add(new HomePageBannerEntity(picAddress[3]));
            banners.addAll(data.homeBannerEntities);
            cbHomePage.notifyDataSetChanged();
        }
    }
    private enum State{
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }
}
