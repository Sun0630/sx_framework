package com.sx.baselibrary.navigation;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @Author sunxin
 * @Date 2017/8/2 17:50
 * @Description 使用Builder设计模式构建头部基类
 */

public abstract class AbsNavigationBar<P extends AbsNavigationBar.Builder.AbsNavigationParams> implements INavigationBar {

    private P mParams;
    private View mNavigationView;

    public AbsNavigationBar(P params) {
        mParams = params;
        createAndBindView();
    }

    public P getParams() {
        return mParams;
    }

    /**
     * 设置头部标题文字和右侧文字
     *
     * @param viewId
     * @param text
     */
    protected void setText(int viewId, String text) {
        TextView textView = findViewById(viewId);
        if (!TextUtils.isEmpty(text)) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
        }
    }

    /**
     * 设置点击事件
     *
     * @param viewId
     * @param listener
     */
    protected void setOnClickListener(int viewId, View.OnClickListener listener) {
        findViewById(viewId).setOnClickListener(listener);
    }


    /**
     * 设置右侧图标
     *
     * @param viewId
     * @param imgRes
     */
    protected void setRightIcon(int viewId, int imgRes) {
        TextView textView = findViewById(viewId);
        if (textView!=null){
            textView.setVisibility(View.VISIBLE);
            textView.setBackgroundResource(imgRes);
        }
    }

    /**
     * 简化 findViewById不用再强转了
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T findViewById(int viewId) {
        return (T) mNavigationView.findViewById(viewId);
    }

    /**
     * 创建和绑定View
     */
    private void createAndBindView() {

        //由于调用的时候没有传入根布局id，所以需要遍历布局找到根布局
        if (mParams.mParent == null) {
            ViewGroup rootActivity = (ViewGroup) ((Activity) mParams.mContext).findViewById(android.R.id.content);
            mParams.mParent = (ViewGroup) rootActivity.getChildAt(0);
        }

        if (mParams.mParent == null){
            return;
        }

        //1，创建View
        mNavigationView = LayoutInflater.from(mParams.mContext).inflate(bindLayoutId(), mParams.mParent, false);
        //2，添加到ViewGroup
        mParams.mParent.addView(mNavigationView, 0);

        applyView();
    }


    //Builder    Params

    public abstract static class Builder {

        AbsNavigationParams P;

        public abstract AbsNavigationBar build();

        public Builder(Context context, ViewGroup parent) {
            //创建Params
            P = new AbsNavigationParams(context, parent);
        }


        public static class AbsNavigationParams {

            public Context mContext;
            public ViewGroup mParent;

            public AbsNavigationParams(Context context, ViewGroup parent) {

                mContext = context;
                mParent = parent;
            }
        }
    }
}
