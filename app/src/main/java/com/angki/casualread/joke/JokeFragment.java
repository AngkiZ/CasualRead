package com.angki.casualread.joke;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.angki.casualread.R;
import com.angki.casualread.joke.adapter.JokeFragmentRecyclerViewAdapter;
import com.angki.casualread.joke.db.dbJoke;
import com.angki.casualread.joke.gson.JokeData;
import com.angki.casualread.recommend.adapter.JokeContentAdapter;
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

public class JokeFragment extends Fragment{

    private XRecyclerView jokeRecyclerView;
    private JokeFragmentRecyclerViewAdapter adapter;
    private List<dbJoke> dataList = new ArrayList<>();
    private dbJoke mdbJoke;
    private boolean isnetwork;//判断是否有网
    private int page;

    public static JokeFragment newInstance() {
        return new JokeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.joke_fragment, container, false);
        dataList.clear();//清空列表
        page = 1;
        isnetwork = new NetworkStatus().judgment(getContext());
        loadJokeList(page);
        loadModule(view);

        return view;
    }

    /**
     * 加载Joke碎片的内容
     */
    private void loadJokeList(final int page) {

        String url = Api.JOKE + "?key=f24ebcac31973eebf02a0391b1e8953a&page=" + page + "&pagesize=20";

        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //获取指定页数的内容集合
                List<dbJoke> jokeList = DataSupport
                        .where("db_joke_page like ?", "%" + page + "%")
                        .order("db_joke_listSorting asc")
                        .find(dbJoke.class);
                //获取之前集合大小
                int a = dataList.size();
                for (int i = 0; i < jokeList.size(); i++) {
                    dataList.add(i + a, jokeList.get(i));
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
                final List<JokeData> jokeData = Utility.handleJokeResponse(responseData);
                //获取之前集合大小
                int a = dataList.size();
                for (int i = 0; i < jokeData.size(); i++) {
                    mdbJoke = new dbJoke();
                    mdbJoke.setDb_joke_id(jokeData.get(i).getHashId());
                    mdbJoke.setDb_joke_content(jokeData.get(i).getContent());
                    mdbJoke.setDb_joke_listSorting(i);
                    mdbJoke.setDb_joke_page(page);
                    dataList.add(i + a, mdbJoke);
                }
                //存储数据
                new dbUtil().dbjokeSave(jokeData, page);
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

        jokeRecyclerView = (XRecyclerView) view.findViewById(R.id.joke_content);
        //指定加载布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        jokeRecyclerView.setLayoutManager(layoutManager);
        //加载XRecycleView的刷新风格
        jokeRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        jokeRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        //下拉刷新，上拉加载事件
        jokeRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

                if (isnetwork) {
                    new Handler().postDelayed(new Runnable(){
                        public void run() {
                            dataList.clear();
                            page = 1;
                            loadJokeList(page);
                            jokeRecyclerView.refreshComplete();
                        }

                    }, 1000);
                }else {
                    ToastUtil.showToast(getContext(), "没有网哟~");
                    jokeRecyclerView.refreshComplete();
                }
            }

            @Override
            public void onLoadMore() {
                page++;
                List<dbJoke> jokeList = DataSupport.select("db_joke_page")
                        .where("db_joke_page like ?", "%" + page + "%")
                        .find(dbJoke.class);
                if (jokeList.size() == 0 && isnetwork == false) {
                    ToastUtil.showToast(getContext(), "已经到底了哟~");
                    jokeRecyclerView.refreshComplete();
                }else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadJokeList(page);
                            jokeRecyclerView.refreshComplete();
                        }
                    },1000);
                }
            }
        });
        //加载adapter
        adapter = new JokeFragmentRecyclerViewAdapter(dataList);
        jokeRecyclerView.setAdapter(adapter);
    }

    /**
     * 销毁所占内存
     */
    private void end() {
        jokeRecyclerView = null;
        dataList.clear();
        dataList = null;
        adapter.clearMemory();
        adapter = null;
        mdbJoke = null;
    }

    @Override
    public void onDestroy() {
        end();
        super.onDestroy();
    }
}
