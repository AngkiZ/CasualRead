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
import com.angki.casualread.gank.db.dbWelfare;
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
    private List<dbWelfare> mDataList;

    static class ViewHoler extends RecyclerView.ViewHolder{

        LinearLayout linearLayout;
        ImageView image;

        public ViewHoler(View view) {

            super(view);
            linearLayout = (LinearLayout) view;
            image = (ImageView) view.findViewById(R.id.welfare_item_image);
        }
    }

    public WelfareFragmentRecycleViewAdapter(Context context, List<dbWelfare> dataList) {

        this.mcontext = context;
        this.mDataList = dataList;
    }

    @Override
    public WelfareFragmentRecycleViewAdapter.ViewHoler onCreateViewHolder(final ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.welfare_item_layout, parent, false);
        final ViewHoler holer = new ViewHoler(view);

        holer.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holer.getAdapterPosition() - 1;
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("urlList", UrlList());
                bundle.putStringArrayList("idList", IdList());
                bundle.putInt("code", position);
                Intent intent = new Intent(parent.getContext(), WelfareActivity.class);
                intent.putExtras(bundle);
                parent.getContext().startActivity(intent);
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

        Glide.with(mcontext).load(mDataList.get(position).getDb_we_url())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.ic_meizi)
                .error(R.drawable.ic_meizi)
                .dontAnimate()
                .centerCrop()
                .into(holder.image);
    }

    @Override
    public int getItemCount() {

        return mDataList.size();
    }
    //转换数组属性
    private ArrayList<String> UrlList() {

        ArrayList<String> mUrlList = new ArrayList<>();
        for (int i = 0; i < mDataList.size(); i++) {
            mUrlList.add(mDataList.get(i).getDb_we_url());
        }
        return mUrlList;
    }

    //转换数组属性
    private ArrayList<String> IdList() {

        ArrayList<String> mIdList = new ArrayList<>();
        for (int i = 0; i < mDataList.size(); i++) {
            mIdList.add(mDataList.get(i).getDb_we_id());
        }
        return mIdList;
    }

    /**
     * 清除内存
     */
    public void clearMemory() {
        Glide.get(mcontext).clearMemory();
        mcontext = null;
        mDataList.clear();
        mDataList = null;
    }
}
