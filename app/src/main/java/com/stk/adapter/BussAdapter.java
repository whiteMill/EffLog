package com.stk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stk.efflog.R;
import com.stk.model.BussinessVo;

import java.util.List;

import static com.stk.efflog.R.id.buss_name;

/**
 * Created by admin on 2017/1/10.
 */

public class BussAdapter extends BaseAdapter {

    private List<BussinessVo> bussinessVoList;
    private Context context;

    public BussAdapter(List<BussinessVo> bussinessVoList, Context context) {
        this.bussinessVoList = bussinessVoList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return bussinessVoList.size();
    }

    @Override
    public Object getItem(int position) {
        return bussinessVoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder myViewHolder = null;
        if(convertView == null){
            myViewHolder = new MyViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.buss_adapter_layout,null);
            myViewHolder.buss_name = (TextView) convertView.findViewById(buss_name);
            myViewHolder.buss_time = (TextView) convertView.findViewById(R.id.buss_time);
            convertView.setTag(myViewHolder);
        }
        myViewHolder = (MyViewHolder) convertView.getTag();
        myViewHolder.buss_name.setText(bussinessVoList.get(position).getBUSS_NAME());
        myViewHolder.buss_time.setText("创建于 "+bussinessVoList.get(position).getBUSS_TIME());
        return convertView;
    }

    private class MyViewHolder{
       private TextView buss_name;
       private TextView buss_time;

    }
}
