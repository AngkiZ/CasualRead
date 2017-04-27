package com.angki.casualread.zhihu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angki.casualread.R;
import com.angki.casualread.util.Api;
import com.angki.casualread.util.App;
import com.angki.casualread.util.HttpUtil;
import com.angki.casualread.util.NetworkStatus;
import com.angki.casualread.util.ToastUtil;
import com.angki.casualread.util.Utility;
import com.angki.casualread.util.dbUtil;
import com.angki.casualread.zhihu.adapter.ZhihuFragmentRecycleViewAdapter;
import com.angki.casualread.zhihu.db.dbZhihuNews;
import com.angki.casualread.zhihu.db.dbZhihuNewsDate;
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
    private List<dbZhihuNews> dataList = new ArrayList<>();
    private ZhihuFragmentRecycleViewAdapter adapter;
    private Calendar c_db;//数据库中所需时间
    private Calendar c_net;//请求连接所需时间
    private List<dbZhihuNews> l;//没有网时加载的数据
    private boolean isnetwork;//判断是否有网

    public static ZhihuFragment newInstance() {
        return new ZhihuFragment();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: ");
        c_db = Calendar.getInstance();
        c_net = Calendar.getInstance();
        c_net.add(Calendar.DAY_OF_MONTH, +1);
        //记录最后一次打开软件并且从网络加载保存内容的日期
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(getActivity()).edit();
        editor.putString("date", Analysis(c_db));

        editor.apply();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.zhihu_fragment, container, false);
        dataList.clear();//清空列表
        isnetwork = new NetworkStatus().judgment(getContext());

        if (App.isFirstLoad) {
            Log.d(TAG, "onCreateView: " + App.isFirstLoad);
            //加载RecycleView知乎日报数据
            loadZhihuDailyNews(true);
            loadModule(view);
            App.isFirstLoad = false;
        }else {
            Log.d(TAG, "onCreateView: " + App.isFirstLoad);
            loadModule(view);
            failureZhihu(true, true);
        }

        return view;
    }

    /**
     * 加载知乎日报RecycleView新闻数据
     * boolean b为true时，表示第一次加载，为false时，表示加载更多
     */
    private void loadZhihuDailyNews(final boolean b){
        Log.d(TAG, "loadZhihuDailyNews: date:" + Analysis(c_db));
        //请求的地址
        String url;
        if (b) {
            url = Api.ZHIHU_BEFORE + Analysis(c_net);
        }else {
            url = Api.ZHIHU_BEFORE + Analysis(c_net);
        }
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                failureZhihu(b, false);
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        zhihuRecyclerView.setLayoutManager(layoutManager);
        //加载XRecycleView的刷新风格
        //.setRefreshProgressStyle会引发内存泄露
        //zhihuRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
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
                            loadZhihuDailyNews(true);
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
                c_db.add(c_db.DAY_OF_MONTH, -1);
                c_net.add(c_net.DAY_OF_MONTH, -1);
                List<dbZhihuNewsDate> date = DataSupport.select("db_znd_date", "dbnewscount")
                        .where("db_znd_date like ?", "%" + Analysis(c_db) + "%")
                        .find(dbZhihuNewsDate.class);
                Log.d(TAG, "onLoadMore: date.size:" + date.get(0).getDbnewscount());
                //判断要加载的数据库中是否有,没有就请求
                if (date.size() == 0) {
                    //判断是否有网，有网的话就请求内容，没有的话显示已经到底
                    if (!isnetwork) {
                        new Handler().postDelayed(new Runnable(){
                            public void run() {
                                ToastUtil.showToast(getContext(), "已经到底了哟~");
                                zhihuRecyclerView.refreshComplete();
                            }
                        }, 1000);
                    } else {
                        new Handler().postDelayed(new Runnable(){
                            public void run() {
                                loadZhihuDailyNews(false);
                                zhihuRecyclerView.loadMoreComplete();
                            }
                        }, 1000);
                    }
                    //判断数据库中某日期的内容是否够，不够的话就请求内容，够的话便从数据库中提取
                }else if (date.get(0).getDbnewscount() != 20){
                    new Handler().postDelayed(new Runnable(){
                        public void run() {
                            loadZhihuDailyNews(false);
                            zhihuRecyclerView.loadMoreComplete();
                        }
                    }, 1000);
                }else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            failureZhihu(false, false);
                            zhihuRecyclerView.loadMoreComplete();
                        }
                    }, 1000);
                }
            }
        });

        //加载adpter适配器
        adapter = new ZhihuFragmentRecycleViewAdapter(getContext(), dataList, 1);
        zhihuRecyclerView.setAdapter(adapter);
    }

    /**
     * 从数据库中加载
     * @param b 是否第一次加载
     * @param therd 是否在UI线程中，true为UI线程，flase为请求线程
     */
    private void failureZhihu(boolean b, boolean therd) {
        if (b) {
            //提出记录的日期，按日期加载内容
            SharedPreferences prefer = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String date = prefer.getString("date", null);
            Log.d(TAG, "onFailure: date:" + date);
            int id = DataSupport.select("db_znd_date")
                    .where("db_znd_date like ?", "%" + date + "%")
                    .find(dbZhihuNewsDate.class).get(0).getId();
            l = DataSupport.where("dbzhihunewsdate_id like ?", "%" + id + "%")
                    .order("listSorting asc")
                    .find(dbZhihuNews.class);
        } else {
            int id = DataSupport.select("db_znd_date")
                    .where("db_znd_date like ?", "%" + Analysis(c_db) + "%")
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
        if (therd) {
            adapter.notifyDataSetChanged();
        }else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
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
    /**
     * 销毁所占内存
     */
    private void end() {
        zhihuRecyclerView = null;
        dataList.clear();
        dataList = null;
        adapter.clearMemory();//清除adapter中各个变量的内存
        adapter = null;
        c_db = null;
    }
    @Override
    public void onDestroy() {
        end();//当onDestroy()执行时执行end()方法释放内存
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
}
