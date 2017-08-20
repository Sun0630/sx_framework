package com.sx.essayjoke.imageselector;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sx.essayjoke.R;
import com.sx.essayjoke.bean.ImageEntity;
import com.sx.framelibrary.adapter.CommonRecyclerAdapter;
import com.sx.framelibrary.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author sunxin
 * @Date 2017/8/20 0:40
 * @Description
 */

public class SelectImageAdapter extends CommonRecyclerAdapter<ImageEntity> {


    private static final String TAG = "SelectImageAdapter";
    private ArrayList<String> mResultList;
    private int mMaxCount;//最多可选的张数
    private ImageView mImageView;

    public SelectImageAdapter(Context context, List<ImageEntity> data, ArrayList<String> resultList
            ,int maxCount) {
        super(context, data, R.layout.media_choose_item);
        mResultList = resultList;
        mMaxCount = maxCount;
    }


    @Override
    public void convert(ViewHolder holder, final ImageEntity item) {

        if (TextUtils.isEmpty(item.getPath())) {
            //显示拍照
            holder.setViewVisibility(R.id.camera_ll, View.VISIBLE);
            holder.setViewVisibility(R.id.media_select_indicator, View.INVISIBLE);
            holder.setViewVisibility(R.id.image_view, View.INVISIBLE);

            //打开相机
            holder.setOnIntemClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //调用相机拍照，注意权限

                }
            });

        } else {
            //显示图片
            holder.setViewVisibility(R.id.camera_ll, View.INVISIBLE);
            holder.setViewVisibility(R.id.media_select_indicator, View.VISIBLE);
            holder.setViewVisibility(R.id.image_view, View.VISIBLE);

            //Glide加载图片
            mImageView = holder.getView(R.id.image_view);
            Glide
                    .with(mContext)
                    .load(item.getPath())
                    .into(mImageView);

            final ImageView ivIndicator = holder.getView(R.id.media_select_indicator);
            if (mResultList.contains(item.getPath())) {
                ivIndicator.setSelected(true);
            } else {
                ivIndicator.setSelected(false);
            }

            //设置条目点击事件
            holder.setOnIntemClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //判断传递过来的图片有没有在列表
                    if (mResultList.contains(item.getPath())) {
                        mResultList.remove(item.getPath());
                    } else {
                        //判断最多可选的张数
                        if (mResultList.size()>=mMaxCount){
                            Toast.makeText(mContext, "最多可选"+mMaxCount+"张图片", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mResultList.add(item.getPath());
                    }

                    notifyDataSetChanged();

                    //通知改变布局
                    if (mSelectedListener != null) {
                        mSelectedListener.select();
                    }
                }
            });
        }
    }

    private ImageSelectedListener mSelectedListener;
    public void setOnImageSelectedLisener(ImageSelectedListener lisener){
        this.mSelectedListener = lisener;
    }

}
