package com.example.gongzhiyao.safemanagerofmobile.Contact;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 宫智耀 on 2016/7/13.
 */
public class ContactInfoParser {

    private static String data1;
    private static String mimetype;

    public static List<ContactInfo> getSystemContact(Context context) {
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri datauri = Uri.parse("content://com.android.contacts/data");
        List<ContactInfo> infos = new ArrayList<ContactInfo>();
        /**
         * 查询raw_contacts表获得联系人id
         */
        Cursor cursor = resolver.query(uri, new String[]{"contact_id"}, null, null, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            if (id != null) {
                System.out.println("联系人id=" + id);
                ContactInfo info = new ContactInfo();
                info.id = id;


                /**
                 * 通过id查询data表得到号码等信息
                 */

                Cursor dataCursor = resolver.query(datauri, new String[]{"data1", "mimetype"}, "raw_contact_id=?", new String[]{id}, null);


                while (dataCursor.moveToNext()) {
                    data1 = dataCursor.getString(0);
                     mimetype = dataCursor.getString(1);
                    if ("vnd.android.cursor.item/name".equals(mimetype)) {
                        info.name = data1;
                    } else if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
                        info.phone = data1;
                    }
                }
                if (info.name!=null && info.phone!=null) {

                    System.out.println(info.name+"                  "+info.phone);
                    infos.add(info);
                }

                dataCursor.close();

            }
        }
        /**
         * 防止内存泄漏
         */
        cursor.close();
        return infos;
    }
}
