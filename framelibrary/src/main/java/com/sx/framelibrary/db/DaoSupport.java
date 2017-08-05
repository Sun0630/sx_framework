package com.sx.framelibrary.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.util.ArrayMap;

import com.sx.framelibrary.utils.DaoUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @Author sunxin
 * @Date 2017/8/5 9:26
 * @Description 实现类
 */

public class DaoSupport<T> implements IDaoSupport<T> {


    private SQLiteDatabase mSqLiteDatabase;
    private Class<T> mTClass;

    private static final Map<String, Method> sPutMethod = new ArrayMap<>();

    private final Object[] mPutMethodArgs = new Object[2];


    @Override
    public void init(SQLiteDatabase sqLiteDatabase, Class<T> tClass) {
        mSqLiteDatabase = sqLiteDatabase;
        mTClass = tClass;

        //创建表
//        create table if not exists Person (id integer primary key autoincrement, age integer, name text);
//        create table if not exists Person(id integer primary key autoincrement, age integer, name text)
        StringBuffer sb = new StringBuffer();
        sb.append("create table if not exists ")
                .append(DaoUtils.getTableName(mTClass))
                .append(" (id integer primary key autoincrement, ");

        //需要通过反射，拿到实体类的字段和其类型，以创建数据库表的字段和其类型
        Field[] fields = mTClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);//设置权限
            String fieldName = field.getName();//字段名
            String fieldTypeName = field.getType().getSimpleName();//字段类型  int  string boolean
            //需要将获取的字段类型进行转换  int --> integer   String --> text....
            sb.append(fieldName).append(DaoUtils.getColumnType(fieldTypeName)).append(", ");
        }

        sb.replace(sb.length() - 2, sb.length(), ");");

        String createTableSql = sb.toString();
        System.out.println("建表语句为：" + createTableSql);
        mSqLiteDatabase.execSQL(createTableSql);
    }

    /**
     * 插入数据
     *
     * @param obj 任意对象
     * @return 修改的行数
     */
    @Override
    public long insert(T obj) {

        ContentValues values = contentValuesByObj(obj);
        return mSqLiteDatabase.insert(DaoUtils.getTableName(mTClass), null, values);
    }

    @Override
    public void insert(List<T> datas) {
        //优化1，添加事务
        mSqLiteDatabase.beginTransaction();
        for (T data : datas) {
            insert(data);
        }
        mSqLiteDatabase.setTransactionSuccessful();
        mSqLiteDatabase.endTransaction();
    }

    /**
     * 将任意对象obj转换成ContentValues
     *
     * @param obj
     * @return
     */
    private ContentValues contentValuesByObj(T obj) {
        ContentValues values = new ContentValues();
        Field[] fields = mTClass.getDeclaredFields();
        for (Field field : fields) {
//            values.put();
            try {
                field.setAccessible(true);//设置权限为可访问
                //获取key
                String key = field.getName();
                //获取value
                Object value = field.get(obj);
//                values.put(key,value);  put  方法第二个参数必须接受一个有类型的变量
                mPutMethodArgs[0] = key;
                mPutMethodArgs[1] = value;
                //1,可以判断value的类型，然后进行转换
//                if (value instanceof String){
//
//                }
                //2,通过反射来执行values 的 put 方法
                //优化数据库效率：使用反射获得method会有性能问题，目的就是缓存方法，使用反射拿一次，然后保存起来，下一次直接从ArrayMap中获取
                String typeName = field.getType().getName();
                Method putMethod = sPutMethod.get(typeName);
                if (putMethod == null) {
                    putMethod = ContentValues.class.getDeclaredMethod("put", String.class, value.getClass());
                    sPutMethod.put(typeName, putMethod);
                }


                //通过反射执行
                putMethod.invoke(values, mPutMethodArgs);


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mPutMethodArgs[0] = null;
                mPutMethodArgs[1] = null;
            }

        }
        return values;
    }
}
