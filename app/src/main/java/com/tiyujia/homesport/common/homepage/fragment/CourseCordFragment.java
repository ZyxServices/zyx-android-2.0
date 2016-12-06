package com.tiyujia.homesport.common.homepage.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.BaseFragment;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.adapter.CourseAdapter;
import com.tiyujia.homesport.common.homepage.entity.CurseModel;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.util.RefreshUtil;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 教程:绳索
 * 作者: Cymbi on 2016/11/17 17:50.
 * 邮箱:928902646@qq.com1
 */

public class CourseCordFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
    private View view;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout srlRefresh;
    public CourseAdapter adapter;
    private int number=10;
    private int pageNumber=1;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.recycleview_layout,null);
        return view;
    }
    @Override
    protected void initData() {
        recyclerView= (RecyclerView)view.findViewById(R.id.recyclerView);
        srlRefresh= (SwipeRefreshLayout)view.findViewById(R.id.srlRefresh);
        adapter=new CourseAdapter(getActivity(),null);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        adapter.isFirstOnly(false);
        recyclerView.setAdapter(adapter);
        RefreshUtil.refresh(srlRefresh,getActivity());
        srlRefresh.setOnRefreshListener(this);
        onRefresh();
    }


    @Override
    public void onRefresh() {
        OkGo.post(API.BASE_URL+"/v2/city/findCourseList")
                .tag(this)
                .params("labelId",8)//绳结
                .params("number",number)
                .params("pageNumber",pageNumber)
                .cacheMode(CacheMode.IF_NONE_CACHE_REQUEST)
                .execute(new LoadCallback<CurseModel>(getActivity()) {
                    @Override
                    public void onSuccess(CurseModel curseModel, Call call, Response response) {
                    if (curseModel.state==200){adapter.setNewData(curseModel.data);}
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        showToast("网络链接错误");
                    }
                    @Override
                    public void onAfter(@Nullable CurseModel curseModel, @Nullable Exception e) {
                        super.onAfter(curseModel, e);
                        adapter.removeAllFooterView();
                        setRefreshing(false);
                    }
                });
    }
    public void setRefreshing(final boolean refreshing) {
        srlRefresh.post(new Runnable() {
            @Override
            public void run() {
                srlRefresh.setRefreshing(refreshing);
            }
        });
    }
}
