package com.huanglei.wanandroid;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huanglei.wanandroid.base.view.activity.CommonBaseActivity;
import com.huanglei.wanandroid.contract.LoginActivityContract;
import com.huanglei.wanandroid.model.bean.Account;
import com.huanglei.wanandroid.utils.CommonUtils;
import com.huanglei.wanandroid.widget.MyProgressDialog;

import butterknife.BindView;

public class LoginActivity extends CommonBaseActivity<LoginActivityContract.Presenter> implements LoginActivityContract.View {

    @BindView(R.id.lin_account_activity_login)
    LinearLayout linAccountActivityLogin;
    @BindView(R.id.lin_password_activity_login)
    LinearLayout linPasswordActivityLogin;
    @BindView(R.id.lin_repassword_activity_login)
    LinearLayout linRepasswordActivityLogin;
    @BindView(R.id.btn_log_in_activity_login)
    Button btnLogInActivityLogin;
    @BindView(R.id.btn_register_activity_login)
    Button btnRegisterActivityLogin;
    @BindView(R.id.tv_register_activity_login)
    TextView tvRegisterActivityLogin;
    @BindView(R.id.tv_log_in_activity_login)
    TextView tvLogInActivityLogin;
    @BindView(R.id.toolbar_activity_login)
    Toolbar toolbarActivityLogin;
    @BindView(R.id.tv_title_activity_login)
    TextView tvTitleActivityLogin;
    @BindView(R.id.et_account_activity_login)
    EditText etAccountActivityLogin;
    @BindView(R.id.et_password_activity_login)
    EditText etPasswordActivityLogin;
    @BindView(R.id.et_repassword_activity_login)
    EditText etRepasswordActivityLogin;
    private MyProgressDialog mMyProgressDialog;

    @Override
    protected LoginActivityContract.Presenter createPresenter() {
        return new LoginActivityPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initToolbar() {
        setSupportActionBar(toolbarActivityLogin);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initView() {
        showLogIn();
        mMyProgressDialog = new MyProgressDialog(LoginActivity.this);
        btnLogInActivityLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etAccountActivityLogin.getText())
                        || TextUtils.isEmpty(etPasswordActivityLogin.getText()))
                    CommonUtils.showToastMessage(LoginActivity.this, "输入不能为空");
                else {
                    mMyProgressDialog.setText("正在登录，请稍候……").show();
                    getPresenter().login(etAccountActivityLogin.getText().toString(),
                            etPasswordActivityLogin.getText().toString());
                }
            }
        });
        tvRegisterActivityLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegister();
            }
        });
        btnRegisterActivityLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etAccountActivityLogin.getText())
                        || TextUtils.isEmpty(etPasswordActivityLogin.getText())
                        || TextUtils.isEmpty(etRepasswordActivityLogin.getText()))
                    CommonUtils.showToastMessage(LoginActivity.this, "输入不能为空");
                else {
                    mMyProgressDialog.setText("正在注册，请稍候……").show();
                    getPresenter().register(etAccountActivityLogin.getText().toString(),
                            etPasswordActivityLogin.getText().toString(),
                            etRepasswordActivityLogin.getText().toString());
                }
            }
        });
        tvLogInActivityLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogIn();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    private void showRegister() {
        linRepasswordActivityLogin.setVisibility(View.VISIBLE);
        tvTitleActivityLogin.setText("注册");
        etAccountActivityLogin.setText("");
        etPasswordActivityLogin.setText("");
        etRepasswordActivityLogin.setText("");
        tvRegisterActivityLogin.setVisibility(View.GONE);
        tvLogInActivityLogin.setVisibility(View.VISIBLE);
        btnRegisterActivityLogin.setVisibility(View.VISIBLE);
        btnLogInActivityLogin.setVisibility(View.GONE);
    }

    private void showLogIn() {
        linRepasswordActivityLogin.setVisibility(View.GONE);
        tvTitleActivityLogin.setText("登录");
        etPasswordActivityLogin.setText("");
        etAccountActivityLogin.setText("");
        tvRegisterActivityLogin.setVisibility(View.VISIBLE);
        tvLogInActivityLogin.setVisibility(View.GONE);
        btnRegisterActivityLogin.setVisibility(View.GONE);
        btnLogInActivityLogin.setVisibility(View.VISIBLE);
    }


    @Override
    protected void requestData() {

    }

    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    public void loginSucceed(Account account) {
        mMyProgressDialog.dismiss();
        CommonUtils.showToastMessage(this, "登录成功");
        finish();
    }

    @Override
    public void loginFailed(String errorMsg) {
        mMyProgressDialog.dismiss();
        CommonUtils.showToastMessage(this, errorMsg);
        showLogIn();
    }

    @Override
    public void registerSucceed(Account account) {
        mMyProgressDialog.dismiss();
        CommonUtils.showToastMessage(this, "注册成功");
        showLogIn();
    }

    @Override
    public void registerFailed(String errorMsg) {
        mMyProgressDialog.dismiss();
        CommonUtils.showToastMessage(this, errorMsg);
        showRegister();
    }


}
