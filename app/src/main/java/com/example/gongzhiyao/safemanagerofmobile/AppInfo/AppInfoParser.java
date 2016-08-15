package com.example.gongzhiyao.safemanagerofmobile.AppInfo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.example.gongzhiyao.safemanagerofmobile.Application.App;
import com.example.gongzhiyao.safemanagerofmobile.Log.L;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 宫智耀 on 2016/7/19.
 */
public class AppInfoParser {

    public static List<AppInfo> getAppInfo(Context context) {
        L log = new L();
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
        List<AppInfo> appInfos = new ArrayList<AppInfo>();
        for (PackageInfo packageInfo : packageInfos) {
            log.d("正在搜索程序");
            AppInfo appInfo = new AppInfo();
            String packageName = packageInfo.packageName;
            appInfo.packageName = packageName;
            Drawable icon = packageInfo.applicationInfo.loadIcon(pm);
            appInfo.icon = icon;
            String appname = packageInfo.applicationInfo.loadLabel(pm).toString();
            appInfo.appName = appname;
            String apkpath = packageInfo.applicationInfo.sourceDir;
            appInfo.apkPath = apkpath;
            File file = new File(apkpath);
            long appsize = file.length();
            appInfo.appSize = appsize;
            int flags = packageInfo.applicationInfo.flags;
            /**如果相等，外置存储**/
            if ((ApplicationInfo.FLAG_EXTERNAL_STORAGE & flags) != 0) {
                appInfo.isInRoom = false;
            } else {
                appInfo.isInRoom = true;
            }
            /**判断是否为系统应用***/
            if ((ApplicationInfo.FLAG_SYSTEM & flags) != 0) {
                //系统应用
                appInfo.isUserApp = false;
            } else {
                appInfo.isUserApp = true;
            }
            appInfos.add(appInfo);
        }

        return appInfos;
    }


}
