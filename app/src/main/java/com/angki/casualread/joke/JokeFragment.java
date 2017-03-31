package com.angki.casualread.joke;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.angki.casualread.R;
import com.angki.casualread.joke.adapter.JokeFragmentRecyclerViewAdapter;
import com.angki.casualread.joke.gson.JokeData;
import com.angki.casualread.recommend.adapter.JokeContentAdapter;
import com.angki.casualread.util.Api;
import com.angki.casualread.util.HttpUtil;
import com.angki.casualread.util.Utility;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by tengyu on 2017/3/20.
 */

public class JokeFragment extends Fragment{

    private XRecyclerView jokeRecyclerView;

    private JokeFragmentRecyclerViewAdapter adapter;

    private List<JokeData> dataList = new ArrayList<>();

    private int pager = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.joke_fragment, container, false);

        loadJokeList(view, pager);
        loadModule(view);

        return view;
    }

    /**
     * 加载Joke碎片的内容
     * @param view
     */
    private void loadJokeList(final View view, int pager) {

        String url = Api.JOKE + "?key=f24ebcac31973eebf02a0391b1e8953a&page=" + pager + "&pagesize=20";

        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(view.getContext(), "加载失败..", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                //获取JSON数据
                final String responseData = response.body().string();
                //解析数据
                final List<JokeData> jokeData = Utility.handleJokeResponse(responseData);
                //获取之前集合大小
                int a = dataList.size();
                for (int i = 0; i < jokeData.size(); i++) {
                    dataList.add(i + a, jokeData.get(i));
                }
                //刷新数据
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void loadModule(View view) {

        jokeRecyclerView = (XRecyclerView) view.findViewById(R.id.joke_content);
        //指定加载布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        jokeRecyclerView.setLayoutManager(layoutManager);
        //加载XRecycleView的刷新风格
        jokeRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        jokeRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        jokeRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        //下拉刷新，上拉加载事件
        jokeRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        dataList.clear();
                        pager = 1;
                        loadJokeList(getView(), pager);
                        jokeRecyclerView.refreshComplete();
                    }

                }, 1000);
            }

            @Override
            public void onLoadMore() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pager++;
                        loadJokeList(getView(), pager);
                        jokeRecyclerView.refreshComplete();
                    }
                },1000);
            }
        });
        //加载adapter
        adapter = new JokeFragmentRecyclerViewAdapter(getContext(), dataList);
        jokeRecyclerView.setAdapter(adapter);
    }
}
