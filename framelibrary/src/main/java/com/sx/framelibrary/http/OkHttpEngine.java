package com.sx.framelibrary.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.sx.baselibrary.http.EngineCallBack;
import com.sx.baselibrary.http.HttpUtils;
import com.sx.baselibrary.http.IHttpEngine;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * @Author sunxin
 * @Date 2017/8/4 0:19
 * @Description okHttp默认的引擎
 */

public class OkHttpEngine implements IHttpEngine {

    public static final String TAG = "OkHttpEngine";

    private static OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    public void get(final boolean cache, Context context, final String url, Map<String, Object> params, final EngineCallBack callBack) {
        final String joinurl = HttpUtils.joinParams(url, params);
        Log.d("OkHttpEngine-->Get:", joinurl);
//        final IDaoSupport<CacheData> cacheDataSupport = DaoSupportFactory
//                .getFactory()
//                .getDao(CacheData.class);
//
//        final List<CacheData> cacheDatas = cacheDataSupport.querySupport()
//                .selection("url=?")
//                .selectionArgs(joinurl)
//                .query();

        //处理缓存,先判断需不需要缓存，然后判断有没有缓存
        if (cache) {
            String resultJson = CacheDataUtil.getCacheResultJson(joinurl);
            //需要缓存
            if (!TextUtils.isEmpty(resultJson)) {
                Log.e(TAG, "get: 读到缓存");
                //有缓存,直接回调
                callBack.onSuccess(resultJson);
            }
        }


        Request.Builder requestBuilder = new Request.Builder().get().url(url).tag(context);
        final Request request = requestBuilder.build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resultJson = response.body().string();
                //获取到数据之后，先和缓存数据做一个对比，判断是否需要缓存

                if (cache) {
                    String cacheResultJson = CacheDataUtil.getCacheResultJson(joinurl);
                    if (!TextUtils.isEmpty(resultJson)) {
                        // 比对内容
                        if (resultJson.equals(cacheResultJson)) {
                            // 内容一样，不需要执行成功成功方法刷新界面
                            Log.e("数据和缓存一致：", resultJson);
                            return;
                        }
                    }
                }

                callBack.onSuccess(resultJson);
                Log.e("Get返回结果", resultJson);

                //缓存数据
                if (cache) {
                    Log.e(TAG, "放入缓存");
                    // 2.3 缓存数据
                    CacheDataUtil.cacheData(joinurl, resultJson);
                }
            }
        });
    }

    @Override
    public void post(boolean cache, Context context, String url, Map<String, Object> params, final EngineCallBack callBack) {
        String joinUrl = HttpUtils.joinParams(url, params);
        Log.d("OkHttpEngine-->Post:", joinUrl);
        RequestBody requestBody = appendBody(params);
        final Request request = new Request.Builder()
                .url(url)
                .tag(context)
                .post(requestBody)
                .build();

        okHttpClient
                .newCall(request)
                .enqueue(new Callback() {//两个回调都是在子线程中
                    @Override
                    public void onFailure(Call call, IOException e) {
                        callBack.onError(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        Log.d("Post返回结果", result);
                        callBack.onSuccess(result);
                    }
                });
    }

    /**
     * 组装post请求参数body
     */
    protected RequestBody appendBody(Map<String, Object> params) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        addParams(builder, params);
        return builder.build();
    }

    /**
     * 添加参数
     */
    private void addParams(MultipartBody.Builder builder, Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.addFormDataPart(key, params.get(key) + "");
                Object value = params.get(key);
                if (value instanceof File) {
                    // 处理文件 --> Object File
                    File file = (File) value;
                    builder.addFormDataPart(key, file.getName(), RequestBody
                            .create(MediaType.parse(guessMimeType(file
                                    .getAbsolutePath())), file));
                } else if (value instanceof List) {
                    // 代表提交的是 List集合
                    try {
                        List<File> listFiles = (List<File>) value;
                        for (int i = 0; i < listFiles.size(); i++) {
                            // 获取文件
                            File file = listFiles.get(i);
                            builder.addFormDataPart(key + i, file.getName(), RequestBody
                                    .create(MediaType.parse(guessMimeType(file
                                            .getAbsolutePath())), file));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    builder.addFormDataPart(key, value + "");
                }
            }
        }
    }

    /**
     * 猜测文件类型
     */
    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }
}
