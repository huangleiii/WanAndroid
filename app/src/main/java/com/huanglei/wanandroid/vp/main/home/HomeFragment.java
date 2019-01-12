package com.huanglei.wanandroid.vp.main.home;

import android.content.Context;
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
import com.huanglei.wanandroid.vp.articledetail.ArticleDetailActivity;
import com.huanglei.wanandroid.utils.CommonUtils;
import com.huanglei.wanandroid.vp.login.LoginActivity;
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
        normal.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getPresenter().getListData();
                getPresenter().getBannerData();
            }
        });
        mHomeAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
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
                ArticleDetailActivity.startArticleDetailActivity(getContext(), titles.get(position),
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
                        ((Article) adapter.getItem(position)).getTitle(), ((Article) adapter.getItem(position)).getLink(), ((Article) adapter.getItem(position)).isCollect());
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

    public void refreshView() {
        if(isNormal()){
            recyclerViewFragmentHome.scrollToPosition(0);
            normal.autoRefresh();
        }
    }


    @Override
    public void showNewListDataSucceed(List<Article> articles) {
        mHomeAdapter.setNewData(articles);//不同于replaceData()，这个方法可以刷新LoadMoreEnd的状态，使其可以再次LoadMore。
        page = 0;
        if (!isFirstRefresh) {
            normal.finishRefresh(true);
        } else {
            showNormal();
            isFirstRefresh = false;
        }
    }

    @Override
    public void showNewListDataFailed(String errorMsg) {
        if (!isFirstRefresh) {
            normal.finishRefresh(false);
            CommonUtils.showToastMessage(getActivity(), errorMsg);
        } else {
            showError();
        }
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

    public void updateCollectState(boolean isCollected) {
        mHomeAdapter.getItem(mClickPosition).setCollect(isCollected);
        mHomeAdapter.notifyItemChanged(mClickPosition + 1);//这里要加1是因为要把recyclerView的headerView算上。
    }

    public void jumpToTop() {
        recyclerViewFragmentHome.smoothScrollToPosition(0);
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
            mHomeAdapter.loadMoreEnd(true);
            CommonUtils.showToastMessage(getActivity(),"无更多数据");
            page--;
        } else {
            mHomeAdapter.addData(articles);
            mHomeAdapter.loadMoreComplete();
        }
    }

    @Override
    public void showAddListDataFailed(String errorMsg) {
        mHomeAdapter.loadMoreFail();
        page--;
        CommonUtils.showToastMessage(getActivity(), errorMsg);
    }

    @Override
    public void showCollectSucceed(int position, List<Article> articles) {
        CommonUtils.showToastMessage(getActivity(), "收藏成功");
    }

    @Override
    public void showCollectFailed(int position,List<Article> articles,boolean isLoginExpired, String errorMsg) {
        if(isLoginExpired)
            LoginActivity.startLoginActivity(getContext(),Constants.MAIN_ACTIVITY);
        mHomeAdapter.getItem(position).setCollect(false);
        mHomeAdapter.notifyItemChanged(position + 1);
        CommonUtils.showToastMessage(getActivity(), errorMsg);
    }

    @Override
    public void showCancelCollectSucceed(int position, List<Article> articles) {
        CommonUtils.showToastMessage(getActivity(), "取消收藏成功");
    }

    @Override
    public void showCancelCollectFailed(int position,List<Article> articles, boolean isLoginExpired,String errorMsg) {
        if(isLoginExpired)
            LoginActivity.startLoginActivity(getContext(),Constants.MAIN_ACTIVITY);
        mHomeAdapter.getItem(position).setCollect(true);
        mHomeAdapter.notifyItemChanged(position + 1);
        CommonUtils.showToastMessage(getActivity(), errorMsg);

    }


    @Override
    public Context getViewContext() {
        return getContext();
    }
}
