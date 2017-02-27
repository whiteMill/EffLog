package com.stk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stk.efflog.R;

import java.util.List;

public class SpinerAdapter extends BaseAdapter {

    public static interface IOnItemSelectListener{
        public void onItemClick(int pos);
    }

    private List<String> mObjects;

    private LayoutInflater mInflater;

    public SpinerAdapter(Context context, List<String> mObjects){
        this.mObjects = mObjects;
        mInflater = LayoutInflater.from(context);
    }

    public void refreshData(List<String> objects, int selIndex){
        mObjects = objects;
        if (selIndex < 0){
            selIndex = 0;
        }
        if (selIndex >= mObjects.size()){
            selIndex = mObjects.size() - 1;
        }
    }

    @Override
    public int getCount() {
        return mObjects.size();
    }

    @Override
    public Object getItem(int pos) {
        return mObjects.get(pos).toString();
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup arg2) {
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.spiner_item_layout, null);
            viewHolder.timeTextView = (TextView) convertView.findViewById(R.id.timeTextView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.timeTextView.setText(mObjects.get(pos));

        return convertView;
    }


    public static class ViewHolder
    {
        public TextView timeTextView;
    }

}
