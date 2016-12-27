package com.tiyujia.homesport.common.community.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tiyujia.homesport.HomeActivity;
import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.community.adapter.CityAddressAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 作者: Cymbi on 2016/12/20 20:28.
 * 邮箱:928902646@qq.com
 */

public class CityAddressSelect extends ImmersiveActivity implements PoiSearch.OnPoiSearchListener {
    @Bind(R.id.tvSetCityCancel)
    TextView tvSetCityCancel;
    @Bind(R.id.tvNoAddress)
    TextView tvNoAddress;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.etSearchCity)
    EditText etSearchCity;
    // 声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    // 定位参数配置
    private AMapLocationClientOption mLocationOption;
    //纬度
    private double Latitude;
    //经度
    private double Longitude;
    //查询实例
    private PoiSearch.Query query;
    private CityAddressAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_address_select);
        initLocation();
        int gone = getIntent().getIntExtra("gone", 0);
        if(gone==1){
            tvNoAddress.setVisibility(View.GONE);
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new CityAddressAdapter(null);
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        adapter.isFirstOnly(false);
        recyclerView.setAdapter(adapter);
        search();
        etSearchCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tvSetCityCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearchCity.setText("");
            }
        });
        tvNoAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("cityTitle","");
                setResult(111,intent);
                finish();
            }
        });

    }
    private void initLocation() {
        AMapLocationClient client = HomeActivity.client;
        AMapLocation linser = client.getLastKnownLocation();
        //定位成功回调信息，设置相关消息
        Latitude = linser.getLatitude();//获取纬度
        Longitude = linser.getLongitude();//获取经度
    }
    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        if(i==1000&&poiResult!=null){
            ArrayList<PoiItem> pois = poiResult.getPois();
            adapter.setNewData(pois);
            adapter.setOnClickResultListener(new CityAddressAdapter.OnClickResultListener() {
                @Override
                public void onClickResult(String result) {
                    Intent intent=new Intent();
                    intent.putExtra("cityTitle",result);
                    setResult(111,intent);
                    finish();
                }
            });
        }
    }
    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {
    }
    private void search() {
        String content= etSearchCity.getText().toString().trim();
        if(content==null){
            Toast.makeText(CityAddressSelect.this, "输入为空", Toast.LENGTH_SHORT).show();
        }else{
            query = new PoiSearch.Query(content, "地名地址信息");
            // keyWord表示搜索字符串，第二个参数表示POI搜索类型，默认为：生活服务、餐饮服务、商务住宅
            // 共分为以下20种：汽车服务|汽车销售|
            // 汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|
            // 住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|
            // 金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施
            // cityCode表示POI搜索区域，（这里可以传空字符串，空字符串代表全国在全国范围内进行搜索）
            query.setPageSize(40);// 设置每页最多返回多少条poiitem
            query.setPageNum(1);// 设置查第一页
            PoiSearch poiSearch = new PoiSearch(this, query);
            //如果不为空值
            if(Latitude!=0.0&&Longitude!=0.0){
                poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(Latitude,
                        Longitude), 6000));// 设置周边搜索的中心点以及区域
                poiSearch.setOnPoiSearchListener(this);// 设置数据返回的监听器
                poiSearch.searchPOIAsyn();// 开始搜索
            }else{
                Toast.makeText(CityAddressSelect.this, "定位失败", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
