package com.tiyujia.homesport;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.tiyujia.homesport.util.StatusBarUtil;

import butterknife.ButterKnife;
import rx.subscriptions.CompositeSubscription;

public abstract class Immersive extends AppCompatActivity implements View.OnSystemUiVisibilityChangeListener {


	public CompositeSubscription mCompositeSubscription;

	@SuppressWarnings("unchecked")
	public <T extends View> T findView(int id) {
		return (T) findViewById(id);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCompositeSubscription = new CompositeSubscription();
		StatusBarUtil.MIUISetStatusBarLightMode(getWindow(),true);
		//setStatusBarTransparent();
	}

	@Override
	public void setContentView(@LayoutRes int layoutResID) {
		super.setContentView(layoutResID);
		ButterKnife.bind(this);
	}

	@Override
	public void setContentView(View view) {
		super.setContentView(view);
		ButterKnife.bind(this);
	}

	@Override
	public void setContentView(View view, ViewGroup.LayoutParams params) {
		super.setContentView(view, params);
		ButterKnife.bind(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:// 点击返回图标事件
				finish();
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void setStatusBarTransparent(){
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
			//托盘重叠显示在Activity上
			View decorView = getWindow().getDecorView();
			int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
					|View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
			decorView.setSystemUiVisibility(uiOptions);
			decorView.setOnSystemUiVisibilityChangeListener(this);
			// 设置托盘透明
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			Log.d("CP_Common","VERSION.SDK_INT =" + Build.VERSION.SDK_INT);
		}else{
			Log.d("CP_Common", "SDK 小于19不设置状态栏透明效果");
		}
	}

	/** 子类可以重写改变状态栏颜色 */
	protected int setStatusBarColor() {
		return getColorPrimary();
	}

	/** 子类可以重写决定是否使用透明状态栏 */
	protected boolean translucentStatusBar() {
		return false;
	}
	/** 获取主题色 */
	public int getColorPrimary() {
		TypedValue typedValue = new TypedValue();
		getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
		return typedValue.data;
	}

	/** 获取深主题色 */
	public int getDarkColorPrimary() {
		TypedValue typedValue = new TypedValue();
		getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
		return typedValue.data;
	}

	/** 初始化 Toolbar */
	public void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, String title) {
		toolbar.setTitle(title);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnabled);
	}

	public void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, int resTitle) {
		initToolBar(toolbar, homeAsUpEnabled, getString(resTitle));
	}

	public void showToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	private ProgressDialog dialog;

	public void showLoading() {
		if (dialog != null && dialog.isShowing()) return;
		dialog = new ProgressDialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("请求网络中...");
		dialog.show();
	}

	public void dismissLoading() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}
	@Override
	public void onSystemUiVisibilityChange(int visibility) {

	}
}