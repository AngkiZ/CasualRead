package com.angki.casualread.joke.gson;

/**
 * Created by tengyu on 2017/3/23.
 */

public class Data {

    private String content;

    private String unixtime;

    private String updatetime;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUnixtime() {
        return unixtime;
    }

    public void setUnixtime(String unixtime) {
        this.unixtime = unixtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    @Override
    public String toString() {
        return "JokeData{" +
                "content='" + content + '\'' +
                ", unixtime='" + unixtime + '\'' +
                ", updatetime='" + updatetime + '\'' +
                '}';
    }
}
