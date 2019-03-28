package com.angki.casualread.mvp.contract;

import android.content.Context;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;

/**
 * Created by Angki on 2018/6/27.
 */

public interface IFragmentRecommendContract {

    interface IFragmentRecommendView extends IView {

        /**
         * 获取Context的实例
         * @return 返回Context的实例
         */
        Context getThisContext();



    }

    interface IFragmentRecommendModel extends IModel {

    }
}
