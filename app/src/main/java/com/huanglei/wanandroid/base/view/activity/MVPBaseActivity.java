package com.huanglei.wanandroid.base.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.huanglei.wanandroid.base.presenter.IBasePresenter;
import com.huanglei.wanandroid.base.view.IBaseView;

/**
 * Created by HuangLei on 2018/11/13.
 */

public abstract class MVPBaseActivity<T extends IBasePresenter> extends BaseActivity implements IBaseView {
    private T mPresenter;

    protected T getPresenter() {
        return mPresenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mPresenter = createPresenter();
        mPresenter.attachView(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.detachView();
    }

    protected abstract T createPresenter();



}
