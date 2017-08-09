package com.example.machenike.treasure9.custom;

import android.content.Context;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.machenike.treasure9.R;
import com.example.machenike.treasure9.map.MapFragment;
import com.example.machenike.treasure9.treasure.Treasure;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MACHENIKE on 2017/8/9.
 */

public class TreasureView extends RelativeLayout {
    @BindView(R.id.tv_treasureTitle)
    TextView mTvTreasureTitle;
    @BindView(R.id.tv_distance)
    TextView mTvDistance;
    @BindView(R.id.linear_title)
    LinearLayout mLinearTitle;
    @BindView(R.id.tv_treasureLocation)
    TextView mTvTreasureLocation;
    @BindView(R.id.iv_arrow)
    ImageView mIvArrow;
    @BindView(R.id.cardView)
    CardView mCardView;

    //在代码中使用的
    public TreasureView(Context context) {
        super(context);
        init();
    }

    //在XML中使用，但不指定Style
    public TreasureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    //在XML中使用，指定Style
    public TreasureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_treasure, this, true);
        ButterKnife.bind(this);
    }

    public void bindView(Treasure treasure){
        String title = treasure.getTitle();
        String location = treasure.getLocation();

        double latitude = treasure.getLatitude();
        double longitude = treasure.getLongitude();
        LatLng treasureLocation = new LatLng(latitude, longitude);
        LatLng mCurrentLoaction = MapFragment.getLocation();

        double distance = DistanceUtil.getDistance(treasureLocation, mCurrentLoaction);


        DecimalFormat decimalFormat = new DecimalFormat("#0.00"); //注意导包
        String s = decimalFormat.format(distance / 1000) + "km";

        mTvTreasureTitle.setText(title);
        mTvTreasureLocation.setText(location);
        mTvDistance.setText(s);
    }


}
