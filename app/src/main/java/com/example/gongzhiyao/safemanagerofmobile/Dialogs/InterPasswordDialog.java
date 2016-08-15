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
 * Created by 宫智耀 on 2016/7/12.
 */
public class InterPasswordDialog extends Dialog implements View.OnClickListener {

    private EditText et_inter_passwd;
    private Button btn_comfirm, btn_dismiss;
    private MyCallBack myCallBack;
    private Context context;

    public InterPasswordDialog(Context context) {
        super(context, R.style.dialog_custom);
        this.context = context;
    }

    public void setMyCallBack(MyCallBack myCallBack) {
        this.myCallBack = myCallBack;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inter_passwd_dialog);
        initView();
    }



    public String getInterPasswd(){
        return et_inter_passwd.getText().toString();
    }
    private void initView() {
        et_inter_passwd = (EditText) findViewById(R.id.et_inter_passwd);
        btn_comfirm = (Button) findViewById(R.id.btn_comfirm);
        btn_dismiss = (Button) findViewById(R.id.btn_dismiss);
        btn_comfirm.setOnClickListener(this);
        btn_dismiss.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_comfirm:
                myCallBack.confirm();
                break;
            case R.id.btn_dismiss:
                myCallBack.cancle();
                break;

        }
    }

    public interface MyCallBack {
        void confirm();

        void cancle();
    }
}
