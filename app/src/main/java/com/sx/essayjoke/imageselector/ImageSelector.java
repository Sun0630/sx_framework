package com.sx.essayjoke.imageselector;

import android.app.Activity;
import android.content.Intent;

import java.util.ArrayList;

/**
 * @Author sunxin
 * @Date 2017/8/20 15:33
 * @Description 图片选择器链式调用
 */

public class ImageSelector {

    private int mMaxCount = 9;//多选9张
    private int mMode = SelectImageActivity.MODE_MULTI;//多选模式
    private boolean mShowCamera = true;//显示照相机
    private ArrayList<String> mOriginData;//原始数据

    public ImageSelector() {

    }

    public static ImageSelector create() {
        return new ImageSelector();
    }

    /**
     * 单选模式
     *
     * @return
     */
    public ImageSelector single() {
        mMode = SelectImageActivity.MODE_SINGLE;
        return this;
    }

    /**
     * 多选模式
     *
     * @return
     */
    public ImageSelector Multi() {
        mMode = SelectImageActivity.MODE_MULTI;
        return this;
    }

    /**
     * 是否显示相机
     *
     * @param isShow
     * @return
     */
    public ImageSelector showCamera(boolean isShow) {
        mShowCamera = isShow;
        return this;
    }

    /**
     * 原来选好的图片列表
     *
     * @param originData
     * @return
     */
    public ImageSelector origin(ArrayList<String> originData) {
        mOriginData = originData;
        return this;
    }

    /**
     * 启动执行
     *
     * @param activity
     * @param requestCode
     */
    public void start(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, SelectImageActivity.class);
        addParamsByIntent(intent);
        activity.startActivityForResult(intent, requestCode);
    }

    private void addParamsByIntent(Intent intent) {
        if (mOriginData != null && mMode == SelectImageActivity.MODE_MULTI) {
            intent.putStringArrayListExtra(SelectImageActivity.EXTRA_DEFAULT_SELECT_LIST, mOriginData);
        }
        intent.putExtra(SelectImageActivity.EXTRA_SELECT_COUNT, mMaxCount);
        intent.putExtra(SelectImageActivity.EXTRA_SELECT_MODE, mMode);
        intent.putExtra(SelectImageActivity.EXTRA_SHOW_CAMERA, mShowCamera);
    }

}
