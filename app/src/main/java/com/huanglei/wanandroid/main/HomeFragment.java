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
    private List<String> titles;
    private List<String> imgs;
    private List<String> urls;
    private HomeAdapter mHomeAdapter;
    private int page = 0;
    private boolean isFirstRefreshSucceed = true;

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
        titles = new ArrayList<>();
        imgs = new ArrayList<>();
        urls = new ArrayList<>();
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
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent intent = new Intent(getActivity(), ArticleDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(ArticleDetailActivity.ARTICLE_TITLE, titles.get(position));
                bundle.putString(ArticleDetailActivity.ARTICLE_LINK, urls.get(position));
                bundle.putBoolean(ArticleDetailActivity.ARTICLE_CAN_COLLECT, false);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
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
                Intent intent = new Intent(getActivity(), ArticleDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(ArticleDetailActivity.ARTICLE_ID, ((Article) adapter.getItem(position)).getId());
                bundle.putString(ArticleDetailActivity.ARTICLE_TITLE, ((Article) adapter.getItem(position)).getTitle());
                bundle.putString(ArticleDetailActivity.ARTICLE_LINK, ((Article) adapter.getItem(position)).getLink());
                bundle.putBoolean(ArticleDetailActivity.ARTICLE_IS_COLLECTED, ((Article) adapter.getItem(position)).isCollect());
                bundle.putBoolean(ArticleDetailActivity.ARTICLE_CAN_COLLECT, true);
                bundle.putBoolean(ArticleDetailActivity.ARTICLE_IS_HOME, true);
                bundle.putInt(ArticleDetailActivity.ARTICLE_POSITION, position);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void requestData() {
        showLoading();
        getPresenter().getListData();
        getPresenter().getBannerData();
    }


    @Override
    public void showNewListDataSucceed(List<Article> articles) {
        mHomeAdapter.replaceData(articles);
        page = 0;
        if (!isFirstRefreshSucceed) {
            normal.finishRefresh(true);
            mHomeAdapter.setEnableLoadMore(true);
        } else {
            showNormal();
            isFirstRefreshSucceed = false;
        }
    }

    @Override
    public void showNewListDataFailed(String errorMsg) {
        if (!isFirstRefreshSucceed) {
            normal.finishRefresh(false);
            mHomeAdapter.setEnableLoadMore(true);
        } else {
            showError();
        }
        CommonUtils.showToastMessage(getActivity(), errorMsg);
    }

    @Override
    public void showNewBannerDataSucceed(List<Banner> banners) {
        for (Banner banner : banners) {
            titles.add(banner.getTitle());
            urls.add(banner.getUrl());
            imgs.add(banner.getImagePath());
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

    @Override
    public void showCancelCollectEvent(int position) {
        mHomeAdapter.getItem(position).setCollect(false);
//        mHomeAdapter.setData(position,mHomeAdapter.getItem(position));
        mHomeAdapter.notifyItemChanged(position + 1);//这里要加1是因为要把recyclerView的headerView算上。
    }

    @Override
    public void showCollectEvent(int position) {
        mHomeAdapter.getItem(position).setCollect(true);
//        mHomeAdapter.setData(position,mHomeAdapter.getItem(position));
        mHomeAdapter.notifyItemChanged(position + 1);//这里要加1是因为要把recyclerView的headerView算上。
    }

    @Override
    public void showLoginEvent() {
        mHomeAdapter.setEnableLoadMore(false);
        normal.autoRefresh();
    }

    @Override
    public void showLogoutEvent() {
        mHomeAdapter.setEnableLoadMore(false);
        normal.autoRefresh();
    }

}
