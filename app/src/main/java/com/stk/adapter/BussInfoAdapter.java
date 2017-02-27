package com.stk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stk.efflog.R;
import com.stk.model.BussInfoVo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/1/12.
 */

public class BussInfoAdapter  extends BaseAdapter{

    private Context context;
    private List<BussInfoVo> bussInfoVoList;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public BussInfoAdapter(Context context, List<BussInfoVo> bussInfoVoList) {
        this.context = context;
        this.bussInfoVoList = bussInfoVoList;
    }

    @Override
    public int getCount() {
        return bussInfoVoList.size();
    }

    @Override
    public Object getItem(int position) {
        return bussInfoVoList.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.buss_info_layout,null);
            myViewHolder.buss_content = (TextView) convertView.findViewById(R.id.buss_content);
            myViewHolder.end_time = (TextView) convertView.findViewById(R.id.end_time);
            myViewHolder.begin_time = (TextView) convertView.findViewById(R.id.begin_time);
            myViewHolder.buss_name = (TextView) convertView.findViewById(R.id.buss_name);
            convertView.setTag(myViewHolder);
        }
        myViewHolder = (MyViewHolder) convertView.getTag();
        myViewHolder.buss_content.setText(bussInfoVoList.get(position).getBUSS_MESS());



        String item_end_time = bussInfoVoList.get(position).getINFO_END_TIME();
        String item_begin_time  = bussInfoVoList.get(position).getINFO_BEGIN_TIME();

        try {
            if(timeFormat.parse(item_end_time).before(new Date())){
                 myViewHolder.end_time.setVisibility(View.VISIBLE);
            }else{
                myViewHolder.end_time.setVisibility(View.INVISIBLE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        myViewHolder.end_time.setText("截止 "+item_end_time.substring(5));
        myViewHolder.begin_time.setText(item_begin_time.substring(5));
        myViewHolder.buss_name.setText(bussInfoVoList.get(position).getNAME());

        return convertView;
    }

    private  class MyViewHolder{
        private TextView  buss_content;
        private TextView  end_time;
        private TextView  begin_time;
        private TextView  buss_name;
    }
}
