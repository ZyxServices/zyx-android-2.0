package com.tiyujia.homesport.common.homepage.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Poi;
import com.amap.api.maps.AMap.OnPOIClickListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.App;
import com.tiyujia.homesport.BootLoaderActivity;
import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.entity.CityMapModel;
import com.tiyujia.homesport.common.record.fragment.RecordFragment;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.util.CityUtils;

import java.util.zip.Inflater;

import okhttp3.Call;
import okhttp3.Response;

public class CityMapActivity extends ImmersiveActivity implements OnMarkerClickListener {
    ImageView ivBack;
    private MapView mvMap;
    private AMap aMap;
    //声明AMapLocationClient类对象
    public AMapLocationClient client;
    private int type=1;//1、全部，2、距离当前经纬度最近，3、最热门， 4、最大难度
    private Integer number=100;
    private Integer pageNumber=1;
    private TextView tvCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_map);
        mvMap = (MapView) findViewById(R.id.mvMap);
        mvMap.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mvMap.getMap();
        }
        getLocation();
        aMap.setOnMarkerClickListener(this);
        ivBack=(ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void getLocation() {
        client =new AMapLocationClient(App.getContext());
        AMapLocationListener aMapLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if(aMapLocation!=null){
                    if (aMapLocation.getErrorCode() == 0) {
                        final double Latitude=aMapLocation.getLatitude();//获取纬度
                        final double Longitude=aMapLocation.getLongitude();//获取经度
                        OkGo.post(API.BASE_URL+"/v2/venue/findVenue")
                                .tag(this)
                                .params("type",type)
                                .params("lng",Longitude)//经度
                                .params("lat",Latitude)//纬度
                                .params("number",number)
                                .params("pageNumber",pageNumber)
                                .execute(new LoadCallback<CityMapModel>(CityMapActivity.this) {
                                    @Override
                                    public void onSuccess(CityMapModel city, Call call, Response response) {
                                        for (int i=0;i<city.data.size();i++){
                                            CityMapModel.City jk = city.data.get(i);
                                            LatLng mlatlng = new LatLng(jk.latitude,jk.longitude);
                                            MarkerOptions markerOptions=new MarkerOptions();
                                            markerOptions.position(mlatlng);
                                            markerOptions.title(jk.name).snippet(jk.id+"");
                                            markerOptions.draggable(true);
                                            View view=getLayoutInflater().inflate(R.layout.city_info_bubble,null);
                                            tvCity=(TextView)view.findViewById(R.id.tvCity);
                                            TextView  tvNumber=(TextView)view.findViewById(R.id.tvNumber);
                                            tvNumber.setText(i+"");
                                            tvCity.setText(jk.name);
                                            markerOptions.icon(BitmapDescriptorFactory.fromView(view));
                                            markerOptions.setFlat(true);
                                            aMap.addMarker(markerOptions);
                                            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Latitude,Longitude), 10));
                                        }

                                    }
                                    @Override
                                    public void onError(Call call, Response response, Exception e) {
                                        super.onError(call, response, e);
                                        showToast("网络连接失败");
                                    }
                                });
                    }
                }
            }
        };
        client.setLocationListener(aMapLocationListener);
        AMapLocationClientOption option=  new AMapLocationClientOption();
        //设置模式为高精度
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果，该方法默认为false
        option.setOnceLocation(true);
        //获取最近3S内精度最高的一次定位结果
        //设置setOnceLocationLatest(boolean b)接口为true。启动定位是SKD会返回最近3秒最高的一次定位结果，如果设置为true，setOnceLocation(boolean b)也会为true，反之不会，默认为false。
        option.setOnceLocationLatest(true);
        //给定位客户端对象设置定位参数
        client.setLocationOption(option);
        client.startLocation();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mvMap.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mvMap.onPause();
    }
    @Override
    protected void onStop() {
        super.onStop();
        client.stopLocation();//停止定位
    }
    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mvMap.onSaveInstanceState(outState);
    }
    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mvMap.onDestroy();
        client.onDestroy();//销毁定位客户端。
    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        String name=marker.getTitle();
        int id=Integer.parseInt(marker.getSnippet());
        SharedPreferences share = getSharedPreferences("City",MODE_PRIVATE);
        SharedPreferences.Editor etr = share.edit();
        etr.putString("name",name);
        etr.putInt("id",id);
        etr.apply();
        finish();
        return true;
    }

}
