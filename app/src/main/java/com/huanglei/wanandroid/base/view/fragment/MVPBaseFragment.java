package com.huanglei.wanandroid.base.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huanglei.wanandroid.base.presenter.IBasePresenter;
import com.huanglei.wanandroid.base.view.IBaseView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by HuangLei on 2018/11/18.
 */

public abstract class MVPBaseFragment<T extends IBasePresenter> extends Fragment implements IBaseView {
    private T mPresenter;
    private boolean isViewInitiated;
    private boolean isDataLoaded;
    private Unbinder mUnbinder;
    private boolean isShown;
    protected T getPresenter() {
        return mPresenter;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container,false);
        mUnbinder= ButterKnife.bind(this,view);
        mPresenter = createPresenter();
        mPresenter.attachView(this);
        initView();
        return view;
    }

    protected abstract int getLayoutId();

    protected abstract T createPresenter();

    protected abstract void initView();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareRequestData();
    }


    @Override
    public void onResume() {
        super.onResume();
        isShown=true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isShown=false;
    }

    public boolean isShown(){
        return isShown;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        prepareRequestData();
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

    protected abstract void requestData();

    @Override
    public void onDestroyView() {
        isDataLoaded=false;
        isViewInitiated=false;
        if (mPresenter != null)
            mPresenter.detachView();
        mUnbinder.unbind();
        super.onDestroyView();
    }



}
