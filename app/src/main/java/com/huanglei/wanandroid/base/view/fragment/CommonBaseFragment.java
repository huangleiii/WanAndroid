package com.huanglei.wanandroid.base.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huanglei.wanandroid.base.presenter.IBasePresenter;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by HuangLei on 2018/11/18.
 */

public abstract class CommonBaseFragment<T extends IBasePresenter> extends BaseFragment<T> {
    private boolean isViewInitiated;
    private boolean isDataLoaded;
    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container,false);
        mUnbinder= ButterKnife.bind(this,view);
        initView();
        return view;
    }

    abstract protected void initView();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareRequestData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        prepareRequestData();
    }

    @Override
    public void onDestroyView() {
        mUnbinder.unbind();
        super.onDestroyView();
    }

    public boolean prepareRequestData() {
        return prepareRequestData(false);
    }

    public boolean prepareRequestData(boolean forceUpdate) {
        if (isViewInitiated && getUserVisibleHint() && ((!isDataLoaded) || forceUpdate)) {
            requestData();
            isDataLoaded = true;
            return true;
        }
        return false;
    }

    protected abstract int getLayoutId();

    protected abstract void requestData();
}
