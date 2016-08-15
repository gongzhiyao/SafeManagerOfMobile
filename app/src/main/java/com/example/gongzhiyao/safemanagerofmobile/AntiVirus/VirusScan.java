package com.example.gongzhiyao.safemanagerofmobile.AntiVirus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gongzhiyao.safemanagerofmobile.Log.L;
import com.example.gongzhiyao.safemanagerofmobile.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class VirusScan extends AppCompatActivity implements View.OnClickListener {

    private TextView mlastTimeTv;
    private SharedPreferences mSp;
    private L log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_virus_scan);
        mSp = getSharedPreferences("config", MODE_PRIVATE);
        log = new L();
        copyDB("antivirus.db");
        initView();
    }

    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.light_blue));
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setImageResource(R.drawable.back);
        mLeftImgv.setOnClickListener(this);
        mlastTimeTv = (TextView) findViewById(R.id.tv_lastscantime);
        findViewById(R.id.rl_allscanvirus).setOnClickListener(this);
    }

    private void copyDB(final String dbname) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {

                    /**
                     * 不知怎么得无法存入file文件中，将其放到sd卡目录
                     */

                    setDir();

                    /**getFileDir()直接获得file的路径**/
//                    String path = "/data/data/" + getPackageName() + "/databases";
//                    String dbPath = getFilesDir().getAbsolutePath();
//                    log.d(dbPath);
//                    dbPath=dbPath.replace("files", "databases/");
                    String dbPath = Environment.getExternalStorageDirectory() + File.separator + "SMOM" + File.separator + "db";
                    log.d(dbPath);
                    File file = new File(dbPath, dbname);
//                    log.d("name" + dbname);
                    log.d(file.getAbsolutePath());
                    if (file.exists() && file.length() > 0) {
                        log.d("数据库存在");
                        return;
                    }
                    InputStream is = getAssets().open(dbname);//读取文件流
                    /**openFileOutput发生在files文件夹下**/
//                    FileOutputStream fos = openFileOutput(dbname, MODE_PRIVATE);//获得写入流
                    FileOutputStream fos = new FileOutputStream(file);
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
//                        System.out.println(buffer.toString());
                        log.d("写入中");
                    }
                    //fos.flush();//FileOutputStream 的 flush 不执行任何操作
                    is.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        }.start();

    }

    private void setDir() {
        String path = Environment.getExternalStorageDirectory() + File.separator + "SMOM" + File.separator + "db";
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.rl_allscanvirus:
                startActivity(new Intent(this, VirusScanSpeed.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String str = mSp.getString("lastVirusScan", "您还没有查杀病毒");
        mlastTimeTv.setText(str);
    }
}
