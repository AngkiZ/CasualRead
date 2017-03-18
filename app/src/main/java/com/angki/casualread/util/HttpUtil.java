package com.angki.casualread.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by tengyu on 2017/3/14.
 * 使用OKHttp与服务器进行交互
 */

public class HttpUtil {

    public static void sendOkHttpRequest(String address, okhttp3.Callback callback){

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
