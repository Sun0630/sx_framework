package com.sx.essayjoke;

import com.orhanobut.logger.Logger;
import com.sx.baselibrary.ExceptionCrashHandler;
import com.sx.framelibrary.BaseSkinActivity;

import java.io.File;
import java.io.FileReader;

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

    }

    @Override
    protected void initData() {
        //下次进入应用获取到这个文件
        File crashFile = ExceptionCrashHandler.getInstance().getCrashFile();
        if (crashFile.exists()){
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
        }

//        int i = 4 / 0;
    }
}
