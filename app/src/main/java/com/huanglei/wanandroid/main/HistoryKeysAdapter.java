package com.huanglei.wanandroid.main;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huanglei.wanandroid.R;
import com.huanglei.wanandroid.model.bean.HotKey;
import com.huanglei.wanandroid.model.db.HistoryKeyword;

import java.util.List;

/**
 * Created by 黄垒 on 2019/1/5.
 */

public class HistoryKeysAdapter extends BaseQuickAdapter<HistoryKeyword,BaseViewHolder> {

    public HistoryKeysAdapter(@Nullable List<HistoryKeyword> data) {
        super(R.layout.item_activity_search,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HistoryKeyword item) {
        helper.setText(R.id.tv_key_item_activity_search,item.getData());
        helper.addOnClickListener(R.id.tv_delete_item_activity_search);
    }
}
