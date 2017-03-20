package com.angki.casualread.zhihu.gson.ZhihuDailyStory;

import java.util.List;

/**
 * Created by tengyu on 2017/3/14.
 */

public class ZhihuDailyStory {

    private String body;

    private String title;

    private String image;

    private String image_source;

    private String share_url;

    private int id;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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

    public String getImage_source() {
        return image_source;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ZhihuDailyStory{" +
                "body='" + body + '\'' +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", image_source='" + image_source + '\'' +
                ", share_url='" + share_url + '\'' +
                ", id=" + id +
                '}';
    }
}
