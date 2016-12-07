package com.tiyujia.homesport.common.personal.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.personal.activity.PersonalOtherHome;
import com.tiyujia.homesport.common.personal.model.AttentionModel;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.entity.LzyResponse;
import com.tiyujia.homesport.util.LvUtil;
import com.tiyujia.homesport.util.PicUtil;
import com.tiyujia.homesport.util.PicassoUtil;
import com.tiyujia.homesport.util.StringUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者: Cymbi on 2016/11/15 11:58.
 * 邮箱:928902646@qq.com
 */

public class AttentionAdapter extends  BaseQuickAdapter<AttentionModel.AttentionList> {
    Context context;
    public AttentionAdapter(Context context,List<AttentionModel.AttentionList> data) {
        super(R.layout.personal_attention_item, data);
        this.context=context;
    }
    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final AttentionModel.AttentionList attention) {
        final Activity activity=(Activity) context;
        SharedPreferences share = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        final String mToken=share.getString("Token","");
        final int mUserId=share.getInt("UserId",0);
        baseViewHolder.setText(R.id.tvNickname,attention.nickname)
                .setText(R.id.tvContent,attention.signatures);
        ImageView ivAvatar=baseViewHolder.getView(R.id.ivAvatar);
        ImageView ivLv=baseViewHolder.getView(R.id.ivLv);
        final TextView tv_not=baseViewHolder.getView(R.id.tv_not);
        tv_not.setVisibility(View.VISIBLE);
        if(attention.level!=null){
            LvUtil.setLv(ivLv,attention.level.pointDesc);
        }else {
            LvUtil.setLv(ivLv,"初学乍练");
        }
        PicassoUtil.handlePic(context,PicUtil.getImageUrlDetail(context,StringUtil.isNullAvatar(attention.avatar), 320, 320),ivAvatar,320,320);
        tv_not.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkGo.post(API.BASE_URL+"/v2/follow/unfollow")
                        .tag(this)
                        .params("token",mToken)
                        .params("fromUserId",mUserId)
                        .params("toUserId",attention.id)
                        .execute(new LoadCallback<LzyResponse>(activity) {
                            @Override
                            public void onSuccess(LzyResponse lzyResponse, Call call, Response response) {
                                if(lzyResponse.state==200){
                                    Toast.makeText(activity,"取消关注成功",Toast.LENGTH_SHORT).show();
                                    tv_not.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(mContext, PersonalOtherHome.class);
                i.putExtra("id",attention.id);
                mContext.startActivity(i);

            }
        });
    }
}
