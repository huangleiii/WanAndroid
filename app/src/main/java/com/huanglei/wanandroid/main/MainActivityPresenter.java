package com.huanglei.wanandroid.main;

import com.huanglei.wanandroid.base.presenter.RxMVPBasePresenter;
import com.huanglei.wanandroid.contract.MainActivityContract;
import com.huanglei.wanandroid.event.LoginEvent;
import com.huanglei.wanandroid.event.LoginExpiredEvent;
import com.huanglei.wanandroid.event.LogoutEvent;
import com.huanglei.wanandroid.event.RxBus;
import com.huanglei.wanandroid.model.bean.BaseResponse;
import com.huanglei.wanandroid.model.http.HttpHelper;
import com.huanglei.wanandroid.model.sharedpreferences.SharedPreferencesHelper;
import com.huanglei.wanandroid.utils.ErrorConsumer;
import com.huanglei.wanandroid.utils.RxUtils;

import io.reactivex.functions.Consumer;

/**
 * Created by HuangLei on 2018/11/23.
 */

public class MainActivityPresenter extends RxMVPBasePresenter<MainActivityContract.View> implements MainActivityContract.Presenter {
    @Override
    public boolean getLoginStatus() {
        return SharedPreferencesHelper.getInstance().getLoginStatus();
    }

    @Override
    public String getUsername() {
        return SharedPreferencesHelper.getInstance().getUsername();
    }

    @Override
    public void logout() {
        addDisposable(HttpHelper.getInstance()
                .logout()
                .compose(RxUtils.<BaseResponse<Object>>schedulerTransformer())
                .compose(RxUtils.noDataResponseTransformer())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        SharedPreferencesHelper.getInstance().setUsername("");
                        SharedPreferencesHelper.getInstance().setLoginStatus(false);
                        RxBus.getInstance().post(new LogoutEvent());
                        if (isViewAttached())
                            getView().showLogoutSucceed(o);
                    }
                }, new ErrorConsumer<Throwable>() {
                    @Override
                    protected void onError(int errorCode, String errorMessage) {
                        if (isViewAttached())
                            getView().showLogoutFailed(errorMessage);
                    }
                }));
    }

    @Override
    protected void registerEvent() {
        addDisposable(RxBus.getInstance().toObservable(LoginEvent.class)
                .subscribe(new Consumer<LoginEvent>() {
                    @Override
                    public void accept(LoginEvent loginEvent) throws Exception {
                        if (isViewAttached())
                            getView().subscribeLoginEvent();
                    }
                }));
        addDisposable(RxBus.getInstance().toObservable(LoginExpiredEvent.class)
                .subscribe(new Consumer<LoginExpiredEvent>() {
                    @Override
                    public void accept(LoginExpiredEvent loginExpiredEvent) throws Exception {
                        if(isViewAttached())
                            getView().subscribeUnLoginEvent();
                    }
                }));
    }
}
