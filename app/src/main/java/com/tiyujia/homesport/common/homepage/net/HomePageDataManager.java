package com.tiyujia.homesport.common.homepage.net;

import com.tiyujia.homesport.common.homepage.service.HomePageServiceImpl;
import com.tiyujia.homesport.entity.Service;
import com.tiyujia.homesport.entity.UserServiceImpl;

import java.util.HashMap;

/**
 * Created by zzqybyb19860112 on 2016/11/10.1
 */

public class HomePageDataManager {
    private static HashMap<String, Service> services = new HashMap<String, Service>();
    public static <T extends Service> T getService(Class<T> clazz) {
        if (clazz == null) {
            throw new NullPointerException("clazz == null");
        }
        T service = (T) services.get(clazz.getName());
        if (service == null) {
            synchronized (clazz) {
                if (service == null) {
                    if (clazz.isAssignableFrom(UserServiceImpl.class)) {
                        service = (T) new UserServiceImpl();
                        services.put(clazz.getName(), service);
                        service = (T) services.get(clazz.getName());
                    }
                    if (clazz.isAssignableFrom(HomePageServiceImpl.class)) {
                        service = (T) new HomePageServiceImpl();
                        services.put(clazz.getName(), service);
                        service = (T) services.get(clazz.getName());
                    }
                }
            }
        }
        return service;
    }
}
