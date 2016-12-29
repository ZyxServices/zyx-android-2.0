package com.tiyujia.homesport.common.record.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.record.adapter.RecordTrackAdapter;
import com.tiyujia.homesport.common.record.model.CityHistoryModel;
import com.tiyujia.homesport.common.record.model.OverViewModel;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.entity.LzyResponse;
import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者: Cymbi on 2016/11/22 09:44.
 * 邮箱:928902646@qq.com
 */

public class RecordTrackActivity extends ImmersiveActivity implements View.OnClickListener {
    @Bind(R.id.ivBack)    ImageView ivBack;
    @Bind(R.id.ivShare)   ImageView ivShare;
    @Bind(R.id.llTrack)   LinearLayout llTrack;
    @Bind(R.id.recyclerView)    RecyclerView recyclerView;
    @Bind(R.id.tvSportTimes)    TextView tvSportTimes;
    @Bind(R.id.tvTotalScore)    TextView tvTotalScore;
    private String mToken;
    private RecordTrackAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_track);
        initView();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }
    private void initView() {
        SharedPreferences share=getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        mToken=share.getString("Token","");
        ivBack.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        llTrack.setOnClickListener(this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new RecordTrackAdapter(null);
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        adapter.isFirstOnly(false);
        recyclerView.setAdapter(adapter);
        View view= LayoutInflater.from(this).inflate(R.layout.normal_empty_image_view,null);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(lp2);
        TextView tvEmptyText= (TextView) view.findViewById(R.id.text_empty);
        tvEmptyText.setText("暂无数据");
        adapter.setEmptyView(view);
        OkGo.post(API.BASE_URL+"/v2/record/overview")
                .tag(this)
                .params("token",mToken)
                .execute(new LoadCallback<LzyResponse<OverViewModel>>(this) {
                    @Override
                    public void onSuccess(LzyResponse<OverViewModel> model, Call call, Response response) {
                        if(model.state==200){
                            tvSportTimes.setText(model.data.sportTimes+"");
                            tvTotalScore.setText(model.data.totalScore+"");
                        }
                    }
                });
        OkGo.post(API.BASE_URL+"/v2/record/history")
                .tag(this)
                .params("token",mToken)
                .params("pageSize",100)
                .params("pageNum",1)
                .execute(new LoadCallback<CityHistoryModel>(this) {
                    @Override
                    public void onSuccess(CityHistoryModel cityHistoryModel, Call call, Response response) {
                        if(cityHistoryModel.state==200){
                            adapter.setNewData(cityHistoryModel.data);
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        showToast("服务器故障");
                    }
                });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivBack:
                finish();
                break;
            case R.id.llTrack:
                        startActivity(new Intent(RecordTrackActivity.this,CityMapHistoryActivity.class));
                break;
        }
    }
   /* private void showDialog(){
        builder = new AlertDialog.Builder(this).create();
        builder.setView(this.getLayoutInflater().inflate(R.layout.share_succeed_dialog, null));
        builder.show();
        //去掉dialog四边的黑角
        builder.getWindow().setBackgroundDrawable(new BitmapDrawable());
        TextView tvTitle=(TextView)builder.getWindow().findViewById(R.id.tvTitle);
        tvTitle.setText("分享成功");
        TextView tvContent=(TextView)builder.getWindow().findViewById(R.id.tvContent);
        tvContent.setText("感谢您的分享，祝您玩愉快");
    }*/

}
