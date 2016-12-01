package com.tiyujia.homesport.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.tiyujia.homesport.API;
import com.tiyujia.homesport.common.homepage.entity.HomePageRecentVenueEntity;
import com.tiyujia.homesport.common.homepage.entity.VenueWholeBean;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zzqybyb19860112 on 2016/11/29.
 */

public class JSONParseUtil {
    public static void parseNetDataVenue(Context context, String result, String name, List<HomePageRecentVenueEntity> datas, Handler handler, int state) {
        CacheUtils.writeJson(context, result, name, false);
        handleDataVenue(result,datas,handler,state);
    }
    private static void handleDataVenue(String result,  List<HomePageRecentVenueEntity> datas, Handler handler, int stateFinal) {
        try {
            if (datas.size() != 0) {
                datas.clear();
            }
            JSONObject object=new JSONObject(result);
            int state = object.getInt("state");
            if (state!=200) {
                handler.sendEmptyMessage(stateFinal);
                return;
            }else {
                JSONArray array=object.getJSONArray("data");
                for (int i=0;i<array.length();i++){
                    HomePageRecentVenueEntity entity=new HomePageRecentVenueEntity();
                    JSONObject data=array.getJSONObject(i);
                    entity.setId(data.getInt("id"));
                    entity.setType(data.getInt("type"));
                    entity.setName(data.getString("name"));
                    entity.setLongitude(data.getDouble("longitude"));
                    entity.setLatitude(data.getDouble("latitude"));
                    entity.setCity(data.getString("city"));
                    entity.setMark(data.getString("mark"));
                    entity.setDescription(data.getString("description"));
                    entity.setPhone(data.getString("phone"));
                    entity.setAddress(data.getString("address"));
                    String images=data.getString("imgUrls");
                    List<String> urls=new ArrayList<>();
                    if (images!=null&&!images.equals("")&&!images.equals("null")){
                        String [] imageUrls=images.split(",");
                        for (String s:imageUrls){
                            urls.add(s);
                        }
                    }else {
                        urls=new ArrayList<>();
                    }
                    entity.setImgUrls(urls);
                    String level=data.getString("level");
                    int degree=5;
                    if (!level.equals("")&&!level.equals("null")&&level!=null){
                        degree=Integer.valueOf(level);
                    }
                    entity.setLevel(degree);
                    entity.setCreate_time(data.getInt("create_time"));
                    entity.setDistance(data.getInt("distance"));
                    entity.setPnumber(data.getInt("pnumber"));
                    entity.setTalkNumber(new Random().nextInt(5000)+1000);
                    datas.add(entity);
                }
                handler.sendEmptyMessage(stateFinal);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void parseLocalDataVenue(Context context, String name, List<HomePageRecentVenueEntity> datas, Handler handler, int state) {
        List<String> cacheData= (ArrayList<String>) CacheUtils.readJson(context,name);
        String result=cacheData.get(0);
        handleDataVenue(result, datas,handler,state);
    }
    public static void parseNetDataVenueDetail(Context context, String result, String name, VenueWholeBean data, Handler handler, int state) {
        CacheUtils.writeJson(context, result, name, false);
        handleVenueDetail(result,data,handler,state);
    }
    private static void handleVenueDetail(String result,  VenueWholeBean data, Handler handler, int stateFinal){
        try{
            data=new VenueWholeBean();
            JSONObject object=new JSONObject(result);
            int state=object.getInt("state");
            if (state!=200) {
                handler.sendEmptyMessage(stateFinal);
                return;
            }else {
                JSONObject entity=object.getJSONObject("data");
                data.setVenueType(entity.getInt("type"));
//              data.setDevelopBackground(entity.getString("developBackground"));
                data.setDevelopBackground("");
                data.setVenueAddress(entity.getString("address"));
                data.setVenueDegree(entity.getInt("level"));
                data.setVenueDescription(entity.getString("description"));
                data.setVenuePhone(entity.getString("phone"));
                data.setVenueName(entity.getString("name"));
                String images=entity.getString("imgUrls");
                List<String> urlList=new ArrayList<>();
                if (images.contains(",")){
                    String[] urls=images.split(",");
                    for (String s:urls){
                        urlList.add(API.PICTURE_URL+s);
                    }
                }else {
                    urlList.add(API.PICTURE_URL+images);
                }
                data.setVenueImages(urlList);
                Message message=new Message();
                message.what=stateFinal;
                message.obj=data;
                handler.sendMessage(message);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void parseLocalDataVenueDetail(Context context, String name, VenueWholeBean data, Handler handler, int state) {
        List<String> cacheData= (ArrayList<String>) CacheUtils.readJson(context,name);
        String result=cacheData.get(0);
        handleVenueDetail(result,data,handler,state);
    }
}
