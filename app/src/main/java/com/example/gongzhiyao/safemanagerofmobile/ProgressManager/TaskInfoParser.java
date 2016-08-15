package com.example.gongzhiyao.safemanagerofmobile.ProgressManager;

/**
 * Created by 宫智耀 on 2016/8/12.
 */

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug;

import com.example.gongzhiyao.safemanagerofmobile.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务信息& 进程信息的解析器
 */


public class TaskInfoParser {
    public static List<TaskInfo> getRunningTaskInfo(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        PackageManager pm = context.getPackageManager();
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
        for (ActivityManager.RunningAppProcessInfo processInfo : processInfos) {
            String packname = processInfo.processName;
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.packageName = packname;//进程名称
            Debug.MemoryInfo[] memoryinfos = am.getProcessMemoryInfo(new int[]{processInfo.pid});
            long memsize = memoryinfos[0].getTotalPrivateDirty() * 1024;
            taskInfo.appMemory = memsize;//程序占用的内存空间

            try {
                PackageInfo packageInfo = pm.getPackageInfo(packname, 0);
                Drawable icon = packageInfo.applicationInfo.loadIcon(pm);
                taskInfo.appIcon = icon;
                String appname = packageInfo.applicationInfo.loadLabel(pm).toString();
                taskInfo.appName = appname;
                if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0) {
                    //系统进程
                    taskInfo.isUserApp = false;
                } else {
                    taskInfo.isUserApp = true;
                }


            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                taskInfo.appName = packname;
                taskInfo.appIcon = context.getResources().getDrawable(R.drawable.ic_default);
            }
            taskInfos.add(taskInfo);


        }
        return taskInfos;

    }
}
