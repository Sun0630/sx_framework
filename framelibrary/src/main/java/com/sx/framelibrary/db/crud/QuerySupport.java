package com.sx.framelibrary.db.crud;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sx.framelibrary.utils.DaoUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author sunxin
 * @Date 2017/8/6 12:01
 * @Description 查询支持类
 */

public class QuerySupport<T> {

    private SQLiteDatabase mSqLiteDatabase;
    private Class<T> mTClass;

    private String mSelection;//查询的条件
    private String[] mSelectionArgs;//查询的参数
    private String mHaving;//对结果集进行过滤
    private String mLimit;//分页
    private String mOrderBy;//排序
    private String mGroupBy;//分组
    private String[] mColumns;//查询的列

    public QuerySupport(SQLiteDatabase sqLiteDatabase, Class<T> tClass) {

        mSqLiteDatabase = sqLiteDatabase;
        mTClass = tClass;
    }


    public QuerySupport<T> selection(String selection) {
        mSelection = selection;
        return this;
    }

    public QuerySupport<T> selectionArgs(String... selectionArgs) {
        mSelectionArgs = selectionArgs;
        return this;
    }

    public QuerySupport<T> having(String having) {
        mHaving = having;
        return this;
    }

    public QuerySupport<T> limit(String limit) {
        mLimit = limit;
        return this;
    }

    public QuerySupport<T> orderBy(String orderBy) {
        mOrderBy = orderBy;
        return this;
    }

    public QuerySupport<T> groupBy(String groupBy) {
        mGroupBy = groupBy;
        return this;
    }


    public QuerySupport<T> columns(String... columns) {
        mColumns = columns;

        return this;
    }

    /**
     * 按条件查询
     *
     * @return
     */
    public List<T> query() {
        Cursor cursor = mSqLiteDatabase.query(DaoUtils.getTableName(mTClass), mColumns,
                mSelection, mSelectionArgs, mGroupBy, mHaving, mOrderBy, mLimit);

        List<T> list = cursorToList(cursor);
        return list;
    }


    /**
     * 查询所有
     *
     * @return
     */
    public List<T> queryAll() {
        Cursor cursor = mSqLiteDatabase.query(DaoUtils.getTableName(mTClass), null, null, null, null, null, null);
        return cursorToList(cursor);
    }


    /**
     * 清空参数
     */
    public void clearQueryParams() {
        mColumns = null;
        mGroupBy = null;
        mLimit = null;
        mHaving = null;
        mOrderBy = null;
        mSelection = null;
        mSelectionArgs = null;
    }

    /**
     * cursor 转换成List
     *
     * @param cursor
     * @return
     */
    private List<T> cursorToList(Cursor cursor) {
        List<T> list = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                try {
                    T instance = mTClass.newInstance();
                    Field[] fields = mTClass.getDeclaredFields();
                    for (Field field : fields) {
                        field.setAccessible(true);
                        String name = field.getName();
                        //获取列名下标
                        int index = cursor.getColumnIndex(name);
                        if (index == -1) {
                            continue;
                        }
                        //通过反射获取游标方法
                        Method cursorMethod = getCursorMethod(field.getType());
                        if (cursorMethod != null) {
                            //通过反射执行方法
                            Object value = cursorMethod.invoke(cursor, index);
                            if (value == null) {
                                continue;
                            }
                            //处理一些特殊的部分
                            if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                                if ("0".equals(String.valueOf(value))) {
                                    value = false;
                                } else if ("1".equals(String.valueOf(value))) {
                                    value = true;
                                }
                            } else if (field.getType() == char.class || field.getType() == Character.class) {
                                value = ((String) value).charAt(0);
                            } else if (field.getType() == Date.class) {
                                long date = (Long) value;
                                if (date <= 0) {
                                    value = null;
                                } else {
                                    value = new Date(date);
                                }
                            }
                            //为instance的这个field设置新value
                            field.set(instance, value);
                        }
                    }
                    //加入到集合中
                    list.add(instance);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }


        return list;
    }

    /**
     * @param type
     * @return
     */
    private Method getCursorMethod(Class<?> type) {
        String methodName = getColumnMethodName(type);
        try {
            Method method = Cursor.class.getMethod(methodName, int.class);
            return method;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据类型获得cursor的方法名
     *
     * @param type
     * @return
     */
    private String getColumnMethodName(Class<?> type) {
        String typeName = null;
        //primitive:原始的
        if (type.isPrimitive()) {
            //把首字母大写
            typeName = DaoUtils.capitalize(type.getName());
        } else {
            typeName = type.getSimpleName();
        }

        String methodName = "get" + typeName;
        if ("getBoolean".equals(methodName)) {
            methodName = "getInt";
        } else if ("getChar".equals(methodName) || "getCharacter".equals(methodName)) {
            methodName = "getString";
        } else if ("getDate".equals(methodName)) {
            methodName = "getLong";
        } else if ("getInteger".equals(methodName)) {
            methodName = "getInt";
        }

        return methodName;
    }


}
