package com.tiyujia.homesport.common.homepage.service;



import com.tiyujia.homesport.entity.Result;
import com.tiyujia.homesport.common.homepage.entity.HomePageData;
import com.tiyujia.homesport.entity.Service;

import rx.Observable;

/**
 * Created by zzqybyb19860112 on 2016/11/10.1
 */

public interface HomePageService extends Service{
    public Observable<Result<HomePageData>> getAllHotInfo();
}
