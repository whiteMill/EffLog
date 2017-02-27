package com.stk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stk.efflog.R;

import java.util.List;

/**
 * Created by admin on 2017/2/8.
 */

public class TimeRemindAdapter extends BaseAdapter {

    private List<String> stringList;

    private Context context;

    public TimeRemindAdapter(List<String> stringList, Context context) {
        this.stringList = stringList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return stringList.size();
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
        MyViewHolder myViewHolder = null;
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.time_remind_layout,null);
            myViewHolder = new MyViewHolder();
            myViewHolder.timeTxt = (TextView) convertView.findViewById(R.id.timeTxt);
            convertView.setTag(myViewHolder);
        }
        myViewHolder = (MyViewHolder) convertView.getTag();
        myViewHolder.timeTxt.setText(stringList.get(position));
        return convertView;
    }

    private class MyViewHolder{
      private TextView timeTxt;
    }
}
