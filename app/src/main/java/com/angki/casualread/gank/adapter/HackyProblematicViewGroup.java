package com.angki.casualread.gank.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by tengyu on 2017/4/6.
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
