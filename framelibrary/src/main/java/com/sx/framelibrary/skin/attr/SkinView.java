package com.sx.framelibrary.skin.attr;

import android.view.View;

import java.util.List;

/**
 * @Author sunxin
 * @Date 2017/8/9 14:09
 * @Description
 */

public class SkinView {
    private View mView;

    private List<SkinAttr> mAttrs;

    public SkinView(View view, List<SkinAttr> attrs) {
        this.mView = view;
        this.mAttrs = attrs;
    }

    public void skin() {
        for (SkinAttr attr : mAttrs) {
            attr.skin(mView);
        }
    }
}
