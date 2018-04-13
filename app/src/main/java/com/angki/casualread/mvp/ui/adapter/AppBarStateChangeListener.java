package com.angki.casualread.mvp.ui.adapter;

import android.support.design.widget.AppBarLayout;

/**
 * Created by tengyu on 2017/4/28.
 * 监听CollapsingToolbarLayout的折叠、展开状态
 */

public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener{
    //定义三个枚举
    public enum State {
        EXPANDED,//展开状态
        COLLAPSED,//折叠状态
        IDLE//中间状态
    }

    private State mCurrentState = State.IDLE;

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            if (mCurrentState != State.EXPANDED) {
                onStateChanged(appBarLayout, State.EXPANDED);
            }
            //修改状态标记为展开
            mCurrentState = State.EXPANDED;
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            if (mCurrentState != State.COLLAPSED) {
                onStateChanged(appBarLayout, State.COLLAPSED);
            }
            //修改状态标记为折叠
            mCurrentState = State.COLLAPSED;
        } else {
            if (mCurrentState != State.IDLE) {
                onStateChanged(appBarLayout, State.IDLE);
            }
            //修改状态标记为中间
            mCurrentState = State.IDLE;
        }
    }
    //调用的方法
    public abstract void onStateChanged(AppBarLayout appBarLayout, State state);
}
