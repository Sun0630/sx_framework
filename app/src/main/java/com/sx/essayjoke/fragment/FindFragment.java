package com.sx.essayjoke.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.sx.baselibrary.base.BaseFragment;
import com.sx.baselibrary.http.HttpUtils;
import com.sx.baselibrary.ioc.ViewById;
import com.sx.essayjoke.R;
import com.sx.essayjoke.adapter.DiscoveryListAdapter;
import com.sx.essayjoke.model.DiscoverListResult;
import com.sx.framelibrary.HttpCallBack;
import com.sx.framelibrary.adapter.OnItemClickListener;
import com.sx.framelibrary.banner.BannerAdapter;
import com.sx.framelibrary.banner.BannerView;
import com.sx.framelibrary.banner.BannerViewPager;
import com.sx.framelibrary.view.WrapRecyclerView;

import java.util.List;

/**
 * @Author sunxin
 * @Date 2017/8/17 17:42
 * @Description 发现
 */

public class FindFragment extends BaseFragment implements OnItemClickListener, BannerViewPager.BannerItemClickListener {

    @ViewById(R.id.recycler_view)
    private WrapRecyclerView mRecyclerView;

    @Override
    protected void initData() {
        HttpUtils
                .with(mContext)
                .url("http://is.snssdk.com/2/essay/discovery/v3/")//路径和参数都需要放入到jni中
                .cache(true)
                .addParams("iid", "6152551759")
                .addParams("aid", "7")
                .excute(new HttpCallBack<DiscoverListResult>() {
                    @Override
                    public void onSuccess(DiscoverListResult result) {
                        showListData(result.getData().getCategories().getCategory_list());

                        addBannerView(result.getData().getRotate_banner().getBanners());
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
    }

    /**
     * 添加轮播
     *
     * @param banners
     */
    private void addBannerView(final List<DiscoverListResult.DataBean.RotateBannerBean.BannersBean> banners) {

        if (banners.size()<=0){
            return;
        }

        Logger.e("banners.size-->"+banners.size());

        BannerView bannerView = (BannerView) LayoutInflater.from(mContext)
                .inflate(R.layout.layout_banner_view, mRecyclerView, false);
        mRecyclerView.addHeaderView(bannerView);
        bannerView.setAdapter(new BannerAdapter() {
            @Override
            public View getView(int position, View convertView) {
                if (convertView == null) {
                    convertView = new ImageView(mContext);
                }
                ((ImageView) convertView).setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide
                        .with(mContext)
                        .load(banners.get(position).getBanner_url().getUrl_list().get(0).getUrl())
                        .into((ImageView) convertView);
                return convertView;
            }

            @Override
            public int getCount() {
                return banners.size();
            }

            @Override
            public String getBannerDesc(int position) {
                return banners.get(position).getBanner_url().getTitle();
            }
        });

        bannerView.setOnBannerItemClickListener(this);

    }

    /**
     * 显示列表
     *
     * @param list
     */
    private void showListData(List<DiscoverListResult.DataBean.CategoriesBean.CategoryListBean> list) {
        DiscoveryListAdapter discoveryListAdapter = new DiscoveryListAdapter(mContext, list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(discoveryListAdapter);
        discoveryListAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_discovery;
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void click(int position) {
        Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
    }
}
