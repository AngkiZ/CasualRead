package com.angki.casualread.zhihu.gson.ZhihuDailyNews;

/**
 * Created by tengyu on 2017/3/16.
 *
 * 知乎日报每天推荐JSON数据类中热门新闻类
 */

public class TopStoriesBean {
    private int type = 0;
    private String id;
    private String ga_prefix;
    private String title;
    private String image;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "TopStoriesBean{" +
                "type=" + type +
                ", id='" + id + '\'' +
                ", ga_prefix='" + ga_prefix + '\'' +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
