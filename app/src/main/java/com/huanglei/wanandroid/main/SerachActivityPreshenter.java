package com.huanglei.wanandroid.main;

import com.huanglei.wanandroid.base.presenter.RxMVPBasePresenter;
import com.huanglei.wanandroid.contract.SearchActivityContract;
import com.huanglei.wanandroid.model.bean.BaseResponse;
import com.huanglei.wanandroid.model.bean.HomeArticleList;
import com.huanglei.wanandroid.model.bean.HotKey;
import com.huanglei.wanandroid.model.db.DbHelper;
import com.huanglei.wanandroid.model.http.HttpHelper;
import com.huanglei.wanandroid.utils.ErrorConsumer;
import com.huanglei.wanandroid.utils.RxUtils;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by 黄垒 on 2018/12/26.
 */

public class SerachActivityPreshenter extends RxMVPBasePresenter<SearchActivityContract.View> implements SearchActivityContract.Presenter{
    @Override
    public void getSearchArticleList(String key) {
        addDisposable(HttpHelper.getInstance()
        .getSearchArticleList(key,0)
        .compose(RxUtils.<HomeArticleList>responseTransformer())
        .compose(RxUtils.<HomeArticleList>schedulerTransformer())
        .subscribe(new Consumer<HomeArticleList>() {
            @Override
            public void accept(HomeArticleList homeArticleList) throws Exception {
                if(isViewAttached()){
                    getView().showSearchArticleListSucceed(homeArticleList.getDatas());
                }
            }
        }, new ErrorConsumer<Throwable>() {
            @Override
            protected void onError(int errorCode, String errorMessage) {
                if(isViewAttached()){
                    getView().showSearchArticleListFailed(errorMessage);
                }
            }
        }));
        DbHelper.getInstance().addHistoryKeyword(key);
    }

    @Override
    public void addSearchArticleList(String key, int page) {
        addDisposable(HttpHelper.getInstance()
                .getSearchArticleList(key,page)
                .compose(RxUtils.<HomeArticleList>responseTransformer())
                .compose(RxUtils.<HomeArticleList>schedulerTransformer())
                .subscribe(new Consumer<HomeArticleList>() {
                    @Override
                    public void accept(HomeArticleList homeArticleList) throws Exception {
                        if(isViewAttached()){
                            getView().showAddSearchArticleListSucceed(homeArticleList.getDatas());
                        }
                    }
                }, new ErrorConsumer<Throwable>() {
                    @Override
                    protected void onError(int errorCode, String errorMessage) {
                        if(isViewAttached()){
                            getView().showAddSearchArticleListFailed(errorMessage);
                        }
                    }
                }));
    }

    @Override
    public void getHotSearchKeywords() {
        addDisposable(HttpHelper.getInstance()
        .getSearchHotKeys()
        .compose(RxUtils.<BaseResponse<List<HotKey>>>schedulerTransformer())
        .compose(RxUtils.<List<HotKey>>responseTransformer())
        .subscribe(new Consumer<List<HotKey>>() {
            @Override
            public void accept(List<HotKey> hotKeys) throws Exception {
                if(isViewAttached()){
                    getView().showHotSearchKeywordsSucceed(hotKeys);
                }
            }
        }, new ErrorConsumer<Throwable>() {
            @Override
            protected void onError(int errorCode, String errorMessage) {
                if(isViewAttached()){
                    getView().showHotSearchKeywordsFailed(errorMessage);
                }
            }
        }));
    }

    @Override
    public void getHistorySearchKeywords() {
        if(isViewAttached()){
            getView().showHistorySearchKeywordsSucceed(DbHelper.getInstance().getHistoryKeywords());
        }
    }

    @Override
    protected void registerEvent() {

    }
}
