package com.huanglei.wanandroid.vp.main.knowledge;

import com.huanglei.wanandroid.base.presenter.RxMVPBasePresenter;
import com.huanglei.wanandroid.contract.KnowledgeTabListsFragmentContract;
import com.huanglei.wanandroid.model.bean.BaseResponse;
import com.huanglei.wanandroid.model.bean.KnowledgeTabList;
import com.huanglei.wanandroid.model.http.HttpHelper;
import com.huanglei.wanandroid.utils.ErrorConsumer;
import com.huanglei.wanandroid.utils.RxUtils;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by 黄垒 on 2019/1/14.
 */

public class KnowledgeTabListsFragmentPresenter extends RxMVPBasePresenter<KnowledgeTabListsFragmentContract.View> implements KnowledgeTabListsFragmentContract.Presenter {

    @Override
    public void getKnowledgeTabLists() {
        addDisposable(HttpHelper.getInstance()
                .getKnowledgeTabLists()
                .compose(RxUtils.<BaseResponse<List<KnowledgeTabList>>>schedulerTransformer())
                .compose(RxUtils.<List<KnowledgeTabList>>responseTransformer())
                .subscribe(new Consumer<List<KnowledgeTabList>>() {
                    @Override
                    public void accept(List<KnowledgeTabList> knowledgeTabLists) throws Exception {
                        if(isViewAttached()){
                            getView().showKnowledgeTabListsSucceed(knowledgeTabLists);
                        }
                    }
                }, new ErrorConsumer<Throwable>() {
                    @Override
                    protected void onError(int errorCode, String errorMessage) {
                        if(isViewAttached()){
                            getView().showKnowledgeTabListsFailed(errorMessage);
                        }
                    }
                }));
    }

    @Override
    protected void registerEvent() {

    }
}
