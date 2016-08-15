package com.example.gongzhiyao.safemanagerofmobile.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.gongzhiyao.safemanagerofmobile.R;

/**
 * Created by 宫智耀 on 2016/6/21.
 */
public class SetUpPasswdDialog extends Dialog implements View.OnClickListener {

    public SetUpPasswdDialog(Context context) {
        /**
         * 绑定主题
         */
        super(context,R.style.dialog_custom);
    }
    private TextView mTitleTV;
    private EditText mFirstPasswdET,mSecondPasswdET;
    private Button btn_Ok,btn_Cancel;
    private MyCallBack myCallBack;

    public void setMyCallBack(MyCallBack myCallBack){
        this.myCallBack=myCallBack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup_passwd_dialog);
        init();

    }

    public void init(){
        mTitleTV= (TextView) findViewById(R.id.tv_setup_passwd_title);
        mFirstPasswdET= (EditText) findViewById(R.id.et_first_passwd);
        mSecondPasswdET= (EditText) findViewById(R.id.et_second_passwd);
        btn_Ok= (Button) findViewById(R.id.btn_ok);
        btn_Cancel= (Button) findViewById(R.id.btn_cancel);
        btn_Ok.setOnClickListener(this);
        btn_Cancel.setOnClickListener(this);

    }


    public void setmTitleTV(String title){
        if(!title.equals("")){
            mTitleTV.setText(title);
        }
    }


    public String getFirstPasswd(){
        return mFirstPasswdET.getText().toString();
    }

    public String getSecondPasswd(){
        return mSecondPasswdET.getText().toString();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_ok:
                myCallBack.ok();
                break;
            case  R.id.btn_cancel:
                myCallBack.cancel();
                break;
        }
    }


    public interface MyCallBack{
        void ok();
        void cancel();

    }
}
