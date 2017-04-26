package com.angki.casualread.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angki.casualread.R;
import com.angki.casualread.gank.GankFragment;
import com.angki.casualread.gank.WelfareFragment;
import com.angki.casualread.joke.JokeFragment;
import com.angki.casualread.main.adpter.HomeViewPagerAdapter;
import com.angki.casualread.recommend.RecommendFragemnt;
import com.angki.casualread.zhihu.ZhihuFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tengyu on 2017/4/15.
 */

public class HomeFragemnt extends Fragment{

    //所用到Fragment
    private List<Fragment> fragmentList;

    //TabLyout标题名字
    private List<String> tabTitle;

    static private ViewPager viewPager;

    private TabLayout tabLayout;

    private RecommendFragemnt recommendFragemnt;
    private ZhihuFragment zhihuFragment;
    private JokeFragment jokeFragment;
    private GankFragment gankFragment;
    private WelfareFragment welfareFragment;
    private HomeViewPagerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //判断是否已有保存的内容，有的话直接加载，没有在获得新对象
        if (savedInstanceState != null){
            FragmentManager manager = getChildFragmentManager();
            recommendFragemnt = (RecommendFragemnt) manager.getFragment(savedInstanceState, "recommand");
            zhihuFragment = (ZhihuFragment) manager.getFragment(savedInstanceState, "zhihu");
            gankFragment = (GankFragment) manager.getFragment(savedInstanceState, "gank");
            jokeFragment = (JokeFragment) manager.getFragment(savedInstanceState, "joke");
            welfareFragment = (WelfareFragment) manager.getFragment(savedInstanceState, "welfare");
        }else {
            recommendFragemnt = RecommendFragemnt.newInstance();
            zhihuFragment = ZhihuFragment.newInstance();
            gankFragment = GankFragment.newInstance();
            jokeFragment = JokeFragment.newInstance();
            welfareFragment = WelfareFragment.newInstance();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_home_fragment, container, false);

        loadFragment(view);
        return view;
    }

    /**
     * 加载Fragment
     */
    private void loadFragment(View view){

        tabLayout = (TabLayout) view.findViewById(R.id.tab_title);
        viewPager = (ViewPager) view.findViewById(R.id.tab_pager);

        //将Fragment填入列表
        fragmentList = new ArrayList<>();
        fragmentList.add(recommendFragemnt);
        fragmentList.add(zhihuFragment);
        fragmentList.add(gankFragment);
        fragmentList.add(jokeFragment);
        fragmentList.add(welfareFragment);

        //将名称加载tab名字列表
        tabTitle = new ArrayList<>();
        tabTitle.add("推荐");
        tabTitle.add("知乎");
        tabTitle.add("干货");
        tabTitle.add("笑话");
        tabTitle.add("福利");

        //加载适配器
        adapter = new HomeViewPagerAdapter(getChildFragmentManager(),
                fragmentList, tabTitle);
        //建立联系
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager, true);
        //设置缓存碎片页数
        viewPager.setOffscreenPageLimit(4);
    }

    static public ViewPager getViewPager() {
        return viewPager;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        FragmentManager manager = getChildFragmentManager();
        manager.putFragment(outState, "recommand", recommendFragemnt);
        manager.putFragment(outState, "zhihu", zhihuFragment);
        manager.putFragment(outState, "gank", gankFragment);
        manager.putFragment(outState, "joke", jokeFragment);
        manager.putFragment(outState, "welfare", welfareFragment);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter = null;
        viewPager = null;
    }
}
