package com.tiyujia.homesport.common.record.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.BaseFragment;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.activity.CityMapActivity;
import com.tiyujia.homesport.common.record.activity.RecordTopActivity;
import com.tiyujia.homesport.common.record.activity.RecordTrackActivity;
import com.tiyujia.homesport.common.record.model.OverViewModel;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.entity.LzyResponse;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by zzqybyb19860112 on 2016/10/18.
 */
public class RecordFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private TextView tvTop,tvRecord,tvSportTimes,tvTotalScore;
    private AlertDialog builder;
    private LinearLayout llTrack;
    private RelativeLayout rlJumpToMap;
    private String mToken;
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
        tvTotalScore=(TextView)view.findViewById(R.id.tvTotalScore);
        llTrack=(LinearLayout)view.findViewById(R.id.llTrack);
        rlJumpToMap= (RelativeLayout) view.findViewById(R.id.rlJumpToMap);
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
                getActivity().startActivityForResult(intent,2345);
                break;
        }
    }
}
