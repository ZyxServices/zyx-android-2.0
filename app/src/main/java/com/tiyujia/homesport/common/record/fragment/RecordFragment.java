package com.tiyujia.homesport.common.record.fragment;


import android.app.AlertDialog;
import android.content.Intent;
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

import com.tiyujia.homesport.BaseFragment;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.activity.CityMapActivity;
import com.tiyujia.homesport.common.record.activity.RecordTopActivity;
import com.tiyujia.homesport.common.record.activity.RecordTrackActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zzqybyb19860112 on 2016/10/18.
 */
public class RecordFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private TextView tvTop,tvRecord;
    private AlertDialog builder;
    private LinearLayout llTrack;
    private RelativeLayout rlJumpToMap;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.record_fragment,null);
        return view;
    }

    @Override
    protected void initData() {
        tvTop=(TextView)view.findViewById(R.id.tvTop);
        tvRecord=(TextView)view.findViewById(R.id.tvRecord);
        llTrack=(LinearLayout)view.findViewById(R.id.llTrack);
        rlJumpToMap= (RelativeLayout) view.findViewById(R.id.rlJumpToMap);
        tvTop.setOnClickListener(this);
        tvRecord.setOnClickListener(this);
        llTrack.setOnClickListener(this);
        rlJumpToMap.setOnClickListener(this);
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
