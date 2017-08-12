package com.sx.framelibrary.skin;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.sx.framelibrary.skin.attr.SkinView;
import com.sx.framelibrary.skin.callback.ISkinChangeListener;
import com.sx.framelibrary.skin.config.SkinConfig;
import com.sx.framelibrary.skin.config.SkinPreUtils;

import java.io.File;
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
    private Map<ISkinChangeListener, List<SkinView>> mSkinViews = new HashMap<>();
    private SkinResource mSkinResource;

    static {
        mInstance = new SkinManager();
    }

    public static SkinManager getInstance() {
        return mInstance;
    }

    public void init(Context context) {

        //内存泄漏问题
        this.mContext = context.getApplicationContext();
        //每次打开应用都会走该方法，需要做一些措施防止皮肤被删除
        String currentSkinPath = SkinPreUtils.getInstance(context).getSkinPath();
        File file = new File(currentSkinPath);
        if (!file.exists()) {
            //如果文件不存在，就清空皮肤信息
            SkinPreUtils.getInstance(context).clearSkinInfo();
            return;
        }

        //判断一下是否能拿到包名，如果拿不到，说明皮肤文件不是一个apk
        String packageName = context.getPackageManager()
                .getPackageArchiveInfo(currentSkinPath, PackageManager.GET_ACTIVITIES)
                .packageName;

        if (TextUtils.isEmpty(packageName)) {
            SkinPreUtils.getInstance(context).clearSkinInfo();
            return;
        }

        //最好检验签名，增量更新


        //做一些初始化
        mSkinResource = new SkinResource(context, currentSkinPath);

    }

    /**
     * 根据路径加载皮肤
     *
     * @param path
     * @return
     */
    public int loadSkin(String path) {
        //判断文件是否存在
        File file = new File(path);
        if (!file.exists()) {
            return SkinConfig.SKIN_FILE_NOT_EXISTS;
        }

        //判断包名是否有错误
        String packageName = mContext.getPackageManager()
                .getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES)
                .packageName;

        if (TextUtils.isEmpty(packageName)) {

            return SkinConfig.SKIN_FILE_ERROR;
        }

        //1,判断如果当前皮肤一样，就不用换肤
        String currentSkinPath = SkinPreUtils.getInstance(mContext).getSkinPath();
        if (path.equals(currentSkinPath)) {
            return SkinConfig.SKIN_CHANGE_NOTHING;
        }
        //校验签名

        //初始化资源管理
        mSkinResource = new SkinResource(mContext, path);
        //改变皮肤,遍历Map集合
        changeSkin();

        //保存皮肤状态
        saveSkinSatus(path);

        return SkinConfig.SKIN_CHANGE_DEFAULT;
    }

    private void changeSkin() {
        Set<ISkinChangeListener> skinChangeListeners = mSkinViews.keySet();
        for (ISkinChangeListener key : skinChangeListeners) {
            List<SkinView> skinViews = mSkinViews.get(key);
            for (SkinView view : skinViews) {
                view.skin();
            }
            //通知换肤
            key.changeSkin(mSkinResource);
        }
    }

    /**
     * 保存皮肤的路径
     *
     * @param path
     */
    private void saveSkinSatus(String path) {
        SkinPreUtils.getInstance(mContext).saveSkinPath(path);
    }

    /**
     * 加载默认皮肤
     *
     * @return
     */
    public int loadDefault() {
        //判断当前有没有皮肤，没有皮肤就不执行任何方法
        String currentSkinPath = SkinPreUtils.getInstance(mContext).getSkinPath();
        if (TextUtils.isEmpty(currentSkinPath)) {
            return SkinConfig.SKIN_CHANGE_NOTHING;
        }

        //获取当前运行app的路径  /data/data/...
        String skinPath = mContext.getPackageResourcePath();
        mSkinResource = new SkinResource(mContext, skinPath);

        //改变皮肤
        changeSkin();
        //清空皮肤信息
        SkinPreUtils.getInstance(mContext).clearSkinInfo();


        return SkinConfig.SKIN_CHANGE_DEFAULT;
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
     * @param changeListener
     * @param views
     */
    public void regisetr(ISkinChangeListener changeListener, List<SkinView> views) {
        mSkinViews.put(changeListener, views);
    }


    /**
     * 获取当前皮肤资源
     *
     * @return
     */
    public SkinResource getSkinResource() {
        return mSkinResource;
    }

    /**
     * 检测需不需要换肤
     *
     * @param skinView
     */
    public void checkChangeSkin(SkinView skinView) {
        //如果当前有皮肤，就换
        String currentSkinPath = SkinPreUtils.getInstance(mContext).getSkinPath();
        if (!TextUtils.isEmpty(currentSkinPath)) {
            //切换皮肤
            skinView.skin();
        }
    }

    /**
     * 解除注册,防止内存泄漏
     *
     * @param listener
     */
    public void unregister(ISkinChangeListener listener) {
        mSkinViews.remove(listener);
    }
}
