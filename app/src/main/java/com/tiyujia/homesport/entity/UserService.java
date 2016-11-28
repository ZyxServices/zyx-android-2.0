package com.tiyujia.homesport.entity;

import rx.Observable;

/**
 * Created by zzqybyb19860112 on 2016/11/10.1
 */

    public interface UserService {
        public Observable<Result> getVerifyCode(String phone);
        public Observable<Result<VerifyCode>> verifyPhone(String phone, String code);
        public Observable<Result> register(String phone, String pwd, String nickname, String avatar);
        public Observable<Result> retrievePassword(String phone, String pwd, String re_pwd);
        public Observable<Result<UserData>> login(String phone, String pwd);
}
