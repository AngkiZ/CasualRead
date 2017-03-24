package com.angki.casualread.gank;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angki.casualread.R;
import com.angki.casualread.gank.adapter.GankFragmentRecycleViewAdapter;
import com.angki.casualread.gank.gson.GankDatas;
import com.angki.casualread.util.Api;
import com.angki.casualread.util.HttpUtil;
import com.angki.casualread.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by tengyu on 2017/3/20.
 */

public class GankFragment extends Fragment{

    private RecyclerView gankList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.gank_fragment, container, false);

        loadGankList(view);

        return view;
    }

    private void loadGankList(final View view) {

        String url = Api.GANK + "10/1";

        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                //获取JSON数据
                final String responseData = response.body().string();
                //解析数据
                final GankDatas gankDatas = Utility.handleGankResponse(responseData);
                //加载界面
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        gankList = (RecyclerView) view.findViewById(R.id.gank_content);
                        //指定加载布局
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                        gankList.setLayoutManager(layoutManager);
                        //加载adapter
                        GankFragmentRecycleViewAdapter adapter =
                                new GankFragmentRecycleViewAdapter(getContext(), gankDatas.getResults());
                        gankList.setAdapter(adapter);
                    }
                });

            }
        });
    }
}
