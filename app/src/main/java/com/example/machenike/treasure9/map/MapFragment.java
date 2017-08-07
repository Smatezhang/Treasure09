package com.example.machenike.treasure9.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.machenike.treasure9.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by MACHENIKE on 2017/8/3.
 */

public class MapFragment extends Fragment {
    @BindView(R.id.iv_located)
    ImageView mIvLocated;
    @BindView(R.id.btn_HideHere)
    Button mBtnHideHere;
    @BindView(R.id.centerLayout)
    RelativeLayout mCenterLayout;
    @BindView(R.id.iv_scaleUp)
    ImageView mIvScaleUp;
    @BindView(R.id.iv_scaleDown)
    ImageView mIvScaleDown;
    @BindView(R.id.tv_located)
    TextView mTvLocated;
    @BindView(R.id.tv_satellite)
    TextView mTvSatellite;
    @BindView(R.id.tv_compass)
    TextView mTvCompass;
    @BindView(R.id.ll_locationBar)
    LinearLayout mLlLocationBar;
    @BindView(R.id.tv_currentLocation)
    TextView mTvCurrentLocation;
    @BindView(R.id.iv_toTreasureInfo)
    ImageView mIvToTreasureInfo;
    @BindView(R.id.et_treasureTitle)
    EditText mEtTreasureTitle;
    @BindView(R.id.cardView)
    CardView mCardView;
    @BindView(R.id.layout_bottom)
    FrameLayout mLayoutBottom;
    @BindView(R.id.map_frame)
    FrameLayout mMapFrame;
    Unbinder unbinder;
    private BaiduMap mBaiduMap;
    private LatLng mCurrentLocation;
    private boolean isFirst = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, null);

        return view;
    }

    //当onCreateView执行完毕之后执行
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        //初始化地图相关
        initView();
        //初始化位置相关
        initLocation();
    }
    //初始化位置相关
    private void initLocation() {
        //激活百度地图图层的定位功能
        mBaiduMap.setMyLocationEnabled(true);
        //第一步，初始化LocationClient类
        LocationClient locationClient = new LocationClient(getContext().getApplicationContext());
        //第二步，配置定位SDK参数
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll"); //设置默认坐标系，默认GCJ02，误差大
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        locationClient.setLocOption(option);

        //第三步，注册位置监听
        locationClient.registerLocationListener(mBDLocationListener);

        //第四补，开启定位
        locationClient.start();


    }

    private BDLocationListener mBDLocationListener = new BDLocationListener() {



        //获得位置后执行
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            double latitude = bdLocation.getLatitude();//纬度
            double longitude = bdLocation.getLongitude();//经度

            mCurrentLocation = new LatLng(latitude, longitude);
            String addrStr = bdLocation.getAddrStr();//位置描述
            Log.e("TAG","当前位于:"+addrStr+"经纬度是"+longitude+":"+latitude);


            MyLocationData myLocationData = new MyLocationData.Builder()
                    .accuracy(100f)//精度
                    .latitude(latitude)
                    .longitude(longitude)
                    .build();
            mBaiduMap.setMyLocationData(myLocationData);

            if (isFirst){
                moveToLocation();
                isFirst = false;
            }

        }
    };

    private void initView() {

        MapStatus mapStatus = new MapStatus.Builder()
                .overlook(0f)
                .rotate(0f)
                .zoom(11)
                .build();

        BaiduMapOptions options = new BaiduMapOptions();
        options.mapStatus(mapStatus);
        options.scaleControlEnabled(false); //隐藏比例尺
        options.zoomControlsEnabled(false); //隐藏缩放控件
        options.zoomGesturesEnabled(true);  //允许手势缩放
        //创建MapView对象
        MapView mapView = new MapView(getContext(), options);

        mBaiduMap = mapView.getMap();

        //将MapView填充到FrameLayout
        mMapFrame.addView(mapView,0);
    }

    //--------------------------------给地图上的控件添加点击事件------------------------
    //点击定位
    @OnClick({R.id.tv_located})
    public void moveToLocation(){

        MapStatus mapStatus = new MapStatus.Builder()
                .zoom(19)
                .target(mCurrentLocation)
                .build();
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
    }
    //切换地图类型
    @OnClick({R.id.tv_satellite})
    public void switchMapType(){
        //获取当前的地图类型
        int mapType = mBaiduMap.getMapType();
        //根据当前地图类型决定接下来要切换的地图类型
        mapType=mapType==BaiduMap.MAP_TYPE_NORMAL?BaiduMap.MAP_TYPE_SATELLITE:BaiduMap.MAP_TYPE_NORMAL;
        String msg = mapType==BaiduMap.MAP_TYPE_NORMAL?"卫星":"普通";

        mBaiduMap.setMapType(mapType);
        mTvSatellite.setText(msg);
    }
    //指南针
    @OnClick({R.id.tv_compass})
    public void compass(){
        boolean compassEnabled = mBaiduMap.getUiSettings().isCompassEnabled();
        mBaiduMap.getUiSettings().setCompassEnabled(!compassEnabled);
    }
    //地图放大缩小按钮
    @OnClick({R.id.iv_scaleUp,R.id.iv_scaleDown})
    public void switchMapScale(View view){
        switch (view.getId()){
            case R.id.iv_scaleUp:
                //放大
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomIn());
                break;
            case R.id.iv_scaleDown:
                //缩小
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomOut());
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
