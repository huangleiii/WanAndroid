package com.huanglei.wanandroid.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.huanglei.wanandroid.app.WanAndroidApplication;

import java.io.File;

/**
 * Created by HuangLei on 2018/11/13.
 */

public class CommonUtils {
    public static void showToastMessage(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    public static void showSnackbarMessage(Activity activity, String message) {
        final Snackbar snackbar = Snackbar.make(activity.getWindow().getDecorView(), message, Snackbar.LENGTH_SHORT);
        snackbar.setAction("知道了", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        }).show();
    }

    public static boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) WanAndroidApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null;
    }

    public static int px2dp(float px) {
        final float density = WanAndroidApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5f);
    }

    public static int dp2px(float dp) {
        final float density = WanAndroidApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    public static void deleteDirectory(File file) {
        if (file != null)
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    File f = files[i];
                    deleteDirectory(f);
                }
                file.delete();
            } else if (file.exists()) {
                file.delete();
            }
    }

    public static long getDirectorySize(File file) {
        if (file == null)
            return 0;
        long size = 0;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                size += getDirectorySize(f);
            }
            return size;
        } else if (file.exists()) {
            return file.length();
        }
        return 0;
    }

    public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.2f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format("%.2f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format("%.2f KB", f);
        } else {
            return String.format("%d B", size);
        }
    }
}
