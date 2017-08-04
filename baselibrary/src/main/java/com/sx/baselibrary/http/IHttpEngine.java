package com.sx.baselibrary.http;

import android.content.Context;

import java.util.Map;

/**
 * @Author sunxin
 * @Date 2017/8/4 0:14
 * @Description 网络引擎的规范
 */

public interface IHttpEngine {

    /**
     * get请求
     *
     * @param url
     * @param params
     * @param callBack
     */
    void get(Context context, String url, Map<String, Object> params, EngineCallBack callBack);

    /**
     * get请求
     *
     * @param url
     * @param params
     * @param callBack
     */
    void post(Context context,String url, Map<String, Object> params, EngineCallBack callBack);


    //上传文件

    //下载文件

    //https 生成证书


}
