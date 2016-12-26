package com.tiyujia.homesport.common.personal.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.tiyujia.homesport.HomeActivity;
import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.util.DataCleanManagerUtil;
import com.tiyujia.homesport.util.StatusBarUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者: Cymbi on 2016/10/19 15:33.
 * 邮箱:928902646@qq.com
 */

public class PersonalSystemSetting extends ImmersiveActivity implements View.OnClickListener{
    @Bind(R.id.personal_back) ImageView personal_back;
    @Bind(R.id.re_info) RelativeLayout re_info;
    @Bind(R.id.re_attestation) RelativeLayout re_attestation;
    @Bind(R.id.re_grade) RelativeLayout re_grade;
    @Bind(R.id.re_clean) RelativeLayout re_clean;
    @Bind(R.id.re_about) RelativeLayout re_about;
    @Bind(R.id.re_feedback) RelativeLayout re_feedback;
    @Bind(R.id.tv_loginout) TextView tv_loginout;
    @Bind(R.id.tvCache) TextView tvCache;
    @Bind(R.id.togglebutton)ToggleButton togglebutton;
    @Bind(R.id.tv_title)TextView tv_title;
    private AlertDialog builder;
    private String mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_setting);
        setview();
        SharedPreferences share = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        mToken=share.getString("Token","");
    }

    private void setview() {
        tv_title.setText("设置");
        personal_back.setOnClickListener(this);
        re_info.setOnClickListener(this);
        re_attestation.setOnClickListener(this);
        re_grade.setOnClickListener(this);
        re_clean.setOnClickListener(this);
        re_feedback.setOnClickListener(this);
        tv_loginout.setOnClickListener(this);
        re_about.setOnClickListener(this);
        try {
            String cache = DataCleanManagerUtil.getTotalCacheSize(this);
            tvCache.setText(cache);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.personal_back:
                finish();
                break;
            case R.id.re_info:
                if(!TextUtils.isEmpty(mToken)){
                    startActivity(new Intent(this,PersonalSetInfo.class));
                }else {
                    showToast("请先登录");
                }
                break;
            case R.id.re_attestation:
                break;
            case R.id.re_grade:

                break;
            case R.id.re_clean:
                builder = new AlertDialog.Builder(PersonalSystemSetting.this).create();
                builder.setView(getLayoutInflater().inflate(R.layout.clear_personal_dialog, null));
                builder.show();
                //去掉dialog四边的黑角
                builder.getWindow().setBackgroundDrawable(new BitmapDrawable());
                builder.getWindow().findViewById(R.id.dialog_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();
                    }
                });
                builder.getWindow().findViewById(R.id.dialog_confirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DataCleanManagerUtil.clearAllCache(getApplicationContext());
                        Toast.makeText(getApplicationContext(), "清理完成", Toast.LENGTH_LONG).show();
                        String cache = null;
                        try {
                            cache = DataCleanManagerUtil.getTotalCacheSize(PersonalSystemSetting.this);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        tvCache.setText(cache);
                        builder.dismiss();
                    }
                });
                break;
            case R.id.re_feedback:
                startActivity(new Intent(this,PersonalFeedback.class));
                break;
            case R.id.tv_loginout:
                dialog();
                break;
            case R.id.re_about:
                startActivity(new Intent(this,PersonalAbout.class));
                break;
        }


    }

    private void dialog() {

        builder = new AlertDialog.Builder(this).create();
        builder.setView(this.getLayoutInflater().inflate(R.layout.log_out_dialog, null));
        builder.show();
        //去掉dialog四边的黑角
        builder.getWindow().setBackgroundDrawable(new BitmapDrawable());
        builder.getWindow().findViewById(R.id.btnCancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        builder.getWindow().findViewById(R.id.btnSure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences share = getSharedPreferences("UserInfo",MODE_PRIVATE);
                SharedPreferences.Editor etr=share.edit();
                etr.clear().commit();
                Intent i=new Intent(PersonalSystemSetting.this, HomeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                showToast("注销成功");
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
