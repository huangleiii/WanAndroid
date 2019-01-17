package com.huanglei.wanandroid.contract;

import com.huanglei.wanandroid.base.presenter.IBasePresenter;
import com.huanglei.wanandroid.base.view.IBaseView;

/**
 * Created by 黄垒 on 2019/1/16.
 */

public interface SettingActivityContract {
    interface Presenter extends IBasePresenter<View>{
        void clearCache();
        void getCacheSize();
    }
    interface View extends IBaseView{
        void showClearCacheSucceed(String cacheSize);
        void showClearCacheFailed(String cacheSize);

        void showCacheSizeSucceed(String cacheSize);
        void showCacheSizeFailed(String cacheSize);
    }
}
