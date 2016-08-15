package com.example.gongzhiyao.safemanagerofmobile.Adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gongzhiyao.safemanagerofmobile.CacheClear.CacheInfo;
import com.example.gongzhiyao.safemanagerofmobile.R;

import java.util.List;

/**
 * Created by 宫智耀 on 2016/8/11.
 */
public class CacheCleanAdapter extends BaseAdapter {
    private Context context;
    private List<CacheInfo> cacheInfos;

    public CacheCleanAdapter(Context context, List<CacheInfo> cacheInfos) {
        super();
        this.context = context;
        this.cacheInfos = cacheInfos;
    }


    @Override
    public int getCount() {
        return cacheInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return cacheInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_cacheclean_list, null);
            holder = new ViewHolder();
            holder.mAppIconImgv = (ImageView) convertView.findViewById(R.id.imgv_appicon_cacheclean);
            holder.mAppNameTV = (TextView) convertView.findViewById(R.id.tv_appname_cacheclean);
            holder.mCacheSizeTV = (TextView) convertView.findViewById(R.id.tv_appsize_cacheclean);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }


        CacheInfo cacheInfo = cacheInfos.get(position);
        holder.mAppIconImgv.setImageDrawable(cacheInfo.appIcon);
        holder.mCacheSizeTV.setText(Formatter.formatFileSize(context, cacheInfo.cacheSize));
        holder.mAppNameTV.setText(cacheInfo.appName);

        return convertView;
    }


    private class ViewHolder {
        ImageView mAppIconImgv;
        TextView mAppNameTV;
        TextView mCacheSizeTV;
    }
}
