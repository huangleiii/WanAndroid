package com.huanglei.wanandroid.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.huanglei.wanandroid.app.WanAndroidApplication;

/**
 * Created by HuangLei on 2018/11/13.
 */

public class CommonUtils {
    public static void showToastMessage(Activity activity, String message){
        Toast.makeText(activity,message,Toast.LENGTH_SHORT).show();
    }
    public static void showSnackbarMessage(Activity activity,String message){
        final Snackbar snackbar=Snackbar.make(activity.getWindow().getDecorView(),message,Snackbar.LENGTH_SHORT);
        snackbar.setAction("知道了", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        }).show();
    }
    public static boolean isNetworkConnected(){
        ConnectivityManager connectivityManager=(ConnectivityManager) WanAndroidApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null){
            return networkInfo.isAvailable();
        }
        return false;
    }
    public static int px2dp(float px){
        final float density=WanAndroidApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int)(px/density+0.5f);
    }
    public static int dp2px(float dp){
        final float density=WanAndroidApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int)(dp*density+0.5f);
    }
}
