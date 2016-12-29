package com.tiyujia.homesport.common.homepage.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.community.activity.CommunityDynamicDetailActivity;
import com.tiyujia.homesport.common.homepage.activity.HomePageArticleActivity;
import com.tiyujia.homesport.common.homepage.activity.HomePageDateInfo;
import com.tiyujia.homesport.common.homepage.activity.HomePageEquipmentInfo;
import com.tiyujia.homesport.common.homepage.activity.HomePageSearchResultActivity;
import com.tiyujia.homesport.common.homepage.entity.HomePageCommentEntity;
import com.tiyujia.homesport.common.personal.activity.PersonalOtherHome;
import com.tiyujia.homesport.util.EmptyViewHolder;
import com.tiyujia.homesport.util.LvUtil;
import com.tiyujia.homesport.util.StringUtil;
import com.tiyujia.homesport.util.TimeUtil;
import com.w4lle.library.NineGridlayout;

import java.util.ArrayList;
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
            RoundedImageView rivMainUserPhoto = baseViewHolder.getView(R.id.rivMainUserPhoto);
            rivMainUserPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PersonalOtherHome.class);
                    int userId = entity.userVo.id;
                    intent.putExtra("id", userId);
                    mContext.startActivity(intent);
                }
            });
            ImageView ivMainUserLevel = baseViewHolder.getView(R.id.ivMainUserLevel);
            TextView tvTalkContent = baseViewHolder.getView(R.id.tvTalkContent);
            final TextView tvMainUserName = baseViewHolder.getView(R.id.tvMainUserName);
            NineGridlayout nglMainUserImage = baseViewHolder.getView(R.id.nglMainUserImage);
            final RecyclerView rvDiscussCallBack = baseViewHolder.getView(R.id.rvDiscussCallBack);
            Picasso.with(mContext).load(StringUtil.isNullAvatar(entity.userVo.avatar)).into(rivMainUserPhoto);
            baseViewHolder.setText(R.id.tvTalkTime, TimeUtil.formatFriendly(new Date(entity.createTime)));
            tvMainUserName.setText(entity.userVo.nickName);
            tvTalkContent.setText(entity.commentContent);
            String text1 = entity.userVo.levelName;
            if (text1 == null) {
                text1 = "";
            }
            LvUtil.setLv(ivMainUserLevel, LvUtil.setLevelTXT(text1));
            String text2 = entity.commentImgPath;
            if (text2 == null) {
                text2 = "";
            }
            List<String> picList = StringUtil.stringToList(text2);
            if (picList != null && picList.size() != 0) {
                NGLAdapter adapter = new NGLAdapter(mContext, picList);
                nglMainUserImage.setVisibility(View.VISIBLE);
                nglMainUserImage.setGap(6);
                nglMainUserImage.setAdapter(adapter);
            } else {
                nglMainUserImage.setVisibility(View.GONE);
            }
            final List<HomePageCommentEntity.HomePage.ReplyData> replyDatas = entity.replyVos;
            CommentListAdapter commentListAdapter = new CommentListAdapter(mContext, replyDatas);
            if (replyDatas != null && replyDatas.size() != 0) {
                rvDiscussCallBack.setVisibility(View.VISIBLE);
                rvDiscussCallBack.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                rvDiscussCallBack.setAdapter(commentListAdapter);
                rvDiscussCallBack.addItemDecoration(new RecyclerView.ItemDecoration() {
                    @Override
                    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                        super.onDraw(c, parent, state);
                    }

                    @Override
                    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                        super.getItemOffsets(outRect, view, parent, state);
                        outRect.bottom = 10;
                        outRect.top = 10;
                    }
                });
                commentListAdapter.setOnItemClickListener(new CommentListAdapter.OnCommentItemClickListener() {
                    @Override
                    public void onCommentItem(int toID, String backTo) {
                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        if (mContext instanceof HomePageSearchResultActivity) {
                            HomePageSearchResultActivity.replyToId = toID;
                            HomePageSearchResultActivity.isComment = false;
                            HomePageSearchResultActivity.entity = entity;
                            HomePageSearchResultActivity.etToComment.requestFocus();
                            HomePageSearchResultActivity.etToComment.setHint("回复：" + backTo);
                            HomePageSearchResultActivity.rvAddPicture.setVisibility(View.GONE);
                        } else if (mContext instanceof HomePageArticleActivity) {
                            HomePageArticleActivity.replyToId = toID;
                            HomePageArticleActivity.isComment = false;
                            HomePageArticleActivity.entity = entity;
                            HomePageArticleActivity.etToComment.requestFocus();
                            HomePageArticleActivity.etToComment.setHint("回复：" + backTo);
                            HomePageArticleActivity.rvAddPicture.setVisibility(View.GONE);
                        } else if (mContext instanceof CommunityDynamicDetailActivity) {
                            CommunityDynamicDetailActivity.replyToId = toID;
                            CommunityDynamicDetailActivity.isComment = false;
                            CommunityDynamicDetailActivity.entity = entity;
                            CommunityDynamicDetailActivity.etToComment.requestFocus();
                            CommunityDynamicDetailActivity.etToComment.setHint("回复：" + backTo);
                            CommunityDynamicDetailActivity.rvAddPicture.setVisibility(View.GONE);
                        }else if (mContext instanceof HomePageDateInfo) {
                            HomePageDateInfo.replyToId = toID;
                            HomePageDateInfo.isComment = false;
                            HomePageDateInfo.entity = entity;
                            HomePageDateInfo.etToComment.requestFocus();
                            HomePageDateInfo.etToComment.setHint("回复：" + backTo);
                            HomePageDateInfo.rvAddPicture.setVisibility(View.GONE);
                        }else if (mContext instanceof HomePageEquipmentInfo) {
                            HomePageEquipmentInfo.replyToId = toID;
                            HomePageEquipmentInfo.isComment = false;
                            HomePageEquipmentInfo.entity = entity;
                            HomePageEquipmentInfo.etToComment.requestFocus();
                            HomePageEquipmentInfo.etToComment.setHint("回复：" + backTo);
                            HomePageEquipmentInfo.rvAddPicture.setVisibility(View.GONE);
                        }
                    }
                });
            } else {
                rvDiscussCallBack.setVisibility(View.GONE);
            }
            tvTalkContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.OnItemClick(entity, tvMainUserName.getText().toString());
                }
            });
    }
    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void OnItemClick(HomePageCommentEntity.HomePage entity,String backTo);
    }

}
