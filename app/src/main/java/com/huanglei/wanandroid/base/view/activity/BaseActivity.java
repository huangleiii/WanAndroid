package com.huanglei.wanandroid.base.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.huanglei.wanandroid.base.presenter.IBasePresenter;

import butterknife.ButterKnife;

/**
 * Created by HuangLei on 2018/11/13.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initToolbar();
        initView();
        requestData();
    }

    protected abstract int getLayoutId();

    protected abstract void initToolbar();

    protected abstract void initView();

    protected abstract void requestData();
}
