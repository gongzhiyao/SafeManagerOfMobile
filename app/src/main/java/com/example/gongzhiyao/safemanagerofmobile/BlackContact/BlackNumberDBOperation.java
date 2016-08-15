package com.example.gongzhiyao.safemanagerofmobile.BlackContact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuffXfermode;
import android.os.SystemClock;

import com.example.gongzhiyao.safemanagerofmobile.DataBase.BlackNumberOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 宫智耀 on 2016/7/15.
 */
public class BlackNumberDBOperation {
    private BlackNumberOpenHelper helper;

    public BlackNumberDBOperation(Context context) {
        super();
        helper = new BlackNumberOpenHelper(context);
    }

    /**
     * 插入数据
     **/
    public boolean add(BlackContactInfo blackContactInfo) {
        SQLiteDatabase dbwrite = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        /**将带有区号的手机号中的区号去掉**/
        if (blackContactInfo.PhoneNumber.startsWith("+86")) {
            blackContactInfo.PhoneNumber = blackContactInfo.PhoneNumber.substring(3);
        }
        cv.put(BlackNumberOpenHelper.NUMBER, blackContactInfo.PhoneNumber);
        cv.put(BlackNumberOpenHelper.NAME, blackContactInfo.ContactName);
        cv.put(BlackNumberOpenHelper.MODE, blackContactInfo.mode);
        long rowId = dbwrite.insert(BlackNumberOpenHelper.TABLENAME, null, cv);
        System.out.println("rowID="+rowId);
        if (rowId != -1) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 删除数据
     **/

    public boolean delete(BlackContactInfo blackContactInfo) {
        SQLiteDatabase dbwrite = helper.getWritableDatabase();
        if (blackContactInfo.PhoneNumber.startsWith("+86")) {
            blackContactInfo.PhoneNumber = blackContactInfo.PhoneNumber.substring(3);
        }
        int rowId = dbwrite.delete(BlackNumberOpenHelper.TABLENAME, "number=?", new String[]{blackContactInfo.PhoneNumber});
        if (rowId == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 分页查询数据库记录
     *
     * @param pagenumber 第几页
     * @param pagesize   每一个页面的大小
     * @return 返回的是 BlackContactInfo的集合
     */

    public List<BlackContactInfo> getPageBlackNumber(int pagenumber, int pagesize) {
        SQLiteDatabase dbread = helper.getReadableDatabase();
        String sq = "select number,mode,name from blacknumber limit ? offset ?";
        /**
         * 这里比较重要，数据库的分页
         * limit 限制每页多少个
         * offset 则表示一共多少个
         */
        Cursor cursor = dbread.rawQuery(sq, new String[]{String.valueOf(pagesize), String.valueOf(pagesize * pagenumber)});
        List<BlackContactInfo> blackContactInfoList = new ArrayList<BlackContactInfo>();
        while (cursor.moveToNext()) {
            BlackContactInfo info = new BlackContactInfo();
            info.PhoneNumber = cursor.getString(cursor.getColumnIndex(BlackNumberOpenHelper.NUMBER));
            info.ContactName = cursor.getString(cursor.getColumnIndex(BlackNumberOpenHelper.NAME));
            info.mode = cursor.getInt(cursor.getColumnIndex(BlackNumberOpenHelper.MODE));
            blackContactInfoList.add(info);
        }
        cursor.close();
        dbread.close();
        SystemClock.sleep(30);
        return blackContactInfoList;
    }

    /**
     * 查询号码是否存在黑名单中
     *
     * @param number 电话号码
     * @return
     */
    public boolean isNumberExist(String number) {
        SQLiteDatabase dbread = helper.getReadableDatabase();
        String sq = "select * from " + BlackNumberOpenHelper.TABLENAME + " where number =?";
        Cursor cursor = dbread.rawQuery(sq, new String[]{number});
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            dbread.close();
            return true;
        }
        cursor.close();
        dbread.close();
        return false;
    }

    /**
     * 通过手机号查询拦截方式mode
     *
     * @param number
     * @return
     */
    public int getBlackContactMode(String number) {
        int mode = 0;
        SQLiteDatabase dbread = helper.getReadableDatabase();
        String sq = "select " + BlackNumberOpenHelper.MODE + " from " + BlackNumberOpenHelper.TABLENAME + " where " + BlackNumberOpenHelper.NUMBER + "=?";
        Cursor cursor = dbread.rawQuery(sq, new String[]{number});
        while (cursor.moveToNext()) {
            mode = cursor.getInt(cursor.getColumnIndex(BlackNumberOpenHelper.MODE));
        }
        cursor.close();
        dbread.close();
        return mode;
    }


    /**
     * 获得总的数据库的记录数
     *
     * @return
     */
    public int getTotalNum() {
        SQLiteDatabase dbread = helper.getReadableDatabase();
        String sq = "select count(*) from " + BlackNumberOpenHelper.TABLENAME;
        Cursor cursor = dbread.rawQuery(sq, null);
        cursor.moveToNext();
        int count = cursor.getInt(0);//获得count的值(第0列)
        cursor.close();
        dbread.close();
        return count;

    }


}
