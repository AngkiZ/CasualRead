package com.angki.casualread.gank.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.angki.casualread.R;
import com.angki.casualread.gank.WelfareActivity;
import com.angki.casualread.gank.gson.GankWelfareData;
import com.angki.casualread.util.MarginUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tengyu on 2017/3/25.
 */

public class WelfareFragmentRecycleViewAdapter extends
        RecyclerView.Adapter<WelfareFragmentRecycleViewAdapter.ViewHoler>{

    private Context mcontext;

    private List<GankWelfareData> mDataList;

    static class ViewHoler extends RecyclerView.ViewHolder{

        LinearLayout linearLayout;
        ImageView image;

        public ViewHoler(View view) {

            super(view);
            linearLayout = (LinearLayout) view;
            image = (ImageView) view.findViewById(R.id.welfare_item_image);
        }
    }

    public WelfareFragmentRecycleViewAdapter(Context context, List<GankWelfareData> dataList) {

        mcontext = context;
        mDataList = dataList;
    }

    @Override
    public WelfareFragmentRecycleViewAdapter.ViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mcontext)
                .inflate(R.layout.welfare_item_layout, parent, false);
        final ViewHoler holer = new ViewHoler(view);

        holer.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holer.getAdapterPosition() - 1;
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("urlList", UrlList());
                bundle.putInt("code", position);
                Intent intent = new Intent(mcontext, WelfareActivity.class);
                intent.putExtras(bundle);
                mcontext.startActivity(intent);
            }
        });

        return holer;
    }

    @Override
    public void onBindViewHolder(WelfareFragmentRecycleViewAdapter.ViewHoler holder, int position) {

        if (position % 2 == 0) {

            MarginUtil.setViewMargin(holder.linearLayout, 12, 12, 6, 0);
        }else {

            MarginUtil.setViewMargin(holder.linearLayout, 6, 12, 12, 0);
        }

        Glide.with(mcontext).load(mDataList.get(position).getUrl())
//                .skipMemoryCache(true)//跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {

        return mDataList.size();
    }

    private ArrayList<String> UrlList() {

        ArrayList<String> mUrlList = new ArrayList<>();
        for (int i = 0; i < mDataList.size(); i++) {
            mUrlList.add(mDataList.get(i).getUrl());
        }
        return mUrlList;
    }
}
