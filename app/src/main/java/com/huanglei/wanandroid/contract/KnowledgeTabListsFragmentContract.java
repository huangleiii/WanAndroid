package com.huanglei.wanandroid.contract;

import com.huanglei.wanandroid.base.presenter.IBasePresenter;
import com.huanglei.wanandroid.base.view.IBaseView;
import com.huanglei.wanandroid.model.bean.KnowledgeTabList;
import com.huanglei.wanandroid.model.bean.Tab;

import java.util.List;

/**
 * Created by 黄垒 on 2019/1/13.
 */

public interface KnowledgeTabListsFragmentContract {
    interface Presenter extends IBasePresenter<View>{
        void getKnowledgeTabLists();
    }
    interface View extends IBaseView{
        void showKnowledgeTabListsSucceed(List<KnowledgeTabList> lists);
        void showKnowledgeTabListsFailed(String errorMsg);
    }
}
