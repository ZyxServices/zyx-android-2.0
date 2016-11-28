package com.tiyujia.homesport.common.record.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者: Cymbi on 2016/11/21 14:36.
 * 邮箱:928902646@qq.com
 */

public class RecordTopActivity extends ImmersiveActivity implements View.OnClickListener{
    @Bind(R.id.ivBack)    ImageView ivBack;
    @Bind(R.id.ivShare)    ImageView ivShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_top);
        ButterKnife.bind(this);
        setView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivShare:

                break;
        }
    }

    private void setView() {
        ivBack.setOnClickListener(this);
        ivShare.setOnClickListener(this);
    }
}
