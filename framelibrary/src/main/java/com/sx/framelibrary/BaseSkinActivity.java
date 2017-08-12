package com.sx.framelibrary;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.VectorEnabledTintResources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;

import com.sx.baselibrary.base.BaseActivity;
import com.sx.framelibrary.skin.SkinManager;
import com.sx.framelibrary.skin.attr.SkinAttr;
import com.sx.framelibrary.skin.attr.SkinView;
import com.sx.framelibrary.skin.support.SkinAppCompatViewInflater;
import com.sx.framelibrary.skin.support.SkinAttrSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author sunxin
 * @Date 2017/3/22 9:24
 * @Description 中间层插件换肤
 */

public abstract class BaseSkinActivity extends BaseActivity implements LayoutInflaterFactory {
    private static final String TAG = "BaseSkinActivity";

    private SkinAppCompatViewInflater mAppCompatViewInflater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        if (layoutInflater.getFactory() == null) {
            LayoutInflaterCompat.setFactory(layoutInflater, this);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        //可以在这里面拦截View的创建,然后需要去解析
        //1,创建View
        // If the Factory didn't handle it, let our createView() method try
        View view = createView(parent, name, context, attrs);
        Log.e(TAG, "BaseSkinActivity-->onCreateView: " + view);
        Log.e(TAG, "BaseSkinActivity-->onParentView: " + parent);

        //2,解析属性  textColor,src,backageground,自定义的属性
        // 一个Activity的布局肯定对应多个SkinView
        if (view != null) {
            List<SkinAttr> skinAttrs = SkinAttrSupport.getSkinAttrs(context, attrs);
            SkinView skinView = new SkinView(view, skinAttrs);
            //3,交由SkinManager 统一管理
            managerSkinView(skinView);
        }
        return view;
    }

    /**
     * 统一管理SkinView
     *
     * @param view
     */
    private void managerSkinView(SkinView view) {
        List<SkinView> skinViews = SkinManager.getInstance().getSkinViews(this);
        if (skinViews == null) {
            skinViews = new ArrayList<>();
            SkinManager.getInstance().regisetr(this, skinViews);
        }
        skinViews.add(view);
    }


    public View createView(View parent, final String name, @NonNull Context context,
                           @NonNull AttributeSet attrs) {
        final boolean isPre21 = Build.VERSION.SDK_INT < 21;

        if (mAppCompatViewInflater == null) {
            mAppCompatViewInflater = new SkinAppCompatViewInflater();
        }

        // We only want the View to inherit its context if we're running pre-v21
        final boolean inheritContext = isPre21 && shouldInheritContext((ViewParent) parent);

        return mAppCompatViewInflater.createView(parent, name, context, attrs, inheritContext,
                isPre21, /* Only read android:theme pre-L (L+ handles this anyway) */
                true, /* Read read app:theme as a fallback at all times for legacy reasons */
                VectorEnabledTintResources.shouldBeUsed() /* Only tint wrap the context if enabled */
        );
    }

    private boolean shouldInheritContext(ViewParent parent) {
        if (parent == null) {
            // The initial parent is null so just return false
            return false;
        }
        final View windowDecor = getWindow().getDecorView();
        while (true) {
            if (parent == null) {
                // Bingo. We've hit a view which has a null parent before being terminated from
                // the loop. This is (most probably) because it's the root view in an inflation
                // call, therefore we should inherit. This works as the inflated layout is only
                // added to the hierarchy at the end of the inflate() call.
                return true;
            } else if (parent == windowDecor || !(parent instanceof View)
                    || ViewCompat.isAttachedToWindow((View) parent)) {
                // We have either hit the window's decor view, a parent which isn't a View
                // (i.e. ViewRootImpl), or an attached view, so we know that the original parent
                // is currently added to the view hierarchy. This means that it has not be
                // inflated in the current inflate() call and we should not inherit the context.
                return false;
            }
            parent = parent.getParent();
        }
    }
}
