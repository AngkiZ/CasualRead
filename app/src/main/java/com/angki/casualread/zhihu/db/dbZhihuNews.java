package com.angki.casualread.zhihu.db;

import org.litepal.crud.DataSupport;

/**
 * Created by tengyu on 2017/4/10.
 */

public class dbZhihuNews extends DataSupport{

    private String db_zn_id;

    private String db_zn_title;

    private String db_zn_image;

    private dbZhihuNewsDate date;

    private int dbzhihunewsdate_id;

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
                '}';
    }
}
