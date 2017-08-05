package com.sx.framelibrary.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * @Author sunxin
 * @Date 2017/8/5 9:25
 * @Description 数据库操作的接口规范
 */

public interface IDaoSupport<T> {
    /**
     * 初始化
     *
     * @param sqLiteDatabase
     * @param tClass
     */
    void init(SQLiteDatabase sqLiteDatabase, Class<T> tClass);


    /**
     * 插入一条数据
     *
     * @param t
     * @return
     */
    long insert(T t);


    /**
     * 批量插入数据
     *
     * @param datas
     */
    void insert(List<T> datas);


}
