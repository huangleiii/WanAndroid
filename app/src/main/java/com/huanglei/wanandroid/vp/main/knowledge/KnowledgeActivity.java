package com.huanglei.wanandroid.vp.main.knowledge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.huanglei.wanandroid.R;
import com.huanglei.wanandroid.app.Constants;
import com.huanglei.wanandroid.base.presenter.IBasePresenter;
import com.huanglei.wanandroid.base.view.activity.MVPBaseActivity;
import com.huanglei.wanandroid.contract.KnowledgeActivityContract;
import com.huanglei.wanandroid.model.bean.KnowledgeTabList;
import com.huanglei.wanandroid.model.bean.Tab;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 黄垒 on 2019/1/14.
 */

public class KnowledgeActivity extends MVPBaseActivity<IBasePresenter> implements KnowledgeActivityContract.View {
    @BindView(R.id.tv_title_activity_knowledge)
    TextView tvTitleActivityKnowledge;
    @BindView(R.id.toolbar_activity_knowledge)
    Toolbar toolbarActivityKnowledge;
    @BindView(R.id.tab_layout_activity_knowledge)
    SlidingTabLayout tabLayoutActivityKnowledge;
    @BindView(R.id.viewpager_content_activity_knowledge)
    ViewPager viewpagerContentActivityKnowledge;
    @BindView(R.id.float_button_activity_knowledge)
    FloatingActionButton floatButtonActivityKnowledge;
    @BindView(R.id.linearlayout_normal)
    LinearLayout normal;
    private static final String TITLE = "title";
    private String title;
    private static final String TABS = "tabs";
    private List<Tab> tabList;

    public static void startKnowledgeActivity(Context context, KnowledgeTabList knowledgeTabList) {
        Intent intent = new Intent(context, KnowledgeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, knowledgeTabList.getName());
        bundle.putSerializable(TABS, (Serializable) knowledgeTabList.getChildren());
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_knowledge;
    }

    @Override
    protected IBasePresenter createPresenter() {
        return new KnowledgeActivityPresenter();
    }

    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            title = bundle.getString(TITLE);
            tabList = (List<Tab>) bundle.getSerializable(TABS);
        }
        setSupportActionBar(toolbarActivityKnowledge);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvTitleActivityKnowledge.setText(title);
        final List<KnowledgeArticleListFragment> fragments = new ArrayList<>();
        for (Tab tab : tabList) {
            KnowledgeArticleListFragment knowledgeArticleListFragment = KnowledgeArticleListFragment.newInstance(this, tab.getId());
            fragments.add(knowledgeArticleListFragment);
        }
        viewpagerContentActivityKnowledge.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragments.get(i);
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
        tabLayoutActivityKnowledge.setViewPager(viewpagerContentActivityKnowledge);
        tabLayoutActivityKnowledge.setCurrentTab(0);
        floatButtonActivityKnowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToTop();
            }
        });
    }

    @Override
    protected void requestData() {

    }

    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    private void jumpToTop() {
        int position = tabLayoutActivityKnowledge.getCurrentTab();
        KnowledgeArticleListFragment knowledgeArticleListFragment =
                (KnowledgeArticleListFragment) viewpagerContentActivityKnowledge.getAdapter()
                        .instantiateItem(viewpagerContentActivityKnowledge, position);
        knowledgeArticleListFragment.jumpToTop();
    }

    @Override
    public void subscribeLoginEvent(String activityName) {
        if (activityName.equals(Constants.KNOWLEDGE_ACTIVITY)) {
            int position = tabLayoutActivityKnowledge.getCurrentTab();
            KnowledgeArticleListFragment knowledgeArticleListFragment =
                    (KnowledgeArticleListFragment) viewpagerContentActivityKnowledge.getAdapter()
                            .instantiateItem(viewpagerContentActivityKnowledge, position);
            knowledgeArticleListFragment.refreshView();
        }
    }

    @Override
    public void subscribeCancelCollectEvent(String activityName) {
        if (activityName.equals(Constants.KNOWLEDGE_ACTIVITY)) {
            int position = tabLayoutActivityKnowledge.getCurrentTab();
            KnowledgeArticleListFragment knowledgeArticleListFragment =
                    (KnowledgeArticleListFragment) viewpagerContentActivityKnowledge.getAdapter()
                            .instantiateItem(viewpagerContentActivityKnowledge, position);
            knowledgeArticleListFragment.updateCollectState(false);
        }

    }

    @Override
    public void subscribeCollectEvent(String activityName) {
        if (activityName.equals(Constants.KNOWLEDGE_ACTIVITY)) {
            int position = tabLayoutActivityKnowledge.getCurrentTab();
            KnowledgeArticleListFragment knowledgeArticleListFragment =
                    (KnowledgeArticleListFragment) viewpagerContentActivityKnowledge.getAdapter()
                            .instantiateItem(viewpagerContentActivityKnowledge, position);
            knowledgeArticleListFragment.updateCollectState(true);
        }

    }
}
