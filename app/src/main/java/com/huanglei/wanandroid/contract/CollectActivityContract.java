package com.huanglei.wanandroid.contract;

import com.huanglei.wanandroid.base.presenter.IBasePresenter;
import com.huanglei.wanandroid.base.view.IBaseView;
import com.huanglei.wanandroid.model.bean.Article;
import com.huanglei.wanandroid.model.bean.ArticleInCollectPage;

import java.util.List;

/**
 * Created by 黄垒 on 2019/1/7.
 */

public interface CollectActivityContract {
    interface Presenter extends IBasePresenter<View>{
        void getCollectArticles();
        void addCollectArticles(int page);
        void cancelCollect(int position, List<ArticleInCollectPage> articles);
        void setCurrentActivity(String activityName);
        String getCurrentActivity();
    }
    interface View extends IBaseView{
        void showCollectArticlesSucceed(List<ArticleInCollectPage> articles);
        void showCollectArticlesFailed(String errorMsg);

        void showAddCollectArticlesSucceed(List<ArticleInCollectPage> articles);
        void showAddCollectArticlesFailed(String errorMsg);

        void showCancelCollectSucceed(int position,List<ArticleInCollectPage> articles);
        void showCancelCollectFailed(int position,List<ArticleInCollectPage> articles,String errorMsg);

        void subscribeLoginEvent();
        void subscribeCancelCollectEvent();
        void subscribeCollectEvent();
    }
}
