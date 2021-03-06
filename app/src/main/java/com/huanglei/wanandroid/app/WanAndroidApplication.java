package com.huanglei.wanandroid.app;

import android.app.Application;
import android.content.Context;

import com.huanglei.wanandroid.R;
import com.huanglei.wanandroid.model.db.DaoMaster;
import com.huanglei.wanandroid.model.db.DaoSession;
import com.huanglei.wanandroid.model.db.MyDaoMasterOpenHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import org.greenrobot.greendao.database.Database;

/**
 * Created by HuangLei on 2018/11/17.
 */

public class WanAndroidApplication extends Application{
    private static Context mContext;
    private static DaoSession mDaoSession;
    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.white, R.color.grey);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
//        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
//            @Override
//            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
//                //指定为经典Footer，默认是 BallPulseFooter
//                return new ClassicsFooter(context).setDrawableSize(20);
//            }
//        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext=getApplicationContext();
        setupDataBase();
    }

    public static Context getInstance(){
        return mContext;
    }

    private void setupDataBase(){
        MyDaoMasterOpenHelper openHelper=new MyDaoMasterOpenHelper(this,Constants.HISTORY_SEARCH_KEYWORDS_DATABASE_NAME);
        Database db=openHelper.getWritableDb();
        DaoMaster daoMaster=new DaoMaster(db);
        mDaoSession=daoMaster.newSession();
    }

    public static DaoSession getDaoSession(){
        return mDaoSession;
    }
}
