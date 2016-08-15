package com.example.gongzhiyao.safemanagerofmobile.Receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import com.example.gongzhiyao.safemanagerofmobile.Log.L;
import com.example.gongzhiyao.safemanagerofmobile.R;
import com.example.gongzhiyao.safemanagerofmobile.Service.GPSLocationService;

public class SmsLostFindReceiver extends BroadcastReceiver {
    private L log;
    private SharedPreferences sharedPreferences;

    public SmsLostFindReceiver() {
        log = new L();

    }

    String realSender;

    @Override
    public void onReceive(Context context, Intent intent) {
        sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean isProtecting = sharedPreferences.getBoolean("protecting", true);
        if (isProtecting) {
            /**
             * 获取超级管理员
             */

            log.d("进入了系统保护！！！！！！！！！！！！！！！！！！！！");
            DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
            Object[] objs = (Object[]) intent.getExtras().get("pdus");
            for (Object obj : objs) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
                String sender = smsMessage.getOriginatingAddress();
                if (sender.startsWith("+86")) {
                    realSender = sender.substring(3);
                } else {
                    realSender = sender;
                }

                log.d("sender:" + sender);
                log.d("realSender:" + realSender);
                String body = smsMessage.getMessageBody();
                log.d("body:" + body);
                String safephone = sharedPreferences.getString("safephone", null);
                log.d("safePhone:" + safephone);
                if (!TextUtils.isEmpty(safephone) && safephone.equals(realSender)) {
                    if ("#*location*#".equals(body)) {
                        log.d("返回位置信息");
                        Intent service = new Intent(context, GPSLocationService.class);
                        context.startService(service);

                        abortBroadcast();
                    } else if ("#*alarm*#".equals(body)) {
                        log.d("播放音乐，报警");
                        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ylzs);
                        mediaPlayer.setVolume(1.0f, 1.0f);
                        sharedPreferences.edit().putBoolean("alarm", true).commit();
                        mediaPlayer.setLooping(true);
                        mediaPlayer.start();
                        abortBroadcast();
                    } else if ("#*wipedata*#".equals(body)) {
                        log.d("远程擦除数据");
                        dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
                        abortBroadcast();
                    } else if ("#*lockScreen*#".equals(body)) {
                        log.d("远程锁屏");
                        dpm.resetPassword("i3739124", 0);
                        dpm.lockNow();
                        abortBroadcast();
                    }
                }
            }

        }
    }
}
