package com.angki.casualread.recommend.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.angki.casualread.R;
import com.angki.casualread.gank.WelfareActivity;
import com.angki.casualread.gank.gson.GankData;
import com.angki.casualread.gank.gson.GankWelfareData;
import com.angki.casualread.joke.gson.JokeData;
import com.angki.casualread.zhihu.ZhihuActivity;
import com.angki.casualread.zhihu.gson.ZhihuDailyNews.NewsBean;
import com.angki.casualread.zhihu.gson.ZhihuDailyNews.TopNewsBean;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tengyu on 2017/3/31.
 */

public class RecommendAdapter extends RecyclerView.Adapter implements OnBannerListener{

    private Context mcontext;
    private List<String> images = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private List<String> topId = new ArrayList<>();
    private List<NewsBean> zhihuData = new ArrayList<>();
    private List<GankWelfareData> welfareData = new ArrayList<>();
    private List<GankData> gankData = new ArrayList<>();
    private List<JokeData> jokeData = new ArrayList<>();
    private ViewPager viewPager;

    public RecommendAdapter (Context context, List<String> images, List<String> titles,
                             List<NewsBean> zhihuData, List<GankWelfareData> welfareData,
                             List<GankData> gankData, List<JokeData> jokeData,
                             ViewPager viewPager, List<String> topId) {

        this.mcontext = context;
        this.images = images;
        this.titles = titles;
        this.zhihuData = zhihuData;
        this.welfareData = welfareData;
        this.gankData = gankData;
        this.jokeData = jokeData;
        this.viewPager = viewPager;
        this.topId = topId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case 0:
                View view0 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recommend_title_carousel, parent, false);
                return new ViewHolder1(view0);
            case 1:
                View view1 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recommend_title_zhihu, parent, false);
                return new ViewHolder2(view1);
            case 2:
                View view2 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recommend_title_welfare, parent, false);
                return new ViewHolder3(view2);
            case 3:
                View view3 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recommand_title_gank, parent, false);
                return new ViewHolder4(view3);
            case 4:
                View view4 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recommend_title_joke, parent, false);
                return new ViewHolder5(view4);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolder1) {
            loadholder1(holder);
        }else if (holder instanceof ViewHolder2) {
            loadholder2(holder);
        }else if (holder instanceof ViewHolder3) {
           loadholder3(holder);
        }else if (holder instanceof ViewHolder4) {
            loadholder4(holder);
        }else if (holder instanceof ViewHolder5) {
            loadholder5(holder);
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    @Override
    public int getItemViewType(int position) {
        //判断加载哪个布局
        switch (position) {
            case 0://banner布局
                return 0;
            case 1://知乎布局
                return 1;
            case 2://福利布局
                return 2;
            case 3://gank布局
                return 3;
            case 4://joke布局
                return 4;
            default:
                return -1;
        }
    }

    /**
     * 加载各个布局
     */
    private static class ViewHolder1 extends RecyclerView.ViewHolder {

        Banner banner;
        ViewHolder1(View view) {

            super(view);
            banner = (Banner) view.findViewById(R.id.zhihu_daily_hot);
        }
    }

    private static class ViewHolder2 extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;
        TextView textview;

        ViewHolder2(View view) {

            super(view);
            recyclerView = (RecyclerView) view.findViewById(R.id.recommand_zhihu_recyclerview);
            textview = (TextView) view.findViewById(R.id.title_zhihu_more);
        }
    }

    private static class ViewHolder3 extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        ViewHolder3(View view) {

            super(view);
            imageView = (ImageView) view.findViewById(R.id.recommand_welfare_image);
            textView = (TextView) view.findViewById(R.id.title_welfare_more);
        }
    }

    private static class ViewHolder4 extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;
        TextView textview;

        ViewHolder4(View view) {

            super(view);
            recyclerView = (RecyclerView) view.findViewById(R.id.recommand_gank_recyclerview);
            textview = (TextView) view.findViewById(R.id.title_gank_more);
        }
    }

    private static class ViewHolder5 extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;
        TextView textview;

        ViewHolder5(View view) {

            super(view);
            recyclerView = (RecyclerView) view.findViewById(R.id.recommand_joke_recyclerview);
            textview = (TextView) view.findViewById(R.id.title_joke_more);
        }
    }

    /**
     * 设置每个布局的组件的数据
     */
    private void loadholder1(RecyclerView.ViewHolder holder) {

        ((ViewHolder1) holder).banner
                .setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE)//设置banner样式
                //设置图片加载器
                .setImageLoader(new GlideImageLoader())
                //设置图片集合
                .setImages(images)
                //设置标题集合
                .setBannerTitles(titles)
                //设置指示器位置
                .setIndicatorGravity(BannerConfig.CENTER)
                //设置点击事件
                .setOnBannerListener(this)
                //banner设置方法全部调用完毕时最后调用
                .start();

    }
    private void loadholder2(RecyclerView.ViewHolder holder) {
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        ((ViewHolder2) holder).recyclerView
                .setHasFixedSize(true);
        ((ViewHolder2) holder).recyclerView
                .setLayoutManager(new GridLayoutManager(mcontext, 2));
        ((ViewHolder2) holder).recyclerView
                .setAdapter(new ZhihuContentAdapter(mcontext, zhihuData));
        ((ViewHolder2) holder).textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
            }
        });
    }

    private void loadholder3(RecyclerView.ViewHolder holder) {

        if (welfareData.size() > 0) {
            Glide.with(mcontext)
                    .load(welfareData.get(0).getUrl())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(((ViewHolder3) holder).imageView);
        }else {
            Glide.with(mcontext)
                    .load(R.drawable.big_image)
                    .into(((ViewHolder3) holder).imageView);
        }
        ((ViewHolder3) holder).imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putStringArrayList("urlList", UrlList());
                bundle.putInt("code", 0);
                Intent intent = new Intent(view.getContext(), WelfareActivity.class);
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);
            }
        });
        ((ViewHolder3) holder).textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(4);
            }
        });
    }

    private void loadholder4(RecyclerView.ViewHolder holder) {

        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        ((ViewHolder4) holder).recyclerView
                .setHasFixedSize(true);
        ((ViewHolder4) holder).recyclerView
                .setLayoutManager(new LinearLayoutManager(mcontext));
        ((ViewHolder4) holder).recyclerView
                .setAdapter(new GankContentAdapter(mcontext, gankData));
        ((ViewHolder4) holder).textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(2);
            }
        });
    }

    private void loadholder5(RecyclerView.ViewHolder holder) {

        ((ViewHolder5) holder).recyclerView
                .setLayoutManager(new LinearLayoutManager(mcontext));
        ((ViewHolder5) holder).recyclerView
                .setAdapter(new JokeContentAdapter(mcontext, jokeData));
        ((ViewHolder5) holder).textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(3);
            }
        });
    }

    /**
     * Banner的点击事件
     * @param position
     */
    @Override
    public void OnBannerClick(int position) {

        Intent intent = new Intent(mcontext, ZhihuActivity.class);
        intent.putExtra("news_id", topId.get(position));
        mcontext.startActivity(intent);
    }

    private ArrayList<String> UrlList() {

        ArrayList<String> mUrlList = new ArrayList<>();
        for (int i = 0; i < welfareData.size(); i++) {
            mUrlList.add(welfareData.get(i).getUrl());
        }
        return mUrlList;
    }

    /**
     * 清除内存
     */
    public void clearMemory() {
        Glide.get(mcontext).clearMemory();
        mcontext = null;
        zhihuData.clear();
        welfareData.clear();
        gankData.clear();
        jokeData.clear();
        zhihuData = null;
        welfareData = null;
        gankData = null;
        jokeData = null;
        viewPager = null;
    }
}
