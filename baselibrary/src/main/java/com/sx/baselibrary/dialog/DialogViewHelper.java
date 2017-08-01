package com.sx.baselibrary.dialog;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * @Author sunxin
 * @Date 2017/5/18 19:13
 * @Description Dialog辅助类
 */

class DialogViewHelper {

    private View mContentView = null;
    //使用弱引用，防止内存泄漏
    private SparseArray<WeakReference<View>> mViews;

    public DialogViewHelper(Context context, int layoutResId) {
        this();
        mContentView = LayoutInflater.from(context).inflate(layoutResId, null);
    }

    public DialogViewHelper() {
        mViews = new SparseArray<>();
    }

    /**
     * 设置布局
     *
     * @param view
     */
    public void setContentView(View view) {
        this.mContentView = view;
    }

    /**
     * 设置文字
     *
     * @param viewId
     * @param sequence
     */
    public void setText(int viewId, CharSequence sequence) {
        //减少findViewbyid的次数，通过缓存，将本次传过来的viewid存放在SparseArray中，下次取的时候先判断
        TextView tv = getView(viewId);

        if (tv != null) {
            tv.setText(sequence);
        }
    }

    /**
     * 设置点击事件
     *
     * @param viewId
     * @param listener
     */
    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        if (view != null) {

            view.setOnClickListener(listener);
        }
    }


    public View getContentView() {
        return mContentView;
    }

    public <T extends View> T getView(int viewId) {
        WeakReference<View> viewWeakReference = mViews.get(viewId);
        View view = null;
        if (viewWeakReference != null) {
            view = viewWeakReference.get();
        }
        if (view == null) {
            view = mContentView.findViewById(viewId);
            //如果viewID传的不对
            if (view != null) {
                mViews.put(viewId, new WeakReference<>(view));
            }
        }
        return (T) view;
    }
}
