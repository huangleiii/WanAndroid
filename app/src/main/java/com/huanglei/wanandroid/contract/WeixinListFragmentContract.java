package com.huanglei.wanandroid.contract;

import com.huanglei.wanandroid.base.presenter.IBasePresenter;
import com.huanglei.wanandroid.base.view.IBaseView;
import com.huanglei.wanandroid.model.bean.Article;

import java.util.List;

/**
 * Created by 黄垒 on 2019/1/11.
 */

public interface WeixinListFragmentContract {
    interface Presenter extends IBasePresenter<View> {
        void getWxArticles(int id);
        void addWxArticles(int num,int id);
        void collect(int position,List<Article> articles);
        void cancelCollect(int position,List<Article> articles);
    }
    interface View extends IBaseView {
        void showWxArticlesSucceed(List<Article> articles);
        void showWxArticlesFailed(String errorMsg);

        void showAddWxArticlesSucceed(List<Article> articles);
        void showAddWxArticlesFailed(String errorMsg);

        void showCollectSucceed(int position,List<Article> articles);
        void showCollectFailed(int position,List<Article> articles,String errorMsg);

        void showCancelCollectSucceed(int position,List<Article> articles);
        void showCanaelCollectFailed(int position,List<Article> articles,String errorMsg);

    }
}
