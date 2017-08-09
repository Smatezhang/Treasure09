package com.example.machenike.treasure9.treasure.detail;

import com.example.machenike.treasure9.net.NetClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by MACHENIKE on 2017/8/9.
 */

public class TreasureDetailActivityPresenter {
    private TreasureDetailActivityView mView;

    public TreasureDetailActivityPresenter(TreasureDetailActivityView view) {
        mView = view;
    }

    public void getTreasureDecribe(TreasureDetail treasureDetail){
        NetClient.getInstance().getNetRequest().getTreasureDetail(treasureDetail).enqueue(new Callback<List<TreasureDetailResult>>() {
            @Override
            public void onResponse(Call<List<TreasureDetailResult>> call, Response<List<TreasureDetailResult>> response) {
                if (response.isSuccessful()){
                    List<TreasureDetailResult> treasureDetailResultList = response.body();
                    if (treasureDetailResultList==null){
                        mView.showMessage("未知错误");
                        return;
                    }

                    mView.setTreasureDetailResultData(treasureDetailResultList);
                }
            }

            @Override
            public void onFailure(Call<List<TreasureDetailResult>> call, Throwable t) {
                mView.showMessage("请求失败");
            }
        });
    }
}
