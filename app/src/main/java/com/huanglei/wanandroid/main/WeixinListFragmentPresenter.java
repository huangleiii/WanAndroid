package com.huanglei.wanandroid.main;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.huanglei.wanandroid.base.presenter.RxMVPBasePresenter;
import com.huanglei.wanandroid.contract.WeixinFragmentContract;
import com.huanglei.wanandroid.contract.WeixinListFragmentContract;
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
 * Created by 黄垒 on 2019/1/11.
 */

public class WeixinListFragmentPresenter extends RxMVPBasePresenter<WeixinListFragmentContract.View> implements WeixinListFragmentContract.Presenter {


    @Override
    public void getWxArticles(int id) {
        addDisposable(HttpHelper.getInstance()
                .getWxArticles(1, id)
                .compose(RxUtils.<BaseResponse<ArticleList>>schedulerTransformer())
                .compose(RxUtils.<ArticleList>responseTransformer())
                .subscribe(new Consumer<ArticleList>() {
                    @Override
                    public void accept(ArticleList articleList) throws Exception {
                        if (isViewAttached()) {
                            getView().showWxArticlesSucceed(articleList.getDatas());
                        }
                    }
                }, new ErrorConsumer<Throwable>() {
                    @Override
                    protected void onError(int errorCode, String errorMessage) {
                        if (isViewAttached()) {
                            getView().showWxArticlesFailed(errorMessage);
                        }
                    }
                }));
    }

    @Override
    public void addWxArticles(int num, int id) {
        addDisposable(HttpHelper.getInstance()
                .getWxArticles(num, id)
                .compose(RxUtils.<BaseResponse<ArticleList>>schedulerTransformer())
                .compose(RxUtils.<ArticleList>responseTransformer())
                .subscribe(new Consumer<ArticleList>() {
                    @Override
                    public void accept(ArticleList articleList) throws Exception {
                        if (isViewAttached()) {
                            getView().showAddWxArticlesSucceed(articleList.getDatas());
                        }
                    }
                }, new ErrorConsumer<Throwable>() {
                    @Override
                    protected void onError(int errorCode, String errorMessage) {
                        if (isViewAttached()) {
                            getView().showAddWxArticlesFailed(errorMessage);
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
                        if(isViewAttached()){
                            getView().showCollectSucceed(position,articles);
                        }
                    }
                }, new ErrorConsumer<Throwable>() {
                    @Override
                    protected void onError(int errorCode, String errorMessage) {
                        if(errorCode== BaseResponse.LOGIN_FAILED){
                            RxBus.getInstance().post(new LoginExpiredEvent());
                        }
                        if(isViewAttached()){
                            getView().showCollectFailed(position,articles,errorMessage);
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
                        if(isViewAttached()){
                            getView().showCancelCollectSucceed(position,articles);
                        }
                    }
                }, new ErrorConsumer<Throwable>() {
                    @Override
                    protected void onError(int errorCode, String errorMessage) {
                        if(errorCode==BaseResponse.LOGIN_FAILED){
                            RxBus.getInstance().post(new LoginExpiredEvent());
                        }
                        if(isViewAttached()){
                            getView().showCanaelCollectFailed(position,articles,errorMessage);
                        }
                    }
                }));
    }

    @Override
    protected void registerEvent() {

    }

}
