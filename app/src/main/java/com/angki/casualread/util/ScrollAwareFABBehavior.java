package com.angki.casualread.util;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by tengyu on 2017/4/17.
 */

public class ScrollAwareFABBehavior extends FloatingActionButton.Behavior{

    /**
     *
     * @param context 上下文
     * @param attrs 通过AttributeSet可以获得布局文件中定义的所有属性的key和value
     */
    public ScrollAwareFABBehavior(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,
                                       FloatingActionButton child, View directTargetChild,
                                       View target, int nestedScrollAxes) {
        //确保我们对垂直滚动做出反应
        //ViewCompat.SCROLL_AXIS_VERTICAL垂直滚动
        Log.d("ScrollAwareFABBehavior", "onStartNestedScroll:1");
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL ||
                super.onStartNestedScroll(coordinatorLayout, child, directTargetChild,
                        target, nestedScrollAxes);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout,
                                  FloatingActionButton child, View target, int dx,
                                  int dy, int[] consumed) {

        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        if (dy > 0 && child.getVisibility() == View.VISIBLE) {
            //用户向下滚动，FAB当前可见 - >隐藏FAB
            child.hide();
        }else if (dy < 0 && child.getVisibility() != View.VISIBLE) {
            //用户向上滚动，FAB当前不可见 - >显示FAB
            child.show();
        }
    }
}
