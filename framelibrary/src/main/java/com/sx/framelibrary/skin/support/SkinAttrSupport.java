package com.sx.framelibrary.skin.support;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.sx.framelibrary.skin.attr.SkinAttr;
import com.sx.framelibrary.skin.attr.SkinType;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author sunxin
 * @Date 2017/8/9 14:08
 * @Description 皮肤属性支持类
 */

public class SkinAttrSupport {

    public static final String TAG = "SkinAttrSupport";

    public static List<SkinAttr> getSkinAttrs(Context context, AttributeSet attrs) {
        //解析  background  src   textcolor
        List<SkinAttr> attrList = new ArrayList<>();
        for (int index = 0; index < attrs.getAttributeCount(); index++) {
            String name = attrs.getAttributeName(index);
            String value = attrs.getAttributeValue(index);
            Log.e(TAG, "attrName --> " + name + "  attrValue --> " + value);
            //只获取我们需要的属性
            SkinType skinType = getSkinType(name);
            if (skinType != null) {
                // 资源名称 --> @17170461  要把资源id转换成文字
                String resName = getResName(context, value);
                Logger.d("resName-->" + resName);
                if (TextUtils.isEmpty(resName)) {
                    continue;
                }
                SkinAttr skinAttr = new SkinAttr(skinType, resName);
                attrList.add(skinAttr);
            }

        }
        return attrList;
    }

    /**
     * 把资源id转换成资源名称
     *
     * @param context
     * @param value
     * @return
     */
    private static String getResName(Context context, String value) {

        if (value.startsWith("@")) {
            value = value.substring(1);
            int resId = Integer.parseInt(value);
            return context.getResources().getResourceEntryName(resId);
        }

        return null;
    }

    /**
     * 通过名称获取SkinType
     *
     * @param name
     * @return
     */
    private static SkinType getSkinType(String name) {
        SkinType[] skinTypes = SkinType.values();

        for (SkinType type : skinTypes) {
            if (type.getResName().equals(name)) {
                Logger.e("进来了"+type.getResName());
                return type;
            }
        }
        return null;
    }
}
