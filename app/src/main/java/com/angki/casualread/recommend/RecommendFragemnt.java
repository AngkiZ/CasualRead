package com.angki.casualread.recommend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.angki.casualread.R;
import com.angki.casualread.gank.gson.GankDatas;
import com.angki.casualread.gank.gson.GankWelfareDatas;
import com.angki.casualread.joke.gson.JokeDatas;
import com.angki.casualread.recommend.adapter.GankContentAdapter;
import com.angki.casualread.recommend.adapter.JokeContentAdapter;
import com.angki.casualread.recommend.adapter.ZhihuContentAdapter;
import com.angki.casualread.util.Api;
import com.angki.casualread.util.HttpUtil;
import com.angki.casualread.util.Utility;
import com.angki.casualread.zhihu.adapter.GlideImageLoader;
import com.angki.casualread.zhihu.gson.ZhihuDailyNews.NewsBean;
import com.angki.casualread.zhihu.gson.ZhihuDailyNews.NewsBeans;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by tengyu on 2017/3/20.
 */

public class RecommendFragemnt extends Fragment{

    private static final String TAG = "RecommendFragemnt";

    private RecyclerView zhihuContent;

    private RecyclerView gankContent;

    private RecyclerView jokeContent;

    private ImageView welfareContent;

    private Banner banner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.recommend_fragment, container, false);

        //loadZhihuContent(view);

        //loadWelfareContent(view);

       // loadGankContent(view);

        loadJokeContent(view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


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

                //知乎模块内容集合
                final List<NewsBean> dataList = new ArrayList<NewsBean>();
                //轮播图显示集合
                final List<String> images = new ArrayList<String>();
                final List<String> titles = new ArrayList<String>();
                //获取Json数据
                final String responseData = response.body().string();
                //解析Json数据
                final NewsBeans newsBeans = Utility.handleZHDNResponse(responseData);

                //知乎模块显示内容的集合
                for (int i = 0; i < 4; i++) {

                    dataList.add(newsBeans.getStories().get(i));

                }

                //轮播图显示内容的集合
                for(int i = 0; i < newsBeans.getTopStories().size(); i++){

                    images.add(newsBeans.getTopStories().get(i).getImage());
                    titles.add(newsBeans.getTopStories().get(i).getTitle());
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        /**
                         * 加载RecyclerView
                         */
                        zhihuContent = (RecyclerView) view.findViewById(R.id.recommand_zhihu_recyclerview);
                        //指定布局方式
                        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
                        zhihuContent.setLayoutManager(layoutManager);
                        //加载adpter适配器
                        ZhihuContentAdapter adapter = new ZhihuContentAdapter(getContext(), dataList);
                        zhihuContent.setAdapter(adapter);

                        /**
                         * 加载轮播图控件banner
                         */
                        banner = (Banner) view.findViewById(R.id.zhihu_daily_hot);
                        //设置banner样式
                        banner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);
                        //设置图片加载器
                        banner.setImageLoader(new GlideImageLoader());
                        //设置图片集合
                        banner.setImages(images);
                        //设置标题集合
                        banner.setBannerTitles(titles);
                        //设置指示器位置
                        banner.setIndicatorGravity(BannerConfig.CENTER);
                        //banner设置方法全部调用完毕时最后调用
                        banner.start();
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

                Log.d(TAG, "gankWelfareDatas: " + gankWelfareDatas);
                //加载图片
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        welfareContent = (ImageView) view.findViewById(R.id.recommand_welfare_image);

                        Glide.with(view.getContext())
                                .load(gankWelfareDatas.getResults().get(0).getUrl())
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .into(welfareContent);
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
                //加载数据
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        gankContent = (RecyclerView) view.findViewById(R.id.recommand_gank_recyclerview);
                        //指定加载布局
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                        gankContent.setLayoutManager(layoutManager);
                        //加载adapter
                        GankContentAdapter adapter = new GankContentAdapter(getContext(), gankDatas.getResults());
                        gankContent.setAdapter(adapter);
                    }
                });
            }
        });
    }

    /**
     * 加载推荐碎片中笑话部分内容
     */
    private void loadJokeContent(final View view) {

        String url = Api.JOKE + "?key=f24ebcac31973eebf02a0391b1e8953a&page=1&pagesize=4";

        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                //获取JSON数据
                final String responseData = response.body().string();
                Log.d(TAG, "responseData: " + responseData);
                //解析数据
                final JokeDatas jokeDatas = Utility.handleJokeResponse(responseData);
                Log.d(TAG, "jokeDatas: " + jokeDatas);
                //加载数据
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        jokeContent = (RecyclerView) view.findViewById(R.id.recommand_joke_recyclerview);
                        //指定加载布局
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                        jokeContent.setLayoutManager(layoutManager);
                        //加载adapter
                        JokeContentAdapter adapter = new JokeContentAdapter(getContext(), jokeDatas.getResult().getData());
                        jokeContent.setAdapter(adapter);
                    }
                });

            }
        });
    }
}
