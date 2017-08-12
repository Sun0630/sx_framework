package com.sx.essayjoke;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.alipay.euler.andfix.patch.PatchManager;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.sx.baselibrary.ExceptionCrashHandler;
import com.sx.baselibrary.http.HttpUtils;
import com.sx.framelibrary.http.OkHttpEngine;
import com.sx.framelibrary.skin.SkinManager;

/**
 * @Author Sunxin
 * @Date 2017/3/20 15:55
 * @Description
 */

public class MyApplication extends Application {
    private static final String YOUR_TAG = "EssayJoke";
    public static PatchManager mPatchManager;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化全局处理异常类
        initLogger();
        //初始化网络请求引擎为okHttp
        HttpUtils.init(new OkHttpEngine());
        ExceptionCrashHandler.getInstance().init(this);
        initAndFix();//初始化阿里热修复andfix

        //初始化换肤
        SkinManager.getInstance().init(this);
    }


    private void initAndFix() {
        mPatchManager = new PatchManager(this);
        String appVersion = getAppVersicon();
        mPatchManager.init(appVersion);
        mPatchManager.loadPatch();
    }

    /**
     * 获取当前应用的版本号
     * @return
     */
    private String getAppVersicon() {
        PackageInfo packageInfo;
        String versionName = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(this.getPackageName(), PackageManager.GET_ACTIVITIES);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
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
