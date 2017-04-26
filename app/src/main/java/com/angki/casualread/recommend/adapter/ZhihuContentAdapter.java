package com.angki.casualread.recommend.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.angki.casualread.R;
import com.angki.casualread.zhihu.ZhihuActivity;
import com.angki.casualread.zhihu.gson.ZhihuDailyNews.NewsBean;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by tengyu on 2017/3/22.
 */

public class ZhihuContentAdapter extends RecyclerView.Adapter<ZhihuContentAdapter.ViewHolder> {

    private final Context mContext;

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

        this.mContext = context;

        this.mZhihuDailyNewsList = ZhihuDailyNewsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.recommand_content_zhihu, parent , false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Intent intent = new Intent(view.getContext(), ZhihuActivity.class);
                intent.putExtra("news_id", mZhihuDailyNewsList.get(position).getId());
                view.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        NewsBean zhihuDailyNews = mZhihuDailyNewsList.get(position);
        holder.title.setText(zhihuDailyNews.getTitle());
        Glide.with(mContext)
                .load(zhihuDailyNews.getImages().get(0))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .into(holder.image);
    }

    @Override
    public int getItemCount() {

        return mZhihuDailyNewsList.size();
    }
}
