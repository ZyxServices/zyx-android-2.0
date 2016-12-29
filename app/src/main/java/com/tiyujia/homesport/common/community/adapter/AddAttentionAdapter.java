package com.tiyujia.homesport.common.community.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.community.activity.CommunityAddAttention;
import com.tiyujia.homesport.common.personal.model.AttentionModel;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.entity.LzyResponse;
import com.tiyujia.homesport.util.LvUtil;
import com.tiyujia.homesport.util.PicUtil;
import com.tiyujia.homesport.util.PicassoUtil;
import com.tiyujia.homesport.util.StringUtil;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者: Cymbi on 2016/11/15 11:58.1
 * 邮箱:928902646@qq.com
 */

public class AddAttentionAdapter extends BaseQuickAdapter<AttentionModel.AttentionList> implements Filterable {
    Context context;
    List<AttentionModel.AttentionList> values;
    List<AttentionModel.AttentionList> mCopyInviteMessages;
    List<AttentionModel.AttentionList> inviteMessages;

    public AddAttentionAdapter(Context context,List<AttentionModel.AttentionList> data) {
        super(R.layout.personal_attention_item, data);
        this.context=context;
        values=new ArrayList<>();
    }
    @Override
    protected void convert(BaseViewHolder baseViewHolder, final AttentionModel.AttentionList attentionList) {
        final Activity activity=(Activity) context;
        baseViewHolder.setText(R.id.tvNickname,attentionList.nickname)
                .setText(R.id.tvContent,attentionList.signature);
        ImageView ivAvatar=baseViewHolder.getView(R.id.ivAvatar);
        ImageView ivLv=baseViewHolder.getView(R.id.ivLv);
        final TextView tv_yes=baseViewHolder.getView(R.id.tv_yes);
        final TextView tv_not=baseViewHolder.getView(R.id.tv_not);
        tv_yes.setVisibility(View.VISIBLE);
        if(attentionList.level==null||attentionList.level.equals("null")){
            LvUtil.setLv(ivLv,"初学乍练");
        }else {
            LvUtil.setLv(ivLv,attentionList.level.pointDesc);
        }
        SharedPreferences share = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        final String mToken=share.getString("Token","");
        final int mUserId=share.getInt("UserId",0);
        if(TextUtils.isEmpty(attentionList.avatar)){
            ivAvatar.setImageResource(R.mipmap.pic_gray);
        }else {
            PicassoUtil.handlePic(context, PicUtil.getImageUrlDetail(context, StringUtil.isNullAvatar(attentionList.avatar), 320, 320), ivAvatar, 320, 320);
        }
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
                                    tv_not.setVisibility(View.VISIBLE);
                                    tv_yes.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });
        tv_not.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkGo.post(API.BASE_URL+"/v2/follow/unfollow")
                        .tag(this)
                        .params("token",mToken)
                        .params("fromUserId",mUserId)
                        .params("toUserId",attentionList.id)
                        .execute(new LoadCallback<LzyResponse>(activity) {
                            @Override
                            public void onSuccess(LzyResponse lzyResponse, Call call, Response response) {
                                if(lzyResponse.state==200){
                                    Toast.makeText(activity,"取消关注成功",Toast.LENGTH_SHORT).show();
                                    tv_not.setVisibility(View.GONE);
                                    tv_yes.setVisibility(View.VISIBLE);
                                }
                            }
                        });
            }
        });
    }
    public void setFriends(List<AttentionModel.AttentionList> data) {
        //复制数据
        mCopyInviteMessages = new ArrayList<>();
        this.mCopyInviteMessages.addAll(data);
        this.inviteMessages = data;
        this.notifyDataSetChanged();
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                //初始化过滤结果对象
                FilterResults results = new FilterResults();
                //假如搜索为空的时候，将复制的数据添加到原始数据，用于继续过滤操作
                if (results.values == null) {
                    values.clear();
                    values.addAll(mCopyInviteMessages);
                }
                //关键字为空的时候，搜索结果为复制的结果
                if (constraint == null || constraint.length() == 0) {
                    results.values = mCopyInviteMessages;
                    results.count = mCopyInviteMessages.size();
                } else {
                    String searchText= CommunityAddAttention.getSearchText();
                    String prefixString;
                    if (searchText.equals("")){
                        prefixString=searchText.toString();
                    }else {
                        prefixString= constraint.toString();
                    }
                    final int count = inviteMessages.size();
                    //用于存放暂时的过滤结果
                    final ArrayList<AttentionModel.AttentionList> newValues = new ArrayList<AttentionModel.AttentionList>();
                    for (int i = 0; i < count; i++) {
                        final AttentionModel.AttentionList value = inviteMessages.get(i);
                        String username = value.nickname;
                        // First match against the whole ,non-splitted value，假如含有关键字的时候，添加
                        if (username.contains(prefixString)) {
                            newValues.add(value);
                        } else {
                            //过来空字符开头
                            final String[] words = username.split(" ");
                            final int wordCount = words.length;
                            // Start at index 0, in case valueText starts with space(s)
                            for (int k = 0; k < wordCount; k++) {
                                if (words[k].contains(prefixString)) {
                                    newValues.add(value);
                                    break;
                                }
                            }
                        }
                    }
                    results.values = newValues;
                    results.count = newValues.size();
                }
                return results;//过滤结果
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                inviteMessages.clear();//清除原始数据
                inviteMessages.addAll((List<AttentionModel.AttentionList>) results.values);//将过滤结果添加到这个对象
                if (results.count > 0) {
                    notifyDataSetChanged();//有关键字的时候刷新数据
                } else {
                    //关键字不为零但是过滤结果为空刷新数据
                    if (constraint.length() != 0) {
                        notifyDataSetChanged();
                        return;
                    }
                    //加载复制的数据，即为最初的数据
                    setFriends(mCopyInviteMessages);
                }
            }
        };
    }
}
