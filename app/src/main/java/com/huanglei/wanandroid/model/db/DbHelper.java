package com.huanglei.wanandroid.model.db;

import com.huanglei.wanandroid.app.WanAndroidApplication;

import java.util.Iterator;
import java.util.List;

/**
 * Created by 黄垒 on 2019/1/3.
 */

public class DbHelper {
    private static volatile DbHelper mDbHelper;
    private static final int HISTORY_LIST_SIZE=10;
    private DbHelper(){}
    public static DbHelper getInstance(){
        if(mDbHelper==null){
            synchronized(DbHelper.class){
                if(mDbHelper==null){
                    mDbHelper=new DbHelper();
                }
            }
        }
        return mDbHelper;
    }
    public List<HistoryKeyword> getHistoryKeywords(){
        return WanAndroidApplication.getDaoSession().queryBuilder(HistoryKeyword.class)
                .orderDesc(HistoryKeywordDao.Properties.Date)
                .list();
    }
    public void clearHistoryKeywords(){
        WanAndroidApplication.getDaoSession().deleteAll(HistoryKeyword.class);
    }
    public long addHistoryKeyword(String data){
        HistoryKeyword historyKeyword=new HistoryKeyword();
        historyKeyword.setData(data);
        historyKeyword.setDate(System.currentTimeMillis());
        deleteHistoryKeyword(data);
        long count=WanAndroidApplication.getDaoSession().queryBuilder(HistoryKeyword.class).count();
        if(!(count<HISTORY_LIST_SIZE)){
            HistoryKeyword historyKeyword1=WanAndroidApplication.getDaoSession().queryBuilder(HistoryKeyword.class)
                    .orderAsc(HistoryKeywordDao.Properties.Date)
                    .limit(1)
                    .list()
                    .get(0);
            WanAndroidApplication.getDaoSession().delete(historyKeyword1);
        }
        return WanAndroidApplication.getDaoSession().insert(historyKeyword);
    }
    public void deleteHistoryKeyword(String data){
        Iterator<HistoryKeyword> iterator=getHistoryKeywords().iterator();
        while(iterator.hasNext()){
            HistoryKeyword historyKeywory=iterator.next();
            if(historyKeywory.getData().equals(data)){
                iterator.remove();
            }
        }
    }

}
