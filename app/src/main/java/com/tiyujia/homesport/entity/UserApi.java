package com.tiyujia.homesport.entity;

import java.util.HashMap;
import okhttp3.RequestBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import rx.Observable;
/**
 * Created by zzqybyb19860112 on 2016/11/10.
 */

public interface UserApi {
    /**
     * 获取验证码
     */
    @POST("/v2/sendCode") @FormUrlEncoded Observable<Result> getVerifyCode(
            @FieldMap HashMap<String, String> params);
    /**
     * 验证手机号
     */
    @POST("/v2/account/validate/code") @FormUrlEncoded Observable<Result<VerifyCode>> verifyPhone(
            @FieldMap HashMap<String, String> params);
    /**
     * 注册账号
     */
    @Multipart
    @POST("/v2/account/register2") Observable<Result> register(
            @PartMap HashMap<String, RequestBody> params);
    /**
     * 忘记密码
     */
    @POST("/v2/account/retrieve_secret") @FormUrlEncoded Observable<Result> retrievePassword(
            @FieldMap HashMap<String, String> params);
    /**
     * 登录
     */
    @POST("/v2/account/log_in") @FormUrlEncoded Observable<Result<UserData>> login(
            @FieldMap HashMap<String, String> params);
}

