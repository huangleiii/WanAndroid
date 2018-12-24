package com.huanglei.wanandroid.base.presenter;

import com.huanglei.wanandroid.base.view.IBaseView;

/**
 * Created by HuangLei on 2018/11/23.
 */

public interface IBasePresenter<T extends IBaseView> {
    void attachView(T view);
    void detachView();
    boolean isViewAttached();
}
