package com.huanglei.wanandroid.model.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by 黄垒 on 2019/1/3.
 */
@Entity
public class HistoryKeyword {
    @Id(autoincrement = true)
    private long id;
    private String data;
    private long date;
    @Generated(hash = 953949518)
    public HistoryKeyword(long id, String data, long date) {
        this.id = id;
        this.data = data;
        this.date = date;
    }
    @Generated(hash = 32903645)
    public HistoryKeyword() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getData() {
        return this.data;
    }
    public void setData(String data) {
        this.data = data;
    }
    public long getDate() {
        return this.date;
    }
    public void setDate(long date) {
        this.date = date;
    }
}
