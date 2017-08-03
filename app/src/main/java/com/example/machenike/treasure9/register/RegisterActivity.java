package com.example.machenike.treasure9.register;

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

public class RegisterActivity extends AppCompatActivity implements RegisterView{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_Username)
    EditText mEtUsername;
    @BindView(R.id.et_Password)
    EditText mEtPassword;
    @BindView(R.id.et_Confirm)
    EditText mEtConfirm;
    @BindView(R.id.btn_Register)
    Button mBtnRegister;
    private ActivityUtils mActivityUtils;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);
        //设置Toolbar
        setSupportActionBar(mToolbar);
        if (getSupportActionBar()!=null){
            //显示返回箭头
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //设置标题
            getSupportActionBar().setTitle(R.string.register);
        }

        mEtUsername.addTextChangedListener(mTextWatcher);
        mEtPassword.addTextChangedListener(mTextWatcher);
        mEtConfirm.addTextChangedListener(mTextWatcher);
    }

    private String mUserName;
    private String mPassWord;
    private String mConfirm;
    private TextWatcher mTextWatcher = new TextWatcher() {
        //改变之前
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
        //变化中
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
        //改变之后
        @Override
        public void afterTextChanged(Editable s) {
            mUserName = mEtUsername.getText().toString();
            mPassWord = mEtPassword.getText().toString();
            mConfirm = mEtConfirm.getText().toString();

            boolean canRegister = !TextUtils.isEmpty(mUserName)&&!TextUtils.isEmpty(mPassWord)&&!TextUtils.isEmpty(mConfirm);
            mBtnRegister.setEnabled(canRegister);
        }
    };
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //点击注册
    @OnClick(R.id.btn_Register)
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

        if (!mPassWord.equals(mConfirm)){
            AlertDialogFragment.getInstance("密码确认错误","前后输入密码不一致")
                    .show(getSupportFragmentManager(),"password_error");
            return;
        }
        new RegisterPresenter(this).register(new User(mUserName,mPassWord));
    }
//---------------------------实现自视图接口的方法------------------------
    @Override
    public void showProgress() {
        mProgressDialog = ProgressDialog.show(this, "注册", "正在注册中，请稍候....");
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
    public void NavigateToHomeActivity() {
        mActivityUtils.startActivity(HomeActivity.class);
        finish();
        Intent intent = new Intent(MainActivity.ACTION_MAIN);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
