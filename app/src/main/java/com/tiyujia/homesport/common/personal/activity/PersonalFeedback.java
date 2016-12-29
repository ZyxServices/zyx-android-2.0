package com.tiyujia.homesport.common.personal.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.community.activity.CommunityDynamicPublish;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.entity.LzyResponse;
import com.tiyujia.homesport.util.EmojiFilterUtil;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;

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
        SharedPreferences share = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        final String mToken = share.getString("Token", "");
        final int mUserId = share.getInt("UserId", 0);
        personal_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        content.addTextChangedListener(textWatcher);
        tv_push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempText = content.getText().toString().trim();
                final String Content= EmojiFilterUtil.filterEmoji(PersonalFeedback.this,tempText);
                OkGo.post(API.BASE_URL+"/v2/user/suggest")
                        .tag(this)
                        .params("token",mToken)
                        .params("userId",mUserId)
                        .params("content",Content)
                        .execute(new LoadCallback<LzyResponse>(PersonalFeedback.this) {
                            @Override
                            public void onSuccess(LzyResponse lzyResponse, Call call, Response response) {
                            if(lzyResponse.state==200){showToast("反馈成功");finish();}
                            else{showToast("服务器故障");}}
                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                showToast("服务器故障");
                            }
                        });
            }
        });
    }
    TextWatcher textWatcher=new TextWatcher(){
        private CharSequence temp;
        private int editStart ;
        private int editEnd ;
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp=s;
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
        @Override
        public void afterTextChanged(Editable s) {
            editStart = content.getSelectionStart();
            editEnd = content.getSelectionEnd();
            tv_number.setText(temp.length()+"/"+"500");
            if (temp.length() > 500) {
                showToast("你输入的字数已经超过了限制");
                s.delete(editStart-1, editEnd);
                int tempSelection = editStart;
                content.setText(s);
                content.setSelection(tempSelection);
            }
        }
    };
}
