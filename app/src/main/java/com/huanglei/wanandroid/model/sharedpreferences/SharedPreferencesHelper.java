package com.huanglei.wanandroid.model.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.huanglei.wanandroid.app.Constants;
import com.huanglei.wanandroid.app.WanAndroidApplication;

/**
 * Created by HuangLei on 2018/11/27.
 */

public class SharedPreferencesHelper {
    private static volatile SharedPreferencesHelper mHelper;
    private SharedPreferences mSharedPreference;
    private SharedPreferencesHelper(){
        mSharedPreference= WanAndroidApplication.getInstance().getSharedPreferences(Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE);
    }
    public static SharedPreferencesHelper getInstance(){
        if(mHelper==null){
            synchronized (SharedPreferencesHelper.class){
                if(mHelper==null){
                    mHelper=new SharedPreferencesHelper();
                }
            }
        }
        return mHelper;
    }

    public void setUsername(String username) {
        mSharedPreference.edit().putString(Constants.USERNAME,username).apply();
    }

    public void setLoginStatus(boolean isLogin) {
        mSharedPreference.edit().putBoolean(Constants.LOGIN_STATUS,isLogin).apply();
    }

    public String getUsername() {
        return mSharedPreference.getString(Constants.USERNAME,"");
    }

    public boolean getLoginStatus() {
        return mSharedPreference.getBoolean(Constants.LOGIN_STATUS,false);
    }
    public void setCurrentActivity(String activityName){
        mSharedPreference.edit().putString(Constants.ACTIVITY_NAME,activityName).apply();
    }
    public String getCurrentActivity(){
        return mSharedPreference.getString(Constants.ACTIVITY_NAME,"");
    }

}
