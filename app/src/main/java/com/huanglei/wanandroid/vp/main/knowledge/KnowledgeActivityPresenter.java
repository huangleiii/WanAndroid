package com.huanglei.wanandroid.vp.main.knowledge;

import com.huanglei.wanandroid.base.presenter.RxMVPBasePresenter;
import com.huanglei.wanandroid.base.view.IBaseView;
import com.huanglei.wanandroid.contract.KnowledgeActivityContract;
import com.huanglei.wanandroid.event.CancelCollectEvent;
import com.huanglei.wanandroid.event.CollectEvent;
import com.huanglei.wanandroid.event.LoginEvent;
import com.huanglei.wanandroid.event.RxBus;

import io.reactivex.functions.Consumer;

/**
 * Created by 黄垒 on 2019/1/14.
 */

public class KnowledgeActivityPresenter extends RxMVPBasePresenter<KnowledgeActivityContract.View>{
    @Override
    protected void registerEvent() {
        addDisposable(RxBus.getInstance().toObservable(LoginEvent.class)
                .subscribe(new Consumer<LoginEvent>() {
                    @Override
                    public void accept(LoginEvent loginEvent) throws Exception {
                        if (isViewAttached())
                            getView().subscribeLoginEvent(loginEvent.getActivityName());
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
