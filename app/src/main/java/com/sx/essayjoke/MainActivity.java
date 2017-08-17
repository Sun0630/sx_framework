package com.sx.essayjoke;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Toast;

import com.sx.baselibrary.ExceptionCrashHandler;
import com.sx.baselibrary.fix.FixDexManager;
import com.sx.essayjoke.keepservice.GuardService;
import com.sx.essayjoke.keepservice.JobWakeUpService;
import com.sx.essayjoke.keepservice.MessageService;
import com.sx.framelibrary.BaseSkinActivity;
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
        //启动服务，这是服务端
        startService(new Intent(this,MessageService.class));
        startService(new Intent(this,GuardService.class));

        //必须5.0以上才能运行
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            startService(new Intent(this,JobWakeUpService.class));
        }

        //写在一起了啊，这是客户端要去连接服务端
//        Intent service = new Intent(this,MessageService.class);
//        bindService(service,conn, Context.BIND_AUTO_CREATE);
    }

    private IUser mUser;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //连接成功
            mUser = IUser.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //断开连接
        }
    };

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


    @Override
    public void changeSkin(SkinResource skinResource) {
        super.changeSkin(skinResource);
        Toast.makeText(this, "换肤了~", Toast.LENGTH_SHORT).show();
    }

    public void getUserame(View view) {
        try {
            Toast.makeText(this, mUser.getUsername(), Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void getPassword(View view) {
        try {
            Toast.makeText(this, mUser.getPassword(), Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
