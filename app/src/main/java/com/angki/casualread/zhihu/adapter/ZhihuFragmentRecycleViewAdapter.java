package com.angki.casualread.zhihu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.angki.casualread.R;
import com.angki.casualread.zhihu.gson.ZhihuDailyNews.NewsBean;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by tengyu on 2017/3/15.
 */

public class ZhihuFragmentRecycleViewAdapter extends RecyclerView.Adapter<ZhihuFragmentRecycleViewAdapter.ViewHolder>{

    private Context mcontext;

    private List<NewsBean> mZhihuDailyNewsList;

    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView zhihuImage;
        TextView zhihuTitle;

        public ViewHolder(View view){

            super(view);
            zhihuImage = (ImageView) view.findViewById(R.id.zhihu_item_image);
            zhihuTitle = (TextView) view.findViewById(R.id.zhihu_item_title);
        }

    }

    public ZhihuFragmentRecycleViewAdapter(Context context, List<NewsBean> ZhihuDailyNewsList){

        mcontext = context;

        mZhihuDailyNewsList = ZhihuDailyNewsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.zhihu_item_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        NewsBean zhihuDailyNews = mZhihuDailyNewsList.get(position);
        holder.zhihuTitle.setText(zhihuDailyNews.getTitle());
        Glide.with(mcontext)
                .load(zhihuDailyNews.getImages().get(0))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.zhihuImage);
    }

    @Override
    public int getItemCount() {
        return mZhihuDailyNewsList.size();
    }


}
