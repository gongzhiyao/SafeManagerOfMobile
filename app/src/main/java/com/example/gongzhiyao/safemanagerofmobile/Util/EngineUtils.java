package com.example.gongzhiyao.safemanagerofmobile.Util;

import com.stericson.RootTools.RootTools;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import com.example.gongzhiyao.safemanagerofmobile.AppInfo.AppInfo;
import com.stericson.RootTools.RootToolsException;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * Created by 宫智耀 on 2016/7/19.
 */
public class EngineUtils {
    /**
     * 分享
     **/
    public static void shareApp(Context context, AppInfo appInfo) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "推荐您使用一款软件，名称叫"
                + appInfo.appName +
                "下载路径：http://play.google.com/store/apps/details?id="
                + appInfo.packageName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 开启应用
     **/
    public static void startApp(Context context, AppInfo appInfo) {
        //打开这个应用程序的入口activity
        PackageManager pm = context.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(appInfo.packageName);
        if (intent != null) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "启动错误，无法启动！", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 开启设置界面
     */

    public static void settingAppDetail(Context context, AppInfo appInfo) {
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:" + appInfo.packageName));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 卸载应用
     */
    public static void uninstallApp(Context context, AppInfo appInfo) {
        if (appInfo.isUserApp) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("package:" + appInfo.packageName));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            /**系统应用，需要使用root权限，使用linux命令删除**/
            if (!RootTools.isRootAvailable()) {
                Toast.makeText(context, "当前设备没有root权限", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                if (!RootTools.isAccessGiven()) {
                    Toast.makeText(context, "请授与本程序超级ROOT权限", Toast.LENGTH_SHORT).show();
                    return;
                }

                /**通过linux语句卸载**/
                RootTools.sendShell("mount -o remount ,rw /system", 3000);
                RootTools.sendShell("rm -r " + appInfo.apkPath, 30000);

            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (RootToolsException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
