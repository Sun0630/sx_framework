package com.sx.framelibrary.http;

import android.util.Log;

import com.sx.framelibrary.db.DaoSupportFactory;
import com.sx.framelibrary.db.IDaoSupport;
import com.sx.framelibrary.utils.MD5Util;

import java.util.List;

/**
 * Email 240336124@qq.com
 * Created by Darren on 2017/3/12.
 * Version 1.0
 * Description:
 */
public class CacheDataUtil {

    /**
     * 获取数据
     */
    public static String getCacheResultJson(String finalUrl) {
        final IDaoSupport<CacheData> dataDaoSupport = DaoSupportFactory.getFactory().getDao(CacheData.class);
        // 需要缓存，从数据库拿缓存，问题又来了，OkHttpEngine  BaseLibrary
        // 数据库缓存在 FrameLibrary
        List<CacheData> cacheDatas = dataDaoSupport.querySupport()
                // finalUrl http:w 报错  finalUrl -> MD5处理
                .selection("url = ?").selectionArgs(MD5Util.getMd5Value(finalUrl)).query();

        if (cacheDatas.size() != 0) {
            // 代表有数据
            CacheData cacheData = cacheDatas.get(0);
            String resultJson = cacheData.jsonString;

            return resultJson;
        }
        return null;
    }

    /**
     * 缓存数据
     */
    public static long cacheData(String finalUrl, String resultJson) {
        final IDaoSupport<CacheData> dataDaoSupport = DaoSupportFactory.getFactory().
                getDao(CacheData.class);
        dataDaoSupport.delete("url=?", MD5Util.getMd5Value(finalUrl));
        long number = dataDaoSupport.insert(new CacheData(MD5Util.getMd5Value(finalUrl), resultJson));
        Log.e("TAG", "number --> " + number);
        return number;
    }
}
