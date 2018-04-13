package com.angki.casualread.mvp.model.entity.result;

/**
 * Created by tengyu on 2017/3/24.
 */

public class JokeData {

    private String content;

    private String unixtime;

    private String hashId;

    private String updatetime;

    public JokeData(String content, String unixtime, String hashId, String updatetime) {
        this.content = content;
        this.unixtime = unixtime;
        this.hashId = hashId;
        this.updatetime = updatetime;
    }

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

    public String getHashId() {
        return hashId;
    }

    public void setHashId(String hashId) {
        this.hashId = hashId;
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
                ", hashId='" + hashId + '\'' +
                ", updatetime='" + updatetime + '\'' +
                '}';
    }
}
