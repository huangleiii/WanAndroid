package com.huanglei.wanandroid.contract;

import com.huanglei.wanandroid.base.presenter.IBasePresenter;
import com.huanglei.wanandroid.base.view.IBaseView;
import com.huanglei.wanandroid.model.bean.Tab;

import java.util.List;

/**
 * Created by 黄垒 on 2019/1/12.
 */

public interface ProjectFragmentContract {

    interface Presenter extends IBasePresenter<View> {
        void getProjectTabs();
    }
    interface View extends IBaseView {
        void showProjectTabsSucceed(List<Tab> tabList);
        void showProjectTabsFailed(String errorMsg);
    }
}
