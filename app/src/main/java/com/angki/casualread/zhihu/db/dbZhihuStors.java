package com.angki.casualread.zhihu.db;

import org.litepal.crud.DataSupport;

/**
 * Created by tengyu on 2017/4/10.
 */

public class dbZhihuStors extends DataSupport{

    private String db_zs_id;

    private String db_zs_body;

    private String db_zs_title;

    private String db_zs_image;

    public String getDb_zs_id() {
        return db_zs_id;
    }

    public void setDb_zs_id(String db_zs_id) {
        this.db_zs_id = db_zs_id;
    }

    public String getDb_zs_body() {
        return db_zs_body;
    }

    public void setDb_zs_body(String db_zs_body) {
        this.db_zs_body = db_zs_body;
    }

    public String getDb_zs_title() {
        return db_zs_title;
    }

    public void setDb_zs_title(String db_zs_title) {
        this.db_zs_title = db_zs_title;
    }

    public String getDb_zs_image() {
        return db_zs_image;
    }

    public void setDb_zs_image(String db_zs_image) {
        this.db_zs_image = db_zs_image;
    }


    @Override
    public String toString() {
        return "dbZhihuStors{" +
                "db_zs_id='" + db_zs_id + '\'' +
                ", db_zs_body='" + db_zs_body + '\'' +
                ", db_zs_title='" + db_zs_title + '\'' +
                ", db_zs_image='" + db_zs_image + '\'' +
                '}';
    }
}
