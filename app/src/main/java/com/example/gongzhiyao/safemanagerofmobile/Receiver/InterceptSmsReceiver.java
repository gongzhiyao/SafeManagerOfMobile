package com.example.gongzhiyao.safemanagerofmobile.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.telephony.SmsMessage;

import com.example.gongzhiyao.safemanagerofmobile.BlackContact.BlackNumberDBOperation;

public class InterceptSmsReceiver extends BroadcastReceiver {

    public InterceptSmsReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        SharedPreferences mSp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean BlackNumStatus = mSp.getBoolean("BlackNumStatus", true);
        if (!BlackNumStatus) {

            return;
        }


        BlackNumberDBOperation operation = new BlackNumberDBOperation(context);
        Object[] objs = (Object[]) intent.getExtras().get("pdus");
        for (Object obj : objs) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
            String sender = smsMessage.getOriginatingAddress();
            String body = smsMessage.getMessageBody();
            if (sender.startsWith("+86")) {
                sender = sender.substring(3, sender.length()).trim();
            }

            if(operation.isNumberExist(sender)){
                System.out.println("号码存在于黑名单中");
                int mode = operation.getBlackContactMode(sender);
                System.out.println("mode="+mode);
                if (mode == 2 || mode == 3) {
                    /**同时都需要拦截短信，这时要截断广播**/

                    /***
                     * android 4.4以上拦截不住
                     */
                    abortBroadcast();
                }
            }

        }
    }
}
