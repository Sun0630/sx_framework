package com.sx.baselibrary.navigation;

/**
 * @Author sunxin
 * @Date 2017/8/2 17:47
 * @Description 头部导航栏的规范
 */

public interface INavigationBar {
    /**
     * 绑定布局ID
     *
     * @return
     */
    int bindLayoutId();

    /**
     * 绑定头部参数
     */
    void applyView();
}
