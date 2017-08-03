package com.sx.baselibrary.http;

import java.util.Map;

/**
 * @Author sunxin
 * @Date 2017/8/4 0:14
 * @Description 自己打造网络框架
 */

public class HttpUtils implements IHttpEngine {

    //默认的引擎是okHttp
    private static IHttpEngine mHttpEngine = new OkHttpEngine();


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

    @Override
    public void get(String url, Map<String, Object> params, EngineCallBack callBack) {
        mHttpEngine.get(url, params, callBack);
    }

    @Override
    public void post(String url, Map<String, Object> params, EngineCallBack callBack) {
        mHttpEngine.post(url, params, callBack);
    }
}
