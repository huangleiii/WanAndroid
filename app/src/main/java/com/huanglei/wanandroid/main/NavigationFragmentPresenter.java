package com.huanglei.wanandroid.main;

import com.huanglei.wanandroid.base.presenter.RxMVPBasePresenter;
import com.huanglei.wanandroid.contract.NavigationFragmentContract;
import com.huanglei.wanandroid.model.bean.BaseResponse;
import com.huanglei.wanandroid.model.bean.NavigationArticleList;
import com.huanglei.wanandroid.model.http.HttpHelper;
import com.huanglei.wanandroid.utils.ErrorConsumer;
import com.huanglei.wanandroid.utils.RxUtils;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by 黄垒 on 2019/1/10.
 */

public class NavigationFragmentPresenter extends RxMVPBasePresenter<NavigationFragmentContract.View> implements NavigationFragmentContract.Presenter {
    @Override
    public void getNavigationLists() {
        addDisposable(HttpHelper.getInstance()
                .getNavigationLists()
                .compose(RxUtils.<BaseResponse<List<NavigationArticleList>>>schedulerTransformer())
                .compose(RxUtils.<List<NavigationArticleList>>responseTransformer())
                .subscribe(new Consumer<List<NavigationArticleList>>() {
                    @Override
                    public void accept(List<NavigationArticleList> navigationArticleLists) throws Exception {
                        if(isViewAttached()){
                            getView().showNavigationListSucceed(navigationArticleLists);
                        }
                    }
                }, new ErrorConsumer<Throwable>() {
                    @Override
                    protected void onError(int errorCode, String errorMessage) {
                        if(isViewAttached()){
                            getView().showNavigationListFailed(errorMessage);
                        }
                    }
                }));
    }

    @Override
    protected void registerEvent() {

    }
}
