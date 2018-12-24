package com.huanglei.wanandroid;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huanglei.wanandroid.model.bean.Article;

import java.util.List;

/**
 * Created by HuangLei on 2018/11/20.
 */

public class HomeAdapter extends BaseMultiItemQuickAdapter<Article,BaseViewHolder> {
private Context mContext;
    public HomeAdapter(Context context,@Nullable List<Article> data) {
        super(data);
        mContext=context;
        addItemType(Article.TYPE_ARTICLE,R.layout.item_fragment_home);
        addItemType(Article.TYPE_PROJECT,R.layout.item_fragment_project);
    }

    @Override
    protected void convert(BaseViewHolder helper, Article item) {
        switch (helper.getItemViewType()){
            case Article.TYPE_ARTICLE:
                helper.setText(R.id.tv_title_item_fragment_home,item.getTitle())
                        .setText(R.id.tv_date_item_fragment_home,item.getNiceDate())
                        .setText(R.id.tv_author_item_fragment_home,item.getAuthor())
                        .setText(R.id.tv_type_item_fragment_home,item.getSuperChapterName()+"/"+item.getChapterName())
                        .setImageResource(R.id.img_collect_item_fragment_home,
                                item.isCollect()?R.drawable.ic_favorite_red_24dp:R.drawable.ic_favorite_grey_24dp)
                        .setGone(R.id.tv_tag_new_item_fragment_home,item.isFresh())
                        .setGone(R.id.tv_tag_weixin_item_fragment_home,item.getSuperChapterName().equals("公众号"))
                        .addOnClickListener(R.id.img_collect_item_fragment_home)
                        .addOnClickListener(R.id.tv_type_item_fragment_home);
                break;
            case Article.TYPE_PROJECT:
                helper.setText(R.id.tv_title_item_fragment_project,item.getTitle())
                        .setText(R.id.tv_date_item_fragment_project,item.getNiceDate())
                        .setText(R.id.tv_author_item_fragment_project,item.getAuthor())
                        .setText(R.id.tv_type_item_fragment_project,item.getSuperChapterName()+"/"+item.getChapterName())
                        .setImageResource(R.id.img_collect_item_fragment_project,
                                item.isCollect()?R.drawable.ic_favorite_red_24dp:R.drawable.ic_favorite_grey_24dp)
                        .setGone(R.id.tv_tag_new_item_fragment_project,item.isFresh())
//                        .setGone(R.id.tv_tag_project_item_fragment_project,item.getSuperChapterName().equals("开源项目主Tab"))
                        .addOnClickListener(R.id.img_collect_item_fragment_project)
                        .addOnClickListener(R.id.tv_type_item_fragment_project);
                Glide.with(mContext).load(item.getEnvelopePic()).into((ImageView)helper.getView(R.id.img_item_fragment_project));
                break;
            default:
                break;
        }
    }

}
