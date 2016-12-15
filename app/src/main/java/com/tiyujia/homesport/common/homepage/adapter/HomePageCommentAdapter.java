package com.tiyujia.homesport.common.homepage.adapter;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.entity.HomePageCommentEntity;
import com.tiyujia.homesport.common.personal.activity.PersonalOtherHome;
import com.tiyujia.homesport.util.LvUtil;
import com.tiyujia.homesport.util.StringUtil;
import com.tiyujia.homesport.util.TimeUtil;
import com.w4lle.library.NineGridlayout;
import java.util.Date;
import java.util.List;
/**
 * Created by zzqybyb19860112 on 2016/12/15.
 */
public class HomePageCommentAdapter extends BaseQuickAdapter<HomePageCommentEntity.HomePage> {
    public HomePageCommentAdapter(List<HomePageCommentEntity.HomePage> data) {
        super(R.layout.item_rv_homepage_venuedetail_user_say,data);
    }
    @Override
    protected void convert(BaseViewHolder baseViewHolder, final HomePageCommentEntity.HomePage entity) {
        RoundedImageView rivMainUserPhoto=baseViewHolder.getView(R.id.rivMainUserPhoto);
        rivMainUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, PersonalOtherHome.class);
                int userId=entity.userVo.id;
                intent.putExtra("id",userId);
                mContext.startActivity(intent);
            }
        });
        ImageView ivMainUserLevel=baseViewHolder.getView(R.id.ivMainUserLevel);
        NineGridlayout nglMainUserImage=baseViewHolder.getView(R.id.nglMainUserImage);
        RecyclerView rvDiscussCallBack=baseViewHolder.getView(R.id.rvDiscussCallBack);
        Picasso.with(mContext).load(StringUtil.isNullAvatar(entity.userVo.avatar)).into(rivMainUserPhoto);
        baseViewHolder.setText(R.id.tvMainUserName,entity.userVo.nickName)
                .setText(R.id.tvTalkTime, TimeUtil.formatFriendly(new Date(entity.createTime)))
                .setText(R.id.tvTalkContent,entity.commentContent);
        String text1=entity.userVo.levelName;
        if (text1==null){
            text1="";
        }
        LvUtil.setLv(ivMainUserLevel,LvUtil.setLevelTXT(text1));
        String text2=entity.commentImgPath;
        if (text2==null){
            text2="";
        }
        List<String> picList=StringUtil.stringToList(text2);
        if (picList!=null&&picList.size()!=0){
            NGLAdapter adapter = new NGLAdapter(mContext, picList);
            nglMainUserImage.setVisibility(View.VISIBLE);
            nglMainUserImage.setGap(6);
            nglMainUserImage.setAdapter(adapter);
        }else {
            nglMainUserImage.setVisibility(View.GONE);
        }
        List<HomePageCommentEntity.HomePage.ReplyData> replyDatas = entity.replyVos;
        if (replyDatas!=null&&replyDatas.size()!=0){
            rvDiscussCallBack.setVisibility(View.VISIBLE);
            rvDiscussCallBack.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL,false));
            CommentListAdapter commentListAdapter=new CommentListAdapter(mContext,replyDatas);
            rvDiscussCallBack.setAdapter(commentListAdapter);
        }else {
            rvDiscussCallBack.setVisibility(View.GONE);
        }
    }


}
