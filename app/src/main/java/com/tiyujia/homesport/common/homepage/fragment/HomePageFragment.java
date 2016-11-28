package com.tiyujia.homesport.common.homepage.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
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
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.squareup.picasso.Picasso;
import com.tiyujia.homesport.App;
import com.tiyujia.homesport.BaseFragment;
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
import com.tiyujia.homesport.util.RefreshUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
    List<HomePageRecentVenueEntity> datas;
    private Toolbar tb;
    private AppBarLayout appbar;
    private State state;
    private HomePageFragmentReceiver mReceiver;
    String selectCity;
    private List<HomePageBannerEntity> banners = new ArrayList<>();
    int [] picAddress=new int[]{R.drawable.demo_05,R.drawable.demo_06,R.drawable.demo_09,R.drawable.demo_10};
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
        String nowCity=App.nowCity;
        if (nowCity==null){
            tvSearchCity.setText("定位中");
        }else {
            tvSearchCity.setText(nowCity);
            tvSearchCity.postInvalidate();
        }
        return view;
    }
    @Override
    protected void initData() {
        banners.add(new HomePageBannerEntity(picAddress[0]));
        banners.add(new HomePageBannerEntity(picAddress[1]));
        banners.add(new HomePageBannerEntity(picAddress[2]));
        banners.add(new HomePageBannerEntity(picAddress[3]));
        setDatas();
        adapter=new HomePageRecentVenueAdapter(getActivity(),datas);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvRecentVenue.setLayoutManager(layoutManager);
        rvRecentVenue.setAdapter(adapter);
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
        datas=new ArrayList<>();
        String []types={"室内","室外"};
        for (int i=0;i<10;i++){
            HomePageRecentVenueEntity entity=new HomePageRecentVenueEntity();
            entity.setBigPicUrl(R.drawable.demo_05+"");
            entity.setVenueName("成都体育馆");
            entity.setDegreeNumber(new Random().nextInt(5)+1);
            entity.setNumberGone(new Random().nextInt(1200)+1);
            entity.setNumberTalk(new Random().nextInt(3200)+1);
            List<String> typeList=new ArrayList<>();
            if (i%2==0){
                typeList.add(types[0]);
            }else {
                typeList.add(types[1]);
            }
            typeList.add("抱石");
            entity.setVenueType(typeList);
            datas.add(entity);
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
    private class HomePageFragmentReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String city=intent.getStringExtra("CITY");
            tvSearchCity.setText(city);
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
