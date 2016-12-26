package com.tiyujia.homesport.util;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.lzy.okgo.OkGo;
import com.tiyujia.homesport.entity.LoadCallback;
import com.tiyujia.homesport.entity.LzyResponse;
import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by zzqybyb19860112 on 2016/12/23.
 */

public class FollowUtil {
    public static void handleFollowTransaction(final Activity activity, String uri, String token, int fromUserId, int toUserId, final TextView view1,final TextView view2){
        String []tempStrs=uri.split("/");
        String toast="";
        String lastStr=tempStrs[tempStrs.length-1];
        if (lastStr.equals("unfollow")){
            toast="取消关注成功";
        }else {
            toast="添加关注成功";
        }
        final String finalToast = toast;
        OkGo.post(uri)
                .params("token",token)
                .params("fromUserId",fromUserId)
                .params("toUserId",toUserId)
                .execute(new LoadCallback<LzyResponse>(activity) {
                    @Override
                    public void onSuccess(LzyResponse lzyResponse, Call call, Response response) {
                        if(lzyResponse.state==200){
                            Toast.makeText(activity, finalToast,Toast.LENGTH_SHORT).show();
                            view1.setVisibility(View.GONE);
                            view2.setVisibility(View.VISIBLE);
                            view1.invalidate();
                            view2.invalidate();
                        }
                    }
                });
    }
}
