package com.sx.baselibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author sunxin
 * @Date 2017/3/22 22:28
 * @Description 全局的异常处理类  单例模式
 */

public class ExceptionCrashHandler implements Thread.UncaughtExceptionHandler {

    private static ExceptionCrashHandler mInstance;
    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultExceptionHandler;

    public static ExceptionCrashHandler getInstance() {
        if (mInstance == null) {
            //同步锁，处理并发问题
            synchronized (ExceptionCrashHandler.class) {
                if (mInstance == null) {
                    mInstance = new ExceptionCrashHandler();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context) {
        mContext = context;
        //设置全局的异常类为本类
        Thread.currentThread().setUncaughtExceptionHandler(this);
        //获取系统默认的异常处理
        mDefaultExceptionHandler = Thread.currentThread().getDefaultUncaughtExceptionHandler();
    }

    /**
     * 当有异常的时候会回调此方法
     * @param t
     * @param e
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        //全局异常
        Logger.e("报异常了！");
        //拿到异常信息，写到本地文件，包括异常信息，机型信息，当前版本等

        //1,异常信息
        //2,应用信息，包名  版本号
        //3,手机机型信息
        //4,保存当前文件，当应用再次启动的时候再去上传。   上传文件不能在这里处理

        String crashFileName = saveInfo2SD(e);

        Logger.d("CrashFileName--->" + crashFileName);

        //缓存崩溃日志
        cacheCrashLog(crashFileName);

        //系统默认的处理方式
        mDefaultExceptionHandler.uncaughtException(t, e);
    }

    /**
     * 缓存崩溃日志
     *
     * @param name
     */
    private void cacheCrashLog(String name) {
        //保存到本地
        SharedPreferences sp = mContext.getSharedPreferences("crash", Context.MODE_PRIVATE);
        sp.edit().putString("CRASH_FILE_NAME", name).commit();
    }

    /**
     * 获取崩溃的文件
     *
     * @return
     */
    public File getCrashFile() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("crash", Context.MODE_PRIVATE);
        String carshFileName = sharedPreferences.getString("CRASH_FILE_NAME", "");
        return new File(carshFileName);
    }

    /**
     * 保存异常信息，软件信息，设备信息到sd卡中
     *
     * @param e
     * @return
     */
    private String saveInfo2SD(Throwable e) {
        String fileName = null;
        StringBuffer stringBuffer = new StringBuffer();
        Map<String, String> simpleInfo = getSimpleInfo(mContext);
        //循环获取map中的键值对，拼接到StringBuffer中
        for (Map.Entry<String, String> entry : simpleInfo.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            stringBuffer.append(key).append("=").append(value).append("\n");
        }

        //拼接系统异常信息
        stringBuffer.append(getExceptionInfo(e));

        //将数据写入到Sd卡中
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //sd卡可用
            File dir = new File(mContext.getFilesDir() + File.separator + "crash" + File.separator);

            //删除之前的异常信息
            if (dir.exists()) {
                deleteDir(dir);
            }

            //在创建新的文件夹
            if (!dir.exists()) {
                dir.mkdirs();
            }

            try {
                fileName = dir.toString() + File.separator + getAssignTime("yyyy_MM_dd_HH_mm") + ".txt";
                FileOutputStream fos = new FileOutputStream(fileName);
                fos.write(stringBuffer.toString().getBytes());
                fos.flush();
                fos.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return fileName;
    }


    /**
     * 返回日期格式
     *
     * @param dateFormat
     * @return
     */
    private String getAssignTime(String dateFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        long currentTimeMillis = System.currentTimeMillis();
        return simpleDateFormat.format(currentTimeMillis);
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     */
    private boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                file.delete();
            }
        }
        return true;
    }


    /**
     * 获取系统报错信息
     *
     * @param throwable
     * @return
     */
    private String getExceptionInfo(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        printWriter.close();
        return stringWriter.toString();
    }


    /**
     * 获取软件版本，手机型号等信息存储在Map中
     *
     * @param context
     * @return
     */
    private Map<String, String> getSimpleInfo(Context context) {
        HashMap<String, String> map = new HashMap<>();

        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        map.put("versionCode", packageInfo.versionCode + "");
        map.put("packageName", packageInfo.packageName);
        map.put("versionName", packageInfo.versionName);
        map.put("MODEL", Build.MODEL);
        map.put("SDK_INT", Build.VERSION.SDK_INT + "");
        map.put("MOBILE_INFO", getMobileInfo());

        return map;
    }

    /**
     * 利用反射Build类获取手机信息
     *
     * @return
     */
    private String getMobileInfo() {
        StringBuffer stringBuffer = new StringBuffer();
        //利用反射获取字段
        Field[] fields = Build.class.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();//获取字段名
                String value = field.get(null).toString();//获取值

                stringBuffer.append(name + "=" + value);
                stringBuffer.append("\n");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }
}
