package com.huanglei.wanandroid.model.db;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 黄垒 on 2019/1/13.
 */

public class MySingleThreadPool {
    private static volatile MySingleThreadPool mySingleThreadPool;
    private ExecutorService singleThreadPool;
    private MySingleThreadPool(){
        singleThreadPool= Executors.newSingleThreadExecutor();
    }
    public static MySingleThreadPool newInstance(){
        if(mySingleThreadPool ==null){
            synchronized (MySingleThreadPool.class){
                if(mySingleThreadPool ==null){
                    mySingleThreadPool =new MySingleThreadPool();
                }
            }
        }
        return mySingleThreadPool;
    }
    public void execute(Runnable r){
        singleThreadPool.execute(r);
    }
}
