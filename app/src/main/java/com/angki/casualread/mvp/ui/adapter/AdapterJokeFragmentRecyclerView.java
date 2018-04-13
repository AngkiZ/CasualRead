package com.angki.casualread.mvp.ui.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.angki.casualread.R;
import com.angki.casualread.mvp.model.entity.bean.dbJoke;

import java.util.List;

/**
 * Created by tengyu on 2017/3/24.
 */

public class AdapterJokeFragmentRecyclerView extends RecyclerView.Adapter<AdapterJokeFragmentRecyclerView.ViewHoler>{

    private List<dbJoke> mDataList;

    static class ViewHoler extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView content;

        public ViewHoler(View view) {

            super(view);
            cardView = (CardView) view;
            content = (TextView) view.findViewById(R.id.joke_item_content);
        }
    }

    public AdapterJokeFragmentRecyclerView(List<dbJoke> dataList) {
        mDataList = dataList;
    }

    @Override
    public ViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.joke_item_layout, parent, false);

        return new ViewHoler(view);
    }

    @Override
    public void onBindViewHolder(ViewHoler holder, int position) {

        dbJoke jokeData = mDataList.get(position);

        holder.content.setText(jokeData.getDb_joke_content());
    }

    @Override
    public int getItemCount() {

        return mDataList.size();
    }

    /**
     * 清除内存
     */
    public void clearMemory() {
        mDataList.clear();
        mDataList = null;
    }
}
