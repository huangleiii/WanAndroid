package com.huanglei.wanandroid.vp.main.home;

import com.huanglei.wanandroid.base.presenter.RxMVPBasePresenter;
import com.huanglei.wanandroid.contract.HomeFragmentContract;
import com.huanglei.wanandroid.event.LoginExpiredEvent;
import com.huanglei.wanandroid.event.RxBus;
import com.huanglei.wanandroid.model.bean.Article;
import com.huanglei.wanandroid.model.bean.Banner;
import com.huanglei.wanandroid.model.bean.BaseResponse;
import com.huanglei.wanandroid.model.bean.ArticleList;
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
                .compose(RxUtils.<BaseResponse<ArticleList>>schedulerTransformer())
                .compose(RxUtils.<ArticleList>responseTransformer())
                .subscribe(new Consumer<ArticleList>() {
                    @Override
                    public void accept(ArticleList articleList) throws Exception {
                        if (isViewAttached())
                            getView().showNewListDataSucceed(articleList.getDatas());
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
                .compose(RxUtils.<BaseResponse<ArticleList>>schedulerTransformer())
                .compose(RxUtils.<ArticleList>responseTransformer())
                .subscribe(new Consumer<ArticleList>() {
                    @Override
                    public void accept(ArticleList articleList) throws Exception {
                        if (isViewAttached())
                            getView().showAddListDataSucceed(articleList.getDatas());
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
                        boolean isLoginExpired = false;
                        if (errorCode == BaseResponse.LOGIN_FAILED) {
                            LoginExpiredEvent loginExpiredEvent = new LoginExpiredEvent();
                            RxBus.getInstance().post(loginExpiredEvent);
                            isLoginExpired = true;
                        }
                        if (isViewAttached()) {
                            getView().showCollectFailed(position, articles, isLoginExpired, errorMessage);
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

    @Override
    protected void registerEvent() {

    }
}
