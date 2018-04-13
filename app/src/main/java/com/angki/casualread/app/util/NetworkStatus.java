package com.angki.casualread.app.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by tengyu on 2017/4/11.
 * 判断是否有网
 */

public class NetworkStatus {

    public boolean judgment(Context context) {

        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            return true;
        }else {
            return false;
        }
    }
}
