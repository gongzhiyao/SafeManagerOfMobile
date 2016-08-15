package com.example.gongzhiyao.safemanagerofmobile.OperatorSet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gongzhiyao.safemanagerofmobile.OperatorSet.Dao.TrafficDao;
import com.example.gongzhiyao.safemanagerofmobile.OperatorSet.Service.TrafficMonitoringService;
import com.example.gongzhiyao.safemanagerofmobile.ProgressManager.SystemInfoUtils;
import com.example.gongzhiyao.safemanagerofmobile.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TrafficMonitoring extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences mSp;
    private Button mCorrectFlowBtn;
    private TextView mTotalTV;
    private TextView mUsedTV;
    private TextView mTodayTV;
    private TrafficDao dao;
    private ImageView mRemindImgv;
    private TextView mRemindTV;
    private CorrectFlowReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_traffic_monitoring);
        mSp = getSharedPreferences("config", MODE_PRIVATE);
        boolean flag = mSp.getBoolean("isset_operator", false);
        //如果没有运营商信息，就直接进入信息设置界面
        if (!flag) {
            startActivity(new Intent(this, OperatorSet.class));
            finish();
        }

        /**
         * 服务没有写
         */
        if (SystemInfoUtils.isServiceRunning(this, "com.example.gongzhiyao.safemanagerofmobile.OperatorSet.Service.TrafficMonitoringService")) {
            startService(new Intent(this, TrafficMonitoringService.class));
        }

        initView();
        register_receiver();
        initData();


    }

    private void register_receiver() {
        receiver = new CorrectFlowReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(receiver, filter);
    }

    private void initData() {
        long totalflow = mSp.getInt("totalflow", 0);
        long usedflow = mSp.getLong("usedflow", 0);
        if (totalflow > 0 & usedflow >= 0) {
            float scale = usedflow / totalflow;
            if (scale > 0.9) {
                mRemindImgv.setEnabled(false);
                mRemindTV.setText("您的套餐流量即将用完！");
            } else {
                mRemindImgv.setEnabled(true);
                mRemindTV.setText("本月流量充足请放心使用");
            }
        }

        mTotalTV.setText("本月流量：" + Formatter.formatFileSize(this, totalflow));
        mUsedTV.setText("本月已用：" + Formatter.formatFileSize(this, usedflow));
        dao = new TrafficDao(this);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(date);
        long mobileGPRS = dao.getMobileGPRS(dateString);
        if (mobileGPRS < 0) {
            mobileGPRS = 0;
        }
        mTodayTV.setText("今天已用：" + Formatter.formatFileSize(this, mobileGPRS));
    }

    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.light_green));
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        ((TextView) findViewById(R.id.tv_title)).setText("流量监控");
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setBackgroundResource(R.drawable.back);
        mCorrectFlowBtn = (Button) findViewById(R.id.btn_correction_flow);
        mCorrectFlowBtn.setOnClickListener(this);
        mTotalTV = (TextView) findViewById(R.id.tv_month_totalgprs);
        mUsedTV = (TextView) findViewById(R.id.tv_month_usedgprs);
        mTodayTV = (TextView) findViewById(R.id.tv_today_gprs);
        mRemindImgv = (ImageView) findViewById(R.id.imgv_traffic_remind);
        mRemindTV = (TextView) findViewById(R.id.tv_traffic_remind);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.btn_correction_flow:
                //首先判断是哪个运营商
                int i = mSp.getInt("operator", 0);
                SmsManager smsManager = SmsManager.getDefault();
                switch (i) {
                    case 0:
                        /**没有设置运营商*/
                        Toast.makeText(getApplicationContext(), "您还未设置运营商", Toast.LENGTH_SHORT).show();

                        break;

                    case 1:
                        //中国移动
                        //发送CXGPRS到10086
                        smsManager.sendTextMessage("10086", null, "CXGPRS", null, null);
                        break;
                    case 2:
                        //中国联通，发送LLCX到10010
                        //获取系统默认的短信管理器
                        smsManager.sendTextMessage("10010", null, "LLCX", null, null);
                        break;
                    case 3:
                        //中国电信
                        //发送CXLL到10001
                        smsManager.sendTextMessage("10001", null, "CXLL", null, null);
                        break;
                }
                break;
        }
    }

    class CorrectFlowReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] objs = (Object[]) intent.getExtras().get("pdus");
            for (Object obj : objs) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
                String body = smsMessage.getMessageBody();
                String address = smsMessage.getOriginatingAddress();
                //根据不同的运营商判断
                if (!address.equals("10010")) {
                    return;
                }
                String[] split = body.split("，");
                //本月剩余
                long left = 0;
                //本月已使用
                long used = 0;
                //本月超出
                long beyond = 0;

                for (int i = 0; i < split.length; i++) {
                    if (split[i].contains("本月流量已使用")) {
                        String usedflow = split[i].substring(7, split[i].length());
                        used = getStringTofloat(usedflow);
                    } else if (split[i].contains("剩余流量")) {
                        String leftflow = split[i].substring(4, split[i].length());
                        left = getStringTofloat(leftflow);
                    } else if (split[i].contains("流量外套餐")) {
                        String beyondflow = split[i].substring(5, split[i].length());
                        beyond = getStringTofloat(beyondflow);
                    }
                }


                SharedPreferences.Editor editor = mSp.edit();
                editor.putLong("totalflow", used + left);
                editor.putLong("usedflow", used + beyond);
                editor.commit();
                mTotalTV.setText("本月流量：" + Formatter.formatFileSize(context, (used + left)));
                mUsedTV.setText("本月已用：" + Formatter.formatFileSize(context, (used + beyond)));
            }
        }
    }

    private long getStringTofloat(String str) {
        long flow = 0;
        if (!TextUtils.isEmpty(str)) {
            if (str.contains("KB")) {
                String[] split = str.split("KB");
                float m = Float.parseFloat(split[0]);
                flow = (long) (m * 1024);
            } else if (str.contains("MB")) {
                String[] split = str.split("MB");
                float m = Float.parseFloat(split[0]);
                flow = (long) (m * 1024 * 1024);
            } else if (str.contains("GB")) {
                String[] split = str.split("GB");
                float m = Float.parseFloat(split[0]);
                flow = (long) (m * 1024 * 1024 * 1024);
            }
        }
        return flow;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }
}


