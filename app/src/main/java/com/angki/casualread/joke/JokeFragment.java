package com.angki.casualread.joke;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angki.casualread.R;
import com.angki.casualread.joke.adapter.JokeFragmentRecyclerViewAdapter;
import com.angki.casualread.joke.gson.JokeData;
import com.angki.casualread.recommend.adapter.JokeContentAdapter;
import com.angki.casualread.util.Api;
import com.angki.casualread.util.HttpUtil;
import com.angki.casualread.util.Utility;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by tengyu on 2017/3/20.
 */

public class JokeFragment extends Fragment{

    private RecyclerView jokeList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.joke_fragment, container, false);

        loadJokeList(view);

        return view;
    }

    /**
     * 加载Joke碎片的内容
     * @param view
     */
    private void loadJokeList(final View view) {

        String url = Api.JOKE + "?key=f24ebcac31973eebf02a0391b1e8953a&page=1&pagesize=20";

        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                //获取JSON数据
                final String responseData = response.body().string();
                //解析数据
                final List<JokeData> jokeData = Utility.handleJokeResponse(responseData);
                //加载数据
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        jokeList = (RecyclerView) view.findViewById(R.id.joke_content);
                        //指定加载布局
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                        jokeList.setLayoutManager(layoutManager);
                        //加载adapter
                        JokeFragmentRecyclerViewAdapter adapter = new JokeFragmentRecyclerViewAdapter(getContext(), jokeData);
                        jokeList.setAdapter(adapter);
                    }
                });
            }
        });
    }
}
