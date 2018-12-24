package com.huanglei.wanandroid.contract;

import com.huanglei.wanandroid.base.presenter.IBasePresenter;
import com.huanglei.wanandroid.base.view.IBaseView;
import com.huanglei.wanandroid.model.bean.Article;
import com.huanglei.wanandroid.model.bean.BannerData;

import java.util.List;

/**
 * Created by HuangLei on 2018/11/19.
 */

public interface HomeFragmentContract {
    interface Presenter extends IBasePresenter<View> {
        void getListData();

        void getBannerData();

        void addListData(int page);

        void collect(int position, List<Article> articles);

        void cancelCollect(int position, List<Article> articles);
    }

    interface View extends IBaseView {
        void showNewListDataSucceed(List<Article> articles);
        void showNewListDataFailed(String errorMsg);

        void showNewBannerDataSucceed(List<BannerData> bannerData);
        void showNewBannerDataFailed(String errorMsg);

        void showAddListDataSucceed(List<Article> articles);
        void showAddListDataFailed(String errorMsg);

        void showCollectSucceed(int position, List<Article> articles);
        void showCollectFailed(int position,String errorMsg);

        void showCancelCollectSucceed(int position, List<Article> articles);
        void showCancelCollectFailed(int position,String errorMsg);

        void showCancelCollectEvent(int position);
        void showCollectEvent(int position);
        void showLoginEvent();
        void showLogoutEvent();
    }

}
