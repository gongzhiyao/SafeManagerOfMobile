package com.example.gongzhiyao.safemanagerofmobile.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;

import com.example.gongzhiyao.safemanagerofmobile.Application.App;
import com.example.gongzhiyao.safemanagerofmobile.OperatorSet.Service.TrafficMonitoringService;
import com.example.gongzhiyao.safemanagerofmobile.ProgressManager.SystemInfoUtils;
import com.example.gongzhiyao.safemanagerofmobile.R;

public class BootCompleteReceiver extends BroadcastReceiver {
    SharedPreferences sp;

    public BootCompleteReceiver() {

    }


    @Override
    public void onReceive(Context context, Intent intent) {
        /**
         * 监听开机广播，一旦开机就检测SIM卡是否更换
         */
        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        ((App) context.getApplicationContext()).checkSIM();
        boolean isAlarm = sp.getBoolean("alarm", false);

        if (isAlarm) {
            MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ylzs);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }


        if (!SystemInfoUtils.isServiceRunning(context, "com.example.gongzhiyao.safemanagerofmobile.OperatorSet.Service.TrafficMonitoringService")) {
            context.startService(new Intent(context, TrafficMonitoringService.class));
        }


    }
}
