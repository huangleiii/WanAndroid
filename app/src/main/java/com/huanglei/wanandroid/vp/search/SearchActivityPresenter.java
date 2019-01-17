package com.huanglei.wanandroid.vp.search;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.huanglei.wanandroid.base.presenter.RxMVPBasePresenter;
import com.huanglei.wanandroid.contract.SearchActivityContract;
import com.huanglei.wanandroid.model.bean.BaseResponse;
import com.huanglei.wanandroid.model.bean.HotKey;
import com.huanglei.wanandroid.model.db.DbHelper;
import com.huanglei.wanandroid.model.db.HistoryKeyword;
import com.huanglei.wanandroid.model.db.MySingleThreadPool;
import com.huanglei.wanandroid.model.http.HttpHelper;
import com.huanglei.wanandroid.utils.ErrorConsumer;
import com.huanglei.wanandroid.utils.RxUtils;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by 黄垒 on 2018/12/26.
 */

public class SearchActivityPresenter extends RxMVPBasePresenter<SearchActivityContract.View> implements SearchActivityContract.Presenter {

    @Override
    public void getHotSearchKeywords() {
        addDisposable(HttpHelper.getInstance()
                .getSearchHotKeys()
                .compose(RxUtils.<BaseResponse<List<HotKey>>>schedulerTransformer())
                .compose(RxUtils.<List<HotKey>>responseTransformer())
                .subscribe(new Consumer<List<HotKey>>() {
                    @Override
                    public void accept(List<HotKey> hotKeys) throws Exception {
                        if (isViewAttached()) {
                            getView().showHotSearchKeywordsSucceed(hotKeys);
                        }
                    }
                }, new ErrorConsumer<Throwable>() {
                    @Override
                    protected void onError(int errorCode, String errorMessage) {
                        if (isViewAttached()) {
                            getView().showHotSearchKeywordsFailed(errorMessage);
                        }
                    }
                }));
    }

    @Override
    public void getHistorySearchKeywords() {
        MySingleThreadPool.newInstance().execute(new Runnable() {
            @Override
            public void run() {
                final List<HistoryKeyword> list = DbHelper.getInstance().getHistoryKeywords();
                if (isViewAttached()) {
                    ((Activity) getView().getViewContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getView().showHistorySearchKeywordsSucceed(list);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void addHistorySearchKeyword(final String keyword) {
        MySingleThreadPool.newInstance().execute(new Runnable() {
            @Override
            public void run() {
                final List<HistoryKeyword> list = DbHelper.getInstance().addHistoryKeyword(keyword);
                if (isViewAttached()) {
                    ((Activity) getView().getViewContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getView().showAddHistorySearchKeywordSucceed(list);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void deleteHistorySearchKeyword(final int position, final String keyword) {
        MySingleThreadPool.newInstance().execute(new Runnable() {
            @Override
            public void run() {
                DbHelper.getInstance().deleteHistoryKeyword(keyword);
                if (isViewAttached()) {
                    ((Activity) getView().getViewContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getView().showDeleteHistorySearchKeywordSucceed(position);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void clearHistorySearchKeywords() {
        MySingleThreadPool.newInstance().execute(new Runnable() {
            @Override
            public void run() {
                DbHelper.getInstance().clearHistoryKeywords();
                if (isViewAttached()) {
                    ((Activity) getView().getViewContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getView().showClearHistorySearchKeywordsSucceed();
                        }
                    });
                }
            }
        });

    }

    @Override
    protected void registerEvent() {

    }
}
