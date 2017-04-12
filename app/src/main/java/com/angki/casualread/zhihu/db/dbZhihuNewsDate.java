package com.angki.casualread.zhihu.db;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tengyu on 2017/4/11.
 */

public class dbZhihuNewsDate extends DataSupport{

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String db_znd_date;

    private List<dbZhihuNews> dbZhihuNewsList = new ArrayList<>();

    private int dbnewscount;

    public int getDbnewscount() {
        return dbnewscount;
    }

    public void setDbnewscount(int dbnewscount) {
        this.dbnewscount = dbnewscount;
    }

    public String getDb_znd_date() {
        return db_znd_date;
    }

    public void setDb_znd_date(String db_znd_date) {
        this.db_znd_date = db_znd_date;
    }

    public List<dbZhihuNews> getDbZhihuNewsList() {
        return dbZhihuNewsList;
    }

    public void setDbZhihuNewsList(List<dbZhihuNews> dbZhihuNewsList) {
        this.dbZhihuNewsList = dbZhihuNewsList;
    }

    @Override
    public String toString() {
        return "dbZhihuNewsDate{" +
                "db_znd_date='" + db_znd_date + '\'' +
                ", dbZhihuNewsList=" + dbZhihuNewsList +
                '}';
    }
}
