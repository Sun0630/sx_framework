package com.sx.essayjoke;

import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sx.baselibrary.ExceptionCrashHandler;
import com.sx.baselibrary.dialog.AlertDialog;
import com.sx.baselibrary.fix.FixDexManager;
import com.sx.framelibrary.BaseSkinActivity;
import com.sx.framelibrary.DefaultNavigationBar;

import java.io.File;
import java.io.IOException;

public class MainActivity extends BaseSkinActivity {

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initTitle() {
        DefaultNavigationBar defaultNavigationBar = new DefaultNavigationBar.Builder(this)
                .setTitle("投稿")
                .setRightText("发布")
                .setOnRightClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "已经发布", Toast.LENGTH_SHORT).show();
                    }
                })
//                .setRightIcon(R.mipmap.account_icon_weibo)
                .build();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

        //自定义热修复
        fixDexBug();

        //阿里热修复
//        andFix();
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

    public void click(View view) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setContentView(R.layout.detail_common_dialog)
                .fromBottom(true)
                .fullWidth()
                .show();

        final EditText et_common = dialog.getView(R.id.comment_editor);
        dialog.setText(R.id.submit_btn, "发送");
        dialog.setOnClickListener(R.id.submit_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, et_common.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }

}
