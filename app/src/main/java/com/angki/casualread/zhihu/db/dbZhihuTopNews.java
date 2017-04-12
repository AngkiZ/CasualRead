package com.angki.casualread.zhihu.db;

import org.litepal.crud.DataSupport;

/**
 * Created by tengyu on 2017/4/10.
 */

public class dbZhihuTopNews extends DataSupport{

    private String db_ztn_id;

    private String db_ztn_title;

    private String db_ztn_image;

    public String getDb_ztn_id() {
        return db_ztn_id;
    }

    public void setDb_ztn_id(String db_ztn_id) {
        this.db_ztn_id = db_ztn_id;
    }

    public String getDb_ztn_image() {
        return db_ztn_image;
    }

    public void setDb_ztn_image(String db_ztn_image) {
        this.db_ztn_image = db_ztn_image;
    }

    public String getDb_ztn_title() {
        return db_ztn_title;
    }

    public void setDb_ztn_title(String db_ztn_title) {
        this.db_ztn_title = db_ztn_title;
    }

    @Override
    public String toString() {
        return "dbZhihuTopNews{" +
                "db_ztn_id='" + db_ztn_id + '\'' +
                ", db_ztn_title='" + db_ztn_title + '\'' +
                ", db_ztn_image='" + db_ztn_image + '\'' +
                '}';
    }
}
