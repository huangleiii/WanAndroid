package com.huanglei.wanandroid.base.presenter;

import com.huanglei.wanandroid.base.view.IBaseView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by HuangLei on 2018/11/27.
 */

public abstract class RxMVPBasePresenter<T extends IBaseView> extends MVPBasePresenter<T> {
    private CompositeDisposable compositeDisposable;

    @Override
    public void attachView(T view) {
        super.attachView(view);
        registerEvent();
    }

    /**
     * 订阅RxBus事件，并将观察者添加到compositeDisposable，以便于统一取消订阅，防止内存泄漏。
     */
    protected abstract void registerEvent();


    @Override
    public void detachView() {
        super.detachView();
        if (compositeDisposable != null)
            compositeDisposable.clear();
    }

    protected void addDisposable(Disposable d) {
        if (compositeDisposable == null)
            compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(d);
    }
}
