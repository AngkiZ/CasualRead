package com.angki.casualread.mvp.ui.adapter;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.angki.casualread.R;
import com.angki.casualread.mvp.ui.activity.ActivityGank;
import com.angki.casualread.mvp.model.entity.result.GankData;
import com.angki.casualread.app.util.MarginUtil;

import java.util.List;

/**
 * Created by tengyu on 2017/3/23.
 */

public class AdapterGankContent extends RecyclerView.Adapter<AdapterGankContent.ViewHolder> {

    private List<GankData> mGankDataList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView title;
        TextView author;

        public ViewHolder(View view) {

            super(view);
            cardView = (CardView) view;
            title = (TextView) view.findViewById(R.id.recommand_gank_recyclerview_title);
            author = (TextView) view.findViewById(R.id.recommand_gank_recyclerview_author);
        }
    }

    public AdapterGankContent(List<GankData> GankDataList) {

        this.mGankDataList = GankDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.recommand_content_gank, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Intent intent = new Intent(view.getContext(), ActivityGank.class);
                intent.putExtra("gank_url", mGankDataList.get(position).getUrl());
                view.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (position == 0 ) {
            MarginUtil.setViewMargin(holder.cardView, 0, 0, 0, 8);
        }else if (position == mGankDataList.size()) {
            MarginUtil.setViewMargin(holder.cardView, 0, 8, 0, 0);
        }else {
            MarginUtil.setViewMargin(holder.cardView, 0, 8, 0, 8);
        }
        GankData gankData = mGankDataList.get(position);
        holder.title.setText(gankData.getDesc());
        holder.author.setText(gankData.getWho());
    }

    @Override
    public int getItemCount() {

        return mGankDataList.size();

    }
}
