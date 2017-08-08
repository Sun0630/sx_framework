package com.sx.baselibrary.http;

import android.content.Context;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author sunxin
 * @Date 2017/8/4 0:14
 * @Description 自己打造网络框架，链式调用
 */

public class HttpUtils {

    private Context mContext;
    private String mUrl;//url地址
    private Map<String, Object> mParams;//参数
    private int mType = GET_TYPE;//请求类型，get or post
    public static final int GET_TYPE = 0x0011;
    public static final int POST_TYPE = 0x0012;
    //默认的引擎是okHttp
    private static IHttpEngine mHttpEngine = null;
    private boolean mIsCache = false;


    private HttpUtils(Context context) {
        mContext = context;
        mParams = new HashMap<>();
    }


    public static HttpUtils with(Context context) {
        return new HttpUtils(context);
    }

    /**
     * 是否添加缓存功能
     *
     * @param isCache
     * @return
     */
    public HttpUtils cache(boolean isCache) {
        mIsCache = isCache;
        return this;
    }

    /**
     * 传入URL
     *
     * @param url
     * @return
     */
    public HttpUtils url(String url) {
        mUrl = url;
        return this;
    }

    /**
     * get
     *
     * @return
     */
    public HttpUtils get() {
        mType = GET_TYPE;
        return this;
    }

    public HttpUtils post() {
        mType = POST_TYPE;
        return this;
    }

    /**
     * 添加键值对参数
     *
     * @param key
     * @param value
     * @return
     */
    public HttpUtils addParams(String key, Object value) {
        mParams.put(key, value);
        return this;
    }

    /**
     * 添加Map类型的参数
     *
     * @param params
     * @return
     */
    public HttpUtils addParams(HashMap<String, Object> params) {
        mParams.putAll(params);
        return this;
    }


    /**
     * 回调方法
     *
     * @param callBack
     */
    public void excute(EngineCallBack callBack) {

        //调用callback的
        callBack.onPreExcute(mContext, mParams);


        if (callBack == null) {
            callBack = EngineCallBack.DEFAULT_CALL_BACK;
        }

        if (mType == GET_TYPE) {
            get(mIsCache,mUrl, mParams, callBack);
        }

        if (mType == POST_TYPE) {
            post(mIsCache,mUrl, mParams, callBack);
        }
    }

    public void excute() {
        excute(null);
    }


    //在Application中初始化网络引擎
    public static void init(IHttpEngine httpEngine) {
        mHttpEngine = httpEngine;
    }

    /**
     * 切换引擎
     *
     * @param httpEngine
     */
    public static void exchangeEngine(IHttpEngine httpEngine) {
        mHttpEngine = httpEngine;
    }

    private void get(boolean cache,String url, Map<String, Object> params, EngineCallBack callBack) {
        mHttpEngine.get(cache,mContext, url, params, callBack);
    }

    private void post(boolean cache,String url, Map<String, Object> params, EngineCallBack callBack) {
        mHttpEngine.post(cache,mContext, url, params, callBack);
    }

    /**
     * 合并参数
     *
     * @param url
     * @param params
     * @return
     */
    public static String joinParams(String url, Map<String, Object> params) {
        if (params == null || params.size() <= 0) {
            return url;
        }

        StringBuffer stringBuffer = new StringBuffer(url);
        if (!url.contains("?")) {
            stringBuffer.append("?");
        } else {
            if (!url.endsWith("?")) {
                stringBuffer.append("&");
            }
        }

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
        }

        stringBuffer.deleteCharAt(stringBuffer.length() - 1);

        return stringBuffer.toString();
    }

    /**
     * 解析一个类上面的class信息
     * 根据一个类的实体得到其Class对象
     */
    public static Class<?> analysisClazzInfo(Object object) {
        Type genType = object.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class<?>) params[0];
    }
}
