package com.example.machenike.treasure9.map;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.machenike.treasure9.MainActivity;
import com.example.machenike.treasure9.R;
import com.example.machenike.treasure9.UserPrefs;
import com.example.machenike.treasure9.commons.ActivityUtils;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mUnbinder = ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);

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
                // TODO: 2017/8/3 跳转到个人信息界面
                mActivityUtils.showToast("跳转到个人信息界面");
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
                mActivityUtils.showToast("埋藏宝藏");
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
}
