package com.huanglei.wanandroid.vp.collect;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.huanglei.wanandroid.R;
import com.huanglei.wanandroid.app.Constants;
import com.huanglei.wanandroid.base.view.activity.MVPBaseActivity;
import com.huanglei.wanandroid.contract.CollectActivityContract;
import com.huanglei.wanandroid.model.bean.Article;
import com.huanglei.wanandroid.model.bean.ArticleInCollectPage;
import com.huanglei.wanandroid.vp.articledetail.ArticleDetailActivity;
import com.huanglei.wanandroid.utils.CommonUtils;
import com.huanglei.wanandroid.vp.login.LoginActivity;
import com.huanglei.wanandroid.widget.MyProgressDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CollectActivity extends MVPBaseActivity<CollectActivityContract.Presenter> implements CollectActivityContract.View {

    @BindView(R.id.tv_title_activity_collect)
    TextView tvTitleActivityCollect;
    @BindView(R.id.toolbar_activity_collect)
    Toolbar toolbarActivityCollect;
    @BindView(R.id.recycler_activity_collect)
    RecyclerView recyclerActivityCollect;
    @BindView(R.id.smart_activity_collect)
    SmartRefreshLayout smartActivityCollect;
    @BindView(R.id.float_button_activity_collect)
    FloatingActionButton floatButtonActivityCollect;
    private boolean isFirstLoad = true;
    private CollectPageAdapter mAdapter;
    private int page;
    private int clickPosition;
    private List<ArticleInCollectPage> beforeArticleList = new ArrayList<>();
    private MyProgressDialog myProgressDialog;

    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    public void showCollectArticlesSucceed(List<ArticleInCollectPage> articles) {
        mAdapter.setNewData(articles);
        page = 1;
        if (isFirstLoad) {
            mAdapter.setEmptyView(R.layout.empty);
            ((TextView) (mAdapter.getEmptyView().findViewById(R.id.tv_empty))).setText("我的收藏为空");
            isFirstLoad = false;
            smartActivityCollect.setEnableRefresh(true);
        } else {
            smartActivityCollect.finishRefresh(true);
        }
    }

    @Override
    public void showCollectArticlesFailed(String errorMsg) {
        if (isFirstLoad) {
            mAdapter.setEmptyView(R.layout.error);
            mAdapter.getEmptyView().findViewById(R.id.relativelayout_retry_error).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestData();
                }
            });
        } else {
            smartActivityCollect.finishRefresh(false);
            CommonUtils.showToastMessage(this, errorMsg);
        }
    }

    @Override
    public void showAddCollectArticlesSucceed(List<ArticleInCollectPage> articles) {
        page++;
        if (articles.isEmpty()) {
            mAdapter.loadMoreEnd(true);
            CommonUtils.showToastMessage(this, "无更多数据");
        } else {
            mAdapter.addData(articles);
            mAdapter.loadMoreComplete();
        }
    }

    @Override
    public void showAddCollectArticlesFailed(String errorMsg) {
        mAdapter.loadMoreFail();
        CommonUtils.showToastMessage(this, errorMsg);
    }

    @Override
    public void showCancelCollectSucceed(int position, List<ArticleInCollectPage> articles) {
        CommonUtils.showToastMessage(this, "取消收藏成功");
    }

    @Override
    public void showCancelCollectFailed(int position, List<ArticleInCollectPage> articles, boolean isLoginExpired,String errorMsg) {
        if(isLoginExpired)
            LoginActivity.startLoginActivity(this,Constants.COLLECT_ACTIVITY);
        CommonUtils.showToastMessage(this, errorMsg);
        mAdapter.replaceData(articles);
    }

    @Override
    public void showCollectOutsideArticleSucceed(String title,String author,String link) {
        myProgressDialog.dismiss();
        ArticleInCollectPage article=new ArticleInCollectPage();
        article.setTitle(title);
        article.setAuthor(author);
        article.setLink(link);
        article.setNiceDate("刚刚");
        mAdapter.getData().add(0,article);
        mAdapter.notifyItemInserted(0);
        recyclerActivityCollect.smoothScrollToPosition(0);
        CommonUtils.showToastMessage(this,"收藏成功");
    }

    @Override
    public void showCollectOutsideArticleFailed(boolean isLoginExpired,String errorMsg) {
        myProgressDialog.dismiss();
        CommonUtils.showToastMessage(this,errorMsg);
    }

    @Override
    public void subscribeLoginEvent(String activityName) {
        if (activityName.equals(Constants.COLLECT_ACTIVITY))
            refreshView();
    }

    @Override
    public void subscribeCancelCollectEvent(String activityName) {
        if (activityName.equals(Constants.COLLECT_ACTIVITY))
            mAdapter.remove(clickPosition);
    }

    @Override
    public void subscribeCollectEvent(String activityName) {
        if (activityName.equals(Constants.COLLECT_ACTIVITY))
            mAdapter.replaceData(beforeArticleList);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_collect;
    }

    @Override
    protected CollectActivityContract.Presenter createPresenter() {
        return new CollectActivityPresenter();
    }


    @Override
    protected void initView() {
        myProgressDialog=new MyProgressDialog(this);
        setSupportActionBar(toolbarActivityCollect);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvTitleActivityCollect.setText("我的收藏");
        mAdapter = new CollectPageAdapter(this, new ArrayList<ArticleInCollectPage>());
        mAdapter.bindToRecyclerView(recyclerActivityCollect);
        recyclerActivityCollect.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerActivityCollect.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mAdapter.setEmptyView(R.layout.empty);
        ((TextView) (mAdapter.getEmptyView().findViewById(R.id.tv_empty))).setText("我的收藏为空");
        smartActivityCollect.setEnableRefresh(false);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                clickPosition = position;
                beforeArticleList.clear();
                beforeArticleList.addAll(mAdapter.getData());
                ArticleDetailActivity.startArticleDetailActivity(CollectActivity.this, Constants.COLLECT_ACTIVITY,
                        mAdapter.getItem(position).getOriginId(),//注意是getOriginId()而非getId()
                        mAdapter.getItem(position).getTitle(), mAdapter.getItem(position).getLink(),
                        true);
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.img_collect_item_fragment_home:
                        getPresenter().cancelCollect( position, mAdapter.getData());
                        mAdapter.remove(position);
                        break;
                    default:
                        break;
                }
            }
        });
        //设置了Listener，setEnableLoadMore()方法就无效了，状态始终为true
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getPresenter().addCollectArticles(page);
            }
        }, recyclerActivityCollect);
        smartActivityCollect.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getPresenter().getCollectArticles();
            }
        });
        floatButtonActivityCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToTop();
            }
        });
    }

    @Override
    protected void requestData() {
        mAdapter.setEmptyView(R.layout.loading);
        getPresenter().getCollectArticles();
    }

    private void refreshView() {
        if (mAdapter.getEmptyView().findViewById(R.id.tv_empty) != null) {
            recyclerActivityCollect.scrollToPosition(0);
            smartActivityCollect.autoRefresh();
        }
    }

    private void jumpToTop() {
        recyclerActivityCollect.smoothScrollToPosition(0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAdapter.getEmptyView().findViewById(R.id.tv_error) != null) {
            requestData();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.collect_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.add_collect_menu:
                View view=getLayoutInflater().inflate(R.layout.dialog_collect,null);
                final EditText etTitle=view.findViewById(R.id.et_title_dialog_collect);
                final EditText etAuthor=view.findViewById(R.id.et_author_dialog_collect);
                final EditText etLink=view.findViewById(R.id.et_link_dialog_collect);
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                AlertDialog alertDialog=builder.setTitle("收藏自定义文章")
                        .setView(view)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String title=etTitle.getText().toString();
                                String author=etAuthor.getText().toString();
                                String link=etLink.getText().toString();
                                if(TextUtils.isEmpty(title)||TextUtils.isEmpty(author)||TextUtils.isEmpty(link)){
                                    CommonUtils.showToastMessage(CollectActivity.this,"输入不能为空");
                                }else {
                                    getPresenter().collectOutsideArticle(title,author,link);
                                    myProgressDialog.show();
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).create();
                //可以通过getWindow的方式设置alertdialog的长宽gravity等。
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
                break;
            default:
                break;
        }
        return true;
    }

}
