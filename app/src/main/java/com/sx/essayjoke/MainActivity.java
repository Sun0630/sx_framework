package com.sx.essayjoke;

import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.sx.baselibrary.ExceptionCrashHandler;
import com.sx.baselibrary.fix.FixDexManager;
import com.sx.framelibrary.BaseSkinActivity;
import com.sx.framelibrary.skin.SkinManager;
import com.sx.framelibrary.skin.SkinResource;

import java.io.File;
import java.io.IOException;

public class MainActivity extends BaseSkinActivity {

    private static final String TAG = "Main";

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {

    }

    /**
     * 自定义热修复
     */
    private void fixDexBug() {
        File fixFile = new File(Environment.getExternalStorageDirectory(), "fix.dex");
        if (fixFile.exists()) {
            FixDexManager fixDexManager = new FixDexManager(this);
            try {
                fixDexManager.fixDexBug(fixFile.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void andFix() {
        //下次进入应用获取到这个文件
        File crashFile = ExceptionCrashHandler.getInstance().getCrashFile();

        //每次启动的时候，就要去后台获取差分包 fix.patch ，然后修复bug

        //差分包需要请求接口拿到一个文件，这里只做测试，保存到本地一个fix.patch
        File fixFile = new File(Environment.getExternalStorageDirectory(), "fix.apatch");

        if (fixFile.exists()) {
            //如果文件存在，就去修复bug。
            try {
                MyApplication.mPatchManager.addPatch(fixFile.getAbsolutePath());
                Toast.makeText(this, "修复成功", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "修复失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 换肤
     *
     * @param view
     */
    public void click(View view) {
        Toast.makeText(this, "点击了", Toast.LENGTH_SHORT).show();
        String skinPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "red.skin";
        int result = SkinManager.getInstance().loadSkin(skinPath);
    }

    /**
     * 默认
     *
     * @param view
     */
    public void clickDefault(View view) {
        //加载默认皮肤
        int result = SkinManager.getInstance().loadDefault();
    }

    /**
     * 跳转
     *
     * @param view
     */
    public void clickJump(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }


    @Override
    public void changeSkin(SkinResource skinResource) {
        super.changeSkin(skinResource);
        Toast.makeText(this, "换肤了~", Toast.LENGTH_SHORT).show();
    }
}
