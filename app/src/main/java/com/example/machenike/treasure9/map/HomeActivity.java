package com.example.machenike.treasure9.map;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.machenike.treasure9.MainActivity;
import com.example.machenike.treasure9.R;
import com.example.machenike.treasure9.UserPrefs;
import com.example.machenike.treasure9.account.AccountActivity;
import com.example.machenike.treasure9.commons.ActivityUtils;
import com.example.machenike.treasure9.treasure.list.TreasureListFragment;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fragment_container)
    FrameLayout mFragmentContainer;
    @BindView(R.id.navigation)
    NavigationView mNavigation;
    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;
    private Unbinder mUnbinder;
    private ActivityUtils mActivityUtils;
    private ImageView mUserIcon;
    private MapFragment mMapFragment;
    private TreasureListFragment mTreasureListFragment;
    private FragmentManager mSupportFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mUnbinder = ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);

        mSupportFragmentManager = getSupportFragmentManager();
        mMapFragment = (MapFragment) mSupportFragmentManager.findFragmentById(R.id.mapFragment);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //将DrawerLayout和Toolbar的状态进行同步
        actionBarDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        //给侧滑页面设置点击监听
        mNavigation.setNavigationItemSelectedListener(this);

        mUserIcon = (ImageView) mNavigation.getHeaderView(0).findViewById(R.id.iv_usericon);

        mUserIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivityUtils.startActivity(AccountActivity.class);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        String photo = UserPrefs.getInstance().getPhoto();
        if (photo!=null){
            Picasso.with(this).load(photo).into(mUserIcon);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
    //策划页面的点击监听
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_hide:
                mMapFragment.changeUiMode(2);
                break;
            case R.id.menu_my_list:
                mActivityUtils.showToast("我的列表");
                break;
            case R.id.menu_help:
                mActivityUtils.showToast("帮助");
                break;
            case R.id.menu_logout:
                UserPrefs.getInstance().clearUser();
                mActivityUtils.startActivity(MainActivity.class);
                finish();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    //当activity创建的时候执行，在activity的每一次生命周期中只执行一次
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home,menu);
        return super.onCreateOptionsMenu(menu);
    }
    //当你执行invalidateOptionsMenu()就会执行一次
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.getItem(0);
        if (mTreasureListFragment!=null&&mTreasureListFragment.isAdded()){
            item.setIcon(R.drawable.ic_map);
        }else {
            item.setIcon(R.drawable.ic_view_list);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_toggle:
                showTreasureList();
                //唤起onPrepareOptionsMenu
                invalidateOptionsMenu();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showTreasureList() {

        if (mTreasureListFragment!=null&&mTreasureListFragment.isAdded()){
            // 将ListFragment弹出回退栈
            mSupportFragmentManager.popBackStack();
            mSupportFragmentManager.beginTransaction().remove(mTreasureListFragment).commit();
            return;
        }
        mTreasureListFragment = new TreasureListFragment();
        mSupportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container,mTreasureListFragment)
                .addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        if(mMapFragment.isNomalMode()){
            super.onBackPressed();
        }else {
            mMapFragment.changeUiMode(0);
        }
    }
}
