package com.stk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stk.efflog.R;
import com.stk.model.CommitLogVo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by admin on 2016/12/29.
 */

public class FreeLogAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CommitLogVo> logVoArrayList;

    public FreeLogAdapter(Context context, ArrayList<CommitLogVo> logVoArrayList) {
        this.context = context;
        this.logVoArrayList = logVoArrayList;
    }

    @Override
    public int getCount() {
        return logVoArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return logVoArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder myViewHolder = null;
        if(convertView==null){
            myViewHolder = new MyViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.free_log_adapter,null);
            myViewHolder.log_content = (TextView) convertView.findViewById(R.id.log_content);
            myViewHolder.log_level = (TextView) convertView.findViewById(R.id.log_order);
            myViewHolder.time  = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(myViewHolder);
        }
        myViewHolder = (MyViewHolder) convertView.getTag();
        myViewHolder.log_content.setText(logVoArrayList.get(position).getLOG_CONTENT());
        myViewHolder.log_level.setText(logVoArrayList.get(position).getLOG_LEVEL());

        String start =  logVoArrayList.get(position).getBEGIN_TIME();
        String end  = logVoArrayList.get(position).getEND_TIME();
        if(isYesterday(logVoArrayList.get(position).getBEGIN_TIME())==0){
            start = start.substring(start.length()-5,start.length());
        }else{
            start = start.substring(5);
        }

        if(isYesterday(logVoArrayList.get(position).getEND_TIME())==0){
            end = end.substring(end.length()-5,end.length());
        }else{
            end = end.substring(5);
        }

        //myViewHolder.time.setText("起： "+start+"         止： "+end);
        myViewHolder.time.setText("起： "+start);
        return convertView;
    }

    private class MyViewHolder{
      private TextView log_level;
       private TextView log_content;
        private TextView time;
    }

    public int isYesterday(String date){
        int day =0;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d1  = new Date();//当前时间
            Date d2 = sdf.parse(date);//传进的时间
            long cha = d2.getTime() - d1.getTime();
            day = new Long(cha/(1000*60*60*24)).intValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day;
    }
}
