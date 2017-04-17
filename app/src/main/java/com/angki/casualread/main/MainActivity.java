package com.angki.casualread.main;

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
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.angki.casualread.R;

import static com.angki.casualread.R.id.drawer_layout_collect;
import static com.angki.casualread.R.id.drawer_layout_home;


public class MainActivity extends AppCompatActivity {

    //toolbar标题栏
    private Toolbar toolbar;

    //侧滑菜单
    private DrawerLayout drawerLayout;

    //侧滑菜单所用控件
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        replaceFragment(new HomeFragemnt());

        //滑动菜单中的点击事件
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case drawer_layout_home:
                        replaceFragment(new HomeFragemnt());
                        actionBar.setTitle("瞎读");
                        drawerLayout.closeDrawers();
                        break;
                    case drawer_layout_collect:
                        replaceFragment(new CollectionFragment());
                        actionBar.setTitle("收藏");
                        drawerLayout.closeDrawers();
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
}