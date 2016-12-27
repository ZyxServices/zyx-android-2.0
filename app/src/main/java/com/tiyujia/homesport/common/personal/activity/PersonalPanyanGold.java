package com.tiyujia.homesport.common.personal.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.personal.model.CoindayModel;
import com.tiyujia.homesport.common.personal.model.PanyanGoldModel;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.entity.LzyResponse;

import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者: Cymbi on 2016/11/15 11:51.
 * 邮箱:928902646@qq.com
 */

public class PersonalPanyanGold extends ImmersiveActivity implements View.OnClickListener{

    @Bind(R.id.tv_title)TextView tv_title;
    @Bind(R.id.tv_rule)TextView tv_rule;
    @Bind(R.id.tvShareNumber)TextView tvShareNumber;
    @Bind(R.id.tvDynamicNumber)TextView tvDynamicNumber;
    @Bind(R.id.tvPinlun)TextView tvPinlun;
    @Bind(R.id.tvZanNumber)TextView tvZanNumber;
    @Bind(R.id.tv_gold_number)TextView tv_gold_number;
    @Bind(R.id.personal_back)ImageView personal_back;
    @Bind(R.id.re_record)RelativeLayout re_record;
    @Bind(R.id.re_gold)RelativeLayout re_gold;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_panyangold);
        tv_title.setText("攀岩币");
        initView();
    }
    private void initView() {
        re_record.setOnClickListener(this);
        re_gold.setOnClickListener(this);
        tv_rule.setOnClickListener(this);
        personal_back.setOnClickListener(this);
        SharedPreferences share = getSharedPreferences("UserInfo", MODE_PRIVATE);
        int userId=share.getInt("UserId",0);
        OkGo.post(API.BASE_URL+"/v2/coin/get")
                .tag(this)
                .params("userId",userId)
                .execute(new LoadCallback<LzyResponse<PanyanGoldModel>>(this) {
                    @Override
                    public void onSuccess(LzyResponse<PanyanGoldModel> GoldModel, Call call, Response response) {
                        if(GoldModel.state==200){
                            tv_gold_number.setText(GoldModel.data.coin+"");
                        }
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        showToast("网络连接失败");
                    }
                });
        //｛1-新用户第一次使用 2-完善资料 3-分享 4-关注 点赞 5-发布评论 6-发布活动 7-发布动态 8-老带新 9-装备秀 10-被举报｝
        OkGo.post(API.BASE_URL+"/v2/coin/day")
                .tag(this)
                .params("userId",userId)
                .params("offset",0)
                .execute(new LoadCallback<LzyResponse<List<CoindayModel>>>(this) {
                    @Override
                    public void onSuccess(LzyResponse<List<CoindayModel>> coinday, Call call, Response response) {
                        if(coinday.state==200){
                            for(int i=0;i<coinday.data.size();i++){
                                CoindayModel jk = coinday.data.get(i);
                                int operId=jk.operId;
                                int operTimes=jk.operTimes;
                                if(operId==4){
                                    if(operTimes<5){
                                        tvZanNumber.setText("未完成"+"("+operTimes+"/5"+")");
                                    }else {
                                        tvZanNumber.setText("已完成");
                                    }
                                }else if(operId==5){
                                    if(operTimes<2){
                                        tvPinlun.setText("未完成"+"("+operTimes+"/2"+")");
                                    }else {
                                        tvPinlun.setText("已完成");
                                    }
                                }else if(operId==7){
                                    if(operTimes<5){
                                        tvDynamicNumber.setText("未完成"+"("+operTimes+"/5"+")");
                                    }else {
                                        tvDynamicNumber.setText("已完成");
                                    }
                                }else if(operId==3){
                                    if(operTimes<3){
                                        tvShareNumber.setText("未完成"+"("+operTimes+"/3"+")");
                                    }else {
                                        tvShareNumber.setText("已完成");
                                    }
                                }
                            }
                        }
                    }
                });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.personal_back:
                finish();
                break;
            case R.id.re_record:
                startActivity(new Intent(this,PersonalPanyanGoldRecord.class));
                break;
            case R.id.re_gold:
                startActivity(new Intent(this,PersonalPanyanGoldInfo.class));
                break;
            case R.id.tv_rule:
                Intent a=new Intent(this,ProtocolAcitvity.class);
                a.putExtra("title","趣攀岩攀岩币规则");
                a.putExtra("Url","/ip/phone/rule/climbingCoins");
                startActivity(a);
                break;
        }
    }
}