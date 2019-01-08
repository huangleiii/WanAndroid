package com.huanglei.wanandroid.contract;

import com.huanglei.wanandroid.base.presenter.IBasePresenter;
import com.huanglei.wanandroid.base.view.IBaseView;
import com.huanglei.wanandroid.model.bean.Article;
import com.huanglei.wanandroid.model.bean.HotKey;
import com.huanglei.wanandroid.model.db.HistoryKeyword;

import java.util.List;

/**
 * Created by 黄垒 on 2018/12/26.
 */

public interface SearchActivityContract {
    interface Presenter extends IBasePresenter<View>{
        void getHotSearchKeywords();
        void getHistorySearchKeywords();
        void addHistorySearchKeyword(String keyword);
        void deleteHistorySearchKeyword(int position,String keyword);
        void clearHistorySearchKeywords();
    }
    interface View extends IBaseView{

        void showHotSearchKeywordsSucceed(List<HotKey> keys);
        void showHotSearchKeywordsFailed(String errorMs);

        void showHistorySearchKeywordsSucceed(List<HistoryKeyword> keys);
        void showHistorySearchKeywordsFailed(String errorMsg);

        void showAddHistorySearchKeywordSucceed(List<HistoryKeyword> keys);
        void showAddHistorySearchKeywordFailed(String errorMsg);

        void showDeleteHistorySearchKeywordSucceed(int position);
        void showDeleteHistorySearchKeywordFailed(String errorMsg);

        void showClearHistorySearchKeywordsSucceed();
        void showClearHistorySearchKeywordsFailed(String errorMsg);
    }
}
