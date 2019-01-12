package com.huanglei.wanandroid.vp.main.project;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;

import com.flyco.tablayout.SlidingTabLayout;
import com.huanglei.wanandroid.R;
import com.huanglei.wanandroid.base.view.fragment.StateMVPBaseFragment;
import com.huanglei.wanandroid.contract.ProjectFragmentContract;
import com.huanglei.wanandroid.model.bean.Tab;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by 黄垒 on 2019/1/12.
 */

public class ProjectFragment  extends StateMVPBaseFragment<ProjectFragmentContract.Presenter> implements ProjectFragmentContract.View {

    @BindView(R.id.tab_layout_fragment_project)
    SlidingTabLayout tabLayoutFragmentProject;
    @BindView(R.id.normal)
    LinearLayout normal;
    @BindView(R.id.viewpager_content_fragment_project)
    ViewPager viewpagerContentFragmentProject;

    public static ProjectFragment newInstance(){
        ProjectFragment projectFragment=new ProjectFragment();
        return projectFragment;
    }
    @Override
    public Context getViewContext() {
        return getContext();
    }

    @Override
    public void showProjectTabsSucceed(final List<Tab> tabList) {
        showNormal();
        if(!tabList.isEmpty()){
            final List<ProjectListFragment> projectListFragments=new ArrayList<>();
            for(Tab tab:tabList){
                ProjectListFragment projectListFragment=ProjectListFragment.newInstance(tab.getId());
                projectListFragments.add(projectListFragment);
            }
            viewpagerContentFragmentProject.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
                @Override
                public Fragment getItem(int i) {
                    return projectListFragments.get(i);
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
            tabLayoutFragmentProject.setViewPager(viewpagerContentFragmentProject);
            viewpagerContentFragmentProject.setCurrentItem(0);
        }

    }

    @Override
    public void showProjectTabsFailed(String errorMsg) {
        showError();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_project;
    }

    @Override
    protected ProjectFragmentContract.Presenter createPresenter() {
        return new ProjectFragmentPresenter();
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void requestData() {
        showLoading();
        getPresenter().getProjectTabs();
    }

    public void refreshView(){
        if(isNormal()){
            int position=viewpagerContentFragmentProject.getCurrentItem();
            ProjectListFragment projectListFragment= (ProjectListFragment) viewpagerContentFragmentProject.getAdapter().instantiateItem(viewpagerContentFragmentProject,position);
            projectListFragment.refreshView();
        }
    }
    public void updateCollectState(boolean isCollected){
        int position=viewpagerContentFragmentProject.getCurrentItem();
        ProjectListFragment projectListFragment= (ProjectListFragment) viewpagerContentFragmentProject.getAdapter().instantiateItem(viewpagerContentFragmentProject,position);
        projectListFragment.updateCollectState(isCollected);
    }

    public void jumpToTop(){
        if(isNormal()){
            int position=viewpagerContentFragmentProject.getCurrentItem();
            ProjectListFragment projectListFragment= (ProjectListFragment) viewpagerContentFragmentProject.getAdapter().instantiateItem(viewpagerContentFragmentProject,position);
            projectListFragment.jumpToTop();
        }
    }
}
