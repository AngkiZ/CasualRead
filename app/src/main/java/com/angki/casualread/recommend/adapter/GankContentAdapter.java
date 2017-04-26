package com.angki.casualread.recommend.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.angki.casualread.R;
import com.angki.casualread.gank.GankActivity;
import com.angki.casualread.gank.gson.GankData;

import java.util.List;

/**
 * Created by tengyu on 2017/3/23.
 */

public class GankContentAdapter extends RecyclerView.Adapter<GankContentAdapter.ViewHolder> {

    private final Context mcontext;

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

    public GankContentAdapter(Context context, List<GankData> GankDataList) {

        this.mcontext = context;

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
                Intent intent = new Intent(view.getContext(), GankActivity.class);
                intent.putExtra("gank_url", mGankDataList.get(position).getUrl());
                view.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        GankData gankData = mGankDataList.get(position);

        holder.title.setText(gankData.getDesc());
        holder.author.setText(gankData.getWho());
    }

    @Override
    public int getItemCount() {

        return mGankDataList.size();

    }
}
