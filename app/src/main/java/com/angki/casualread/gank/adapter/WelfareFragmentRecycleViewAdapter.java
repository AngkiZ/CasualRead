package com.angki.casualread.gank.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.angki.casualread.R;
import com.angki.casualread.gank.gson.GankWelfareData;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

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

        return new ViewHoler(view);
    }

    @Override
    public void onBindViewHolder(WelfareFragmentRecycleViewAdapter.ViewHoler holder, int position) {

        Glide.with(mcontext).load(mDataList.get(position).getUrl())
                .skipMemoryCache(true)//跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {

        return mDataList.size();
    }
}