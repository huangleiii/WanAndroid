package com.huanglei.wanandroid.main;

import com.huanglei.wanandroid.base.presenter.RxMVPBasePresenter;
import com.huanglei.wanandroid.contract.CollectActivityContract;
import com.huanglei.wanandroid.event.CancelCollectEvent;
import com.huanglei.wanandroid.event.CollectEvent;
import com.huanglei.wanandroid.event.LoginEvent;
import com.huanglei.wanandroid.event.LoginExpiredEvent;
import com.huanglei.wanandroid.event.RxBus;
import com.huanglei.wanandroid.model.bean.ArticleInCollectPage;
import com.huanglei.wanandroid.model.bean.ArticleInCollectPageList;
import com.huanglei.wanandroid.model.bean.BaseResponse;
import com.huanglei.wanandroid.model.bean.ArticleList;
import com.huanglei.wanandroid.model.http.HttpHelper;
import com.huanglei.wanandroid.model.sharedpreferences.SharedPreferencesHelper;
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
                .compose(RxUtils.<ArticleInCollectPageList>responseTransformer())
                .compose(RxUtils.<ArticleInCollectPageList>schedulerTransformer())
                .subscribe(new Consumer<ArticleInCollectPageList>() {
                    @Override
                    public void accept(ArticleInCollectPageList articleInCollectPageList) throws Exception {
                        if (isViewAttached()) {
                            getView().showCollectArticlesSucceed(articleInCollectPageList.getDatas());
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
                .compose(RxUtils.<ArticleInCollectPageList>responseTransformer())
                .compose(RxUtils.<ArticleInCollectPageList>schedulerTransformer())
                .subscribe(new Consumer<ArticleInCollectPageList>() {
                    @Override
                    public void accept(ArticleInCollectPageList articleInCollectPageList) throws Exception {
                        if (isViewAttached()) {
                            getView().showAddCollectArticlesSucceed(articleInCollectPageList.getDatas());
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
        addDisposable(HttpHelper.getInstance()
//                .cancelCollectInCollectPage(articles.get(position).getId())
                .cancelCollect(articles.get(position).getOriginId())
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
                        if (errorCode == BaseResponse.LOGIN_FAILED) {
                            RxBus.getInstance().post(new LoginExpiredEvent());
                        }
                        if (isViewAttached()) {
                            getView().showCancelCollectFailed(position,articles, errorMessage);
                        }
                    }
                }));
    }

    @Override
    public void setCurrentActivity(String activityName) {
        SharedPreferencesHelper.getInstance().setCurrentActivity(activityName);
    }

    @Override
    public String getCurrentActivity() {
        return SharedPreferencesHelper.getInstance().getCurrentActivity();
    }

    @Override
    protected void registerEvent() {
        addDisposable(RxBus.getInstance()
                .toObservable(LoginEvent.class)
                .subscribe(new Consumer<LoginEvent>() {
                    @Override
                    public void accept(LoginEvent loginEvent) throws Exception {
                        if (isViewAttached()) {
                            getView().subscribeLoginEvent();
                        }
                    }
                }));
        addDisposable(RxBus.getInstance()
                .toObservable(CancelCollectEvent.class)
                .subscribe(new Consumer<CancelCollectEvent>() {
                    @Override
                    public void accept(CancelCollectEvent cancelCollectEvent) throws Exception {
                        if (isViewAttached()) {
                            getView().subscribeCancelCollectEvent();
                        }
                    }
                }));
        addDisposable(RxBus.getInstance()
                .toObservable(CollectEvent.class)
                .subscribe(new Consumer<CollectEvent>() {
                    @Override
                    public void accept(CollectEvent collectEvent) throws Exception {
                        if (isViewAttached()) {
                            getView().subscribeCollectEvent();
                        }
                    }
                }));
    }
}
