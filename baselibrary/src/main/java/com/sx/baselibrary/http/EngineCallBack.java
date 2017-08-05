package com.sx.baselibrary.http;

import android.content.Context;

import java.util.Map;

/**
 * @Author sunxin
 * @Date 2017/8/4 0:16
 * @Description 自己的回调
 */

public interface EngineCallBack {
    /**
     * 请求失败回调
     *
     * @param e
     */
    void onError(Exception e);

    /**
     * 请求成功回调
     *
     * @param result
     */
    void onSuccess(String result);


    /**
     * 执行之前调用
     *
     * @param context
     * @param params
     */
    void onPreExcute(Context context, Map<String, Object> params);


    EngineCallBack DEFAULT_CALL_BACK = new EngineCallBack() {
        @Override
        public void onError(Exception e) {

        }

        @Override
        public void onSuccess(String result) {

        }

        @Override
        public void onPreExcute(Context context, Map<String, Object> params) {

        }
    };

}
