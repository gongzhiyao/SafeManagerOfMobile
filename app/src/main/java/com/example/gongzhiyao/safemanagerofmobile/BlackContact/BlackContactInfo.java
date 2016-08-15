package com.example.gongzhiyao.safemanagerofmobile.BlackContact;

/**
 * Created by 宫智耀 on 2016/7/15.
 */
public class BlackContactInfo {
    public String ContactName, PhoneNumber;
    public int mode;

    public String getModeString(int mode) {
        switch (mode) {
            case 1:
                return "电话拦截";

            case 2:
                return "短信拦截";

            case 3:
                return "电话，短信拦截";

        }
        return "";
    }
}
