package com.angki.casualread.gank;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.angki.casualread.R;
import com.angki.casualread.gank.adapter.GankFragmentRecycleViewAdapter;
import com.angki.casualread.gank.gson.GankData;
import com.angki.casualread.gank.gson.GankDatas;
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

public class GankFragment extends Fragment{

    private XRecyclerView gankRecyclerView;

    private List<GankData> dataList = new ArrayList<>();

    private GankFragmentRecycleViewAdapter adapter;

    private int pager = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.gank_fragment, container, false);

        loadGankList(view, pager);
        loadModule(view);

        return view;
    }

    /**
     * 加载数据
     * @param view
     * @param pager
     */
    private void loadGankList(final View view, int pager) {

        String url = Api.GANK + "10/" + pager;

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
                final GankDatas gankDatas = Utility.handleGankResponse(responseData);
                //获取之前集合大小
                int a = dataList.size();
                for (int i = 0; i < gankDatas.getResults().size(); i++) {
                    dataList.add(i + a, gankDatas.getResults().get(i));
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

    /**
     * 加载组件
     * @param view
     */
    private void loadModule(View view) {

        gankRecyclerView = (XRecyclerView) view.findViewById(R.id.gank_content);
        //指定加载布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        gankRecyclerView.setLayoutManager(layoutManager);
        //加载XRecycleView的刷新风格
        gankRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        gankRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        gankRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        //下拉刷新，上拉加载事件
        gankRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        dataList.clear();
                        pager = 1;
                        loadGankList(getView(), pager);
                        gankRecyclerView.refreshComplete();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pager++;
                        loadGankList(getView(), pager);
                        gankRecyclerView.refreshComplete();
                    }
                },1000);
            }
        });
        //加载adapter
        adapter = new GankFragmentRecycleViewAdapter(getContext(), dataList);
        gankRecyclerView.setAdapter(adapter);
    }

}
