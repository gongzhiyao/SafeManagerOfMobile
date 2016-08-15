package com.example.gongzhiyao.safemanagerofmobile.ProgressManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.gongzhiyao.safemanagerofmobile.R;

public class ProcessManagerSetting extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private ToggleButton mShowSysAppsTgb;
    private ToggleButton mKillProcessTgb;
    private SharedPreferences mSp;
    private boolean running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_process_manager_setting);
        mSp = getSharedPreferences("config", MODE_PRIVATE);
        initView();
    }

    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.bright_green));
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setBackgroundResource(R.drawable.back);
        ((TextView) findViewById(R.id.tv_title)).setText("进程管理设置");
        mShowSysAppsTgb = (ToggleButton) findViewById(R.id.tgb_showsys_progress);
        mKillProcessTgb = (ToggleButton) findViewById(R.id.tgb_killprocess_lockscreen);
        mShowSysAppsTgb.setChecked(mSp.getBoolean("showSystemProcess", true));
        running = SystemInfoUtils.isServiceRunning(this, "com.example.gongzhiyao.safemanagerofmobile.ProgressManager.AutoKillProcessService");
        mKillProcessTgb.setChecked(running);
        initListener();

    }

    private void initListener() {
        mKillProcessTgb.setOnCheckedChangeListener(this);
        mShowSysAppsTgb.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_leftbtn:
                finish();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.tgb_killprocess_lockscreen:
                Intent service = new Intent(this, AutoKillProcessService.class);
                if (isChecked) {
                    startService(service);
                } else {
                    stopService(service);
                }
                break;

            case R.id.tgb_showsys_progress:
                saveStatus("showSystemProcess", isChecked);
                break;
        }
    }

    private void saveStatus(String string, boolean isChecked) {
        mSp.edit().putBoolean(string, isChecked).commit();
    }
}
