package com.example.machenike.treasure9.account;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.machenike.treasure9.R;
import com.example.machenike.treasure9.UserPrefs;
import com.example.machenike.treasure9.commons.ActivityUtils;
import com.example.machenike.treasure9.custom.IconSelectedPopupWindow;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import org.hybridsquad.android.library.CropHandler;
import org.hybridsquad.android.library.CropHelper;
import org.hybridsquad.android.library.CropParams;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountActivity extends AppCompatActivity implements AccountView{

    @BindView(R.id.account_toolbar)
    Toolbar mAccountToolbar;
    @BindView(R.id.iv_usericon)
    CircularImageView mIvUsericon;
    @BindView(R.id.user_name)
    TextView mUserName;
    @BindView(R.id.linearLayout)
    RelativeLayout mLinearLayout;
    private IconSelectedPopupWindow mIconSelectedPopupWindow;
    private ActivityUtils mActivityUtils;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);

        setSupportActionBar(mAccountToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.account_msg);
        }
    }

    private IconSelectedPopupWindow.OnClickListener mOnClickListener = new IconSelectedPopupWindow.OnClickListener() {
        @Override
        public void toGalarry() {
            Intent intent = CropHelper.buildCropFromGalleryIntent(mCropHandler.getCropParams());
            startActivityForResult(intent, CropHelper.REQUEST_CROP);
        }

        @Override
        public void toCamera() {
            Intent intent = CropHelper.buildCaptureIntent(mCropHandler.getCropParams().uri);
            startActivityForResult(intent, CropHelper.REQUEST_CAMERA);
        }

        @Override
        public void cancel() {
            //取消
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        String photo = UserPrefs.getInstance().getPhoto();
        if (photo != null) {
            Picasso.with(this).load(photo).into(mIvUsericon);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.iv_usericon)
    public void onViewClicked() {
        if(mIconSelectedPopupWindow==null){
            mIconSelectedPopupWindow = new IconSelectedPopupWindow(AccountActivity.this,mOnClickListener);
        }
        if (mIconSelectedPopupWindow.isShowing()){
            mIconSelectedPopupWindow.dismiss();
            return;
        }
        mIconSelectedPopupWindow.show();
    }

    private CropHandler mCropHandler = new CropHandler() {
        @Override
        public void onPhotoCropped(Uri uri) {
            File file = new File(uri.getPath());
            new AccountPresenter(AccountActivity.this).uploadHeadIcon(file);
        }

        @Override
        public void onCropCancel() {
           mActivityUtils.showToast("剪切取消");
        }

        @Override
        public void onCropFailed(String message) {
            mActivityUtils.showToast("剪切失败");
        }

        @Override
        public CropParams getCropParams() {
            return new CropParams();
        }

        @Override
        public Activity getContext() {
            return AccountActivity.this;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CropHelper.handleResult(mCropHandler, requestCode, resultCode, data);
    }

    //清理缓存
    @Override
    protected void onDestroy() {
        if (mCropHandler.getCropParams() != null)
            CropHelper.clearCachedCropFile(mCropHandler.getCropParams().uri);
        super.onDestroy();
    }
//实现自视图接口的方法
    @Override
    public void showProgress() {
        mProgressDialog = ProgressDialog.show(this, "上传头像", "头像上传中...");
    }

    @Override
    public void showMessage(String message) {
        mActivityUtils.showToast(message);
    }

    @Override
    public void hideProgress() {
        mProgressDialog.dismiss();
    }

    @Override
    public void updatePhoto(String url) {
        if (url!=null)
        Picasso.with(this).load(url)
                .error(R.mipmap.user_icon)// 加载错误显示的视图
                .placeholder(R.mipmap.user_icon)// 占位视图
                .into(mIvUsericon);
    }
}
