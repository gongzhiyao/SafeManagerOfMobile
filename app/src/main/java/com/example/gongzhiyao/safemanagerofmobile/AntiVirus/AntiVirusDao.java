package com.example.gongzhiyao.safemanagerofmobile.AntiVirus;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;

/**
 * Created by 宫智耀 on 2016/7/20.
 */

/***
 * 查询是否有病毒符合病毒数据库
 */
public class AntiVirusDao {

    public static String checkVirus(String md5) {
        String desc = null;
        //打开病毒库
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "SMOM" + File.separator + "db" + "/antivirus.db");
        if (file.exists()) {
            System.out.println("数据库存在");
            SQLiteDatabase db = SQLiteDatabase
                    .openDatabase(
                            Environment.getExternalStorageDirectory() + File.separator + "SMOM" + File.separator + "db" + "/antivirus.db",
                            null, SQLiteDatabase.OPEN_READONLY);

            String sq = "select  desc from datable where md5=?";
            Cursor cursor = db.rawQuery(sq, new String[]{md5});
            while (cursor.moveToNext()) {
                desc = cursor.getString(0);
            }
            cursor.close();
            db.close();
            return desc;
        } else {
            System.out.println("数据库不存在");
            return null;
        }
//        SQLiteDatabase db = SQLiteDatabase
//                .openDatabase(
//                        "/data/data/com.example.gongzhiyao.safemanagerofmobile/databases/antivirus.db",
//                        null, SQLiteDatabase.OPEN_READONLY);

    }
}
