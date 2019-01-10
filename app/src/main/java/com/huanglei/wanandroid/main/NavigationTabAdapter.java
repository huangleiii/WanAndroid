package com.huanglei.wanandroid.main;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huanglei.wanandroid.R;
import com.huanglei.wanandroid.contract.NavigationFragmentContract;
import com.huanglei.wanandroid.model.bean.NavigationTab;

import java.util.List;

/**
 * Created by 黄垒 on 2019/1/10.
 */

public class NavigationTabAdapter extends BaseQuickAdapter<NavigationTab,BaseViewHolder>{
    private Context context;
    public NavigationTabAdapter(Context context,@Nullable List<NavigationTab> data) {
        super(R.layout.item_fragment_navigation_tab,data);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, NavigationTab item) {
        helper.setText(R.id.tv_item_fragment_navigation_tab,item.getName());
        if(item.isChecked()){
            helper.setBackgroundColor(R.id.tv_item_fragment_navigation_tab,context.getResources().getColor(R.color.transparent));
            helper.setTextColor(R.id.tv_item_fragment_navigation_tab,context.getResources().getColor(R.color.blue));
        }else{
            helper.setBackgroundColor(R.id.tv_item_fragment_navigation_tab,context.getResources().getColor(R.color.lightGrey));
            helper.setTextColor(R.id.tv_item_fragment_navigation_tab,context.getResources().getColor(R.color.grey));
        }
    }
}
