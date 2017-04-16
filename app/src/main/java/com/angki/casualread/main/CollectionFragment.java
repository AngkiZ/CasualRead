package com.angki.casualread.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.angki.casualread.R;
import com.angki.casualread.gank.db.dbGank;
import com.angki.casualread.main.adpter.CollectionRecyclerAdapter;
import com.angki.casualread.zhihu.db.dbZhihuNews;
import com.angki.casualread.zhihu.db.dbZhihuStors;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tengyu on 2017/4/15.
 */

public class CollectionFragment extends Fragment{

    private List<String> titles;
    //被收藏的知乎新闻
    private List<dbZhihuNews> collection_zhihu_list = new ArrayList<>();
    //被收藏的干货
    private List<dbGank> collection_gank_list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_collection_fragment, container, false);

        loadMould(view);
        return view;
    }

    private void loadMould(View view) {

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.collection_recyclerView);

        // 设置启动列表的修改动画效果(默认为关闭状态)
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(true);
        }
        // 设置动画时长
        recyclerView.getItemAnimator().setChangeDuration(100);
        recyclerView.getItemAnimator().setMoveDuration(100);

        // 实现RecyclerView实现竖向列表展示模式
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        loadData();
        // 实例化数据适配器并绑定在控件上
        CollectionRecyclerAdapter adapter = new
                CollectionRecyclerAdapter
                (getContext(), titles, collection_zhihu_list, collection_gank_list);
        recyclerView.setAdapter(adapter);
    }

    private void loadData() {

        collection_zhihu_list = DataSupport.where("db_zn_collection = ?", "1")
                .find(dbZhihuNews.class);
        collection_gank_list = DataSupport.where("db_gank_collection = ?", "1")
                .find(dbGank.class);
        titles = new ArrayList<>();
        titles.add("知乎收藏");
        titles.add("干货收藏");
    }
}
