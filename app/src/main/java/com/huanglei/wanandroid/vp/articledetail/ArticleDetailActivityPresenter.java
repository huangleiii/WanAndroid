package com.huanglei.wanandroid.vp.articledetail;

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
    public void collect(final String activityName, int id) {
        addDisposable(HttpHelper.getInstance()
                .collect(id)
                .compose(RxUtils.noDataResponseTransformer())
                .compose(RxUtils.schedulerTransformer())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        CollectEvent collectEvent = new CollectEvent();
                        collectEvent.setActivityName(activityName);
                        RxBus.getInstance().post(collectEvent);
                        if (isViewAttached()) {
                            getView().showCollectSucceed();
                        }
                    }
                }, new ErrorConsumer<Throwable>() {
                    @Override
                    protected void onError(int errorCode, String errorMessage) {
                        boolean isLoginExpired=false;
                        if (errorCode == BaseResponse.LOGIN_FAILED) {
                            LoginExpiredEvent loginExpiredEvent = new LoginExpiredEvent();
                            RxBus.getInstance().post(loginExpiredEvent);
                            isLoginExpired=true;
                        }
                        if (isViewAttached()) {
                            getView().showCollectFailed(isLoginExpired,errorMessage);
                        }
                    }
                }));
    }

    @Override
    public void cancelCollect(final String activityName, int id) {
        addDisposable(HttpHelper.getInstance()
                .cancelCollect(id)
                .compose(RxUtils.noDataResponseTransformer())
                .compose(RxUtils.schedulerTransformer())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        CancelCollectEvent cancelCollectEvent = new CancelCollectEvent();
                        cancelCollectEvent.setActivityName(activityName);
                        RxBus.getInstance().post(cancelCollectEvent);
                        if (isViewAttached()) {
                            getView().showCancelCollectSucceed();
                        }
                    }
                }, new ErrorConsumer<Throwable>() {
                    @Override
                    protected void onError(int errorCode, String errorMessage) {
                        boolean isLoginExpired=false;
                        if (errorCode == BaseResponse.LOGIN_FAILED) {
                            LoginExpiredEvent loginExpiredEvent = new LoginExpiredEvent();
                            RxBus.getInstance().post(loginExpiredEvent);
                            isLoginExpired=true;
                        }
                        if (isViewAttached()) {
                            getView().showCancelCollectFailed(isLoginExpired,errorMessage);
                        }
                    }
                }));
    }

    @Override
    protected void registerEvent() {

    }
}
