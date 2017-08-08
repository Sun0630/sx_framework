package com.sx.framelibrary.http;

/**
 * @Author sunxin
 * @Date 2017/8/7 9:52
 * @Description 缓存的实体类
 */

public class CacheData {
    public String url;//key
    public String jsonString;//value

    public CacheData(){

    }

    public CacheData(String url, String jsonString) {
        this.url = url;
        this.jsonString = jsonString;
    }
}
