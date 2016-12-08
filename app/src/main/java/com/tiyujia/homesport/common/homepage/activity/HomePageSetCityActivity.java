package com.tiyujia.homesport.common.homepage.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.App;
import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.adapter.HomePageCityAdapter;
import com.tiyujia.homesport.common.homepage.adapter.HomePageGVHotCityAdapter;
import com.tiyujia.homesport.common.homepage.customview.QuicLocationBar;
import com.tiyujia.homesport.common.homepage.dao.CityDBManager;
import com.tiyujia.homesport.common.homepage.entity.CityBean;
import com.tiyujia.homesport.common.homepage.entity.CityModel;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.util.CityUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import okhttp3.Call;
import okhttp3.Response;

//1
public class HomePageSetCityActivity extends ImmersiveActivity {
    GridView gvHotCity;
    List<String> testCities;
    HomePageGVHotCityAdapter hotCityAdapter;
    public static final int HANDLE_HOT_CITY=1;
    private ListView mCityLit;
    private TextView overlay;
    private TextView tvNowCity;
    private EditText etSearchCity;
    private QuicLocationBar mQuicLocationBar;
    private HashMap<String, Integer> alphaIndexer;
    private ArrayList<CityBean> mCityNames;
    HomePageCityAdapter cityAdapter;
    private AMapLocationClientOption locationOption = new AMapLocationClientOption();
    private AMapLocationClient locationClient = null;
    /**
     * a-z排序
     */
    @SuppressWarnings("rawtypes")
    Comparator comparator = new Comparator<CityBean>() {
        @Override
        public int compare(CityBean lhs, CityBean rhs) {
            String a = lhs.getNameSort().substring(0, 1);
            String b = rhs.getNameSort().substring(0, 1);
            int flag = a.compareTo(b);
            if (flag == 0) {
                return a.compareTo(b);
            } else {
                return flag;
            }
        }
    };
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HANDLE_HOT_CITY:
                    hotCityAdapter=new HomePageGVHotCityAdapter(HomePageSetCityActivity.this,testCities);
                    gvHotCity.setAdapter(hotCityAdapter);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_set_city);
        gvHotCity= (GridView) findViewById(R.id.gvHotCity);
        setBaseData();
        initLocation();
        mQuicLocationBar=(QuicLocationBar)findViewById(R.id.city_loactionbar);
        mQuicLocationBar.setOnTouchLitterChangedListener(new LetterListViewListener());
        overlay=(TextView)findViewById(R.id.city_dialog);
        tvNowCity=(TextView)findViewById(R.id.tvNowCity);
        etSearchCity= (EditText) findViewById(R.id.etSearchCity);
        mCityLit=(ListView) findViewById(R.id.city_list);
        mQuicLocationBar.setTextDialog(overlay);
       // String nowCity=App.nowCity;
       /* if (TextUtils.isEmpty(nowCity)){
            tvNowCity.setText("定位中");
            tvNowCity.postInvalidate();
        }else {
            tvNowCity.setText(nowCity);
            tvNowCity.postInvalidate();
        }*/
        initList();
        setListeners();
    }
    private AMapLocationClientOption getDefaultOption(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(true);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        return mOption;
    }
    private void setListeners() {
        tvNowCity.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           /* Intent intent=new Intent();
            Toast.makeText(HomePageSetCityActivity.this,"您已选择城市："+tvNowCity.getText(),Toast.LENGTH_SHORT).show();
            intent.putExtra("SelectCity",tvNowCity.getText());
            setResult(10002,intent);
            finish();*/
            // 启动定位
            locationClient.startLocation();

        }
    });
        etSearchCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().equals("")) {
                    mCityNames = getCityNamesBySearch(s.toString().trim());
                    cityAdapter=new HomePageCityAdapter(HomePageSetCityActivity.this,mCityNames);
                    mCityLit.setAdapter(cityAdapter);
                    cityAdapter.notifyDataSetChanged();
                }else {
                   initList();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        gvHotCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cityHot=testCities.get(position);
                Intent intent=new Intent();
                Toast.makeText(HomePageSetCityActivity.this,"您已选择城市："+cityHot,Toast.LENGTH_SHORT).show();
                intent.putExtra("SelectCity",cityHot);
                setResult(10002,intent);
                finish();
            }
        });
    }
    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation loc) {
            if (null != loc) {
                //解析定位结果
                String result = CityUtils.getLocationStr(loc);
                showToast(result);
            } else {
                showToast("定位失败，loc is null");
            }
        }
    };
    /**
     * 初始化定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void initLocation(){
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }
    private void initList() {
        mCityNames=getCityNames();
        cityAdapter=new HomePageCityAdapter(this,mCityNames);
        mCityLit.setAdapter(cityAdapter);
        alphaIndexer=cityAdapter.getCityMap();
        mCityLit.setOnItemClickListener(new CityListOnItemClick());
    }
    private ArrayList<CityBean> getCityNames() {
        CityDBManager dbManager=new CityDBManager(this);
        dbManager.openDateBase();
        dbManager.closeDatabase();
        SQLiteDatabase database=SQLiteDatabase.openOrCreateDatabase(CityDBManager.DB_PATH+"/"+CityDBManager.DB_NAME,null);
        ArrayList<CityBean> names=new ArrayList<CityBean>();
        Cursor cursor=database.rawQuery("select * from city",null);
        if(cursor.moveToFirst()){
            do{
                CityBean cityModel=new CityBean();
                cityModel.setCityName(cursor.getString(1));
                cityModel.setNameSort(cursor.getString(2).substring(0,1).toUpperCase());
                names.add(cityModel);
            }while(cursor.moveToNext());
        }
        database.close();
        Collections.sort(names, comparator);
        return names;
    }
    private ArrayList<CityBean> getCityNamesBySearch(String tempStr) {
        CityDBManager dbManager=new CityDBManager(this);
        dbManager.openDateBase();
        dbManager.closeDatabase();
        SQLiteDatabase database=SQLiteDatabase.openOrCreateDatabase(CityDBManager.DB_PATH+"/"+CityDBManager.DB_NAME,null);
        ArrayList<CityBean> names=new ArrayList<CityBean>();
        Cursor cursor=database.rawQuery("select * from city where name like '%"+tempStr+"%'",null);
        if(cursor.moveToFirst()){
            do{
                CityBean cityModel=new CityBean();
                cityModel.setCityName(cursor.getString(1));
                cityModel.setNameSort(cursor.getString(2).substring(0,1).toUpperCase());
                names.add(cityModel);
            }while(cursor.moveToNext());
        }
        database.close();
        Collections.sort(names, comparator);
        return names;
    }
    private class CityListOnItemClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3){
            CityBean cityModel=(CityBean)mCityLit.getAdapter().getItem(pos);
            Toast.makeText(HomePageSetCityActivity.this,"您已选择城市："+cityModel.getCityName(),Toast.LENGTH_SHORT).show();
            Intent intent=new Intent();
            intent.putExtra("SelectCity",cityModel.getCityName());
            setResult(10002,intent);
            finish();
        }
    }
    private class LetterListViewListener implements QuicLocationBar.OnTouchLetterChangedListener {
        @Override
        public void touchLetterChanged(String s) {
            if (alphaIndexer.get(s) != null) {
                int position=alphaIndexer.get(s);
                mCityLit.setSelection(position);
            }
        }
    }
    private void setBaseData() {
        testCities=new ArrayList<>();
        OkGo.post(API.BASE_URL+"/v2/city/findHotCity")
                .tag(this)
                .execute(new LoadCallback<CityModel>(this) {
                    @Override
                    public void onSuccess(CityModel cityModel, Call call, Response response) {
                        for (CityModel.CityList c:cityModel.data){
                            testCities.add(c.cityName);
                        }
                        handler.sendEmptyMessage(HANDLE_HOT_CITY);
                    }
                });
    }
}
