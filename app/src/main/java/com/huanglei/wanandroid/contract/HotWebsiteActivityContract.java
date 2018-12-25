package com.huanglei.wanandroid.contract;

import com.huanglei.wanandroid.base.presenter.IBasePresenter;
import com.huanglei.wanandroid.base.view.IBaseView;
import com.huanglei.wanandroid.main.HotWebsiteActivity;
import com.huanglei.wanandroid.model.bean.HotWebsite;

import java.util.List;

/**
 * Created by 黄垒 on 2018/12/26.
 */

public interface HotWebsiteActivityContract {
    interface View extends IBaseView{
        void showHotWebsiteListSucceed(List<HotWebsite> hotWebsites);
        void showHotWebsiteListFailed(String errorMsg);
    }
    interface Presenter extends IBasePresenter<View>{
        void getHotWebsiteList();
    }
}
