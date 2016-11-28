package com.tiyujia.homesport.common.homepage.fragment;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;
import com.tiyujia.homesport.BaseFragment;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.adapter.HomePageCourseVideoAdapter;
import com.tiyujia.homesport.entity.VideoEntity;
import com.tiyujia.homesport.util.RefreshUtil;
import com.tiyujia.homesport.util.VideoUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者: Cymbi on 2016/11/17 17:50.
 * 邮箱:928902646@qq.com1
 */

public class CourseAllFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
    private View view;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout srlRefresh;
    private List<VideoEntity> mDatas;
    public static final int HANDLE_DATA=1;
    public HomePageCourseVideoAdapter adapter;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HANDLE_DATA:
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(layoutManager);
                    adapter=new HomePageCourseVideoAdapter(getActivity(),mDatas);
//                    adapter.setFriends(mDatas);
//                    adapter.getFilter().filter(HomePageVenueSurveyActivity.getSearchText());
                    recyclerView.setAdapter(adapter);
                    srlRefresh.setRefreshing(false);
                    break;
            }
        }
    };
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.recycleview_layout,null);
        return view;
    }
    @Override
    protected void initData() {
        recyclerView= (RecyclerView)view.findViewById(R.id.recyclerView);
        srlRefresh= (SwipeRefreshLayout)view.findViewById(R.id.srlRefresh);
        setData();
        RefreshUtil.refresh(srlRefresh, getActivity());
        srlRefresh.setOnRefreshListener(this);
    }

    private void setData() {
        mDatas = new ArrayList<>();
        int [] localPath={R.raw.brother,R.raw.gzqy,R.raw.brother};
        String [] titles={"兄弟","鸽子情缘","兄弟"};
        VideoView videoView=new VideoView(getActivity());
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
        for (int i=0;i<3;i++){
            VideoEntity entity=new VideoEntity();
            entity.setTitle(titles[i]);
            entity.setLocalPath(localPath[i]);
            String uri = "android.resource://" + getActivity().getPackageName() + "/" + localPath[i];
            videoView.setVideoURI(Uri.parse(uri));
            Drawable drawable=VideoUtil.getThumbnail(getActivity(),Uri.parse(uri));
            entity.setDrawable(drawable);
            int timeTotal=videoView.getDuration();
            long totalTime=(long)timeTotal;
            String timeNow=sdf.format(totalTime);
            String []hms=timeNow.split(":");
            if (hms[0].equals("00")){
                timeNow=timeNow.substring(3);
            }
            entity.setTotalTime(timeNow);
            mDatas.add(entity);
        }
        handler.sendEmptyMessage(HANDLE_DATA);
    }

    @Override
    public void onRefresh() {
        setData();
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                // 停止刷新
                srlRefresh.setRefreshing(false);
            }
        }, 1000);
    }
}
