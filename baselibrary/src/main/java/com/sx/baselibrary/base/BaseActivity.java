package com.sx.baselibrary.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sx.baselibrary.ioc.ViewUtils;

/**
 * @Author sunxin
 * @Date 2017/3/21 22:04
 * @Description 整个应用的基类 BaseActivity
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置Layout布局
        setContentView();
        //一些公用的
        initCommon();
        //初始化头部
        initTitle();
        //初始化布局
        initView();
        //初始化数据
        initData();


    }

    private void initCommon() {
        ViewUtils.inject(this);
    }

    /**
     * 设置布局文件
     */
    protected abstract void setContentView();

    /**
     * 初始化头部
     */
    protected abstract void initTitle();

    /**
     * 初始化视图
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 开启一个Activity
     * @param clazz
     */
    protected void startActivity(Class clazz){
        startActivity(new Intent(this,clazz));
    }

    /**
     * findViewById 简化，不用强转
     * @param viewId
     * @param <T>
     * @return
     */
    protected  <T extends View> T viewById(int viewId){
        return (T) findViewById(viewId);
    }

}
