package com.huanglei.wanandroid.contract;

import com.huanglei.wanandroid.base.presenter.IBasePresenter;
import com.huanglei.wanandroid.base.view.IBaseView;
import com.huanglei.wanandroid.model.bean.Article;
import com.huanglei.wanandroid.model.bean.HotKey;
import com.huanglei.wanandroid.model.db.HistoryKeyword;

import java.util.List;

/**
 * Created by 黄垒 on 2018/12/26.
 */

public interface SearchActivityContract {
    interface Presenter extends IBasePresenter<View>{
        void getSearchArticleList(String key);
        void addSearchArticleList(String key,int page);
        void getHotSearchKeywords();
        void getHistorySearchKeywords();
    }
    interface View extends IBaseView{
        void showSearchArticleListSucceed(List<Article> articleList);
        void showSearchArticleListFailed(String errorMsg);

        void showAddSearchArticleListSucceed(List<Article> articles);
        void showAddSearchArticleListFailed(String errorMsg);

        void showHotSearchKeywordsSucceed(List<HotKey> keys);
        void showHotSearchKeywordsFailed(String errorMs);

        void showHistorySearchKeywordsSucceed(List<HistoryKeyword> keys);
        void showHistorySearchKeywordsFailed(String errorMsg);
    }
}
