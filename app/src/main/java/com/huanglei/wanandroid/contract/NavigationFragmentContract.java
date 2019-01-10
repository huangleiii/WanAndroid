package com.huanglei.wanandroid.contract;

import com.huanglei.wanandroid.base.presenter.IBasePresenter;
import com.huanglei.wanandroid.base.view.IBaseView;
import com.huanglei.wanandroid.model.bean.NavigationArticleList;

import java.util.List;

/**
 * Created by 黄垒 on 2019/1/10.
 */

public interface NavigationFragmentContract {
    interface Presenter extends IBasePresenter<View>{
        void getNavigationLists();
    }
    interface View extends IBaseView{
        void showNavigationListSucceed(List<NavigationArticleList> navigationArticleLists);
        void showNavigationListFailed(String errorMsg);
    }
}
