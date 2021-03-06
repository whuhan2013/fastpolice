package com.zj.showgirl.entity;


import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.zj.showgirl.db.AppDatabase;

/**
 * Created by Administrator on 2016/3/20.
 */
@Table(database = AppDatabase.class)
public class Girl extends BaseModel implements Comparable<Girl>{

    @PrimaryKey
    private String desc;

    @PrimaryKey
    private String url;

    @PrimaryKey
    private String publishedAt;

    public String getDesc() {
        return desc;
    }

    public String getUrl() {
        return url;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    @Override
    public int compareTo(Girl another) {
        String anotherPublishedAt = another.getPublishedAt();
        Integer yearMonthDay = Integer.valueOf(publishedAt.substring(0,4)+publishedAt.substring(5,7)+publishedAt.substring(8,10));
        Integer anotherYearMonthDay = Integer.valueOf(anotherPublishedAt.substring(0,4)+anotherPublishedAt.substring(5,7)+anotherPublishedAt.substring(8,10));
        return anotherYearMonthDay.compareTo(yearMonthDay);
    }
}
