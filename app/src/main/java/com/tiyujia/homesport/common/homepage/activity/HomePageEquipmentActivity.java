package com.tiyujia.homesport.common.homepage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.fragment.EquipmentAllFragment;
import com.tiyujia.homesport.common.homepage.fragment.EquipmentHelmetFragment;
import com.tiyujia.homesport.common.homepage.fragment.EquipmentLockFragment;
import com.tiyujia.homesport.common.homepage.fragment.EquipmentRopeFragment;
import com.tiyujia.homesport.common.homepage.fragment.EquipmentShoesFragment;
import com.tiyujia.homesport.widget.TablayoutVPAdapter;

import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者: Cymbi on 2016/11/17 17:42.1
 * 邮箱:928902646@qq.com
 */

public class HomePageEquipmentActivity extends ImmersiveActivity {
    @Bind(R.id.ivBack)   ImageView ivBack;
    @Bind(R.id.ivPush)   ImageView ivPush;
    @Bind(R.id.tab)    TabLayout tab;
    @Bind(R.id.vp)    ViewPager vp;
    private List<String> mTitle=new ArrayList<String>();
    private List<Fragment> mFragment = new ArrayList<Fragment>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_equipment);
        ButterKnife.bind(this);
        setview();
        TablayoutVPAdapter adapter=new TablayoutVPAdapter(getSupportFragmentManager(),mFragment,mTitle);
        vp.setAdapter(adapter);
        //tablayout和viewpager关联
        tab.setupWithViewPager(vp);
        vp.setOffscreenPageLimit(5);
        tab.setTabsFromPagerAdapter(adapter);
        tab.setTabGravity(TabLayout.GRAVITY_FILL);
        tab.setTabMode(TabLayout.MODE_FIXED);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(HomePageEquipmentActivity.this,HomePageEquipmentShowActivity.class));
            }
        });
    }

    private void setview() {
        mTitle.add("全部");
        mTitle.add("鞋子");
        mTitle.add("动力绳");
        mTitle.add("头盔");
        mTitle.add("主锁");
        mFragment.add(new EquipmentAllFragment());
        mFragment.add(new EquipmentShoesFragment());
        mFragment.add(new EquipmentRopeFragment());
        mFragment.add(new EquipmentHelmetFragment());
        mFragment.add(new EquipmentLockFragment());
    }
}
