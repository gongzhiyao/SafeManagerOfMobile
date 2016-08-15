package com.example.gongzhiyao.safemanagerofmobile.AppManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gongzhiyao.safemanagerofmobile.Adapter.AppManagerAdapter;
import com.example.gongzhiyao.safemanagerofmobile.AppInfo.AppInfo;
import com.example.gongzhiyao.safemanagerofmobile.AppInfo.AppInfoParser;
import com.example.gongzhiyao.safemanagerofmobile.Application.App;
import com.example.gongzhiyao.safemanagerofmobile.Log.L;
import com.example.gongzhiyao.safemanagerofmobile.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public class AppManager extends AppCompatActivity implements View.OnClickListener {
    private TextView mPhoneMemoryTV;
    private TextView mSDMemoryTV;
    private ListView mListView;
    private List<AppInfo> appInfos;
    private List<AppInfo> userAppInfos = new ArrayList<AppInfo>();
    private List<AppInfo> systemAppInfos = new ArrayList<AppInfo>();
    private AppManagerAdapter adapter;

    private UninstallReceiver receiver;
    private TextView mAppNumTV;
    private L log;
    private Handler mHanlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 10:
                    if (adapter == null) {
                        adapter = new AppManagerAdapter(getApplicationContext(), userAppInfos, systemAppInfos);
                    }
                    mListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    /**
                     * 在这里需要setAdapter
                     */


                    break;

                case 15:
                    /**
                     * 这里需要刷新数据
                     */
                    adapter.notifyDataSetChanged();


                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_app_manager);
        /**
         * 动态注册广播
         */
        log = new L();
        receiver = new UninstallReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        registerReceiver(receiver, filter);

        initView();


    }

    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.bright_yellow));
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        ((TextView) findViewById(R.id.tv_title)).setText("软件管家");
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        mPhoneMemoryTV = (TextView) findViewById(R.id.tv_phonememory_appmanager);
        mSDMemoryTV = (TextView) findViewById(R.id.tv_sdmemory_appmanager);
        mAppNumTV = (TextView) findViewById(R.id.tv_appnumber);
        mListView = (ListView) findViewById(R.id.lv_appmanager);
        /**取得手机剩余内存和sd卡剩余内存**/
        getMemoryFromPhone();
        initData();
        initListener();

    }

    private void initListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (adapter != null) {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            AppInfo mappinfo = (AppInfo) adapter.getItem(position);
                            //记住当前条目

                            boolean flag = mappinfo.isSelected;
                            /**把所有的item变为未选中**/
                            for (AppInfo appinfo : userAppInfos) {
                                appinfo.isSelected = false;
                            }
                            for (AppInfo appinfo : systemAppInfos) {
                                appinfo.isSelected = false;
                            }

                            /**已选中转为未选中，未选中改为选中**/
                            if (mappinfo != null) {
                                if (flag) {
                                    mappinfo.isSelected = false;
                                } else {
                                    mappinfo.isSelected = true;
                                }
                            }
                            mHanlder.sendEmptyMessage(15);

                        }
                    }.start();
                }
            }
        });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem >= userAppInfos.size() + 1) {
                    mAppNumTV.setText("系统应用：" + systemAppInfos.size() + "个");
                } else {
                    mAppNumTV.setText("用户程序：" + userAppInfos.size() + "个");
                }
            }
        });

    }

    private void getMemoryFromPhone() {
        /**分别获取内存和外存的剩余内存**/

        long avail_sd = Environment.getExternalStorageDirectory().getFreeSpace();
        long avail_rom = Environment.getDataDirectory().getFreeSpace();
        //格式化内存,将内存显示为可读懂的格式
        String str_avail_sd = android.text.format.Formatter.formatFileSize(this, avail_sd);
        String str_avail_rom = android.text.format.Formatter.formatFileSize(this, avail_rom);
        mPhoneMemoryTV.setText("剩余手机内存：" + str_avail_rom);
        mSDMemoryTV.setText("剩余SD卡内存：" + str_avail_sd);

    }

    private void initData() {
        appInfos = new ArrayList<AppInfo>();
        new Thread() {
            @Override
            public void run() {
                super.run();
                appInfos.clear();
                userAppInfos.clear();
                systemAppInfos.clear();
                List<AppInfo> infos = AppInfoParser.getAppInfo(AppManager.this);
                appInfos.addAll(infos);
                for (AppInfo appInfo : appInfos) {
                    if (appInfo.isUserApp) {
                        userAppInfos.add(appInfo);
                    } else {
                        systemAppInfos.add(appInfo);
                    }
                }
                mHanlder.sendEmptyMessage(10);

            }
        }.start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_leftbtn:

                finish();
                break;
        }
    }


    public class UninstallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            initData();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        receiver = null;
    }
}
