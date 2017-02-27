package com.stk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stk.efflog.R;
import com.stk.model.LogVo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by wangl on 2016/12/29.
 */

public class WorkItemAdapter extends BaseAdapter{

    private Context context;
    private List<LogVo> logVoList;

    public WorkItemAdapter(Context context, List<LogVo> logVoList) {
        this.context = context;
        this.logVoList = logVoList;
    }

    @Override
    public int getCount() {
        return logVoList.size();
    }

    @Override
    public Object getItem(int position) {
        return logVoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder myViewHolder = null;
        if(convertView ==null){
            myViewHolder = new MyViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.log_item_adapter,null);
            myViewHolder.log_order = (TextView) convertView.findViewById(R.id.log_order);
            myViewHolder.log_content = (TextView) convertView.findViewById(R.id.log_content);
            myViewHolder.time = (TextView) convertView.findViewById(R.id.time);
            myViewHolder.log_mind_text = (TextView) convertView.findViewById(R.id.log_mind_text);
            convertView.setTag(myViewHolder);
        }
        myViewHolder = (MyViewHolder) convertView.getTag();
        myViewHolder.log_order.setText("("+(position+1)+") "+logVoList.get(position).getLOG_LEVEL());
        myViewHolder.log_content.setText(logVoList.get(position).getLOG_CONTENT());

        if(logVoList.get(position).getLOG_MIND().equals("null")){
            myViewHolder.log_mind_text.setVisibility(View.GONE);
        }else{
            myViewHolder.log_mind_text.setVisibility(View.VISIBLE);
            myViewHolder.log_mind_text.setText(logVoList.get(position).getLOG_MIND());
        }

        String start =  logVoList.get(position).getBEGIN_TIME();
        String end  = logVoList.get(position).getEND_TIME();
        if(isYesterday(logVoList.get(position).getBEGIN_TIME())==0){
            start = start.substring(start.length()-5,start.length());
        }else{
            start = start.substring(5);
        }

        if(isYesterday(logVoList.get(position).getEND_TIME())==0){
            end = end.substring(end.length()-5,end.length());
        }else{
            end = end.substring(5);
        }

        myViewHolder.time.setText("起： "+start+"         止： "+end);


        return convertView;
    }

    private class MyViewHolder{
      private TextView log_order;
      private TextView log_content;
      private TextView time;
      private TextView log_mind_text;
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
