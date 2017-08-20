package com.sx.framelibrary;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.sx.baselibrary.navigation.AbsNavigationBar;

/**
 * @Author sunxin
 * @Date 2017/8/2 18:10
 * @Description 默认的头部导航栏
 */

public class DefaultNavigationBar extends AbsNavigationBar<DefaultNavigationBar.Builder.DefaultNavigationParams> {

    public DefaultNavigationBar(Builder.DefaultNavigationParams params) {
        super(params);
    }

    @Override
    public int bindLayoutId() {
        return R.layout.title_bar;
    }

    @Override
    public void applyView() {
        //添加效果
        setText(R.id.title, getParams().mTitle);
        setText(R.id.right_text, getParams().mRightText);

        setOnClickListener(R.id.back, getParams().mLeftClickListener);
        setRightIcon(R.id.right_text, getParams().mRightIcon);
        //左边箭头
        setVisiable(R.id.back,getParams().mLeftIconVisiable);
        setColor(R.id.rl_container,getParams().mColor);
        setTextColor(R.id.title,getParams().mTextColor);
    }




    public static class Builder extends AbsNavigationBar.Builder {
        DefaultNavigationParams P;

        public Builder(Context context, ViewGroup parent) {
            super(context, parent);
            P = new DefaultNavigationParams(context, parent);
        }

        public Builder(Context context) {
            super(context, null);
            P = new DefaultNavigationParams(context, null);
        }

        @Override
        public DefaultNavigationBar build() {
            DefaultNavigationBar navigationBar = new DefaultNavigationBar(P);
            return navigationBar;
        }

        //设置所有效果

        /**
         * 设置标题
         *
         * @param title
         * @return
         */
        public DefaultNavigationBar.Builder setTitle(String title) {
            P.mTitle = title;
            return this;
        }

        /**
         * 设置右侧文本
         *
         * @param rightText
         * @return
         */
        public DefaultNavigationBar.Builder setRightText(String rightText) {
            P.mRightText = rightText;
            return this;
        }


        /**
         * 设置右侧点击事件
         *
         * @param listener
         * @return
         */
        public DefaultNavigationBar.Builder setOnRightClickListener(View.OnClickListener listener) {
            P.mRightClickListener = listener;
            return this;
        }

        /**
         * 设置左侧点击事件
         *
         * @param listener
         * @return
         */
        public DefaultNavigationBar.Builder setOnLeftClickListener(View.OnClickListener listener) {
            P.mLeftClickListener = listener;
            return this;
        }

        /**
         * 设置右侧图标
         *
         * @param rightIcon
         * @return
         */
        public DefaultNavigationBar.Builder setRightIcon(int rightIcon) {
            P.mRightIcon = rightIcon;
            return this;
        }

        /**
         * 隐藏左侧的图标
         * @return
         */
        public DefaultNavigationBar.Builder hideLeftIcon() {
            P.mLeftIconVisiable = View.INVISIBLE;
            return this;
        }

        /**
         * 设置背景颜色
         * @param color
         * @return
         */
        public DefaultNavigationBar.Builder setColor(int color) {
            P.mColor = color;
            return this;
        }

        /**
         * 设置文字颜色
         * @param textColor
         * @return
         */
        public DefaultNavigationBar.Builder setTextColor(int textColor) {
            P.mTextColor = textColor;
            return this;
        }


        public static class DefaultNavigationParams extends AbsNavigationParams {
            public String mTitle;
            public String mRightText;
            public View.OnClickListener mRightClickListener;
            public int mRightIcon;
            public int mColor;
            public View.OnClickListener mLeftClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity) mContext).finish();
                }
            };//左侧按钮点击事件写默认关闭
            public int mLeftIconVisiable = View.VISIBLE;//左侧箭头
            public int mTextColor;//文字颜色

            //所有的效果都放在这里


            public DefaultNavigationParams(Context context, ViewGroup parent) {
                super(context, parent);
            }
        }

    }

}
