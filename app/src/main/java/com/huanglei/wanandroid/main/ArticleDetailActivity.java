package com.huanglei.wanandroid.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huanglei.wanandroid.R;
import com.huanglei.wanandroid.base.view.activity.MVPBaseActivity;
import com.huanglei.wanandroid.contract.ArticleDetailActivityContract;
import com.huanglei.wanandroid.utils.CommonUtils;
import com.just.agentweb.AgentWeb;

import butterknife.BindView;

public class ArticleDetailActivity extends MVPBaseActivity<ArticleDetailActivityContract.Presenter> implements ArticleDetailActivityContract.View {
    @BindView(R.id.toolbar_activity_article_detail)
    Toolbar toolbarActivityArticleDetail;
    @BindView(R.id.linear_content_activity_article_detail)
    LinearLayout linearContentActivityArticleDetail;
    @BindView(R.id.tv_title_activity_article_detail)
    TextView tvTitleActivityArticleDetail;
    private AgentWeb mAgentWeb;
    public static final String ARTICLE_TITLE = "article title";
    private String articleTitle;
    public static final String ARTICLE_LINK = "article link";
    private String articleLink;
    public static final String ARTICLE_ID = "article id";
    private int articleId;
    public static final String ARTICLE_IS_COLLECTED = "article is collected or not";
    private boolean articleIsCollected;
    public static final String ARTICLE_POSITION="article position";
    private int articlePosition;
    public static final String ARTICLE_IS_HOME="article is on the front page or not";
    private boolean articleIsHome;
    public static final String ARTICLE_CAN_COLLECT="article can collect or not";
    private boolean articleCanCollect;
    private WebView webView;

    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    public void showCollectSucceed() {
        CommonUtils.showToastMessage(this,"收藏成功");
    }

    @Override
    public void showCollectFailed(String errorMsg) {
        articleIsCollected=false;
        supportInvalidateOptionsMenu();
        CommonUtils.showToastMessage(this,errorMsg);
    }

    @Override
    public void showCancelCollectSucceed() {
        CommonUtils.showToastMessage(this,"取消收藏成功");
    }

    @Override
    public void showCancelCollectFailed(String errorMsg) {
        articleIsCollected=true;
        supportInvalidateOptionsMenu();
        CommonUtils.showToastMessage(this,errorMsg);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_article_detail;
    }

    @Override
    protected void initToolbar() {
        getBundleData();
        setSupportActionBar(toolbarActivityArticleDetail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvTitleActivityArticleDetail.setText(articleTitle);
    }

    @Override
    protected void initView() {
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(linearContentActivityArticleDetail, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator()
                .setWebChromeClient(new WebChromeClient(){
                    @Override
                    public void onReceivedTitle(WebView view, String title) {
                        tvTitleActivityArticleDetail.setText(title);
                    }
                })
                .setMainFrameErrorView(R.layout.error,R.id.tv_retry_error)
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                .createAgentWeb()
                .ready()
                .go(articleLink);
        webView=mAgentWeb.getWebCreator().getWebView();
        WebSettings mSettings = webView.getSettings();
        mSettings.setBlockNetworkImage(false);//设置为true就不加载图片
        mSettings.setAppCacheEnabled(true);
        mSettings.setDomStorageEnabled(true);
        mSettings.setDatabaseEnabled(true);
        if (CommonUtils.isNetworkConnected()) {
            mSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            mSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        mSettings.setJavaScriptEnabled(true);
        mSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        mSettings.setSupportZoom(true);
        mSettings.setBuiltInZoomControls(true);
        //不显示缩放按钮
        mSettings.setDisplayZoomControls(false);
        //是否允许使用 <viewport> 标签
        mSettings.setUseWideViewPort(true);
        //缩放至屏幕的大小
        mSettings.setLoadWithOverviewMode(true);
        //支持内容重新布局
        mSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
    }

    private void getBundleData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            articleTitle = bundle.getString(ARTICLE_TITLE);
            articleLink = bundle.getString(ARTICLE_LINK);
            articleId = bundle.getInt(ARTICLE_ID);
            articleIsCollected = bundle.getBoolean(ARTICLE_IS_COLLECTED);
            articleIsHome=bundle.getBoolean(ARTICLE_IS_HOME);
            articlePosition=bundle.getInt(ARTICLE_POSITION);
            articleCanCollect=bundle.getBoolean(ARTICLE_CAN_COLLECT);
        }
    }

    @Override
    protected void requestData() {

    }

    @Override
    protected ArticleDetailActivityContract.Presenter createPresenter() {
        return new ArticleDetailActivityPresenter();
    }

    @Override
    protected void onDestroy() {
        mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAgentWeb.handleKeyEvent(keyCode, event))
            return true;
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (!mAgentWeb.back())
            finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu,menu);
        MenuItem collectMenuItem=menu.findItem(R.id.favorite_detail_menu);
        if(!articleCanCollect){
            collectMenuItem.setVisible(false);
        }else {
            if(articleIsCollected){
                collectMenuItem.setIcon(R.drawable.ic_favorite_red_24dp);
                collectMenuItem.setTitle("取消收藏");
            }else {
                collectMenuItem.setIcon(R.drawable.ic_favorite_border_white_24dp);
                collectMenuItem.setTitle("收藏");
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.favorite_detail_menu:
                if(articleIsCollected){
                    articleIsCollected=false;
                    getPresenter().cancelCollect(articlePosition,articleId);
                }else {
                    articleIsCollected=true;
                    getPresenter().collect(articlePosition,articleId);
                }
                supportInvalidateOptionsMenu();
                break;
            case R.id.share_detail_menu:
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,getString(R.string.app_name)+"分享【"+articleTitle+"】:\n"+articleLink);
                startActivity(Intent.createChooser(intent,"分享文章"));
                break;
            case R.id.open_detail_menu:
                Intent intent1=new Intent(Intent.ACTION_VIEW);
                intent1.setData(Uri.parse(articleLink));
                startActivity(intent1);
                break;
            default:
                break;
        }
        return true;
    }
}