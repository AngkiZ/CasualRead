package com.angki.casualread.app;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.angki.casualread.BuildConfig;
import com.angki.casualread.app.lifeCycle.ActivityLifecycleCallbacksImpl;
import com.angki.casualread.app.lifeCycle.AppLifecyclesImpl;
import com.angki.casualread.app.globalError.ResponseErrorListenerImpl;
import com.angki.casualread.app.httpHandler.GlobalHttpHandlerImpl;
import com.angki.casualread.app.imageLoader.GlideImageLoaderStrategy;
import com.google.gson.GsonBuilder;
import com.jess.arms.base.delegate.AppLifecycles;
import com.jess.arms.di.module.AppModule;
import com.jess.arms.di.module.ClientModule;
import com.jess.arms.di.module.GlobalConfigModule;
import com.jess.arms.http.log.RequestInterceptor;
import com.jess.arms.integration.ConfigModule;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.rx_cache2.internal.RxCache;
import me.jessyan.progressmanager.ProgressManager;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * App 的全局配置信息在此配置, 需要将此实现类声明到 AndroidManifest 中
 * ConfigModule 的实现类可以有无数多个, 在 Application 中只是注册回调, 并不会影响性能 (多个 ConfigModule 在多 Module 环境下尤为受用)
 * 不过要注意 ConfigModule 接口的实现类对象是通过反射生成的, 这里会有些性能损耗
 * @author ：Administrator on 2018/3/2 11:26
 * @eamil ：503001231@qq.com
 */

public class GlobalConfiguration implements ConfigModule {

    /**
     *  App全局配置信息
     * @param context
     * @param builder
     */
    @Override
    public void applyOptions(Context context, GlobalConfigModule.Builder builder) {

        //Release 时,让框架不再打印 Http 请求和响应的信息
        if (!BuildConfig.LOG_DEBUG) {
            builder.printHttpLogLevel(RequestInterceptor.Level.NONE);
        }
        //使用builder可以为框架配置一些配置信息
        builder
                //这里可以自己自定义配置Gson的参数
                .gsonConfiguration(new AppModule.GsonConfiguration() {
                    @Override
                    public void configGson(Context context, GsonBuilder gsonBuilder) {
                        gsonBuilder
                                //支持序列化null的参数
                                .serializeNulls()
                                //支持将序列化key为object的map,默认只能序列化key为string的map
                                .enableComplexMapKeySerialization()
                                //配置日期
                                .setDateFormat("yyyy-MM-dd HH:mm:ss");
                    }
                })
                //这里可以自己自定义配置Retrofit的参数
                .retrofitConfiguration(new ClientModule.RetrofitConfiguration() {
                    @Override
                    public void configRetrofit(Context context, Retrofit.Builder retrofitBuilder) {
                        retrofitBuilder
                                //设置RxJava
                                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
                    }
                })
                //这里可以自己自定义配置Okhttp的参数
                .okhttpConfiguration(new ClientModule.OkhttpConfiguration() {
                    @Override
                    public void configOkhttp(Context context, OkHttpClient.Builder okhttpBuilder) {
                        okhttpBuilder
                                //读取超时时间设置
                                .readTimeout(15, TimeUnit.SECONDS)
                                //连接超时时间设置
                                .connectTimeout(10, TimeUnit.SECONDS);
                        //使用一行代码监听 Retrofit／Okhttp 上传下载进度监听,以及 Glide 加载进度监听 详细使用方法查看 https://github.com/JessYanCoding/ProgressManager
                        ProgressManager.getInstance().with(okhttpBuilder);
                        //让 Retrofit 同时支持多个 BaseUrl 以及动态改变 BaseUrl. 详细使用请方法查看 https://github.com/JessYanCoding/RetrofitUrlManager
                        RetrofitUrlManager.getInstance().with(okhttpBuilder);
                    }
                })
                //这里可以自己自定义配置 RxCache 的参数
                .rxCacheConfiguration(new ClientModule.RxCacheConfiguration() {
                    @Override
                    public RxCache configRxCache(Context context, RxCache.Builder rxCacheBuilder) {
                        rxCacheBuilder.useExpiredDataIfLoaderNotAvailable(true);
                        return null;
                    }
                })
                //用来处理 rxjava 中发生的所有错误,rxjava 中发生的每个错误都会回调此接口
                .responseErrorListener(new ResponseErrorListenerImpl())
                //配置Glide
                .imageLoaderStrategy(new GlideImageLoaderStrategy())
                // 这里提供一个全局处理 Http 请求和响应结果的处理类,可以比客户端提前一步拿到服务器返回的结果,可以做一些操作,比如token超时,重新获取
                .globalHttpHandler(new GlobalHttpHandlerImpl(context));
    }

    /**
     * AppLifecycles 的所有方法都会在基类 Application 的对应的生命周期中被调用,所以在对应的方法中可以扩展一些自己需要的逻辑
     * @param context
     * @param lifecycles
     */
    @Override
    public void injectAppLifecycle(Context context, List<AppLifecycles> lifecycles) {
        lifecycles.add(new AppLifecyclesImpl());
    }

    /**
     * ActivityLifecycleCallbacks 的所有方法都会在 Activity (包括三方库) 的对应的生命周期中被调用,所以在对应的方法中可以扩展一些自己需要的逻辑
     * @param context
     * @param lifecycles
     */
    @Override
    public void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycles) {
        lifecycles.add(new ActivityLifecycleCallbacksImpl());
    }

    @Override
    public void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {
//        lifecycles.add(new FragmentLifecycleCallbacksImpl());
    }

}
