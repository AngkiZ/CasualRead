package com.angki.casualread.app.util;

import android.util.Log;

import com.angki.casualread.mvp.model.entity.bean.dbGank;
import com.angki.casualread.mvp.model.entity.bean.dbWelfare;
import com.angki.casualread.mvp.model.entity.result.GankDatas;
import com.angki.casualread.mvp.model.entity.result.GankWelfareDatas;
import com.angki.casualread.mvp.model.entity.bean.dbJoke;
import com.angki.casualread.mvp.model.entity.result.JokeData;
import com.angki.casualread.mvp.model.entity.bean.dbZhihuNews;
import com.angki.casualread.mvp.model.entity.bean.dbZhihuNewsDate;
import com.angki.casualread.mvp.model.entity.bean.dbZhihuStors;
import com.angki.casualread.mvp.model.entity.result.ZhihuDailyNews.NewsBeans;
import com.angki.casualread.mvp.model.entity.result.ZhihuDailyStory.StoryBean;

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

    private dbZhihuStors mdbZhihuStors;

    private dbGank mdbGank;

    private dbWelfare mdbWelfare;

    private dbJoke mdbJoke;

    /**
     * 存储知乎的数据
     * @param newsBeans
     */
    public void dbzhihuSave(NewsBeans newsBeans) {
        //查询指定日期
        List<dbZhihuNewsDate> date = DataSupport.select("db_znd_date").where("db_znd_date like ?", "%" + newsBeans.getDate() + "%").find(dbZhihuNewsDate.class);
        mdbZhihuNewsDate = new dbZhihuNewsDate();
        //判断该日期是否存在，存在就更新数据，不存在则添加数据
        if (date.size() == 0) {
            Log.d(TAG, "db_znd_date: true");
            mdbZhihuNewsDate.setDb_znd_date(newsBeans.getDate());
            int m = -1;//标记已有新闻
            for (int i = 0; i < newsBeans.getStories().size(); i++) {
                List<dbZhihuNews> new_id = DataSupport.select("db_zn_id").where("db_zn_id like ?","%" + newsBeans.getStories().get(i).getId() + "%").find(dbZhihuNews.class);
                mdbZhihuNews = new dbZhihuNews();
                //判断该id是否存在，存在就更新数据，不存在则添加数据
                if ( new_id.size() == 0) {
                    mdbZhihuNews.setDb_zn_collection(false);
                    mdbZhihuNews.setDb_zn_id(newsBeans.getStories().get(i).getId());
                    mdbZhihuNews.setDb_zn_title(newsBeans.getStories().get(i).getTitle());
                    mdbZhihuNews.setDb_zn_image(newsBeans.getStories().get(i).getImages().get(0));
                    mdbZhihuNews.setListSorting(i);
                    mdbZhihuNews.save();
                    mdbZhihuNewsDate.getDbZhihuNewsList().add(mdbZhihuNews);
                } else {
                    m = i;
                    mdbZhihuNews.setListSorting(i);
                    mdbZhihuNews.updateAll("db_zn_id = ?", new_id.get(0).getDb_zn_id());
                }
            }
            mdbZhihuNewsDate.setDbnewscount(mdbZhihuNewsDate.getDbZhihuNewsList().size());
            mdbZhihuNewsDate.save();
            //把已有新闻添加到新队列
            if (m != -1) {
                List<dbZhihuNewsDate> date1 = DataSupport.select("db_znd_date").where("db_znd_date like ?", "%" + newsBeans.getDate() + "%").find(dbZhihuNewsDate.class);
                mdbZhihuNews = new dbZhihuNews();
                mdbZhihuNews.setDbzhihunewsdate_id(date1.get(0).getId());
                mdbZhihuNews.updateAll("db_zn_id = ?", newsBeans.getStories().get(m).getId());
            }
        }else {
            Log.d(TAG, "db_znd_date: flase");
            int count = 0;
            for (int i = 0; i < newsBeans.getStories().size(); i++) {
                List<dbZhihuNews> new_id = DataSupport.select("db_zn_id").where("db_zn_id like ?","%" + newsBeans.getStories().get(i).getId() + "%").find(dbZhihuNews.class);
                mdbZhihuNews = new dbZhihuNews();
                if (new_id.size() == 0) {
                    Log.d(TAG, "updata: add");
                    mdbZhihuNews.setDb_zn_collection(false);
                    mdbZhihuNews.setDb_zn_id(newsBeans.getStories().get(i).getId());
                    mdbZhihuNews.setDb_zn_title(newsBeans.getStories().get(i).getTitle());
                    mdbZhihuNews.setDb_zn_image(newsBeans.getStories().get(i).getImages().get(0));
                    mdbZhihuNews.setDbzhihunewsdate_id(date.get(0).getId());
                    mdbZhihuNews.setListSorting(i);
                    mdbZhihuNews.save();
                    mdbZhihuNewsDate.getDbZhihuNewsList().add(mdbZhihuNews);
                }else {
                    mdbZhihuNews.setListSorting(i);
                    mdbZhihuNews.updateAll("db_zn_id = ?", new_id.get(0).getDb_zn_id());
                }
                count ++;
            }
            mdbZhihuNewsDate.setDbnewscount(count);
            Log.d(TAG, "Dbnewscount: " + mdbZhihuNewsDate.getDbZhihuNewsList().size());
            //更新数据
            mdbZhihuNewsDate.updateAll("db_znd_date = ?", newsBeans.getDate());
        }
    }

    /**
     * 存储知乎新闻的内容
     * @param storyBean
     * @return
     */
    public dbZhihuStors dbzhihustorsSave(StoryBean storyBean) {
        List<dbZhihuNews> news = DataSupport.select("db_zn_id")
                .where("db_zn_id like ?","%" + storyBean.getId() + "%").find(dbZhihuNews.class);
        //判断是否dbZhihuNews表中是否有该条新闻，没有的话便添加
        if (news.size() == 0) {
            mdbZhihuNews = new dbZhihuNews();
            mdbZhihuNews.setDb_zn_collection(false);
            mdbZhihuNews.setDb_zn_id(storyBean.getId());
            mdbZhihuNews.setDb_zn_title(storyBean.getTitle());
            mdbZhihuNews.setDb_zn_image(storyBean.getImages().get(0));
            mdbZhihuNews.save();
        }
        List<dbZhihuStors> news_id = DataSupport
                .where("db_zs_id like ?", "%" + storyBean.getId() + "%")
                .find(dbZhihuStors.class);
        if (news_id.size() == 0 ){
            mdbZhihuStors = new dbZhihuStors();
            mdbZhihuStors.setDb_zs_body(storyBean.getBody());
            mdbZhihuStors.setDb_zs_id(storyBean.getId());
            mdbZhihuStors.setDb_zs_title(storyBean.getTitle());
            mdbZhihuStors.setDb_zs_image(storyBean.getImage());
            mdbZhihuStors.save();

            return mdbZhihuStors;
        }else {

            return news_id.get(0);
        }
    }

    /**
     * 存储干货的内容
     * @param gankDatas
     * @param pager
     */
    public void dbgankSave(GankDatas gankDatas, int pager) {

        for (int i = 0; i < gankDatas.getResults().size(); i++) {

            List<dbGank> content = DataSupport.select("db_gank_id")
                    .where("db_gank_id like ?", "%" + gankDatas.getResults().get(i).get_id() + "%")
                    .find(dbGank.class);
            mdbGank = new dbGank();
            if (content.size() == 0) {
                mdbGank.setDb_gank_id(gankDatas.getResults().get(i).get_id());
                mdbGank.setDb_gank_title(gankDatas.getResults().get(i).getDesc());
                if (gankDatas.getResults().get(i).getImages() != null) {
                    mdbGank.setDb_gank_image(gankDatas.getResults().get(i).getImages().get(0));
                } else {
                    mdbGank.setDb_gank_image("null");
                }
                mdbGank.setDb_gank_who(gankDatas.getResults().get(i).getWho());
                mdbGank.setDb_gank_url(gankDatas.getResults().get(i).getUrl());
                mdbGank.setDb_gank_page(pager);
                mdbGank.setDb_gank_listSorting(i);
                mdbGank.setDb_gank_collection(false);
                mdbGank.save();
            }else {
                mdbGank.setDb_gank_page(pager);
                mdbGank.setDb_gank_listSorting(i);
                mdbGank.updateAll("db_gank_id = ?", content.get(0).getDb_gank_id());
            }
        }
    }

    /**
     * 存储福利的内容
     * @param gankWelfareDatas
     * @param pager
     */
    public void dbwelfareSave(GankWelfareDatas gankWelfareDatas, int pager) {

        for (int i = 0; i < gankWelfareDatas.getResults().size(); i++) {
            List<dbWelfare> content = DataSupport.select("db_we_id")
                    .where("db_we_id like ?", "%" + gankWelfareDatas.getResults().get(i).get_id() + "%")
                    .find(dbWelfare.class);
            mdbWelfare = new dbWelfare();
            if (content.size() == 0) {
                mdbWelfare.setDb_we_id(gankWelfareDatas.getResults().get(i).get_id());
                mdbWelfare.setDb_we_title(gankWelfareDatas.getResults().get(i).getDesc());
                mdbWelfare.setDb_we_url(gankWelfareDatas.getResults().get(i).getUrl());
                mdbWelfare.setDb_we_pager(pager);
                mdbWelfare.setDb_we_listSorting(i);
                mdbWelfare.save();
            }else {
                mdbWelfare.setDb_we_pager(pager);
                mdbWelfare.setDb_we_listSorting(i);
                mdbWelfare.updateAll("db_we_id = ?", content.get(0).getDb_we_id());
            }
        }
    }

    public void dbjokeSave(List<JokeData> jokeData, int page) {

        for (int i = 0; i < jokeData.size(); i++) {
            List<dbJoke> content = DataSupport.select("db_joke_id")
                    .where("db_joke_id like ?", "%" + jokeData.get(i).getHashId() + "%")
                    .find(dbJoke.class);
            mdbJoke = new dbJoke();
            if (content.size() == 0) {
                mdbJoke.setDb_joke_id(jokeData.get(i).getHashId());
                mdbJoke.setDb_joke_content(jokeData.get(i).getContent());
                mdbJoke.setDb_joke_listSorting(i);
                mdbJoke.setDb_joke_page(page);
                mdbJoke.setDb_joke_collection(false);
                mdbJoke.save();
            }else {
                mdbJoke.setDb_joke_listSorting(i);
                mdbJoke.setDb_joke_page(page);
                mdbJoke.updateAll("db_joke_id = ?", content.get(0).getDb_joke_id());
            }
        }
    }
}