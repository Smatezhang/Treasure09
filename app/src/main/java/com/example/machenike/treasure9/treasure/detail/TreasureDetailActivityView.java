package com.example.machenike.treasure9.treasure.detail;

import java.util.List;

/**
 * Created by MACHENIKE on 2017/8/9.
 */

public interface TreasureDetailActivityView {
    void showMessage(String message);

    void setTreasureDetailResultData(List<TreasureDetailResult> list);
}
