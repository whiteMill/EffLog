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
import com.stk.model.ContactUser;
import com.stk.utils.CharacterUtils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by wl on 2016/10/13.
 */
public class UserAdapter extends BaseAdapter {

    private Context context;
    private List<ContactUser> contactUsersusers;
    private HashMap<Integer,Boolean> isSelected = null;


    public UserAdapter(Context context, List<ContactUser> contactUsersusers) {
        this.context = context;
        this.contactUsersusers = contactUsersusers;
        isSelected = new HashMap<>();
        initCheckBox();
    }

    private void initCheckBox() {
        for (int i = 0; i < contactUsersusers.size(); i++) {
            if(contactUsersusers.get(i).getCheck()){
                isSelected.put(i,true);
            }else{
                isSelected.put(i,false);
            }
        }
    }

    public HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public int getCount() {
        return contactUsersusers.size();
    }

    @Override
    public Object getItem(int position) {
        return contactUsersusers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.contact_item_adapter, null);
            viewHolder.tv_firstCharacter = (TextView) convertView.findViewById(R.id.tv_firstCharacter);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.isChecked  = (CheckBox) convertView.findViewById(R.id.isChecked);
            viewHolder.ch_layout = (LinearLayout) convertView.findViewById(R.id.ch_layout);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();

         viewHolder.isChecked.setChecked(isSelected.get(position));

        if (position == 0) {
            viewHolder.tv_firstCharacter.setText(contactUsersusers.get(position).getFirstCharacter());
            viewHolder.tv_name.setText(contactUsersusers.get(position).getUsername());
        } else {
            if (CharacterUtils.getCnAscii(contactUsersusers.get(position).getFirstCharacter().charAt(0)) > CharacterUtils.getCnAscii(contactUsersusers.get(position - 1).getFirstCharacter().charAt(0))) {
                viewHolder.tv_firstCharacter.setVisibility(View.VISIBLE);
                viewHolder.tv_firstCharacter.setText(contactUsersusers.get(position).getFirstCharacter());
                viewHolder.tv_name.setText(contactUsersusers.get(position).getUsername());
            } else {
                viewHolder.tv_firstCharacter.setVisibility(View.GONE);
                viewHolder.tv_name.setText(contactUsersusers.get(position).getUsername());
            }
        }

        final ViewHolder finalViewHolder = viewHolder;
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

        return convertView;
    }

    private class ViewHolder {
        private TextView tv_firstCharacter;
        private TextView tv_name;
        private CheckBox isChecked;
        private LinearLayout ch_layout;
    }

    //通过字符查找位置
    public int getSelection(String s){
        Log.d("CCC",s);
        for(int i=0;i<getCount();i++){
            String firStr = contactUsersusers.get(i).getFirstCharacter();
            Log.d("CCC",firStr+"字母");
            if(firStr.equals(s)){
                return i;
            }
        }
        return -1;
    }
}
