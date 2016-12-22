package com.tiyujia.homesport.common.personal.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ff.imagezoomdrag.ImageDetailActivity;
import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.adapter.NGLAdapter;
import com.tiyujia.homesport.common.personal.model.EquipmentShowModel;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.entity.LzyResponse;
import com.tiyujia.homesport.util.PicUtil;
import com.tiyujia.homesport.util.PicassoUtil;
import com.tiyujia.homesport.util.StringUtil;
import com.tiyujia.homesport.util.TimeUtil;
import com.w4lle.library.NineGridlayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者: Cymbi on 2016/12/7 12:08.
 * 邮箱:928902646@qq.comcxzczcxcxcxz
 */

public class EquipmentShowAdapter extends BaseQuickAdapter<EquipmentShowModel.Model> {
    public EquipmentShowAdapter(List<EquipmentShowModel.Model> data) {
        super(R.layout.personal_equipment_show_item, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final EquipmentShowModel.Model model) {
        SharedPreferences share=mContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        final int accountId=share.getInt("UserId",0);
        final String token=share.getString("Token","");
        baseViewHolder.setText(R.id.tv_nickname,model.userIconVo.nickName)
        .setText(R.id.desc,"  "+model.content)
        .setText(R.id.tv_msg,model.commentCounts+"")
        .setText(R.id.tv_zan,model.zanCounts+"")
        .setText(R.id.tv_time,API.simpleDateFormat.format(model.createTime)+"发布");
        NineGridlayout nineGrid= baseViewHolder.getView(R.id.nineGrid);
        ImageView ivAvatar= baseViewHolder.getView(R.id.ivAvatar);
        final TextView tv_zan = baseViewHolder.getView(R.id.tv_zan);
        tv_zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkGo.post(API.BASE_URL+"/v2/zan/add")
                        .tag(this)
                        .params("token",token)
                        .params("bodyId",model.id)
                        .params("bodyType",4)
                        .params("bodyUserId",model.userIconVo.id)
                        .params("accountId",accountId)
                        .execute(new LoadCallback<LzyResponse>((Activity)mContext) {
                            @Override
                            public void onSuccess(LzyResponse lzyResponse, Call call, Response response) {
                                if (lzyResponse.successmsg.equals("添加成功")){
                                    tv_zan.setText(Integer.valueOf(tv_zan.getText().toString())+1+"");
                                    Drawable drawable = mContext.getResources().getDrawable(R.mipmap.btn_o_zan_s);
                                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                    tv_zan.setCompoundDrawables(drawable,null,null,null);
                                    Toast.makeText(mContext,"已点赞！",Toast.LENGTH_LONG).show();}
                                else if (lzyResponse.successmsg.equals("取消点赞成功")){
                                    tv_zan.setText(Integer.valueOf(tv_zan.getText().toString())-1+"");
                                    Drawable drawable = mContext.getResources().getDrawable(R.mipmap.btn_good);
                                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                    tv_zan.setCompoundDrawables(drawable,null,null,null);
                                    Toast.makeText(mContext,"已取消点赞！",Toast.LENGTH_LONG).show();
                                }
                            }
                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                super.onError(call, response, e);
                                Toast.makeText((Activity)mContext,"网络故障或服务器故障",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        if (model.imgUrl != null) {
            String str = model.imgUrl;
            final ArrayList<String> imgUrls =(ArrayList) StringUtil.stringToList(str);;
            NGLAdapter adapter = new NGLAdapter(mContext, imgUrls);
            nineGrid.setVisibility(View.VISIBLE);
            nineGrid.setGap(6);
            nineGrid.setAdapter(adapter);
            nineGrid.setOnItemClickListerner(new NineGridlayout.OnItemClickListerner() {
                @Override
                public void onItemClick(View view, int position) {
                    mContext.startActivity(ImageDetailActivity.getMyStartIntent(mContext, imgUrls,position, ImageDetailActivity.url_path));
                }
            });
        }else {
            nineGrid.setVisibility(View.GONE);
        }
        PicassoUtil.handlePic(mContext, PicUtil.getImageUrlDetail(mContext, StringUtil.isNullAvatar(model.userIconVo.avatar), 320, 320), ivAvatar, 320, 320);
    }
}
