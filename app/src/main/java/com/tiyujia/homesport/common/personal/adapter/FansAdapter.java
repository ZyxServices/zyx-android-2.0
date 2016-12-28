package com.tiyujia.homesport.common.personal.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.personal.model.AttentionModel;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.entity.LzyResponse;
import com.tiyujia.homesport.util.LvUtil;
import com.tiyujia.homesport.util.PicUtil;
import com.tiyujia.homesport.util.PicassoUtil;
import com.tiyujia.homesport.util.StringUtil;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者: Cymbi on 2016/11/15 11:58.
 * 邮箱:928902646@qq.com
 */

public class FansAdapter extends BaseQuickAdapter<AttentionModel.AttentionList> {
Context context;
    public FansAdapter(Context context,List<AttentionModel.AttentionList> data) {
        super(R.layout.personal_attention_item, data);
        this.context=context;
    }
    @Override
    protected void convert(BaseViewHolder baseViewHolder, final AttentionModel.AttentionList attentionList) {
        SharedPreferences share = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        final String mToken=share.getString("Token","");
        final int mUserId=share.getInt("UserId",0);
        final Activity activity=(Activity) context;
        baseViewHolder.setText(R.id.tvNickname,attentionList.nickname)
                .setText(R.id.tvContent,attentionList.signatures);
        ImageView ivAvatar=baseViewHolder.getView(R.id.ivAvatar);
        ImageView ivLv=baseViewHolder.getView(R.id.ivLv);
        final TextView tv_yes=baseViewHolder.getView(R.id.tv_yes);
        tv_yes.setVisibility(View.VISIBLE);
        if (attentionList.level!=null&&attentionList.level.equals("")){
            LvUtil.setLv(ivLv,attentionList.level.pointDesc);
        }else {
            LvUtil.setLv(ivLv,"初学乍练");
        }
        PicassoUtil.handlePic(context, PicUtil.getImageUrlDetail(context, StringUtil.isNullAvatar(attentionList.avatar), 320, 320),ivAvatar,320,320);
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkGo.post(API.BASE_URL+"/v2/follow/add")
                        .tag(this)
                        .params("token",mToken)
                        .params("fromUserId",mUserId)
                        .params("toUserId",attentionList.id)
                        .execute(new LoadCallback<LzyResponse>(activity) {
                            @Override
                            public void onSuccess(LzyResponse lzyResponse, Call call, Response response) {
                                if(lzyResponse.state==200){
                                    Toast.makeText(activity,"关注成功",Toast.LENGTH_SHORT).show();
                                    tv_yes.setText("已关注");
                                    tv_yes.setClickable(false);
                                }
                            }
                        });
            }
        });
    }
}
