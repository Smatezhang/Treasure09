package com.example.machenike.treasure9.treasure.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.machenike.treasure9.R;

/**
 * Created by MACHENIKE on 2017/8/11.
 */

public class TreasureListFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return new ImageButton(getContext());
    }
}
