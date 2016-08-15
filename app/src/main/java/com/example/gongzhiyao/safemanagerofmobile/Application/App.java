package com.example.gongzhiyao.safemanagerofmobile.Application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * Created by 宫智耀 on 2016/7/12.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        checkSIM();

    }

    public void checkSIM() {
        SharedPreferences sp=getSharedPreferences("config", MODE_PRIVATE);
        boolean protecting=sp.getBoolean("protecting",true);
        if(protecting){
            String sim=sp.getString("sim","");
            TelephonyManager tm= (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            String realSim=tm.getSimSerialNumber();
            if(sim.equals(realSim)){

            }else {
                String safenum=sp.getString("safephone","");
                if(!TextUtils.isEmpty(safenum)){
                    SmsManager manager=SmsManager.getDefault();
                    manager.sendTextMessage(safenum,null,"您的好友("+sim+")的手机的SIM卡已被更换，请注意！",null,null);
                }
            }
        }
    }




}
