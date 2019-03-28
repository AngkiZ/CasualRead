package com.angki.casualread.app.appLifeCycle;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Message;
import android.support.v7.app.AppCompatDelegate;

import com.angki.casualread.BuildConfig;
import com.angki.casualread.app.util.LogUtils;
import com.angki.casualread.app.util.Utils;
import com.angki.casualread.app.util.myUtil;
import com.jess.arms.base.delegate.AppLifecycles;
import com.jess.arms.integration.AppManager;
import com.jess.arms.utils.ArmsUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import org.litepal.LitePal;

/**
 * AppLifecycles 的所有方法都会在基类 Application 的对应的生命周期中被调用
 * @author ：Administrator on 2018/3/2 11:32
 * @eamil ：503001231@qq.com
 */

public class AppLifecyclesImpl implements AppLifecycles {

    /**
     * 包名
     */
    public static String mPackageName;
    /**
     * 这里比 onCreate 先执行,常用于 MultiDex 初始化,插件化框架的初始化
     * @param base
     */
    @Override
    public void attachBaseContext(Context base) {
        //LitePal数据库注册
        LitePal.initialize(base);
    }

    /**
     * 初始化各种控件
     * @param application
     */
    @Override
    public void onCreate(Application application) {
        mPackageName = application.getPackageName();
        //日志打印
        if (BuildConfig.LOG_DEBUG) {

            //leakCanary内存泄露检查
            ArmsUtils.obtainAppComponentFromContext(application).extras().put(RefWatcher.class.getName(), BuildConfig.USE_CANARY ? LeakCanary.install(application) : RefWatcher.DISABLED);
        }
        Utils.init(application);
        LogUtils.Config config = LogUtils.getConfig()
                // 设置 log 总开关，包括输出到控制台和文件，默认开
                .setLogSwitch(BuildConfig.DEBUG)
                // 设置是否输出到控制台开关，默认开
                .setConsoleSwitch(BuildConfig.DEBUG)
                // 设置 log 全局标签，默认为空
                // 当全局标签不为空时，我们输出的 log 全部为该 tag，
                // 为空时，如果传入的 tag 为空那就显示类名，否则显示 tag
                .setGlobalTag(null)
                // 设置 log 头信息开关，默认为开
                .setLogHeadSwitch(true)
                // 打印 log 时是否存到文件的开关，默认关
                .setLog2FileSwitch(false)
                // 当自定义路径为空时，写入应用的/cache/log/目录中
                .setDir("")
                // 当文件前缀为空时，默认为"util"，即写入文件为"util-MM-dd.txt"
                .setFilePrefix("")
                // 输出日志是否带边框开关，默认开
                .setBorderSwitch(true)
                // log 的控制台过滤器，和 logcat 过滤器同理，默认 Verbose
                .setConsoleFilter(LogUtils.V)
                // log 文件过滤器，和 logcat 过滤器同理，默认 Verbose
                .setFileFilter(LogUtils.V)
                // log 栈深度，默认为 1
                .setStackDeep(1);
        LogUtils.d(config.toString());

        //扩展AppManager功能
        ArmsUtils.obtainAppComponentFromContext(application).appManager().setHandleListener(new AppManager.HandleListener() {
            @Override
            public void handleMessage(AppManager appManager, Message message) {
                switch (message.what) {
                    case 1001:
                        break;
                    default:
                        break;
                }
            }
        });

        //获取选择主题
        String themeFileName = "ThemeData";
        if (myUtil.isExisred(themeFileName)){
            switch (myUtil.getIntFromSP(application, themeFileName, "theme", 0)) {
                case 1:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    break;
                case 2:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    break;
                default:
                    break;
            }
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

    }

    /**
     * 在模拟环境中程序终止时会被调用
     */
    @Override
    public void onTerminate(Application application) {
        LogUtils.eTag("Angki","onTerminate");
    }

}
