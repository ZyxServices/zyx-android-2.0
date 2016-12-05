package com.tiyujia.homesport;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.common.homepage.fragment.HomePageFragment;

/**
 * 作者: Cymbi on 2016/10/19 17:25.1
 * 邮箱:928902646@qq.com
 */

public class App extends Application {
    private static Context mContext = null;
    public static boolean debug = true;
    public static AMapLocationClient mLocationClient = null;
    public static String nowCity=null;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        OkGo.init(this);
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        AMapLocationClientOption option=new AMapLocationClientOption();
        AMapLocationListener locationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation loc) {
                if (null != loc) {
                    //解析定位结果
                    nowCity = loc.getCity();
                    if (nowCity!=null&&!nowCity.equals("")){
                        Intent intent=new Intent();
                        intent.setAction("GET_LOCATION");
                        intent.putExtra("CITY",nowCity);
                        mContext.sendBroadcast(intent);
                    }
                }
            }
        };
        mLocationClient.setLocationListener(locationListener);
        resetOption(option);
        mLocationClient.setLocationOption(option);
        // 启动定位
        mLocationClient.startLocation();
    }
    public static Context getContext() {
        return mContext;
    }

    private   void resetOption(AMapLocationClientOption option) {
        option= new AMapLocationClientOption();
        option.setNeedAddress(true);
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        option.setGpsFirst(false);
        // 设置是否开启缓存
        option.setLocationCacheEnable(true);
        //设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
        option.setOnceLocationLatest(true);
        //设置是否使用传感器
        option.setInterval(2000);//设置定位间隔,单位毫秒,默认为2000ms
    }
}
