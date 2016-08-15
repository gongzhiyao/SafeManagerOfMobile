package com.example.gongzhiyao.safemanagerofmobile.Service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;

import com.example.gongzhiyao.safemanagerofmobile.Log.L;

public class GPSLocationService extends Service {
    private LocationManager lm;
    private MyListener listener;
    private L log;

    public GPSLocationService() {

    }


    @Override
    public void onCreate() {
        log = new L();
        super.onCreate();
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = new MyListener();
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);//获取准确位置
        criteria.setCostAllowed(true);//允许产生开销
        String name = lm.getBestProvider(criteria, true);
        log.d("最好的位置提供者" + name);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            log.d("缺少权限，权限检查未通过");
            return;
        }
        lm.requestLocationUpdates(name, 0, 0, listener);


    }


    private class MyListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            StringBuilder sb = new StringBuilder();
            sb.append("accuracy:" + location.getAccuracy() + "\n");
            sb.append("speed:" + location.getSpeed() + "\n");
            sb.append("jingdu:" + location.getLongitude() + "\n");
            sb.append("weidu:" + location.getLatitude() + "\n");
            String result = sb.toString();
            log.d("发送短信" + result);
            SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
            String safenum = sp.getString("safephone", "");
            SmsManager.getDefault().sendTextMessage(safenum, null, result, null, null);
            stopSelf();
        }


        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            log.d("权限检查未通过");
            return;
        }
        /**
         * 在destroy中要注销位置监听器
         */
        lm.removeUpdates(listener);
        listener=null;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
