package com.angki.casualread.zhihu.gson;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by tengyu on 2017/3/18.
 */

public class GlideImageLoader extends ImageLoader{

    @Override
    public void displayImage(Context context, Object url, ImageView imageView) {

        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }
}
