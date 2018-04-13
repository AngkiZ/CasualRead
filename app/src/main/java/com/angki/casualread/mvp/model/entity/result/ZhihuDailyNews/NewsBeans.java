package com.angki.casualread.mvp.model.entity.result.ZhihuDailyNews;

import java.util.List;

/**
 * Created by tengyu on 2017/3/16.
 *
 * 知乎日报每天推荐JSON数据类（包含每日新闻和热门新闻）
 */

public class NewsBeans {
    private String date;
    private List<NewsBean> stories;
    private List<TopNewsBean> topStories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<NewsBean> getStories() {
        return stories;
    }

    public void setStories(List<NewsBean> stories) {
        this.stories = stories;
    }

    public List<TopNewsBean> getTopStories() {
        return topStories;
    }

    public void setTopStories(List<TopNewsBean> topStories) {
        this.topStories = topStories;
    }

    @Override
    public String toString() {
        return "NewsBeans{" +
                "date='" + date + '\'' +
                ", stories=" + stories +
                ", topStories=" + topStories +
                '}';
    }
}
