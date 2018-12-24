package com.huanglei.wanandroid.model.bean;

import java.util.List;

/**
 * Created by HuangLei on 2018/11/26.
 */

public class Account {
    private int id;
    private String username;
    private String password;
    private String icon;
    private int type;
    private String token;
    private String email;
    private List<Integer> collectIds;
    private List<Integer> chapterTops;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Integer> getChapterTops() {
        return chapterTops;
    }

    public void setChapterTops(List<Integer> chapterTops) {
        this.chapterTops = chapterTops;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Integer> getCollectIds() {
        return collectIds;
    }

    public void setCollectIds(List<Integer> collectIds) {
        this.collectIds = collectIds;
    }
}
