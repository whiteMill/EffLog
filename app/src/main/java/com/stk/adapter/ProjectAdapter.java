package com.stk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stk.efflog.R;
import com.stk.model.ProjectVo;

import java.util.List;

import static com.stk.efflog.R.id.buss_name;

/**
 * Created by admin on 2017/1/16.
 */

public class ProjectAdapter extends BaseAdapter {
    private List<ProjectVo> projectVoVoList;
    private Context context;

    public ProjectAdapter(List<ProjectVo> projectVoVoList, Context context) {
        this.projectVoVoList = projectVoVoList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return projectVoVoList.size();
    }

    @Override
    public Object getItem(int position) {
        return projectVoVoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProjectAdapter.MyViewHolder myViewHolder = null;
        if(convertView == null){
            myViewHolder = new ProjectAdapter.MyViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.pro_adapter_layout,null);
            myViewHolder.buss_name = (TextView) convertView.findViewById(buss_name);
            myViewHolder.buss_time = (TextView) convertView.findViewById(R.id.buss_time);
            convertView.setTag(myViewHolder);
        }
        myViewHolder = (ProjectAdapter.MyViewHolder) convertView.getTag();
        myViewHolder.buss_name.setText(projectVoVoList.get(position).getPROJECT_NAME());
        myViewHolder.buss_time.setText("创建于 "+projectVoVoList.get(position).getPROJECT_TIME());
        return convertView;
    }

    private class MyViewHolder{
        private TextView buss_name;
        private TextView buss_time;

    }
}
