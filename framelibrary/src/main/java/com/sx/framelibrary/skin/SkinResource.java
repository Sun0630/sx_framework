package com.sx.framelibrary.skin;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.orhanobut.logger.Logger;

import java.lang.reflect.Method;

/**
 * @Author sunxin
 * @Date 2017/8/9 14:09
 * @Description 皮肤资源管理
 */

public class SkinResource {

    private static final String TAG = "SkinResource";
    private Resources mSkinResources;
    private String mPackageName;

    public SkinResource(Context context, String path) {
        try {
            Resources superRes = context.getResources();
            //通过反射拿到AssetManager的实例
            AssetManager assets = AssetManager.class.newInstance();
            //通过反射调用addAssetPath
            Method method = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            //执行方法
            method.invoke(assets, path);

            mSkinResources = new Resources(assets, superRes.getDisplayMetrics(), superRes.getConfiguration());
            Logger.e("path-->" + path);
            //根据路径获取包名
            mPackageName = context.getPackageManager()
                    .getPackageArchiveInfo(path, PackageManager.MATCH_UNINSTALLED_PACKAGES)
                   .packageName;

            Logger.e("mPackageName-->" + mPackageName);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 根据名称获取Drawable
     *
     * @param resName
     * @return
     */
    public Drawable getDrawableByName(String resName) {
        Logger.d("getDrawableByName-->resName-->" + resName);
        try {
            int resId = mSkinResources.getIdentifier(resName, "drawable", mPackageName);
            Log.e(TAG, "resId --> " + resId + "  packageName --> " + mPackageName);
            Drawable drawable = mSkinResources.getDrawable(resId);
            return drawable;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据名称获取Color
     *
     * @param resName
     * @return
     */
    public ColorStateList getColorByName(String resName) {
        try {
            Logger.d("getColorByName-->resName-->" + resName);
            int resId = mSkinResources.getIdentifier(resName, "color", mPackageName);
            Log.e(TAG, "colorStateList-->resId --> " + resId + "  packageName --> " + mPackageName);
            ColorStateList colorStateList = mSkinResources.getColorStateList(resId);
            return colorStateList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
