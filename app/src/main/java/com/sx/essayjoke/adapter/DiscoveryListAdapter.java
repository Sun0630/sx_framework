package com.sx.essayjoke.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;

import com.sx.essayjoke.GlideImageLoader;
import com.sx.essayjoke.R;
import com.sx.essayjoke.model.DiscoverListResult;
import com.sx.framelibrary.adapter.CommonRecyclerAdapter;
import com.sx.framelibrary.adapter.ViewHolder;

import java.util.List;

/**
 * @Author sunxin
 * @Date 2017/8/18 21:24
 * @Description 发现列表的适配器
 */

public class DiscoveryListAdapter extends CommonRecyclerAdapter<DiscoverListResult.DataBean.CategoriesBean.CategoryListBean> {
    public DiscoveryListAdapter(Context context, List<DiscoverListResult.DataBean.CategoriesBean.CategoryListBean> data) {
        super(context, data, R.layout.channel_list_item);
    }

    @Override
    public void convert(ViewHolder holder, DiscoverListResult.DataBean.CategoriesBean.CategoryListBean item) {
        //显示数据
        String str = item.getSubscribe_count() + "订阅 | 总帖数 <font color='#FF678D'>" + item.getTotal_updates() + "</font>";
        holder.setText(R.id.channel_text, item.getName())
                .setText(R.id.channel_topic, item.getIntro())
                .setText(R.id.channel_update_info, Html.fromHtml(str));

        //判断是否是推荐
        if (item.isIs_recommend()){
            holder.setViewVisibility(R.id.recommend_label, View.VISIBLE);
        }else {
            holder.setViewVisibility(R.id.recommend_label, View.GONE);
        }

        //加载图片
        holder.setImageByUrl(R.id.channel_icon,new GlideImageLoader(item.getIcon_url()));
    }


}
