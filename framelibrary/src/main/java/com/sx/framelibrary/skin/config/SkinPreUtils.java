package com.sx.framelibrary.skin.config;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @Author sunxin
 * @Date 2017/8/12 14:46
 * @Description 操作SP的工具类
 */

public class SkinPreUtils {
    //搞一个单例
    private static SkinPreUtils mInstance;
    private Context mContext;

    private SkinPreUtils(Context context) {

        mContext = context;
    }

    public static SkinPreUtils getInstance(Context context) {
        if (mInstance == null) {
            synchronized (SkinPreUtils.class) {
                if (mInstance == null) {
                    return new SkinPreUtils(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 保存当前路径
     *
     * @param skinPath
     */
    public void saveSkinPath(String skinPath) {
        SharedPreferences preferences = mContext.getSharedPreferences(SkinConfig.SKIN_INFO_NAME, Context.MODE_PRIVATE);
        preferences.edit().putString(SkinConfig.SKIN_PATH_NAME, skinPath).apply();
    }


    /**
     * 获取当前皮肤路径
     *
     * @return
     */
    public String getSkinPath() {
        String skinPath = mContext.getSharedPreferences(SkinConfig.SKIN_INFO_NAME, Context.MODE_PRIVATE)
                .getString(SkinConfig.SKIN_PATH_NAME, "");
        return skinPath;
    }


    /**
     * 清空皮肤信息
     */
    public void clearSkinInfo() {
        saveSkinPath("");
    }
}
