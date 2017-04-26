package com.angki.casualread.gank.adapter;

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
import com.angki.casualread.gank.GankActivity;
import com.angki.casualread.gank.db.dbGank;
import com.angki.casualread.gank.gson.GankData;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by tengyu on 2017/3/24.
 */

public class GankFragmentRecycleViewAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mcontext;
    private List<dbGank> mDataList;

    private int msource;//请求来自哪个碎片，1为知乎列表，2为收藏列表

    //建立枚举 2个item 类型
    public enum ITEM_TYPE {
        ITEM1,
        ITEM2
    }

    //item1 的ViewHolder
    static class ViewHolder1 extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView title1;
        ImageView image;
        TextView author1;

        public ViewHolder1(View view) {

            super(view);
            cardView = (CardView) view;
            //有图片时加载的
            title1 = (TextView) view.findViewById(R.id.gank_item_title_1);
            image = (ImageView) view.findViewById(R.id.gank_item_image);
            author1 = (TextView) view.findViewById(R.id.gank_item_author_1);

        }
    }

    //item2 的ViewHolder
    static class ViewHolder2 extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView title2;
        TextView author2;

        public ViewHolder2(View view) {

            super(view);
            cardView = (CardView) view;
            title2 = (TextView) view.findViewById(R.id.gank_item_title_2);
            author2 = (TextView) view.findViewById(R.id.gank_item_author_2);

        }
    }

    public GankFragmentRecycleViewAdapter(Context context, List<dbGank> dataList, int source) {

        mcontext = context;
        mDataList = dataList;
        msource = source;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //加载Item View的时候根据不同TYPE加载不同的布局
        if (viewType == ITEM_TYPE.ITEM1.ordinal()) {

            View view1 = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.gank_item_layout_1, parent, false);
            final ViewHolder1 holder1 = new ViewHolder1(view1);

            holder1.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = 0;
                    if (msource == 1) {
                        position = holder1.getAdapterPosition() - 1;
                    }else if (msource == 2) {
                        position = holder1.getAdapterPosition();
                    }
                    Intent intent = new Intent(view.getContext(), GankActivity.class);
                    intent.putExtra("gank_url", mDataList.get(position).getDb_gank_url());
                    view.getContext().startActivity(intent);
                }
            });

            return holder1;
        } else {

            View view2 = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.gank_item_layout_2, parent, false);
            final ViewHolder2 holder2 = new ViewHolder2(view2);

            holder2.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = 0;
                    if (msource == 1) {
                        position = holder2.getAdapterPosition() - 1;
                    }else if (msource == 2) {
                        position = holder2.getAdapterPosition();
                    }
                    Intent intent = new Intent(view.getContext(), GankActivity.class);
                    intent.putExtra("gank_url", mDataList.get(position).getDb_gank_url());
                    view.getContext().startActivity(intent);
                }
            });

            return holder2;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        dbGank gankData = mDataList.get(position);

        if (holder instanceof ViewHolder1) {

            ((ViewHolder1) holder).title1.setText(gankData.getDb_gank_title());
            ((ViewHolder1) holder).author1.setText(gankData.getDb_gank_who());
            Glide.with(mcontext).load(gankData.getDb_gank_image())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .override(80, 80)
                    .centerCrop()
                    .into(((ViewHolder1) holder).image);

        } else if (holder instanceof ViewHolder2){

            ((ViewHolder2) holder).title2.setText(gankData.getDb_gank_title());
            ((ViewHolder2) holder).author2.setText(gankData.getDb_gank_who());
        }
    }

    //设置显示哪个布局
    @Override
    public int getItemViewType(int position) {

        if (mDataList.get(position).getDb_gank_image().equals("null")){

            return ITEM_TYPE.ITEM2.ordinal();
        }else {

            return ITEM_TYPE.ITEM1.ordinal();
        }
    }

    @Override
    public int getItemCount() {

        return mDataList.size();
    }

    /**
     * 清除内存
     */
    public void clearMemory() {
        Glide.get(mcontext).clearMemory();
        mcontext = null;
        mcontext = null;
        mDataList.clear();
        mDataList = null;
    }
}
