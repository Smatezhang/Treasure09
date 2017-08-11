package com.example.machenike.treasure9.treasure.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.machenike.treasure9.R;
import com.example.machenike.treasure9.treasure.Treasure;
import com.example.machenike.treasure9.treasure.TreasureRepo;
import com.example.machenike.treasure9.treasure.detail.TreasureDetailActivity;

/**
 * Created by MACHENIKE on 2017/8/11.
 */

public class TreasureListFragment extends Fragment{

    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRecyclerView = new RecyclerView(getContext());
        //设置布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //设置Item之间的间隔
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL));
        //设置背景
        mRecyclerView.setBackgroundResource(R.mipmap.scale_bg);
        //设置适配器
        MyAdapter myAdapter = new MyAdapter(TreasureRepo.getInstance().getTreasure());
        myAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Treasure treasure) {
                //跳转到宝藏详情页面
                //Toast.makeText(getContext(),"好痛！别点",Toast.LENGTH_SHORT).show();
                TreasureDetailActivity.open(getContext(),treasure);

            }
        });
        mRecyclerView.setAdapter(myAdapter);
        return mRecyclerView;
    }
}
