package com.tiyujia.homesport;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.lzy.okgo.OkGo;

/**
 * 作者: Cymbi on 2016/10/19 17:25.1
 * 邮箱:928902646@qq.com
 */

public class App extends Application {
    private static Context mContext = null;
    public static boolean debug = true;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        OkGo.init(this);
    }
    public static Context getContext() {
        return mContext;
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
