package com.huanglei.wanandroid;

import com.huanglei.wanandroid.base.presenter.RxBasePresenter;
import com.huanglei.wanandroid.contract.MainActivityContract;
import com.huanglei.wanandroid.event.LoginEvent;
import com.huanglei.wanandroid.event.UnLogin;
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

public class MainActivityPresenter extends RxBasePresenter<MainActivityContract.View> implements MainActivityContract.Presenter {
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
        addDisposable(RxBus.getInstance().toObservable(UnLogin.class)
                .subscribe(new Consumer<UnLogin>() {
                    @Override
                    public void accept(UnLogin unLogin) throws Exception {
                        if(isViewAttached())
                            getView().subscribeUnLoginEvent();
                    }
                }));
    }
}
