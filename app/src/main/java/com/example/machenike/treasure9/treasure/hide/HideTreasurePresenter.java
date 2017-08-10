package com.example.machenike.treasure9.treasure.hide;

import android.util.Log;

import com.example.machenike.treasure9.net.NetClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by MACHENIKE on 2017/8/10.
 */

public class HideTreasurePresenter {

    private HideTreasureView mHideTreasureView;

    public HideTreasurePresenter(HideTreasureView hideTreasureView) {
        mHideTreasureView = hideTreasureView;
    }

    public void hideTreasure(final HideTreasure hideTreasure){
        //显示进度条
        mHideTreasureView.showProgress();
        NetClient.getInstance().getNetRequest().hideTreasure(hideTreasure).enqueue(new Callback<HideTreasureResult>() {
            @Override
            public void onResponse(Call<HideTreasureResult> call, Response<HideTreasureResult> response) {
                //隐藏进度条
                mHideTreasureView.hideProgress();
                if (response.isSuccessful()){
                    HideTreasureResult hideTreasureResult = response.body();
                    if (hideTreasureResult==null){
                        //显示未知错误
                        mHideTreasureView.showMessage("未知错误");
                        return;
                    }

                    //上传成功，finish掉本界面，返回上一届面
                    Log.e("==========",hideTreasureResult.getMsg());
                    mHideTreasureView.backHome();
                }
            }

            @Override
            public void onFailure(Call<HideTreasureResult> call, Throwable t) {
                //隐藏进度条
                mHideTreasureView.hideProgress();
                //显示上传失败
                mHideTreasureView.showMessage("上传失败");
            }
        });
    }
}
