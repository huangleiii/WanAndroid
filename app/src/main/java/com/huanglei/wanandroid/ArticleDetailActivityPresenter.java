package com.huanglei.wanandroid;

import com.huanglei.wanandroid.base.presenter.RxBasePresenter;
import com.huanglei.wanandroid.contract.ArticleDetailActivityContract;

/**
 * Created by 黄垒 on 2018/12/6.
 */

public class ArticleDetailActivityPresenter extends RxBasePresenter<ArticleDetailActivityContract.View> implements ArticleDetailActivityContract.Presenter{

    @Override
    public void collect(int id) {

    }

    @Override
    public void cancelCollect(int id) {

    }

    @Override
    protected void registerEvent() {

    }
}
