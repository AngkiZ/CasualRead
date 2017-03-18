package com.angki.casualread.zhihu.gson.ZhihuDailyNews;

import java.util.List;

/**
 * Created by tengyu on 2017/3/16.
 *
 * 知乎日报每天推荐JSON数据类（包含每日新闻和热门新闻）
 */

public class StoriesBeans {
    private String date;
    private List<StoriesBean> stories;
    private List<TopStoriesBean> topStories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<StoriesBean> getStories() {
        return stories;
    }

    public void setStories(List<StoriesBean> stories) {
        this.stories = stories;
    }

    public List<TopStoriesBean> getTopStories() {
        return topStories;
    }

    public void setTopStories(List<TopStoriesBean> topStories) {
        this.topStories = topStories;
    }

    @Override
    public String toString() {
        return "StoriesBeans{" +
                "date='" + date + '\'' +
                ", stories=" + stories +
                ", topStories=" + topStories +
                '}';
    }
}
