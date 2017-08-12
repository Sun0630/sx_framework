package com.sx.framelibrary.skin.attr;

import android.view.View;

/**
 * @Author sunxin
 * @Date 2017/8/9 14:10
 * @Description
 */

public class SkinAttr {
    private SkinType mTypes;
    private String mResName;

    public SkinAttr(SkinType type, String name) {
        this.mTypes = type;
        this.mResName = name;
    }

    public void skin(View view) {
        mTypes.skin(view,mResName);
    }
}
