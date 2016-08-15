package com.example.gongzhiyao.safemanagerofmobile.OperatorSet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.gongzhiyao.safemanagerofmobile.R;

public class OperatorSet extends AppCompatActivity implements View.OnClickListener {

    private Spinner mSelectSP;
    private String[] operators = {"中国移动", "中国联通", "中国电信"};
    private ArrayAdapter mSelectadapter;
    private SharedPreferences mSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_operator_set);
        mSp = getSharedPreferences("config", MODE_PRIVATE);
        initView();
    }

    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.light_green));
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        ((TextView) findViewById(R.id.tv_title)).setText("运营商信息");
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setBackgroundResource(R.drawable.back);
        mSelectSP = (Spinner) findViewById(R.id.spinner_operator_select);
        mSelectadapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, operators);
        mSelectSP.setAdapter(mSelectadapter);
        findViewById(R.id.btn_operator_finish).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_leftbtn:
//                mSp.edit().putBoolean("isset_operator", false);
                finish();
                break;

            case R.id.btn_operator_finish:
                mSp.edit().putInt("operator", mSelectSP.getSelectedItemPosition() + 1).commit();
                mSp.edit().putBoolean("isset_operator", true).commit();
                startActivity(new Intent(this, TrafficMonitoring.class));
                finish();
                break;
        }
    }
}
