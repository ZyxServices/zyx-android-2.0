package com.tiyujia.homesport.common.record.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;
import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.BaseFragment;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.activity.CityMapActivity;
import com.tiyujia.homesport.common.record.activity.RecordTopActivity;
import com.tiyujia.homesport.common.record.activity.RecordTrackActivity;
import com.tiyujia.homesport.common.record.model.LevelModel;
import com.tiyujia.homesport.common.record.model.OverViewModel;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.entity.LzyResponse;
import com.tiyujia.homesport.entity.view.LoopView;
import com.tiyujia.homesport.entity.view.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by zzqybyb19860112 on 2016/10/18.
 */
public class RecordFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private TextView tvTop,tvRecord,tvSportTimes,tvTotalScore,tvName;
    private AlertDialog builder;
    private LinearLayout llTrack;
    private RelativeLayout rlJumpToMap;
    private String mToken;
    private View ivSelect;
    private RelativeLayout.LayoutParams layoutParams;
    private Integer levelid;

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
        tvSportTimes=(TextView)view.findViewById(R.id.tvSportTimes);
        tvName=(TextView)view.findViewById(R.id.tvName);
        tvTotalScore=(TextView)view.findViewById(R.id.tvTotalScore);
        llTrack=(LinearLayout)view.findViewById(R.id.llTrack);
        rlJumpToMap= (RelativeLayout) view.findViewById(R.id.rlJumpToMap);
        ivSelect=view.findViewById(R.id.ivSelect);
        tvTop.setOnClickListener(this);
        tvRecord.setOnClickListener(this);
        llTrack.setOnClickListener(this);
        rlJumpToMap.setOnClickListener(this);
        setData();
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
                    }
                });
                break;
            case R.id.llTrack:
                getActivity().startActivity(new Intent(getActivity(),RecordTrackActivity.class));
                break;
            case R.id.rlJumpToMap:
                Intent intent=new Intent(getActivity(),CityMapActivity.class);
                startActivity(intent);
                break;
            case R.id.ivSelect:
                layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                final RelativeLayout rootview = (RelativeLayout) view.findViewById(R.id.rootview);
                final LoopView loopView = new LoopView(getActivity());
                if(levelid!=null){
                    OkGo.post(API.BASE_URL+"/v2/record/sportinfo/level")
                            .tag(this)
                            .params("venueId",levelid)
                            .execute(new LoadCallback<LevelModel>(getActivity()) {
                                @Override
                                public void onSuccess(LevelModel levelModel, Call call, Response response) {
                                    if (levelModel.state==200){
                                        ArrayList<String> list = new ArrayList<>();
                                        for (int i=0;i<levelModel.data.size();i++){
                                            LevelModel.Level jk = levelModel.data.get(i);
                                            list.add(jk.level);
                                        }
                                        //滚动监听
                                        loopView.setListener(new OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(int index) {

                                            }
                                        });
                                        //设置原始数据
                                        loopView.setItems(list);
                                        //设置初始位置
                                        loopView.setInitPosition(5);
                                        //设置字体大小
                                        loopView.setTextSize(10);
                                        rootview.addView(loopView, layoutParams);
                                    }

                                }
                            });
                }else {}


                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences city=getActivity().getSharedPreferences("City", Context.MODE_PRIVATE);
        String name=city.getString("name","");
        levelid=city.getInt("id",0);
        if(!TextUtils.isEmpty(name)){
            tvName.setText(name);
        }else {}


    }
}
