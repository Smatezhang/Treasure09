package com.example.machenike.treasure9.net;

import com.example.machenike.treasure9.User;
import com.example.machenike.treasure9.login.UserResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by MACHENIKE on 2017/8/2.
 * 构建网络请求的接口
 */

public interface NetRequest {

    //登陆
    @POST("/Handler/UserHandler.ashx?action=login")
    Call<UserResult> login(@Body User user);
}
