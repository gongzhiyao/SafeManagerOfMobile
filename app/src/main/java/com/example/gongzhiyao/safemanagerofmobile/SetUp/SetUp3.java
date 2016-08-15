package com.example.gongzhiyao.safemanagerofmobile.SetUp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gongzhiyao.safemanagerofmobile.Contact_select.Contact_Select;
import com.example.gongzhiyao.safemanagerofmobile.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetUp3 extends BaseSetUp implements View.OnClickListener {

    private EditText mInputPhone;
    private RadioButton rb;
    private Button add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up3);
        initView();
    }

    private void initView() {
        rb= (RadioButton) findViewById(R.id.rb_third);
        rb.setChecked(true);
        add= (Button) findViewById(R.id.btn_addcontact);
        add.setOnClickListener(this);
        mInputPhone= (EditText) findViewById(R.id.et_inputphone);
        String safenum=sp.getString("safephone",null);
        if(!TextUtils.isEmpty(safenum)){
            mInputPhone.setText(safenum);
        }

    }

    @Override
    public void showPre() {
        startActivityAndFinishSelf(SetUp2.class);
    }

    @Override
    public void showNext() {
        String safenum=mInputPhone.getText().toString().trim();
        if(TextUtils.isEmpty(safenum)){
            Toast.makeText(getApplicationContext(),"请输入安全号码",Toast.LENGTH_SHORT).show();
            return;
        }


        /**
         * 在这里可以提醒用户是否应用更改的手机号
         */

        /**
         * 在这里还要检查是否是号码
         */
        if(checkPhone(safenum)) {

            sp.edit().putString("safephone", safenum).commit();
            startActivityAndFinishSelf(SetUp4.class);
        }else {
            Toast.makeText(getApplicationContext(),"您输入的号码格式不正确，请重新输入！",Toast.LENGTH_SHORT).show();
            mInputPhone.setText("");
        }
    }

    public static boolean checkPhone(String phone){
        Pattern pattern=Pattern.compile("^(13[0-9]|15[0-9]|153|15[6-9]|180|18[5-9])\\d{8}$");
        Matcher matcher=pattern.matcher(phone);
        if(matcher.matches()){
            return true;
        }
        return false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_addcontact:
                startActivityForResult(new Intent(this,Contact_Select.class),0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            String phone=data.getStringExtra("phone");
            mInputPhone.setText(phone);
        }

    }
}
