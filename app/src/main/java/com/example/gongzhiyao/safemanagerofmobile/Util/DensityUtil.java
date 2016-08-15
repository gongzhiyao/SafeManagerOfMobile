package com.example.gongzhiyao.safemanagerofmobile.Util;

import android.content.Context;

/**
 * Created by 宫智耀 on 2016/7/19.
 */
public class DensityUtil {
    /**
     * dip转换像素px
     */
    public static int dip2px(Context context, float dpValue) {
        try {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (int) dpValue;
    }


    /**
     * 像素px转dip
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        try {
            /**获得屏幕分辨率**/
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (pxValue / scale + 0.5f);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (int) pxValue;
    }

}
