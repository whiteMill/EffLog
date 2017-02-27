package com.stk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stk.efflog.R;
import com.stk.model.BussPushVo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/1/20.
 */

public class BussPushAdapter extends BaseAdapter {

    private Context context;
    private List<BussPushVo> bussPushVoList;

    public BussPushAdapter(Context context, List<BussPushVo> bussPushVoList) {
        this.context = context;
        this.bussPushVoList = bussPushVoList;
    }

    @Override
    public int getCount() {
        return bussPushVoList.size();
    }

    @Override
    public Object getItem(int position) {
        return bussPushVoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder myViewHolder = null;
        if(convertView==null){
            myViewHolder  = new MyViewHolder();
            convertView  = LayoutInflater.from(context).inflate(R.layout.remind_layout,null);
            myViewHolder.remind_content = (TextView) convertView.findViewById(R.id.remind_content);
            myViewHolder.remind_time = (TextView) convertView.findViewById(R.id.remind_time);
            convertView.setTag(myViewHolder);
        }
        myViewHolder = (MyViewHolder) convertView.getTag();
        myViewHolder.remind_content.setText(bussPushVoList.get(position).getMESSAGE());
        String logIme = bussPushVoList.get(position).getBUSS_TIME();
        int point  = isYesterday(logIme);
        if(point==0){
            myViewHolder.remind_time.setText("今天 "+logIme.substring(logIme.length()-5,logIme.length()));
        }else if(point==1){
            myViewHolder.remind_time.setText(logIme.substring(5));
        }else{
            myViewHolder.remind_time.setText(logIme.substring(5));
        }
        return convertView;
    }

    private class MyViewHolder{
        private TextView remind_time;
        private TextView remind_content;
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
