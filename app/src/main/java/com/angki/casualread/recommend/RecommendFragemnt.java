package com.angki.casualread.recommend;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angki.casualread.MainActivity;
import com.angki.casualread.R;
import com.angki.casualread.gank.gson.GankData;
import com.angki.casualread.gank.gson.GankDatas;
import com.angki.casualread.gank.gson.GankWelfareData;
import com.angki.casualread.gank.gson.GankWelfareDatas;
import com.angki.casualread.joke.gson.JokeData;
import com.angki.casualread.recommend.adapter.RecommendAdapter;
import com.angki.casualread.util.Api;
import com.angki.casualread.util.HttpUtil;
import com.angki.casualread.util.Utility;
import com.angki.casualread.zhihu.gson.ZhihuDailyNews.NewsBean;
import com.angki.casualread.zhihu.gson.ZhihuDailyNews.NewsBeans;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by tengyu on 2017/3/20.
 */

public class RecommendFragemnt extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private List<String> images = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private List<NewsBean> zhihuData = new ArrayList<>();
    private List<GankWelfareData> welfareData = new ArrayList<>();
    private List<GankData> gankData = new ArrayList<>();
    private List<JokeData> jokeData = new ArrayList<>();

    private RecommendAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.recommend_fragment, container, false);

        loadZhihuContent(view);
        loadWelfareContent(view);
        loadGankContent(view);
        loadJokeContent(view);
        loadModule(view);

        return view;
    }


    private void loadModule(final View view) {

        final MainActivity mainActivity = (MainActivity) getActivity();
        final ViewPager viewPager = mainActivity.getViewPager();

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swip_refresh);
        recyclerView = (RecyclerView) view.findViewById(R.id.recommand_xrecyclerview);
        //设置swipeRefreshLayout
        swipeRefreshLayout.setColorSchemeResources(R.color.swipeColor);//进度条颜色
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout
                .OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadZhihuContent(view);
                        loadWelfareContent(view);
                        loadGankContent(view);
                        loadJokeContent(view);
                        loadModule(view);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        //加载recyclerView布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecommendAdapter(getContext(),
                images, titles, zhihuData, welfareData, gankData, jokeData, viewPager);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 加载推荐碎片中知乎模块部分内容
     * @param view
     */
    private void loadZhihuContent(final View view) {

        String url = Api.ZHIHU_NEWS + "latest";

        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                //获取Json数据
                final String responseData = response.body().string();
                //解析Json数据
                final NewsBeans newsBeans = Utility.handleZHDNResponse(responseData);

                zhihuData.clear();
                images.clear();
                titles.clear();
                //知乎模块显示内容的集合
                for (int i = 0; i < 4; i++) {

                    zhihuData.add(newsBeans.getStories().get(i));
                }

                //轮播图显示内容的集合
                for(int i = 0; i < newsBeans.getTopStories().size(); i++){

                    images.add(newsBeans.getTopStories().get(i).getImage());
                    titles.add(newsBeans.getTopStories().get(i).getTitle());
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
     * 加载推荐碎片中福利部分内容
     */
    private void loadWelfareContent(final View view) {

        String url = Api.WELFARE + "1/1";

        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                //获取JSON数据
                final String responseData = response.body().string();
                //解析数据
                final GankWelfareDatas gankWelfareDatas = Utility.handleGankWelfareResponse(responseData);

                welfareData.clear();
                for (int i = 0; i < gankWelfareDatas.getResults().size(); i++) {

                    welfareData.add(gankWelfareDatas.getResults().get(i));
                }
                //加载图片
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
     * 加载推荐碎片中干货部分内容
     */
    private void loadGankContent(final View view) {

        String url = Api.GANK + "4/1";

        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                //获取JSON数据
                final String reponseData = response.body().string();
                //解析数据
                final GankDatas gankDatas = Utility.handleGankResponse(reponseData);

                gankData.clear();
                for (int i = 0; i < gankDatas.getResults().size(); i++) {

                    gankData.add(gankDatas.getResults().get(i));
                }
                //加载数据
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
     * 加载推荐碎片中笑话部分内容
     */
    private void loadJokeContent(final View view) {

        String url = Api.JOKE + "?key=f24ebcac31973eebf02a0391b1e8953a&page=1&pagesize=3";

        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                //获取JSON数据
                final String responseData = response.body().string();
                //解析数据
                final List<JokeData> jokeDatas = Utility.handleJokeResponse(responseData);

                jokeData.clear();
                for (int i = 0; i < jokeDatas.size(); i++) {

                    jokeData.add(jokeDatas.get(i));
                }
                //加载数据
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });

            }
        });
    }

}
