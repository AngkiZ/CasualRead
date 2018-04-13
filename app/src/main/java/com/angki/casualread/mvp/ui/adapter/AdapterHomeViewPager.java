package com.angki.casualread.mvp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.List;

/**
 * Created by tengyu on 2017/3/20.
 * ViewPager的adapter
 */

public class AdapterHomeViewPager extends FragmentPagerAdapter{

    //fragment列表
    private List<Fragment> list_fragment;
    //tab名的列表
    private List<String> list_title;

    public AdapterHomeViewPager(FragmentManager fm, List<Fragment> list_fragment,
                                List<String> list_title) {

        super(fm);
        this.list_fragment = list_fragment;
        this.list_title = list_title;
        Log.d("11111", "AdapterHomeViewPager: ");
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
