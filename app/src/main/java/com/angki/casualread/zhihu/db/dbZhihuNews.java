package com.angki.casualread.zhihu.db;

import org.litepal.crud.DataSupport;

/**
 * Created by tengyu on 2017/4/10.
 */

public class dbZhihuNews extends DataSupport{

    private boolean db_zn_collection;//文章是否收藏

    private String db_zn_id;//文章id

    private String db_zn_title;//文章标题

    private String db_zn_image;//文章配图

    private dbZhihuNewsDate date;//与日期的表联系

    private int dbzhihunewsdate_id;//文章对应的日期

    private int listSorting;//日期内的所有文章的序号

    public boolean isDb_zn_collection() {
        return db_zn_collection;
    }

    public void setDb_zn_collection(boolean db_zn_collection) {
        this.db_zn_collection = db_zn_collection;
    }

    public int getListSorting() {
        return listSorting;
    }

    public void setListSorting(int listSorting) {
        this.listSorting = listSorting;
    }

    public int getDbzhihunewsdate_id() {
        return dbzhihunewsdate_id;
    }

    public void setDbzhihunewsdate_id(int dbzhihunewsdate_id) {
        this.dbzhihunewsdate_id = dbzhihunewsdate_id;
    }

    public String getDb_zn_id() {
        return db_zn_id;
    }

    public void setDb_zn_id(String db_zn_id) {
        this.db_zn_id = db_zn_id;
    }

    public String getDb_zn_title() {
        return db_zn_title;
    }

    public void setDb_zn_title(String db_zn_title) {
        this.db_zn_title = db_zn_title;
    }

    public String getDb_zn_image() {
        return db_zn_image;
    }

    public void setDb_zn_image(String db_zn_image) {
        this.db_zn_image = db_zn_image;
    }

    public dbZhihuNewsDate getDate() {
        return date;
    }

    public void setDate(dbZhihuNewsDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "dbZhihuNews{" +
                "db_zn_id='" + db_zn_id + '\'' +
                ", db_zn_title='" + db_zn_title + '\'' +
                ", db_zn_image='" + db_zn_image + '\'' +
                ", date=" + date +
                ", dbzhihunewsdate_id=" + dbzhihunewsdate_id +
                ", listSorting=" + listSorting +
                '}';
    }
}
