package com.angki.casualread.main;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.angki.casualread.R;

import static com.angki.casualread.R.id.drawer_layout_collect;
import static com.angki.casualread.R.id.drawer_layout_home;
import static com.angki.casualread.R.id.drawer_layout_theme;



public class MainActivity extends AppCompatActivity {

    //toolbar标题栏
    private Toolbar toolbar;
    //侧滑菜单
    private DrawerLayout drawerLayout;
    //侧滑菜单所用控件
    private NavigationView navigationView;
    //主碎片
    private HomeFragemnt homeFragemnt;
    //收藏碎片
    private CollectionFragment collectionFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //判断是否有保存数据
        if (savedInstanceState != null) {
            homeFragemnt = (HomeFragemnt) getSupportFragmentManager().getFragment(savedInstanceState, "HomeFragment");
            collectionFragment = (CollectionFragment) getSupportFragmentManager().getFragment(savedInstanceState, "CollcetionFragment");
        } else {
            homeFragemnt = new HomeFragemnt();
        }

        loadMoudle();
    }

    /**
     * 加载组件
     */
    private void loadMoudle() {

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.drawer_view);
        toolbar = (Toolbar) findViewById(R.id.main_layout_toolbar);
        //设置标题栏
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示导航按钮
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        replaceFragment(homeFragemnt);

        //滑动菜单中的点击事件
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case drawer_layout_home:
                        replaceFragment(homeFragemnt);
                        actionBar.setTitle("瞎读");
                        break;
                    case drawer_layout_collect:
                        if (collectionFragment == null) {
                            collectionFragment = new CollectionFragment();
                        }
                        actionBar.setTitle("收藏");
                        replaceFragment(collectionFragment);
                        break;
                    case drawer_layout_theme:
                        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
                            @Override
                            public void onDrawerSlide(View drawerView, float slideOffset) {

                            }

                            @Override
                            public void onDrawerOpened(View drawerView) {

                            }

                            @Override
                            public void onDrawerClosed(View drawerView) {
                                int currentNightMode = getResources().getConfiguration().uiMode
                                        & Configuration.UI_MODE_NIGHT_MASK;
                                SharedPreferences.Editor editor = getSharedPreferences("ThemeData", MODE_PRIVATE).edit();
                                if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                                    editor.putInt("theme", 1);
                                }else {
                                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                                    editor.putInt("theme", 2);
                                }
                                Log.d("theme", "onDrawerClosed: ");
                                getWindow().setWindowAnimations(R.style.WindowAnimationFadeInOut);
                                editor.apply();
                                recreate();
                            }

                            @Override
                            public void onDrawerStateChanged(int newState) {

                            }
                        });
                        break;
                }
                return true;
            }
        });
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

    /**
     * 动态切换fragment
     * @param fragment
     */
    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        //启动编辑操作
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.activity_main_fragment, fragment);
        transaction.commit();
    }

    /**
     * 保存临时的Fragment的数据
     * @param outState 保存的数据
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("------", "onSaveInstanceState: ");
        //判断homeFragemnt是否被添加
        if (homeFragemnt.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "HomeFragment", homeFragemnt);
        }
        //判断collectionFragment是否被添加，且判断collectionFragment是否为空
        if (collectionFragment != null && collectionFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "CollcetionFragment", collectionFragment);
        }
    }

}
