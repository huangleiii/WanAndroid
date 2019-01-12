package com.huanglei.wanandroid.contract;

import com.huanglei.wanandroid.base.presenter.IBasePresenter;
import com.huanglei.wanandroid.base.view.IBaseView;
import com.huanglei.wanandroid.model.bean.Tab;

import java.util.List;

/**
 * Created by 黄垒 on 2019/1/11.
 */

public interface WeixinFragmentContract {
    interface Presenter extends IBasePresenter<View>{
        void getWxTabs();
    }
    interface View extends IBaseView{
        void showWxTabsSucceed(List<Tab> tabList);
        void showWxTabsFailed(String errorMsg);
    }
}
