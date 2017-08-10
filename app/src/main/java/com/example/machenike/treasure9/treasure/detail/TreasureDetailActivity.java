package com.example.machenike.treasure9.treasure.detail;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.example.machenike.treasure9.MainActivity;
import com.example.machenike.treasure9.R;
import com.example.machenike.treasure9.commons.ActivityUtils;
import com.example.machenike.treasure9.custom.TreasureView;
import com.example.machenike.treasure9.map.MapFragment;
import com.example.machenike.treasure9.treasure.Treasure;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TreasureDetailActivity extends AppCompatActivity implements TreasureDetailActivityView{

    private static final String TREASURE_KEY = "treasure_key";
    @BindView(R.id.iv_navigation)
    ImageView mIvNavigation;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.frameLayout)
    FrameLayout mFrameLayout;
    @BindView(R.id.detail_treasure)
    TreasureView mDetailTreasure;
    @BindView(R.id.tv_detail_description)
    TextView mTvDetailDescription;
    private Treasure mTreasure;
    private BaiduMap mBaiduMap;
    private ActivityUtils mActivityUtils;

    public static void open(Context context, Treasure treasure) {
        Intent intent = new Intent(context, TreasureDetailActivity.class);
        intent.putExtra(TREASURE_KEY, treasure);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treasure_detail);
        ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);

        //获取宝物信息
        Intent intent = getIntent();
        mTreasure = (Treasure) intent.getSerializableExtra(TREASURE_KEY);


        setSupportActionBar(mToolbar);

        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(mTreasure.getTitle());
        }
        //初始化地图
        initMapView();

        mDetailTreasure.bindView(mTreasure);

        TreasureDetail treasureDetail = new TreasureDetail(mTreasure.getId());
        new TreasureDetailActivityPresenter(this).getTreasureDecribe(treasureDetail);

    }

    private void initMapView() {
        double latitude = mTreasure.getLatitude();
        double longitude = mTreasure.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        MapStatus mapStatus = new MapStatus.Builder()
                .target(latLng)
                .zoom(19)
                .rotate(0f)
                .overlook(-20f)
                .build();
        BaiduMapOptions options = new BaiduMapOptions();
        options.mapStatus(mapStatus);
        options.scaleControlEnabled(false); //隐藏比例尺
        options.zoomControlsEnabled(false); //隐藏缩放控件
        options.compassEnabled(false);
        options.overlookingGesturesEnabled(false);
        options.rotateGesturesEnabled(false);
        options.scrollGesturesEnabled(false);
        options.zoomGesturesEnabled(false);
        MapView mapView = new MapView(this, options);

        mBaiduMap = mapView.getMap();

        mFrameLayout.addView(mapView);

        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.treasure_expanded);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.anchor(0.5f,0.5f);
        markerOptions.rotate(0);
        markerOptions.icon(bitmapDescriptor);
        markerOptions.position(latLng);
        mBaiduMap.addOverlay(markerOptions);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.iv_navigation})
    public void showPupMenu(){
        PopupMenu popupMenu = new PopupMenu(this, mIvNavigation);
        popupMenu.inflate(R.menu.menu_navigation);

        popupMenu.setOnMenuItemClickListener(mOnMenuItemClickListener);
        popupMenu.show();
    }


    private PopupMenu.OnMenuItemClickListener mOnMenuItemClickListener = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            LatLng startPoint = MapFragment.getLocation();
            String startAdress = MapFragment.getAdress();

            LatLng endPoint = new LatLng(mTreasure.getLatitude(), mTreasure.getLongitude());
            String endAdress = mTreasure.getLocation();
            switch (item.getItemId()){
                case R.id.walking_navi:
                    //点击开启步行导航
                    openWalkingNavi(startPoint,startAdress,endPoint,endAdress);
                    break;
                case R.id.biking_navi:
                    //点击开启骑行导航
                    openBikingNavi(startPoint,startAdress,endPoint,endAdress);
                    break;
            }
            return false;
        }
    };

    private void openBikingNavi(LatLng startPoint, String startAdress, LatLng endPoint, String endAdress) {
        NaviParaOption naviParaOption = new NaviParaOption()
                .startName(startAdress)
                .startPoint(startPoint)
                .endName(endAdress)
                .endPoint(endPoint);
        boolean b = BaiduMapNavigation.openBaiduMapBikeNavi(naviParaOption, this);
        if (!b){
            new AlertDialog.Builder(this)
                    .setTitle("骑行导航")
                    .setMessage("系统检测到您未安装百度地图或百度地图版本过低，是否下载最新的百度地图？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            OpenClientUtil.getLatestBaiduMapApp(TreasureDetailActivity.this);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }
    }

    //开启步行导航
    private void openWalkingNavi(LatLng startPoint, String startAdress, LatLng endPoint, String endAdress) {
        NaviParaOption naviParaOption = new NaviParaOption()
                .startName(startAdress)
                .startPoint(startPoint)
                .endName(endAdress)
                .endPoint(endPoint);

        boolean b = BaiduMapNavigation.openBaiduMapWalkNavi(naviParaOption, this);
        if (!b){
            BaiduMapNavigation.openWebBaiduMapNavi(naviParaOption,this);
        }
    }



    //----------------------------实现自视图接口上的方法----------------------------
    @Override
    public void showMessage(String message) {
        mActivityUtils.showToast(message);
    }

    @Override
    public void setTreasureDetailResultData(List<TreasureDetailResult> list) {
        if (list.size()>=1){
            mTvDetailDescription.setText(list.get(0).description);
            return;
        }
        mTvDetailDescription.setText("没有描述");
    }
}
