package com.huanglei.wanandroid.vp.main.weixin;

import com.huanglei.wanandroid.base.presenter.RxMVPBasePresenter;
import com.huanglei.wanandroid.contract.WeixinFragmentContract;
import com.huanglei.wanandroid.model.bean.Tab;
import com.huanglei.wanandroid.model.http.HttpHelper;
import com.huanglei.wanandroid.utils.ErrorConsumer;
import com.huanglei.wanandroid.utils.RxUtils;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by 黄垒 on 2019/1/11.
 */

public class WeixinFragmentPresenter extends RxMVPBasePresenter<WeixinFragmentContract.View> implements WeixinFragmentContract.Presenter {

    @Override
    public void getWxTabs() {
        addDisposable(HttpHelper.getInstance()
                .getWxTrees()
                .compose(RxUtils.<List<Tab>>responseTransformer())
                .compose(RxUtils.<List<Tab>>schedulerTransformer())
                .subscribe(new Consumer<List<Tab>>() {
                    @Override
                    public void accept(List<Tab> tabs) throws Exception {
                        if(isViewAttached()){
                            getView().showWxTabsSucceed(tabs);
                        }
                    }
                }, new ErrorConsumer<Throwable>() {
                    @Override
                    protected void onError(int errorCode, String errorMessage) {
                        if(isViewAttached()){
                            getView().showWxTabsFailed(errorMessage);
                        }
                    }
                }));
    }

    @Override
    protected void registerEvent() {

    }
}
