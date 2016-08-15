package com.example.gongzhiyao.safemanagerofmobile.Contact_select;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gongzhiyao.safemanagerofmobile.Contact.ContactAdapter;
import com.example.gongzhiyao.safemanagerofmobile.Contact.ContactInfo;
import com.example.gongzhiyao.safemanagerofmobile.Contact.ContactInfoParser;
import com.example.gongzhiyao.safemanagerofmobile.Log.L;
import com.example.gongzhiyao.safemanagerofmobile.R;

import java.util.List;

public class Contact_Select extends AppCompatActivity implements View.OnClickListener {

    private ListView mListView;
    private ContactAdapter adapter;
    private List<ContactInfo> contactInfos;
    private TextView tv_Title;
    private ImageView mleftImgv;
    private L log;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_contact__select);
        initView();
        new getContact().execute();
        log=new L();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String phone = contactInfos.get(position).phone;
                String name=contactInfos.get(position).name;
                String idd=contactInfos.get(position).id;
                log.d("点击的是"+phone+"                       "+name+"                          "+idd);
                Intent i = new Intent();
                i.putExtra("phone", phone);
                setResult(0, i);
                finish();

            }
        });


    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.lv_contact);
        tv_Title = (TextView) findViewById(R.id.tv_title);
        mleftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        tv_Title.setText("选择联系人");
        mleftImgv.setOnClickListener(this);
        mleftImgv.setImageResource(R.drawable.back);
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.purple));


    }


    private class getContact extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            contactInfos = ContactInfoParser.getSystemContact(getApplicationContext());
//            contactInfos.addAll()

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new ContactAdapter(getApplicationContext(), contactInfos, R.layout.item_list_contact_select);
            mListView.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.imgv_leftbtn:
                finish();
                break;
        }
    }
}
