package com.huanglei.wanandroid.model.bean;

import java.util.List;

/**
 * Created by 黄垒 on 2019/1/7.
 */

public class ArticleListInCollectPage {
    private int curPage;
    private List<ArticleInCollectPage> datas;
    private  int offset;
    private boolean over;
    private int pageCount;
    private int size;
    private int total;

    public List<ArticleInCollectPage> getDatas() {
        return datas;
    }

    public void setDatas(List<ArticleInCollectPage> datas) {
        this.datas = datas;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCurPage() {

        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }
}
