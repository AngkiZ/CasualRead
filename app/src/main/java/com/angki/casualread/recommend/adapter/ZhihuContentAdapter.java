package com.angki.casualread.recommend.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
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
 * Created by tengyu on 2017/3/22.
 */

public class ZhihuContentAdapter extends RecyclerView.Adapter<ZhihuContentAdapter.ViewHolder> {

    private Context mContext;

    private List<NewsBean> mZhihuDailyNewsList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView image;
        TextView title;

        public ViewHolder(View view) {

            super(view);
            cardView = (CardView) view;
            image = (ImageView) view.findViewById(R.id.recommand_zhihu_recyclerview_image);
            title = (TextView) view.findViewById(R.id.recommand_zhihu_recyclerview_title);
        }
    }

    public ZhihuContentAdapter(Context context, List<NewsBean> ZhihuDailyNewsList) {

        mContext = context;

        mZhihuDailyNewsList = ZhihuDailyNewsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).
                inflate(R.layout.recommand_content_zhihu, parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        NewsBean zhihuDailyNews = mZhihuDailyNewsList.get(position);
        holder.title.setText(zhihuDailyNews.getTitle());
        Glide.with(mContext)
                .load(zhihuDailyNews.getImages().get(0))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {

        return mZhihuDailyNewsList.size();
    }
}
