package com.angki.casualread.main;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by tengyu on 2017/4/26.
 * 避免出现InputMethodManager 内存泄露的activity
 */

public class DummyActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                System.exit(0);//彻底退出程序
            }
        }, 500);
    }
}
