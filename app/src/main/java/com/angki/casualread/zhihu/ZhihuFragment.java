package com.angki.casualread.zhihu;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.angki.casualread.R;
import com.angki.casualread.util.Api;
import com.angki.casualread.util.HttpUtil;
import com.angki.casualread.util.NetworkStatus;
import com.angki.casualread.util.ToastUtil;
import com.angki.casualread.util.Utility;
import com.angki.casualread.util.dbUtil;
import com.angki.casualread.zhihu.adapter.ZhihuFragmentRecycleViewAdapter;
import com.angki.casualread.zhihu.db.dbZhihuNews;
import com.angki.casualread.zhihu.db.dbZhihuNewsDate;
import com.angki.casualread.zhihu.gson.ZhihuDailyNews.NewsBean;
import com.angki.casualread.zhihu.gson.ZhihuDailyNews.NewsBeans;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by tengyu on 2017/3/15.
 */

public class ZhihuFragment extends Fragment{

    private static final String TAG = "ZhihuFragment";

    private XRecyclerView zhihuRecyclerView;

    private LinearLayoutManager layoutManager;

    private List<dbZhihuNews> dataList = new ArrayList<>();

    private ZhihuFragmentRecycleViewAdapter adapter;

    private String url;//请求的地址

    private Calendar c;//获取时间类

    private List<dbZhihuNews> l;//没有网时加载的数据

    private boolean isnetwork;//判断是否有网
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.zhihu_fragment, container, false);
        dataList.clear();//清空列表
        isnetwork = new NetworkStatus().judgment(getContext());
        //加载RecycleView知乎日报数据
        loadZhihuDailyNews(view, true);
        loadModule(view);
        return view;
    }

    /**
     * 加载知乎日报RecycleView新闻数据
     */
    private void loadZhihuDailyNews(final View view,final boolean b){

        if (b) {
            url = Api.ZHIHU_BEFORE + date(true);
            Log.d("------", "url: " + url);
        }else {
            url = Api.ZHIHU_BEFORE + bedate(false);
            Log.d("------", "beurl: " + url);
        }
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                if (b) {
                    int id = DataSupport.select("db_znd_date")
                            .where("db_znd_date like ?", "%" + date(false) + "%")
                            .find(dbZhihuNewsDate.class).get(0).getId();
                    l = DataSupport.where("dbzhihunewsdate_id like ?", "%" + id + "%")
                            .order("listSorting asc")
                            .find(dbZhihuNews.class);
                } else {
                    int id = DataSupport.select("db_znd_date")
                            .where("db_znd_date like ?", "%" + bedate(false) + "%")
                            .find(dbZhihuNewsDate.class).get(0).getId();
                    l = DataSupport.where("dbzhihunewsdate_id like ?", "%" + id + "%")
                            .order("listSorting asc")
                            .find(dbZhihuNews.class);
                }
                //获取之前集合大小
                int a = dataList.size();

                for (int i = 0; i < l.size(); i++) {
                    dataList.add(i + a, l.get(i));
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response)
                    throws IOException {
                //获取Json数据
                final String responseData = response.body().string();

                //解析Json数据
                NewsBeans newsBeans = Utility.handleZHDNResponse(responseData);

                //获取之前集合大小
                int a = dataList.size();

                for (int i = 0; i < newsBeans.getStories().size(); i++) {
                    dbZhihuNews d = new dbZhihuNews();
                    d.setDb_zn_id(newsBeans.getStories().get(i).getId());
                    d.setDb_zn_title(newsBeans.getStories().get(i).getTitle());
                    d.setDb_zn_image(newsBeans.getStories().get(i).getImages().get(0));
                    dataList.add(i + a, d);
                }

                //存储数据
                new dbUtil().dbzhihuSave(newsBeans);

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
        /**
         * 加载RecyclerView
         */
        zhihuRecyclerView = (XRecyclerView) view.findViewById(R.id.zhihu_daily_list);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        zhihuRecyclerView.setHasFixedSize(true);
        //指定布局方式
        layoutManager = new LinearLayoutManager(getContext());
        zhihuRecyclerView.setLayoutManager(layoutManager);
        //加载XRecycleView的刷新风格
        zhihuRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        zhihuRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        zhihuRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        //下拉刷新，上拉加载事件
        zhihuRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            //下拉刷新
            @Override
            public void onRefresh() {
                if (isnetwork) {
                    new Handler().postDelayed(new Runnable(){
                        public void run() {
                            dataList.clear();
                            loadZhihuDailyNews(getView(), true);
                            zhihuRecyclerView.refreshComplete();
                        }

                    }, 1000);
                } else {
                    ToastUtil.showToast(getContext(), "没有网哟~");
                    zhihuRecyclerView.refreshComplete();
                }
            }
            //上拉加载
            @Override
            public void onLoadMore() {

                List<dbZhihuNewsDate> date = DataSupport.select("db_znd_date")
                        .where("db_znd_date like ?", "%" + bedate(true) + "%")
                        .find(dbZhihuNewsDate.class);
                if (date.size() != 0) {
                    new Handler().postDelayed(new Runnable(){
                        public void run() {
                            loadZhihuDailyNews(getView(), false);
                            zhihuRecyclerView.loadMoreComplete();
                        }
                    }, 1000);
                } else {
                    ToastUtil.showToast(getContext(), "已经到底了哟~");
                    zhihuRecyclerView.refreshComplete();
                }
            }
        });

        //加载adpter适配器
        adapter = new ZhihuFragmentRecycleViewAdapter(getContext(), dataList, 1);
        zhihuRecyclerView.setAdapter(adapter);
    }

    /**
     * 今日date
     * @return
     * @boolean b 判断是否有网
     */
    private String date(boolean b){

        c = Calendar.getInstance();
        if (b) {
            /**
             * 有网时，因为数据请求需要，获取今日内容需要的日期是第二天
             */
            c.add(Calendar.DAY_OF_MONTH, +1);
        }
        return Analysis(c);
    }

    /**
     * 之前date
     * @return
     */
    private String bedate(boolean b) {

        if (b) {
            /**
             * 将时间往前一天
             */
            c.add(c.DAY_OF_MONTH, -1);
        }
        return Analysis(c);
    }

    /**
     * 将时间转换成字符串
     * @param c
     * @return
     */
    private String Analysis(Calendar c) {

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DATE);


        if (month < 10 && day < 10) {

            return "" + year + "0" + month + "0" + day;
        }else if (month >= 10 && day < 10) {

            return "" + year + month + "0" + day;
        }else if (month < 10 && day >= 10) {

            return "" + year + "0" + month + day;
        }

        return "" + year + month +day;
    }

}
