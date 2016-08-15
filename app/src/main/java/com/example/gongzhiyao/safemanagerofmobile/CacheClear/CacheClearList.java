package com.example.gongzhiyao.safemanagerofmobile.CacheClear;

import android.content.Intent;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gongzhiyao.safemanagerofmobile.Adapter.CacheCleanAdapter;
import com.example.gongzhiyao.safemanagerofmobile.Log.L;
import com.example.gongzhiyao.safemanagerofmobile.R;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CacheClearList extends AppCompatActivity implements View.OnClickListener {

    protected static final int SCANNING = 100;
    protected static final int FINISH = 101;
    private AnimationDrawable animationDrawable;

    /**
     * 建议清理
     **/
    private TextView mRecomandTV;
    /**
     * 可清理
     **/
    private TextView mCancleanTV;

    private long cacheMemory;
    private List<CacheInfo> cacheInfos = new ArrayList<CacheInfo>();
    private List<CacheInfo> mCacheInfos = new ArrayList<CacheInfo>();

    private PackageManager pm;
    private CacheCleanAdapter adapter;
    private ListView mCacheLV;
    private Button mCacheBtn;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SCANNING:
                    PackageInfo info = (PackageInfo) msg.obj;
                    mRecomandTV.setText("正在扫描：" + info.packageName);
                    mCancleanTV.setText("已扫描缓存：" + Formatter.formatFileSize(CacheClearList.this, cacheMemory));
                    //在主线程添加变化后的集合
                    mCacheInfos.clear();
                    mCacheInfos.addAll(cacheInfos);
                    //lv刷新
                    adapter.notifyDataSetChanged();
                    //滑到最后
                    mCacheLV.setSelection(mCacheInfos.size());
                    break;
                case FINISH:
                    //扫描完毕，停止动画
                    animationDrawable.stop();
                    if (cacheMemory > 0) {
                        mCacheBtn.setEnabled(true);
                    } else {
                        mCacheBtn.setEnabled(false);
                        Toast.makeText(getApplicationContext(), "您的手机清洁如新", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cache_clear_list);
        pm = getPackageManager();
        initView();

    }

    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.rose_red));
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setBackgroundResource(R.drawable.back);
        ((TextView) findViewById(R.id.tv_title)).setText("缓存扫描");
        mRecomandTV = (TextView) findViewById(R.id.tv_recommend_clean);
        mCancleanTV = (TextView) findViewById(R.id.tv_can_clean);
        mCacheLV = (ListView) findViewById(R.id.lv_scancache);
        mCacheBtn = (Button) findViewById(R.id.btn_cleanall);
        mCacheBtn.setOnClickListener(this);
        animationDrawable = (AnimationDrawable) findViewById(R.id.imgv_broom).getBackground();
        animationDrawable.setOneShot(false);
        animationDrawable.start();
        adapter = new CacheCleanAdapter(this, mCacheInfos);
        mCacheLV.setAdapter(adapter);
        fillData();

    }

    private void fillData() {
        thread = new Thread() {
            @Override
            public void run() {
                super.run();
                cacheInfos.clear();
                List<PackageInfo> infos = pm.getInstalledPackages(0);
                for (PackageInfo info : infos) {
                    getCacheSize(info);
                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message msg = Message.obtain();
                    msg.obj = info;
                    msg.what = SCANNING;
                    mHandler.sendMessage(msg);

                }

                Message msg = Message.obtain();
                msg.what = FINISH;
                mHandler.sendMessage(msg);
            }
        };
        thread.start();
    }


    /**
     * 关键
     * 获取每个包名对用的应用层序的缓存的大小
     *
     * @param info
     */
    private void getCacheSize(PackageInfo info) {
        try {
            Method method = PackageManager.class.getDeclaredMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
            method.invoke(pm, info.packageName, new MyPackObserver(info));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        animationDrawable.stop();
        if (thread != null) {
            thread.interrupt();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.btn_cleanall:
                if (cacheMemory > 0) {
                    Intent intent = new Intent(this, CleanCache.class);
                    intent.putExtra("cacheMemory", cacheMemory);
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }


    private class MyPackObserver extends android.content.pm.IPackageStatsObserver.Stub {
        private PackageInfo info;

        public MyPackObserver(PackageInfo info) {
            this.info = info;
        }

        @Override
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
            long cachesize = pStats.cacheSize;
            if (cachesize >= 0) {
                CacheInfo cacheInfo = new CacheInfo();
                cacheInfo.cacheSize = cachesize;
                cacheInfo.packageName = info.packageName;
                cacheInfo.appName = info.applicationInfo.loadLabel(pm).toString();
                cacheInfo.appIcon = info.applicationInfo.loadIcon(pm);
                cacheInfos.add(cacheInfo);
                cacheMemory += cachesize;
            }
        }


    }


}
