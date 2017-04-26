package com.angki.casualread.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
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
import com.angki.casualread.about.AboutActivity;

import static com.angki.casualread.R.id.drawer_layout_about;
import static com.angki.casualread.R.id.drawer_layout_collect;
import static com.angki.casualread.R.id.drawer_layout_home;
import static com.angki.casualread.R.id.drawer_layout_theme;



public class MainActivity extends AppCompatActivity {

    private static final String TAG = "!MainActivity";
    //侧滑菜单
    private DrawerLayout drawerLayout;
    //主碎片
    private HomeFragemnt homeFragemnt;
    //收藏碎片
    private CollectionFragment collectionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_main);
        //判断是否有保存数据
        if (savedInstanceState != null) {
            homeFragemnt = (HomeFragemnt) getSupportFragmentManager().getFragment(savedInstanceState, "HomeFragment");
            collectionFragment = (CollectionFragment) getSupportFragmentManager().getFragment(savedInstanceState, "CollcetionFragment");
        } else {
            homeFragemnt = new HomeFragemnt();
            collectionFragment = new CollectionFragment();
        }
        //加载组件
        loadMoudle();
    }

    /**
     * 加载组件
     */
    private void loadMoudle() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //侧滑菜单所用控件
        NavigationView navigationView = (NavigationView) findViewById(R.id.drawer_view);
        //标题栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_layout_toolbar);
        //设置标题栏
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示导航按钮
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        //在收藏碎片切换主题时，homeFragemnt为null，然后把标题改为收藏
        if (homeFragemnt != null) {
            replaceFragment(homeFragemnt);
        }else {
            actionBar.setTitle("收藏");
        }
        //滑动菜单中的点击事件
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d(TAG, "onNavigationItemSelected: ");
                drawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case drawer_layout_home:
                        //判断主页面碎片是否有实例，没有的话就新建一个
                        if (homeFragemnt == null) {
                            homeFragemnt = new HomeFragemnt();
                        }
                        replaceFragment(homeFragemnt);
                        actionBar.setTitle("瞎读");
                        break;
                    case drawer_layout_collect:
                        //判断收藏碎片是否有实例，没有的话就新建一个
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
                                //设置切换主题时的动画过渡效果
                                getWindow().setWindowAnimations(R.style.WindowAnimationFadeInOut);
                                //获取当前主题
                                int currentNightMode = getResources().getConfiguration().uiMode
                                        & Configuration.UI_MODE_NIGHT_MASK;
                                Log.d("asd", "c2: " + currentNightMode);
                                //记录下当前主题，下一次打开App保持当前主题
                                SharedPreferences.Editor editor = getSharedPreferences("ThemeData", MODE_PRIVATE).edit();
                                //根据当前主题来执行不同的主题设置
                                if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                                    getDelegate().setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                                    editor.putInt("theme", 1);
                                }else {
                                    getDelegate().setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                                    editor.putInt("theme", 2);
                                }
                                editor.apply();
                                recreate();//刷新Activity
                            }

                            @Override
                            public void onDrawerStateChanged(int newState) {

                            }
                        });
                        break;
                    case drawer_layout_about:
                        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });
    }

    /**
     * 继承ActionBar上的点击事件
     * @param item
     * @return
     */
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
        Log.d(TAG, "onSaveInstanceState: ");
        //判断homeFragemnt是否被添加
        if (homeFragemnt.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "HomeFragment", homeFragemnt);
        }
        //判断collectionFragment是否被添加，且判断collectionFragment是否为空
        if (collectionFragment != null && collectionFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "CollcetionFragment", collectionFragment);
        }
    }

    /**
     * 销毁所占内存
     */
    private void end() {
        drawerLayout = null;
        homeFragemnt = null;
        collectionFragment = null;
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ");
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        }else {
            end();
            startActivity(new Intent(this, DummyActivity.class));
            finish();
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        end();
    }
}
