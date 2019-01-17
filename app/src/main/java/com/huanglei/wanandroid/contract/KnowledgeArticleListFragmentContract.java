package com.huanglei.wanandroid.contract;

import com.huanglei.wanandroid.base.presenter.IBasePresenter;
import com.huanglei.wanandroid.base.view.IBaseView;
import com.huanglei.wanandroid.model.bean.Article;
import com.huanglei.wanandroid.model.bean.Tab;

import java.util.List;

/**
 * Created by 黄垒 on 2019/1/13.
 */

public interface KnowledgeArticleListFragmentContract {
    interface Presenter extends IBasePresenter<View> {
        void getKnowledgeArticleList(int id);
        void addKnowledgeArticleList(int num,int id);
        void collect(int position,List<Article> articles);
        void cancelCollect(int position,List<Article> articles);
    }
    interface View extends IBaseView {
        void showKnowledgeArticleListSucceed(List<Article> articles);
        void showKnowledgeArticleListFailed(String errorMsg);

        void showAddKnowledgeArticleListSucceed(List<Article> articles);
        void showAddKnowledgeArticleListFailed(String errorMsg);

        void showCollectSucceed(int position,List<Article> articles);
        void showCollectFailed(int position,List<Article> articles,boolean isLoginExpired,String errorMsg);

        void showCancelCollectSucceed(int position,List<Article> articles);
        void showCancelCollectFailed(int position,List<Article> articles,boolean isLoginExpired,String errorMsg);
    }
}
