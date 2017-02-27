package com.stk.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stk.efflog.R;
import com.stk.model.CommentVo;
import com.stk.ui.UserCommentActivity;

import java.util.List;

/**
 * Created by admin on 2016/12/7.
 */

public class CommentAdapter extends BaseAdapter{

    private Context context;
    private List<CommentVo> commentVoArrayList;

    public CommentAdapter(Context context, List<CommentVo> commentVoArrayList) {
        this.context = context;
        this.commentVoArrayList = commentVoArrayList;
    }

    @Override
    public int getCount() {
        return commentVoArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentVoArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
       MyViewHolder myViewHolder = null;
        if(convertView == null){
            myViewHolder  =new MyViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.comment_adaper_layout,null);
            myViewHolder.from_name = (TextView) convertView.findViewById(R.id.from_name);
            myViewHolder.to_name = (TextView) convertView.findViewById(R.id.to_name);
            myViewHolder.comment_time = (TextView) convertView.findViewById(R.id.comment_time);
            myViewHolder.comment_content = (TextView) convertView.findViewById(R.id.comment_content);
            myViewHolder.back_comment = (TextView) convertView.findViewById(R.id.back_comment);
            convertView.setTag(myViewHolder);
        }
        myViewHolder = (MyViewHolder) convertView.getTag();
        myViewHolder.from_name.setText(commentVoArrayList.get(position).getFROM_NAME());
        myViewHolder.to_name.setText("回复 "+commentVoArrayList.get(position).getTO_NAME()+": ");
        myViewHolder.comment_time.setText(commentVoArrayList.get(position).getCOMMENT_TIME());
        myViewHolder.comment_content.setText(commentVoArrayList.get(position).getCOMMENT_CONTENT());
        myViewHolder.back_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context, UserCommentActivity.class);
                intent.putExtra("log_index",commentVoArrayList.get(position).getLOG_INDEX());
                intent.putExtra("to_id",commentVoArrayList.get(position).getFROM_ID());
                intent.putExtra("to_name",commentVoArrayList.get(position).getFROM_NAME());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    private class MyViewHolder{
        private TextView from_name;
        private TextView to_name;
        private TextView comment_time;
        private TextView comment_content;
        private TextView back_comment;
    }
}
