package com.angki.casualread.app.util;

import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import com.squareup.leakcanary.LeakCanary;

import org.litepal.LitePalApplication;

/**
 * Created by tengyu on 2017/4/19.
 * 继承LitePalApplication，增加初始化主题
 */

public class App extends LitePalApplication{
    private static final String TAG = "App";

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT > 23) {
            LeakCanary.install(this);
        }
        SharedPreferences prefs = getSharedPreferences("ThemeData", MODE_PRIVATE);
        Log.d(TAG, "prefs: " + prefs.getInt("theme", 0));
        if (prefs != null) {
            switch (prefs.getInt("theme", 0)) {
                case 1:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    break;
                case 2:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    break;
            }
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

}
