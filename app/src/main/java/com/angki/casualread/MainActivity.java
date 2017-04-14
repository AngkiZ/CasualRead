package com.angki.casualread;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.angki.casualread.gank.GankFragment;
import com.angki.casualread.gank.WelfareFragment;
import com.angki.casualread.recommend.RecommendFragemnt;
import com.angki.casualread.joke.JokeFragment;
import com.angki.casualread.zhihu.ZhihuFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    //toolbar标题栏
    private Toolbar toolbar;

    //侧滑菜单
    private DrawerLayout drawerLayout;

    //侧滑菜单所用控件
    private NavigationView navigationView;

    //TabLayout标题栏
    private TabLayout tabLayout;

    private ViewPager viewPager;

    //所用到Fragment
    private List<Fragment> fragmentList;

    //TabLyout标题名字
    private List<String> tabTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.drawer_view);
        toolbar = (Toolbar) findViewById(R.id.main_layout_toolbar);
        //设置标题栏
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示导航按钮
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        loadFragment();

        //滑动菜单中的点击事件
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    /**
     * 加载Fragment
     */
    private void loadFragment(){

        tabLayout = (TabLayout) findViewById(R.id.tab_title);
        viewPager = (ViewPager) findViewById(R.id.tab_pager);

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
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager(),
                fragmentList, tabTitle);
        //建立联系
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager, true);
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }
}
