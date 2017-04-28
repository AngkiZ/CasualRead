package com.angki.casualread.recommend.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.angki.casualread.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by tengyu on 2017/3/18.
 * 轮播图的图片加载器
 */

public class GlideImageLoader extends ImageLoader{

    @Override
    public void displayImage(Context context, Object url, ImageView imageView) {

        Glide.with(context).load(url)
                .placeholder(R.drawable.ic_meizi)
                .error(R.drawable.ic_meizi)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .dontAnimate()
                .into(imageView);
    }
}
