package com.sx.framelibrary.db;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;

/**
 * @Author sunxin
 * @Date 2017/8/5 9:27
 * @Description 数据库的操作工厂，使用单例模式+工厂模式
 */

public class DaoSupportFactory {

    private static DaoSupportFactory mFactory;
    private final SQLiteDatabase mSqLiteDatabase;

    private DaoSupportFactory() {
        //在构造的时候，要指定数据库文件存放在sd卡中
        //指定数据库文件存放根目录
        //注意：需要判断是否有内存卡，6.0要动态获取权限
        File dbRoot = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator + "EssayJoke" + File.separator + "database");

        if (!dbRoot.exists()) {
            dbRoot.mkdirs();
        }

        //创建数据库文件
        File dbFile = new File(dbRoot, "essayjoke.db");

        //打开或创建一个数据库
        mSqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
    }

    public static DaoSupportFactory getFactory() {
        if (mFactory == null) {
            synchronized (DaoSupportFactory.class) {
                if (mFactory == null) {
                    mFactory = new DaoSupportFactory();
                }
            }
        }
        return mFactory;
    }

    /**
     * 获取数据库操作对象,直接操作接口
     *
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> IDaoSupport<T> getDao(Class<T> tClass) {
        IDaoSupport<T> daoSupport = new DaoSupport();
        daoSupport.init(mSqLiteDatabase,tClass);
        return daoSupport;
    }


}
