package com.tiyujia.homesport.common.personal.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.activity.HomePageDateInfo;
import com.tiyujia.homesport.common.personal.activity.PersonalOtherHome;
import com.tiyujia.homesport.common.personal.model.ActiveModel;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.entity.LzyResponse;
import com.tiyujia.homesport.util.LvUtil;
import com.tiyujia.homesport.util.PicUtil;
import com.tiyujia.homesport.util.PicassoUtil;
import com.tiyujia.homesport.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者: Cymbi on 2016/11/14 11:10.
 * 邮箱:928902646@qq.com
 */

public class AttendAdapter extends BaseQuickAdapter<ActiveModel.Active> {
    Context context;
    private SimpleDateFormat sdf =new SimpleDateFormat("MM-dd HH:mm");
    public AttendAdapter(Context context,List<ActiveModel.Active> data) {
        super(R.layout.attend_fragment_item,data);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final ActiveModel.Active active) {
        final Activity activity=(Activity) context;
        SharedPreferences share=mContext.getSharedPreferences("UserInfo",Context.MODE_PRIVATE);
        final int accountId=share.getInt("UserId",0);
        final String token=share.getString("Token","");
        ImageView ivAvatar=baseViewHolder.getView(R.id.ivAvatar);
        ImageView ivLv=baseViewHolder.getView(R.id.ivLv);
        final TextView tv_zan=baseViewHolder.getView(R.id.tv_zan);
        ImageView iv_background=baseViewHolder.getView(R.id.iv_background);
        TextView tv_active_lable=baseViewHolder.getView(R.id.tv_active_lable);
        String createTime= sdf.format(active.createTime);
        long lastTime=active.lastTime;
        long currentTime=System.currentTimeMillis();
        if(lastTime>currentTime){
            baseViewHolder.setText(R.id.tv_apply_lable,"正在报名");
        }else {
            baseViewHolder.setText(R.id.tv_apply_lable,"报名已结束");
        }
        int memberPeople=active.memberPeople;
        int maxPeople=active.maxPeople;

        baseViewHolder.setText(R.id.tvNickname,active.user.nickname)
                .setText(R.id.tv_title,active.title)
                .setText(R.id.tv_address,active.city)
                .setText(R.id.tv_msg,active.commentNumber+"")
                .setText(R.id.tvTime,createTime)
                .setText(R.id.tv_zan,active.zan+"");
        TextView tv_apply = baseViewHolder.getView(R.id.tv_apply);
        if(maxPeople==0){
            tv_apply.setText("已报名："+memberPeople);
        }else if(maxPeople>memberPeople) {
            tv_apply.setText("已报名："+memberPeople+"，还剩: "+(maxPeople-memberPeople)+"名");
        }else if(maxPeople<=memberPeople){
            tv_apply.setText("报名人数已满");
        }

        if (active.activityType==0){
            tv_active_lable.setText("求约");
        }else if (active.activityType==1){
            tv_active_lable.setText("求带");
        }
        if (active.user.level!=null||active.user.level.equals("")){
            LvUtil.setLv(ivLv,active.user.level.pointDesc);
        }else {}
        PicassoUtil.handlePic(context, PicUtil.getImageUrlDetail(context, StringUtil.isNullAvatar(active.user.avatar), 320, 320),ivAvatar,320,320);
        PicassoUtil.handlePic(context, PicUtil.getImageUrlDetail(context, StringUtil.isNullImage(active.imgUrls), 720, 720),iv_background,720,720);
        View view= baseViewHolder.getConvertView();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(mContext,HomePageDateInfo.class);
                i.putExtra("id",active.id);
                mContext.startActivity(i);
            }
        });
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mContext, PersonalOtherHome.class);
                i.putExtra("id",active.user.id);
                mContext.startActivity(i);
            }
        });
        tv_zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkGo.post(API.BASE_URL+"/v2/zan/add")
                        .tag(this)
                        .params("token",token)
                        .params("bodyId",active.id)
                        .params("bodyType",4)
                        .params("bodyUserId",active.user.id)
                        .params("accountId",accountId)
                        .execute(new LoadCallback<LzyResponse>(activity) {
                            @Override
                            public void onSuccess(LzyResponse lzyResponse, Call call, Response response) {
                                            if (lzyResponse.successmsg.equals("添加成功")){
                                                tv_zan.setText(Integer.valueOf(tv_zan.getText().toString())+1+"");
                                                Drawable drawable = context.getResources().getDrawable(R.mipmap.btn_o_zan_s);
                                                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                                tv_zan.setCompoundDrawables(drawable,null,null,null);
                                                Toast.makeText(context,"已点赞！",Toast.LENGTH_LONG).show();}
                                            else if (lzyResponse.successmsg.equals("取消点赞成功")){
                                                tv_zan.setText(Integer.valueOf(tv_zan.getText().toString())-1+"");
                                                Drawable drawable = context.getResources().getDrawable(R.mipmap.btn_good);
                                                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                                tv_zan.setCompoundDrawables(drawable,null,null,null);
                                                Toast.makeText(context,"已取消点赞！",Toast.LENGTH_LONG).show();
                                            }
                            }
                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                super.onError(call, response, e);
                                Toast.makeText(activity,"网络故障或服务器故障",Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
    }
}
