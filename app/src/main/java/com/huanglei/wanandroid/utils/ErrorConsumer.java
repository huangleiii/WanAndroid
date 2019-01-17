package com.huanglei.wanandroid.utils;

import com.huanglei.wanandroid.model.http.ServerException;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.functions.Consumer;

/**
 * Created by HuangLei on 2018/11/28.
 */

public abstract class ErrorConsumer<T extends Throwable> implements Consumer<T> {
    @Override
    public void accept(T t) throws Exception {
        String errorMessage = "";
        int errorCode = 0;
        if (t instanceof ServerException) {
            errorCode = ((ServerException) t).getErrorCode();
            errorMessage = ((ServerException) t).getErrorMessage();
        } else if (t instanceof UnknownHostException)
            errorMessage = "请求失败，请稍候重试……";
        else if (t instanceof SocketException)
            errorMessage = "网络异常，请检查网络重试";
        else if (t instanceof SocketTimeoutException)
            errorMessage = "请求超时";
        else
            errorMessage=t.toString();
//            errorMessage = "未知错误";
        onError(errorCode, errorMessage);
    }

    protected abstract void onError(int errorCode, String errorMessage);
}
