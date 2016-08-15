package com.example.gongzhiyao.safemanagerofmobile.SetUp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.gongzhiyao.safemanagerofmobile.R;

public class LostFind extends AppCompatActivity implements View.OnClickListener {

    private TextView mSafePhoneTV, Title_TV;
    private RelativeLayout mInterSetUpRL;
    private SharedPreferences mSharedPreferences;
    private ToggleButton mToggleButton;
    private TextView mprotectStatus;
    private ImageView image_left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_lost_find);
        mSharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        if (!isSetUp()) {
            startSetUpActivity();
        }
        initView();
    }

    private void initView() {
        Title_TV = (TextView) findViewById(R.id.tv_title);
        Title_TV.setText("手机防盗");
        image_left = (ImageView) findViewById(R.id.imgv_leftbtn);
        image_left.setOnClickListener(this);
        image_left.setImageResource(R.drawable.back);
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.purple));
        mSafePhoneTV= (TextView) findViewById(R.id.tv_safephone);
        mSafePhoneTV.setText(mSharedPreferences.getString("safephone",""));
        mToggleButton= (ToggleButton) findViewById(R.id.togglebtn_lostfind);
        mInterSetUpRL= (RelativeLayout) findViewById(R.id.rl_inter_setup_wizard);
        mInterSetUpRL.setOnClickListener(this);
        mprotectStatus= (TextView) findViewById(R.id.tv_lostfind_protectstauts);
        boolean isprotecting =mSharedPreferences.getBoolean("protecting",true);
        if(isprotecting){
            mprotectStatus.setText("防盗保护已开启");
            mToggleButton.setChecked(true);
        }else {
            mprotectStatus.setText("防盗保护尚未开启");
            mToggleButton.setChecked(false);
        }
        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mprotectStatus.setText("防盗保护已开启");
                }else {
                    mprotectStatus.setText("防盗保护尚未开启");
                }
                mSharedPreferences.edit().putBoolean("protecting",isChecked).commit();
            }
        });


    }

    private boolean isSetUp() {
        return mSharedPreferences.getBoolean("isSetUp", false);
    }

    private void startSetUpActivity() {
        Intent i = new Intent(getApplicationContext(), SetUp1.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.imgv_leftbtn:
                finish();
                break;

            case R.id.rl_inter_setup_wizard:
                startSetUpActivity();
                break;
        }
    }
}
