package com.tiyujia.homesport.common.homepage.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.entity.ArticleModel;
import com.tiyujia.homesport.common.homepage.entity.CurseModel;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.util.PicUtil;
import com.tiyujia.homesport.util.PicassoUtil;
import com.tiyujia.homesport.util.StringUtil;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者: Cymbi on 2016/12/6 19:19.
 * 邮箱:928902646@qq.com
 */

public class HomePageArticleActivity extends ImmersiveActivity {
    @Bind(R.id.ivBack)    ImageView ivBack;
    @Bind(R.id.ivMenu)    ImageView ivMenu;
    @Bind(R.id.ivAvatar)    ImageView ivAvatar;
    @Bind(R.id.ivLv)    ImageView ivLv;
    @Bind(R.id.tvNickname)    TextView tvNickname;
    @Bind(R.id.tvTime)    TextView tvTime;
    @Bind(R.id.tv_not)    TextView tv_not;
    @Bind(R.id.tv_yes)    TextView tv_yes;
    @Bind(R.id.tvTitle)    TextView tvTitle;
    @Bind(R.id.webview)    WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_info);
        int courseId= getIntent().getIntExtra("id",0);
        OkGo.post(API.BASE_URL+"/v2/city/findCourseById")
                .tag(this)
                .params("courseId",courseId)
                .execute(new LoadCallback<ArticleModel>(this) {
                    @Override
                    public void onSuccess(ArticleModel articleModel, Call call, Response response) {
                        tvTitle.setText(articleModel.data.title);
                        WindowManager wm = (WindowManager) HomePageArticleActivity.this.getSystemService(Context.WINDOW_SERVICE);
                        int width = wm.getDefaultDisplay().getWidth();
                        if(width > 520){
                            webview.setInitialScale(160);
                        }else if(width > 450){
                            webview.setInitialScale(140);
                        }else if(width > 300){
                            webview.setInitialScale(120);
                        }else{
                            webview.setInitialScale(100);
                        }
                        webview.loadDataWithBaseURL(null, articleModel.data.content, "text/html", "utf-8", null);
                        webview.getSettings().setJavaScriptEnabled(true);
                        // 设置启动缓存 ;
                        webview.getSettings().setAppCacheEnabled(true);
                        webview.setWebChromeClient(new WebChromeClient());
                        webview.getSettings().setJavaScriptEnabled(true);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        showToast("网络连接失败");
                    }
                });

    }
}
