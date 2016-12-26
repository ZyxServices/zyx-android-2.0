package com.tiyujia.homesport.common.community.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ff.imagezoomdrag.ImageDetailActivity;
import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.community.activity.CommunityDynamicDetailActivity;
import com.tiyujia.homesport.common.community.model.RecommendModel;
import com.tiyujia.homesport.common.homepage.adapter.NGLAdapter;
import com.tiyujia.homesport.common.personal.activity.PersonalOtherHome;
import com.tiyujia.homesport.common.personal.model.ActiveModel;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.entity.LzyResponse;
import com.tiyujia.homesport.util.PicUtil;
import com.tiyujia.homesport.util.PicassoUtil;
import com.tiyujia.homesport.util.StringUtil;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;
import com.w4lle.library.NineGridlayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者: Cymbi on 2016/11/14 15:37.1
 * 邮箱:928902646@qq.com
 */

public class RecommendAdapter extends BaseQuickAdapter<RecommendModel.Recommend> {
    Context context;
    public RecommendAdapter(Context context, List<RecommendModel.Recommend> data) {
        super(R.layout.personal_dynamic_item, data);
        this.context=context;
    }
    @Override
    protected void convert(BaseViewHolder baseViewHolder, final RecommendModel.Recommend recommend) {
        final String videoUrl=recommend.videoUrl;
        ImageView ivAvatar=baseViewHolder.getView(R.id.ivAvatar);
        NineGridlayout nineGrid=baseViewHolder.getView(R.id.nineGrid);
        FrameLayout frVideo=baseViewHolder.getView(R.id.frVideo);
        ImageView ivVideoStart=baseViewHolder.getView(R.id.ivVideoStart);
        baseViewHolder.setText(R.id.tvNickname,recommend.userIconVo.nickName)
                .setText(R.id.tvDesc,recommend.topicContent)
                .setText(R.id.tvMsg,recommend.commentCounts+"");
        final TextView tvZan=baseViewHolder.getView(R.id.tvZan);
        tvZan.setText(recommend.zanCounts+"");
        if(!TextUtils.isEmpty(recommend.local)){
            baseViewHolder.setText(R.id.tvAddress,recommend.local);
        }else {
            baseViewHolder.setText(R.id.tvAddress,"");
        }
        PicassoUtil.handlePic(context, PicUtil.getImageUrlDetail(context, StringUtil.isNullAvatar(recommend.userIconVo.avatar), 320, 320),ivAvatar,320,320);
        String str= recommend.imgUrl;
        if(TextUtils.isEmpty(videoUrl)){
            frVideo.setVisibility(View.GONE);
        }else {
            ivVideoStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   Uri uri=Uri.parse(videoUrl);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Log.v("URI:::::::::", uri.toString());
                    intent.setDataAndType(uri, "video/mp4");
                    mContext.startActivity(intent);
                }
            });
        }
        if(str!=null){
        final ArrayList<String>  imgUrls=(ArrayList<String>) StringUtil.stringToList(str);;
            NGLAdapter adapter = new NGLAdapter(context, imgUrls);
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
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(mContext, PersonalOtherHome.class);
                i.putExtra("id",recommend.userIconVo.id);
                mContext.startActivity(i);
            }
        });
        View view=baseViewHolder.getConvertView();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(mContext, CommunityDynamicDetailActivity.class);
                i.putExtra("recommendId",recommend.id);
                mContext.startActivity(i);
            }
        });
        tvZan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences share = mContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                String token = share.getString("Token", "");
                if (TextUtils.isEmpty(token)) {
                    Toast.makeText(mContext, "您还没有登陆，亲！", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    int accountId=share.getInt("UserId",0);
                    int bodyId=recommend.id;
                    int bodyType=1;
                    int bodyUserId=recommend.userIconVo.id;
                    OkGo.post(API.BASE_URL + "/v2/zan/add")
                            .tag(this)
                            .params("token", ""+token)
                            .params("bodyId", bodyId)
                            .params("bodyType",bodyType)
                            .params("bodyUserId",bodyUserId)
                            .params("accountId", accountId)
                            .execute(new LoadCallback<LzyResponse>((Activity) mContext) {
                                @Override
                                public void onSuccess(LzyResponse lzyResponse, Call call, Response response) {
                                    String msg=lzyResponse.successmsg;
                                    if (msg.equals("添加成功")){
                                        tvZan.setText(Integer.valueOf(tvZan.getText().toString())+1+"");
                                        Drawable drawable = context.getResources().getDrawable(R.mipmap.btn_o_zan_s);
                                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                        tvZan.setCompoundDrawables(drawable,null,null,null);
                                        Toast.makeText(context,"已点赞！",Toast.LENGTH_LONG).show();
                                    }else {
                                        tvZan.setText(Integer.valueOf(tvZan.getText().toString())-1+"");
                                        Drawable drawable = context.getResources().getDrawable(R.mipmap.btn_good);
                                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                        tvZan.setCompoundDrawables(drawable,null,null,null);
                                        Toast.makeText(context,"已取消点赞！",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });

    }


}
