package com.huanglei.wanandroid.model.bean;

/**
 * Created by HuangLei on 2018/11/15.
 */

public class BaseResponse<T> {
    public static final int SUCCESS=0;
    public static final int LOGIN_FAILED=-1001;
    private int errorCode;
    private String errorMsg;
    private T data;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
