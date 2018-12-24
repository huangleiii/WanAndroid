package com.huanglei.wanandroid.model.http;

/**
 * Created by HuangLei on 2018/11/28.
 */

public class ServerException extends Exception {
    private int errorCode;
    private String errorMessage;

    public ServerException(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
