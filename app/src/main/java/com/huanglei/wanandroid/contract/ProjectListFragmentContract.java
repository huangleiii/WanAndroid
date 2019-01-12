package com.huanglei.wanandroid.contract;

import com.huanglei.wanandroid.base.presenter.IBasePresenter;
import com.huanglei.wanandroid.base.view.IBaseView;
import com.huanglei.wanandroid.model.bean.Article;

import java.util.List;

/**
 * Created by 黄垒 on 2019/1/12.
 */

public interface ProjectListFragmentContract {
    interface Presenter extends IBasePresenter<View> {
        void getProjectArticles(int id);
        void addProjectArticles(int num,int id);
        void collect(int position,List<Article> articles);
        void cancelCollect(int position,List<Article> articles);
    }
    interface View extends IBaseView {
        void showProjectArticlesSucceed(List<Article> articles);
        void showProjectArticlesFailed(String errorMsg);

        void showAddProjectArticlesSucceed(List<Article> articles);
        void showAddProjectArticlesFailed(String errorMsg);

        void showCollectSucceed(int position,List<Article> articles);
        void showCollectFailed(int position,List<Article> articles,boolean isLoginExpired,String errorMsg);

        void showCancelCollectSucceed(int position,List<Article> articles);
        void showCancelCollectFailed(int position,List<Article> articles,boolean isLoginExpired,String errorMsg);

    }
}
