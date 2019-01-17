package com.huanglei.wanandroid.vp.main.knowledge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.huanglei.wanandroid.R;
import com.huanglei.wanandroid.app.Constants;
import com.huanglei.wanandroid.base.view.fragment.StateMVPBaseFragment;
import com.huanglei.wanandroid.contract.KnowledgeArticleListFragmentContract;
import com.huanglei.wanandroid.model.bean.Article;
import com.huanglei.wanandroid.utils.CommonUtils;
import com.huanglei.wanandroid.vp.articledetail.ArticleDetailActivity;
import com.huanglei.wanandroid.vp.login.LoginActivity;
import com.huanglei.wanandroid.vp.main.home.HomeAdapter;
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

public class KnowledgeArticleListFragment extends StateMVPBaseFragment<KnowledgeArticleListFragmentContract.Presenter> implements KnowledgeArticleListFragmentContract.View {
    @BindView(R.id.recycler_fragment_knowledge_article_list)
    RecyclerView recyclerFragmentKnowledgeArticleList;
    @BindView(R.id.normal)
    SmartRefreshLayout normal;
    private static final String ID = "id";
    private int id;
    private boolean isFirstLoad;
    private HomeAdapter homeAdapter;
    private int page;
    private int currentClickPosition;

    public static KnowledgeArticleListFragment newInstance(Context context, int id) {
        KnowledgeArticleListFragment knowledgeArticleListFragment = new KnowledgeArticleListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ID, id);
        knowledgeArticleListFragment.setArguments(bundle);
        return knowledgeArticleListFragment;
    }

    @Override
    public Context getViewContext() {
        return getContext();
    }

    @Override
    public void showKnowledgeArticleListSucceed(List<Article> articles) {
        page=1;
        homeAdapter.setNewData(articles);
        if(isFirstLoad){
            isFirstLoad=false;
            showNormal();
        }else {
            normal.finishRefresh(true);
        }

    }

    @Override
    public void showKnowledgeArticleListFailed(String errorMsg) {
        if(isFirstLoad){
            showError();
        }else {
            normal.finishRefresh(false);
            CommonUtils.showToastMessage(getActivity(),errorMsg);
        }
    }

    @Override
    public void showAddKnowledgeArticleListSucceed(List<Article> articles) {
        page++;
        if(!articles.isEmpty()){
            homeAdapter.addData(articles);
            homeAdapter.loadMoreComplete();
        }else {
            homeAdapter.loadMoreEnd(true);
            CommonUtils.showToastMessage(getActivity(),"无更多数据");
        }
    }

    @Override
    public void showAddKnowledgeArticleListFailed(String errorMsg) {
        homeAdapter.loadMoreFail();
        CommonUtils.showToastMessage(getActivity(),errorMsg);
    }

    @Override
    public void showCollectSucceed(int position, List<Article> articles) {
        CommonUtils.showToastMessage(getActivity(),"收藏成功");
    }

    @Override
    public void showCollectFailed(int position, List<Article> articles, boolean isLoginExpired, String errorMsg) {
        if(isLoginExpired)
            LoginActivity.startLoginActivity(getContext(),Constants.KNOWLEDGE_ACTIVITY);
        CommonUtils.showToastMessage(getActivity(),errorMsg);
        homeAdapter.getItem(position).setCollect(false);
        homeAdapter.notifyItemChanged(position);
    }

    @Override
    public void showCancelCollectSucceed(int position, List<Article> articles) {
        CommonUtils.showToastMessage(getActivity(),"取消收藏成功");
    }

    @Override
    public void showCancelCollectFailed(int position, List<Article> articles, boolean isLoginExpired, String errorMsg) {
        if(isLoginExpired)
            LoginActivity.startLoginActivity(getContext(),Constants.KNOWLEDGE_ACTIVITY);
        CommonUtils.showToastMessage(getActivity(),errorMsg);
        homeAdapter.getItem(position).setCollect(true);
        homeAdapter.notifyItemChanged(position);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_knowledge_article_list;
    }

    @Override
    protected KnowledgeArticleListFragmentContract.Presenter createPresenter() {
        return new KnowledgeArticleListFragmentPresenter();
    }

    @Override
    protected void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            id = bundle.getInt(ID, -1);
        }
        isFirstLoad = true;
        homeAdapter = new HomeAdapter(getContext(), new ArrayList<Article>());
        homeAdapter.bindToRecyclerView(recyclerFragmentKnowledgeArticleList);
        recyclerFragmentKnowledgeArticleList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        homeAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (id > 0)
                    getPresenter().addKnowledgeArticleList(page, id);
            }
        }, recyclerFragmentKnowledgeArticleList);
        normal.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (id > 0)
                    getPresenter().getKnowledgeArticleList(id);
            }
        });
        homeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                currentClickPosition=position;
                ArticleDetailActivity.startArticleDetailActivity(getContext(), Constants.KNOWLEDGE_ACTIVITY,
                        homeAdapter.getItem(position).getId(),homeAdapter.getItem(position).getTitle(),
                        homeAdapter.getItem(position).getLink(),homeAdapter.getItem(position).isCollect());
            }
        });
        homeAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.img_collect_item_fragment_home:
                    case R.id.img_collect_item_fragment_project:
                        if(homeAdapter.getItem(position).isCollect()){
                            getPresenter().cancelCollect(position,homeAdapter.getData());
                            homeAdapter.getItem(position).setCollect(false);
                            homeAdapter.notifyItemChanged(position);
                        }else{
                            getPresenter().collect(position,homeAdapter.getData());
                            homeAdapter.getItem(position).setCollect(true);
                            homeAdapter.notifyItemChanged(position);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        recyclerFragmentKnowledgeArticleList.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
    }

    @Override
    protected void requestData() {
        if(id>0){
            showLoading();
            getPresenter().getKnowledgeArticleList(id);
        }
    }

    public void refreshView() {
        if(isNormal()){
            recyclerFragmentKnowledgeArticleList.scrollToPosition(0);
            normal.autoRefresh();
        }
    }

    public void updateCollectState(boolean isCollected) {
        homeAdapter.getItem(currentClickPosition).setCollect(isCollected);
        homeAdapter.notifyItemChanged(currentClickPosition);
    }

    public void jumpToTop() {
        if(isNormal()){
            recyclerFragmentKnowledgeArticleList.smoothScrollToPosition(0);
        }
    }

}
