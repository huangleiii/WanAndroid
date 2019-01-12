package com.huanglei.wanandroid.vp.main.project;

import com.huanglei.wanandroid.base.presenter.RxMVPBasePresenter;
import com.huanglei.wanandroid.contract.ProjectListFragmentContract;
import com.huanglei.wanandroid.event.LoginExpiredEvent;
import com.huanglei.wanandroid.event.RxBus;
import com.huanglei.wanandroid.model.bean.Article;
import com.huanglei.wanandroid.model.bean.ArticleList;
import com.huanglei.wanandroid.model.bean.BaseResponse;
import com.huanglei.wanandroid.model.http.HttpHelper;
import com.huanglei.wanandroid.utils.ErrorConsumer;
import com.huanglei.wanandroid.utils.RxUtils;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by 黄垒 on 2019/1/12.
 */

public class ProjectListFragmentPresenter extends RxMVPBasePresenter<ProjectListFragmentContract.View> implements ProjectListFragmentContract.Presenter {


    @Override
    public void getProjectArticles(int id) {
        addDisposable(HttpHelper.getInstance()
                .getProjectArticles(1, id)
                .compose(RxUtils.<BaseResponse<ArticleList>>schedulerTransformer())
                .compose(RxUtils.<ArticleList>responseTransformer())
                .subscribe(new Consumer<ArticleList>() {
                    @Override
                    public void accept(ArticleList articleList) throws Exception {
                        if (isViewAttached()) {
                            getView().showProjectArticlesSucceed(articleList.getDatas());
                        }
                    }
                }, new ErrorConsumer<Throwable>() {
                    @Override
                    protected void onError(int errorCode, String errorMessage) {
                        if (isViewAttached()) {
                            getView().showProjectArticlesFailed(errorMessage);
                        }
                    }
                }));
    }

    @Override
    public void addProjectArticles(int num, int id) {
        addDisposable(HttpHelper.getInstance()
                .getProjectArticles(num, id)
                .compose(RxUtils.<BaseResponse<ArticleList>>schedulerTransformer())
                .compose(RxUtils.<ArticleList>responseTransformer())
                .subscribe(new Consumer<ArticleList>() {
                    @Override
                    public void accept(ArticleList articleList) throws Exception {
                        if (isViewAttached()) {
                            getView().showAddProjectArticlesSucceed(articleList.getDatas());
                        }
                    }
                }, new ErrorConsumer<Throwable>() {
                    @Override
                    protected void onError(int errorCode, String errorMessage) {
                        if (isViewAttached()) {
                            getView().showAddProjectArticlesFailed(errorMessage);
                        }
                    }
                }));
    }

    @Override
    public void collect(final int position, final List<Article> articles) {
        addDisposable(HttpHelper.getInstance()
                .collect(articles.get(position).getId())
                .compose(RxUtils.noDataResponseTransformer())
                .compose(RxUtils.schedulerTransformer())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (isViewAttached()) {
                            getView().showCollectSucceed(position, articles);
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
                            getView().showCollectFailed(position, articles, isLoginExpired, errorMessage);
                        }
                    }
                }));
    }

    @Override
    public void cancelCollect(final int position, final List<Article> articles) {
        addDisposable(HttpHelper.getInstance()
                .cancelCollect(articles.get(position).getId())
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

    @Override
    protected void registerEvent() {

    }

}
