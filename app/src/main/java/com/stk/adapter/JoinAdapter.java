package com.stk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stk.efflog.R;
import com.stk.model.ContactUser;

import java.util.List;

/**
 * Created by admin on 2017/1/5.
 */

public class JoinAdapter extends BaseAdapter{

    private Context context;
    private List<ContactUser> contactUsers;

    public JoinAdapter(Context context, List<ContactUser> contactUsers) {
        this.context = context;
        this.contactUsers = contactUsers;
    }

    @Override
    public int getCount() {
        return contactUsers.size();
    }

    @Override
    public Object getItem(int position) {
        return contactUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder =null;
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.join_adapter,null);
            viewHolder.join_name = (TextView) convertView.findViewById(R.id.join_name);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();

            viewHolder.join_name.setText((position+2)+"---"+contactUsers.get(position).getUsername());

        return convertView;
    }


    private class ViewHolder{
        private TextView join_name;


    }
}
