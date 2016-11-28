package com.tiyujia.homesport.util;

import android.content.Context;

import com.tiyujia.homesport.API;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zzqybyb19860112 on 2016/11/10.
 */

public class RetrofitUtil {
    private static volatile Retrofit retrofit;
    public static <T> T createApi(Context context, Class<T> clazz) {
        if (retrofit == null) {
            synchronized (RetrofitUtil.class) {
                if (retrofit == null) {
                    retrofit = new Retrofit.Builder().baseUrl(API.BASE_URL)
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(OkHttpUtil.getInstance(context))
                            .build();
                }
            }
        }
        return retrofit.create(clazz);
    }
}
