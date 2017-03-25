package com.angki.casualread.gank;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.angki.casualread.R;
import com.angki.casualread.gank.adapter.WelfareFragmentRecycleViewAdapter;
import com.angki.casualread.gank.gson.GankWelfareDatas;
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

public class WelfareFragment extends Fragment{

    private RecyclerView welfareList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.welfare_fragment, container, false);

        loadWelfareList(view);
        return view;
    }

    private void loadWelfareList(final View view) {

        String url = Api.WELFARE + "10/1";

        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                //获取JSON数据
                final String responseData = response.body().string();
                //解析数据
                final GankWelfareDatas gankWelfareDatas = Utility.
                        handleGankWelfareResponse(responseData);
                //加载页面
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        welfareList = (RecyclerView) view.findViewById(R.id.welfare_content);
                        //加载布局
                        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
                        welfareList.setLayoutManager(layoutManager);
                        //加载adapter
                        WelfareFragmentRecycleViewAdapter adapter =
                                new WelfareFragmentRecycleViewAdapter(getContext(),
                                        gankWelfareDatas.getResults());
                        welfareList.setAdapter(adapter);
                    }
                });
            }
        });
    }
}
