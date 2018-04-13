package com.angki.casualread.mvp.ui.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by tengyu on 2017/4/6.
 * PhotoView中的框架问题，尚未解决，暂时的解决方案是写这个捕获异常
 */

public class HackyProblematicViewGroup extends ViewPager {

    public HackyProblematicViewGroup(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }
}
