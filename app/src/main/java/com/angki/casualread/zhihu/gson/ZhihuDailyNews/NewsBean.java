package com.angki.casualread.zhihu.gson.ZhihuDailyNews;

import java.util.List;

/**
 * Created by tengyu on 2017/3/16.
 *
 * 知乎日报每天推荐JSON数据类每日新闻类
 */

public class NewsBean {
    private int type = 0;
    private String id;
    private String ga_prefix;
    private String title;
    private List<String> images;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "NewsBean{" +
                "type=" + type +
                ", id='" + id + '\'' +
                ", ga_prefix='" + ga_prefix + '\'' +
                ", title='" + title + '\'' +
                ", images=" + images +
                '}';
    }
}
