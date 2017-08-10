package com.example.machenike.treasure9.treasure.hide;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.baidu.mapapi.model.LatLng;
import com.example.machenike.treasure9.R;
import com.example.machenike.treasure9.UserPrefs;
import com.example.machenike.treasure9.commons.ActivityUtils;
import com.example.machenike.treasure9.treasure.TreasureRepo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HideTreasureActivity extends AppCompatActivity implements HideTreasureView{

    private static final String TREASURE_TITLE = "treasure_title";
    private static final String TREASURE_LOCATION = "treasure_location";
    private static final String TREASURE_ADRESS = "treasure_adress";
    @BindView(R.id.hide_send)
    ImageView mHideSend;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_description)
    EditText mEtDescription;
    private ProgressDialog mProgressDialog;
    private ActivityUtils mActivityUtils;
    private String mTitle;
    private LatLng mLocation;
    private String mAdress;

    public static void open(Context context, String title, LatLng currentLocation,String adress) {

        Intent intent = new Intent(context, HideTreasureActivity.class);
        intent.putExtra(TREASURE_TITLE, title);
        intent.putExtra(TREASURE_LOCATION,currentLocation);
        intent.putExtra(TREASURE_ADRESS,adress);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hide_treasure);
        ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);

        Intent intent = getIntent();
        mTitle = intent.getStringExtra(TREASURE_TITLE);
        mLocation = intent.getParcelableExtra(TREASURE_LOCATION);
        mAdress = intent.getStringExtra(TREASURE_ADRESS);
        setSupportActionBar(mToolbar);

        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(mTitle);
        }
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

    @OnClick({R.id.hide_send})
    public void hideTreasure(){
        String description = mEtDescription.getText().toString();
        int tokenid = UserPrefs.getInstance().getTokenid();
        HideTreasure hideTreasure = new HideTreasure();
        hideTreasure.setAltitude(0);
        hideTreasure.setDescription(description);
        hideTreasure.setLatitude(mLocation.latitude);
        hideTreasure.setLongitude(mLocation.longitude);
        hideTreasure.setTitle(mTitle);
        hideTreasure.setTokenId(tokenid);
        hideTreasure.setLocation(mAdress);
        new HideTreasurePresenter(this).hideTreasure(hideTreasure);
    }
//---------------------------实现自视图接口的方法---------------------------------
    @Override
    public void showProgress() {
        mProgressDialog = ProgressDialog.show(this, "上传宝藏", "宝藏上传中...");
    }

    @Override
    public void hideProgress() {
        mProgressDialog.dismiss();
    }

    @Override
    public void showMessage(String message) {
        mActivityUtils.showToast(message);
    }

    @Override
    public void backHome() {
        finish();
        // 清除缓存 : 为了返回到之前的页面重新去请求数据
        TreasureRepo.getInstance().clear();
    }
}
