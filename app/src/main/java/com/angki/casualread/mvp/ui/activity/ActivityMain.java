package com.angki.casualread.mvp.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.angki.casualread.app.util.FragmentUtils;
import com.angki.casualread.mvp.ui.fragment.FragmentHome;
import com.angki.casualread.mvp.ui.fragment.FragmentCollection;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;

import butterknife.BindView;

import static com.angki.casualread.R.id.drawer_layout_about;
import static com.angki.casualread.R.id.drawer_layout_collect;
import static com.angki.casualread.R.id.drawer_layout_home;
import static com.angki.casualread.R.id.drawer_layout_theme;

/**
 * 主页
 * @author :Angki
 * @date : 2018-04-16-14-54
 */
public class ActivityMain extends BaseActivity {

    /**
     * 侧滑菜单
     */
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    /**
     * 侧滑菜单所用控件
     */
    @BindView(R.id.drawer_view)
    NavigationView mNavigationView;
    /**
     * 标题栏
     */
    @BindView(R.id.main_layout_toolbar)
    Toolbar mToolbar;
    /**
     * 主碎片
     */
    private FragmentHome mFragmentHome;
    /**
     * 收藏碎片
     */
    private FragmentCollection mFragmentCollection;
    /**
     * 当前展示Fragment
     */
    private Fragment mFragment;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //判断是否保存数据,获取Fragment对象
        if (savedInstanceState != null) {
            mFragmentHome = (FragmentHome) getSupportFragmentManager().getFragment(savedInstanceState, "HomeFragment");
            mFragmentCollection = (FragmentCollection) getSupportFragmentManager().getFragment(savedInstanceState, "CollcetionFragment");
        }else {
            mFragmentHome = FragmentHome.newInstance(1);
            mFragmentCollection = FragmentCollection.newInstance(1);
        }
        FragmentUtils.addFragment(getSupportFragmentManager(), mFragmentHome, R.id.activity_main_fragment);
        mFragment = mFragmentHome;
        initView();
    }

    /**
     * 加载组件
     */
    private void initView() {

        //设置标题栏
        setSupportActionBar(mToolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //显示导航按钮
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        //滑动菜单中的点击事件
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d(TAG, "onNavigationItemSelected: ");
                mDrawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case drawer_layout_home:
                        if (!(mFragment instanceof FragmentHome)) {
                            //判断主页面碎片是否有实例，没有的话就新建一个
                            if (mFragmentHome == null) {
                                mFragmentHome = FragmentHome.newInstance(1);
                            }
                            FragmentUtils.replaceFragment(mFragment, mFragmentHome, true);
                            actionBar.setTitle("瞎读");
                        }
                        break;
                    case drawer_layout_collect:
                        //判断收藏碎片是否有实例，没有的话就新建一个
                        if (!(mFragment instanceof FragmentCollection)) {
                            if (mFragmentCollection == null) {
                                mFragmentCollection = FragmentCollection.newInstance(1);
                            }
                            FragmentUtils.replaceFragment(mFragment, mFragmentCollection, true);
                            actionBar.setTitle("收藏");
                        }
                        break;
                    case drawer_layout_theme:

                        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
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
                                //记录下当前主题，下一次打开App保持当前主题
                                SharedPreferences.Editor editor = getSharedPreferences("ThemeData", MODE_PRIVATE).edit();
                                //根据当前主题来执行不同的主题设置
                                if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                                    editor.putInt("theme", 1);
                                }else {
                                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
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
                        Intent intent = new Intent(ActivityMain.this, ActivityAbout.class);
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
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 保存临时的Fragment的数据
     * @param outState 保存的数据
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //判断homeFragment是否被添加
        if (mFragmentHome.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "HomeFragment", mFragmentHome);
        }
        //判断collectionFragment是否被添加，且判断collectionFragment是否为空
        if (mFragmentCollection != null && mFragmentCollection.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "CollcetionFragment", mFragmentCollection);
        }
    }

    /**
     * 销毁所占内存
     */
    private void end() {
        mDrawerLayout = null;
    }

    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
        }else {
            end();
            startActivity(new Intent(this, ActivityDummy.class));
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
