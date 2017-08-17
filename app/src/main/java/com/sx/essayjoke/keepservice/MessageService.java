package com.sx.essayjoke.keepservice;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.sx.essayjoke.ProcessConnection;

/**
 * @Author sunxin
 * @Date 2017/8/17 11:48
 * @Description 模拟通讯服务
 */

public class MessageService extends Service {

    public static final String TAG = "MessageService";
    private final int MessageId = 1;


    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(200);
                        Log.e(TAG, "等待接收消息");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //连接上
            Toast.makeText(MessageService.this, "MessageService 连接", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //断开连接,重新启动，重新绑定服务
            startService(new Intent(MessageService.this, GuardService.class));
            bindService(new Intent(MessageService.this, GuardService.class),conn,BIND_IMPORTANT);

        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //提高进程优先级为前台进程
        startForeground(MessageId,new Notification());
        //开启守护进程
        bindService(new Intent(this,GuardService.class),conn,BIND_IMPORTANT);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
//        绑定
        return new ProcessConnection.Stub() {
        };
    }



}
