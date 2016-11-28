package com.tiyujia.homesport.common.personal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者: Cymbi on 2016/11/15 11:51.
 * 邮箱:928902646@qq.com
 */

public class PersonalPanyanGold extends ImmersiveActivity implements View.OnClickListener{

    @Bind(R.id.tv_title)TextView tv_title;
    @Bind(R.id.tv_rule)TextView tv_rule;
    @Bind(R.id.personal_back)ImageView personal_back;
    @Bind(R.id.re_record)RelativeLayout re_record;
    @Bind(R.id.re_gold)RelativeLayout re_gold;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_panyangold);
        ButterKnife.bind(this);
        tv_title.setText("攀岩币");
        initView();
    }

    private void initView() {
        re_record.setOnClickListener(this);
        re_gold.setOnClickListener(this);
        tv_rule.setOnClickListener(this);
        personal_back.setOnClickListener(this);
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
                startActivity(new Intent(this,PersonalGoldRule.class));
                break;
        }
    }
}
