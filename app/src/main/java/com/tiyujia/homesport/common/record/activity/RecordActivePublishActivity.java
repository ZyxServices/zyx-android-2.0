package com.tiyujia.homesport.common.record.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者: Cymbi on 2016/11/18 15:22.
 * 邮箱:928902646@qq.com
 */

public class RecordActivePublishActivity extends ImmersiveActivity implements View.OnClickListener{
    @Bind(R.id.personal_back)    ImageView personal_back;
    @Bind(R.id.tvPush)    TextView tvPush;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_active_publish);
        ButterKnife.bind(this);
        initview();
    }
    private void initview() {
        personal_back.setOnClickListener(this);
        tvPush.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.personal_back:
                finish();
                break;
            case  R.id.tvPush:
                showToast("发布");
                break;

        }
    }
}
