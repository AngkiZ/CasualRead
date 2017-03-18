package com.angki.casualread.zhihu.gson;

import java.util.List;

/**
 * Created by tengyu on 2017/3/14.
 */

public class ZhihuDailyStory {

    private String body;

    private String title;

    private String image;

    private List<String> css;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getCss() {
        return css;
    }

    public void setCss(List<String> css) {
        this.css = css;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
