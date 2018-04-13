package com.angki.casualread.app.imageLoader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.jess.arms.http.imageloader.BaseImageLoaderStrategy;
import com.jess.arms.http.imageloader.glide.GlideArms;
import com.jess.arms.http.imageloader.glide.GlideRequest;
import com.jess.arms.http.imageloader.glide.GlideRequests;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 改变框架图片加载
 * @author ：Administrator on 2018/3/2 13:57
 * @eamil ：503001231@qq.com
 */

public class GlideImageLoaderStrategy implements BaseImageLoaderStrategy<ImageConfigImpl> {

    /**
     * 加载图片
     * @param ctx
     * @param config
     */
    @SuppressLint("CheckResult")
    @Override
    public void loadImage(Context ctx, ImageConfigImpl config) {
        //null处理
        if (ctx == null) {
            throw new NullPointerException("Context is required");
        }
        if (config == null) {
            throw new NullPointerException("ImageConfigImpl is required");
        }
        if (TextUtils.isEmpty(config.getUrl()) && config.getLoad() == -1) {
            throw new NullPointerException("Url is required");
        }
        if (config.getImageView() == null) {
            throw new NullPointerException("Imageview is required");
        }
        //构建Glide
        GlideRequests requests;
        GlideRequest<Drawable> glideRequest;
        //如果context是activity则自动使用Activity的生命周期
        requests = GlideArms.with(ctx);
        //判断加载网络还时本地
        if (config.getLoad() == -1 && !TextUtils.isEmpty(config.getUrl())) {
            glideRequest = requests.load(config.getUrl())
                    .transition(DrawableTransitionOptions.withCrossFade());
        }else {
            glideRequest = requests.load(config.getLoad())
                    .transition(DrawableTransitionOptions.withCrossFade());
        }
        //是否跳过内存缓存
        if (config.isSkipMemoryCache()) {
            glideRequest.skipMemoryCache(true);
        }

        //缓存策略
        switch (config.getDiskCacheStrategy()) {
            case 0:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.ALL);
                break;
            case 1:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.NONE);
                break;
            case 2:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                break;
            case 3:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.DATA);
                break;
            case 4:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
                break;
            default:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.ALL);
                break;
        }
        //改变图形的形状
        if (config.getTransformation() != null) {
            glideRequest.apply(config.getTransformation());
        }
        //设置关键字日期
        if (config.getStringSignature() != null) {
            glideRequest.signature(config.getStringSignature());
        }
        //设置占位符
        if (config.getPlaceholder() != 0) {
            glideRequest.placeholder(config.getPlaceholder());
        }
        //设置错误的图片
        if (config.getErrorPic() != 0){
            glideRequest.error(config.getErrorPic());
        }
        //设置请求 url 为空图片
        if (config.getFallback() != 0) {
            glideRequest.fallback(config.getFallback());
        }
        //设置图片加载的监听
        if (config.getRequestListener() != null) {
            glideRequest.listener(config.getRequestListener());
        }
        //设置图片加载的大小
        if (config.getmSize() != 0) {
            glideRequest.override(config.getmSize());
        }
        glideRequest.transition(DrawableTransitionOptions.withCrossFade());
        //初始化完成,当控件为自定义的控件时，会出现不显示头像，这时需要加入一个加载完成回调，当加载完成时加载图片
        glideRequest.into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@android.support.annotation.NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                config.getImageView().setImageDrawable(resource);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                config.getImageView().setImageDrawable(errorDrawable);
            }
        });
    }

    /**
     * 清除缓存
     * @param ctx
     * @param config
     */
    @Override
    public void clear(Context ctx, ImageConfigImpl config) {

        if (ctx == null) throw new NullPointerException("Context is required");
        if (config == null) throw new NullPointerException("ImageConfigImpl is required");
        //取消在执行的任务并且释放资源
        if (config.getImageViews() != null && config.getImageViews().length > 0) {
            for (ImageView imageView : config.getImageViews()) {
                GlideArms.get(ctx).getRequestManagerRetriever().get(ctx).clear(imageView);
            }
        }
        //清除本地缓存
        if (config.isClearDiskCache()) {
            Observable.just(0)
                    .observeOn(Schedulers.io())
                    .subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(@NonNull Integer integer) throws Exception {
                            Glide.get(ctx).clearDiskCache();
                        }
                    });
        }
        //清除内存缓存
        if (config.isClearMemory()) {
            Observable.just(0)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(@NonNull Integer integer) throws Exception {
                            Glide.get(ctx).clearMemory();
                        }
                    });
        }
    }
}
