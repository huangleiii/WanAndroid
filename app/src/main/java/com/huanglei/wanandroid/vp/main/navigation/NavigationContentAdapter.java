package com.huanglei.wanandroid.vp.main.navigation;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huanglei.wanandroid.R;
import com.huanglei.wanandroid.app.Constants;
import com.huanglei.wanandroid.model.bean.Article;
import com.huanglei.wanandroid.model.bean.NavigationArticleList;
import com.huanglei.wanandroid.vp.articledetail.ArticleDetailActivity;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

/**
 * Created by 黄垒 on 2019/1/10.
 */

public class NavigationContentAdapter extends BaseQuickAdapter<NavigationArticleList,BaseViewHolder>{
    private Context context;
    public NavigationContentAdapter(Context context,@Nullable List<NavigationArticleList> data) {
        super(R.layout.item_fragment_navigation_content,data);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, NavigationArticleList item) {
        helper.setText(R.id.tv_item_fragment_navigation_content,item.getName());
        final List<Article> articles=item.getArticles();
        TagFlowLayout tagFlowLayout=helper.getView(R.id.tagflowlayout_item_fragment_navigation_content);
        tagFlowLayout.setAdapter(new TagAdapter<Article>(articles) {
            @Override
            public View getView(FlowLayout parent, int position,Article article) {
                TextView textView=(TextView)LayoutInflater.from(context).inflate(R.layout.item_flowlayout_navigation,parent,false);
                textView.setText(article.getTitle());
                return textView;
            }
        });
        tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                ArticleDetailActivity.startArticleDetailActivity(context, Constants.MAIN_ACTIVITY,articles.get(position).getId(),
                        articles.get(position).getTitle(),articles.get(position).getLink(),articles.get(position).isCollect());
                return true;
            }
        });
    }
}
