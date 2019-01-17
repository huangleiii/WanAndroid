package com.huanglei.wanandroid.vp.main.project;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.huanglei.wanandroid.R;
import com.huanglei.wanandroid.app.Constants;
import com.huanglei.wanandroid.base.view.fragment.StateMVPBaseFragment;
import com.huanglei.wanandroid.contract.ProjectListFragmentContract;
import com.huanglei.wanandroid.model.bean.Article;
import com.huanglei.wanandroid.vp.articledetail.ArticleDetailActivity;
import com.huanglei.wanandroid.vp.login.LoginActivity;
import com.huanglei.wanandroid.vp.main.home.HomeAdapter;
import com.huanglei.wanandroid.utils.CommonUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by 黄垒 on 2019/1/12.
 */

public class ProjectListFragment  extends StateMVPBaseFragment<ProjectListFragmentContract.Presenter> implements ProjectListFragmentContract.View {
    private static final String ID = "id";
    private int id;
    @BindView(R.id.recycler_fragment_project_list)
    RecyclerView recyclerFragmentProjectList;
    @BindView(R.id.normal)
    SmartRefreshLayout normal;
    private HomeAdapter mHomeAdapter;
    private boolean isFirstLoad;
    private int page;
    private int currentClickPosition;

    public static ProjectListFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt(ID, id);
        ProjectListFragment fragment = new ProjectListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Context getViewContext() {
        return getContext();
    }

    @Override
    public void showProjectArticlesSucceed(final List<Article> articles) {
        if (isFirstLoad) {
            showNormal();
            mHomeAdapter.setEnableLoadMore(true);
            normal.setEnableRefresh(true);
            mHomeAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    if (id > 0)
                    getPresenter().addProjectArticles(page, id);
                }
            }, recyclerFragmentProjectList);
            normal.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    if (id > 0)
                    getPresenter().getProjectArticles(id);
                }
            });
            mHomeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    currentClickPosition = position;
                    ArticleDetailActivity.startArticleDetailActivity(getContext(), Constants.MAIN_ACTIVITY,
                            mHomeAdapter.getItem(position).getId(), mHomeAdapter.getItem(position).getTitle(),
                            mHomeAdapter.getItem(position).getLink(), mHomeAdapter.getItem(position).isCollect());
                }
            });
            mHomeAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    if (mHomeAdapter.getItem(position).isCollect()) {
                        getPresenter().cancelCollect(position, articles);
                        mHomeAdapter.getItem(position).setCollect(false);
                        mHomeAdapter.notifyItemChanged(position);
                    } else {
                        getPresenter().collect(position, articles);
                        mHomeAdapter.getItem(position).setCollect(true);
                        mHomeAdapter.notifyItemChanged(position);
                    }
                }
            });
            isFirstLoad = false;
        } else {
            normal.finishRefresh(true);
        }
        page = 2;
        mHomeAdapter.setNewData(articles);
    }

    @Override
    public void showProjectArticlesFailed(String errorMsg) {
        if (isFirstLoad) {
            showError();
        } else {
            normal.finishRefresh(false);
            CommonUtils.showToastMessage(getActivity(), errorMsg);
        }
    }

    @Override
    public void showAddProjectArticlesSucceed(List<Article> articles) {
        page++;
        if (articles.isEmpty()) {
            mHomeAdapter.loadMoreEnd(true);
            CommonUtils.showToastMessage(getActivity(), "无更多数据");
        } else {
            mHomeAdapter.loadMoreComplete();
            mHomeAdapter.addData(articles);
        }
    }

    @Override
    public void showAddProjectArticlesFailed(String errorMsg) {
        mHomeAdapter.loadMoreFail();
        CommonUtils.showToastMessage(getActivity(), errorMsg);
    }

    @Override
    public void showCollectSucceed(int position, List<Article> articles) {
        CommonUtils.showToastMessage(getActivity(), "收藏成功");
    }

    @Override
    public void showCollectFailed(int position, List<Article> articles,boolean isLoginExpired, String errorMsg) {
        if(isLoginExpired)
            LoginActivity.startLoginActivity(getContext(),Constants.MAIN_ACTIVITY);
        mHomeAdapter.getItem(position).setCollect(false);
        mHomeAdapter.notifyItemChanged(position);
        CommonUtils.showToastMessage(getActivity(), errorMsg);
    }

    @Override
    public void showCancelCollectSucceed(int position, List<Article> articles) {
        CommonUtils.showToastMessage(getActivity(), "取消收藏成功");

    }

    @Override
    public void showCancelCollectFailed(int position, List<Article> articles,boolean isLoginExpired, String errorMsg) {
        if(isLoginExpired)
            LoginActivity.startLoginActivity(getContext(),Constants.MAIN_ACTIVITY);
        mHomeAdapter.getItem(position).setCollect(true);
        mHomeAdapter.notifyItemChanged(position);
        CommonUtils.showToastMessage(getActivity(), errorMsg);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_project_list;
    }

    @Override
    protected ProjectListFragmentContract.Presenter createPresenter() {
        return new ProjectListFragmentPresenter();
    }

    @Override
    protected void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            id = bundle.getInt(ID, -1);
        }
        isFirstLoad=true;
        mHomeAdapter = new HomeAdapter(getContext(), new ArrayList<Article>());
        mHomeAdapter.bindToRecyclerView(recyclerFragmentProjectList);
        recyclerFragmentProjectList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mHomeAdapter.setEnableLoadMore(false);
        normal.setEnableRefresh(false);
        recyclerFragmentProjectList.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
    }

    @Override
    protected void requestData() {
        if (id >= 0) {
            showLoading();
            getPresenter().getProjectArticles(id);
        }
    }

    public void refreshView() {
        if (isNormal() && id >= 0) {
            recyclerFragmentProjectList.scrollToPosition(0);
            normal.autoRefresh();
        }
    }

    public void updateCollectState(boolean isCollected) {
        mHomeAdapter.getItem(currentClickPosition).setCollect(isCollected);
        mHomeAdapter.notifyItemChanged(currentClickPosition);
    }

    public void jumpToTop() {
        if (isNormal()) {
            recyclerFragmentProjectList.smoothScrollToPosition(0);
        }
    }
}

