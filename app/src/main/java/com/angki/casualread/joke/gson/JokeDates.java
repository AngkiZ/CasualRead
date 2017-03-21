package com.angki.casualread.joke.gson;

import java.util.List;

/**
 * Created by tengyu on 2017/3/21.
 */

public class JokeDates {

    private String reason;

    private List<JokeDate> result;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<JokeDate> getResult() {
        return result;
    }

    public void setResult(List<JokeDate> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "JokeDates{" +
                "reason='" + reason + '\'' +
                ", result=" + result +
                '}';
    }
}
