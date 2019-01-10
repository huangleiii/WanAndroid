package com.huanglei.wanandroid.model.bean;

/**
 * Created by 黄垒 on 2019/1/10.
 */

public class NavigationTab {
    private int cid;
    private String name;
    private boolean isChecked;

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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
