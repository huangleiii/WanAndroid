package com.huanglei.wanandroid;

import com.huanglei.wanandroid.base.presenter.RxBasePresenter;
import com.huanglei.wanandroid.contract.HomeFragmentContract;
import com.huanglei.wanandroid.event.CancelCollectEvent;
import com.huanglei.wanandroid.event.CollectEvent;
import com.huanglei.wanandroid.event.LoginEvent;
import com.huanglei.wanandroid.event.LoginExpiredEvent;
import com.huanglei.wanandroid.event.LogoutEvent;
import com.huanglei.wanandroid.event.RxBus;
import com.huanglei.wanandroid.model.bean.Article;
import com.huanglei.wanandroid.model.bean.BannerData;
import com.huanglei.wanandroid.model.bean.BaseResponse;
import com.huanglei.wanandroid.model.bean.HomeArticleListData;
import com.huanglei.wanandroid.model.http.HttpHelper;
import com.huanglei.wanandroid.utils.ErrorConsumer;
import com.huanglei.wanandroid.utils.RxUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import retrofit2.http.HTTP;

/**
 * Created by HuangLei on 2018/11/19.
 */

public class HomeFragmentPresenter extends RxBasePresenter<HomeFragmentContract.View> implements HomeFragmentContract.Presenter {
    @Override
    public void getListData() {
        addDisposable(HttpHelper.getInstance()
                .getHomeArticleListData(0)
                .compose(RxUtils.<BaseResponse<HomeArticleListData>>schedulerTransformer())
                .compose(RxUtils.<HomeArticleListData>responseTransformer())
                .subscribe(new Consumer<HomeArticleListData>() {
                    @Override
                    public void accept(HomeArticleListData homeArticleListData) throws Exception {
                        if (isViewAttached())
                            getView().showNewListDataSucceed(homeArticleListData.getDatas());
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
                .compose(RxUtils.<BaseResponse<List<BannerData>>>schedulerTransformer())
                .compose(RxUtils.<List<BannerData>>responseTransformer())
                .subscribe(new Consumer<List<BannerData>>() {
                    @Override
                    public void accept(List<BannerData> bannerData) throws Exception {
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
                .compose(RxUtils.<BaseResponse<HomeArticleListData>>schedulerTransformer())
                .compose(RxUtils.<HomeArticleListData>responseTransformer())
                .subscribe(new Consumer<HomeArticleListData>() {
                    @Override
                    public void accept(HomeArticleListData homeArticleListData) throws Exception {
                        if (isViewAttached())
                            getView().showAddListDataSucceed(homeArticleListData.getDatas());
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
