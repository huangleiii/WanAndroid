package com.huanglei.wanandroid.main;

import com.huanglei.wanandroid.base.presenter.RxMVPBasePresenter;
import com.huanglei.wanandroid.contract.SearchListActivityContract;
import com.huanglei.wanandroid.event.CancelCollectEvent;
import com.huanglei.wanandroid.event.CollectEvent;
import com.huanglei.wanandroid.event.LoginEvent;
import com.huanglei.wanandroid.event.LoginExpiredEvent;
import com.huanglei.wanandroid.event.RxBus;
import com.huanglei.wanandroid.model.bean.Article;
import com.huanglei.wanandroid.model.bean.BaseResponse;
import com.huanglei.wanandroid.model.bean.ArticleList;
import com.huanglei.wanandroid.model.http.HttpHelper;
import com.huanglei.wanandroid.utils.ErrorConsumer;
import com.huanglei.wanandroid.utils.RxUtils;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by 黄垒 on 2019/1/6.
 */

public class SearchListActivityPresenter extends RxMVPBasePresenter<SearchListActivityContract.View> implements SearchListActivityContract.Presenter {

    @Override
    public void getSearchArticleList(String key) {
        addDisposable(HttpHelper.getInstance()
        .getSearchArticleList(key,0)
        .compose(RxUtils.<ArticleList>responseTransformer())
        .compose(RxUtils.<ArticleList>schedulerTransformer())
        .subscribe(new Consumer<ArticleList>() {
            @Override
            public void accept(ArticleList articleList) throws Exception {
                if(isViewAttached()){
                    getView().showSearchArticleListSucceed(articleList.getDatas());
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
    }

    @Override
    public void addSearchArticleList(String key, int page) {
        addDisposable(HttpHelper.getInstance()
                .getSearchArticleList(key,page)
                .compose(RxUtils.<ArticleList>responseTransformer())
                .compose(RxUtils.<ArticleList>schedulerTransformer())
                .subscribe(new Consumer<ArticleList>() {
                    @Override
                    public void accept(ArticleList articleList) throws Exception {
                        if(isViewAttached()){
                            getView().showAddSearchArticleListSucceed(articleList.getDatas());
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
    public void collect(final int position, final List<Article> articles) {
        addDisposable(HttpHelper.getInstance()
                .collect(articles.get(position).getId())
                .compose(RxUtils.<BaseResponse<Object>>schedulerTransformer())
                .compose(RxUtils.noDataResponseTransformer())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (isViewAttached())
                            getView().showCollectSucceed(position, articles);
                    }
                }, new ErrorConsumer<Throwable>() {
                    @Override
                    protected void onError(int errorCode, String errorMessage) {
                        if (errorCode == BaseResponse.LOGIN_FAILED) {
                            RxBus.getInstance().post(new LoginExpiredEvent());
                        }
                        if (isViewAttached()) {
                            getView().showCollectFailed(position, articles,errorMessage);
                        }
                    }
                }));
    }

    @Override
    public void cancelCollect(final int position, final List<Article> articles) {
        addDisposable(HttpHelper.getInstance()
                .cancelCollect(articles.get(position).getId())
                .compose(RxUtils.<BaseResponse<Object>>schedulerTransformer())
                .compose(RxUtils.noDataResponseTransformer())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (isViewAttached())
                            getView().showCancelCollectSucceed(position, articles);
                    }
                }, new ErrorConsumer<Throwable>() {
                    @Override
                    protected void onError(int errorCode, String errorMessage) {
                        if (errorCode == BaseResponse.LOGIN_FAILED) {
                            RxBus.getInstance().post(new LoginExpiredEvent());
                        }
                        if (isViewAttached()) {
                            getView().showCancelCollectFailed(position, articles,errorMessage);
                        }
                    }
                }));
    }

    @Override
    protected void registerEvent() {
        addDisposable(RxBus.getInstance().toObservable(LoginEvent.class)
                .subscribe(new Consumer<LoginEvent>() {
                    @Override
                    public void accept(LoginEvent loginEvent) throws Exception {
                        if (isViewAttached())
                            getView().subscribeLoginEvent();
                    }
                }));
        addDisposable(RxBus.getInstance()
                .toObservable(CancelCollectEvent.class)
                .subscribe(new Consumer<CancelCollectEvent>() {
                    @Override
                    public void accept(CancelCollectEvent cancelCollectEvent) throws Exception {
                        if (isViewAttached()) {
                            getView().subscribeCancelCollectEvent(cancelCollectEvent.getActivityName());
                        }
                    }
                }));
        addDisposable(RxBus.getInstance()
                .toObservable(CollectEvent.class)
                .subscribe(new Consumer<CollectEvent>() {
                    @Override
                    public void accept(CollectEvent collectEvent) throws Exception {
                        if (isViewAttached()) {
                            getView().subscribeCollectEvent(collectEvent.getActivityName());
                        }
                    }
                }));
    }
}
