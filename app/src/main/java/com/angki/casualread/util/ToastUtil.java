package com.angki.casualread.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by tengyu on 2017/4/6.
 */

public class ToastUtil {

    private static Toast mToast;

    public static void showToast(Context context, String text) {

        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }
        mToast.setText(text);
        mToast.show();
    }
}