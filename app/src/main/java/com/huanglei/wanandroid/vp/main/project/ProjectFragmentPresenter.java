package com.huanglei.wanandroid.vp.main.project;

import com.huanglei.wanandroid.base.presenter.RxMVPBasePresenter;
import com.huanglei.wanandroid.contract.ProjectFragmentContract;
import com.huanglei.wanandroid.model.bean.Tab;
import com.huanglei.wanandroid.model.http.HttpHelper;
import com.huanglei.wanandroid.utils.ErrorConsumer;
import com.huanglei.wanandroid.utils.RxUtils;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by 黄垒 on 2019/1/12.
 */

public class ProjectFragmentPresenter extends RxMVPBasePresenter<ProjectFragmentContract.View> implements ProjectFragmentContract.Presenter {

    @Override
    public void getProjectTabs() {
        addDisposable(HttpHelper.getInstance()
                .getProjectTrees()
                .compose(RxUtils.<List<Tab>>responseTransformer())
                .compose(RxUtils.<List<Tab>>schedulerTransformer())
                .subscribe(new Consumer<List<Tab>>() {
                    @Override
                    public void accept(List<Tab> tabs) throws Exception {
                        if(isViewAttached()){
                            getView().showProjectTabsSucceed(tabs);
                        }
                    }
                }, new ErrorConsumer<Throwable>() {
                    @Override
                    protected void onError(int errorCode, String errorMessage) {
                        if(isViewAttached()){
                            getView().showProjectTabsFailed(errorMessage);
                        }
                    }
                }));
    }

    @Override
    protected void registerEvent() {

    }
}
