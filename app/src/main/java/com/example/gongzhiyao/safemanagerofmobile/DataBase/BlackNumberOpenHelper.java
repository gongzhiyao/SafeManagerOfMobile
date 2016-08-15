package com.example.gongzhiyao.safemanagerofmobile.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 宫智耀 on 2016/7/15.
 */
public class BlackNumberOpenHelper extends SQLiteOpenHelper {
    public static final String TABLENAME="blacknumber";
    public static final String NUMBER="number";
    public static final String NAME="name";
    public static final String MODE="mode";



    private static final String sq1="create table blacknumber("
            +"_id Integer Primary key autoincrement,"
            +"number TEXT,"
            +"name TEXT,"
            +"mode integer"
            +")";


    public BlackNumberOpenHelper(Context context) {
        super(context, "blackNumber.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sq1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
