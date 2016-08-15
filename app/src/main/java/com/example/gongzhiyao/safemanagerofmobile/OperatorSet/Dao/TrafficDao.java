package com.example.gongzhiyao.safemanagerofmobile.OperatorSet.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.example.gongzhiyao.safemanagerofmobile.OperatorSet.database.TrafficOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 宫智耀 on 2016/8/12.
 */
public class TrafficDao {
    private TrafficOpenHelper helper;

    public TrafficDao(Context context) {
        helper = new TrafficOpenHelper(context);
    }


    /**
     * 获取一天使用的流量，来自数据库
     *
     * @param dateString
     * @return
     */

    public long getMobileGPRS(String dateString) {
        SQLiteDatabase db = helper.getReadableDatabase();
        long gprs = 0;
        String sq = "select gprs from traffic where date =?";
        Cursor cursor = db.rawQuery(sq, new String[]{"datetime(" + dateString + ")"});
        if (cursor.moveToNext()) {
            String gprsStr = cursor.getString(0);//0就表示着查询参数的第一个
            if (!TextUtils.isEmpty(gprsStr)) {
                gprs = Long.parseLong(gprsStr);
            } else {
                gprs = -1;
            }
        }
        return gprs;
    }


    /**
     * 添加今天的数据
     *
     * @param gprs
     */

    public void insertTodayGPRS(long gprs) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Date dNow = new Date();
        Calendar calendar = Calendar.getInstance();//得到日历
        calendar.setTime(dNow);//把当前日期赋给日历
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(dNow);
        ContentValues cv = new ContentValues();
        cv.put("gprs", String.valueOf(gprs));
        cv.put("date", "datetime(" + dateString + ")");
        db.insert("traffic", null, cv);

    }


    public void UpdateTodayGPRS(long gprs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM-dd");
        String dateString = sdf.format(date);
        ContentValues cv = new ContentValues();
        cv.put("gprs", String.valueOf(gprs));
        cv.put("date", "datetime(" + dateString + ")");
        db.update("traffic", cv, "date=?", new String[]{"datetime(" + dateString + ")"});
    }


}
