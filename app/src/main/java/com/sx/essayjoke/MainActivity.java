package com.sx.essayjoke;

import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.sx.baselibrary.ExceptionCrashHandler;
import com.sx.framelibrary.BaseSkinActivity;

import java.io.File;
import java.io.IOException;

public class MainActivity extends BaseSkinActivity {

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initView() {
        findViewById(R.id.tv_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, 2 / 1 + "测试", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void initData() {
        //下次进入应用获取到这个文件
        File crashFile = ExceptionCrashHandler.getInstance().getCrashFile();
        /*if (crashFile.exists()){
            //如果文件存在，上传服务器 。。。
            Logger.d("上传到服务器");
            //读取出来
            try {
                FileReader fileReader = new FileReader(crashFile);
                int len = 0;

                char[] buf = new char[1024];
                while ((len = fileReader.read(buf)) != -1){
                    //继续读
                    String crashInfo = new String(buf, 0, len);
                    Logger.d(crashInfo);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/

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
}
