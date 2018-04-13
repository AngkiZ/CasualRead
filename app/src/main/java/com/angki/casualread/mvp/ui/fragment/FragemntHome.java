package com.angki.casualread.mvp.ui.fragment;

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
import com.angki.casualread.mvp.ui.adapter.AdapterHomeViewPager;
import com.angki.casualread.app.util.App;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tengyu on 2017/4/15.
 */

public class FragemntHome extends Fragment{

    //所用到Fragment
    private List<Fragment> fragmentList;

    //TabLyout标题名字
    private List<String> tabTitle;

    static private ViewPager viewPager;

    private TabLayout tabLayout;

    private FragemntRecommend fragemntRecommend;
    private FragmentZhihu fragmentZhihu;
    private FragmentJoke fragmentJoke;
    private FragmentGank fragmentGank;
    private FragmentWelfare fragmentWelfare;
    private AdapterHomeViewPager adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //判断是否已有保存的内容，有的话直接加载，没有在获得新对象
        if (savedInstanceState != null){
            FragmentManager manager = getChildFragmentManager();
            fragemntRecommend = (FragemntRecommend) manager.getFragment(savedInstanceState, "recommand");
            fragmentZhihu = (FragmentZhihu) manager.getFragment(savedInstanceState, "zhihu");
            fragmentGank = (FragmentGank) manager.getFragment(savedInstanceState, "gank");
            fragmentJoke = (FragmentJoke) manager.getFragment(savedInstanceState, "joke");
            fragmentWelfare = (FragmentWelfare) manager.getFragment(savedInstanceState, "welfare");
        }else {
            fragemntRecommend = FragemntRecommend.newInstance();
            fragmentZhihu = FragmentZhihu.newInstance();
            fragmentGank = FragmentGank.newInstance();
            fragmentJoke = FragmentJoke.newInstance();
            fragmentWelfare = FragmentWelfare.newInstance();
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
        fragmentList.add(fragemntRecommend);
        fragmentList.add(fragmentZhihu);
        fragmentList.add(fragmentGank);
        fragmentList.add(fragmentJoke);
        fragmentList.add(fragmentWelfare);

        //将名称加载tab名字列表
        tabTitle = new ArrayList<>();
        tabTitle.add("推荐");
        tabTitle.add("知乎");
        tabTitle.add("干货");
        tabTitle.add("笑话");
        tabTitle.add("福利");

        //加载适配器
        adapter = new AdapterHomeViewPager(getChildFragmentManager(),
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
        manager.putFragment(outState, "recommand", fragemntRecommend);
        manager.putFragment(outState, "zhihu", fragmentZhihu);
        manager.putFragment(outState, "gank", fragmentGank);
        manager.putFragment(outState, "joke", fragmentJoke);
        manager.putFragment(outState, "welfare", fragmentWelfare);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        App.isFirstLoad = false;//当主页碎片退出时，意味着App不再是第一次加载
        adapter = null;
        viewPager = null;
    }
}
