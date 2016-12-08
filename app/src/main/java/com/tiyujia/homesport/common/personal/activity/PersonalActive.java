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
import com.tiyujia.homesport.widget.TablayoutVPAdapter;
import com.tiyujia.homesport.common.personal.fragment.ActiveFragment;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 作者: Cymbi on 2016/11/10 17:59.
 * 邮箱:928902646@qq.com
 */

public class PersonalActive extends ImmersiveActivity  {
    @Bind(R.id.personal_back)    ImageView personal_back;
    @Bind(R.id.tab)              TabLayout tab;
    @Bind(R.id.vp)               ViewPager vp;
    @Bind(R.id.tv_title)         TextView tv_title;
    private List<String> mTitle=new ArrayList<String>();
    private List<Fragment> mFragment = new ArrayList<Fragment>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_active);
        ButterKnife.bind(this);
        setview();
        TablayoutVPAdapter adapter=new TablayoutVPAdapter(getSupportFragmentManager(),mFragment,mTitle);
        vp.setAdapter(adapter);
        //tablayout和viewpager关联
        tab.setupWithViewPager(vp);
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
        tv_title.setText("我的活动");
        mTitle.add("我参加的");
        mTitle.add("我发起的");
        mFragment.add(new ActiveFragment());
        mFragment.add(new ActiveFragment());
    }


}
