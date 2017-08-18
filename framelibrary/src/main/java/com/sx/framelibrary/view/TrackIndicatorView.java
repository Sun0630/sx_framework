package com.sx.framelibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

import com.sx.framelibrary.R;
import com.sx.framelibrary.indicator.IndicatorAdapter;

import java.util.List;

/**
 * @Author sunxin
 * @Date 2017/8/18 16:57
 * @Description  横向滑动的布局
 */

public class TrackIndicatorView extends HorizontalScrollView implements ViewPager.OnPageChangeListener {
    // Adapter适配器设计模式 - Adapter
    private IndicatorAdapter mAdapter;

    // 3. 动态的添加View - 指示器条目的容器
    private IndicatorGroupView mIndicatorGroup;

    // 4.指定Item的宽度 - 一屏幕可见显示多少个
    private int mTabVisibleNums = 0;

    // 4.指定Item的宽度 - Item的宽度
    private int mItemWidth;

    // 5.当前指示器需要居中 - ViewPager
    private ViewPager mViewPager;

    // 点击或者切换的时候改变状态 - 当前位置
    private int mCurrentPosition = 0;
    // 8.解决点击抖动的问题
    private boolean mIsExecuteScroll = false;

    private List<ColorTrackTextView> mIndicators;

    private boolean mSmoothScroll;

    public TrackIndicatorView(Context context) {
        this(context, null);
    }

    public TrackIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TrackIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 3.初始化指示器条目的容器
        mIndicatorGroup = new IndicatorGroupView(context);
        addView(mIndicatorGroup);

        // 4.指定Item的宽度  ， 自定义属性
        initAttribute(context, attrs);
    }

    /**
     * 4.初始化自定义属性
     */
    private void initAttribute(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TrackIndicatorView);
        mTabVisibleNums = array.getInt(R.styleable.TrackIndicatorView_tabVisibleNums, mTabVisibleNums);
        // 回收
        array.recycle();
    }

    /**
     * 1.设置一个适配器
     */
    public void setAdapter(IndicatorAdapter adapter) {
        if (adapter == null) {
            throw new NullPointerException("adapter is null!");
        }
        this.mAdapter = adapter;

        // 动态添加View  获取有多少条数据
        int itemCount = mAdapter.getCount();
        // 循环添加ItemView
        for (int i = 0; i < itemCount; i++) {
            View itemView = mAdapter.getView(i, mIndicatorGroup);
            mIndicatorGroup.addItemView(itemView);
            // 6.Indicator与ViewPager一起联动 设置点击事件
            if (mViewPager != null) {
                switchItemClick(itemView, i);
            }
        }
        // 4. 指定Item的宽度  getWidth() = 0
        // int width = getWidth();

        // 7.点击或者切换的时候改变状态 默认点亮第一个位置
        mAdapter.highLightIndicator(mIndicatorGroup.getItemAt(0));
    }

    /**
     * 6.Indicator与ViewPager一起联动 设置点击事件
     */
    private void switchItemClick(View itemView, final int position) {
        itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(position,mSmoothScroll);
                // 移动IndicatorView
                smoothScrollIndicator(position);
                // 移动下标
                mIndicatorGroup.scrollBottomTrack(position);
            }
        });
    }

    /**
     * 8. 点击移动 带动画
     */
    private void smoothScrollIndicator(int position) {
        // 当前总共的位置
        float totalScroll = (position) * mItemWidth;
        // 左边的偏移
        int offsetScroll = (getWidth() - mItemWidth) / 2;
        // 最终的一个偏移量
        final int finalScroll = (int) (totalScroll - offsetScroll);
        // 调用ScrollView自带的方法  而且带动画
        smoothScrollTo(finalScroll, 0);
    }

    /**
     * 5.设置适配器
     */
    public void setAdapter(IndicatorAdapter adapter, ViewPager viewPager) {
        setAdapter(adapter,viewPager,true);
    }

    /**
     * 5.设置适配器
     */
    public void setAdapter(IndicatorAdapter adapter, ViewPager viewPager, boolean smoothScroll) {
        this.mSmoothScroll= smoothScroll;
        if (viewPager == null) {
            throw new NullPointerException("viewPager is null!");
        }
        mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(this);
        setAdapter(adapter);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (changed && mItemWidth == 0) {
            // 4. 指定Item的宽度
            mItemWidth = getItemWidth();
            // 循环指定Item的宽度
            for (int i = 0; i < mAdapter.getCount(); i++) {
                mIndicatorGroup.getItemAt(i).getLayoutParams().width = mItemWidth;
            }

            // 8.添加底部跟踪的指示器
            mIndicatorGroup.addBottomTrackView(mAdapter.getBottomTrackView(),mItemWidth);
        }
    }

    /**
     * 4. 获取Item的宽度
     */
    public int getItemWidth() {
        // 有没有指定
        int parentWidth = getWidth();
        if (mTabVisibleNums != 0) {
            return parentWidth / mTabVisibleNums;
        }

        // 没有指定
        int itemWidth = 0;

        // 获取最宽的
        int maxItemWidth = 0;

        for (int i = 0; i < mAdapter.getCount(); i++) {
            int currentItemWidth = mIndicatorGroup.getItemAt(i).getMeasuredWidth();
            maxItemWidth = Math.max(currentItemWidth, maxItemWidth);
        }
        // 宽度就是获取最宽的一个
        itemWidth = maxItemWidth;

        int allWidth = mAdapter.getCount() * itemWidth;
        // 最后算一次所有条目宽度相加是不是大于一屏幕
        if (allWidth < parentWidth) {
            itemWidth = parentWidth / mAdapter.getCount();
        }

        return itemWidth;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if(mIsExecuteScroll) {
            // 5.滚动的时候会不断的调用
            scrollCurrentIndicator(position, positionOffset);
            mIndicatorGroup.scrollBottomTrack(position, positionOffset);
            // 如果是点击就不要执行onPageScrolled这个方法
        }
    }

    /**
     * 5.不断的滚动当前的指示器
     */
    private void scrollCurrentIndicator(int position, float positionOffset) {
        // 当前总共的位置
        float totalScroll = (position + positionOffset) * mItemWidth;
        // 左边的偏移
        int offsetScroll = (getWidth() - mItemWidth) / 2;
        // 最终的一个偏移量
        final int finalScroll = (int) (totalScroll - offsetScroll);
        // 调用ScrollView自带的方法
        scrollTo(finalScroll, 0);
    }

    @Override
    public void onPageSelected(int position) {
        // 上一个位置重置，将当前位置点亮，
        mAdapter.restoreIndicator(mIndicatorGroup.getItemAt(mCurrentPosition));

        mCurrentPosition = position;
        // 将当前位置点亮
        mAdapter.highLightIndicator(mIndicatorGroup.getItemAt(mCurrentPosition));
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if(state == 1){
            mIsExecuteScroll = true;
        }

        if(state == 0){
            mIsExecuteScroll = false;
        }
    }
}
