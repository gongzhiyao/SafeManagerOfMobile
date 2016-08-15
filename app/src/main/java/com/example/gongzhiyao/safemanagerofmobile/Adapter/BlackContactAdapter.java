package com.example.gongzhiyao.safemanagerofmobile.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gongzhiyao.safemanagerofmobile.BlackContact.BlackContactInfo;
import com.example.gongzhiyao.safemanagerofmobile.BlackContact.BlackNumberDBOperation;
import com.example.gongzhiyao.safemanagerofmobile.R;

import java.util.List;

/**
 * Created by 宫智耀 on 2016/7/18.
 */
public class BlackContactAdapter extends BaseAdapter {
    private Context context;
    private List<BlackContactInfo> contactInfos;
    private BlackNumberDBOperation operation;
    private BlackContactCallBack callBack;

    public void setCallBack(BlackContactCallBack callBack) {
        this.callBack = callBack;
    }

    public BlackContactAdapter(List<BlackContactInfo> systemContact, Context context) {
        super();
        this.contactInfos = systemContact;
        this.context = context;
        operation = new BlackNumberDBOperation(context);
    }

    @Override
    public int getCount() {
        return contactInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return contactInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_blackcontact, null);
            holder = new ViewHolder();
            holder.mNameTV = (TextView) convertView.findViewById(R.id.tv_black_name);
            holder.mModeTV = (TextView) convertView.findViewById(R.id.tv_black_mode);
            holder.mContactImgv = (ImageView) convertView.findViewById(R.id.view_black_icon);
            holder.mbtn_Delete = (ImageButton) convertView.findViewById(R.id.btn_black_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mNameTV.setText(contactInfos.get(position).ContactName + "(" + contactInfos.get(position).PhoneNumber + ")");
        holder.mModeTV.setText(contactInfos.get(position).getModeString(contactInfos.get(position).mode));
        holder.mNameTV.setTextColor(context.getResources().getColor(R.color.bright_purple));
        holder.mModeTV.setTextColor(context.getResources().getColor(R.color.bright_purple));
        holder.mContactImgv.setBackgroundResource(R.drawable.brightpurple_contact_icon);
        holder.mbtn_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean delete = operation.delete(contactInfos.get(position));
                if (delete) {
                    contactInfos.remove(contactInfos.get(position));
                    BlackContactAdapter.this.notifyDataSetChanged();
                    /**
                     * 如果库中没有数据，则执行回调函数
                     * 因为没有数据了，界面要改变，有些view需要隐藏
                     * 所以通过callback 回调
                     */
                    if (operation.getTotalNum() == 0) {
                        callBack.DataSizeChanged();
                    } else {
                        Toast.makeText(context, "删除失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return convertView;
    }


    public class ViewHolder {
        TextView mNameTV;
        TextView mModeTV;
        ImageView mContactImgv;
        ImageButton mbtn_Delete;
    }


    public interface BlackContactCallBack {
        void DataSizeChanged();
    }
}
