package com.sx.framelibrary.db;

import android.database.sqlite.SQLiteDatabase;

import com.sx.framelibrary.db.crud.QuerySupport;

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


    /**
     * 查询支持类
     *
     * @return
     */
    QuerySupport<T> querySupport();

    /**
     * 更新
     *
     * @param obj
     * @param whereClause
     * @param whereArgs
     * @return
     */
    int update(T obj, String whereClause, String[] whereArgs);


    /**
     * 删除
     *
     * @param whereClause
     * @param whereArgs
     * @return
     */
    int delete(String whereClause, String... whereArgs);

}
