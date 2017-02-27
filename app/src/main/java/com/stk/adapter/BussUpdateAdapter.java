package com.stk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.stk.efflog.R;
import com.stk.model.BussUpdateVo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/1/18.
 */

public class BussUpdateAdapter extends BaseAdapter{

    private Context context;
    private List<BussUpdateVo> bussUpdateVoList;
    private Map<Integer,Boolean> isSelect;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public BussUpdateAdapter(Context context, List<BussUpdateVo> bussUpdateVoList) {
        this.context = context;
        this.bussUpdateVoList = bussUpdateVoList;
        isSelect = new HashMap<>();
        initDate();
    }

    private void initDate() {
        for (int i = 0; i < bussUpdateVoList.size(); i++) {
            isSelect.put(i,false);
        }
    }

    public Map<Integer, Boolean> getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(Map<Integer, Boolean> isSelect) {
        this.isSelect = isSelect;
    }

    @Override
    public int getCount() {
        return bussUpdateVoList.size();
    }

    @Override
    public Object getItem(int position) {
        return bussUpdateVoList.get(position);
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
        myViewHolder.pro_mess.setText(bussUpdateVoList.get(position).getBUSS_MESS());
        myViewHolder.pro_name.setText(bussUpdateVoList.get(position).getBUSS_NAME());
        String item_end_time = bussUpdateVoList.get(position).getINFO_END_TIME();

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
