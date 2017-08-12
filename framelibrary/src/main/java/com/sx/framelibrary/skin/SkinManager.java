package com.sx.framelibrary.skin;

import android.app.Activity;
import android.content.Context;

import com.sx.framelibrary.skin.attr.SkinView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author sunxin
 * @Date 2017/8/9 14:08
 * @Description 皮肤管理类
 */

public class SkinManager {

    private static SkinManager mInstance;
    private Context mContext;
    private Map<Activity, List<SkinView>> mSkinViews = new HashMap<>();
    private SkinResource mSkinResource;

    static {
        mInstance = new SkinManager();
    }

    public static SkinManager getInstance() {
        return mInstance;
    }

    public void init(Context context) {
        this.mContext = context;
    }

    /**
     * 根据路径加载皮肤
     *
     * @param path
     * @return
     */
    public int loadSkin(String path) {
        //校验签名

        //初始化资源管理
        mSkinResource = new SkinResource(mContext, path);
        //改变皮肤,遍历Map集合
        Set<Activity> activities = mSkinViews.keySet();
        for (Activity activity : activities) {
            List<SkinView> skinViews = mSkinViews.get(activity);
            for (SkinView view : skinViews) {
                view.skin();
            }
        }

        return 0;
    }

    /**
     * 加载默认皮肤
     *
     * @return
     */
    public int loadDefault() {
        return 0;
    }

    /**
     * 通过Activity获取SkinView
     *
     * @param activity
     * @return
     */
    public List<SkinView> getSkinViews(Activity activity) {
        return mSkinViews.get(activity);
    }

    /**
     * 注册
     *
     * @param activity
     * @param views
     */
    public void regisetr(Activity activity, List<SkinView> views) {
        mSkinViews.put(activity, views);
    }


    /**
     * 获取当前皮肤资源
     *
     * @return
     */
    public SkinResource getSkinResource() {
        return mSkinResource;
    }
}
