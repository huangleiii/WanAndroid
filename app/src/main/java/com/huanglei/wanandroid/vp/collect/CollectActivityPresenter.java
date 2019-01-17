package com.huanglei.wanandroid.vp.collect;

import com.huanglei.wanandroid.base.presenter.RxMVPBasePresenter;
import com.huanglei.wanandroid.contract.CollectActivityContract;
import com.huanglei.wanandroid.event.CancelCollectEvent;
import com.huanglei.wanandroid.event.CollectEvent;
import com.huanglei.wanandroid.event.LoginEvent;
import com.huanglei.wanandroid.event.LoginExpiredEvent;
import com.huanglei.wanandroid.event.RxBus;
import com.huanglei.wanandroid.model.bean.ArticleInCollectPage;
import com.huanglei.wanandroid.model.bean.ArticleListInCollectPage;
import com.huanglei.wanandroid.model.bean.BaseResponse;
import com.huanglei.wanandroid.model.http.HttpHelper;
import com.huanglei.wanandroid.utils.ErrorConsumer;
import com.huanglei.wanandroid.utils.RxUtils;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by 黄垒 on 2019/1/7.
 */

public class CollectActivityPresenter extends RxMVPBasePresenter<CollectActivityContract.View> implements CollectActivityContract.Presenter {

    @Override
    public void getCollectArticles() {
        addDisposable(HttpHelper.getInstance()
                .getCollectArticles(0)
                .compose(RxUtils.<ArticleListInCollectPage>responseTransformer())
                .compose(RxUtils.<ArticleListInCollectPage>schedulerTransformer())
                .subscribe(new Consumer<ArticleListInCollectPage>() {
                    @Override
                    public void accept(ArticleListInCollectPage articleListInCollectPage) throws Exception {
                        if (isViewAttached()) {
                            getView().showCollectArticlesSucceed(articleListInCollectPage.getDatas());
                        }
                    }
                }, new ErrorConsumer<Throwable>() {
                    @Override
                    protected void onError(int errorCode, String errorMessage) {
                        if (isViewAttached()) {
                            getView().showCollectArticlesFailed(errorMessage);
                        }
                    }
                }));
    }

    @Override
    public void addCollectArticles(int page) {
        addDisposable(HttpHelper.getInstance()
                .getCollectArticles(page)
                .compose(RxUtils.<ArticleListInCollectPage>responseTransformer())
                .compose(RxUtils.<ArticleListInCollectPage>schedulerTransformer())
                .subscribe(new Consumer<ArticleListInCollectPage>() {
                    @Override
                    public void accept(ArticleListInCollectPage articleListInCollectPage) throws Exception {
                        if (isViewAttached()) {
                            getView().showAddCollectArticlesSucceed(articleListInCollectPage.getDatas());
                        }
                    }
                }, new ErrorConsumer<Throwable>() {
                    @Override
                    protected void onError(int errorCode, String errorMessage) {
                        if (isViewAttached()) {
                            getView().showAddCollectArticlesFailed(errorMessage);
                        }
                    }
                }));

    }

    @Override
    public void cancelCollect(final int position, final List<ArticleInCollectPage> articles) {
        int id=articles.get(position).getId();
        int orginId=articles.get(position).getOriginId();
        if(orginId>=0){
            addDisposable(HttpHelper.getInstance()
                    .cancelCollect(orginId)
                    .compose(RxUtils.noDataResponseTransformer())
                    .compose(RxUtils.schedulerTransformer())
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            if (isViewAttached()) {
                                getView().showCancelCollectSucceed(position, articles);
                            }
                        }
                    }, new ErrorConsumer<Throwable>() {
                        @Override
                        protected void onError(int errorCode, String errorMessage) {
                            boolean isLoginExpired = false;
                            if (errorCode == BaseResponse.LOGIN_FAILED) {
                                LoginExpiredEvent loginExpiredEvent = new LoginExpiredEvent();
                                RxBus.getInstance().post(loginExpiredEvent);
                                isLoginExpired = true;
                            }
                            if (isViewAttached()) {
                                getView().showCancelCollectFailed(position, articles, isLoginExpired, errorMessage);
                            }
                        }
                    }));
        }else {
            addDisposable(HttpHelper.getInstance()
                .cancelCollectInCollectPage(id,orginId)
                    .compose(RxUtils.noDataResponseTransformer())
                    .compose(RxUtils.schedulerTransformer())
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            if (isViewAttached()) {
                                getView().showCancelCollectSucceed(position, articles);
                            }
                        }
                    }, new ErrorConsumer<Throwable>() {
                        @Override
                        protected void onError(int errorCode, String errorMessage) {
                            boolean isLoginExpired = false;
                            if (errorCode == BaseResponse.LOGIN_FAILED) {
                                LoginExpiredEvent loginExpiredEvent = new LoginExpiredEvent();
                                RxBus.getInstance().post(loginExpiredEvent);
                                isLoginExpired = true;
                            }
                            if (isViewAttached()) {
                                getView().showCancelCollectFailed(position, articles, isLoginExpired, errorMessage);
                            }
                        }
                    }));
        }

    }

    @Override
    public void collectOutsideArticle(final String title, final String author, final String link) {
        addDisposable(HttpHelper.getInstance()
                .collectOutsideArticle(title, author, link)
                .compose(RxUtils.noDataResponseTransformer())
                .compose(RxUtils.schedulerTransformer())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if(isViewAttached()){
                            getView().showCollectOutsideArticleSucceed(title,author,link);
                        }
                    }
                }, new ErrorConsumer<Throwable>() {
                    @Override
                    protected void onError(int errorCode, String errorMessage) {
                        boolean isLoginExpired = false;
                        if (errorCode == BaseResponse.LOGIN_FAILED) {
                            LoginExpiredEvent loginExpiredEvent = new LoginExpiredEvent();
                            RxBus.getInstance().post(loginExpiredEvent);
                            isLoginExpired = true;
                        }
                        if (isViewAttached()) {
                            getView().showCollectOutsideArticleFailed(isLoginExpired, errorMessage);
                        }
                    }
                }));
    }


    @Override
    protected void registerEvent() {
        addDisposable(RxBus.getInstance()
                .toObservable(LoginEvent.class)
                .subscribe(new Consumer<LoginEvent>() {
                    @Override
                    public void accept(LoginEvent loginEvent) throws Exception {
                        if (isViewAttached()) {
                            getView().subscribeLoginEvent(loginEvent.getActivityName());
                        }
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
