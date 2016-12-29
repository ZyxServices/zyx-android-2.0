package com.tiyujia.homesport.common.community.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.NewBaseActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.community.adapter.AddAttentionAdapter;
import com.tiyujia.homesport.common.personal.model.AttentionModel;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.util.RefreshUtil;
import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者: Cymbi on 2016/11/16 17:38.1
 * 邮箱:928902646@qq.com
 */

public class CommunityAddAttention extends NewBaseActivity implements View.OnClickListener ,SwipeRefreshLayout.OnRefreshListener{

    @Bind(R.id.ivBack)              ImageView ivBack;
    @Bind(R.id.ivSearch)            ImageView ivSearch;
    @Bind(R.id.srlRefresh)          SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.recyclerView)        RecyclerView recyclerView;
    @Bind(R.id.tvTitle)             TextView tv_title;
    @Bind(R.id.tvSuggest)           TextView tvSuggest;
    @Bind(R.id.llSearchUser)        LinearLayout llSearchUser;
    @Bind(R.id.tvSearchUserClose)   TextView tvSearchUserClose;
    static EditText etSearchUser;
    private AddAttentionAdapter adapter;
    private String mToken;
    private int mUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attention);
        setInfo();
        etSearchUser= (EditText) llSearchUser.findViewById(R.id.etSearchUser);
        adapter =new AddAttentionAdapter(this,null);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        adapter.isFirstOnly(false);
        recyclerView.setAdapter(adapter);
        View view= LayoutInflater.from(CommunityAddAttention.this).inflate(R.layout.normal_empty_image_view,null);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(lp2);
        TextView tvEmptyText= (TextView) view.findViewById(R.id.text_empty);
        tvEmptyText.setText("暂无数据");
        adapter.setEmptyView(view);
        RefreshUtil.refresh(swipeRefresh,this);
        swipeRefresh.setOnRefreshListener(this);
        onRefresh();
        setListeners();
    }

    private void setListeners() {
        ivBack.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        tvSearchUserClose.setOnClickListener(this);
        etSearchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                s=s.toString().trim();
                adapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setInfo() {
        SharedPreferences share = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        mToken=share.getString("Token","");
        mUserId=share.getInt("UserId",0);
        tv_title.setText("添加关注");
    }
    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        switch (v.getId()){
            case R.id.ivBack:
                finish();
                break;
            case R.id.tvSearchUserClose:
                tvSuggest.setVisibility(View.VISIBLE);
                llSearchUser.setVisibility(View.GONE);
                ivSearch.setVisibility(View.VISIBLE);
                etSearchUser.clearFocus();
                etSearchUser.setText("");
                imm.hideSoftInputFromWindow(etSearchUser.getWindowToken(), 0);
                adapter.getFilter().filter("");
                break;
            case R.id.ivSearch:
                llSearchUser.setVisibility(View.VISIBLE);
                tvSuggest.setVisibility(View.GONE);
                ivSearch.setVisibility(View.GONE);
                etSearchUser.requestFocus();
                etSearchUser.setText("");
                imm.showSoftInput(etSearchUser,InputMethodManager.SHOW_FORCED);
                break;
        }
    }
    public static String getSearchText(){
        if (etSearchUser==null){
            return "";
        }else {
            return etSearchUser.getText().toString().trim().equals("")?"":etSearchUser.getText().toString().trim();
        }
    }
    @Override
    public void onRefresh() {
        OkGo.post(API.BASE_URL+"/v2/follow/list")
                .tag(this)
                .params("token",mToken)
                .params("loginUserId",mUserId)
                .execute(new LoadCallback<AttentionModel>(this) {
                    @Override
                    public void onSuccess(AttentionModel attentionModel, Call call, Response response) {
                        if(attentionModel.state==200){
                            adapter.setNewData(attentionModel.data);
                            adapter.setFriends(attentionModel.data);
                            Log.i("tag","size-------------"+attentionModel.data.size());
                        }
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                    }
                    @Override
                    public void onAfter(@Nullable AttentionModel attentionModel, @Nullable Exception e) {
                        super.onAfter(attentionModel, e);
                        adapter.removeAllFooterView();
                        setRefreshing(false);
                    }
                });
    }
    public void setRefreshing(final boolean refreshing) {
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(refreshing);
            }
        });
    }
}
