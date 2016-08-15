package com.example.gongzhiyao.safemanagerofmobile.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gongzhiyao.safemanagerofmobile.AntiVirus.ScanAppInfo;
import com.example.gongzhiyao.safemanagerofmobile.R;

import java.util.List;

/**
 * Created by 宫智耀 on 2016/8/10.
 */
public class ScanVirusAdapter extends BaseAdapter {

    private List<ScanAppInfo> mScanAppInfos;
    private Context context;

    public ScanVirusAdapter(List<ScanAppInfo> scanAppInfos, Context context) {
        this.context = context;
        mScanAppInfos = scanAppInfos;
    }

    @Override
    public int getCount() {
        return mScanAppInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mScanAppInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_applock, null);
            holder = new ViewHolder();
            holder.mAppiconImgv = (ImageView) convertView.findViewById(R.id.imgv_appicon);
            holder.mAppNameTV = (TextView) convertView.findViewById(R.id.tv_appname);
            holder.mScanIconImgv = (ImageView) convertView.findViewById(R.id.imgv_lock);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ScanAppInfo scanAppInfo = mScanAppInfos.get(position);
        if (!scanAppInfo.isVirus) {
            holder.mScanIconImgv.setBackgroundResource(R.drawable.blue_right_icon);
            holder.mAppNameTV.setText(scanAppInfo.appName);
        } else {
            holder.mAppNameTV.setTextColor(context.getResources().getColor(R.color.bright_red));
            holder.mAppNameTV.setText(scanAppInfo.appName + "(" + scanAppInfo.description + ")");
        }
        holder.mAppiconImgv.setImageDrawable(scanAppInfo.appicon);

        return convertView;
    }

    private class ViewHolder {
        ImageView mAppiconImgv;
        TextView mAppNameTV;
        ImageView mScanIconImgv;
    }
}
