package com.angki.casualread.app.httpHandler;

import android.content.Context;

import com.jess.arms.http.GlobalHttpHandler;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 全局处理 Http 请求和响应结果的处理类,可以比客户端提前一步拿到服务器返回的结果
 * @author ：Administrator on 2018/4/4 11:48
 * @eamil ：503001231@qq.com
 */

public class GlobalHttpHandlerImpl implements GlobalHttpHandler {

    private Context context;

    public GlobalHttpHandlerImpl(Context context) {
        this.context = context;
    }

    @Override
    public Response onHttpResultResponse(String httpResult, Interceptor.Chain chain, Response response) {
//        LogUtils.eTag("Angki", httpResult);
//        LogUtils.eTag("Angki", response);
        return response;
    }

    @Override
    public Request onHttpRequestBefore(Interceptor.Chain chain, Request request) {
//        LogUtils.eTag("Angki", request);
        return request;
    }
}
