package com.angki.casualread.zhihu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.angki.casualread.R;
import com.angki.casualread.util.Api;
import com.angki.casualread.util.HttpUtil;
import com.angki.casualread.util.Utility;
import com.angki.casualread.zhihu.adapter.ZhihuFragmentAdapter;
import com.angki.casualread.zhihu.gson.GlideImageLoader;
import com.angki.casualread.zhihu.gson.ZhihuDailyNews.StoriesBeans;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by tengyu on 2017/3/15.
 */

public class ZhihuFragment extends Fragment{

    private static final String TAG = "ZhihuFragment";

    private Banner banner;

    private RecyclerView zhihuRecyclerView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.zhihu_fragment, container, false);

        //加载RecycleView知乎日报列表
        loadZhihuDailyNews(view);

        return view;
    }

    /**
     * 加载知乎日报RecycleView新闻列表
     */
    private void loadZhihuDailyNews(final View view){

        String url = Api.ZHIHU_NEWS + "latest";
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
                final StoriesBeans storiesBeans = Utility.handleZHDNResponse(responseData);

                final List<String> images = new ArrayList<String>();
                final List<String> titles = new ArrayList<String>();
                for(int i = 0; i < storiesBeans.getTopStories().size(); i++){

                    images.add(storiesBeans.getTopStories().get(i).getImage());
                    titles.add(storiesBeans.getTopStories().get(i).getTitle());
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        /**
                         * 加载RecyclerView
                         */
                        zhihuRecyclerView = (RecyclerView) view.findViewById(R.id.zhihu_daily_list);
                        //指定布局方式
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                        zhihuRecyclerView.setLayoutManager(layoutManager);
                        //加载adpter适配器
                        ZhihuFragmentAdapter adapter = new ZhihuFragmentAdapter(getContext(), storiesBeans.getStories());
                        zhihuRecyclerView.setAdapter(adapter);

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
}
