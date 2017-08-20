package com.sx.essayjoke.bean;

import android.text.TextUtils;

/**
 * @Author sunxin
 * @Date 2017/8/20 0:23
 * @Description 图片对象实体
 */

public class ImageEntity {
    private String path;
    private String name;
    private long time;

    public ImageEntity() {
    }

    public ImageEntity(String path, String name, long time) {
        this.path = path;
        this.name = name;
        this.time = time;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ImageEntity){
            ImageEntity compare = (ImageEntity) obj;
            return TextUtils.equals(this.path,compare.path);
        }
        return false;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
