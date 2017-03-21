package com.angki.casualread.gank.gson;

import java.util.List;

/**
 * Created by tengyu on 2017/3/21.
 */

public class GankWelfareDatas {


    private String error;

    private List<GankWelfareData> results;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<GankWelfareData> getResults() {
        return results;
    }

    public void setResults(List<GankWelfareData> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "GankWelfareDatas{" +
                "error='" + error + '\'' +
                ", results=" + results +
                '}';
    }
}
