package com.example.gongzhiyao.safemanagerofmobile.SecurityPhone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gongzhiyao.safemanagerofmobile.BlackContact.BlackContactInfo;
import com.example.gongzhiyao.safemanagerofmobile.BlackContact.BlackNumberDBOperation;
import com.example.gongzhiyao.safemanagerofmobile.Contact_select.Contact_Select;
import com.example.gongzhiyao.safemanagerofmobile.R;

public class AddBlackNumber extends AppCompatActivity implements View.OnClickListener {

    private CheckBox mSmsCB, mTelCB;
    private EditText mNumET, mNameET;
    private BlackNumberDBOperation operation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_black_number);
        operation = new BlackNumberDBOperation(this);
        initView();
    }

    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.bright_purple));
        ((TextView) findViewById(R.id.tv_title)).setText("添加黑名单");
        ImageView mLeft = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeft.setOnClickListener(this);
        mLeft.setBackgroundResource(R.drawable.back);
        mSmsCB = (CheckBox) findViewById(R.id.cb_blacknumber_sms);
        mTelCB = (CheckBox) findViewById(R.id.cb_blacknumber_tel);
        mNumET = (EditText) findViewById(R.id.et_blacknumber);
        mNameET = (EditText) findViewById(R.id.et_blackname);
        findViewById(R.id.add_blacknum_btn).setOnClickListener(this);
        findViewById(R.id.add_fromcontact_btn).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.add_blacknum_btn:
                String number = mNumET.getText().toString().trim();
                System.out.println("获得号码"+number);
                String name = mNameET.getText().toString().trim();
                if (TextUtils.isEmpty(number) || TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(), "号码和手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    BlackContactInfo blackContactInfo = new BlackContactInfo();
                    blackContactInfo.PhoneNumber = number;
                    blackContactInfo.ContactName = name;
                    if (mSmsCB.isChecked() & mTelCB.isChecked()) {
                        /**两种拦截模式都选**/
                        blackContactInfo.mode = 3;
                    } else if (mSmsCB.isChecked() & !mTelCB.isChecked()) {
                        /**短信拦截**/
                        blackContactInfo.mode = 2;
                    } else if (!mSmsCB.isChecked() & mTelCB.isChecked()) {
                        /**电话拦截**/
                        blackContactInfo.mode = 1;
                    } else {
                        Toast.makeText(this, "请选择拦截模式", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!operation.isNumberExist(blackContactInfo.PhoneNumber)) {
                        operation.add(blackContactInfo);
                        Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "该号码已经存在黑名单中", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }
                break;
            case R.id.add_fromcontact_btn:
                startActivityForResult(new Intent(this, Contact_Select.class), 0);
                break;

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            String phone = data.getStringExtra("phone");
            String name = data.getStringExtra("name");
            mNameET.setText(name);
            mNumET.setText(phone);
        }
    }
}
