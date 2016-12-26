package com.tiyujia.homesport.common.record.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.BaseFragment;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.community.activity.CommunityDynamicPublish;
import com.tiyujia.homesport.common.homepage.activity.CityMapActivity;
import com.tiyujia.homesport.common.record.activity.RecordTopActivity;
import com.tiyujia.homesport.common.record.activity.RecordTrackActivity;
import com.tiyujia.homesport.common.record.model.OverViewModel;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.entity.LzyResponse;
import com.tiyujia.homesport.util.PickerViewUtil;

import java.util.ArrayList;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by zzqybyb19860112 on 2016/10/18.
 */
public class RecordFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private TextView tvTop,tvRecord,tvSportTimes,tvTotalScore,tvName,tvDifficulty,tvRecordEnd;
    private AlertDialog builder;
    private LinearLayout llTrack;
    private RelativeLayout rlJumpToMap;
    private Chronometer tvTimer;
    private String mToken;
    private View ivSelect;
    private RelativeLayout.LayoutParams layoutParams;
    private Integer levelid;
    private Integer sportInfoId;
    private long spendTime;
    private String[] level={"5.1","5.2","5.3","5.4","5.5","5.6","5.7","5.8","5.9","5.10a","5.10b","5.10c","5.10d","5.11a","5.11b","5.11c","5.11d","5.12a","5.12b","5.12c","5.12d",
            "5.13a","5.13b","5.13c","5.13d","5.14a","5.14b","5.14c","5.14d","5.15a","5.15b"};
    private ArrayList<String> list;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.record_fragment,null);
        return view;
    }

    @Override
    protected void initData() {
        SharedPreferences share=getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        mToken=share.getString("Token","");
        tvTop=(TextView)view.findViewById(R.id.tvTop);
        tvRecord=(TextView)view.findViewById(R.id.tvRecord);
        tvRecordEnd=(TextView)view.findViewById(R.id.tvRecordEnd);
        tvSportTimes=(TextView)view.findViewById(R.id.tvSportTimes);
        tvTotalScore=(TextView)view.findViewById(R.id.tvTotalScore);
        tvDifficulty=(TextView)view.findViewById(R.id.tvDifficulty);
        tvTimer=(Chronometer)view.findViewById(R.id.tvTimer);
        llTrack=(LinearLayout)view.findViewById(R.id.llTrack);
        rlJumpToMap= (RelativeLayout) view.findViewById(R.id.rlJumpToMap);
        ivSelect=view.findViewById(R.id.ivSelect);
        tvTop.setOnClickListener(this);
        tvRecord.setOnClickListener(this);
        llTrack.setOnClickListener(this);
        rlJumpToMap.setOnClickListener(this);
        ivSelect.setOnClickListener(this);
        tvRecordEnd.setOnClickListener(this);
        if(!TextUtils.isEmpty(mToken)){
            setData();
        }else {
            showToast("还未登录无法使用这里的功能哟");
        }

    }

    private void setData() {
        OkGo.post(API.BASE_URL+"/v2/record/overview")
                .tag(this)
                .params("token",mToken)
                .execute(new LoadCallback<LzyResponse<OverViewModel>>(getActivity()) {
                    @Override
                    public void onSuccess(LzyResponse<OverViewModel> model, Call call, Response response) {
                        if(model.state==200){
                            tvSportTimes.setText(model.data.sportTimes+"");
                            tvTotalScore.setText(model.data.totalScore+"");
                        }
                    }
                });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvTop:
                getActivity().startActivity(new Intent(getActivity(),RecordTopActivity.class));
                break;
            case R.id.tvRecord:
                String s=tvName.getText().toString();
                String b=tvDifficulty.getText().toString();
                if(!TextUtils.isEmpty(s)&&!TextUtils.isEmpty(b)){
                    tvTimer.setBase(SystemClock.elapsedRealtime());//计时器清零
                    spendTime=tvTimer.getBase();
                    int hour = (int) ((SystemClock.elapsedRealtime() -spendTime ) / 1000 / 60);
                    tvTimer.setFormat("0"+String.valueOf(hour)+":%s");
                    tvTimer.start();
                    tvRecord.setVisibility(View.GONE);
                    tvRecordEnd.setVisibility(View.VISIBLE);
                }else {
                    showToast("请选择场馆和线路难度");
                }
                break;
            //上传用户运动记录
            case R.id.tvRecordEnd:
                OkGo.post(API.BASE_URL+"/v2/record/upload")
                        .tag(this)
                        .params("token",mToken)
                        .params("venueId",levelid)
                        .params("level",tvDifficulty.getText().toString())
                        .params("spendTime",spendTime)
                        .execute(new LoadCallback<LzyResponse>(getActivity()) {
                            @Override
                            public void onSuccess(LzyResponse lzyResponse, Call call, Response response) {
                                if(lzyResponse.state==200){
                                    tvRecord.setVisibility(View.VISIBLE);
                                    tvRecordEnd.setVisibility(View.GONE);
                                    tvTimer.stop();
                                    builder = new AlertDialog.Builder(getActivity()).create();
                                    builder.setView(getActivity().getLayoutInflater().inflate(R.layout.record_succeed_dialog, null));
                                    builder.show();
                                    //去掉dialog四边的黑角
                                    builder.getWindow().setBackgroundDrawable(new BitmapDrawable());
                                    builder.getWindow().findViewById(R.id.tvLookRecord).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    });
                                    builder.getWindow().findViewById(R.id.tvShow).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            startActivity(new Intent(getActivity(),CommunityDynamicPublish.class));
                                        }
                                    });
                                }else {
                                    showToast("网络异常，请重试");
                                }
                            }
                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                super.onError(call, response, e);
                                showToast("网络连接失败");
                            }
                        });
                break;
            case R.id.llTrack:
                getActivity().startActivity(new Intent(getActivity(),RecordTrackActivity.class));
                break;
            case R.id.rlJumpToMap:
                Intent intent=new Intent(getActivity(),CityMapActivity.class);
                getActivity().startActivityForResult(intent,11);
                break;
            case R.id.ivSelect:
                String name=tvName.getText().toString();
                if(TextUtils.isEmpty(name)){
                    showToast("先选择场馆");
                }else {
                    if(levelid!=null){
                        list = new ArrayList<String>();
                        for(int i=0;i<level.length;i++){
                            list.add(level[i]);
                        }
                        PickerViewUtil.alertBottomWheelOption(getActivity(), list, new PickerViewUtil.OnWheelViewClick() {
                            @Override
                            public void onClick(View view, int postion) {
                                tvDifficulty.setText(list.get(postion));
                            }
                        });

                        /*  OkGo.post(API.BASE_URL+"/v2/record/sportinfo/level")
                                .tag(this)
                                .params("venueId",levelid)
                                .execute(new LoadCallback<LevelModel>(getActivity()) {
                                    @Override
                                    public void onSuccess(LevelModel levelModel, Call call, Response response) {
                                        if (levelModel.state==200){

                                            final ArrayList<Integer> listid = new ArrayList<>();
                                            if(levelModel.data.size()>0){
                                                for (int i=0;i<levelModel.data.size();i++){
                                                    LevelModel.Level jk = levelModel.data.get(i);
                                                    list.add(jk.level);
                                                    listid.add(jk.id);
                                                }
                                            }else {}
                                            if(list.size()>0){

                                            }else {}
                                        }
                                    }
                                });*/
                    }else {}
                }
                break;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        tvName=(TextView)view.findViewById(R.id.tvName);
        if(TextUtils.isEmpty(tvName.getText())){
            tvName.setText("0");
        }else {
        SharedPreferences city=getActivity().getSharedPreferences("City", Context.MODE_PRIVATE);
        String name=city.getString("name","");
        levelid=city.getInt("id",0);
        if(!TextUtils.isEmpty(name)){
            tvName.setText(name);
        }else {}
    }}
}
