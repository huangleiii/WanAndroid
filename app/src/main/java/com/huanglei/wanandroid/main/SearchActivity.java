package com.huanglei.wanandroid.main;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.huanglei.wanandroid.base.view.activity.MVPBaseActivity;
import com.huanglei.wanandroid.contract.SearchActivityContract;
import com.huanglei.wanandroid.model.bean.Article;
import com.huanglei.wanandroid.model.bean.HotKey;
import com.huanglei.wanandroid.model.db.HistoryKeyword;

import java.util.List;

public class SearchActivity extends MVPBaseActivity<SearchActivityContract.Presenter> implements SearchActivityContract.View{

    @Override
    public Context getViewContext() {
        return null;
    }

    @Override
    public void showSearchArticleListSucceed(List<Article> articleList) {

    }

    @Override
    public void showSearchArticleListFailed(String errorMsg) {

    }

    @Override
    public void showAddSearchArticleListSucceed(List<Article> articles) {

    }

    @Override
    public void showAddSearchArticleListFailed(String errorMsg) {

    }

    @Override
    public void showHotSearchKeywordsSucceed(List<HotKey> keys) {

    }

    @Override
    public void showHotSearchKeywordsFailed(String errorMs) {

    }

    @Override
    public void showHistorySearchKeywordsSucceed(List<HistoryKeyword> keys) {

    }

    @Override
    public void showHistorySearchKeywordsFailed(String errorMsg) {

    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initToolbar() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void requestData() {

    }

    @Override
    protected SearchActivityContract.Presenter createPresenter() {
        return null;
    }
}
