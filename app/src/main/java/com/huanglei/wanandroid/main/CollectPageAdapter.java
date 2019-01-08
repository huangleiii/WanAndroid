package com.huanglei.wanandroid.main;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huanglei.wanandroid.R;
import com.huanglei.wanandroid.model.bean.ArticleInCollectPage;

import java.util.List;

/**
 * Created by 黄垒 on 2019/1/7.
 */

public class CollectPageAdapter extends BaseQuickAdapter<ArticleInCollectPage, BaseViewHolder> {
    private Context mContext;

    public CollectPageAdapter(Context context, @Nullable List<ArticleInCollectPage> data) {
        super(R.layout.item_fragment_home,data);
        mContext=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ArticleInCollectPage item) {
        helper.setText(R.id.tv_title_item_fragment_home, Html.fromHtml(item.getTitle()))
                .setText(R.id.tv_date_item_fragment_home, item.getNiceDate())
                .setText(R.id.tv_author_item_fragment_home, item.getAuthor())
                .setText(R.id.tv_type_item_fragment_home, !TextUtils.isEmpty(item.getChapterName()) ? item.getChapterName() : "")
                .setImageResource(R.id.img_collect_item_fragment_home, R.drawable.ic_favorite_red_24dp)
                .addOnClickListener(R.id.img_collect_item_fragment_home)
                .addOnClickListener(R.id.tv_type_item_fragment_home);
    }
}
