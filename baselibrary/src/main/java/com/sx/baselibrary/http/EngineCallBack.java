package com.sx.baselibrary.http;

/**
 * @Author sunxin
 * @Date 2017/8/4 0:16
 * @Description 自己的回调
 */

public interface EngineCallBack {
    /**
     * 请求失败回调
     * @param e
     */
    void onError(Exception e);

    /**
     * 请求成功回调
     * @param result
     */
    void onSuccess(String result);

}
