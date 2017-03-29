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
import java.util.Date;
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

    private Calendar c;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.zhihu_fragment, container, false);
        //加载RecycleView知乎日报数据
        loadZhihuDailyNews(view);
        loadModule(view);
        return view;
    }

    /**
     * 加载知乎日报RecycleView新闻数据
     */
    private void loadZhihuDailyNews(final View view){

        String url = Api.ZHIHU_BEFORE + date();
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

                for (int i = 0; i < newsBeans.getStories().size(); i++) {
                    dataList.add(i, newsBeans.getStories().get(i));
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

        zhihuRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        zhihuRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        zhihuRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);

        zhihuRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        dataList.clear();
                        loadZhihuDailyNews(getView());
                        zhihuRecyclerView.refreshComplete();
                    }

                }, 1000);
            }

            @Override
            public void onLoadMore() {


                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        loadZhihuDailyNews(getView());
                        zhihuRecyclerView.loadMoreComplete();
                    }
                }, 1000);
            }
        });

        //加载adpter适配器
        adapter = new ZhihuFragmentRecycleViewAdapter(getContext(), dataList);
        zhihuRecyclerView.setAdapter(adapter);
    }

    private String date(){

        c = Calendar.getInstance();

        c.add(Calendar.DAY_OF_MONTH, +1);

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DATE);

        if (month < 10 && day < 10) {

            return "" + year + "0" + month + "0" + day;
        }else if (month > 10 && day < 10) {

            return "" + year + month + "0" + day;
        }else if (month < 10 && day > 10) {

            return "" + year + "0" + month + day;
        }

        return "" + year + month +day;
    }
}
