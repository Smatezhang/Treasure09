package com.example.machenike.treasure9.account;

import android.util.Log;

import com.example.machenike.treasure9.UserPrefs;
import com.example.machenike.treasure9.net.NetClient;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

/**
 * Created by MACHENIKE on 2017/8/14.
 */

public class AccountPresenter {

    private AccountView mAccountView;

    public AccountPresenter(AccountView accountView) {
        mAccountView = accountView;
    }

    public void uploadHeadIcon(File file){
        //显示进度条
        mAccountView.showProgress();
        RequestBody requestBody = RequestBody.create(null, file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("上传头像", "icon.jpg", requestBody);
        NetClient.getInstance().getNetRequest().uploadImage(part).enqueue(new Callback<UploadResult>() {
            @Override
            public void onResponse(Call<UploadResult> call, Response<UploadResult> response) {
                //隐藏进度条
                mAccountView.hideProgress();
                if (response.isSuccessful()){
                    final UploadResult uploadResult = response.body();
                    if (uploadResult==null){
                        //未知错误
                        mAccountView.showMessage("未知错误");
                        return;
                    }

                    if (uploadResult.getCount()==0){
                        //图片上传异常
                        Log.e("TAG","图片上传异常");
                        mAccountView.showMessage(uploadResult.getMsg());
                        return;
                    }
                    Log.e("TAG","图片上传成功");
                    int tokenid = UserPrefs.getInstance().getTokenid();
                    final String url = uploadResult.getUrl();
                    final String photo = url.substring(url.lastIndexOf("/")+1);
                    Update update = new Update(tokenid, photo);
                    mAccountView.showProgress();
                    NetClient.getInstance().getNetRequest().updateImage(update).enqueue(new Callback<UpdateResult>() {
                        @Override
                        public void onResponse(Call<UpdateResult> call, Response<UpdateResult> response) {
                            mAccountView.hideProgress();
                            if (response.isSuccessful()){
                                UpdateResult updateResult = response.body();
                                if (updateResult==null){
                                    //未知错误
                                    mAccountView.showMessage("未知错误");
                                    return;
                                }

                                if (updateResult.getCode()!=1){
                                    //打印错误信息
                                    Log.e("TAG","图片更新成功");
                                    mAccountView.showMessage(updateResult.getMsg());
                                    return;
                                }
                                //缓存头像
                                UserPrefs.getInstance().setPhoto(NetClient.BASE_URL+url);
                                //更新头像
                                mAccountView.updatePhoto(NetClient.BASE_URL+url);

                            }
                        }

                        @Override
                        public void onFailure(Call<UpdateResult> call, Throwable t) {
                            mAccountView.hideProgress();
                            mAccountView.showMessage("onFailure");
                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<UploadResult> call, Throwable t) {
                mAccountView.hideProgress();
                mAccountView.showMessage("onFailure");
            }
        });
    }
}
