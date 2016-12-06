package com.tiyujia.homesport.common.personal.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tiyujia.homesport.API;
import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.personal.adapter.DynamicAdapter;
import com.tiyujia.homesport.common.personal.model.MyDynamicModel;
import com.tiyujia.homesport.util.GetUtil;
import com.tiyujia.homesport.util.RefreshUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * 作者: Cymbi on 2016/11/14 17:25.
 * 邮箱:928902646@qq.com
 */
public class PersonalDynamic extends ImmersiveActivity implements SwipeRefreshLayout.OnRefreshListener{
    @Bind(R.id.personal_back) ImageView personal_back;
    @Bind(R.id.srlRefresh)  SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.recyclerView)  RecyclerView recyclerView;
    @Bind(R.id.tv_title)    TextView tv_title;
    private String mToken;
    private int mUserId;
    List<MyDynamicModel> activityList=new ArrayList<>();
    public static final int HANFLE_DATA_UPDATE=1;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case  HANFLE_DATA_UPDATE:
                    DynamicAdapter   adapter= new DynamicAdapter(PersonalDynamic.this,activityList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    swipeRefresh.setRefreshing(false);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_dynamic);
        setInfo();
        getdata();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RefreshUtil.refresh(swipeRefresh,this);
        swipeRefresh.setOnRefreshListener(this);
    }

    private void getdata() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String uri=API.BASE_URL+"/v2/my/concern/list";
                HashMap<String ,String> params=new HashMap<String, String>();
                params.put("token",mToken);
                params.put("accountId",mUserId+"");
                String result=GetUtil.sendGetMessage(uri,params);
                try {
                    JSONObject json=new JSONObject(result);
                    JSONArray data = json.getJSONArray("data");
                    for (int i=0;i<data.length();i++){
                        ArrayList<String> imageURL=new ArrayList<String>();
                        JSONObject  object=  data.getJSONObject(i);
                        int id=object.getInt("id");
                        int userId=object.getInt("userId");
                   //     int type=object.getInt("type");
                        int zanCounts=object.getInt("zanCounts");
                        int commentCounts=object.getInt("commentCounts");
                        long createTime=object.getLong("createTime");
                        String topicContent=object.getString("topicContent");
                        String local=object.getString("local");
                        JSONObject userIconVo=object.getJSONObject("userIconVo");
                        int userIconVoId=userIconVo.getInt("id");
                        String nickName=userIconVo.getString("nickName");
                        String avatar=userIconVo.getString("avatar");

                        String urls= object.getString("imgUrl");
                        if (urls==null|urls.equals("null")|urls.equals("")){
                        }else {
                            String [] imageUrls=urls.split(",");
                            for (int j=0;j<imageUrls.length;j++){
                                imageURL.add("http://image.tiyujia.com/"+imageUrls[j]);
                            }
                        }
                        MyDynamicModel model =new MyDynamicModel();
                        model.setId(id);
                        model.setUserId(userId);
                      //  model.setType(type);
                        model.setZanCounts(zanCounts);
                        model.setCommentCounts(commentCounts);
                        model.setCreateTime(createTime);
                        model.setTopicContent(topicContent);
                        model.setLocal(local);
                        MyDynamicModel.UserIconVo info=new MyDynamicModel.UserIconVo();
                        info.setId(userIconVoId);
                        info.setNickName(nickName);
                        info.setAvatar(avatar);
                        model.setUserIconVo(info);
                        model.setImageUrl(imageURL);
                        activityList.add(model);
                    }
                    handler.sendEmptyMessage(HANFLE_DATA_UPDATE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void setInfo() {
        SharedPreferences share = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        mToken=share.getString("Token","");
        mUserId=share.getInt("UserId",0);
        tv_title.setText("我的动态");
        personal_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @Override
    public void onRefresh() {
        if(activityList.size()!=0){
            activityList.clear();
            getdata();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // 停止刷新
                    swipeRefresh.setRefreshing(false);
                }
            }, 1000);
        }else {
        }
    }
}
