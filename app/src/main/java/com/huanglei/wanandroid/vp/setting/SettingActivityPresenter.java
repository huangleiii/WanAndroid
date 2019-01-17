package com.huanglei.wanandroid.vp.setting;

import android.app.Activity;

import com.huanglei.wanandroid.app.Constants;
import com.huanglei.wanandroid.app.WanAndroidApplication;
import com.huanglei.wanandroid.base.presenter.RxMVPBasePresenter;
import com.huanglei.wanandroid.contract.SettingActivityContract;
import com.huanglei.wanandroid.model.db.MySingleThreadPool;
import com.huanglei.wanandroid.utils.CommonUtils;

import java.io.File;

/**
 * Created by 黄垒 on 2019/1/16.
 */

public class SettingActivityPresenter extends RxMVPBasePresenter<SettingActivityContract.View> implements SettingActivityContract.Presenter{

    @Override
    public void clearCache() {
        MySingleThreadPool.newInstance()
                .execute(new Runnable() {
                    @Override
                    public void run() {
                        File file=new File(Constants.PATH_CACHE);
                        CommonUtils.deleteDirectory(file);
                        final String size=CommonUtils.convertFileSize(CommonUtils.getDirectorySize(file));
                        if(isViewAttached()){
                            ((Activity)getView().getViewContext()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getView().showClearCacheSucceed(size);
                                }
                            });
                        }
                    }
                });
    }

    @Override
    public void getCacheSize() {
        MySingleThreadPool.newInstance()
                .execute(new Runnable() {
                    @Override
                    public void run() {
                        File file=new File(Constants.PATH_CACHE);
                        final String size=CommonUtils.convertFileSize(CommonUtils.getDirectorySize(file));
                        if(isViewAttached()){
                            ((Activity)getView().getViewContext()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getView().showCacheSizeSucceed(size);
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
