package com.huanglei.wanandroid.vp.main.weixin;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;

import com.flyco.tablayout.SlidingTabLayout;
import com.huanglei.wanandroid.R;
import com.huanglei.wanandroid.base.view.fragment.StateMVPBaseFragment;
import com.huanglei.wanandroid.contract.WeixinFragmentContract;
import com.huanglei.wanandroid.model.bean.Tab;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class WeixinFragment extends StateMVPBaseFragment<WeixinFragmentContract.Presenter> implements WeixinFragmentContract.View {

    @BindView(R.id.tab_layout_fragment_weixin)
    SlidingTabLayout tabLayoutFragmentWeixin;
    @BindView(R.id.normal)
    LinearLayout normal;
    @BindView(R.id.viewpager_content_fragment_weixin)
    ViewPager viewpagerContentFragmentWeixin;

    public static WeixinFragment newInstance(){
        WeixinFragment weixinFragment=new WeixinFragment();
        return weixinFragment;
    }
    @Override
    public Context getViewContext() {
        return getContext();
    }

    @Override
    public void showWxTabsSucceed(final List<Tab> tabList) {
        showNormal();
        if(!tabList.isEmpty()){
            final List<WeixinListFragment> weixinListFragments=new ArrayList<>();
            for(Tab tab:tabList){
                WeixinListFragment weixinListFragment=WeixinListFragment.newInstance(tab.getId());
                weixinListFragments.add(weixinListFragment);
            }
            viewpagerContentFragmentWeixin.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
                @Override
                public Fragment getItem(int i) {
                    return weixinListFragments.get(i);
                }

                @Override
                public int getCount() {
                    return tabList.size();
                }

                @Nullable
                @Override
                public CharSequence getPageTitle(int position) {
                    return tabList.get(position).getName();
                }
            });
            tabLayoutFragmentWeixin.setViewPager(viewpagerContentFragmentWeixin);
            viewpagerContentFragmentWeixin.setCurrentItem(0);
        }

    }

    @Override
    public void showWxTabsFailed(String errorMsg) {
        showError();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_weixin;
    }

    @Override
    protected WeixinFragmentContract.Presenter createPresenter() {
        return new WeixinFragmentPresenter();
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void requestData() {
        showLoading();
        getPresenter().getWxTabs();
    }

    public void refreshView(){
        if(isNormal()){
            int position=viewpagerContentFragmentWeixin.getCurrentItem();
            WeixinListFragment weixinListFragment= (WeixinListFragment) viewpagerContentFragmentWeixin.getAdapter().instantiateItem(viewpagerContentFragmentWeixin,position);
            weixinListFragment.refreshView();
        }
    }
    public void updateCollectState(boolean isCollected){
        int position=viewpagerContentFragmentWeixin.getCurrentItem();
        WeixinListFragment weixinListFragment= (WeixinListFragment) viewpagerContentFragmentWeixin.getAdapter().instantiateItem(viewpagerContentFragmentWeixin,position);
        weixinListFragment.updateCollectState(isCollected);
    }

    public void jumpToTop(){
        if(isNormal()){
            int position=viewpagerContentFragmentWeixin.getCurrentItem();
            WeixinListFragment weixinListFragment= (WeixinListFragment) viewpagerContentFragmentWeixin.getAdapter().instantiateItem(viewpagerContentFragmentWeixin,position);
            weixinListFragment.jumpToTop();
        }
    }
}
