package com.example.gongzhiyao.safemanagerofmobile;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.gongzhiyao.safemanagerofmobile.Adapter.HomeAdapter;
import com.example.gongzhiyao.safemanagerofmobile.AntiVirus.VirusScan;
import com.example.gongzhiyao.safemanagerofmobile.AppManager.AppManager;
import com.example.gongzhiyao.safemanagerofmobile.CacheClear.CacheClearList;
import com.example.gongzhiyao.safemanagerofmobile.Dialogs.InterPasswordDialog;
import com.example.gongzhiyao.safemanagerofmobile.Dialogs.SetUpPasswdDialog;
import com.example.gongzhiyao.safemanagerofmobile.Log.L;
import com.example.gongzhiyao.safemanagerofmobile.OperatorSet.OperatorSet;
import com.example.gongzhiyao.safemanagerofmobile.OperatorSet.TrafficMonitoring;
import com.example.gongzhiyao.safemanagerofmobile.ProgressManager.ProgressManager;
import com.example.gongzhiyao.safemanagerofmobile.Receiver.MyDeviceAdminReciever;
import com.example.gongzhiyao.safemanagerofmobile.SecurityPhone.SecurityPhone;
import com.example.gongzhiyao.safemanagerofmobile.SetUp.SetUp1;

public class HomeActivity extends AppCompatActivity {

    private HomeAdapter adapter;

    private L log;
    private GridView gv_home;
    private SharedPreferences sp;
    /**
     * 设备管理员
     */
    private DevicePolicyManager manager;
    /**
     * 申请权限
     **/
    private ComponentName componentName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        boolean is = sp.getBoolean("protecting", false);
        log.d("保护开启是" + is);
        log = new L();
        init();
        adapter = new HomeAdapter(this, R.layout.item_home);
        gv_home.setAdapter(adapter);

        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {

                    case 0:
                        if (isSetUpPasswd()) {

                            showInterPswdDialog();
                        } else {
                            showSetUpPswdDialog();
                        }
                        break;
                    case 1:
                        startActivity(SecurityPhone.class);
                        break;
                    case 2:
                        startActivity(AppManager.class);
                        break;
                    case 3:
                        startActivity(VirusScan.class);
                        break;
                    case 4:
                        startActivity(CacheClearList.class);
                        break;
                    case 5:
                        startActivity(ProgressManager.class);
                        break;
                    case 6:
                        startActivity(TrafficMonitoring.class);
                        break;
                    case 7:

                        break;
                    case 8:

                        break;

                }
            }
        });
        /**获取管理员**/
        manager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        /**申请权限**/
        componentName = new ComponentName(this, MyDeviceAdminReciever.class);
        /**判断，如果没有权限，则申请权限**/
        boolean active = manager.isAdminActive(componentName);
        if (!active) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "获取超级管理员权限，用于远程锁屏和清除数据");
            startActivity(intent);
        }


    }

    public void init() {
        gv_home = (GridView) findViewById(R.id.gv_home);

    }


    public boolean isSetUpPasswd() {

        String passwd = sp.getString("passwd", "");
//        boolean isSet = sp.getBoolean("isSetUpPasswd", false);
        if (!TextUtils.isEmpty(passwd)) {
            return true;
        }
        return false;

    }

    public void startActivity(Class<?> cla) {
        Intent intent = new Intent(HomeActivity.this, cla);
        startActivity(intent);
    }


    long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - mExitTime < 2000) {
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            }
            return true;
        }


        return super.onKeyDown(keyCode, event);
    }


    public void showInterPswdDialog() {
        final InterPasswordDialog interdialog = new InterPasswordDialog(this);
        interdialog.setMyCallBack(new InterPasswordDialog.MyCallBack() {
            @Override
            public void confirm() {

                String passwd = sp.getString("passwd", null);
                String passwd_inter = interdialog.getInterPasswd();
                if (passwd != null && passwd.equals(passwd_inter)) {
                    Toast.makeText(getApplicationContext(), "密码输入正确", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), SetUp1.class));
                    interdialog.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "密码错误，请重试", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void cancle() {

                interdialog.dismiss();

            }
        });
        interdialog.setCancelable(true);
        interdialog.show();
    }


    public void showSetUpPswdDialog() {
        final SetUpPasswdDialog setdialog = new SetUpPasswdDialog(this);
        setdialog.setMyCallBack(new SetUpPasswdDialog.MyCallBack() {
            @Override
            public void ok() {

                String passwd1 = setdialog.getFirstPasswd();
                String passwd2 = setdialog.getSecondPasswd();
                log.d("第一次输入的密码是" + passwd1 + "\n" + "第二次输入的密码是" + passwd2);

                if (passwd1.equals(passwd2) && !passwd1.equals("")) {
                    sp.edit().putString("passwd", passwd1).commit();
                    Toast.makeText(getApplicationContext(), "密码设置成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), SetUp1.class));
//                    sp.edit().putBoolean("isSetUpPasswd",true).commit();
                    setdialog.dismiss();
                } else if (!passwd1.equals(passwd2)) {
                    Toast.makeText(getApplicationContext(), "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                } else if (passwd1.equals("") && passwd2.equals("")) {
                    Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void cancel() {

                setdialog.dismiss();
                log.d("444444444444444444444444");
            }
        });

        setdialog.setCancelable(true);
        setdialog.show();
    }


}
