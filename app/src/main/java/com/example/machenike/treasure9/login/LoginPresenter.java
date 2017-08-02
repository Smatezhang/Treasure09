package com.example.machenike.treasure9.login;

import com.example.machenike.treasure9.User;
import com.example.machenike.treasure9.UserPrefs;
import com.example.machenike.treasure9.net.NetClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by MACHENIKE on 2017/8/2.
 */

public class LoginPresenter {
    private LoginView mLoginView;

    public LoginPresenter(LoginView loginView) {
        mLoginView = loginView;
    }

    //登陆业务
    public void login(User user){
        //显示进度条
        mLoginView.showProgress();
        NetClient.getInstance().getNetRequest().login(user).enqueue(new Callback<UserResult>() {
            @Override
            public void onResponse(Call<UserResult> call, Response<UserResult> response) {
                //隐藏进度条
                mLoginView.hideProgress();
                if (response.isSuccessful()){
                    UserResult userResult = response.body();
                    if (userResult==null){
                        //显示信息，未知错误
                        mLoginView.showMessage("未知错误！");
                        return;
                    }
                    if (userResult.getErrcode()!=1){
                        //显示信息
                        mLoginView.showMessage(userResult.getErrmsg());
                        return;
                    }

                    String headpic = userResult.getHeadpic();
                    int tokenid = userResult.getTokenid();
                    //缓存头像
                    UserPrefs.getInstance().setPhoto(NetClient.BASE_URL+headpic);
                    //缓存TokenId
                    UserPrefs.getInstance().setTokenid(tokenid);
                    //跳转到HomeActivity
                    mLoginView.navigateToHomeActivity();
                }
            }

            @Override
            public void onFailure(Call<UserResult> call, Throwable t) {
                //隐藏进度条
                mLoginView.hideProgress();
                //显示错误信息
                mLoginView.showMessage("请求失败"+t.getMessage());
            }
        });
    }
}
