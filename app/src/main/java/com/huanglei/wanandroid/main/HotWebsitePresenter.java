package com.huanglei.wanandroid.main;

import com.huanglei.wanandroid.base.presenter.RxMVPBasePresenter;
import com.huanglei.wanandroid.contract.HotWebsiteActivityContract;
import com.huanglei.wanandroid.model.bean.BaseResponse;
import com.huanglei.wanandroid.model.bean.HotWebsite;
import com.huanglei.wanandroid.model.http.HttpHelper;
import com.huanglei.wanandroid.utils.ErrorConsumer;
import com.huanglei.wanandroid.utils.RxUtils;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by 黄垒 on 2018/12/26.
 */

public class HotWebsitePresenter extends RxMVPBasePresenter<HotWebsiteActivityContract.View> implements HotWebsiteActivityContract.Presenter{

    @Override
    public void getHotWebsiteList() {
        addDisposable(HttpHelper.getInstance()
        .getHotWebsiteList()
        .compose(RxUtils.<BaseResponse<List<HotWebsite>>>schedulerTransformer())
        .compose(RxUtils.<List<HotWebsite>>responseTransformer())
        .subscribe(new Consumer<List<HotWebsite>>() {
            @Override
            public void accept(List<HotWebsite> hotWebsites) throws Exception {
                if (isViewAttached()) {
                    getView().showHotWebsiteListSucceed(hotWebsites);
                }
            }
        }, new ErrorConsumer<Throwable>() {
            @Override
            protected void onError(int errorCode, String errorMessage) {
                if(isViewAttached()){
                    getView().showHotWebsiteListFailed(errorMessage);
                }
            }
        }));
    }

    @Override
    protected void registerEvent() {

    }
}
