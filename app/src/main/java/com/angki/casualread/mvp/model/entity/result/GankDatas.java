package com.angki.casualread.mvp.model.entity.result;

import java.util.List;

/**
 * Created by tengyu on 2017/3/20.
 */

public class GankDatas {

    private String error;

    private List<GankData> results;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<GankData> getResults() {
        return results;
    }

    public void setResults(List<GankData> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "GankDatas{" +
                "error='" + error + '\'' +
                ", results=" + results +
                '}';
    }
}
