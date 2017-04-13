package com.angki.casualread.gank.db;

import org.litepal.crud.DataSupport;

/**
 * Created by tengyu on 2017/4/13.
 */

public class dbWelfare extends DataSupport{

    private String db_we_id;//图片id

    private String db_we_title;//图片的标题

    private String db_we_url;//图片来源

    private int db_we_pager;//请求的页数

    private int db_we_listSorting;//一页中的排序

    public String getDb_we_id() {
        return db_we_id;
    }

    public void setDb_we_id(String db_we_id) {
        this.db_we_id = db_we_id;
    }

    public String getDb_we_title() {
        return db_we_title;
    }

    public void setDb_we_title(String db_we_title) {
        this.db_we_title = db_we_title;
    }

    public String getDb_we_url() {
        return db_we_url;
    }

    public void setDb_we_url(String db_we_url) {
        this.db_we_url = db_we_url;
    }

    public int getDb_we_pager() {
        return db_we_pager;
    }

    public void setDb_we_pager(int db_we_pager) {
        this.db_we_pager = db_we_pager;
    }

    public int getDb_we_listSorting() {
        return db_we_listSorting;
    }

    public void setDb_we_listSorting(int db_we_listSorting) {
        this.db_we_listSorting = db_we_listSorting;
    }

    @Override
    public String toString() {
        return "dbWelfare{" +
                "db_we_id='" + db_we_id + '\'' +
                ", db_we_title='" + db_we_title + '\'' +
                ", db_we_url='" + db_we_url + '\'' +
                ", db_we_pager=" + db_we_pager +
                ", db_we_listSorting=" + db_we_listSorting +
                '}';
    }
}
