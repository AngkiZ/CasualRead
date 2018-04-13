package com.angki.casualread.mvp.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.angki.casualread.R;

public class ActivityAbout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        loadMoudle();

    }

    /**
     * 加载组件
     */
    private void loadMoudle() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_about_toolbar);
        LinearLayout github = (LinearLayout) findViewById(R.id.activity_about_github);
        LinearLayout feedback_bug = (LinearLayout) findViewById(R.id.activity_about_bug);
        LinearLayout data_source_z = (LinearLayout) findViewById(R.id.data_source_zhihu);
        LinearLayout data_source_g = (LinearLayout) findViewById(R.id.data_source_gank);
        LinearLayout data_source_j = (LinearLayout) findViewById(R.id.data_source_joke);
        LinearLayout update = (LinearLayout) findViewById(R.id.activity_about_update);
        //设置标题栏,作为ActionBar显示
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //启用HomeAsUp按钮
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://fir.im/wmst"));
                startActivity(intent);
            }
        });

        github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://github.com/AngkiZ"));
                startActivity(intent);
            }
        });

        feedback_bug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://github.com/AngkiZ/CasualRead/issues"));
                startActivity(intent);
            }
        });

        data_source_z.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://github.com/izzyleung/ZhihuDailyPurify/wiki/%E7%9F%A5%E4%B9%8E%E6%97%A5%E6%8A%A5-API-%E5%88%86%E6%9E%90"));
                startActivity(intent);
            }
        });

        data_source_g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://gank.io/api"));
                startActivity(intent);
            }
        });

        data_source_j.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.juhe.cn/"));
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

}
