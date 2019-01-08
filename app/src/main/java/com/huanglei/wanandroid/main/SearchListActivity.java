package com.huanglei.wanandroid.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.huanglei.wanandroid.R;
import com.huanglei.wanandroid.app.Constants;
import com.huanglei.wanandroid.base.view.activity.MVPBaseActivity;
import com.huanglei.wanandroid.contract.SearchListActivityContract;
import com.huanglei.wanandroid.model.bean.Article;
import com.huanglei.wanandroid.utils.CommonUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchListActivity extends MVPBaseActivity<SearchListActivityContract.Presenter> implements SearchListActivityContract.View {
    @BindView(R.id.tv_title_activity_search_list)
    TextView tvTitleActivitySearchList;
    @BindView(R.id.toolbar_activity_search_list)
    Toolbar toolbarActivitySearchList;
    @BindView(R.id.recycler_activity_search_list)
    RecyclerView recyclerActivitySearchList;
    @BindView(R.id.smart_activity_search_list)
    SmartRefreshLayout smartActivitySearchList;
    private static final String KEY = "key";
    @BindView(R.id.float_button_activity_search_list)
    FloatingActionButton floatButtonActivitySearchList;
    private String key;
    private HomeAdapter mHomeAdapter;
    private boolean isFirstLoad = true;
    private int page;
    private int clickPosition;

    public static void startSearchListActivity(Context context, String key) {
        Intent intent = new Intent(context, SearchListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY, key);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    public void showSearchArticleListSucceed(List<Article> articleList) {
        mHomeAdapter.setNewData(articleList);
        page = 1;
        if (isFirstLoad) {
            isFirstLoad = false;
            mHomeAdapter.setEmptyView(R.layout.empty);
            ((TextView) (mHomeAdapter.getEmptyView().findViewById(R.id.tv_empty))).setText("搜索结果为空");
            smartActivitySearchList.setEnableRefresh(true);
        } else {
            smartActivitySearchList.finishRefresh(true);
        }
    }

    @Override
    public void showSearchArticleListFailed(String errorMsg) {
        if (isFirstLoad) {
            mHomeAdapter.setEmptyView(R.layout.error);
            mHomeAdapter.getEmptyView().findViewById(R.id.relativelayout_retry_error).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestData();
                }
            });
        } else {
            smartActivitySearchList.finishRefresh(false);
            CommonUtils.showToastMessage(this, errorMsg);
        }
    }

    @Override
    public void showAddSearchArticleListSucceed(List<Article> articles) {
        page++;
        if (articles.isEmpty()) {
            mHomeAdapter.loadMoreEnd(true);
            CommonUtils.showToastMessage(this, "无更多数据");
        } else {
            mHomeAdapter.addData(articles);
            mHomeAdapter.loadMoreComplete();
        }
    }

    @Override
    public void showAddSearchArticleListFailed(String errorMsg) {
        mHomeAdapter.loadMoreFail();
        CommonUtils.showToastMessage(this, errorMsg);
    }

    @Override
    public void showCollectSucceed(int position, List<Article> articles) {
        CommonUtils.showToastMessage(this, "收藏成功");
    }

    @Override
    public void showCollectFailed(int position, List<Article> articles, String errorMsg) {
        mHomeAdapter.getItem(position).setCollect(false);
        mHomeAdapter.notifyItemChanged(position);
        CommonUtils.showToastMessage(this, "收藏失败");
    }

    @Override
    public void showCancelCollectSucceed(int position, List<Article> articles) {
        CommonUtils.showToastMessage(this, "取消收藏成功");
    }

    @Override
    public void showCancelCollectFailed(int position, List<Article> articles, String errorMsg) {
        mHomeAdapter.getItem(position).setCollect(true);
        mHomeAdapter.notifyItemChanged(position);
        CommonUtils.showToastMessage(this, "取消收藏失败");
    }

    @Override
    public void subscribeLoginEvent() {
        refreshView();
    }

    @Override
    public void subscribeCancelCollectEvent(String activityName) {
        if (activityName.equals(Constants.SEARCH_LIST_ACTIVITY)) {
            mHomeAdapter.getItem(clickPosition).setCollect(false);
            mHomeAdapter.notifyItemChanged(clickPosition);
        }
    }

    @Override
    public void subscribeCollectEvent(String activityName) {
        if (activityName.equals(Constants.SEARCH_LIST_ACTIVITY)) {
            mHomeAdapter.getItem(clickPosition).setCollect(true);
            mHomeAdapter.notifyItemChanged(clickPosition);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_list;
    }

    @Override
    protected SearchListActivityContract.Presenter createPresenter() {
        return new SearchListActivityPresenter();
    }

    @Override
    protected void initToolbar() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                key = bundle.getString(KEY);
            }
        }
        setSupportActionBar(toolbarActivitySearchList);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvTitleActivitySearchList.setText(key);
    }

    @Override
    protected void initView() {
        mHomeAdapter = new HomeAdapter(this, new ArrayList<Article>());
        mHomeAdapter.bindToRecyclerView(recyclerActivitySearchList);
        recyclerActivitySearchList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mHomeAdapter.setEmptyView(R.layout.empty);
        recyclerActivitySearchList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        ((TextView) (mHomeAdapter.getEmptyView().findViewById(R.id.tv_empty))).setText("搜索结果为空");
        smartActivitySearchList.setEnableRefresh(false);
        smartActivitySearchList.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getPresenter().getSearchArticleList(key);
            }
        });
        mHomeAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getPresenter().addSearchArticleList(key, page);
            }
        }, recyclerActivitySearchList);
        mHomeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                clickPosition = position;
                ArticleDetailActivity.startArticleDetailActivity(SearchListActivity.this, Constants.SEARCH_LIST_ACTIVITY,
                        mHomeAdapter.getItem(position).getId(), mHomeAdapter.getItem(position).getTitle(),
                        mHomeAdapter.getItem(position).getLink(), mHomeAdapter.getItem(position).isCollect());
            }
        });
        mHomeAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.img_collect_item_fragment_home:
                    case R.id.img_collect_item_fragment_project:
                        if (mHomeAdapter.getItem(position).isCollect()) {
                            mHomeAdapter.getItem(position).setCollect(false);
                            mHomeAdapter.notifyItemChanged(position);
                            getPresenter().cancelCollect(position, mHomeAdapter.getData());
                        } else {
                            mHomeAdapter.getItem(position).setCollect(true);
                            mHomeAdapter.notifyItemChanged(position);
                            getPresenter().collect(position, mHomeAdapter.getData());
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        floatButtonActivitySearchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToTop();
            }
        });
    }

    protected void requestData() {
        mHomeAdapter.setEmptyView(R.layout.loading);
        getPresenter().getSearchArticleList(key);
    }

    private void refreshView() {
        if (mHomeAdapter.getEmptyView().findViewById(R.id.tv_empty) != null) {
            recyclerActivitySearchList.scrollToPosition(0);
            smartActivitySearchList.autoRefresh();
        }
    }

    private void jumpToTop() {
        recyclerActivitySearchList.smoothScrollToPosition(0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mHomeAdapter.getEmptyView().findViewById(R.id.tv_error) != null) {
            requestData();
        }
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

}
