package com.huanglei.wanandroid.vp.hotwebsite;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huanglei.wanandroid.R;
import com.huanglei.wanandroid.base.view.activity.MVPBaseActivity;
import com.huanglei.wanandroid.contract.HotWebsiteActivityContract;
import com.huanglei.wanandroid.model.bean.HotWebsite;
import com.huanglei.wanandroid.vp.articledetail.ArticleDetailActivity;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

import butterknife.BindView;

public class HotWebsiteActivity extends MVPBaseActivity<HotWebsiteActivityContract.Presenter> implements HotWebsiteActivityContract.View {
    //    @BindView(R.id.toolbar_activity_hot_website)
//    Toolbar toolbarActivityHotWebsite;
    @BindView(R.id.tv_title_activity_hot_website)
    TextView tvTitleActivityHotWebsite;
    @BindView(R.id.img_close_activity_hot_website)
    ImageView imgCloseActivityHotWebsite;
    @BindView(R.id.flowlayout_activity_hot_website)
    TagFlowLayout flowlayoutActivityHotWebsite;
    @BindView(R.id.relativelayout_loading)
    RelativeLayout loading;
    @BindView(R.id.relativelayout_retry_error)
    RelativeLayout retryError;
    @BindView(R.id.normal)
    NestedScrollView normal;
    private List<HotWebsite> mHotWebsites;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hot_website;
    }


    @Override
    protected void initView() {
        tvTitleActivityHotWebsite.setText("常用网站");
        imgCloseActivityHotWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        setFinishOnTouchOutside(false);
        retryError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                getPresenter().getHotWebsiteList();
            }
        });
    }

    @Override
    protected void requestData() {
        showLoading();
        getPresenter().getHotWebsiteList();
    }
    private void showLoading(){
        loading.setVisibility(View.VISIBLE);
        retryError.setVisibility(View.GONE);
        normal.setVisibility(View.GONE);
    }
    private void showError(){
        loading.setVisibility(View.GONE);
        retryError.setVisibility(View.VISIBLE);
        normal.setVisibility(View.GONE);
    }
    private void showNormal(){
        loading.setVisibility(View.GONE);
        retryError.setVisibility(View.GONE);
        normal.setVisibility(View.VISIBLE);

    }

    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    public void showHotWebsiteListSucceed(List<HotWebsite> hotWebsites) {
        showNormal();
        mHotWebsites = hotWebsites;
        flowlayoutActivityHotWebsite.setAdapter(new TagAdapter<HotWebsite>(hotWebsites) {
            @Override
            public View getView(FlowLayout parent, int position, HotWebsite hotWebsite) {
                TextView textView = (TextView) getLayoutInflater().inflate(R.layout.item_flowlayout_hotwebsite, parent, false);
                textView.setText(hotWebsite.getName());
                return textView;
            }
        });
        flowlayoutActivityHotWebsite.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                ArticleDetailActivity.startArticleDetailActivity(HotWebsiteActivity.this,
                        mHotWebsites.get(position).getName(), mHotWebsites.get(position).getLink());
                finish();
                return true;
            }
        });
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                finish();
//                break;
//            default:
//                break;
//        }
//        return true;
//    }

    @Override
    public void showHotWebsiteListFailed(String errorMsg) {
        showError();
    }

    @Override
    protected HotWebsiteActivityContract.Presenter createPresenter() {
        return new HotWebsiteActivityPresenter();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);//将原生的退场动画去除掉，这样才不会对style中设置的动画产生干扰
    }

}
