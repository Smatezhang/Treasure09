package com.example.machenike.treasure9.net;

import com.example.machenike.treasure9.User;
import com.example.machenike.treasure9.login.UserResult;
import com.example.machenike.treasure9.register.RegisterResult;
import com.example.machenike.treasure9.treasure.Area;
import com.example.machenike.treasure9.treasure.Treasure;

import java.util.List;

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

    //注册
    @POST("/Handler/UserHandler.ashx?action=register")
    Call<RegisterResult> register(@Body User user);

    //获取宝藏信息
    @POST("/Handler/\n" +
            "TreasureHandler.ashx?action=show")
    Call<List<Treasure>> getTreasure(@Body Area area);
}
