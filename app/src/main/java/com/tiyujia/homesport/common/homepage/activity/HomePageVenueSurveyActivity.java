package com.tiyujia.homesport.common.homepage.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tiyujia.homesport.NewBaseActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.adapter.HomePageSearchRecordAdapter;
import com.tiyujia.homesport.common.homepage.dao.DBVenueContext;
import com.tiyujia.homesport.common.homepage.entity.HomePageSearchEntity;
import com.tiyujia.homesport.common.homepage.fragment.AllVenueFragment;
import com.tiyujia.homesport.common.homepage.fragment.HardVenueFragment;
import com.tiyujia.homesport.common.homepage.fragment.HotVenueFragment;
import com.tiyujia.homesport.common.homepage.fragment.NearVenueFragment;
import com.tiyujia.homesport.widget.TablayoutVPAdapter;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
//1
public class HomePageVenueSurveyActivity extends NewBaseActivity implements View.OnClickListener{
    @Bind(R.id.tab)    TabLayout tab;
    @Bind(R.id.vp)     ViewPager vp;
    private List<String> mTitle=new ArrayList<String>();
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private List<HomePageSearchEntity> list;
    @Bind(R.id.ivVenueSurveySearch) ImageView ivVenueSurveySearch;
    @Bind(R.id.ivVenueSurveyBack)   ImageView ivVenueSurveyBack;
    @Bind(R.id.tvVenueSurveyClose)  TextView tvVenueSurveyClose;
    @Bind(R.id.tvClearAll)          TextView tvClearAll;
    @Bind(R.id.tvSearchTitle)       TextView tvSearchTitle;
    @Bind(R.id.recyclerView)        RecyclerView recyclerView;
    @Bind(R.id.rlStartSearch)       RelativeLayout rlStartSearch;
    @Bind(R.id.llEndSearch)         LinearLayout llEndSearch;
    @Bind(R.id.tabResult)           LinearLayout tabResult;//搜索结果布局
    @Bind(R.id.llSearchResult)      LinearLayout llSearchResult;//搜索记录布局
    public static EditText etVenueSearch;
    DBVenueContext dbVenueContext;
    AllVenueFragment allVenueFragment;
    NearVenueFragment nearVenueFragment;
    HotVenueFragment hotVenueFragment;
    HardVenueFragment hardVenueFragment;
    HomePageSearchRecordAdapter recordAdapter;
    TablayoutVPAdapter tabAdapter;
    public static final int HANDLE_RECORD_DATA=1;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HANDLE_RECORD_DATA:
                    recordAdapter=new HomePageSearchRecordAdapter(HomePageVenueSurveyActivity.this,list);
                    if (list.size()==0){
                        llSearchResult.setVisibility(View.GONE);
                        tvClearAll.setVisibility(View.GONE);
                        tabResult.setVisibility(View.VISIBLE);
                    }
                    recyclerView.setLayoutManager(new LinearLayoutManager(HomePageVenueSurveyActivity.this));
                    recyclerView.setAdapter(recordAdapter);
                    recordAdapter.setOnItemClickListener(new HomePageSearchRecordAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(String searchText) {
                            if (allVenueFragment!=null){
                                Log.i("tag",searchText+"-----------------");
                                allVenueFragment.adapter.getFilter().filter(searchText);
                                allVenueFragment.adapter.notifyDataSetChanged();
                                tabAdapter.notifyDataSetChanged();
                                rlStartSearch.setVisibility(View.VISIBLE);
                                tabResult.setVisibility(View.VISIBLE);
                                llSearchResult.setVisibility(View.GONE);
                                llEndSearch.setVisibility(View.GONE);
                            }
                        }
                    });
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_venue_survey);
        etVenueSearch= (EditText) findViewById(R.id.etVenueSearch);
        setView();
        tabAdapter=new TablayoutVPAdapter(getSupportFragmentManager(),mFragments,mTitle);
        vp.setAdapter(tabAdapter);
        vp.setOffscreenPageLimit(3);
        vp.setCurrentItem(0);
        //tablayout和viewpager关联
        tab.setupWithViewPager(vp);
        tab.setTabsFromPagerAdapter(tabAdapter);
        tab.setTabGravity(TabLayout.GRAVITY_FILL);
        tab.setTabMode(TabLayout.MODE_FIXED);
        setListeners();
    }
    private void setListeners() {
        tvVenueSurveyClose.setOnClickListener(this);
        ivVenueSurveySearch.setOnClickListener(this);
        ivVenueSurveyBack.setOnClickListener(this);
        etVenueSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("tag",allVenueFragment.toString());//有
                Log.i("tag",allVenueFragment.adapter.toString());//有
                Log.i("tag",hotVenueFragment.toString());//有
                Log.i("tag",hotVenueFragment.adapter.toString());//空
                allVenueFragment.adapter.getFilter().filter(s);
//                hotVenueFragment.adapter.getFilter().filter(s);
//                hardVenueFragment.adapter.getFilter().filter(s);
//                nearVenueFragment.adapter.getFilter().filter(s);
                tabAdapter.notifyDataSetChanged();
                if (!s.toString().trim().equals("")){
                    llSearchResult.setVisibility(View.GONE);
                    tabResult.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        tvClearAll.setOnClickListener(this);
    }
    private void setView() {
        mTitle.add("全部");
        mTitle.add("离我最近");
        mTitle.add("最热门");
        mTitle.add("难度最大");
        allVenueFragment=new AllVenueFragment();
        nearVenueFragment=new NearVenueFragment();
        hotVenueFragment=new HotVenueFragment();
        hardVenueFragment=new HardVenueFragment();
        mFragments.add(allVenueFragment);
        mFragments.add(nearVenueFragment);
        mFragments.add(hotVenueFragment);
        mFragments.add(hardVenueFragment);
        dbVenueContext=new DBVenueContext(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivVenueSurveySearch:
                list=dbVenueContext.query();
                handler.sendEmptyMessage(HANDLE_RECORD_DATA);
                llEndSearch.setVisibility(View.VISIBLE);
                llSearchResult.setVisibility(View.VISIBLE);
                tabResult.setVisibility(View.GONE);
                rlStartSearch.setVisibility(View.GONE);
                break;
            case R.id.tvVenueSurveyClose:
                rlStartSearch.setVisibility(View.VISIBLE);
                tabResult.setVisibility(View.VISIBLE);
                llSearchResult.setVisibility(View.GONE);
                llEndSearch.setVisibility(View.GONE);
                etVenueSearch.setText("");
                break;
            case R.id.tvClearAll:
                dbVenueContext.deleteAllData();
                tvClearAll.setVisibility(View.GONE);
                tvSearchTitle.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                break;
            case R.id.ivVenueSurveyBack:
                finish();
                break;
        }
    }
    public static String getSearchText(){
        if (etVenueSearch==null){
            return "";
        }else {
            return etVenueSearch.getText().toString().trim().equals("")?"":etVenueSearch.getText().toString().trim();
        }
    }
}
