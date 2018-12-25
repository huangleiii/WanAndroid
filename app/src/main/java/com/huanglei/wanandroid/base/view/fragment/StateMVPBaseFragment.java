package com.huanglei.wanandroid.base.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huanglei.wanandroid.R;
import com.huanglei.wanandroid.base.presenter.IBasePresenter;

/**
 * Created by HuangLei on 2018/11/20.
 */

public abstract class StateMVPBaseFragment<T extends IBasePresenter> extends MVPBaseFragment<T> {
    private static final int STATE_NORMAL = 0;
    private static final int STATE_LOADING = 1;
    private static final int STATE_ERROR = 2;
    private int currentState = STATE_NORMAL;
    private View mNormalView;
    private View mLoadingView;
    private View mErrorView;
    private TextView mRetry;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= super.onCreateView(inflater, container, savedInstanceState);
        if (!(view instanceof ViewGroup)) {
            throw new IllegalStateException(
                    "The rootView should be a ViewGroup.");
        }
        mNormalView = view.findViewById(R.id.normal);
        if (mNormalView == null) {
            throw new IllegalStateException(
                    "The rootView must contain a View named 'mNormalView'.");
        }
        mLoadingView=getLayoutInflater().inflate(R.layout.loading, (ViewGroup) view,false);
        ((ViewGroup)view).addView(mLoadingView);
        mErrorView=getLayoutInflater().inflate(R.layout.error,  (ViewGroup)view,false);
        ((ViewGroup)view).addView(mErrorView);
        mRetry=mErrorView.findViewById(R.id.tv_retry_error);
        mRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareRequestData(true);
            }
        });
        mErrorView.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.GONE);
        mNormalView.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(currentState==STATE_ERROR){
            prepareRequestData(true);
        }
    }

    protected boolean isNormal(){
        if(currentState==STATE_NORMAL)
            return true;
        return false;
    }

    @Override
    public Context getViewContext() {
        return getContext();
    }

    protected void showNormal() {
        if (currentState == STATE_NORMAL)
            return;
        hideCurrentView();
        currentState=STATE_NORMAL;
        mNormalView.setVisibility(View.VISIBLE);

    }

    protected void showError() {
        if (currentState == STATE_ERROR)
            return;
        hideCurrentView();
        currentState=STATE_ERROR;
        mErrorView.setVisibility(View.VISIBLE);
    }

    protected void showLoading() {
        if (currentState == STATE_LOADING)
            return;
        hideCurrentView();
        currentState=STATE_LOADING;
        mLoadingView.setVisibility(View.VISIBLE);

    }

    private void hideCurrentView() {
        switch (currentState) {
            case STATE_NORMAL:
                mNormalView.setVisibility(View.GONE);
                break;
            case STATE_LOADING:
                mLoadingView.setVisibility(View.GONE);
                break;
            case STATE_ERROR:
                mErrorView.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }


}
