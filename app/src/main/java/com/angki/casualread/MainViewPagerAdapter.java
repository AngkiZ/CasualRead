package com.angki.casualread;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by tengyu on 2017/3/20.
 * ViewPager的adapter
 */

public class MainViewPagerAdapter extends FragmentPagerAdapter{

    //fragment列表
    private List<Fragment> list_fragment;
    //tab名的列表
    private List<String> list_title;

    public MainViewPagerAdapter(FragmentManager fm, List<Fragment> list_fragment,
                                List<String> list_title) {

        super(fm);
        this.list_fragment = list_fragment;
        this.list_title = list_title;
    }

    @Override
    public Fragment getItem(int position) {

        return list_fragment.get(position);
    }

    @Override
    public int getCount() {

        return list_title.size();
    }

    //显示tab上的名字
    @Override
    public CharSequence getPageTitle(int position) {

        return list_title.get(position % list_title.size());
    }
}
