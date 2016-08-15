package com.example.gongzhiyao.safemanagerofmobile.OperatorSet.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 宫智耀 on 2016/8/12.
 */
public class TrafficOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "traffic.db";
    private static final String TABLE_NAME = "traffic";

    private final static String GPRS = "gprs";
    private final static String TIME = "date";

    public TrafficOpenHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    final String sq = "create table " + TABLE_NAME + " (" +
            "_id INTEGER primary key autoincrement,"
            + GPRS + " varchar(255),"
            + TIME + " datetime)";

    ;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sq);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
