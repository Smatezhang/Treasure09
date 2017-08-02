package com.example.machenike.treasure9.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.machenike.treasure9.MainActivity;
import com.example.machenike.treasure9.R;
import com.example.machenike.treasure9.User;
import com.example.machenike.treasure9.commons.ActivityUtils;
import com.example.machenike.treasure9.commons.RegexUtils;
import com.example.machenike.treasure9.custom.AlertDialogFragment;
import com.example.machenike.treasure9.map.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoginActivity extends AppCompatActivity implements LoginView{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_Username)
    EditText mEtUsername;
    @BindView(R.id.et_Password)
    EditText mEtPassword;
    @BindView(R.id.tv_forgetPassword)
    TextView mTvForgetPassword;
    @BindView(R.id.btn_Login)
    Button mBtnLogin;
    private Unbinder mUnbinder;
    private ActivityUtils mActivityUtils;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUnbinder = ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar()!=null){
            //显示返回箭头
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //设置标题
            getSupportActionBar().setTitle(R.string.login);
        }

        //对edittext添加文本监听
        mEtUsername.addTextChangedListener(mTextWatcher);
        mEtPassword.addTextChangedListener(mTextWatcher);
    }

    private String mUserName;
    private String mPassWord;
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            mUserName = mEtUsername.getText().toString();
            mPassWord = mEtPassword.getText().toString();

            boolean canLogin = !TextUtils.isEmpty(mUserName)&&!TextUtils.isEmpty(mPassWord);

            mBtnLogin.setEnabled(canLogin);
        }
    };
    @OnClick(R.id.btn_Login)
    public void onViewClicked() {
        if (RegexUtils.verifyUsername(mUserName)!=RegexUtils.VERIFY_SUCCESS){
            AlertDialogFragment.getInstance(getString(R.string.username_error),getString(R.string.username_rules))
                    .show(getSupportFragmentManager(),"username_error");
            return;
        }
        if (RegexUtils.verifyPassword(mPassWord)!=RegexUtils.VERIFY_SUCCESS){
            AlertDialogFragment.getInstance(getString(R.string.password_error),getString(R.string.password_rules))
                    .show(getSupportFragmentManager(),"password_error");
            return;
        }

        new LoginPresenter(this).login(new User(mUserName,mPassWord));
    }

    //返回箭头的点击

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
//------------------------------实现自视图接口的方法--------------------------
    @Override
    public void showProgress() {
        mProgressDialog = ProgressDialog.show(LoginActivity.this, "登录", "正在登陆中，请稍候......");
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
    public void navigateToHomeActivity() {
        mActivityUtils.startActivity(HomeActivity.class);
        finish();

        LocalBroadcastManager.getInstance(LoginActivity.this).sendBroadcast(new Intent(MainActivity.ACTION_MAIN));
    }
}
