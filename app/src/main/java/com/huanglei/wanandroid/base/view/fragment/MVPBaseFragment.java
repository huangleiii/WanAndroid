package com.huanglei.wanandroid.base.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.huanglei.wanandroid.base.presenter.IBasePresenter;
import com.huanglei.wanandroid.base.view.IBaseView;

/**
 * Created by HuangLei on 2018/11/18.
 */

public abstract class MVPBaseFragment<T extends IBasePresenter> extends BaseFragment implements IBaseView {
    private T mPresenter;

    protected T getPresenter() {
        return mPresenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mPresenter = createPresenter();
        mPresenter.attachView(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.detachView();
    }

    protected abstract T createPresenter();



}
