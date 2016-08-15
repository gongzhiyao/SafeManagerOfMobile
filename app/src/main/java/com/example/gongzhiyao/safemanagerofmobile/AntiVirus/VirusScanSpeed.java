package com.example.gongzhiyao.safemanagerofmobile.AntiVirus;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gongzhiyao.safemanagerofmobile.Adapter.ScanVirusAdapter;
import com.example.gongzhiyao.safemanagerofmobile.R;
import com.example.gongzhiyao.safemanagerofmobile.Util.MD5Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VirusScanSpeed extends AppCompatActivity implements View.OnClickListener {

    public static final int SCAN_BEGIN = 100;
    public static final int SCANNING = 101;
    public static final int SCAN_FINISH = 102;
    private int total;
    private int progress;
    private TextView mprogressTv;
    private PackageManager pm;
    private boolean flag;
    private boolean isStop;
    private TextView mSanAppTv;
    private Button mCancleBtn;
    private ImageView mScanningIcon;
    private RotateAnimation rani;
    private ListView mScanListView;
    private List<ScanAppInfo> scanAppInfos = new ArrayList<ScanAppInfo>();
    private SharedPreferences mSp;
    private ScanVirusAdapter adapter;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SCAN_BEGIN:
                    mSanAppTv.setText("初始化杀毒引擎中...");
                    break;
                case SCANNING:
                    ScanAppInfo info = (ScanAppInfo) msg.obj;
                    mSanAppTv.setText("正在扫描：" + info.appName);
                    int speed = msg.arg1;
                    mprogressTv.setText((speed * 100 / total) + "%");
                    scanAppInfos.add(info);
                    adapter.notifyDataSetChanged();
                    mScanListView.setSelection(scanAppInfos.size());
                    break;
                case SCAN_FINISH:
                    mSanAppTv.setText("扫描完成！");
                    mScanningIcon.setBackgroundResource(R.drawable.scan_complete);
                    saveScanTime();
                    break;
            }
        }
    };

    private void saveScanTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String CurrentTime = format.format(new Date());
        CurrentTime = "上次查杀：" + CurrentTime;
        mSp.edit().putString("lastVirusScan", CurrentTime).commit();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_virus_scan_speed);
        pm = getPackageManager();
        mSp = getSharedPreferences("config", MODE_PRIVATE);
        initView();
        scanVirus();
    }

    private void scanVirus() {
        flag = true;
        isStop = false;
        progress = 0;
        scanAppInfos.clear();
        new Thread() {
            @Override
            public void run() {
                super.run();
                Message msg = Message.obtain();
                msg.what = SCAN_BEGIN;
                mHandler.sendMessage(msg);
                List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
                total = installedPackages.size();
                for (PackageInfo info : installedPackages) {
//                    System.out.println("循环");
                    if (!flag) {
                        isStop = true;
//                        System.out.println("111111111");
                        return;
                    }
//                    System.out.println("2222222");
                    String apkpath = info.applicationInfo.sourceDir;
                    //检查MD5码
                    String md5info = MD5Utils.getFileMd5(apkpath);
                    String result = AntiVirusDao.checkVirus(md5info);
                    msg = Message.obtain();
                    msg.what = SCANNING;
                    ScanAppInfo scanAppInfo = new ScanAppInfo();
                    if (result == null) {
                        scanAppInfo.description = "扫描安全";
                        scanAppInfo.isVirus = false;
                    } else {
                        scanAppInfo.description = result;
                        scanAppInfo.isVirus = true;
                    }
                    progress++;
                    scanAppInfo.packagename = info.packageName;
                    scanAppInfo.appName = info.applicationInfo.loadLabel(pm).toString();
                    scanAppInfo.appicon = info.applicationInfo.loadIcon(pm);
                    msg.obj = scanAppInfo;
                    msg.arg1 = progress;
                    mHandler.sendMessage(msg);
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                msg = Message.obtain();
                msg.what = SCAN_FINISH;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.light_blue));
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setImageResource(R.drawable.back);
        mLeftImgv.setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_title)).setText("病毒查杀进度");
        mprogressTv = (TextView) findViewById(R.id.tv_scanprogress);
        mSanAppTv = (TextView) findViewById(R.id.tv_scansapp);
        mCancleBtn = (Button) findViewById(R.id.btn_cancelscan);
        mCancleBtn.setOnClickListener(this);
        mScanListView = (ListView) findViewById(R.id.lv_scanapps);
        adapter = new ScanVirusAdapter(scanAppInfos, this);
        mScanListView.setAdapter(adapter);
        mScanningIcon = (ImageView) findViewById(R.id.imgv_scanningicon);
        startAnim();

    }

    private void startAnim() {
        if (rani == null) {
            rani = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        }
        rani.setRepeatCount(Animation.INFINITE);
        rani.setDuration(2000);
        mScanningIcon.startAnimation(rani);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.btn_cancelscan:
                if (progress == total & progress > 0) {
                    /**扫描已完成**/
                    finish();
                } else if (progress > 0 & progress < total & isStop == false) {
                    mScanningIcon.clearAnimation();
                    /**取消扫描**/
                    flag = false;
                    mCancleBtn.setBackgroundResource(R.drawable.restart_scan_btn);
                } else if (isStop) {
                    startAnim();
                    scanVirus();
                    mCancleBtn.setBackgroundResource(R.drawable.cancel_scan_btn_selector);
                }
                break;
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        flag = false;

    }
}
