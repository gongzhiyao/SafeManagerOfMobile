package com.example.gongzhiyao.safemanagerofmobile.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gongzhiyao.safemanagerofmobile.R;


/**
 * Created by 宫智耀 on 2016/6/21.
 */
public class HomeAdapter extends BaseAdapter {

    String[] names = {"手机防盗", "通讯卫士", "软件管家", "手机杀毒", "缓存清理", "进程管理", "流量统计", "高级工具", "设置中心"};

    int [] imageId={R.drawable.safe, R.drawable.callmsgsafe,R.drawable.app,R.drawable.trojan,R.drawable.sysoptimize,R.drawable.taskmanager,R.drawable.netmanager,R.drawable.atools,R.drawable.settings};
    Context context;
    int layoutID;
    ImageView iv;
    TextView tv;
    public HomeAdapter(Context context, int layoutID) {
        this.context=context;
        this.layoutID=layoutID;
    }

    @Override
    public int getCount() {
        return 9;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(layoutID,null);
        }else {

        }

        iv= (ImageView) convertView.findViewById(R.id.iv_icon);
        tv= (TextView) convertView.findViewById(R.id.tv_name);
        iv.setImageResource(imageId[position]);
        tv.setText(names[position]);




        return convertView;
    }
}
