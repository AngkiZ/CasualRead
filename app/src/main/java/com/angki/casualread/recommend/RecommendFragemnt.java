package com.angki.casualread.recommend;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
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
import com.angki.casualread.util.NetworkStatus;
import com.angki.casualread.util.ToastUtil;
import com.angki.casualread.util.Utility;
import com.angki.casualread.zhihu.ZhihuActivity;
import com.angki.casualread.zhihu.gson.ZhihuDailyNews.NewsBean;
import com.angki.casualread.zhihu.gson.ZhihuDailyNews.NewsBeans;
import com.angki.casualread.zhihu.gson.ZhihuDailyNews.TopNewsBean;


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
    private List<String> topid = new ArrayList<>();
    private List<NewsBean> zhihuData = new ArrayList<>();
    private List<GankWelfareData> welfareData = new ArrayList<>();
    private List<GankData> gankData = new ArrayList<>();
    private List<JokeData> jokeData = new ArrayList<>();

    private RecommendAdapter adapter;
    private NetworkStatus networkStatus;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.recommend_fragment, container, false);
        //清空列表
        zhihuData.clear();
        topid.clear();
        images.clear();
        titles.clear();
        welfareData.clear();
        gankData.clear();
        jokeData.clear();

        loadZhihuContent();
        loadWelfareContent();
        loadGankContent();
        loadJokeContent();
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
                        networkStatus = new NetworkStatus();
                        if (networkStatus.judgment(getContext())) {
                            loadZhihuContent();
                            loadWelfareContent();
                            loadGankContent();
                            loadJokeContent();
                            loadModule(view);
                        }else {
                            ToastUtil.showToast(getContext(), "没有网络..");
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        //加载recyclerView布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecommendAdapter(getContext(),
                images, titles, zhihuData, welfareData, gankData, jokeData, viewPager, topid);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 加载推荐碎片中知乎模块部分内容
     */
    private void loadZhihuContent() {

        String url = Api.ZHIHU_NEWS + "latest";

        HttpUtil.sendOkHttpRequest(url, new Callback() {
            //没有网时加载
            @Override
            public void onFailure(Call call, IOException e) {

                SharedPreferences prefs = PreferenceManager
                        .getDefaultSharedPreferences(getActivity());
                String JSON = prefs.getString("zhihu", null);
                final NewsBeans newsBeans = Utility.handleZHDNResponse(JSON);
                for (int i = 0; i < 4; i++) {

                    zhihuData.add(newsBeans.getStories().get(i));
                }

                //轮播图显示内容的集合
                for(int i = 0; i < newsBeans.getTopStories().size(); i++){

                    images.add(newsBeans.getTopStories().get(i).getImage());
                    titles.add(newsBeans.getTopStories().get(i).getTitle());
                    topid.add(newsBeans.getTopStories().get(i).getId());

                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                //获取Json数据
                final String responseData = response.body().string();
                //解析Json数据
                final NewsBeans newsBeans = Utility.handleZHDNResponse(responseData);

                //知乎模块显示内容的集合
                for (int i = 0; i < 4; i++) {

                    zhihuData.add(newsBeans.getStories().get(i));
                }

                //轮播图显示内容的集合
                for(int i = 0; i < newsBeans.getTopStories().size(); i++){

                    images.add(newsBeans.getTopStories().get(i).getImage());
                    titles.add(newsBeans.getTopStories().get(i).getTitle());
                    topid.add(newsBeans.getTopStories().get(i).getId());

                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //存储
                        SharedPreferences.Editor editor = PreferenceManager
                                .getDefaultSharedPreferences(getActivity()).edit();
                        editor.putString("zhihu", responseData);
                        editor.apply();
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    /**
     * 加载推荐碎片中福利部分内容
     */
    private void loadWelfareContent() {

        String url = Api.WELFARE + "1/1";

        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String JSON = prefs.getString("Welfare", null);
                //解析数据
                final GankWelfareDatas gankWelfareDatas =
                        Utility.handleGankWelfareResponse(JSON);
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

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                //获取JSON数据
                final String responseData = response.body().string();
                //解析数据
                final GankWelfareDatas gankWelfareDatas =
                        Utility.handleGankWelfareResponse(responseData);

                for (int i = 0; i < gankWelfareDatas.getResults().size(); i++) {

                    welfareData.add(gankWelfareDatas.getResults().get(i));
                }
                //加载图片
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        SharedPreferences.Editor editor = PreferenceManager
                                .getDefaultSharedPreferences(getActivity()).edit();
                        editor.putString("Welfare", responseData);
                        editor.apply();
                    }
                });
            }
        });
    }

    /**
     * 加载推荐碎片中干货部分内容
     */
    private void loadGankContent() {

        String url = Api.GANK + "4/1";

        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                SharedPreferences prefs = PreferenceManager
                        .getDefaultSharedPreferences(getActivity());
                String JSON = prefs.getString("Gank", null);
                //解析数据
                final GankDatas gankDatas = Utility.handleGankResponse(JSON);

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

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                //获取JSON数据
                final String reponseData = response.body().string();
                //解析数据
                final GankDatas gankDatas = Utility.handleGankResponse(reponseData);

                for (int i = 0; i < gankDatas.getResults().size(); i++) {

                    gankData.add(gankDatas.getResults().get(i));
                }
                //加载数据
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        SharedPreferences.Editor editor = PreferenceManager
                                .getDefaultSharedPreferences(getActivity()).edit();
                        editor.putString("Gank", reponseData);
                        editor.apply();
                    }
                });
            }
        });
    }

    /**
     * 加载推荐碎片中笑话部分内容
     */
    private void loadJokeContent() {

        String url = Api.JOKE + "?key=f24ebcac31973eebf02a0391b1e8953a&page=1&pagesize=3";

        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                SharedPreferences prefs = PreferenceManager
                        .getDefaultSharedPreferences(getActivity());
                String JSON = prefs.getString("Joke", null);
                //解析数据
                final List<JokeData> jokeDatas = Utility.handleJokeResponse(JSON);

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

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                //获取JSON数据
                final String responseData = response.body().string();
                //解析数据
                final List<JokeData> jokeDatas = Utility.handleJokeResponse(responseData);

                for (int i = 0; i < jokeDatas.size(); i++) {

                    jokeData.add(jokeDatas.get(i));
                }
                //加载数据
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        SharedPreferences.Editor editor = PreferenceManager
                                .getDefaultSharedPreferences(getActivity()).edit();
                        editor.putString("Joke", responseData);
                        editor.apply();
                    }
                });

            }
        });
    }

}
