package com.huanglei.wanandroid.contract;

import com.huanglei.wanandroid.base.presenter.IBasePresenter;
import com.huanglei.wanandroid.base.view.IBaseView;
import com.huanglei.wanandroid.model.bean.Article;

import java.util.List;

/**
 * Created by 黄垒 on 2019/1/6.
 */

public interface SearchListActivityContract {
    interface Presenter extends IBasePresenter<View>{
        void getSearchArticleList(String key);
        void addSearchArticleList(String key,int page);

        void collect(int position, List<Article> articles);

        void cancelCollect(int position, List<Article> articles);

    }
    interface View extends IBaseView{
        void showSearchArticleListSucceed(List<Article> articleList);
        void showSearchArticleListFailed(String errorMsg);

        void showAddSearchArticleListSucceed(List<Article> articles);
        void showAddSearchArticleListFailed(String errorMsg);

        void showCollectSucceed(int position,List<Article> articles);
        void showCollectFailed(int position,List<Article> articles,boolean isLoginExpired,String errorMsg);

        void showCancelCollectSucceed(int position,List<Article> articles);
        void showCancelCollectFailed(int position,List<Article> articles,boolean isLoginExpired,String errorMsg);

        void subscribeLoginEvent(String activityName);
        void subscribeCancelCollectEvent(String activityName);
        void subscribeCollectEvent(String activityName);
    }
}
