package com.angki.casualread.gank;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.angki.casualread.R;
import com.angki.casualread.gank.adapter.WelfareFragmentRecycleViewAdapter;
import com.angki.casualread.gank.gson.GankWelfareData;
import com.angki.casualread.gank.gson.GankWelfareDatas;
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

public class WelfareFragment extends Fragment{

    private XRecyclerView welfareRecyclerView;

    private List<GankWelfareData> dataList = new ArrayList<>();

    private WelfareFragmentRecycleViewAdapter adapter;

    private int pager = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.welfare_fragment, container, false);

        loadWelfareList(view, pager);
        loadModule(view);
        return view;
    }

    /**
     * 加载数据
     * @param view
     */
    private void loadWelfareList(final View view, int pager) {

        String url = Api.WELFARE + "10/" + pager;

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
                final GankWelfareDatas gankWelfareDatas = Utility.
                        handleGankWelfareResponse(responseData);
                //获取之前集合大小
                int a = dataList.size();
                for (int i = 0; i < gankWelfareDatas.getResults().size(); i++) {
                    dataList.add(i + a, gankWelfareDatas.getResults().get(i));
                }
                Log.d("------", "dataList: " + dataList.size());
                //加载页面
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
     */
    private void loadModule(View view) {

        welfareRecyclerView = (XRecyclerView) view.findViewById(R.id.welfare_content);
        //加载布局
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        welfareRecyclerView.setLayoutManager(layoutManager);
        //加载XRecycleView的刷新风格
        welfareRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        welfareRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        welfareRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        //下拉刷新，上拉加载事件
        welfareRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        dataList.clear();
                        pager = 1;
                        loadWelfareList(getView(), pager);
                        welfareRecyclerView.refreshComplete();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pager++;
                        loadWelfareList(getView(), pager);
                        welfareRecyclerView.refreshComplete();
                    }
                },1000);
            }
        });
        //加载adapter
        adapter = new WelfareFragmentRecycleViewAdapter(getContext(), dataList);
        welfareRecyclerView.setAdapter(adapter);
    }
}
