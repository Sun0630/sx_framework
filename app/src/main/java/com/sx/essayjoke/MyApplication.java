package com.sx.essayjoke;

import android.app.Application;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.sx.baselibrary.ExceptionCrashHandler;

/**
 * @Author Sunxin
 * @Date 2017/3/20 15:55
 * @Description
 */

public class MyApplication extends Application {
    private static final String YOUR_TAG = "EssayJoke";

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化全局处理异常类
        initLogger();
        ExceptionCrashHandler.getInstance().init(this);
    }

    /**
     * 初始化Logger
     */
    private void initLogger() {
        Logger
                .init(YOUR_TAG)                 // default PRETTYLOGGER or use just init()
                .methodCount(3)                 // default 2
//                .hideThreadInfo()               // default shown
                .logLevel(LogLevel.FULL)        // default LogLevel.FULL
                .methodOffset(2);     // default 0
    }
}
