package com.stk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.stk.efflog.R;
import com.stk.model.ProUpdateVo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/1/17.
 */

public class ProUpdateAdapter extends BaseAdapter {
    private List<ProUpdateVo> proUpdateVoList;
    private Context context;
    private Map<Integer,Boolean> isSelect;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public ProUpdateAdapter(List<ProUpdateVo> proUpdateVoList, Context context) {
        this.proUpdateVoList = proUpdateVoList;
        this.context = context;
        isSelect = new HashMap<>();
        initDate();
    }


    public Map<Integer, Boolean> getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(Map<Integer, Boolean> isSelect) {
        this.isSelect = isSelect;
    }

    private void initDate() {
        for (int i = 0; i < proUpdateVoList.size(); i++) {
            isSelect.put(i,false);
        }
    }

    @Override
    public int getCount() {
        return proUpdateVoList.size();
    }

    @Override
    public Object getItem(int position) {
        return proUpdateVoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder myViewHolder = null;
        if(convertView  ==null){
            myViewHolder = new MyViewHolder();
            convertView  = LayoutInflater.from(context).inflate(R.layout.pro_update_layout,null);
            myViewHolder.pro_mess = (TextView) convertView.findViewById(R.id.pro_mess);
            myViewHolder.pro_name = (TextView) convertView.findViewById(R.id.pro_name);
            myViewHolder.isComplete = (CheckBox) convertView.findViewById(R.id.isComplete);
            myViewHolder.end_time = (TextView) convertView.findViewById(R.id.end_time);
            convertView.setTag(myViewHolder);
        }
        myViewHolder = (MyViewHolder) convertView.getTag();
        myViewHolder.pro_mess.setText(proUpdateVoList.get(position).getITEM_MESS());
        myViewHolder.pro_name.setText(proUpdateVoList.get(position).getPROJECT_NAME());

        String item_end_time = proUpdateVoList.get(position).getITEM_END_TIME();

        try {
            if(timeFormat.parse(item_end_time).before(new Date())){
                myViewHolder.end_time.setTextColor(context.getResources().getColor(R.color.red));
            }else{
                myViewHolder.end_time.setTextColor(context.getResources().getColor(R.color.textdefault));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        myViewHolder.end_time.setText("截止 "+item_end_time.substring(5));
        myViewHolder.isComplete.setChecked(isSelect.get(position));

        myViewHolder.isComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    isSelect.put(position,true);
                }else{
                    isSelect.put(position,false);
                }
            }
        });
        return convertView;
    }

    private class MyViewHolder{
        private TextView pro_mess;
        private TextView pro_name;
        private CheckBox isComplete;
        private TextView end_time;

    }
}
