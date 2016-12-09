package com.tiyujia.homesport;

import android.Manifest;
import android.content.Intent;
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
import com.tiyujia.homesport.util.PermissionUtil;

/**
 * 作者: Cymbi on 2016/11/22 14:53.
 * 邮箱:928902646@qq.com
 */

public class BootLoaderActivity extends ImmersiveActivity {
    public   static final int HANDLER =1;
    public static AMapLocationClient client = null;
    public static String nowCity ;//当前城市
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
        client = new AMapLocationClient(this);
        AMapLocationClientOption option = new AMapLocationClientOption();
        String[] requestPermissions=new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
                Manifest.permission.CHANGE_CONFIGURATION,
                Manifest.permission.WRITE_SETTINGS,
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE};
        if (!PermissionUtil.hasPermission(this,requestPermissions)){
            PermissionUtil.requestPermission(this,11,requestPermissions);
        }else {
            //初始化定位参数
            //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
            option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置是否返回地址信息（默认返回地址信息）
            option.setNeedAddress(true);
            //设置是否只定位一次,默认为false
            option.setOnceLocation(true);
            //设置是否强制刷新WIFI，默认为强制刷新
            option.setWifiActiveScan(true);
            //设置是否允许模拟位置,默认为false，不允许模拟位置
            option.setGpsFirst(false);
            option.setLocationCacheEnable(true);
            option.setMockEnable(false);
            //设置定位间隔,单位毫秒,默认为2000ms
            option.setInterval(2000);
            //给定位客户端对象设置定位参数
            AMapLocationListener locationListener = new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation loc) {
                    if (null != loc) {
                        //解析定位结果
                        nowCity = loc.getCity();
                        Log.i("tag","now----------->"+nowCity);
                        Intent intent = new Intent("GET_LOCATION");
                        intent.putExtra("CITY", nowCity);
                        BootLoaderActivity.this.sendBroadcast(intent);
                    }
                }
            };
            client.setLocationListener(locationListener);
            client.setLocationOption(option);
            //启动定位
            client.startLocation();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 11) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                nowCity = "未知";
            }
        }
    }
}
