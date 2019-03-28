package com.angki.casualread.app.imageLoader;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.jess.arms.http.imageloader.ImageConfig;

/**
 * 图片加载
 * @author ：Administrator on 2018/7/25 17:27
 * @project ：WX_MutualAid
 * @package ：com.xmon.wx_mutualaid.app.lifeCycle
 * @e-mail ：503001231@qq.com
 */
public class ImageConfigImpl extends ImageConfig {

    /**
     * 0对应DiskCacheStrategy.all
     * 1对应DiskCacheStrategy.NONE
     * 2对应DiskCacheStrategy.SOURCE
     * 3对应DiskCacheStrategy.RESULT
     */
    private int diskCacheStrategy;
    /**
     * 请求 url 为空,则使用此图片作为占位符
     */
    private int fallback;
    /**
     * glide用它来改变图形的形状
     */
    private RequestOptions transformation;
    private ImageView[] imageViews;
    /**
     * 清理内存缓存
     */
    private boolean isClearMemory;
    /**
     * 清理本地缓存
     */
    private boolean isClearDiskCache;
    /**
     * 关键字
     */
    private Key stringSignature;
    /**
     * 本地图片
     */
    private int load;
    /**
     * 图片监听
     */
    private RequestListener<Drawable> requestListener;
    /**
     * 内存缓存
     */
    private boolean skipMemoryCache;
    /**
     * 加载尺寸
     */
    private int mSize;
    /**
     * 是否使用淡入淡出过渡动画
     */
    private boolean isCrossFade;

    private ImageConfigImpl(Builder builder){
        this.url = builder.url;
        this.imageView = builder.imageView;
        this.placeholder = builder.placeholder;
        this.errorPic = builder.errorPic;
        this.fallback = builder.fallback;
        this.diskCacheStrategy = builder.diskCacheStrategy;
        this.transformation = builder.transformation;
        this.imageViews = builder.imageViews;
        this.isClearMemory = builder.isClearMemory;
        this.isClearDiskCache = builder.isClearDiskCache;
        this.stringSignature = builder.stringSignature;
        this.load = builder.load;
        this.requestListener = builder.requestListener;
        this.skipMemoryCache = builder.skipMemoryCache;
        this.mSize = builder.mSize;
        this.isCrossFade = builder.isCrossFade;
    }

    public int getDiskCacheStrategy() {
        return diskCacheStrategy;
    }

    public int getFallback() {
        return fallback;
    }

    public RequestOptions getTransformation() {
        return transformation;
    }

    public ImageView[] getImageViews() {
        return imageViews;
    }

    public boolean isClearMemory() {
        return isClearMemory;
    }

    public boolean isClearDiskCache() {
        return isClearDiskCache;
    }

    public Key getStringSignature() {
        return stringSignature;
    }

    public int getLoad() {
        return load;
    }

    public RequestListener<Drawable> getRequestListener() {
        return requestListener;
    }

    public boolean isSkipMemoryCache() {
        return skipMemoryCache;
    }

    public int getmSize() {
        return mSize;
    }

    public boolean isCrossFade() {
        return isCrossFade;
    }

    public static ImageConfigImpl.Builder builder() {
        return new ImageConfigImpl.Builder();
    }

    public static final class Builder {
        /**
         * 图片链接
         */
        private String url;
        /**
         * 图片加载控件
         */
        private ImageView imageView;
        /**
         * 图片占位符
         */
        private int placeholder;
        /**
         * 加载错误占位符
         */
        private int errorPic;
        /**
         * 请求 url 为空,则使用此图片作为占位符
         */
        private int fallback;
        /**
         * 0对应DiskCacheStrategy.all,1对应DiskCacheStrategy.NONE,2对应DiskCacheStrategy.SOURCE,3对应DiskCacheStrategy.RESULT
         */
        private int diskCacheStrategy;
        /**
         * glide用它来改变图形的形状
         */
        private RequestOptions transformation;
        private ImageView[] imageViews;
        /**
         * 清理内存缓存
         */
        private boolean isClearMemory;
        /**
         * 清理本地缓存
         */
        private boolean isClearDiskCache;
        /**
         * 关键字
         */
        private Key stringSignature;
        /**
         * 本地图片
         */
        private int load = -1;
        /**
         * 图片监听
         */
        private RequestListener<Drawable> requestListener;
        /**
         * 是否使用内存缓存
         */
        private boolean skipMemoryCache = false;
        /**
         * 加载尺寸
         */
        private int mSize = 0;
        /**
         * 是否使用淡入淡出过渡动画
         */
        private boolean isCrossFade;


        private Builder() {}

        public ImageConfigImpl.Builder url(String url) {
            this.url = url;
            return this;
        }

        public ImageConfigImpl.Builder placeholder(int placeholder) {
            this.placeholder = placeholder;
            return this;
        }

        public ImageConfigImpl.Builder errorPic(int errorPic) {
            this.errorPic = errorPic;
            return this;
        }

        public ImageConfigImpl.Builder fallback(int fallback) {
            this.fallback = fallback;
            return this;
        }

        public ImageConfigImpl.Builder imageView(ImageView imageView) {
            this.imageView = imageView;
            return this;
        }

        public ImageConfigImpl.Builder diskCacheStrategy(int diskCacheStrategy) {
            this.diskCacheStrategy = diskCacheStrategy;
            return this;
        }

        public ImageConfigImpl.Builder transformation(RequestOptions transformation) {
            this.transformation = transformation;
            return this;
        }

        public ImageConfigImpl.Builder imageViews(ImageView... imageViews) {
            this.imageViews = imageViews;
            return this;
        }

        public ImageConfigImpl.Builder isClearMemory(boolean isClearMemory) {
            this.isClearMemory = isClearMemory;
            return this;
        }

        public ImageConfigImpl.Builder isClearDiskCache(boolean isClearDiskCache) {
            this.isClearDiskCache = isClearDiskCache;
            return this;
        }

        public ImageConfigImpl.Builder signature(Key stringSignature){
            this.stringSignature = stringSignature;
            return this;
        }

        public ImageConfigImpl.Builder localImage(int load) {
            this.load = load;
            return this;
        }

        public ImageConfigImpl.Builder listener(RequestListener<Drawable> requestListener) {
            this.requestListener = requestListener;
            return this;
        }

        public ImageConfigImpl.Builder skipMemoryCache(boolean b) {
            this.skipMemoryCache = b;
            return this;
        }

        public ImageConfigImpl.Builder override(int size) {
            this.mSize = size;
            return this;
        }

        public ImageConfigImpl.Builder isCrossFade(boolean isCrossFade) {
            this.isCrossFade = isCrossFade;
            return this;
        }

        public ImageConfigImpl build() {
            return new ImageConfigImpl(this);
        }

    }
}
