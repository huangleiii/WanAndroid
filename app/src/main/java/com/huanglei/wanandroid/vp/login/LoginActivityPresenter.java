package com.huanglei.wanandroid.vp.login;

import com.huanglei.wanandroid.base.presenter.RxMVPBasePresenter;
import com.huanglei.wanandroid.contract.LoginActivityContract;
import com.huanglei.wanandroid.event.LoginEvent;
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

public class LoginActivityPresenter extends RxMVPBasePresenter<LoginActivityContract.View> implements LoginActivityContract.Presenter {

    @Override
    public void login(final String activityName, String username, String password) {
        addDisposable(HttpHelper.getInstance()
                .logIn(username, password)
                .compose(RxUtils.<BaseResponse<Account>>schedulerTransformer())
                .compose(RxUtils.<Account>responseTransformer())
                .subscribe(new Consumer<Account>() {
                    @Override
                    public void accept(Account account) throws Exception {
                        if (isViewAttached())
                            getView().loginSucceed(account);
                        LoginEvent loginEvent=new LoginEvent();
                        loginEvent.setActivityName(activityName);
                        RxBus.getInstance().post(loginEvent);
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
    public void setLoginStatus(boolean isLogin,String username) {
        SharedPreferencesHelper.getInstance().setUsername(username);
        SharedPreferencesHelper.getInstance().setLoginStatus(isLogin);
    }


    @Override
    protected void registerEvent() {

    }
}
