package com.angki.casualread.app.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.angki.casualread.app.lifeCycle.AppLifecyclesImpl;

import java.io.File;

/**
 *  工具类
 * @author ：Administrator on 2018/4/13 17:37
 * @e-mail ：503001231@qq.com
 */
public final class myUtil {

    private myUtil() {
        throw new IllegalStateException("you can't instantiate me!");
    }

    //#################### SharePreferences的操作 ####################
    /**
     * @param fileName 文件名
     * @param mode     1. MODE_APPEND: 追加方式存储
     *                 2. MODE_PRIVATE: 私有方式存储,其他应用无法访问
     *                 3. MODE_WORLD_READABLE: 表示当前文件可以被其他应用读取
     *                 4. MODE_WORLD_WRITEABLE: 表示当前文件可以被其他应用写入
     *                 5. MODE_MULTI_PROCESS: 适用于多进程访问(目前已被废弃，google官方推荐使用ContentProvider来实现进程间共享访问)
     * @return
     */
    public static SharedPreferences.Editor edit(Context context, String fileName, int mode) {
        return context.getSharedPreferences(fileName, mode).edit();
    }

    public static boolean isExisred(String fileName) {
        return new File("/data/data/"+ AppLifecyclesImpl.mPackageName+"/shared_prefs/ThemeData.xml").exists();
    }

    /**
     * 获取字符串
     * @param fileName 文件名
     * @param var1     文件里的属性名
     * @param var2     默认值
     * @return
     */
    public static String getStringFromSP(Context context, String fileName, String var1, String var2) {
        return context.getSharedPreferences(fileName, Context.MODE_PRIVATE).getString(var1, var2);
    }

    /**
     * 获取boolean
     * @param fileName 文件名
     * @param var1     文件里的属性名
     * @param var2     默认值
     * @return
     */
    public static boolean getStringFromSP(Context context, String fileName, String var1, boolean var2) {
        return context.getSharedPreferences(fileName, Context.MODE_PRIVATE).getBoolean(var1, var2);
    }

    /**
     * 获取int
     * @param fileName 文件名
     * @param var1     文件里的属性名
     * @param var2     默认值
     * @return
     */
    public static int getIntFromSP(Context context, String fileName, String var1, int var2) {
        return context.getSharedPreferences(fileName, Context.MODE_PRIVATE).getInt(var1, var2);
    }
}
