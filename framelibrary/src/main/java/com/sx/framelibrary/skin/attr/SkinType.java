package com.sx.framelibrary.skin.attr;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.sx.framelibrary.skin.SkinManager;
import com.sx.framelibrary.skin.SkinResource;

/**
 * @Author sunxin
 * @Date 2017/8/9 14:10
 * @Description 皮肤类型的枚举类
 */

public enum SkinType {

    TEXTCOLOR("textColor") {
        @Override
        public void skin(View view, String name) {
            SkinResource skinResource = getSkinResource();
            ColorStateList color = skinResource.getColorByName(name);
            if (color == null) {
                return;
            }

            TextView textView = (TextView) view;
            textView.setTextColor(color);

        }
    }, BACKGROUND("background") {
        @Override
        public void skin(View view, String name) {
            //drawable 可能是图片，也可能是颜色
            SkinResource skinResource = getSkinResource();
//            Drawable drawable = skinResource.getDrawableByName(name);
//            if (drawable != null) {
//                ImageView imageView = (ImageView) view;
//                imageView.setImageDrawable(drawable);
//                return;
//            }
            Logger.e("ColorResName--->"+name);
            //可能是颜色
            ColorStateList color = skinResource.getColorByName(name);
            Logger.e("color-->" + color);
            if (color != null) {
                view.setBackgroundColor(color.getDefaultColor());
            }

        }
    }, SRC("src") {
        @Override
        public void skin(View view, String name) {
//            获取资源设置
            SkinResource skinResource = getSkinResource();
            Drawable drawable = skinResource.getDrawableByName(name);
            if (drawable != null) {
                ImageView imageView = (ImageView) view;
                imageView.setImageDrawable(drawable);
                return;
            }
        }
    };

    private String mResName;

    SkinType(String resName) {
        this.mResName = resName;
    }

    public abstract void skin(View view, String name);

    public String getResName() {
        return mResName;
    }

    public SkinResource getSkinResource() {
        return SkinManager.getInstance().getSkinResource();
    }
}
