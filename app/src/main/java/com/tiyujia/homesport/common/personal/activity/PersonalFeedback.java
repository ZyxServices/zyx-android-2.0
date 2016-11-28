package com.tiyujia.homesport.common.personal.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;

import butterknife.Bind;

/**
 * 作者: Cymbi on 2016/11/10 17:47.
 * 邮箱:928902646@qq.com
 */

public class PersonalFeedback extends ImmersiveActivity {
    @Bind(R.id.tv_number)
    TextView tv_number;
    @Bind(R.id.tv_push)
    TextView tv_push;
    @Bind(R.id.content)
    EditText content;
    @Bind(R.id.personal_back)
    ImageView personal_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        setview();
    }

    private void setview() {
        personal_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
