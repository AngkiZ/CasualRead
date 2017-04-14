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
import com.angki.casualread.gank.db.dbGank;
import com.angki.casualread.gank.db.dbWelfare;
import com.angki.casualread.gank.gson.GankWelfareData;
import com.angki.casualread.gank.gson.GankWelfareDatas;
import com.angki.casualread.util.Api;
import com.angki.casualread.util.HttpUtil;
import com.angki.casualread.util.NetworkStatus;
import com.angki.casualread.util.ToastUtil;
import com.angki.casualread.util.Utility;
import com.angki.casualread.util.dbUtil;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.litepal.crud.DataSupport;

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

    private List<dbWelfare> dataList = new ArrayList<>();

    private WelfareFragmentRecycleViewAdapter adapter;

    private dbWelfare mdbWelfare;

    private int pager;

    private boolean isnetwork;//判断是否有网

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.welfare_fragment, container, false);
        dataList.clear();//清空列表
        pager = 1;
        isnetwork = new NetworkStatus().judgment(getContext());
        loadWelfareList(view, pager);
        loadModule(view);
        return view;
    }

    /**
     * 加载数据
     * @param view
     */
    private void loadWelfareList(final View view, final int pager) {

        String url = Api.WELFARE + "10/" + pager;

        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //获取指定页数的内容集合
                List<dbWelfare> welfareList = DataSupport
                            .where("db_we_pager like ?", "%" + pager + "%")
                            .order("db_we_listSorting asc").find(dbWelfare.class);
                //获取之前集合大小
                int a = dataList.size();
                for (int i = 0; i < welfareList.size(); i++) {
                    dataList.add(i + a, welfareList.get(i));
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
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
                    mdbWelfare = new dbWelfare();
                    mdbWelfare.setDb_we_id(gankWelfareDatas.getResults().get(i).get_id());
                    mdbWelfare.setDb_we_title(gankWelfareDatas.getResults().get(i).getDesc());
                    mdbWelfare.setDb_we_url(gankWelfareDatas.getResults().get(i).getUrl());
                    mdbWelfare.setDb_we_pager(pager);
                    mdbWelfare.setDb_we_listSorting(i);
                    dataList.add(i + a, mdbWelfare);
                }
                //存储数据
                new dbUtil().dbwelfareSave(gankWelfareDatas, pager);
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
                if (isnetwork) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dataList.clear();
                            pager = 1;
                            loadWelfareList(getView(), pager);
                            welfareRecyclerView.refreshComplete();
                        }
                    }, 1000);
                }else {
                    ToastUtil.showToast(getContext(), "没有网哟~");
                    welfareRecyclerView.refreshComplete();
                }
            }

            @Override
            public void onLoadMore() {
                pager++;
                List<dbWelfare> welfareList = DataSupport
                        .where("db_we_pager like ?", "%" + pager + "%")
                        .order("db_we_listSorting asc").find(dbWelfare.class);
                if (welfareList.size() == 0 && isnetwork == false) {
                    ToastUtil.showToast(getContext(), "已经到底了哟~");
                    welfareRecyclerView.refreshComplete();
                }else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadWelfareList(getView(), pager);
                            welfareRecyclerView.refreshComplete();
                        }
                    },1000);
                }
            }
        });
        //加载adapter
        adapter = new WelfareFragmentRecycleViewAdapter(getContext(), dataList);
        welfareRecyclerView.setAdapter(adapter);
    }

}
