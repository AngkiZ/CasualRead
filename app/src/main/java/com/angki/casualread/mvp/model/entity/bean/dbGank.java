package com.angki.casualread.mvp.model.entity.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by tengyu on 2017/4/13.
 */

public class dbGank extends DataSupport{

    private String db_gank_id;//干货id

    private String db_gank_title;//干货标题

    private String db_gank_image;//干货配图

    private String db_gank_url;//干货地址

    private String db_gank_who;//干货提供者

    private int db_gank_page;

    private int db_gank_listSorting;

    private boolean db_gank_collection;//是否收藏

    public String getDb_gank_id() {
        return db_gank_id;
    }

    public void setDb_gank_id(String db_gank_id) {
        this.db_gank_id = db_gank_id;
    }

    public String getDb_gank_title() {
        return db_gank_title;
    }

    public void setDb_gank_title(String db_gank_title) {
        this.db_gank_title = db_gank_title;
    }

    public String getDb_gank_image() {
        return db_gank_image;
    }

    public void setDb_gank_image(String db_gank_image) {
        this.db_gank_image = db_gank_image;
    }

    public String getDb_gank_url() {
        return db_gank_url;
    }

    public void setDb_gank_url(String db_gank_url) {
        this.db_gank_url = db_gank_url;
    }

    public String getDb_gank_who() {
        return db_gank_who;
    }

    public void setDb_gank_who(String db_gank_who) {
        this.db_gank_who = db_gank_who;
    }

    public int getDb_gank_page() {
        return db_gank_page;
    }

    public void setDb_gank_page(int db_gank_page) {
        this.db_gank_page = db_gank_page;
    }

    public int getDb_gank_listSorting() {
        return db_gank_listSorting;
    }

    public void setDb_gank_listSorting(int db_gank_listSorting) {
        this.db_gank_listSorting = db_gank_listSorting;
    }

    public boolean isDb_gank_collection() {
        return db_gank_collection;
    }

    public void setDb_gank_collection(boolean db_gank_collection) {
        this.db_gank_collection = db_gank_collection;
    }

    @Override
    public String toString() {
        return "dbGankList{" +
                "db_gank_id='" + db_gank_id + '\'' +
                ", db_gank_title='" + db_gank_title + '\'' +
                ", db_gank_image='" + db_gank_image + '\'' +
                ", db_gank_url='" + db_gank_url + '\'' +
                ", db_gank_who='" + db_gank_who + '\'' +
                ", db_gank_page=" + db_gank_page +
                ", db_gank_listSorting=" + db_gank_listSorting +
                '}';
    }
}
