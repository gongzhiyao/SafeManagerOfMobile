package com.example.gongzhiyao.safemanagerofmobile.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.gongzhiyao.safemanagerofmobile.ProgressManager.TaskInfo;
import com.example.gongzhiyao.safemanagerofmobile.R;
import com.example.gongzhiyao.safemanagerofmobile.Util.DensityUtil;

import java.util.List;

/**
 * Created by 宫智耀 on 2016/8/11.
 */
public class ProcessManagerAdapter extends BaseAdapter {

    private Context context;
    private List<TaskInfo> mUsertaskInfos;
    private List<TaskInfo> mSystaskInfos;
    private SharedPreferences mSp;

    public ProcessManagerAdapter(Context context, List<TaskInfo> userTaskInfos, List<TaskInfo> sysTaskInfos) {
        super();
        this.context = context;
        this.mUsertaskInfos = userTaskInfos;
        this.mSystaskInfos = sysTaskInfos;
        mSp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
    }


    @Override
    public int getCount() {
        if (mSystaskInfos.size() > 0 & mSp.getBoolean("showSystemProcess", true)) {
            return mUsertaskInfos.size() + mSystaskInfos.size() + 2;
        } else {
            return mUsertaskInfos.size() + 1;
        }
    }

    @Override
    public Object getItem(int position) {
        if (position == 0 || position == mUsertaskInfos.size() + 1) {
            return null;
        } else if (position <= mUsertaskInfos.size()) {
            return mUsertaskInfos.get(position - 1);
        } else {
            return mSystaskInfos.get(position - mUsertaskInfos.size() - 2);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == 0) {
            TextView tv = getTextView();
            tv.setText("用户进程：" + mUsertaskInfos.size() + "个");
            return tv;
        } else if (position == mUsertaskInfos.size() + 1) {
            TextView tv = getTextView();
            if (mSystaskInfos.size() > 0) {
                tv.setText("系统进程：" + mSystaskInfos.size() + "个");
                return tv;
            }
        }

        TaskInfo info = null;
        if (position <= mUsertaskInfos.size()) {
            info = mUsertaskInfos.get(position - 1);
        } else if (mSystaskInfos.size() > 0) {
            info = mSystaskInfos.get(position - mUsertaskInfos.size() - 2);
        }

        ViewHolder holder = null;
        if (convertView != null && convertView instanceof RelativeLayout) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = View.inflate(context, R.layout.item_progressmanager_list, null);
            holder = new ViewHolder();
            holder.mAppIconImgv = (ImageView) convertView.findViewById(R.id.imgv_appicon_progressmana);
            holder.mAppNameTV = (TextView) convertView.findViewById(R.id.tv_appname_processmana);
            holder.mAppMemoryTV = (TextView) convertView.findViewById(R.id.tv_appmemory_progressmana);
            holder.mCheckBox = (CheckBox) convertView.findViewById(R.id.checkbox_process);
            convertView.setTag(holder);
        }


        if (info != null) {
            holder.mAppNameTV.setText(info.appName);
            holder.mAppMemoryTV.setText("占用内存：" + Formatter.formatFileSize(context, info.appMemory));
            holder.mAppIconImgv.setImageDrawable(info.appIcon);
            if (info.packageName.equals(context.getPackageName())) {
                holder.mCheckBox.setVisibility(View.GONE);
            } else {
                holder.mCheckBox.setVisibility(View.VISIBLE);
            }
            holder.mCheckBox.setChecked(info.isChecked);
        }


        return convertView;
    }


    private TextView getTextView() {
        TextView tv = new TextView(context);
        tv.setBackgroundColor(context.getResources().getColor(R.color.graye5));
        tv.setPadding(DensityUtil.dip2px(context, 5), DensityUtil.dip2px(context, 5), DensityUtil.dip2px(context, 5), DensityUtil.dip2px(context, 5));
        tv.setTextColor(context.getResources().getColor(R.color.black));
        return tv;
    }


    static class ViewHolder {
        ImageView mAppIconImgv;
        TextView mAppNameTV;
        TextView mAppMemoryTV;
        CheckBox mCheckBox;
    }
}
