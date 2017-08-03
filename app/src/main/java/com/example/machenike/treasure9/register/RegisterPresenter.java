package com.example.machenike.treasure9.register;

import com.example.machenike.treasure9.User;
import com.example.machenike.treasure9.UserPrefs;
import com.example.machenike.treasure9.net.NetClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by MACHENIKE on 2017/8/3.
 */

public class RegisterPresenter {
    private RegisterView mRegisterView;

    public RegisterPresenter(RegisterView registerView) {
        mRegisterView = registerView;
    }

    public void register(User user){
        //显示进度条
        mRegisterView.showProgress();
        NetClient.getInstance().getNetRequest().register(user).enqueue(new Callback<RegisterResult>() {
            @Override
            public void onResponse(Call<RegisterResult> call, Response<RegisterResult> response) {
                //隐藏进度条
                mRegisterView.hideProgress();
                if (response.isSuccessful()){
                    RegisterResult registerResult = response.body();
                    if (registerResult==null){
                        //未知错误
                        mRegisterView.showMessage("未知错误");
                        return;
                    }
                    switch (registerResult.getErrcode()){
                        case 1:
                            //注册成功
                            //缓存TokenId
                            UserPrefs.getInstance().setTokenid(registerResult.getTokenid());
                            //跳转到HomeActivity
                            mRegisterView.NavigateToHomeActivity();
                            break;
                        default:
                            //显示信息
                            mRegisterView.showMessage(registerResult.getErrmsg());
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterResult> call, Throwable t) {
                //隐藏进度条
                mRegisterView.hideProgress();
                //注册失败
                mRegisterView.showMessage("注册失败"+t.getMessage());
            }
        });
    }
}
