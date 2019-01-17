package com.huanglei.wanandroid.contract;

import com.huanglei.wanandroid.base.presenter.IBasePresenter;
import com.huanglei.wanandroid.base.view.IBaseView;

/**
 * Created by 黄垒 on 2019/1/14.
 */

public interface KnowledgeActivityContract {
    interface View extends IBaseView{
        void subscribeLoginEvent(String activityName);
        void subscribeCancelCollectEvent(String activityName);
        void subscribeCollectEvent(String activityName);
    }
}
