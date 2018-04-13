package com.angki.casualread.app.util;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by tengyu on 2017/3/26.
 * 设置间距的一个工具类
 */

public class MarginUtil {

    public static ViewGroup.LayoutParams
    setViewMargin(View view, int left, int top, int right, int bottom) {

        ViewGroup.LayoutParams params = view.getLayoutParams();
        ViewGroup.MarginLayoutParams marginParams = null;

        //获取view的margin设置参数
        if (params instanceof ViewGroup.MarginLayoutParams) {
            marginParams = (ViewGroup.MarginLayoutParams) params;
        } else {
            //不存在时创建一个新的参数
            marginParams = new ViewGroup.MarginLayoutParams(params);
        }

        marginParams.setMargins(left, top, right, bottom);

        return marginParams;
    }
}
