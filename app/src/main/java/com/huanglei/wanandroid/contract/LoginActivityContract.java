package com.huanglei.wanandroid.contract;

import com.huanglei.wanandroid.base.presenter.IBasePresenter;
import com.huanglei.wanandroid.base.view.IBaseView;
import com.huanglei.wanandroid.model.bean.Account;

/**
 * Created by HuangLei on 2018/11/26.
 */

public interface LoginActivityContract {
    interface Presenter extends IBasePresenter<View>{
        void login(String username,String password);
        void register(String username,String password,String repassword);
    }
    interface View extends IBaseView{
        void loginSucceed(Account account);
        void loginFailed(String errorMsg);
        void registerSucceed(Account account);
        void registerFailed(String errorMsg);
    }
}
