package com.huanglei.wanandroid.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.huanglei.wanandroid.R;
import com.huanglei.wanandroid.app.Constants;
import com.huanglei.wanandroid.base.view.fragment.StateMVPBaseFragment;
import com.huanglei.wanandroid.contract.HomeFragmentContract;
import com.huanglei.wanandroid.model.bean.Article;
import com.huanglei.wanandroid.model.bean.Banner;
import com.huanglei.wanandroid.utils.CommonUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class HomeFragment extends StateMVPBaseFragment<HomeFragmentContract.Presenter> implements HomeFragmentContract.View {
    @BindView(R.id.recycler_view_fragment_home)
    RecyclerView recyclerViewFragmentHome;
    @BindView(R.id.normal)
    SmartRefreshLayout normal;
    @BindView(R.id.lin_root_fragment_home)
    LinearLayout linRootFragmentHome;
    private com.youth.banner.Banner mBanner;
    private List<Integer> ids;
    private List<String> titles;
    private List<String> imgs;
    private List<String> urls;
    private HomeAdapter mHomeAdapter;
    private int page = 0;
    private boolean isFirstRefresh = true;
    private int mClickPosition;

    public static HomeFragment newInstance() {
        HomeFragment homeFragment = new HomeFragment();
        return homeFragment;
    }

    @Override
    public HomeFragmentPresenter createPresenter() {
        return new HomeFragmentPresenter();
    }

    @Override
    protected void initView() {
        List<Article> list = new ArrayList<>();
        mHomeAdapter = new HomeAdapter(getContext(), list);
        recyclerViewFragmentHome.setAdapter(mHomeAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewFragmentHome.setLayoutManager(linearLayoutManager);
        recyclerViewFragmentHome.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        normal.setEnableRefresh(true);
        normal.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mHomeAdapter.setEnableLoadMore(false);
                getPresenter().getListData();
                getPresenter().getBannerData();
            }
        });
        mHomeAdapter.setEnableLoadMore(true);
        mHomeAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                normal.setEnableRefresh(false);
                getPresenter().addListData(++page);
            }
        }, recyclerViewFragmentHome);
        mBanner = (com.youth.banner.Banner) getLayoutInflater().inflate(R.layout.banner_home, linRootFragmentHome, false);
        ids = new ArrayList<>();
        titles = new ArrayList<>();
        imgs = new ArrayList<>();
        urls = new ArrayList<>();
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                mClickPosition = -1;
                ArticleDetailActivity.startArticleDetailActivity(getContext(),  titles.get(position),
                        urls.get(position));
            }
        });
        mBanner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE)
                .setBannerTitles(titles)
                .setImages(imgs)
                .setBannerAnimation(Transformer.DepthPage)
                .setDelayTime(5000)
                .setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        Glide.with(context).load(path).into(imageView);
                    }
                }).start();
        mHomeAdapter.addHeaderView(mBanner);
        mHomeAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.img_collect_item_fragment_home:
                    case R.id.img_collect_item_fragment_project:
                        if (((Article) adapter.getItem(position)).isCollect()) {
                            mHomeAdapter.getItem(position).setCollect(false);
                            mHomeAdapter.notifyItemChanged(position + 1);
                            getPresenter().cancelCollect(position, adapter.getData());
                        } else {
                            mHomeAdapter.getItem(position).setCollect(true);
                            mHomeAdapter.notifyItemChanged(position + 1);
                            getPresenter().collect(position, adapter.getData());
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        mHomeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mClickPosition = position;
                ArticleDetailActivity.startArticleDetailActivity(getContext(), Constants.MAIN_ACTIVITY, ((Article) adapter.getItem(position)).getId(),
                        ((Article) adapter.getItem(position)).getTitle(), ((Article) adapter.getItem(position)).getLink(),((Article)adapter.getItem(position)).isCollect());
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void requestData() {
        if (!isFirstRefresh) {
            recyclerViewFragmentHome.scrollToPosition(0);
            mHomeAdapter.setEnableLoadMore(false);
            normal.autoRefresh();
        } else {
            showLoading();
            getPresenter().getListData();
            getPresenter().getBannerData();
        }
    }


    @Override
    public void showNewListDataSucceed(List<Article> articles) {
        mHomeAdapter.replaceData(articles);
        page = 0;
        if (!isFirstRefresh) {
            normal.finishRefresh(true);
            mHomeAdapter.setEnableLoadMore(true);
        } else {
            showNormal();
            isFirstRefresh = false;
        }
    }

    @Override
    public void showNewListDataFailed(String errorMsg) {
        if (!isFirstRefresh) {
            normal.finishRefresh(false);
            mHomeAdapter.setEnableLoadMore(true);
        } else {
            showError();
        }
        CommonUtils.showToastMessage(getActivity(), errorMsg);
    }

    @Override
    public void showNewBannerDataSucceed(List<Banner> banners) {
        titles.clear();
        urls.clear();
        imgs.clear();
        ids.clear();
        for (Banner banner : banners) {
            titles.add(banner.getTitle());
            urls.add(banner.getUrl());
            imgs.add(banner.getImagePath());
            ids.add(banner.getId());
        }
        mBanner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE)
                .setBannerTitles(titles)
                .setImages(imgs)
                .setBannerAnimation(Transformer.DepthPage)
                .setDelayTime(5000)
                .setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        Glide.with(context).load(path).into(imageView);
                    }
                }).start();
    }

    @Override
    public void showNewBannerDataFailed(String errorMsg) {
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isNormal())
            mBanner.startAutoPlay();
    }

    @Override
    public void updateView() {
        if (!(mClickPosition < 0)) {
            mHomeAdapter.getItem(mClickPosition).setCollect(!mHomeAdapter.getItem(mClickPosition).isCollect());
            mHomeAdapter.notifyItemChanged(mClickPosition + 1);//这里要加1是因为要把recyclerView的headerView算上。
        }
    }

    @Override
    public void onStop() {
        if (isNormal())
            mBanner.stopAutoPlay();
        super.onStop();
    }


    @Override
    public void showAddListDataSucceed(List<Article> articles) {
        if (articles.isEmpty()) {
            mHomeAdapter.loadMoreEnd();
            page--;
        } else {
            mHomeAdapter.addData(articles);
            mHomeAdapter.loadMoreComplete();
        }
        normal.setEnableRefresh(true);
    }

    @Override
    public void showAddListDataFailed(String errorMsg) {
        mHomeAdapter.loadMoreFail();
        normal.setEnableRefresh(true);
        CommonUtils.showToastMessage(getActivity(), errorMsg);
        page--;
    }

    @Override
    public void showCollectSucceed(int position, List<Article> articles) {
        CommonUtils.showToastMessage(getActivity(), "收藏成功");
    }

    @Override
    public void showCollectFailed(int position, String errorMsg) {
        mHomeAdapter.getItem(position).setCollect(false);
        mHomeAdapter.notifyItemChanged(position + 1);
        CommonUtils.showToastMessage(getActivity(), errorMsg);
    }

    @Override
    public void showCancelCollectSucceed(int position, List<Article> articles) {
        CommonUtils.showToastMessage(getActivity(), "取消收藏成功");
    }

    @Override
    public void showCancelCollectFailed(int position, String errorMsg) {
        mHomeAdapter.getItem(position).setCollect(true);
        mHomeAdapter.notifyItemChanged(position + 1);
        CommonUtils.showToastMessage(getActivity(), errorMsg);

    }

//    @Override
//    public void showCancelCollectEvent(int position) {
//        mHomeAdapter.getItem(position).setCollect(false);
////        mHomeAdapter.setData(position,mHomeAdapter.getItem(position));
//        mHomeAdapter.notifyItemChanged(position + 1);//这里要加1是因为要把recyclerView的headerView算上。
//    }
//
//    @Override
//    public void showCollectEvent(int position) {
//        mHomeAdapter.getItem(position).setCollect(true);
////        mHomeAdapter.setData(position,mHomeAdapter.getItem(position));
//        mHomeAdapter.notifyItemChanged(position + 1);//这里要加1是因为要把recyclerView的headerView算上。
//    }
//
//    @Override
//    public void showLoginEvent() {
//        prepareRequestData(true);
//    }
//
//    @Override
//    public void showLogoutEvent() {
//        prepareRequestData(true);
//    }

    @Override
    public Context getViewContext() {
        return getContext();
    }
}
