package com.huanglei.wanandroid.vp.main.knowledge;

import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huanglei.wanandroid.R;
import com.huanglei.wanandroid.model.bean.KnowledgeTabList;
import com.huanglei.wanandroid.model.bean.Tab;

import java.util.List;

/**
 * Created by 黄垒 on 2019/1/14.
 */

public class KnowledgeTabListAdapter extends BaseQuickAdapter<KnowledgeTabList,BaseViewHolder>{
    private Context context;
    public KnowledgeTabListAdapter(Context context,@Nullable List<KnowledgeTabList> data) {
        super(R.layout.item_tab_fragment_knowledge,data);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, KnowledgeTabList item) {
        helper.setText(R.id.tv_class_item_fragment_knowledge,item.getName());
        StringBuilder builder=new StringBuilder();
        for(Tab tab:item.getChildren()){
            builder.append(tab.getName()+"    ");
        }
        helper.setText(R.id.tv_tab_item_fragment_knowledge,builder.toString().trim());
    }
}
