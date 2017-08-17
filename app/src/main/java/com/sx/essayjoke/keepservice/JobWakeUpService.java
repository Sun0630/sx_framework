package com.sx.essayjoke.keepservice;

import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.List;

/**
 * @Author sunxin
 * @Date 2017/8/17 13:40
 * @Description
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobWakeUpService extends JobService {

    private int jobId = 1;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //开启一个轮询
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo.Builder jobInfoBuilder = new JobInfo.Builder(jobId, new ComponentName(this, JobWakeUpService.class));
        jobScheduler.schedule(jobInfoBuilder.build());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        //开启定时任务，定时轮询，查看MessageService有没有被杀死，如果杀死了轮询JobWakeUpService
        //先判断有没有服务在运行
        boolean isMessageServiceRunning = isServiceAlive(MessageService.class.getName());
        if (!isMessageServiceRunning){
            startService(new Intent(this,MessageService.class));
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    /**
     * 判断此Service是否在运行
     *
     * @param serviceName
     * @return
     */
    private boolean isServiceAlive(String serviceName) {
        boolean isAlive = false;
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        //获取正在运行的所有服务
        List<ActivityManager.RunningServiceInfo> services = am.getRunningServices(100);
        if (services.size() <= 0) {
            return false;
        }
        for (int i = 0; i < services.size(); i++) {
            String name = services.get(i).service.getClassName().toString();
            if (serviceName.equals(name)) {
                isAlive = true;
                break;
            }
        }

        return isAlive;
    }

}
