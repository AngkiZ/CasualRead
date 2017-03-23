package com.angki.casualread.recommend.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.angki.casualread.R;
import com.angki.casualread.joke.gson.Data;
import com.angki.casualread.joke.gson.JokeData;

import java.util.List;

/**
 * Created by tengyu on 2017/3/23.
 */

public class JokeContentAdapter extends RecyclerView.Adapter<JokeContentAdapter.ViewHolder>{

    private Context mcontext;

    private List<Data> mDataList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView content;

        public ViewHolder(View view) {

            super(view);
            cardView = (CardView) view;
            content = (TextView) view.findViewById(R.id.recommand_joke_recyclerview_content);
        }
    }

    public JokeContentAdapter(Context context, List<Data> dataList) {

        mcontext = context;

        mDataList = dataList;
    }

    @Override
    public JokeContentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mcontext)
                .inflate(R.layout.recommand_content_joke, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(JokeContentAdapter.ViewHolder holder, int position) {

        Data jokeData = mDataList.get(position);

        holder.content.setText(jokeData.getContent());
    }

    @Override
    public int getItemCount() {

        return mDataList.size();
    }
}
