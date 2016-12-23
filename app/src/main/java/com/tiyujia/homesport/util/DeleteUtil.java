package com.tiyujia.homesport.util;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.common.community.fragment.AttentionFragment;
import com.tiyujia.homesport.common.community.fragment.RecommendFragment;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.entity.LzyResponse;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by zzqybyb19860112 on 2016/12/23.
 */

public class DeleteUtil {
    public static void handleDeleteActiveTransaction(final Activity activity, String token, int activityId, int userId){
       String uri= API.BASE_URL+"/v2/activity/delActivityById";
        OkGo.post(uri)
                .params("token",token)
                .params("activityId",activityId)
                .params("userId",userId)
                .execute(new LoadCallback<LzyResponse>(activity) {
                    @Override
                    public void onSuccess(LzyResponse lzyResponse, Call call, Response response) {
                        if(lzyResponse.state==200){
                            activity.finish();
                        }
                    }
                });
    }
    public static void handleDeleteOtherTransaction(final Activity activity,String baseUri, String token, int modelId, int loginUserId){
        String toast="";
        if (baseUri.contains("concern")){
            toast="删除动态成功";
        }else {
            toast="装备秀删除成功";
        }
        final String finalToast = toast;
        OkGo.delete(API.BASE_URL+baseUri+token+"/"+modelId+"/"+loginUserId)
                .execute(new LoadCallback<LzyResponse>(activity) {
                    @Override
                    public void onSuccess(LzyResponse lzyResponse, Call call, Response response) {
                        if(lzyResponse.state==200){
                            Toast.makeText(activity, finalToast,Toast.LENGTH_SHORT).show();
                            activity.finish();
                            AttentionFragment.isFirstIn=false;
                            RecommendFragment.isFirstIn=false;
                        }
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                    }
                });
    }
}
