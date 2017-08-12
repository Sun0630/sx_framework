package com.sx.framelibrary.skin.config;

/**
 * @Author sunxin
 * @Date 2017/8/12 14:50
 * @Description SP的一些配置信息
 */

public class SkinConfig {
    //sp 的文件名称
    public static final String SKIN_INFO_NAME = "skinInfo";
    // 皮肤文件的路径名称
    public static final String SKIN_PATH_NAME = "skinPath";
    //改变成默认皮肤
    public static final int SKIN_CHANGE_DEFAULT = 1;
    //不做任何改变
    public static final int SKIN_CHANGE_NOTHING = -1;
    //皮肤文件不存在
    public static final int SKIN_FILE_NOT_EXISTS = -2;
    //皮肤文件不是apk类型，有错误
    public static final int SKIN_FILE_ERROR = -3;
}
