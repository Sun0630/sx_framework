package com.sx.framelibrary;

import android.content.Context;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.sx.baselibrary.http.EngineCallBack;
import com.sx.baselibrary.http.HttpUtils;

import java.util.Map;

/**
 * @Author sunxin
 * @Date 2017/8/4 17:57
 * @Description
 */

public abstract class HttpCallBack<T> implements EngineCallBack {

    @Override
    public void onPreExcute(Context context, Map<String, Object> params) {
        Logger.d("调用HttpCallback---");

        //添加请求时的公用参数，与业务逻辑有关的
        params.put("app_name", "joke_essay");
        params.put("version_name", "5.7.0");
        params.put("ac", "wifi");
        params.put("device_id", "30036118478");
        params.put("device_brand", "Xiaomi");
        params.put("update_version_code", "5701");
        params.put("manifest_version_code", "570");
        params.put("longitude", "113.000366");
        params.put("latitude", "28.171377");
        params.put("device_platform", "android");

        onPreExcute();
    }

    //开始执行了
    public void onPreExcute() {

    }

    @Override
    public void onSuccess(String result) {
        T objResult = (T) new Gson().fromJson(result, HttpUtils.analysisClazzInfo(this));
        onSuccess(objResult);
    }

    public abstract void onSuccess(T obj);

}
