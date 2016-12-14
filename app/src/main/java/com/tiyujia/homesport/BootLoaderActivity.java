package com.tiyujia.homesport;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.tiyujia.homesport.util.CityUtils;

/**
 * 作者: Cymbi on 2016/11/22 14:53.
 * 邮箱:928902646@qq.com
 */

public class BootLoaderActivity extends ImmersiveActivity {
    public   static final int HANDLER =1;
    public static AMapLocationClient client = null;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
                switch (msg.what){
                    case HANDLER:
                        startActivity(new Intent(BootLoaderActivity.this,HomeActivity.class));
                        finish();
                        break;
                }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bootloader_activity);
        getLocation();
        Message message=handler.obtainMessage(HANDLER);
        handler.sendMessageDelayed(message,2000);
    }
    private void getLocation() {
        client =new AMapLocationClient(App.getContext());
        AMapLocationListener aMapLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if(aMapLocation!=null){
                    String result =CityUtils.getLocationStr(aMapLocation);
                    Log.i("city==>>",result);
                    AMapLocation Location = client.getLastKnownLocation();
                    String City= Location.getCity();
                    SharedPreferences share=getSharedPreferences("UserInfo",MODE_PRIVATE);
                    SharedPreferences.Editor etr = share.edit();
                    etr.putString("City",City);
                    etr.apply();
                }else {
                }
            }
        };
        client.setLocationListener(aMapLocationListener);
        AMapLocationClientOption  option=  new AMapLocationClientOption();
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
}
