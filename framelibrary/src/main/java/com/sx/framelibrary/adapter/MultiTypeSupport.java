package com.sx.framelibrary.adapter;

/**
 * Created by Darren on 2016/12/28.
 * Email: 240336124@qq.com
 * Description:  多布局支持接口
 */
public interface MultiTypeSupport<T> {
    // 根据当前位置或者条目数据返回布局
    int getLayoutId(T item, int position);
}
