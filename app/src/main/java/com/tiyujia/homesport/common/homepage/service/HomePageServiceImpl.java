package com.tiyujia.homesport.common.homepage.service;

import com.tiyujia.homesport.App;
import com.tiyujia.homesport.common.homepage.entity.HomePageData;
import com.tiyujia.homesport.entity.Result;
import com.tiyujia.homesport.util.RetrofitUtil;

import rx.Observable;

/**
 * Created by zzqybyb19860112 on 2016/11/10.111
 */

public class HomePageServiceImpl implements HomePageService {
    HomePageApi api;
    public HomePageServiceImpl() {
        api = RetrofitUtil.createApi(App.getContext(), HomePageApi.class);
    }
    @Override public Observable<Result<HomePageData>> getAllHotInfo() {
        return api.getAllHotInfo();
    }
}