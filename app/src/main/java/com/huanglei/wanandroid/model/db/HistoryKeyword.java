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
    private Long id;//注意为Long型而非long，赋值为null可自动增加
    private String data;
    private Long date;
    @Generated(hash = 1593705847)
    public HistoryKeyword(Long id, String data, Long date) {
        this.id = id;
        this.data = data;
        this.date = date;
    }
    @Generated(hash = 32903645)
    public HistoryKeyword() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getData() {
        return this.data;
    }
    public void setData(String data) {
        this.data = data;
    }
    public Long getDate() {
        return this.date;
    }
    public void setDate(Long date) {
        this.date = date;
    }
}
