package com.angki.casualread.recommend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.angki.casualread.MainActivity;
import com.angki.casualread.R;
import com.angki.casualread.recommend.adapter.ZhihuContentAdapter;
import com.angki.casualread.util.Api;
import com.angki.casualread.util.HttpUtil;
import com.angki.casualread.util.Utility;
import com.angki.casualread.zhihu.adapter.GlideImageLoader;
import com.angki.casualread.zhihu.adapter.ZhihuFragmentAdapter;
import com.angki.casualread.zhihu.gson.ZhihuDailyNews.NewsBean;
import com.angki.casualread.zhihu.gson.ZhihuDailyNews.NewsBeans;
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

    private RecyclerView zhihuContent;

    private Banner banner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.recommend_fragment, container, false);

        loadZhihuContent(view);

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


    }
}
