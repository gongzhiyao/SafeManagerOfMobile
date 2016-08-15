package com.example.gongzhiyao.safemanagerofmobile.OperatorSet.Service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.TrafficStats;
import android.os.IBinder;

import com.example.gongzhiyao.safemanagerofmobile.OperatorSet.Dao.TrafficDao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TrafficMonitoringService extends Service {
    private long mOldRxBytes;
    private long mOldTxBytes;
    private TrafficDao dao;
    private SharedPreferences mSp;
    private long usedFlow;
    boolean flag = true;


    public TrafficMonitoringService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mOldRxBytes = TrafficStats.getMobileRxBytes();
        mOldTxBytes = TrafficStats.getMobileTxBytes();
        dao = new TrafficDao(this);
        mSp = getSharedPreferences("config", MODE_PRIVATE);
        mThread.start();
    }


    private Thread mThread = new Thread() {
        @Override
        public void run() {
            super.run();
            while (flag) {
                try {
                    sleep(2000 * 60);
                    updateTodayGPRS();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    private void updateTodayGPRS() {
        usedFlow = mSp.getLong("usedflow", 0);
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();//得到日历
        calendar.setTime(date);
        if (calendar.DAY_OF_MONTH == 1 & calendar.HOUR_OF_DAY == 0 & calendar.MINUTE < 1 & calendar.SECOND < 30) {
            usedFlow = 0;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(date);
        long mobileGPRS = dao.getMobileGPRS(dateString);
        long mobileRxBytes = TrafficStats.getMobileRxBytes();
        long mobileTxBytes = TrafficStats.getMobileTxBytes();
        //新产生的流量
        long newGPRS = (mobileRxBytes + mobileTxBytes) - mOldTxBytes - mOldRxBytes;
        mOldTxBytes = mobileTxBytes;
        mOldRxBytes = mobileRxBytes;

        if (newGPRS < 0) {
            /**
             * 网络切换过
             */
            newGPRS = mobileRxBytes + mobileTxBytes;
        }
        if (mobileGPRS == -1) {
            dao.insertTodayGPRS(newGPRS);
        } else {
            if (mobileGPRS < 0) {
                mobileGPRS = 0;
            }
            dao.UpdateTodayGPRS(mobileGPRS + newGPRS);

        }

        usedFlow = usedFlow + newGPRS;
        SharedPreferences.Editor editor = mSp.edit();
        editor.putLong("usedflow", usedFlow);
        editor.commit();
    }


    @Override
    public void onDestroy() {
        if (mThread != null & !mThread.interrupted()) {
            flag = false;
            mThread.interrupt();
            mThread = null;
        }
        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


}
