package com.tiyujia.homesport.common.personal.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者: Cymbi on 2016/11/22 15:22.
 * 邮箱:928902646@qq.com
 */

public class PersonalAbout extends ImmersiveActivity {
    @Bind(R.id.personal_back)
    ImageView personal_back;
    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.tvVersions)
    TextView tvVersions;
    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_about);
        tv_title.setText("关于我们");
        tvVersions.setText("版本 7.0.131");
        personal_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
