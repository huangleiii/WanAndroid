package com.huanglei.wanandroid.base.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.huanglei.wanandroid.base.presenter.IBasePresenter;
import com.huanglei.wanandroid.base.view.IBaseView;

import butterknife.ButterKnife;

/**
 * Created by HuangLei on 2018/11/13.
 */

public abstract class MVPBaseActivity<T extends IBasePresenter> extends AppCompatActivity implements IBaseView {
    private T mPresenter;

    protected T getPresenter() {
        return mPresenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        mPresenter = createPresenter();
        mPresenter.attachView(this);
        initToolbar();
        initView();
        requestData();
    }

    protected abstract int getLayoutId();

    protected abstract T createPresenter();

    protected abstract void initToolbar();

    protected abstract void initView();

    protected abstract void requestData();

    @Override
    protected void onDestroy() {
        if (mPresenter != null)
            mPresenter.detachView();
        super.onDestroy();
    }




}
