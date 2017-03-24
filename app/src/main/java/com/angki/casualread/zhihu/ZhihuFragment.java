package com.angki.casualread.zhihu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.angki.casualread.R;
import com.angki.casualread.util.Api;
import com.angki.casualread.util.HttpUtil;
import com.angki.casualread.util.Utility;
import com.angki.casualread.zhihu.adapter.ZhihuFragmentRecycleViewAdapter;
import com.angki.casualread.zhihu.gson.ZhihuDailyNews.NewsBeans;
import com.youth.banner.Banner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by tengyu on 2017/3/15.
 */

public class ZhihuFragment extends Fragment{

    private static final String TAG = "ZhihuFragment";

    private Banner banner;

    private RecyclerView zhihuRecyclerView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.zhihu_fragment, container, false);

        //加载RecycleView知乎日报列表
        loadZhihuDailyNews(view);

        return view;
    }

    /**
     * 加载知乎日报RecycleView新闻列表
     */
    private void loadZhihuDailyNews(final View view){

        String url = Api.ZHIHU_NEWS + "latest";
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
            public void onResponse(Call call, Response response)
                    throws IOException {

                //获取Json数据
                final String responseData = response.body().string();
                //解析Json数据
                final NewsBeans newsBeans = Utility.handleZHDNResponse(responseData);

                final List<String> images = new ArrayList<String>();
                final List<String> titles = new ArrayList<String>();
                for(int i = 0; i < newsBeans.getTopStories().size(); i++){

                    images.add(newsBeans.getTopStories().get(i).getImage());
                    titles.add(newsBeans.getTopStories().get(i).getTitle());
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        /**
                         * 加载RecyclerView
                         */
                        zhihuRecyclerView = (RecyclerView) view.findViewById(R.id.zhihu_daily_list);
                        //指定布局方式
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                        zhihuRecyclerView.setLayoutManager(layoutManager);
                        //加载adpter适配器
                        ZhihuFragmentRecycleViewAdapter adapter = new ZhihuFragmentRecycleViewAdapter(getContext(), newsBeans.getStories());
                        zhihuRecyclerView.setAdapter(adapter);


                    }
                });
            }
        });
    }
}
