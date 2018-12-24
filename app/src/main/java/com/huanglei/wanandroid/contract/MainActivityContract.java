package com.huanglei.wanandroid.contract;

import com.huanglei.wanandroid.base.presenter.IBasePresenter;
import com.huanglei.wanandroid.base.view.IBaseView;

/**
 * Created by HuangLei on 2018/11/23.
 */

public interface MainActivityContract {
    interface Presenter extends IBasePresenter<View> {
        boolean getLoginStatus();

        String getUsername();

        void logout();
    }

    interface View extends IBaseView {
        void showLogoutSucceed(Object o);

        void showLogoutFailed(String msg);

        void subscribeLoginEvent();
        void subscribeUnLoginEvent();
    }
}
