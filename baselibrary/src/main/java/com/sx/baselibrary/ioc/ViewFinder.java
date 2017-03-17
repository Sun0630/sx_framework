package com.sx.baselibrary.ioc;

import android.app.Activity;
import android.view.View;

/**
 * @Author Sunxin
 * @Date 2017/3/17 17:02
 * @Description View的findViewById的辅助类
 */

public class ViewFinder {

    private Activity mActivity;
    private View mView;

    public ViewFinder(Activity activity) {

        mActivity = activity;
    }

    public ViewFinder(View view) {

        mView = view;
    }

    public View findViewById(int viewId){
        return mActivity != null ? mActivity.findViewById(viewId):mView.findViewById(viewId);
    }

    public void setOnClick(int viewId){

    }
}
