package com.huanglei.wanandroid.vp.main.knowledge;

import com.huanglei.wanandroid.base.presenter.RxMVPBasePresenter;
import com.huanglei.wanandroid.contract.KnowledgeArticleListFragmentContract;
import com.huanglei.wanandroid.contract.KnowledgeTabListsFragmentContract;
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
 * Created by 黄垒 on 2019/1/14.
 */

public class KnowledgeArticleListFragmentPresenter extends RxMVPBasePresenter<KnowledgeArticleListFragmentContract.View> implements KnowledgeArticleListFragmentContract.Presenter {
    @Override
    public void getKnowledgeArticleList(int id) {
        addDisposable(HttpHelper.getInstance()
                .getKnowledgeArticleList(0, id)
                .compose(RxUtils.<ArticleList>responseTransformer())
                .compose(RxUtils.<ArticleList>schedulerTransformer())
                .subscribe(new Consumer<ArticleList>() {
                    @Override
                    public void accept(ArticleList articleList) throws Exception {
                        if (isViewAttached()) {
                            getView().showKnowledgeArticleListSucceed(articleList.getDatas());
                        }
                    }
                }, new ErrorConsumer<Throwable>() {
                    @Override
                    protected void onError(int errorCode, String errorMessage) {
                        if (isViewAttached()) {
                            getView().showKnowledgeArticleListFailed(errorMessage);
                        }
                    }
                }));
    }

    @Override
    public void addKnowledgeArticleList(int num, int id) {
        addDisposable(HttpHelper.getInstance()
                .getKnowledgeArticleList(num, id)
                .compose(RxUtils.<ArticleList>responseTransformer())
                .compose(RxUtils.<ArticleList>schedulerTransformer())
                .subscribe(new Consumer<ArticleList>() {
                    @Override
                    public void accept(ArticleList articleList) throws Exception {
                        if (isViewAttached()) {
                            getView().showAddKnowledgeArticleListSucceed(articleList.getDatas());
                        }
                    }
                }, new ErrorConsumer<Throwable>() {
                    @Override
                    protected void onError(int errorCode, String errorMessage) {
                        if (isViewAttached()) {
                            getView().showAddKnowledgeArticleListFailed(errorMessage);
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
                            LoginExpiredEvent loginExpiredEvent=new LoginExpiredEvent();
                            RxBus.getInstance().post(loginExpiredEvent);
                            if(isViewAttached()){
                                getView().showCollectFailed(position,articles,true,errorMessage);
                            }
                        }else {
                            if(isViewAttached()){
                                getView().showCollectFailed(position,articles,false,errorMessage);
                            }
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
                            LoginExpiredEvent loginExpiredEvent=new LoginExpiredEvent();
                            RxBus.getInstance().post(loginExpiredEvent);
                            if(isViewAttached()){
                                getView().showCancelCollectFailed(position,articles,true,errorMessage);
                            }
                        }else {
                            if(isViewAttached()){
                                getView().showCancelCollectFailed(position,articles,false,errorMessage);
                            }

                        }
                    }
                }));

    }

    @Override
    protected void registerEvent() {

    }
}
