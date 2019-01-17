package com.huanglei.wanandroid.vp.main.knowledge;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.huanglei.wanandroid.R;
import com.huanglei.wanandroid.base.view.fragment.StateMVPBaseFragment;
import com.huanglei.wanandroid.contract.KnowledgeTabListsFragmentContract;
import com.huanglei.wanandroid.model.bean.KnowledgeTabList;
import com.huanglei.wanandroid.utils.CommonUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 黄垒 on 2019/1/14.
 */

public class KnowledgeTabListsFragment extends StateMVPBaseFragment<KnowledgeTabListsFragmentContract.Presenter> implements KnowledgeTabListsFragmentContract.View {

    @BindView(R.id.recycler_view_fragment_knowledge_tab)
    RecyclerView recyclerViewFragmentKnowledgeTab;
    @BindView(R.id.normal)
    SmartRefreshLayout normal;
    @BindView(R.id.lin_root_fragment_knowledge_tab)
    LinearLayout linRootFragmentKnowledgeTab;
    private boolean isFirstLoad;
    private KnowledgeTabListAdapter knowledgeTabListAdapter;

    public static KnowledgeTabListsFragment newInstance() {
        KnowledgeTabListsFragment knowledgeTabListsFragment = new KnowledgeTabListsFragment();
        return knowledgeTabListsFragment;
    }

    @Override
    public Context getViewContext() {
        return getContext();
    }

    @Override
    public void showKnowledgeTabListsSucceed(List<KnowledgeTabList> lists) {
        if (isFirstLoad) {
            isFirstLoad = false;
            showNormal();
        } else {
            normal.finishRefresh(true);
        }
        knowledgeTabListAdapter.setNewData(lists);
    }

    @Override
    public void showKnowledgeTabListsFailed(String errorMsg) {
        if (isFirstLoad) {
            showError();
        } else {
            normal.finishRefresh(false);
            CommonUtils.showToastMessage(getActivity(), errorMsg);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_knowledge_tab;
    }


    @Override
    protected KnowledgeTabListsFragmentContract.Presenter createPresenter() {
        return new KnowledgeTabListsFragmentPresenter();
    }

    @Override
    protected void initView() {
        isFirstLoad = true;
        normal.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getPresenter().getKnowledgeTabLists();
            }
        });
        knowledgeTabListAdapter = new KnowledgeTabListAdapter(getContext(), new ArrayList<KnowledgeTabList>());
        knowledgeTabListAdapter.bindToRecyclerView(recyclerViewFragmentKnowledgeTab);
        recyclerViewFragmentKnowledgeTab.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        knowledgeTabListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                KnowledgeActivity.startKnowledgeActivity(getContext(), knowledgeTabListAdapter.getItem(position));
            }
        });
        recyclerViewFragmentKnowledgeTab.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    protected void requestData() {
        showLoading();
        getPresenter().getKnowledgeTabLists();
    }

    public void jumpToTop() {
        if (isNormal()) {
            recyclerViewFragmentKnowledgeTab.smoothScrollToPosition(0);
        }
    }

}
