package com.huanglei.wanandroid.main;

import com.huanglei.wanandroid.base.presenter.RxMVPBasePresenter;
import com.huanglei.wanandroid.contract.ArticleDetailActivityContract;
import com.huanglei.wanandroid.event.CancelCollectEvent;
import com.huanglei.wanandroid.event.CollectEvent;
import com.huanglei.wanandroid.event.LoginExpiredEvent;
import com.huanglei.wanandroid.event.RxBus;
import com.huanglei.wanandroid.model.bean.BaseResponse;
import com.huanglei.wanandroid.model.http.HttpHelper;
import com.huanglei.wanandroid.utils.ErrorConsumer;
import com.huanglei.wanandroid.utils.RxUtils;

import io.reactivex.functions.Consumer;

/**
 * Created by 黄垒 on 2018/12/6.
 */

public class ArticleDetailActivityPresenter extends RxMVPBasePresenter<ArticleDetailActivityContract.View> implements ArticleDetailActivityContract.Presenter {

    @Override
    public void collect( int id) {
        addDisposable(HttpHelper.getInstance()
                .collect(id)
                .compose(RxUtils.noDataResponseTransformer())
                .compose(RxUtils.schedulerTransformer())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (isViewAttached()) {
                            getView().showCollectSucceed();
                            CollectEvent collectEvent = new CollectEvent();
                            RxBus.getInstance().post(collectEvent);
                        }
                    }
                }, new ErrorConsumer<Throwable>() {
                    @Override
                    protected void onError(int errorCode, String errorMessage) {
                        if(errorCode== BaseResponse.LOGIN_FAILED){
                            RxBus.getInstance().post(new LoginExpiredEvent());
                        }
                        if (isViewAttached()) {
                            getView().showCollectFailed(errorMessage);
                        }
                    }
                }));
    }

    @Override
    public void cancelCollect(int id) {
        addDisposable(HttpHelper.getInstance()
                .cancelCollect(id)
                .compose(RxUtils.noDataResponseTransformer())
                .compose(RxUtils.schedulerTransformer())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (isViewAttached()) {
                            getView().showCancelCollectSucceed();
                            CancelCollectEvent cancelCollectEvent = new CancelCollectEvent();
                            RxBus.getInstance().post(cancelCollectEvent);
                        }
                    }
                }, new ErrorConsumer<Throwable>() {
                    @Override
                    protected void onError(int errorCode, String errorMessage) {
                        if(errorCode== BaseResponse.LOGIN_FAILED){
                            RxBus.getInstance().post(new LoginExpiredEvent());
                        }
                        if (isViewAttached()) {
                            getView().showCancelCollectFailed(errorMessage);
                        }
                    }
                }));
    }

    @Override
    protected void registerEvent() {

    }
}
