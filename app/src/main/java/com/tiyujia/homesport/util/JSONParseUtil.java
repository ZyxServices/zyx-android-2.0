package com.tiyujia.homesport.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.tiyujia.homesport.API;
import com.tiyujia.homesport.common.homepage.entity.HomePageBannerEntity;
import com.tiyujia.homesport.common.homepage.entity.HomePageRecentVenueEntity;
import com.tiyujia.homesport.common.homepage.entity.SearchActiveEntity;
import com.tiyujia.homesport.common.homepage.entity.SearchCourseEntity;
import com.tiyujia.homesport.common.homepage.entity.SearchDynamicEntity;
import com.tiyujia.homesport.common.homepage.entity.SearchEquipEntity;
import com.tiyujia.homesport.common.homepage.entity.SearchUserEntity;
import com.tiyujia.homesport.common.homepage.entity.SearchVenueEntity;
import com.tiyujia.homesport.common.homepage.entity.UserModelEntity;
import com.tiyujia.homesport.common.homepage.entity.VenueWholeBean;
import com.tiyujia.homesport.common.homepage.entity.WholeSearchEntity;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

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
                    entity.setTalkNumber(data.getInt("commentNumber"));
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
                data.setDevelopBackground("");
                data.setVenueAddress(entity.getString("address"));
                data.setVenueDegree(entity.getInt("level"));
                data.setVenueDescription(entity.getString("description"));
                data.setVenuePhone(entity.getString("phone"));
                data.setVenueName(entity.getString("name"));
                String images=entity.getString("imgUrls");
               /* List<String> urlList=new ArrayList<>();
                if (images.contains(",")){
                    String[] urls=images.split(",");
                    for (String s:urls){
                        urlList.add(API.PICTURE_URL+s);
                    }
                }else {
                    urlList.add(API.PICTURE_URL+images);
                }*/
                data.setVenueImages(images);
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
                    if (imageUrl!=null&&!imageUrl.equals("")&&!imageUrl.equals("null")){
                        if (imageUrl.contains(",")){
                        String urlList[]=imageUrl.split(",");
                        for (String s:urlList){
                            images.add(API.PICTURE_URL+s);
                    }
                    }else {
                            images.add(API.PICTURE_URL+imageUrl);
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
                            for (String s:urlList) {
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
    public static void parseNetDataSearchCourse(Context context, String result, String name, List<SearchCourseEntity> list, Handler handler, int state){
        CacheUtils.writeJson(context, result, name, false);
        handleSearchCourse(result,list,handler,state);
    }
    private static void handleSearchCourse(String result,  List<SearchCourseEntity> list, Handler handler, int stateFinal){
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
                    SearchCourseEntity entity=new SearchCourseEntity();
                    entity.setCourseId(data.getInt("id"));
                    entity.setCourseTitle(data.getString("title"));
                    entity.setCoursePicture(API.PICTURE_URL+data.getString("imgUrl"));
                    list.add(entity);
                }
                handler.sendEmptyMessage(stateFinal);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void parseLocalDataSearchCourse(Context context, String name, List<SearchCourseEntity> list, Handler handler, int state){
        List<String> cacheData= (ArrayList<String>) CacheUtils.readJson(context,name);
        String result=cacheData.get(0);
        handleSearchCourse(result,list,handler,state);
    }
    public static void parseNetDataSearchUser(Context context, String result, String name, List<SearchUserEntity> list, Handler handler, int state){
        CacheUtils.writeJson(context, result, name, false);
        handleSearchUser(result,list,handler,state);
    }
    private static void handleSearchUser(String result,  List<SearchUserEntity> list, Handler handler, int stateFinal){
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
                    SearchUserEntity entity=new SearchUserEntity();
                    entity.setUserId(data.getInt("id"));
                    String tempStr=data.getString("avatar");
                    boolean isUrlRight=(tempStr==null||!tempStr.contains(".jpg")||!tempStr.contains(".png")||!tempStr.contains(".bmp")||!tempStr.contains(".gif"))?false:true;
                    if (isUrlRight){
                        entity.setUserPhotoUrl(API.PICTURE_URL+tempStr);
                    }else {
                        entity.setUserPhotoUrl(API.PICTURE_URL+"group1/M00/00/0F/dz1CN1g3_KmAXQW8AAAGQaCJo8Q077.jpg");
                    }
                    entity.setUserName(data.getString("nickname"));
                    String level=data.getString("level");
                    UserModelEntity modelEntity=new UserModelEntity();
                    if (!level.equals("")&&level!=null&&!level.equals("null")){
                       JSONObject obj=new JSONObject(level);
                        modelEntity.setPraiseCount(obj.getInt("pointCount"));
                        modelEntity.setUserLabel(obj.getString("pointDesc"));
                        modelEntity.setUserStep(obj.getInt("step"));
                        modelEntity.setUserModelId(obj.getInt("id"));
                    }else {
                        modelEntity=null;
                    }
                    entity.setEntity(modelEntity);
                    list.add(entity);
                }
                handler.sendEmptyMessage(stateFinal);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void parseLocalDataSearchUser(Context context, String name, List<SearchUserEntity> list, Handler handler, int state){
        List<String> cacheData= (ArrayList<String>) CacheUtils.readJson(context,name);
        String result=cacheData.get(0);
        handleSearchUser(result,list,handler,state);
    }
    public static void parseNetDataSearchAll(Context context, String result, String name, WholeSearchEntity data, Handler handler, int state){
        CacheUtils.writeJson(context, result, name, false);
        handleSearchAll(result,data,handler,state);
    }
    private static void handleSearchAll(String result,  WholeSearchEntity entity, Handler handler, int stateFinal){
        try {
            if (entity!=null) {
                entity=new WholeSearchEntity();
            }
            JSONObject object=new JSONObject(result);
            int state = object.getInt("state");
            if (state!=200) {
                handler.sendEmptyMessage(stateFinal);
                return;
            }else {
                JSONObject jsonObject=object.getJSONObject("data");
                //-------------------装备---------------------\\
                JSONArray equipArray=jsonObject.getJSONArray("Equip");
                List<SearchEquipEntity> equipEntityList=new ArrayList<>();
                for (int i=0;i<equipArray.length();i++){
                    JSONObject obj=equipArray.getJSONObject(i);
                    SearchEquipEntity searchEquipEntity=new SearchEquipEntity();
                    searchEquipEntity.setEquipId(obj.getInt("id"));
                    searchEquipEntity.setEquipTitle(obj.getString("title"));
                    String imageUrl=obj.getString("imgUrl");
                    List<String> images=new ArrayList<>();
                    if (imageUrl!=null&&!imageUrl.equals("")&&!imageUrl.equals("null")){
                        if (imageUrl.contains(",")){
                            String urlList[]=imageUrl.split(",");
                            if (urlList.length<=3) {
                                for (String s : urlList) {
                                    images.add(API.PICTURE_URL + s);
                                }
                            }else {
                                for (int j=0;j<3;j++) {
                                    images.add(API.PICTURE_URL + urlList[j]);
                                }
                            }
                        }else {
                            images.add(API.PICTURE_URL+imageUrl);
                        }
                    }
                    searchEquipEntity.setEquipImageUrls(images);
                    equipEntityList.add(searchEquipEntity);
                }
                entity.setEquipList(equipEntityList);
                //-------------------用户---------------------\\
                JSONArray userArray=jsonObject.getJSONArray("User");
                List<SearchUserEntity> userEntityList=new ArrayList<>();
                for (int i=0;i<userArray.length();i++){
                    JSONObject obj=userArray.getJSONObject(i);
                    SearchUserEntity searchUserEntity=new SearchUserEntity();
                    searchUserEntity.setUserId(obj.getInt("id"));
                    String tempStr=obj.getString("avatar");
                    boolean isFomatRight=(!tempStr.contains(".jpg")&&!tempStr.contains(".png")&&!tempStr.contains(".bmp")&&!tempStr.contains(".gif"));
                    boolean isUrlRight=(tempStr==null||isFomatRight)?false:true;
                    if (isUrlRight){
                        searchUserEntity.setUserPhotoUrl(API.PICTURE_URL+tempStr);
                    }else {
                        searchUserEntity.setUserPhotoUrl(API.PICTURE_URL+"group1/M00/00/0F/dz1CN1g3_KmAXQW8AAAGQaCJo8Q077.jpg");
                    }
                    searchUserEntity.setUserName(obj.getString("nickname"));
                    String level=obj.getString("level");
                    UserModelEntity modelEntity=new UserModelEntity();
                    if (!level.equals("")&&level!=null&&!level.equals("null")){
                        JSONObject obj1=new JSONObject(level);
                        modelEntity.setPraiseCount(obj1.getInt("pointCount"));
                        modelEntity.setUserLabel(obj1.getString("pointDesc"));
                        modelEntity.setUserStep(obj1.getInt("step"));
                        modelEntity.setUserModelId(obj1.getInt("id"));
                    }else {
                        modelEntity=null;
                    }
                    searchUserEntity.setEntity(modelEntity);
                    userEntityList.add(searchUserEntity);
                }
                entity.setUserList(userEntityList);
                //------------------场馆---------------------\\
                JSONArray venueArray=jsonObject.getJSONArray("Venues");
                List<SearchVenueEntity> venueEntityList=new ArrayList<>();
                for (int i=0;i<venueArray.length();i++){
                    JSONObject obj=venueArray.getJSONObject(i);
                    SearchVenueEntity searchVenueEntity=new SearchVenueEntity();
                    searchVenueEntity.setVenueId(obj.getInt("id"));
                    searchVenueEntity.setVenueName(obj.getString("name"));
                    searchVenueEntity.setVenueType(Integer.valueOf(obj.getString("type")));
                    searchVenueEntity.setVenueMark(obj.getString("mark"));
                    String imageUrl=obj.getString("imgUrl");
                    searchVenueEntity.setVenuePicture(API.PICTURE_URL+imageUrl);
                    searchVenueEntity.setVenueDegree(obj.getInt("level"));
                    venueEntityList.add(searchVenueEntity);
                }
                entity.setVenueList(venueEntityList);
                //------------------动态---------------------\\
                JSONArray dynamicArray=jsonObject.getJSONArray("Concern");
                List<SearchDynamicEntity> dynamicEntityList=new ArrayList<>();
                for (int i=0;i<dynamicArray.length();i++){
                    JSONObject obj=dynamicArray.getJSONObject(i);
                    SearchDynamicEntity searchDynamicEntity=new SearchDynamicEntity();
                    searchDynamicEntity.setDynamicId(obj.getInt("id"));
                    searchDynamicEntity.setDynamicTitle(obj.getString("topicContent"));
                    String imageUrl=obj.getString("imgUrl");
                    List<String> images = new ArrayList<>();
                    if (imageUrl!=null&&!imageUrl.equals("")&&!imageUrl.equals("null")) {
                        if (imageUrl.contains(",")){
                            String urlList[] = imageUrl.split(",");
                            if (urlList.length<=3){
                                for (String s:urlList) {
                                    images.add(API.PICTURE_URL + s);
                                }
                            }else {
                                for (int j=0;j<3;j++) {
                                    images.add(API.PICTURE_URL + urlList[j]);
                                }
                            }
                        }else {
                            images.add(API.PICTURE_URL+imageUrl);
                        }

                    }
                    searchDynamicEntity.setDynamicImageList(images);
                    dynamicEntityList.add(searchDynamicEntity);
                }
                entity.setDynamicList(dynamicEntityList);
                //------------------活动---------------------\\
                JSONArray activeArray=jsonObject.getJSONArray("Activity");
                List<SearchActiveEntity> activeEntityList=new ArrayList<>();
                for (int i=0;i<activeArray.length();i++){
                    JSONObject obj=activeArray.getJSONObject(i);
                    SearchActiveEntity searchActiveEntity=new SearchActiveEntity();
                    String type=obj.getString("activityType").equals("0")?"求约":"求带";
                    searchActiveEntity.setActiveId(obj.getInt("id"));
                    searchActiveEntity.setType(type);
                    searchActiveEntity.setImageUrl(API.PICTURE_URL+obj.getString("imgUrl"));
                    searchActiveEntity.setTitle(obj.getString("title"));
                    searchActiveEntity.setAlreadyRegistered(obj.getInt("alreadyPeople"));
                    int restNumber=obj.getInt("maxPeople")-obj.getInt("alreadyPeople");
                    searchActiveEntity.setRestNumber(restNumber);
                    searchActiveEntity.setReward(obj.getInt("price"));
                    activeEntityList.add(searchActiveEntity);
                }
                entity.setActiveList(activeEntityList);
                //------------------教程---------------------\\
                JSONArray courseArray=jsonObject.getJSONArray("Activity");
                List<SearchCourseEntity> courseEntityList=new ArrayList<>();
                for (int i=0;i<courseArray.length();i++){
                    JSONObject obj=activeArray.getJSONObject(i);
                    SearchCourseEntity searchCourseEntity=new SearchCourseEntity();
                    searchCourseEntity.setCourseId(obj.getInt("id"));
                    searchCourseEntity.setCourseTitle(obj.getString("title"));
                    searchCourseEntity.setCoursePicture(API.PICTURE_URL+obj.getString("imgUrl"));
                    courseEntityList.add(searchCourseEntity);
                }
                entity.setCourseList(courseEntityList);
                Message message=new Message();
                message.what=stateFinal;
                message.obj=entity;
                handler.sendMessage(message);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void parseLocalDataSearchAll(Context context, String name, WholeSearchEntity data, Handler handler, int state){
        List<String> cacheData= (ArrayList<String>) CacheUtils.readJson(context,name);
        String result=cacheData.get(0);
        handleSearchAll(result,data,handler,state);
    }
    public static void parseNetDataHomeBanner(Context context, String result, String name, List<HomePageBannerEntity> data, Handler handler, int state){
        CacheUtils.writeJson(context, result, name, false);
        handleHomeBanner(result,data,handler,state);
    }
    private static void handleHomeBanner(String result,  List<HomePageBannerEntity> list, Handler handler, int stateFinal){
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
                    HomePageBannerEntity entity=new HomePageBannerEntity();
                    entity.setId(data.getInt("id"));
                    entity.setModel(data.getInt("model"));
                    entity.setModelId(data.getInt("modelId"));
                    entity.setCreateTime(data.getLong("createTime"));
                    entity.setImageUrl(API.PICTURE_URL+data.getString("imageUrl"));
                    list.add(entity);
                }
                handler.sendEmptyMessage(stateFinal);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void parseLocalDataHomeBanner(Context context, String name, List<HomePageBannerEntity> data, Handler handler, int state){
        List<String> cacheData= (ArrayList<String>) CacheUtils.readJson(context,name);
        String result=cacheData.get(0);
        handleHomeBanner(result,data,handler,state);
    }
}
