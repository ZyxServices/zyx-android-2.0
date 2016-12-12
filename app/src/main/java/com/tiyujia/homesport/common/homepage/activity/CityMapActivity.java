package com.tiyujia.homesport.common.homepage.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.tiyujia.homesport.App;
import com.tiyujia.homesport.BootLoaderActivity;
import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;

public class CityMapActivity extends ImmersiveActivity {
    ImageView ivBack;
    private MapView mvMap;
    private AMap aMap;
    //声明AMapLocationClient类对象
    public AMapLocationClient client;
    private double lat;
    private double lon;
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

        ivBack=(ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getLocation() {
        client= BootLoaderActivity.client;
        AMapLocation Location =  client.getLastKnownLocation();
        
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
