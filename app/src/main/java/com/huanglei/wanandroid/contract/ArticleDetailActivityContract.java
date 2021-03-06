package com.huanglei.wanandroid.contract;

import com.huanglei.wanandroid.base.presenter.IBasePresenter;
import com.huanglei.wanandroid.base.view.IBaseView;

/**
 * Created by 黄垒 on 2018/12/6.
 */

public interface ArticleDetailActivityContract {
    interface Presenter extends IBasePresenter<View>{
        void collect(String activityName,int id);
        void cancelCollect(String activityName,int id);
    }
    interface View extends IBaseView{
        void showCollectSucceed();
        void showCollectFailed(boolean isLoginExpired,String errorMsg);

        void showCancelCollectSucceed();
        void showCancelCollectFailed(boolean isLoginExpired,String errorMsg);
    }
}
