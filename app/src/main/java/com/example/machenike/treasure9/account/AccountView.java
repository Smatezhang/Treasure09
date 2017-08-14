package com.example.machenike.treasure9.account;

/**
 * Created by MACHENIKE on 2017/8/14.
 */

public interface AccountView {

    void showProgress();

    void showMessage(String message);

    void hideProgress();

    void updatePhoto(String url);
}
