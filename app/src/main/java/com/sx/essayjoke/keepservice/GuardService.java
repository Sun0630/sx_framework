package com.sx.essayjoke.keepservice;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.Toast;

import com.sx.essayjoke.ProcessConnection;

/**
 * @Author sunxin
 * @Date 2017/8/17 12:28
 * @Description 守护进程  双进程间通信
 */
public class GuardService extends Service {

    private int GuardId = 1;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //提高进程优先级为前台进程
        startForeground(GuardId,new Notification());
        bindService(new Intent(this,MessageService.class),conn,BIND_IMPORTANT);
        return START_STICKY;
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //连接上
            Toast.makeText(GuardService.this, "GuardService 连接", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //断开连接
            //断开连接,重新启动，重新绑定服务
            startService(new Intent(GuardService.this, MessageService.class));
            bindService(new Intent(GuardService.this, MessageService.class),conn,BIND_IMPORTANT);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {

        return new ProcessConnection.Stub() {
        };
    }
}
