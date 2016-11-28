package com.tiyujia.homesport.common.homepage.activity;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tiyujia.homesport.NewBaseActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.adapter.WholeSearchRecordAdapter;
import com.tiyujia.homesport.common.homepage.dao.DBWholeContext;
import com.tiyujia.homesport.common.homepage.entity.WholeBean;
import com.tiyujia.homesport.common.homepage.fragment.WholeSearchFragment;
import com.tiyujia.homesport.common.personal.fragment.AttendFragment;
import com.tiyujia.homesport.widget.TablayoutVPAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import butterknife.Bind;
//1
public class HomePageWholeSearchActivity extends NewBaseActivity implements View.OnClickListener{
    @Bind(R.id.tab)                     TabLayout tab;
    @Bind(R.id.vp)                      ViewPager vp;
    @Bind(R.id.tvWholeSearchClose)      TextView tvWholeSearchClose;
    @Bind(R.id.tvClearWholeRecord)      TextView tvClearWholeRecord;
    @Bind(R.id.tvWholeSearchTitle)      TextView tvWholeSearchTitle;
    @Bind(R.id.etWholeSearch)           EditText etWholeSearch;
    @Bind(R.id.rvWholeSearchResult)     RecyclerView rvWholeSearchResult;
    @Bind(R.id.llWholeSearchResult)     LinearLayout llWholeSearchResult;//显示搜索记录的布局
    @Bind(R.id.tabResult)               LinearLayout tabResult;//显示搜索结果的布局
    private List<String> mTitle=new ArrayList<String>();
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private List<WholeBean> list;
    DBWholeContext wholeContext;
    WholeSearchRecordAdapter wholeAdapter;
    WholeSearchFragment wholeFragment;
    TablayoutVPAdapter tabAdapter;
    public static final int HANDLE_WHOLE_RECORD_DATA=1;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HANDLE_WHOLE_RECORD_DATA:
                    wholeAdapter=new WholeSearchRecordAdapter(HomePageWholeSearchActivity.this,list);
                    if (list.size()!=0){
                        tabResult.setVisibility(View.GONE);
                        llWholeSearchResult.setVisibility(View.VISIBLE);
                        rvWholeSearchResult.setLayoutManager(new LinearLayoutManager(HomePageWholeSearchActivity.this));
                        rvWholeSearchResult.setAdapter(wholeAdapter);
                        wholeAdapter.setOnItemClickListener(new WholeSearchRecordAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(String searchText) {
                                if (wholeFragment!=null){
                                    // wholeFragment.adapter.getFilter().filter(searchText);
                                    tabAdapter.notifyDataSetChanged();
                                    llWholeSearchResult.setVisibility(View.GONE);
                                    tabResult.setVisibility(View.VISIBLE);
                                    ContentValues value = new ContentValues();
                                    value.put("content", "你是猪吗？--->"+new Random().nextBoolean());
                                    wholeContext.insert(value);
                                }
                            }
                        });
                    }else {
                        tabResult.setVisibility(View.VISIBLE);
                        llWholeSearchResult.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_whole_search);
        setView();
        tabAdapter=new TablayoutVPAdapter(getSupportFragmentManager(),mFragments,mTitle);
        vp.setAdapter(tabAdapter);
        //tablayout和viewpager关联
        tab.setupWithViewPager(vp);
        vp.setOffscreenPageLimit(6);
        tab.setTabsFromPagerAdapter(tabAdapter);
        tab.setTabGravity(TabLayout.GRAVITY_FILL);
        tab.setTabMode(TabLayout.MODE_FIXED);
        setListeners();
    }
    private void setView() {
        mTitle.add("全部");
        mTitle.add("活动");
        mTitle.add("装备");
        mTitle.add("动态");
        mTitle.add("教程");
        mTitle.add("场馆");
        wholeFragment=new WholeSearchFragment();
        mFragments.add(wholeFragment);
        mFragments.add(new AttendFragment());
        mFragments.add(new AttendFragment());
        mFragments.add(new AttendFragment());
        mFragments.add(new AttendFragment());
        mFragments.add(new AttendFragment());
        wholeContext=new DBWholeContext(this);
        list=wholeContext.query();
        handler.sendEmptyMessage(HANDLE_WHOLE_RECORD_DATA);
    }
    private void setListeners() {
        tvClearWholeRecord.setOnClickListener(this);
        tvWholeSearchClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvClearWholeRecord:
                wholeContext.deleteAllData();
                tvClearWholeRecord.setVisibility(View.GONE);
                tvWholeSearchTitle.setVisibility(View.GONE);
                rvWholeSearchResult.setVisibility(View.GONE);
                tabResult.setVisibility(View.VISIBLE);
                break;
            case R.id.tvWholeSearchClose:
                finish();
                break;
        }
    }
}
