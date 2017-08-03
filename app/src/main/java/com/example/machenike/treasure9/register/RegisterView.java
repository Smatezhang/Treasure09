package com.example.machenike.treasure9.register;

/**
 * Created by MACHENIKE on 2017/8/3.
 */

public interface RegisterView {

    //显示进度条
    void showProgress();
    //隐藏进度条
    void hideProgress();
    //显示信息
    void showMessage(String message);
    //跳转到Home Activity
    void NavigateToHomeActivity();
}
