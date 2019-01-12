package com.huanglei.wanandroid.vp.search;

import com.huanglei.wanandroid.base.presenter.RxMVPBasePresenter;
import com.huanglei.wanandroid.contract.SearchActivityContract;
import com.huanglei.wanandroid.model.bean.BaseResponse;
import com.huanglei.wanandroid.model.bean.HotKey;
import com.huanglei.wanandroid.model.db.DbHelper;
import com.huanglei.wanandroid.model.http.HttpHelper;
import com.huanglei.wanandroid.utils.ErrorConsumer;
import com.huanglei.wanandroid.utils.RxUtils;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by 黄垒 on 2018/12/26.
 */

public class SearchActivityPresenter extends RxMVPBasePresenter<SearchActivityContract.View> implements SearchActivityContract.Presenter{

    @Override
    public void getHotSearchKeywords() {
        addDisposable(HttpHelper.getInstance()
        .getSearchHotKeys()
        .compose(RxUtils.<BaseResponse<List<HotKey>>>schedulerTransformer())
        .compose(RxUtils.<List<HotKey>>responseTransformer())
        .subscribe(new Consumer<List<HotKey>>() {
            @Override
            public void accept(List<HotKey> hotKeys) throws Exception {
                if(isViewAttached()){
                    getView().showHotSearchKeywordsSucceed(hotKeys);
                }
            }
        }, new ErrorConsumer<Throwable>() {
            @Override
            protected void onError(int errorCode, String errorMessage) {
                if(isViewAttached()){
                    getView().showHotSearchKeywordsFailed(errorMessage);
                }
            }
        }));
    }

    @Override
    public void getHistorySearchKeywords() {
        if(isViewAttached()){
            getView().showHistorySearchKeywordsSucceed(DbHelper.getInstance().getHistoryKeywords());
        }
    }

    @Override
    public void addHistorySearchKeyword(String keyword) {
        if(isViewAttached()){
            getView().showAddHistorySearchKeywordSucceed(DbHelper.getInstance().addHistoryKeyword(keyword));
        }
    }

    @Override
    public void deleteHistorySearchKeyword(int position, String keyword) {
        DbHelper.getInstance().deleteHistoryKeyword(keyword);
        if(isViewAttached()){
            getView().showDeleteHistorySearchKeywordSucceed(position);
        }
    }

    @Override
    public void clearHistorySearchKeywords() {
        DbHelper.getInstance().clearHistoryKeywords();
        if(isViewAttached()){
            getView().showClearHistorySearchKeywordsSucceed();
        }

    }

    @Override
    protected void registerEvent() {

    }
}
