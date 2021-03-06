package com.angki.casualread.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.angki.casualread.R;
import com.angki.casualread.mvp.model.entity.result.JokeData;
import com.angki.casualread.app.util.MarginUtil;

import java.util.List;

/**
 * Created by tengyu on 2017/3/23.
 */

public class AdapterJokeContent extends RecyclerView.Adapter<AdapterJokeContent.ViewHolder>{

    private final Context mcontext;

    private List<JokeData> mDataList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView content;

        public ViewHolder(View view) {

            super(view);
            cardView = (CardView) view;
            content = (TextView) view.findViewById(R.id.recommand_joke_recyclerview_content);
        }
    }

    public AdapterJokeContent(Context context, List<JokeData> dataList) {

        this.mcontext = context;

        this.mDataList = dataList;
    }

    @Override
    public AdapterJokeContent.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recommand_content_joke, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterJokeContent.ViewHolder holder, int position) {

        if (position == 0 ) {
            MarginUtil.setViewMargin(holder.cardView, 0, 0, 0, 8);
        }else if (position == mDataList.size()) {
            MarginUtil.setViewMargin(holder.cardView, 0, 8, 0, 0);
        }else {
            MarginUtil.setViewMargin(holder.cardView, 0, 8, 0, 8);
        }
        JokeData jokeData = mDataList.get(position);
        holder.content.setText(jokeData.getContent());
    }

    @Override
    public int getItemCount() {

        return mDataList.size();
    }
}
