package com.angki.casualread.zhihu;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.angki.casualread.R;
import com.angki.casualread.util.Api;
import com.angki.casualread.util.HttpUtil;
import com.angki.casualread.util.Utility;
import com.angki.casualread.zhihu.adapter.ZhihuFragmentRecycleViewAdapter;
import com.angki.casualread.zhihu.gson.ZhihuDailyNews.NewsBean;
import com.angki.casualread.zhihu.gson.ZhihuDailyNews.NewsBeans;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by tengyu on 2017/3/15.
 */

public class ZhihuFragment extends Fragment{

    private static final String TAG = "ZhihuFragment";

    private XRecyclerView zhihuRecyclerView;

    private LinearLayoutManager layoutManager;

    private List<NewsBean> dataList = new ArrayList<>();

    private ZhihuFragmentRecycleViewAdapter adapter;

    private String url;

    private Calendar c;//获取时间类

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.zhihu_fragment, container, false);
        //加载RecycleView知乎日报数据
        loadZhihuDailyNews(view, true);
        loadModule(view);
        return view;
    }

    /**
     * 加载知乎日报RecycleView新闻数据
     */
    private void loadZhihuDailyNews(final View view, boolean b){

        if (b) {
            url = Api.ZHIHU_BEFORE + date();
            Log.d("------", "url: " + url);
        }else {
            url = Api.ZHIHU_BEFORE + bedate();
            Log.d("------", "beurl: " + url);
        }
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(view.getContext(), "加载失败..", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response)
                    throws IOException {
                //获取Json数据
                final String responseData = response.body().string();

                //解析Json数据
                NewsBeans newsBeans = Utility.handleZHDNResponse(responseData);

                //获取之前集合大小
                int a = dataList.size();

                for (int i = 0; i < newsBeans.getStories().size(); i++) {
                    dataList.add(i + a, newsBeans.getStories().get(i));
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    /**
     * 加载组件
     * @param view
     */
    private void loadModule(View view) {
        /**
         * 加载RecyclerView
         */
        zhihuRecyclerView = (XRecyclerView) view.findViewById(R.id.zhihu_daily_list);
        //指定布局方式
        layoutManager = new LinearLayoutManager(getContext());
        zhihuRecyclerView.setLayoutManager(layoutManager);
        //加载XRecycleView的刷新风格
        zhihuRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        zhihuRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        zhihuRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        //下拉刷新，上拉加载事件
        zhihuRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            //下拉刷新
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        dataList.clear();
                        loadZhihuDailyNews(getView(), true);
                        zhihuRecyclerView.refreshComplete();
                    }

                }, 1000);
            }
            //上拉加载
            @Override
            public void onLoadMore() {


                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        loadZhihuDailyNews(getView(), false);
                        zhihuRecyclerView.loadMoreComplete();
                    }
                }, 1000);
            }
        });

        //加载adpter适配器
        adapter = new ZhihuFragmentRecycleViewAdapter(getContext(), dataList);
        zhihuRecyclerView.setAdapter(adapter);
    }

    /**
     * 今日date
     * @return
     */
    private String date(){

        c = Calendar.getInstance();

        /**
         * 因为数据请求需要，获取今日内容需要的日期是第二天
         */
        c.add(Calendar.DAY_OF_MONTH, +1);

        return Analysis(c);
    }

    /**
     * 之前date
     * @return
     */
    private String bedate() {

        /**
         * 将时间往前一天
         */
        c.add(c.DAY_OF_MONTH, -1);

        return Analysis(c);
    }

    /**
     * 将时间转换成字符串
     * @param c
     * @return
     */
    private String Analysis(Calendar c) {

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DATE);


        if (month < 10 && day < 10) {

            return "" + year + "0" + month + "0" + day;
        }else if (month >= 10 && day < 10) {

            return "" + year + month + "0" + day;
        }else if (month < 10 && day >= 10) {

            return "" + year + "0" + month + day;
        }

        return "" + year + month +day;
    }
}
