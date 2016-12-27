package com.tiyujia.homesport.common.personal.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tiyujia.homesport.API;
import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者: Cymbi on 2016/11/15 17:52.
 * 邮箱:928902646@qq.com
 */

public class ProtocolAcitvity extends ImmersiveActivity {
    @Bind(R.id.tv_title)TextView tv_title;
    @Bind(R.id.personal_back)ImageView personal_back;
    @Bind(R.id.webview)WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_gold_rule);
        ButterKnife.bind(this);
        String title=getIntent().getStringExtra("title");
        String Url=getIntent().getStringExtra("Url");
        tv_title.setText(title);
        webview.loadUrl(API.Protocol_URL+Url);
        personal_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
