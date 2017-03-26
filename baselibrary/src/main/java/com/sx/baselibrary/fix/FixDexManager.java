package com.sx.baselibrary.fix;

import android.content.Context;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.BaseDexClassLoader;

import static android.content.ContentValues.TAG;

/**
 * @Author sunxin
 * @Date 2017/3/26 22:40
 * @Description 自定义热修复管理类
 */

public class FixDexManager {

    private Context mContext;
    private File mDexDir;

    public FixDexManager(Context context) {

        mContext = context;
        //应用可以访问的目录
        mDexDir = context.getDir("odex", Context.MODE_PRIVATE);
    }

    /**
     * 修复dex包
     *
     * @param fixDexPath
     */
    public void fixDexBug(String fixDexPath) throws Exception {
        //1,获取已经运行的dex Element
        ClassLoader mContextClassLoader = mContext.getClassLoader();
        Object dexElements = getDexElementsByClassLoader(mContextClassLoader);
        //2,获取下载好的dex element
        //2.1  移动到系统能够访问的dex目录下
        File srcFile = new File(fixDexPath);
        if (!srcFile.exists()) {
            //如果文件不存在
            throw new FileNotFoundException(fixDexPath);
        }

        File destFile = new File(mDexDir, srcFile.getName());
        if (destFile.exists()) {
            Logger.d(TAG, "patch [" + fixDexPath + "] has be loaded.");
            return;
        }

        //拷贝文件
        copyFile(srcFile, destFile);

        //2.2  ClassLoader读取fixDex路径
        List<File> fixDexFiles = new ArrayList<>();
        fixDexFiles.add(destFile);

        File optimizedDirectory = new File(mDexDir, "odex");
        if (!optimizedDirectory.exists()) {
            optimizedDirectory.mkdirs();
        }


        //修复
        for (File fixDexFile : fixDexFiles) {
            ClassLoader fixDexClassLoader = new BaseDexClassLoader(
                    destFile.getAbsolutePath(),// dex 路径
                    optimizedDirectory,//解压路径
                    null,// .so  文件的位置
                    mContextClassLoader  // 父ClassLoader
            );

            Object fixDexElements = getDexElementsByClassLoader(fixDexClassLoader);

            //3，将下载好的dex element 插入到已经运行的dex element前面
            //合并数组
            dexElements = combineArray(fixDexElements, dexElements);
        }
        //注入到原来的类中
        inject(mContextClassLoader,dexElements);


    }

    /**
     * 把dexElements注入到ClassLoader中
     * @param classLoader
     * @param elements
     */
    private void inject(ClassLoader classLoader, Object elements) throws Exception{
        //1，通过反射获取pathList
        Field pathListFiled = BaseDexClassLoader.class.getDeclaredField("pathList");
        pathListFiled.setAccessible(true);
        Object pathList = pathListFiled.get(classLoader);

        //2,获取pathList中的dexElements
        Field dexElementsFiled = pathList.getClass().getDeclaredField("dexElements");
        dexElementsFiled.setAccessible(true);

        dexElementsFiled.set(pathList,elements);
    }

    /**
     * 合并两个dexElements数组
     *
     * @param arrayLhs
     * @param arrayRhs
     * @return
     */
    private static Object combineArray(Object arrayLhs, Object arrayRhs) {
        Class<?> localClass = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs);
        int j = i + Array.getLength(arrayRhs);
        Object result = Array.newInstance(localClass, j);
        for (int k = 0; k < j; ++k) {
            if (k < i) {
                Array.set(result, k, Array.get(arrayLhs, k));
            } else {
                Array.set(result, k, Array.get(arrayRhs, k - i));
            }
        }
        return result;
    }

    /**
     * copy file
     *
     * @param src  source file
     * @param dest target file
     * @throws IOException
     */
    public static void copyFile(File src, File dest) throws IOException {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            if (!dest.exists()) {
                dest.createNewFile();
            }
            inChannel = new FileInputStream(src).getChannel();
            outChannel = new FileOutputStream(dest).getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }

    /**
     * 通过ClassLoader获取Dex Element
     *
     * @param classLoader
     * @return
     */
    private Object getDexElementsByClassLoader(ClassLoader classLoader) throws Exception {
        //1，通过反射获取pathList
        Field pathListFiled = BaseDexClassLoader.class.getDeclaredField("pathList");
        pathListFiled.setAccessible(true);
        Object pathList = pathListFiled.get(classLoader);

        //2,获取pathList中的dexElements
        Field dexElements = pathList.getClass().getDeclaredField("dexElements");
        dexElements.setAccessible(true);

        return dexElements.get(pathList);
    }


}
