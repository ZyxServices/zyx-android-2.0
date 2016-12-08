package com.tiyujia.homesport.common.personal.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.personal.fragment.PersonalMsgFragment;
import com.tiyujia.homesport.widget.TablayoutVPAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 作者: Cymbi on 2016/11/15 18:15.
 * 邮箱:928902646@qq.com
 */

public class PersonalMsg extends ImmersiveActivity {
    @Bind(R.id.personal_back)  ImageView personal_back;
    @Bind(R.id.tab)   TabLayout tab;
    @Bind(R.id.vp)   ViewPager vp;
    @Bind(R.id.tv_title)   TextView tv_title;
    private List<String> mTitle=new ArrayList<String>();
    private List<Fragment> mFragment = new ArrayList<Fragment>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_msg);
        setview();
        TablayoutVPAdapter adapter=new TablayoutVPAdapter(getSupportFragmentManager(),mFragment,mTitle);
        vp.setAdapter(adapter);
        //tablayout和viewpager关联
        tab.setupWithViewPager(vp);
        vp.setOffscreenPageLimit(4);
        tab.setTabsFromPagerAdapter(adapter);
        tab.setTabGravity(TabLayout.GRAVITY_FILL);
        tab.setTabMode(TabLayout.MODE_FIXED);
    }
    private void setview() {
        personal_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_title.setText("我的消息");
        mTitle.add("回复");
        mTitle.add("报名");
        mTitle.add("点赞");
        mTitle.add("系统消息");
        mFragment.add(new PersonalMsgFragment());
        mFragment.add(new PersonalMsgFragment());
        mFragment.add(new PersonalMsgFragment());
        mFragment.add(new PersonalMsgFragment());
    }
}
