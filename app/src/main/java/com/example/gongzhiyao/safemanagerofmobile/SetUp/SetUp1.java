package com.example.gongzhiyao.safemanagerofmobile.SetUp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;

import com.example.gongzhiyao.safemanagerofmobile.R;

public class SetUp1 extends BaseSetUp {

    RadioButton firstRb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up1);
        initView();
    }


    private void initView() {
        firstRb= (RadioButton) findViewById(R.id.rb_first);
        firstRb.setChecked(true);
    }


    @Override
    public void showPre() {

    }

    @Override
    public void showNext(){
        startActivityAndFinishSelf(SetUp2.class);
    }
}
