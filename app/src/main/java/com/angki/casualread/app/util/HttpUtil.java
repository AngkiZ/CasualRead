package com.angki.casualread.app.util;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by tengyu on 2017/3/14.
 * 使用OKHttp与服务器进行交互
 */

public class HttpUtil {

    public static void sendOkHttpRequest(String address, okhttp3.Callback callback){

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)//超时时间
                .build();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
