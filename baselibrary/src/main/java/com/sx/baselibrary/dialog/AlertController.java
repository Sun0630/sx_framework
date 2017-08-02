package com.sx.baselibrary.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * @Author sunxin
 * @Date 2017/5/18 19:12
 * @Description
 */

class AlertController {


    private AlertDialog mDialog;
    private Window mWindow;
    private DialogViewHelper mDialogViewHelper;

    public AlertController(AlertDialog dialog, Window window) {
        mDialog = dialog;
        mWindow = window;
    }

    public void setDialogViewHelper(DialogViewHelper dialogViewHelper) {
        mDialogViewHelper = dialogViewHelper;
    }

    /**
     * 获取Dialog
     *
     * @return
     */
    public AlertDialog getDialog() {
        return mDialog;
    }

    /**
     * 获取Dialog的window
     *
     * @return
     */
    public Window getWindow() {
        return mWindow;
    }

    public void setText(int viewId, CharSequence text) {
        mDialogViewHelper.setText(viewId, text);
    }

    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        mDialogViewHelper.setOnClickListener(viewId, listener);
    }

    public <T extends View> T getView(int viewId) {
        return mDialogViewHelper.getView(viewId);
    }


    public static class AlertParams {
        public Context mContext;
        public int mThemeResId;
        //点击空白是否能够取消
        public boolean mCancelable = true;
        //取消监听
        public DialogInterface.OnCancelListener mOnCancelListener;
        //消失监听
        public DialogInterface.OnDismissListener mOnDismissListener;

        public DialogInterface.OnKeyListener mOnKeyListener;
        //View对象
        public View mView;
        //自定义的布局ID
        public int mViewLayoutResId;
        //map存放字体的修改,SparseArray比Integer更高效
        public SparseArray<CharSequence> textArray = new SparseArray<>();
        //存放点击事件的修改
        public SparseArray<View.OnClickListener> clickArray = new SparseArray<>();
        //宽度
        public int mWidth = FrameLayout.LayoutParams.WRAP_CONTENT;
        //位置
        public int mGravity = Gravity.CENTER;
        public int mAnimation = 0;
        //设置高
        public int mHeight = FrameLayout.LayoutParams.WRAP_CONTENT;


        public AlertParams(Context context, int themeResId) {
            this.mContext = context;
            this.mThemeResId = themeResId;
        }

        public void apply(AlertController alert) {
            //设置参数
            //设置布局 DialogViewHelper

            DialogViewHelper viewHelper = null;

            if (mViewLayoutResId != 0) {
                viewHelper = new DialogViewHelper(mContext, mViewLayoutResId);
            }

            if (mView != null) {
                viewHelper = new DialogViewHelper();
                viewHelper.setContentView(mView);
            }

            if (viewHelper == null) {
                throw new IllegalArgumentException("请设置布局setContentView()");
            }

            //为Dialog设置布局
//            alert.getWindow().setContentView(viewHelper.getContentView());

            alert.getDialog().setContentView(viewHelper.getContentView());

            alert.setDialogViewHelper(viewHelper);

            //设置文本
            int textArraySize = textArray.size();
            for (int i = 0; i < textArraySize; i++) {
                viewHelper.setText(textArray.keyAt(i), textArray.valueAt(i));
            }

            // 设置点击
            int clickArraySize = clickArray.size();
            for (int i = 0; i < clickArraySize; i++) {
                viewHelper.setOnClickListener(clickArray.keyAt(i), clickArray.valueAt(i));
            }

            //自定义 全屏，从底部弹出，默认动画
            //设置动画
            Window window = alert.getWindow();
            if (mAnimation != 0) {
                window.setWindowAnimations(mAnimation);
            }

            //底部
            window.setGravity(mGravity);

            //设置宽高
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = mWidth;
            params.height = mHeight;
            window.setAttributes(params);
        }
    }
}
