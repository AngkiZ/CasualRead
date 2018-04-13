package com.angki.casualread.mvp.model.entity.result;

import java.util.List;

/**
 * Created by tengyu on 2017/3/20.
 */

public class GankData {

    private String _id;//文章id

    private String desc;//标题

    private List<String> images;//配图

    private String type;//类型

    private String url;//网址

    private String who;//作者

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    @Override
    public String toString() {
        return "GankData{" +
                "desc='" + desc + '\'' +
                ", images=" + images +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", who='" + who + '\'' +
                '}';
    }
}
