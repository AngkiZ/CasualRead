package com.angki.casualread.gank.gson;

import java.util.List;

/**
 * Created by tengyu on 2017/3/20.
 */

public class GankDates {

    private String error;

    private List<GankDate> results;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<GankDate> getResults() {
        return results;
    }

    public void setResults(List<GankDate> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "GankDates{" +
                "error='" + error + '\'' +
                ", results=" + results +
                '}';
    }
}
