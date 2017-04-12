package com.angki.casualread.util;

import android.util.Log;

import com.angki.casualread.zhihu.db.dbZhihuNews;
import com.angki.casualread.zhihu.db.dbZhihuNewsDate;
import com.angki.casualread.zhihu.gson.ZhihuDailyNews.NewsBeans;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by tengyu on 2017/4/12.
 * 数据库操作类
 */

public class dbUtil {

    private static final String TAG = "dbUtil";
    private dbZhihuNews mdbZhihuNews;

    private dbZhihuNewsDate mdbZhihuNewsDate;

    /**
     * 存储知乎的数据
     * @param newsBeans
     */
    public void dbzhihuSave(NewsBeans newsBeans, boolean b) {
        //查询指定日期
        List<dbZhihuNewsDate> date = DataSupport.select("db_znd_date").where("db_znd_date like ?", "%" + newsBeans.getDate() + "%").find(dbZhihuNewsDate.class);
        Log.d(TAG, "db_znd_date: " + date.toString());
        Log.d(TAG, "db_znd_date: " + date.size());
        mdbZhihuNewsDate = new dbZhihuNewsDate();
        //判断该日期是否存在，存在就更新数据，不存在则添加数据
        if (date.size() == 0) {
            Log.d(TAG, "db_znd_date: true");
            mdbZhihuNewsDate.setDb_znd_date(newsBeans.getDate());
            for (int i = 0; i < newsBeans.getStories().size(); i++) {
                List<dbZhihuNews> id = DataSupport.select("db_zn_id").where("db_zn_id like ?","%" + newsBeans.getStories().get(i).getId() + "%").find(dbZhihuNews.class);
                mdbZhihuNews = new dbZhihuNews();
                //判断该id是否存在，存在就跳过，不存在则添加数据
                if ( id.size() == 0) {
                    mdbZhihuNews.setDb_zn_id(newsBeans.getStories().get(i).getId());
                    mdbZhihuNews.setDb_zn_title(newsBeans.getStories().get(i).getTitle());
                    mdbZhihuNews.setDb_zn_image(newsBeans.getStories().get(i).getImages().get(0));
                    mdbZhihuNews.save();
                    mdbZhihuNewsDate.getDbZhihuNewsList().add(mdbZhihuNews);
                }
            }
            if (b) {
                mdbZhihuNewsDate.setDbnewscount(mdbZhihuNewsDate.getDbZhihuNewsList().size());
                mdbZhihuNewsDate.setId(1);
                mdbZhihuNewsDate.save();
            }else {
                mdbZhihuNewsDate.setDbnewscount(mdbZhihuNewsDate.getDbZhihuNewsList().size());
                mdbZhihuNewsDate.save();
            }
        }else {
            Log.d(TAG, "db_znd_date: flase");
            int count = 0;
            for (int i = 0; i < newsBeans.getStories().size(); i++) {
                List<dbZhihuNews> id = DataSupport.select("db_zn_id").where("db_zn_id like ?","%" + newsBeans.getStories().get(i).getId() + "%").find(dbZhihuNews.class);
                mdbZhihuNews = new dbZhihuNews();
                if (id.size() == 0) {
                    Log.d(TAG, "updata: add");
                    mdbZhihuNews.setDb_zn_id(newsBeans.getStories().get(i).getId());
                    mdbZhihuNews.setDb_zn_title(newsBeans.getStories().get(i).getTitle());
                    mdbZhihuNews.setDb_zn_image(newsBeans.getStories().get(i).getImages().get(0));
                    mdbZhihuNews.setDbzhihunewsdate_id(1);
                    mdbZhihuNews.save();
                    mdbZhihuNewsDate.getDbZhihuNewsList().add(mdbZhihuNews);
                }
                count ++;
            }
            mdbZhihuNewsDate.setDbnewscount(count);
            Log.d(TAG, "Dbnewscount: " + mdbZhihuNewsDate.getDbZhihuNewsList().size());
            //更新数据
            mdbZhihuNewsDate.updateAll("db_znd_date = ?", newsBeans.getDate());
        }
    }
}
