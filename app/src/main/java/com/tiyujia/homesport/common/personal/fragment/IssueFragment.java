package com.tiyujia.homesport.common.personal.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tiyujia.homesport.R;
import com.tiyujia.homesport.util.RefreshUtil;

/**
 * 作者: Cymbi on 2016/10/20 16:52.
 * 邮箱:928902646@qq.com
 */

public class IssueFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private View view;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout srlRefresh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.recycleview_layout,null);
        setview();
        return view;
    }

    private void setview() {
        recyclerView= (RecyclerView)view.findViewById(R.id.recyclerView);
        srlRefresh= (SwipeRefreshLayout)view.findViewById(R.id.srlRefresh);
        RefreshUtil.refresh(srlRefresh,getActivity());
        srlRefresh.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 停止刷新
                srlRefresh.setRefreshing(false);
            }
        }, 1000);
    }
}
