package com.huanglei.wanandroid.vp.search;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.huanglei.wanandroid.R;
import com.huanglei.wanandroid.base.view.activity.MVPBaseActivity;
import com.huanglei.wanandroid.contract.SearchActivityContract;
import com.huanglei.wanandroid.model.bean.HotKey;
import com.huanglei.wanandroid.model.db.HistoryKeyword;
import com.huanglei.wanandroid.vp.hotwebsite.HistoryKeysAdapter;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SearchActivity extends MVPBaseActivity<SearchActivityContract.Presenter> implements SearchActivityContract.View {


    @BindView(R.id.img_back_activity_search)
    ImageView imgBackActivitySearch;
    @BindView(R.id.ed_search_activity_search)
    EditText edSearchActivitySearch;
    @BindView(R.id.img_search_activity_search)
    ImageView imgSearchActivitySearch;
    @BindView(R.id.flowlayout_activity_search)
    TagFlowLayout flowlayoutActivitySearch;
    @BindView(R.id.tv_clear_activity_search)
    TextView tvClearActivitySearch;
    @BindView(R.id.recycler_history_activity_search)
    RecyclerView recyclerHistoryActivitySearch;
    @BindView(R.id.relativelayout_loading)
    RelativeLayout loading;
    @BindView(R.id.relativelayout_retry_error)
    RelativeLayout retryError;
    private HistoryKeysAdapter historyKeysAdapter;

    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    public void showHotSearchKeywordsSucceed(final List<HotKey> keys) {
        showNormal();
        flowlayoutActivitySearch.setAdapter(new TagAdapter<HotKey>(keys) {
            @Override
            public View getView(FlowLayout parent, int position, HotKey key) {
                TextView textView = (TextView) LayoutInflater.from(SearchActivity.this).inflate(R.layout.item_flowlayout_hotkey, parent, false);
                textView.setText(key.getName());
                return textView;
            }
        });
        flowlayoutActivitySearch.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                search(keys.get(position).getName());
                return true;
            }
        });
    }

    @Override
    public void showHotSearchKeywordsFailed(String errorMs) {
        showError();
    }

    @Override
    public void showHistorySearchKeywordsSucceed(List<HistoryKeyword> keys) {
        historyKeysAdapter.replaceData(keys);
        historyKeysAdapter.notifyDataSetChanged();
    }

    @Override
    public void showHistorySearchKeywordsFailed(String errorMsg) {

    }

    @Override
    public void showAddHistorySearchKeywordSucceed(List<HistoryKeyword> keys) {
        historyKeysAdapter.replaceData(keys);
        historyKeysAdapter.notifyDataSetChanged();
    }

    @Override
    public void showAddHistorySearchKeywordFailed(String errorMsg) {

    }

    @Override
    public void showDeleteHistorySearchKeywordSucceed(int position) {
        historyKeysAdapter.getData().remove(position);
        historyKeysAdapter.notifyDataSetChanged();
    }

    @Override
    public void showDeleteHistorySearchKeywordFailed(String errorMsg) {

    }

    @Override
    public void showClearHistorySearchKeywordsSucceed() {
        historyKeysAdapter.getData().clear();
        historyKeysAdapter.notifyDataSetChanged();
    }

    @Override
    public void showClearHistorySearchKeywordsFailed(String errorMsg) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }


    @Override
    protected void initView() {
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        setFinishOnTouchOutside(false);
        imgBackActivitySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imgSearchActivitySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = edSearchActivitySearch.getText().toString();
                if (!TextUtils.isEmpty(s))
                    search(s);
            }
        });
        historyKeysAdapter = new HistoryKeysAdapter(new ArrayList<HistoryKeyword>());
        historyKeysAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                search(historyKeysAdapter.getItem(position).getData());
            }
        });
        historyKeysAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.tv_delete_item_activity_search:
                        getPresenter().deleteHistorySearchKeyword(position,historyKeysAdapter.getItem(position).getData());
                        break;
                    default:
                        break;
                }
            }
        });
        historyKeysAdapter.bindToRecyclerView(recyclerHistoryActivitySearch);
        recyclerHistoryActivitySearch.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        historyKeysAdapter.setEmptyView(R.layout.empty);
        ((TextView) (historyKeysAdapter.getEmptyView().findViewById(R.id.tv_empty))).setText("搜索历史为空");
        tvClearActivitySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().clearHistorySearchKeywords();
            }
        });
        edSearchActivitySearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search(v.getText().toString());
                    return true;
                }
                return false;
            }
        });
        retryError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                getPresenter().getHotSearchKeywords();
            }
        });
    }
    private void showNormal(){
        flowlayoutActivitySearch.setVisibility(View.VISIBLE);
        retryError.setVisibility(View.GONE);
        loading.setVisibility(View.GONE);
    }
    private void showError(){
        flowlayoutActivitySearch.setVisibility(View.GONE);
        retryError.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
    }
    private void showLoading(){
        flowlayoutActivitySearch.setVisibility(View.GONE);
        retryError.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
    }

    private void search(String data) {
        getPresenter().addHistorySearchKeyword(data);
        finish();
        SearchListActivity.startSearchListActivity(this, data);
    }

    @Override
    protected void requestData() {
        getPresenter().getHotSearchKeywords();
        getPresenter().getHistorySearchKeywords();
    }


    @Override
    protected SearchActivityContract.Presenter createPresenter() {
        return new SearchActivityPresenter();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);//将原生的退场动画去除掉，这样才不会对style中设置的动画产生干扰
    }

}
