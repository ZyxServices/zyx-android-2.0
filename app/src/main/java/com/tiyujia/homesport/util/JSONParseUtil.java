package com.tiyujia.homesport.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.tiyujia.homesport.API;
import com.tiyujia.homesport.common.homepage.entity.HomePageRecentVenueEntity;
import com.tiyujia.homesport.common.homepage.entity.SearchActiveEntity;
import com.tiyujia.homesport.common.homepage.entity.SearchDynamicEntity;
import com.tiyujia.homesport.common.homepage.entity.SearchEquipEntity;
import com.tiyujia.homesport.common.homepage.entity.SearchVenueEntity;
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
                            urls.add(API.PICTURE_URL+s);
                        }
                    }else {
                        urls=new ArrayList<>();
                    }
                    entity.setImgUrls(urls);
                    String level=data.getString("level");
                    int degree=5;
                    if (!level.equals("")&&!level.equals("null")&&level!=null){
                        int tmpDegreee=Integer.valueOf(level);
                        if (tmpDegreee>5){
                            tmpDegreee=5;
                        }
                        degree=tmpDegreee;
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
    public static void parseNetDataSearchActive(Context context, String result, String name, List<SearchActiveEntity> list, Handler handler, int state){
        CacheUtils.writeJson(context, result, name, false);
        handleSearchActive(result,list,handler,state);
    }
    private static void handleSearchActive(String result,  List<SearchActiveEntity> list, Handler handler, int stateFinal){
        try {
            if (list.size() != 0) {
                list.clear();
            }
            JSONObject object=new JSONObject(result);
            int state = object.getInt("state");
            if (state!=200) {
                handler.sendEmptyMessage(stateFinal);
                return;
            }else {
                JSONArray array=object.getJSONArray("data");
                for (int i=0;i<array.length();i++){
                    JSONObject data=array.getJSONObject(i);
                    SearchActiveEntity entity=new SearchActiveEntity();
                    String type=data.getString("activityType").equals("0")?"求约":"求带";
                    entity.setActiveId(data.getInt("id"));
                    entity.setType(type);
                    entity.setImageUrl(API.PICTURE_URL+data.getString("imgUrl"));
                    entity.setTitle(data.getString("title"));
                    entity.setAlreadyRegistered(data.getInt("alreadyPeople"));
                    int restNumber=data.getInt("maxPeople")-data.getInt("alreadyPeople");
                    entity.setRestNumber(restNumber);
                    entity.setReward(data.getInt("price"));
                    list.add(entity);
                }
                handler.sendEmptyMessage(stateFinal);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void parseLocalDataSearchActive(Context context, String name, List<SearchActiveEntity> list, Handler handler, int state){
        List<String> cacheData= (ArrayList<String>) CacheUtils.readJson(context,name);
        String result=cacheData.get(0);
        handleSearchActive(result,list,handler,state);
    }
    public static void parseNetDataSearchEquip(Context context, String result, String name, List<SearchEquipEntity> list, Handler handler, int state){
        CacheUtils.writeJson(context, result, name, false);
        handleSearchEquip(result,list,handler,state);
    }
    private static void handleSearchEquip(String result,  List<SearchEquipEntity> list, Handler handler, int stateFinal){
        try {
            if (list.size() != 0) {
                list.clear();
            }
            JSONObject object=new JSONObject(result);
            int state = object.getInt("state");
            if (state!=200) {
                handler.sendEmptyMessage(stateFinal);
                return;
            }else {
                JSONArray array=object.getJSONArray("data");
                for (int i=0;i<array.length();i++){
                    JSONObject data=array.getJSONObject(i);
                    SearchEquipEntity entity=new SearchEquipEntity();
                    entity.setEquipId(data.getInt("id"));
                    entity.setEquipTitle(data.getString("title"));
                    String imageUrl=data.getString("imgUrl");
                    List<String> images=new ArrayList<>();
                    if (imageUrl!=null&&!imageUrl.equals("")){
                    String urlList[]=imageUrl.split(",");
                    if (urlList.length!=0){
                        for (String s:urlList){
                            images.add(API.PICTURE_URL+s);
                        }
                    }
                    }
                    entity.setEquipImageUrls(images);
                    list.add(entity);
                }
                handler.sendEmptyMessage(stateFinal);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void parseLocalDataSearchEquip(Context context, String name, List<SearchEquipEntity> list, Handler handler, int state){
        List<String> cacheData= (ArrayList<String>) CacheUtils.readJson(context,name);
        String result=cacheData.get(0);
        handleSearchEquip(result,list,handler,state);
    }
    public static void parseNetDataSearchDynamic(Context context, String result, String name, List<SearchDynamicEntity> list, Handler handler, int state){
        CacheUtils.writeJson(context, result, name, false);
        handleSearchDynamic(result,list,handler,state);
    }
    private static void handleSearchDynamic(String result,  List<SearchDynamicEntity> list, Handler handler, int stateFinal){
        try {
            if (list.size() != 0) {
                list.clear();
            }
            JSONObject object=new JSONObject(result);
            int state = object.getInt("state");
            if (state!=200) {
                handler.sendEmptyMessage(stateFinal);
                return;
            }else {
                JSONArray array=object.getJSONArray("data");
                for (int i=0;i<array.length();i++){
                    JSONObject data=array.getJSONObject(i);
                    SearchDynamicEntity entity=new SearchDynamicEntity();
                    entity.setDynamicId(data.getInt("id"));
                    entity.setDynamicTitle(data.getString("topicContent"));
                    String imageUrl=data.getString("imgUrl");
                    List<String> images = new ArrayList<>();
                    if (imageUrl!=null&&!imageUrl.equals("")) {
                        String urlList[] = imageUrl.split(",");
                        if (urlList.length != 0) {
                            for (String s : urlList) {
                                images.add(API.PICTURE_URL + s);
                            }
                        }
                    }
                    entity.setDynamicImageList(images);
                    list.add(entity);
                }
                handler.sendEmptyMessage(stateFinal);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void parseLocalDataSearchDynamic(Context context, String name, List<SearchDynamicEntity> list, Handler handler, int state){
        List<String> cacheData= (ArrayList<String>) CacheUtils.readJson(context,name);
        String result=cacheData.get(0);
        handleSearchDynamic(result,list,handler,state);
    }
    public static void parseNetDataSearchVenue(Context context, String result, String name, List<SearchVenueEntity> list, Handler handler, int state){
        CacheUtils.writeJson(context, result, name, false);
        handleSearchVenue(result,list,handler,state);
    }
    private static void handleSearchVenue(String result,  List<SearchVenueEntity> list, Handler handler, int stateFinal){
        try {
            if (list.size() != 0) {
                list.clear();
            }
            JSONObject object=new JSONObject(result);
            int state = object.getInt("state");
            if (state!=200) {
                handler.sendEmptyMessage(stateFinal);
                return;
            }else {
                JSONArray array=object.getJSONArray("data");
                for (int i=0;i<array.length();i++){
                    JSONObject data=array.getJSONObject(i);
                    SearchVenueEntity entity=new SearchVenueEntity();
                    entity.setVenueId(data.getInt("id"));
                    entity.setVenueName(data.getString("name"));
                    entity.setVenueType(Integer.valueOf(data.getString("type")));
                    entity.setVenueMark(data.getString("mark"));
                    String imageUrl=data.getString("imgUrl");
                    entity.setVenuePicture(API.PICTURE_URL+imageUrl);
                    entity.setVenueDegree(data.getInt("level"));
                    list.add(entity);
                }
                handler.sendEmptyMessage(stateFinal);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void parseLocalDataSearchVenue(Context context, String name, List<SearchVenueEntity> list, Handler handler, int state){
        List<String> cacheData= (ArrayList<String>) CacheUtils.readJson(context,name);
        String result=cacheData.get(0);
        handleSearchVenue(result,list,handler,state);
    }
}
