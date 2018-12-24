package com.huanglei.wanandroid.base.presenter;

import com.huanglei.wanandroid.base.view.IBaseView;

import java.lang.ref.WeakReference;

/**
 * Created by HuangLei on 2018/11/19.
 */

public abstract class BasePresenter<T extends IBaseView> implements IBasePresenter<T> {
    private WeakReference<T> mWeakView;

    @Override
    public void attachView(T view) {
        mWeakView = new WeakReference<T>(view);
    }
    protected T getView() {
        return mWeakView.get();
    }

    @Override
    public void detachView() {
        if (mWeakView != null) {
            mWeakView.clear();
            mWeakView = null;
        }
    }

    @Override
    public boolean isViewAttached() {
        return mWeakView != null && mWeakView.get() != null;
    }

}
