package com.tiyujia.homesport.common.personal.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ff.imagezoomdrag.ImageDetailActivity;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.community.activity.CommunityDynamicDetailActivity;
import com.tiyujia.homesport.common.homepage.adapter.NGLAdapter;
import com.tiyujia.homesport.common.personal.model.MyDynamicModel;
import com.tiyujia.homesport.entity.GridAdapter;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.entity.LzyResponse;
import com.tiyujia.homesport.util.LvUtil;
import com.tiyujia.homesport.util.PicUtil;
import com.tiyujia.homesport.util.PicassoUtil;
import com.tiyujia.homesport.util.StringUtil;
import com.w4lle.library.NineGridlayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者: Cymbi on 2016/11/14 15:37.
 * 邮箱:928902646@qq.com
 */

public class DynamicAdapter extends BaseQuickAdapter<MyDynamicModel.Dynamic>{

    public DynamicAdapter( List<MyDynamicModel.Dynamic> data) {
        super(R.layout.personal_dynamic_item, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final MyDynamicModel.Dynamic dynamic) {
        SharedPreferences share=mContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        final int accountId=share.getInt("UserId",0);
        final String token=share.getString("Token","");
        baseViewHolder.setText(R.id.tvNickname, dynamic.userIconVo.nickName)
                .setText(R.id.tvDesc, dynamic.topicContent)
                .setText(R.id.tvMsg, dynamic.commentCounts + "")
                .setText(R.id.tvZan, dynamic.zanCounts + "");
        ImageView ivAvatar = baseViewHolder.getView(R.id.ivAvatar);
        TextView tvTime = baseViewHolder.getView(R.id.tvTime);
        NineGridlayout nineGrid = baseViewHolder.getView(R.id.nineGrid);
        final TextView tvZan = baseViewHolder.getView(R.id.tvZan);
        tvTime.setText(API.simpleDateFormat.format(dynamic.createTime));
        tvZan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkGo.post(API.BASE_URL+"/v2/zan/add")
                        .tag(this)
                        .params("token",token)
                        .params("bodyId",dynamic.id)
                        .params("bodyType",4)
                        .params("bodyUserId",dynamic.userIconVo.id)
                        .params("accountId",accountId)
                        .execute(new LoadCallback<LzyResponse>((Activity)mContext) {
                            @Override
                            public void onSuccess(LzyResponse lzyResponse, Call call, Response response) {
                                if (lzyResponse.successmsg.equals("添加成功")){
                                    tvZan.setText(Integer.valueOf(tvZan.getText().toString())+1+"");
                                    Drawable drawable = mContext.getResources().getDrawable(R.mipmap.btn_o_zan_s);
                                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                    tvZan.setCompoundDrawables(drawable,null,null,null);
                                    Toast.makeText(mContext,"已点赞！",Toast.LENGTH_LONG).show();}
                                else if (lzyResponse.successmsg.equals("取消点赞成功")){
                                    tvZan.setText(Integer.valueOf(tvZan.getText().toString())-1+"");
                                    Drawable drawable = mContext.getResources().getDrawable(R.mipmap.btn_good);
                                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                    tvZan.setCompoundDrawables(drawable,null,null,null);
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
        PicassoUtil.handlePic(mContext, PicUtil.getImageUrlDetail(mContext, StringUtil.isNullAvatar(dynamic.userIconVo.avatar), 320, 320), ivAvatar, 320, 320);
        if (dynamic.imgUrl != null) {
            String str = dynamic.imgUrl;
            final ArrayList<String> imgUrls =(ArrayList) StringUtil.stringToList(str);
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
        View view=baseViewHolder.getConvertView();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, CommunityDynamicDetailActivity.class);
                intent.putExtra("recommendId",dynamic.id);
                mContext.startActivity(intent);
            }
        });
    }}
