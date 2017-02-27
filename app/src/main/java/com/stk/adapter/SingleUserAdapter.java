package com.stk.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stk.efflog.R;
import com.stk.model.ManPeoContact;
import com.stk.utils.CharacterUtils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by wangl on 2017/1/12.
 */

public class SingleUserAdapter extends BaseAdapter {
    private Context context;
    private List<ManPeoContact> manaPeoList;
    private HashMap<Integer,Boolean> isSelected = null;

    public SingleUserAdapter(Context context, List<ManPeoContact> manaPeoList) {
        this.context = context;
        this.manaPeoList = manaPeoList;
        isSelected = new HashMap<>();
        initCheckBox();
    }

    private void initCheckBox() {
        for (int i = 0; i < manaPeoList.size(); i++) {
            if(manaPeoList.get(i).getChecked()){
                isSelected.put(i,true);
            }else{
                isSelected.put(i,false);
            }
        }
    }

    @Override
    public int getCount() {
        return manaPeoList.size();
    }

    @Override
    public Object getItem(int position) {
        return manaPeoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new MyViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.contact_item_adapter, null);
            viewHolder.tv_firstCharacter = (TextView) convertView.findViewById(R.id.tv_firstCharacter);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.isChecked  = (CheckBox) convertView.findViewById(R.id.isChecked);
            viewHolder.ch_layout = (LinearLayout) convertView.findViewById(R.id.ch_layout);
            convertView.setTag(viewHolder);
        }
        viewHolder = (MyViewHolder) convertView.getTag();
        viewHolder.isChecked.setChecked(isSelected.get(position));

        if (position == 0) {
            viewHolder.tv_firstCharacter.setText(manaPeoList.get(position).getFirstCharacter());
            viewHolder.tv_name.setText(manaPeoList.get(position).getNAME());
        } else {
            if (CharacterUtils.getCnAscii(manaPeoList.get(position).getFirstCharacter().charAt(0)) > CharacterUtils.getCnAscii(manaPeoList.get(position - 1).getFirstCharacter().charAt(0))) {
                viewHolder.tv_firstCharacter.setVisibility(View.VISIBLE);
                viewHolder.tv_firstCharacter.setText(manaPeoList.get(position).getFirstCharacter());
                viewHolder.tv_name.setText(manaPeoList.get(position).getNAME());
            } else {
                viewHolder.tv_firstCharacter.setVisibility(View.GONE);
                viewHolder.tv_name.setText(manaPeoList.get(position).getNAME());
            }
        }

       /* final MyViewHolder finalViewHolder = viewHolder;
        viewHolder.ch_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ViewHolder viewHolder1 = (ViewHolder) v.getTag();
                if(finalViewHolder.isChecked.isChecked()){
                    finalViewHolder.isChecked.setChecked(false);
                    isSelected.put(position,false);
                }else{
                    finalViewHolder.isChecked.setChecked(true);
                    isSelected.put(position,true);
                }
            }
        });
*/
        return convertView;
    }

    private class MyViewHolder{
        private TextView tv_firstCharacter;
        private TextView tv_name;
        private CheckBox isChecked;
        private LinearLayout ch_layout;

    }
    //通过字符查找位置
    public int getSelection(String s){
        Log.d("CCC",s);
        for(int i=0;i<getCount();i++){
            String firStr = manaPeoList.get(i).getFirstCharacter();
            Log.d("CCC",firStr+"字母");
            if(firStr.equals(s)){
                return i;
            }
        }
        return -1;
    }
}
