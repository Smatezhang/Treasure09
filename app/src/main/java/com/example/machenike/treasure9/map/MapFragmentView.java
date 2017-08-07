package com.example.machenike.treasure9.map;

import com.example.machenike.treasure9.treasure.Treasure;

import java.util.List;

/**
 * Created by MACHENIKE on 2017/8/7.
 */

public interface MapFragmentView {
    void showMessage(String message);

    void setTreasureData(List<Treasure> treasureList);
}
