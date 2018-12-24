package com.huanglei.wanandroid;

import com.huanglei.wanandroid.base.presenter.RxBasePresenter;
import com.huanglei.wanandroid.contract.LoginActivityContract;
import com.huanglei.wanandroid.event.LoginEvent;
import com.huanglei.wanandroid.event.LogoutEvent;
import com.huanglei.wanandroid.event.RxBus;
import com.huanglei.wanandroid.model.bean.Account;
import com.huanglei.wanandroid.model.bean.BaseResponse;
import com.huanglei.wanandroid.model.http.HttpHelper;
import com.huanglei.wanandroid.model.sharedpreferences.SharedPreferencesHelper;
import com.huanglei.wanandroid.utils.ErrorConsumer;
import com.huanglei.wanandroid.utils.RxUtils;

import io.reactivex.functions.Consumer;

/**
 * Created by HuangLei on 2018/11/26.
 */

public class LoginActivityPresenter extends RxBasePresenter<LoginActivityContract.View> implements LoginActivityContract.Presenter {

    @Override
    public void login(String username, String password) {
        addDisposable(HttpHelper.getInstance()
                .logIn(username, password)
                .compose(RxUtils.<BaseResponse<Account>>schedulerTransformer())
                .compose(RxUtils.<Account>responseTransformer())
                .subscribe(new Consumer<Account>() {
                    @Override
                    public void accept(Account account) throws Exception {
                        SharedPreferencesHelper.getInstance().setUsername(account.getUsername());
                        SharedPreferencesHelper.getInstance().setLoginStatus(true);
                        RxBus.getInstance().post(new LoginEvent());
                        if (isViewAttached())
                            getView().loginSucceed(account);
                    }
                }, new ErrorConsumer<Throwable>() {
                    @Override
                    protected void onError(int errorCode, String errorMessage) {
                        if (isViewAttached())
                            getView().loginFailed(errorMessage);
                    }
                }));
    }

    @Override
    public void register(String username, String password, String repassword) {
        addDisposable(HttpHelper.getInstance()
                .register(username, password, repassword)
                .compose(RxUtils.<BaseResponse<Account>>schedulerTransformer())
                .compose(RxUtils.<Account>responseTransformer())
                .subscribe(new Consumer<Account>() {
                    @Override
                    public void accept(Account account) throws Exception {
                        if (isViewAttached())
                            getView().registerSucceed(account);
                    }
                }, new ErrorConsumer<Throwable>() {
                    @Override
                    protected void onError(int errorCode, String errorMessage) {
                        if (isViewAttached())
                            getView().registerFailed(errorMessage);
                    }
                }));
    }

    @Override
    protected void registerEvent() {

    }
}
