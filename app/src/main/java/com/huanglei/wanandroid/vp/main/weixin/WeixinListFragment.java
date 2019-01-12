package com.huanglei.wanandroid.vp.main.weixin;

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
import com.huanglei.wanandroid.contract.WeixinListFragmentContract;
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

public class WeixinListFragment extends StateMVPBaseFragment<WeixinListFragmentContract.Presenter> implements WeixinListFragmentContract.View {
    private static final String ID = "id";
    private int id;
    @BindView(R.id.recycler_fragment_weixin_list)
    RecyclerView recyclerFragmentWeixinList;
    @BindView(R.id.normal)
    SmartRefreshLayout normal;
    private HomeAdapter mHomeAdapter;
    private boolean isFirstLoad;
    private int page;
    private int currentClickPosition;

    public static WeixinListFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt(ID, id);
        WeixinListFragment fragment = new WeixinListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Context getViewContext() {
        return getContext();
    }

    @Override
    public void showWxArticlesSucceed(final List<Article> articles) {
        if (isFirstLoad) {
            showNormal();
            mHomeAdapter.setEnableLoadMore(true);
            normal.setEnableRefresh(true);
            mHomeAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    getPresenter().addWxArticles(page, id);
                }
            }, recyclerFragmentWeixinList);
            normal.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    getPresenter().getWxArticles(id);
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
    public void showWxArticlesFailed(String errorMsg) {
        if (isFirstLoad) {
            showError();
        } else {
            normal.finishRefresh(false);
            CommonUtils.showToastMessage(getActivity(), errorMsg);
        }
    }

    @Override
    public void showAddWxArticlesSucceed(List<Article> articles) {
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
    public void showAddWxArticlesFailed(String errorMsg) {
        mHomeAdapter.loadMoreFail();
        CommonUtils.showToastMessage(getActivity(), errorMsg);
    }

    @Override
    public void showCollectSucceed(int position, List<Article> articles) {
        CommonUtils.showToastMessage(getActivity(), "收藏成功");
    }

    @Override
    public void showCollectFailed(int position, List<Article> articles, boolean isLoginExpired,String errorMsg) {
        if(isLoginExpired)
            LoginActivity.startLoginActivity(getContext(),Constants.MAIN_ACTIVITY);
        mHomeAdapter.getItem(position).setCollect(false);
        mHomeAdapter.notifyItemChanged(position);
        CommonUtils.showToastMessage(getActivity(), "收藏失败");
    }

    @Override
    public void showCancelCollectSucceed(int position, List<Article> articles) {
        CommonUtils.showToastMessage(getActivity(), "取消收藏成功");

    }

    @Override
    public void showCancelCollectFailed(int position, List<Article> articles, boolean isLoginExpired,String errorMsg) {
        if(isLoginExpired)
            LoginActivity.startLoginActivity(getContext(),Constants.MAIN_ACTIVITY);
        mHomeAdapter.getItem(position).setCollect(true);
        mHomeAdapter.notifyItemChanged(position);
        CommonUtils.showToastMessage(getActivity(), "取消收藏失败");

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_weixin_list;
    }

    @Override
    protected WeixinListFragmentContract.Presenter createPresenter() {
        return new WeixinListFragmentPresenter();
    }

    @Override
    protected void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            id = bundle.getInt(ID, -1);
        }
        isFirstLoad=true;//在这里初始化而不要直接在声明时就初始化。因为该fragment在FragmentStatePagerAdapter
                         //中destroy和detach后，其实例仍然被保存下来，不会new一个新的，所以虽然fragment的生命
                         //周期重新进行了一遍，但其成员变量的值维持了下来。造成当一个fragment创建、销毁、再次
                         //创建并刷新数据后，理应被当做第一次刷新，却由于isFirstLoad的false状态保存了下来，所以
                         //没有被当成是第一次刷新，因此将其放在这里赋值，会使其跟着fragment生命周期而一起重新赋值。
        mHomeAdapter = new HomeAdapter(getContext(), new ArrayList<Article>());
        mHomeAdapter.bindToRecyclerView(recyclerFragmentWeixinList);
        recyclerFragmentWeixinList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mHomeAdapter.setEnableLoadMore(false);
        normal.setEnableRefresh(false);
        recyclerFragmentWeixinList.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
    }

    @Override
    protected void requestData() {
        if (id >= 0) {
            showLoading();
            getPresenter().getWxArticles(id);
        }
    }

    public void refreshView() {
        if (isNormal() && id >= 0) {
            recyclerFragmentWeixinList.scrollToPosition(0);
            normal.autoRefresh();
        }
    }

    public void updateCollectState(boolean isCollected) {
        mHomeAdapter.getItem(currentClickPosition).setCollect(isCollected);
        mHomeAdapter.notifyItemChanged(currentClickPosition);
    }

    public void jumpToTop() {
        if (isNormal()) {
            recyclerFragmentWeixinList.smoothScrollToPosition(0);
        }
    }
}
