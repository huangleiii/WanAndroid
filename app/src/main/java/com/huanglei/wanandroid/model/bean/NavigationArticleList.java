package com.huanglei.wanandroid.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 黄垒 on 2019/1/10.
 */

public class NavigationArticleList{
    private int cid;
    private String name;
    private List<Article> articles;

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

}

