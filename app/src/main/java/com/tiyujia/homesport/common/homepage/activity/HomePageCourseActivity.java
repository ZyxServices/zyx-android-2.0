package com.tiyujia.homesport.common.homepage.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.fragment.CourseActionFragment;
import com.tiyujia.homesport.common.homepage.fragment.CourseAllFragment;
import com.tiyujia.homesport.common.homepage.fragment.CourseCordFragment;
import com.tiyujia.homesport.common.homepage.fragment.CourseNoviceFragment;
import com.tiyujia.homesport.common.homepage.fragment.CourseStoneFragment;
import com.tiyujia.homesport.widget.TablayoutVPAdapter;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者: Cymbi on 2016/11/17 18:17.
 * 邮箱:928902646@qq.com11111112
 */

public class HomePageCourseActivity extends ImmersiveActivity {
    @Bind(R.id.ivBack)      ImageView ivBack;
    @Bind(R.id.tab)         TabLayout tab;
    @Bind(R.id.vp)          ViewPager vp;
    private List<String> mTitle=new ArrayList<String>();
    private List<Fragment> mFragment = new ArrayList<Fragment>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_course);
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
    }

    private void setview() {
        mTitle.add("全部");
        mTitle.add("抱石");
        mTitle.add("绳结");
        mTitle.add("动作");
        mTitle.add("新手");
        mFragment.add(new CourseAllFragment());
        mFragment.add(new CourseStoneFragment());
        mFragment.add(new CourseCordFragment());
        mFragment.add(new CourseActionFragment());
        mFragment.add(new CourseNoviceFragment());
    }
}
