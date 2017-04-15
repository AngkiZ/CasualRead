package com.angki.casualread.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.angki.casualread.R;
import com.angki.casualread.gank.GankFragment;
import com.angki.casualread.gank.WelfareFragment;
import com.angki.casualread.joke.JokeFragment;
import com.angki.casualread.main.adpter.MainViewPagerAdapter;
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

        //初始化各个Fragment
        RecommendFragemnt recommendFragemnt = new RecommendFragemnt();//推荐fragment
        ZhihuFragment zhihuFragment = new ZhihuFragment();//知乎fragment
        JokeFragment jokeFragment = new JokeFragment();//笑话fragment
        GankFragment gankFragment = new GankFragment();//Gank fragment
        WelfareFragment welfareFragment = new WelfareFragment();//福利fragment

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
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getChildFragmentManager(),
                fragmentList, tabTitle);
        //建立联系
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager, true);
    }

    static public ViewPager getViewPager() {
        return viewPager;
    }

}
