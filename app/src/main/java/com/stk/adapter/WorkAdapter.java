package com.stk.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stk.efflog.R;
import com.stk.model.LogVo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by admin on 2016/12/28.
 */

public class WorkAdapter extends BaseAdapter {

    private List<List<LogVo>> lists;
    private Context context;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
    private List<LogVo> outTimeList = new ArrayList<>();
    private List<LogVo> unComList = new ArrayList<>();
    private List<LogVo> comList = new ArrayList<>();

    public WorkAdapter(List<List<LogVo>> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.work_adapter_layout,null);
            myViewHolder.user_name = (TextView) convertView.findViewById(R.id.user_name);
            myViewHolder.log_time  = (TextView) convertView.findViewById(R.id.log_time);
            myViewHolder.log_type = (TextView) convertView.findViewById(R.id.log_type);
            myViewHolder.log_comment = (TextView) convertView.findViewById(R.id.log_comment);
            myViewHolder.log_summary = (TextView) convertView.findViewById(R.id.log_summary);
            myViewHolder.summary_layout = (RelativeLayout) convertView.findViewById(R.id.summary_layout);

            myViewHolder.outTimeLayout = (LinearLayout) convertView.findViewById(R.id.outTimeLayout);
            myViewHolder.uncomLayout = (LinearLayout) convertView.findViewById(R.id.uncomLayout);
            myViewHolder.comLayout = (LinearLayout) convertView.findViewById(R.id.comLayout);

            myViewHolder.time_layout = (LinearLayout) convertView.findViewById(R.id.time_layout);
            myViewHolder.unCom_layout = (LinearLayout) convertView.findViewById(R.id.unCom_layout);
            myViewHolder.com_layout = (LinearLayout) convertView.findViewById(R.id.com_layout);
            convertView.setTag(myViewHolder);
        }
         myViewHolder = (MyViewHolder) convertView.getTag();
         List<LogVo> logVoList = lists.get(position);
         myViewHolder.user_name.setText(logVoList.get(0).getNAME());
         String logIme = logVoList.get(0).getLOG_TIME();
         int point  = isYesterday(logIme);
        if(point==0){
            myViewHolder.log_time.setText("今天 "+logIme.substring(logIme.length()-5,logIme.length()));
        }else if(point==1){
            myViewHolder.log_time.setText("昨天 "+logIme.substring(logIme.length()-5,logIme.length()));
        }else{
            myViewHolder.log_time.setText(logIme);
        }

        if(logVoList.get(0).getLOG_SUMMARY().equals("null")){
            myViewHolder.summary_layout.setVisibility(View.GONE);
        }else{
            myViewHolder.summary_layout.setVisibility(View.VISIBLE);
            myViewHolder.log_summary.setText(logVoList.get(0).getLOG_SUMMARY());
        }

        myViewHolder.log_comment.setText("评论("+logVoList.get(0).getCOMMENT_AMOUNT()+")");
        myViewHolder.log_type.setText("日计划");

         try{
             comList.clear();
             outTimeList.clear();
             unComList.clear();
            for (int i = 0; i < lists.get(position).size(); i++) {
                if(lists.get(position).get(i).getLOG_FLAG().equals("0")){
                     comList.add(lists.get(position).get(i));
                }else if(lists.get(position).get(i).getLOG_FLAG().equals("1")&& sdf.parse(lists.get(position).get(i).getEND_TIME()).before(new Date())){
                     outTimeList.add(lists.get(position).get(i));
                }else{
                     unComList.add(lists.get(position).get(i));
                }
            }
        }catch (Exception e){

        }

        if(comList.size()==0){
            myViewHolder.comLayout.setVisibility(View.GONE);
        }else{
            myViewHolder.comLayout.setVisibility(View.VISIBLE);
            myViewHolder.com_layout.removeAllViews();
            drawView(myViewHolder.com_layout,comList);
        }
        if(outTimeList.size()==0){
            myViewHolder.outTimeLayout.setVisibility(View.GONE);
        }else{
            myViewHolder.outTimeLayout.setVisibility(View.VISIBLE);
            myViewHolder.time_layout.removeAllViews();
            drawView(myViewHolder.time_layout,outTimeList);
        }
        if(unComList.size()==0){
            myViewHolder.uncomLayout.setVisibility(View.GONE);
        }else{
            myViewHolder.uncomLayout.setVisibility(View.VISIBLE);
            myViewHolder.unCom_layout.removeAllViews();
            drawView(myViewHolder.unCom_layout,unComList);
        }
        return convertView;
    }


    private void drawView(LinearLayout layout,List<LogVo> logVoList){
            collectionLevel(logVoList);
           for (int i = 0; i < logVoList.size(); i++) {
            RelativeLayout relativeLayout = new RelativeLayout(context);
            RelativeLayout.LayoutParams layoutParams  = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin=5;
            layout.addView(relativeLayout,layoutParams);

            TextView levelTextView = new TextView(context);
            RelativeLayout.LayoutParams levelLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            levelTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen.log_size));
            levelLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            levelTextView.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            levelTextView.setText("("+(i+1)+") "+logVoList.get(i).getLOG_LEVEL());
            levelTextView.setId(R.id.my_level);
            levelTextView.setPadding(0,3,3,3);
            relativeLayout.addView(levelTextView,levelLayoutParams);

            TextView contentTextView = new TextView(context);
            RelativeLayout.LayoutParams contentLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            contentTextView.setGravity(Gravity.CENTER_VERTICAL);
            contentTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen.log_size));
            contentLayoutParams.leftMargin=22;
            contentTextView.setPadding(0,3,3,3);
            contentTextView.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            contentLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            contentLayoutParams.addRule(RelativeLayout.RIGHT_OF,R.id.my_level);
            contentTextView.setId(R.id.my_content);
            contentTextView.setText(logVoList.get(i).getLOG_CONTENT());
            relativeLayout.addView(contentTextView,contentLayoutParams);

            ImageView imageView =new ImageView(context);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1);
            layoutParams2.leftMargin=10;
            layoutParams2.rightMargin =10;
            layoutParams2.topMargin=3;
            imageView.setBackgroundColor(context.getResources().getColor(R.color.province_line_border));
            layout.addView(imageView,layoutParams2);
        }
    }

    private void collectionLevel(List<LogVo> logs){
        Collections.sort(logs, new Comparator<LogVo>() {
            @Override
            public int compare(LogVo logOne, LogVo logTwo) {
                return logOne.getLOG_LEVEL().compareTo(logTwo.getLOG_LEVEL());

            }
        });
    }

    public static int isYesterday(String date){
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


    private  class MyViewHolder{
        private TextView user_name;
        private TextView log_time;
        private TextView log_comment;
        private TextView log_type;
        private TextView log_summary;
        private RelativeLayout summary_layout;
        private LinearLayout outTimeLayout;
        private LinearLayout uncomLayout;
        private LinearLayout comLayout;
        private LinearLayout time_layout;
        private LinearLayout unCom_layout;
        private LinearLayout com_layout;
    }
}
