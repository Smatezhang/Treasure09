package com.example.machenike.treasure9.treasure.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.machenike.treasure9.custom.TreasureView;
import com.example.machenike.treasure9.treasure.Treasure;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MACHENIKE on 2017/8/11.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    private List<Treasure> mTreasureList = new ArrayList<>();

    public MyAdapter(List<Treasure> treasureList) {
        mTreasureList = treasureList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(new TreasureView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Treasure treasure = mTreasureList.get(position);
        holder.mTreasureView.bindView(treasure);
    }

    @Override
    public int getItemCount() {
        return mTreasureList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        private TreasureView mTreasureView;
        public MyViewHolder(View itemView) {
            super(itemView);
            mTreasureView = (TreasureView) itemView;
        }
    }
}
