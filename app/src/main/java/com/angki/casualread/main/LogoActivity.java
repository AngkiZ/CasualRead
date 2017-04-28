package com.angki.casualread.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


/**
 * 启动log的App，改善用户体验（防止用户在打开App时一片空白）
 * Created by tengyu on 2017/4/28.
 */

public class LogoActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LogoActivity.this, MainActivity.class));
                finish();
            }
        }, 1000);
    }
}
