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

public class BootLoaderActivity extends CheckPermissionsActivity {
    public   static final int HANDLER =1;

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
        Message message=handler.obtainMessage(HANDLER);
        handler.sendMessageDelayed(message,2000);
    }

}
