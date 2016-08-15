package com.example.gongzhiyao.safemanagerofmobile.Adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gongzhiyao.safemanagerofmobile.AppInfo.AppInfo;
import com.example.gongzhiyao.safemanagerofmobile.Contact.ContactInfo;
import com.example.gongzhiyao.safemanagerofmobile.R;
import com.example.gongzhiyao.safemanagerofmobile.Util.DensityUtil;
import com.example.gongzhiyao.safemanagerofmobile.Util.EngineUtils;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by 宫智耀 on 2016/7/20.
 */
public class AppManagerAdapter extends BaseAdapter {
    private List<AppInfo> userAppInfos;
    private List<AppInfo> systemAppInfos;
    private Context context;

    public AppManagerAdapter(Context context, List<AppInfo> userAppInfos, List<AppInfo> systemAppInfos) {
        super();
        this.context = context;
        this.userAppInfos = userAppInfos;
        this.systemAppInfos = systemAppInfos;
    }

    @Override
    public int getCount() {
        return userAppInfos.size() + systemAppInfos.size();
    }

    @Override
    public Object getItem(int position) {

        AppInfo appInfo;
        if (position < (userAppInfos.size())) {
            appInfo = userAppInfos.get(position);
        } else {
            int location = position - userAppInfos.size();
            appInfo = systemAppInfos.get(location);
        }
        return appInfo;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        AppInfo appInfo;
        if (position < (userAppInfos.size())) {
            //position为0,显示textView
            appInfo = userAppInfos.get(position);
        } else {
            //系统应用
            appInfo = systemAppInfos.get(position - userAppInfos.size());
        }
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_appmanager_list, null);
            holder.mAppIconImgv = (ImageView) convertView.findViewById(R.id.imgv_appicon);
            holder.mApplocationTv = (TextView) convertView.findViewById(R.id.tv_appisroom);
            holder.mAppSizeTv = (TextView) convertView.findViewById(R.id.tv_appsize);
            holder.mAppNameTv = (TextView) convertView.findViewById(R.id.tv_appname);
            holder.mlaunchAppTv = (TextView) convertView.findViewById(R.id.tv_launch_app);
            holder.mSettingAppTv = (TextView) convertView.findViewById(R.id.tv_setting_app);
            holder.mShareAppTv = (TextView) convertView.findViewById(R.id.tv_share_app);
            holder.muninstallTv = (TextView) convertView.findViewById(R.id.tv_uninstall_app);
            holder.mAppOptionLL = (LinearLayout) convertView.findViewById(R.id.ll_option_app);
            convertView.setTag(holder);

        } else if (convertView != null & convertView instanceof LinearLayout) {
            holder = (ViewHolder) convertView.getTag();
        }
        if (appInfo != null) {

            if (holder == null) {
                System.out.println("holder is null!!!!!!!!!!!!!!!!!!");
            } else {
                holder.mApplocationTv.setText(appInfo.getAppLocation(appInfo.isInRoom));
                holder.mAppIconImgv.setImageDrawable(appInfo.icon);
                holder.mAppSizeTv.setText(Formatter.formatFileSize(context, appInfo.appSize));
                holder.mAppNameTv.setText(appInfo.appName);
                if (appInfo.isSelected) {
                    holder.mAppOptionLL.setVisibility(View.VISIBLE);
                } else {
                    holder.mAppOptionLL.setVisibility(View.GONE);
                }

            }
        }

        MyClickListener clickListener = new MyClickListener(appInfo);
        if (holder != null) {
            holder.mlaunchAppTv.setOnClickListener(clickListener);
            holder.mShareAppTv.setOnClickListener(clickListener);
            holder.mSettingAppTv.setOnClickListener(clickListener);
            holder.muninstallTv.setOnClickListener(clickListener);
        }


        return convertView;
    }





    static class ViewHolder {
        //启动app
        TextView mlaunchAppTv;
        TextView muninstallTv;
        TextView mShareAppTv;
        TextView mSettingAppTv;
        ImageView mAppIconImgv;
        TextView mApplocationTv;
        TextView mAppSizeTv;
        TextView mAppNameTv;
        LinearLayout mAppOptionLL;


    }


    class MyClickListener implements View.OnClickListener {

        private AppInfo appInfo;
        public MyClickListener(AppInfo appInfo) {
            this.appInfo = appInfo;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_launch_app:
                    EngineUtils.startApp(context, appInfo);
                    break;
                case R.id.tv_share_app:
                    EngineUtils.shareApp(context, appInfo);
                    break;
                case R.id.tv_setting_app:
                    EngineUtils.settingAppDetail(context, appInfo);
                    break;
                case R.id.tv_uninstall_app:
                    if (appInfo.packageName.equals(context.getPackageName())) {
                        Toast.makeText(context, "您没有权限卸载此软件", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    EngineUtils.uninstallApp(context, appInfo);
                    break;
            }
        }
    }
}
