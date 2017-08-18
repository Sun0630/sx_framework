package com.sx.essayjoke.fragment;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.ViewGroup;

import com.sx.baselibrary.base.BaseFragment;
import com.sx.baselibrary.ioc.ViewById;
import com.sx.essayjoke.R;
import com.sx.framelibrary.indicator.IndicatorAdapter;
import com.sx.framelibrary.view.ColorTrackTextView;
import com.sx.framelibrary.view.TrackIndicatorView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author sunxin
 * @Date 2017/8/17 17:42
 * @Description
 */

public class HomeFragment extends BaseFragment {

    private String[] items = {"直播", "推荐", "视频", "段友秀", "图片", "段子", "精华", "同城", "游戏"};
    private List<ColorTrackTextView> mIndicators;
    @ViewById(R.id.indicator_view)
    TrackIndicatorView mTrackViewContainer;

    @ViewById(R.id.view_page)
    ViewPager mViewPager;

    @Override
    protected void initData() {
        initIndicator();
        initViewPager();
    }

    private void initIndicator() {
        mIndicators = new ArrayList<>();
        mTrackViewContainer.setAdapter(new IndicatorAdapter<ColorTrackTextView>() {
            @Override
            public int getCount() {
                return items.length;
            }

            @Override
            public ColorTrackTextView getView(int position, ViewGroup parent) {

                ColorTrackTextView colorTrackTextView = new ColorTrackTextView(getActivity());
                // 设置颜色
                colorTrackTextView.setTextSize(14);
                colorTrackTextView.setGravity(Gravity.CENTER);
                colorTrackTextView.setChangeColor(Color.RED);
                colorTrackTextView.setText(items[position]);
//                colorTrackTextView.setTextColor(Color.BLACK);
                int padding = 20;
                colorTrackTextView.setPadding(padding, padding, padding, padding);
                mIndicators.add(colorTrackTextView);
                return colorTrackTextView;
            }

            @Override
            public void highLightIndicator(ColorTrackTextView view) {
                view.setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT);
                view.setCurrentProgress(1);
            }

            @Override
            public void restoreIndicator(ColorTrackTextView view) {
                view.setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT);
                view.setCurrentProgress(0);
            }
        }, mViewPager, true);


    }

    private void initViewPager() {
        mViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return ItemFragment.newInstance(items[position]);
            }

            @Override
            public int getCount() {
                return items.length;
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset > 0) {
                    //获取左边
                    ColorTrackTextView left = mIndicators.get(position);
                    left.setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT);
                    left.setCurrentProgress(1-positionOffset);

                    try {
                        //右侧
                        ColorTrackTextView right = mIndicators.get(position + 1);
                        right.setDirection(ColorTrackTextView.Direction.LEFT_TO_RIGHT);
                        right.setCurrentProgress(positionOffset);
                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }
}
