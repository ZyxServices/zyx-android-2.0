package com.tiyujia.homesport.common.homepage.service;

import com.tiyujia.homesport.common.homepage.entity.HomePageData;
import com.tiyujia.homesport.entity.Result;

import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by zzqybyb19860112 on 2016/11/10.1
 */

public interface HomePageApi {
    /**
     * 获取首页信息
     */
    @POST("/v1/deva/getAll")
    Observable<Result<HomePageData>> getAllHotInfo();
}
