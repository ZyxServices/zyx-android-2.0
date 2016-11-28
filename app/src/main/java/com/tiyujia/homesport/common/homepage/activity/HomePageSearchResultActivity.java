package com.tiyujia.homesport.common.homepage.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.adapter.HomePageDiscussAdapter;
import com.tiyujia.homesport.common.homepage.adapter.HomePageVenueUserAdapter;
import com.tiyujia.homesport.common.homepage.entity.CallBackDetailEntity;
import com.tiyujia.homesport.common.homepage.entity.HomePageDiscussEntity;
import com.tiyujia.homesport.common.homepage.entity.HomePageVenueWhomGoneEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import butterknife.Bind;
//1
public class HomePageSearchResultActivity extends ImmersiveActivity {
    @Bind(R.id.rvHomePageVenueDetailWhomGone) RecyclerView rvHomePageVenueDetailWhomGone;
    @Bind(R.id.rvHomePageVenueDetailSay) RecyclerView rvHomePageVenueDetailSay;
    List<HomePageVenueWhomGoneEntity> list;
    HomePageVenueUserAdapter userAdapter;
    List<HomePageDiscussEntity> mValue;
    HomePageDiscussAdapter discussAdapter;
    public static final int HANDLE_BASE_DATA=1;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HANDLE_BASE_DATA:
                    userAdapter=new HomePageVenueUserAdapter(HomePageSearchResultActivity.this,list);
                    LinearLayoutManager manager1 = new LinearLayoutManager(HomePageSearchResultActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    rvHomePageVenueDetailWhomGone.setLayoutManager(manager1);
                    rvHomePageVenueDetailWhomGone.setAdapter(userAdapter);
                    discussAdapter=new HomePageDiscussAdapter(HomePageSearchResultActivity.this,mValue);
                    LinearLayoutManager manager2 = new LinearLayoutManager(HomePageSearchResultActivity.this, LinearLayoutManager.VERTICAL, false);
                    rvHomePageVenueDetailSay.setLayoutManager(manager2);
                    rvHomePageVenueDetailSay.setAdapter(discussAdapter);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_search_result);
        setData();
    }

    private void setData() {
        list=new ArrayList<>();
        int [] pictures={R.drawable.demo_05,R.drawable.demo_06,R.drawable.demo_10,R.drawable.demo_10};
        String []userName={"土拨鼠","萌小妹","小美爱赵伟","萌小美美"};
        int [] levels={R.mipmap.img_lv1,R.mipmap.img_lv2,R.mipmap.img_lv3,R.mipmap.img_lv4,
                R.mipmap.img_lv5,R.mipmap.img_lv6,R.mipmap.img_lv7,R.mipmap.img_lv8,R.mipmap.img_lv9,
                R.mipmap.img_lv10,R.mipmap.img_lv11,R.mipmap.img_lv12,R.mipmap.img_lv13};
        for (int i=0;i<4;i++){
            HomePageVenueWhomGoneEntity entity=new HomePageVenueWhomGoneEntity();
            entity.setUserPhotoUrl(pictures[i]+"");
            entity.setUserName(userName[i]);
            entity.setUserLevelUrl(levels[new Random().nextInt(13)]+"");
            list.add(entity);
        }
        HomePageVenueWhomGoneEntity empty=new HomePageVenueWhomGoneEntity();
        list.add(empty);

        String [] contents={"今天天气真好，我们一起去打篮球吧","今天天气真好，我们一起去球场上打篮球吧，来pk一下撒！大家说，怎么样呢！。。。。","明天天气很好，我们到时一起去打篮球吧"};
        mValue=new ArrayList<>();
        for (int i=0;i<3;i++){
            HomePageDiscussEntity entity=new HomePageDiscussEntity();
            entity.setMainUserName(userName[i]);
            entity.setMainUserLevelUrl(levels[new Random().nextInt(13)]+"");
            entity.setMainUserPhotoUrl(pictures[3-i]+"");
            entity.setMainUserSendContent(contents[i]);
            entity.setMainUserSendTime("3分钟前");
            List<String> urls=new ArrayList<>();
            for (int j=0;j<=i;j++){
                urls.add(pictures[j]+"");
            }
            entity.setMainUserSendPicUrlList(urls);
            String [] discussTexts={"说走咱就走啊，一路看天不回头啊，水里火里一碗酒啊，路见不平一声吼啊，该出手时就出手啊！","走撒，那个SB不去！","打毛线，要下雨哈！"};
            List<CallBackDetailEntity> entityList=new ArrayList<>();
            if (i!=0){
                for (int k=0;k<3;k++){
                    CallBackDetailEntity entityDetail=new CallBackDetailEntity();
                    entityDetail.setCallFrom(userName[i%2==0?k:3-k]);
                    entityDetail.setCallTo(userName[i%2==1?k:3-k]);
                    entityDetail.setCallDetail(discussTexts[new Random().nextInt(3)]);
                    entityList.add(entityDetail);
                }
            }else {
                CallBackDetailEntity entityDetail=new CallBackDetailEntity();
                entityDetail.setCallFrom(userName[i]);
                entityDetail.setCallDetail(discussTexts[new Random().nextInt(3)]);
                entityList.add(entityDetail);
            }
            entity.setDiscussList(entityList);
            mValue.add(entity);
        }
        handler.sendEmptyMessage(HANDLE_BASE_DATA);
    }
}
