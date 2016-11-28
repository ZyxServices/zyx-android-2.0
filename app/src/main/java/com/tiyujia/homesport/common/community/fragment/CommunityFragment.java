package com.tiyujia.homesport.common.community.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tiyujia.homesport.BaseFragment;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.community.activity.CommunityAddAttention;
import com.tiyujia.homesport.common.community.activity.CommunityDynamicPublish;
import com.tiyujia.homesport.widget.TablayoutVPAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


/**1
 * Created by zzqybyb19860112 on 2016/10/18.
 */
public class CommunityFragment extends BaseFragment{
    private ImageView iv_add;
    private TabLayout tab;
    private ViewPager vp;
    private List<String> mTitle=new ArrayList<String>();
    private List<Fragment> mFragment = new ArrayList<Fragment>();
    private ImageView ivPush;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View  view=inflater.inflate(R.layout.community_home_fragment,null);
        ButterKnife.bind(getActivity());
        iv_add=(ImageView) view.findViewById(R.id.ivAdd);
        vp=(ViewPager) view.findViewById(R.id.vp);
        tab=(TabLayout) view.findViewById(R.id.tab);
        ivPush=(ImageView) view.findViewById(R.id.ivPush);
        initview();
        //创建悬浮窗口

        TablayoutVPAdapter adapter=new TablayoutVPAdapter(getChildFragmentManager(),mFragment,mTitle);
        vp.setAdapter(adapter);
        //tablayout和viewpager关联
        tab.setupWithViewPager(vp);
        //为tablayout设置适配器
        tab.setTabGravity(TabLayout.GRAVITY_CENTER);
        tab.setTabMode(TabLayout.MODE_FIXED);
        vp.setOffscreenPageLimit(2);
        return view;
    }
    private void initview() {
        mTitle.add("推荐");
        mTitle.add("关注");
        mFragment.add(new RecommendFragment());
        mFragment.add(new AttentionFragment());
    }
    @Override
    protected void initData() {
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),CommunityAddAttention.class));
            }
        });
        ivPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),CommunityDynamicPublish.class));
            }
        });
    }
}
