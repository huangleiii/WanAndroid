package com.huanglei.wanandroid.main;

import com.huanglei.wanandroid.base.presenter.RxMVPBasePresenter;
import com.huanglei.wanandroid.contract.HomeFragmentContract;
import com.huanglei.wanandroid.event.CancelCollectEvent;
import com.huanglei.wanandroid.event.CollectEvent;
import com.huanglei.wanandroid.event.LoginEvent;
import com.huanglei.wanandroid.event.LoginExpiredEvent;
import com.huanglei.wanandroid.event.LogoutEvent;
import com.huanglei.wanandroid.event.RxBus;
import com.huanglei.wanandroid.model.bean.Article;
import com.huanglei.wanandroid.model.bean.Banner;
import com.huanglei.wanandroid.model.bean.BaseResponse;
import com.huanglei.wanandroid.model.bean.HomeArticleList;
import com.huanglei.wanandroid.model.http.HttpHelper;
import com.huanglei.wanandroid.utils.ErrorConsumer;
import com.huanglei.wanandroid.utils.RxUtils;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by HuangLei on 2018/11/19.
 */

public class HomeFragmentPresenter extends RxMVPBasePresenter<HomeFragmentContract.View> implements HomeFragmentContract.Presenter {
    @Override
    public void getListData() {
        addDisposable(HttpHelper.getInstance()
                .getHomeArticleListData(0)
                .compose(RxUtils.<BaseResponse<HomeArticleList>>schedulerTransformer())
                .compose(RxUtils.<HomeArticleList>responseTransformer())
                .subscribe(new Consumer<HomeArticleList>() {
                    @Override
                    public void accept(HomeArticleList homeArticleList) throws Exception {
                        if (isViewAttached())
                            getView().showNewListDataSucceed(homeArticleList.getDatas());
                    }
                }, new ErrorConsumer<Throwable>() {
                    @Override
                    protected void onError(int errorCode, String errorMessage) {
                        if (isViewAttached())
                            getView().showNewListDataFailed(errorMessage);
                    }
                }));
    }

    @Override
    public void getBannerData() {
        addDisposable(HttpHelper.getInstance()
                .getBannerListData()
                .compose(RxUtils.<BaseResponse<List<Banner>>>schedulerTransformer())
                .compose(RxUtils.<List<Banner>>responseTransformer())
                .subscribe(new Consumer<List<Banner>>() {
                    @Override
                    public void accept(List<Banner> bannerData) throws Exception {
                        if (isViewAttached())
                            getView().showNewBannerDataSucceed(bannerData);
                    }
                }, new ErrorConsumer<Throwable>() {
                    @Override
                    protected void onError(int errorCode, String errorMessage) {
                        if (isViewAttached())
                            getView().showNewBannerDataFailed(errorMessage);
                    }
                }));
    }

    @Override
    public void addListData(int page) {
        addDisposable(HttpHelper.getInstance()
                .getHomeArticleListData(page)
                .compose(RxUtils.<BaseResponse<HomeArticleList>>schedulerTransformer())
                .compose(RxUtils.<HomeArticleList>responseTransformer())
                .subscribe(new Consumer<HomeArticleList>() {
                    @Override
                    public void accept(HomeArticleList homeArticleList) throws Exception {
                        if (isViewAttached())
                            getView().showAddListDataSucceed(homeArticleList.getDatas());
                    }
                }, new ErrorConsumer<Throwable>() {
                    @Override
                    protected void onError(int errorCode, String errorMessage) {
                        if (isViewAttached())
                            getView().showAddListDataFailed(errorMessage);
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
                            getView().showCollectFailed(position, errorMessage);
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
                            getView().showCancelCollectFailed(position, errorMessage);
                        }
                    }
                }));
    }


    @Override
    protected void registerEvent() {
        addDisposable(RxBus.getInstance()
                .toObservable(CancelCollectEvent.class)
                .subscribe(new Consumer<CancelCollectEvent>() {
                    @Override
                    public void accept(CancelCollectEvent cancelCollectEvent) throws Exception {
                        if (isViewAttached()) {
                            getView().showCancelCollectEvent(cancelCollectEvent.getPosition());
                        }
                    }
                }));
        addDisposable(RxBus.getInstance()
                .toObservable(CollectEvent.class)
                .subscribe(new Consumer<CollectEvent>() {
                    @Override
                    public void accept(CollectEvent collectEvent) throws Exception {
                        if (isViewAttached()) {
                            getView().showCollectEvent(collectEvent.getPosition());
                        }
                    }
                }));
        addDisposable(RxBus.getInstance()
                .toObservable(LoginEvent.class)
                .subscribe(new Consumer<LoginEvent>() {
                    @Override
                    public void accept(LoginEvent loginEvent) throws Exception {
                        if(isViewAttached()){
                            getView().showLoginEvent();
                        }
                    }
                }));
        addDisposable(RxBus.getInstance()
                .toObservable(LogoutEvent.class)
                .subscribe(new Consumer<LogoutEvent>() {
                    @Override
                    public void accept(LogoutEvent logoutEvent) throws Exception {
                        if(isViewAttached()){
                            getView().showLogoutEvent();
                        }
                    }
                }));
    }
}
