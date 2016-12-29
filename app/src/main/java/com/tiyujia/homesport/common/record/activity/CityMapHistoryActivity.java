package com.tiyujia.homesport.common.record.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.App;
import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.entity.CityMapModel;
import com.tiyujia.homesport.common.record.model.CityHistoryModel;
import com.tiyujia.homesport.common.record.model.CityMapHistroyModel;
import com.tiyujia.homesport.common.record.model.OverViewModel;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.entity.LzyResponse;

import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;

public class CityMapHistoryActivity extends ImmersiveActivity {
    ImageView ivBack;
    private MapView mvMap;
    private AMap aMap;
    //声明AMapLocationClient类对象
    public AMapLocationClient client;
    private int type=1;//1、全部，2、距离当前经纬度最近，3、最热门， 4、最大难度
    private Integer number=100;
    private Integer pageNumber=1;
    private TextView tvCity;
    private String token;
    @Bind(R.id.tvCityNumber) TextView tvCityNumber;
    @Bind(R.id.tvHistoryNumber) TextView tvHistoryNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_map);
        SharedPreferences share=getSharedPreferences("UserInfo",MODE_PRIVATE);
        token=share.getString("Token","");
        mvMap = (MapView) findViewById(R.id.mvMap);
        mvMap.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mvMap.getMap();
        }

        OkGo.post(API.BASE_URL+"/v2/record/overview")
                .params("token",token)
                .execute(new LoadCallback<LzyResponse<OverViewModel>>(this) {
                    @Override
                    public void onSuccess(LzyResponse<OverViewModel> model, Call call, Response response) {
                        if(model.state==200){
                            tvCityNumber.setText(model.data.cityNumber+"");
                            tvHistoryNumber.setText(model.data.fpNumber+"");
                        }
                    }
                });

        getLocation();
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
                        AMapLocation Location =  client.getLastKnownLocation();
                        final double Latitude=Location.getLatitude();//获取纬度
                        final double Longitude=Location.getLongitude();//获取经度
                        OkGo.post(API.BASE_URL+"/v2/record/footprint/city")
                                .tag(this)
                                .params("token",token)
                                .execute(new LoadCallback<CityMapHistroyModel>(CityMapHistoryActivity.this) {
                                    @Override
                                    public void onSuccess(CityMapHistroyModel city, Call call, Response response) {
                                        if(city.state==200){
                                            if(city.data!=null&&city.data.size()>0){
                                                for (int i=0;i<city.data.size();i++){
                                                    CityMapHistroyModel.History jk = city.data.get(i);
                                                    for(int j=0;j<jk.footprints.size();j++){
                                                        CityMapHistroyModel.History.Footprints js = jk.footprints.get(j);
                                                        LatLng mlatlng = new LatLng(js.latitude,js.longitude);
                                                        MarkerOptions markerOptions=new MarkerOptions();
                                                        markerOptions.position(mlatlng);
                                                        markerOptions.title(js.venueName).snippet(js.address+"");
                                                        markerOptions.draggable(true);
                                                        View view=getLayoutInflater().inflate(R.layout.city_info_bubble,null);
                                                        tvCity=(TextView)view.findViewById(R.id.tvCity);
                                                        TextView  tvNumber=(TextView)view.findViewById(R.id.tvNumber);
                                                        tvNumber.setText(j+"");
                                                        tvCity.setText(js.venueName);
                                                        markerOptions.icon(BitmapDescriptorFactory.fromView(view));
                                                        //markerOptions.setFlat(true);
                                                        aMap.addMarker(markerOptions);
                                                        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Latitude,Longitude), 10));
                                                    }
                                                }
                                            }else {

                                        }}
                                    }
                                    @Override
                                    public void onError(Call call, Response response, Exception e) {
                                        super.onError(call, response, e);
                                        showToast("服务器故障");
                                    }
                                });
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


}
