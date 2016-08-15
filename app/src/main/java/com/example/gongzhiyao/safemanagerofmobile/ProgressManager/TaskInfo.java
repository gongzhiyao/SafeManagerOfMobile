package com.example.gongzhiyao.safemanagerofmobile.ProgressManager;

import android.graphics.drawable.Drawable;

/**
 * Created by 宫智耀 on 2016/8/11.
 */
public class TaskInfo {
    public String appName;
    public Drawable appIcon;
    public String packageName;
    public long appMemory;
    /**
     * 用来标记app是否被选中
     **/
    public boolean isChecked;
    public boolean isUserApp;
}
