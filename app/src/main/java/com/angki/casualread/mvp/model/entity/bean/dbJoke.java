package com.angki.casualread.mvp.model.entity.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by tengyu on 2017/4/14.
 */

public class dbJoke extends DataSupport{

    private String db_joke_id;//id

    private String db_joke_content;//内容

    private int db_joke_page;//页数

    private int db_joke_listSorting;//页内顺序

    private boolean db_joke_collection;//是否收藏

    public String getDb_joke_id() {
        return db_joke_id;
    }

    public void setDb_joke_id(String db_joke_id) {
        this.db_joke_id = db_joke_id;
    }

    public String getDb_joke_content() {
        return db_joke_content;
    }

    public void setDb_joke_content(String db_joke_content) {
        this.db_joke_content = db_joke_content;
    }

    public int getDb_joke_page() {
        return db_joke_page;
    }

    public void setDb_joke_page(int db_joke_page) {
        this.db_joke_page = db_joke_page;
    }

    public boolean getDb_joke_collection() {
        return db_joke_collection;
    }

    public void setDb_joke_collection(boolean db_joke_collection) {
        this.db_joke_collection = db_joke_collection;
    }

    public int getDb_joke_listSorting() {
        return db_joke_listSorting;
    }

    public void setDb_joke_listSorting(int db_joke_listSorting) {
        this.db_joke_listSorting = db_joke_listSorting;
    }

    @Override
    public String toString() {
        return "dbJoke{" +
                "db_joke_id='" + db_joke_id + '\'' +
                ", db_joke_content='" + db_joke_content + '\'' +
                ", db_joke_page='" + db_joke_page + '\'' +
                ", db_joke_listSorting='" + db_joke_listSorting + '\'' +
                ", db_joke_collection='" + db_joke_collection + '\'' +
                '}';
    }
}
