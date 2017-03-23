package com.angki.casualread.joke.gson;

import java.util.List;

/**
 * Created by tengyu on 2017/3/21.
 */

public class JokeDatas {

    private String reason;

    private JokeData result;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public JokeData getResult() {
        return result;
    }

    public void setResult(JokeData result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "JokeDatas{" +
                "reason='" + reason + '\'' +
                ", result=" + result +
                '}';
    }
}
