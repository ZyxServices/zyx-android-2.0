package com.tiyujia.homesport.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.upload.Const.FileType;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class GetSignTask extends AsyncTask<Void, Integer, String> {

	private Context mContext;
	private String mBucket;
	private String mAppid;
	private String mFileId;
	private String result;
	private String sign;
	private String urlSign;
	private FileType mFileType;
	private OnGetSignListener mListener;
	private ProgressDialog mDialog = null;

	public interface OnGetSignListener {
		public void onSign(String sign);
	}

	public GetSignTask(Context context, String appid, FileType fileType, String bucket, String fileId, String url , OnGetSignListener listener) {
		mContext = context;
		mFileType = fileType;
		mFileId = fileId;
		mAppid = appid;
		mBucket = bucket;
		mListener = listener;
		urlSign ="http://web.file.myqcloud.com/tools/v1/getsign?secret_id=AKIDAsqjH35AoJNmzjB3lfVUIHLDMB18cXG8";
		long expired = 0;
		if(mFileId == null){
			expired = System.currentTimeMillis()/1000 + 24*3600*30;
		}
		urlSign += "&expired="+expired;
		urlSign += "&appid=299201";
		if(fileType == FileType.File) urlSign += "&bucket=bb";
		else if(fileType == FileType.Photo) urlSign += "&bucket=open";
		else urlSign += "&bucket=vincentsu";
		
		
		if(fileId != null){
			
			
			String path ="/" + appid + "/" + bucket + fileId;
			if(fileType == FileType.Photo) path = fileId;
			Log.w("test","path ="+path);
			
			urlSign += "&fileid=" + encodeUrl(path);
		}
		Log.w("test",urlSign);
		
		mDialog = new ProgressDialog(mContext);
		mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mDialog.setIndeterminate(false);
		mDialog.setCancelable(false);
	}

	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub

		try {
			URL url = new URL(urlSign);
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			InputStreamReader in = new InputStreamReader(urlConnection
					.getInputStream());
			BufferedReader buffer = new BufferedReader(in);
			String inputLine = null;
			
			String result = "";
			while((inputLine = buffer.readLine()) != null){
				result += inputLine;
			}
			
			JSONObject jsonData = new JSONObject(result);
			String data = jsonData.getString("data");
			JSONObject jsonSign = new JSONObject(data);
			sign = jsonSign.getString("sign");

		} catch (Exception e) {
			e.printStackTrace();
			Log.w("test","");
		}
		//mListener.onSign(sign);
		return sign;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		mDialog.dismiss();
		mListener.onSign(result);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		mDialog.show();
		mDialog.setMessage("");
	}
	
	public String encodeUrl(String signUrl){
		if(TextUtils.isEmpty(signUrl)){
			return signUrl;
		}
		try {
			return URLEncoder.encode(signUrl,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return signUrl;
	}
}
