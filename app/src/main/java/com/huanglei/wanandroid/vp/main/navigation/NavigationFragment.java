package com.huanglei.wanandroid.vp.main.navigation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.huanglei.wanandroid.R;
import com.huanglei.wanandroid.base.view.fragment.StateMVPBaseFragment;
import com.huanglei.wanandroid.contract.NavigationFragmentContract;
import com.huanglei.wanandroid.model.bean.NavigationArticleList;
import com.huanglei.wanandroid.model.bean.NavigationTab;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;

public class NavigationFragment extends StateMVPBaseFragment<NavigationFragmentContract.Presenter> implements NavigationFragmentContract.View {

    @BindView(R.id.recycler_tab_fragment_navigation)
    RecyclerView recyclerTabFragmentNavigation;
    @BindView(R.id.recycler_content_fragment_navigation)
    RecyclerView recyclerContentFragmentNavigation;
    @BindView(R.id.normal)
    LinearLayout normal;
    @BindView(R.id.lin_root_fragment_navigation)
    LinearLayout linRootFragmentNavigation;
    private NavigationTabAdapter tabAdapter;
    private NavigationContentAdapter contentAdapter;
    private int lastClickPosition;
    private LinearLayoutManager tabLayoutManager;
    private LinearLayoutManager contentLayoutManager;

    public static NavigationFragment newInstance(){
        NavigationFragment navigationFragment=new NavigationFragment();
        return navigationFragment;
    }
    @Override
    public Context getViewContext() {
        return getContext();
    }

    @Override
    public void showNavigationListSucceed(List<NavigationArticleList> navigationArticleLists) {
        showNormal();
        List<NavigationTab> tabs=new ArrayList<>();
        Iterator<NavigationArticleList> tabIterator=null;
        if(!navigationArticleLists.isEmpty()){
            tabIterator= navigationArticleLists.iterator();
            while (tabIterator.hasNext()){
                NavigationArticleList navigationArticleList =tabIterator.next();
                NavigationTab tab=new NavigationTab();
                tab.setName(navigationArticleList.getName());
                tab.setCid(navigationArticleList.getCid());
                tab.setChecked(false);
                tabs.add(tab);
            }
        }
        tabAdapter.setNewData(tabs);
        contentAdapter.setNewData(navigationArticleLists);
        tabAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                clickTab(position);
                contentLayoutManager.scrollToPositionWithOffset(position,0);
            }
        });
        recyclerContentFragmentNavigation.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==RecyclerView.SCROLL_STATE_IDLE){
                    int firstVisibleItemPosition=contentLayoutManager.findFirstVisibleItemPosition();
                    clickTab(firstVisibleItemPosition);
                }
            }
        });
        tabAdapter.getItem(0).setChecked(true);
        tabAdapter.notifyItemChanged(0);
        lastClickPosition=0;
    }
    private void clickTab(int position){
        if(lastClickPosition!=position){
            tabAdapter.getItem(lastClickPosition).setChecked(false);
            tabAdapter.notifyItemChanged(lastClickPosition);
            tabAdapter.getItem(position).setChecked(true);
            tabAdapter.notifyItemChanged(position);
            lastClickPosition=position;
        }
    }

    @Override
    public void showNavigationListFailed(String errorMsg) {
        showError();
    }
    public void jumpToTop() {
        clickTab(0);
        recyclerContentFragmentNavigation.smoothScrollToPosition(0);
        recyclerTabFragmentNavigation.scrollToPosition(0);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_navigation;
    }

    @Override
    protected NavigationFragmentContract.Presenter createPresenter() {
        return new NavigationFragmentPresenter();
    }

    @Override
    protected void initView() {
        tabAdapter=new NavigationTabAdapter(getContext(),new ArrayList<NavigationTab>());
        contentAdapter=new NavigationContentAdapter(getContext(),new ArrayList<NavigationArticleList>());
        tabAdapter.bindToRecyclerView(recyclerTabFragmentNavigation);
        contentAdapter.bindToRecyclerView(recyclerContentFragmentNavigation);
        tabLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        contentLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerContentFragmentNavigation.setLayoutManager(contentLayoutManager);
        recyclerTabFragmentNavigation.setLayoutManager(tabLayoutManager);
        recyclerTabFragmentNavigation.setNestedScrollingEnabled(false);
    }

    @Override
    protected void requestData() {
        showLoading();
        getPresenter().getNavigationLists();
    }
}
