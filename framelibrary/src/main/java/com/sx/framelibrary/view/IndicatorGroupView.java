package com.sx.framelibrary.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


public class IndicatorGroupView extends FrameLayout {

    // 3. 动态的添加View - 指示器条目的容器
    private LinearLayout mIndicatorGroup;

    private View mBottomTrackView;

    // 一个条目的宽度
    private int mItemWidth;

    // 底部指示器的LayoutParams
    LayoutParams mTrackParams;

    private int mInitLeftMargin;

    public IndicatorGroupView(Context context) {
        this(context, null);
    }

    public IndicatorGroupView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorGroupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 3.初始化指示器条目的容器
        mIndicatorGroup = new LinearLayout(context);
        addView(mIndicatorGroup);
    }


    /**
     * 添加ItemView
     */
    public void addItemView(View itemView) {
        mIndicatorGroup.addView(itemView);
    }

    /**
     * 获取当前位置的Item
     */
    public View getItemAt(int position) {
        return mIndicatorGroup.getChildAt(position);
    }

    /**
     * 8.添加底部跟踪的指示器
     */
    public void addBottomTrackView(View bottomTrackView, int itemWidth) {
        if (bottomTrackView == null) {
            return;
        }
        this.mItemWidth = itemWidth;

        this.mBottomTrackView = bottomTrackView;
        // 添加底部跟踪的View
        addView(mBottomTrackView);

        // 要让他在底部   宽度 -> 一个条目的宽度

        // 让其在底部
        mTrackParams = (LayoutParams) mBottomTrackView.getLayoutParams();
        mTrackParams.gravity = Gravity.BOTTOM;
        // 宽度 -> 一个条目的宽度  如果用户设置死了 那么就用用户的值如 88 如果没有那么就用mItemWidth
        int trackWidth = mTrackParams.width;

        // 没有设置宽度
        if (mTrackParams.width == ViewGroup.LayoutParams.MATCH_PARENT) {
            trackWidth = mItemWidth;
        }

        // 设置的宽度过大
        if (trackWidth > mItemWidth) {
            trackWidth = mItemWidth;
        }

        // 最后确定宽度
        mTrackParams.width = trackWidth;

        // 确保在最中间
        mInitLeftMargin = (mItemWidth - trackWidth) / 2;
        mTrackParams.leftMargin = mInitLeftMargin;
    }

    /**
     * 8.滚动底部的指示器  -> leftMargin
     */
    public void scrollBottomTrack(int position, float positionOffset) {
        if (mBottomTrackView == null) {
            return;
        }
        int leftMargin = (int) ((position + positionOffset) * mItemWidth);
        // 控制leftMargin去移动
        mTrackParams.leftMargin = leftMargin + mInitLeftMargin;
        mBottomTrackView.setLayoutParams(mTrackParams);
        // ? 点击的时候会发现很奇怪
    }

    /**
     * 8.滚动底部的指示器 点击移动带动画 -> leftMargin
     */
    public void scrollBottomTrack(int position) {
        if (mBottomTrackView == null) {
            return;
        }
        // 最终要移动的位置
        int finalLeftMargin = (int) ((position) * mItemWidth) + mInitLeftMargin;
        // 当前的位置
        int currentLeftMargin = mTrackParams.leftMargin;
        // 移动的距离
        int distance = finalLeftMargin - currentLeftMargin;

        // 带动画
        ValueAnimator animator = ObjectAnimator.ofFloat(currentLeftMargin, finalLeftMargin).setDuration((long) (Math.abs(distance) * 0.4f));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 会不断的回掉这个方法 不断的设置leftMrgin
                float currentLeftMargin = (float) animation.getAnimatedValue();
                mTrackParams.leftMargin = (int) currentLeftMargin;
                mBottomTrackView.setLayoutParams(mTrackParams);
            }
        });
        // 多介绍一个方法   差值器  让速度越来越慢
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }
}
