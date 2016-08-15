package com.example.gongzhiyao.safemanagerofmobile.Contact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gongzhiyao.safemanagerofmobile.R;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by 宫智耀 on 2016/7/13.
 */
public class ContactAdapter extends BaseAdapter {

    List<ContactInfo> contactInfos;
    private int layout_id;
    private Context context;
    LayoutInflater inflater;


    public ContactAdapter(Context context, List<ContactInfo> list, int layout_id) {
        this.context = context;
        this.contactInfos = list;
        this.layout_id = layout_id;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return contactInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_contact_select, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_names);
            holder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(contactInfos.get(position).name);
        holder.tv_phone.setText(contactInfos.get(position).phone);
        return convertView;
    }

    class ViewHolder {
        TextView tv_name;
        TextView tv_phone;
    }

}
