package com.angki.casualread.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.angki.casualread.R;
import com.angki.casualread.mvp.model.entity.bean.dbGank;
import com.angki.casualread.mvp.model.entity.bean.dbZhihuNews;

import java.util.List;

/**
 * Created by tengyu on 2017/4/16.
 */

public class AdapterCollectionRecycler extends RecyclerView.Adapter<AdapterCollectionRecycler.ViewHolder>{

    // 标题集合
    private List<String> titleList;
    //被收藏的知乎新闻
    private List<dbZhihuNews> mcollection_zhihu_list;
    //被收藏的干货
    private List<dbGank> mcollection_gank_list;
    // 列表展开标识
    int opened = -1;

    private Context mcontext;

    public AdapterCollectionRecycler(Context context, List<String> titles
            , List<dbZhihuNews> collection_zhihu_list, List<dbGank> collection_gank_list) {

        mcontext = context;
        titleList = titles;
        mcollection_zhihu_list = collection_zhihu_list;
        mcollection_gank_list = collection_gank_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ViewGroup view =(ViewGroup) LayoutInflater.from(mcontext)
                .inflate(R.layout.collection_recyc_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String title = titleList.get(position);
        holder.bind(position, title);
        if (position == 0) {
            holder.content.setLayoutManager(new LinearLayoutManager(mcontext));
            holder.content.setAdapter(new AdapterZhihuFragmentRecycleView(mcontext, mcollection_zhihu_list, 2));
        }else if (position == 1) {
            holder.content.setLayoutManager(new LinearLayoutManager(mcontext));
            holder.content.setAdapter(new AdapterGankFragmentRecycleView(mcontext, mcollection_gank_list, 2));
        }
    }

    @Override
    public int getItemCount() {
        return titleList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // 标题
        TextView title;
        // 隐藏的内容
        RecyclerView content;
        public ViewHolder(ViewGroup itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.collection_recyc_title);
            content = (RecyclerView) itemView.findViewById(R.id.collection_recyc_content);

            itemView.setOnClickListener(this);
        }

        // 此方法实现列表的展开和关闭
        public void bind(int pos, String name) {
            title.setText(name);
            if (pos == opened)
                content.setVisibility(View.VISIBLE);
            else
                content.setVisibility(View.GONE);

        }

        @Override
        public void onClick(View view) {
            if (opened == getPosition()) {
                opened = -1;
                notifyItemChanged(getPosition());
            }
            else {
//                int oldOpened = opened;
                opened = getPosition();
//                notifyItemChanged(oldOpened);
                notifyItemChanged(opened);
            }
        }
    }
}
