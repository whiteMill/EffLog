package com.stk.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.stk.adapter.TimeRemindAdapter;
import com.stk.efflog.MyApplication;
import com.stk.efflog.R;
import com.stk.utils.URL;
import com.stk.utils.WeiboDialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class SetRemindActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView back;
    private ListView timeListView;
    private List<String> timeList = new ArrayList<>();
    private DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Intent intent;
    private String  MESSAGE;
    private String BUSS_ID;
    private String BUSS_NAME;
    private String BUSS_INFO_ID;
    private Dialog mWeiboDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_remind);
        intent = getIntent();
        MESSAGE = intent.getStringExtra("MESSAGE");
        BUSS_ID = intent.getStringExtra("BUSS_ID");
        BUSS_NAME = intent.getStringExtra("BUSS_NAME");
        BUSS_INFO_ID = intent.getStringExtra("BUSS_INFO_ID");

        initView();
        initDate();
        timeListView.setAdapter(new TimeRemindAdapter(timeList,SetRemindActivity.this));
        timeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 switch (position){
                     case 0:
                         String timeDateOne = afterTime(fmt.format(new Date()),10);
                         Log.d("sdfafs",timeDateOne);
                         mWeiboDialog = WeiboDialogUtils.createLoadingDialog(SetRemindActivity.this, "正在设置");
                         setMind(timeDateOne);
                         break;
                     case 1:
                         String timeDateTwo =  afterTime(fmt.format(new Date()),30);
                         Log.d("sdfafs",timeDateTwo);
                         mWeiboDialog = WeiboDialogUtils.createLoadingDialog(SetRemindActivity.this, "正在设置");
                         setMind(timeDateTwo);
                         break;
                     case 2:
                         String timeDateThree = afterTime(fmt.format(new Date()),60);
                         Log.d("sdfafs",timeDateThree);
                         mWeiboDialog = WeiboDialogUtils.createLoadingDialog(SetRemindActivity.this, "正在设置");
                         setMind(timeDateThree);
                         break;
                     case 3:
                         String timeDateFour = afterTime(fmt.format(new Date()),180);
                         Log.d("sdfafs",timeDateFour);
                         mWeiboDialog = WeiboDialogUtils.createLoadingDialog(SetRemindActivity.this, "正在设置");
                         setMind(timeDateFour);
                         break;
                     case 4:
                         String timeDateFive = afterTime(fmt.format(new Date()),300);
                         Log.d("sdfafs",timeDateFive);
                         mWeiboDialog = WeiboDialogUtils.createLoadingDialog(SetRemindActivity.this, "正在设置");
                         setMind(timeDateFive);
                         break;
                     case 5:
                         String dayTimeOne = afterDay()+" 09:00:00";
                         Log.d("sdfafs",dayTimeOne);
                         mWeiboDialog = WeiboDialogUtils.createLoadingDialog(SetRemindActivity.this, "正在设置");
                         setMind(dayTimeOne);
                         break;
                     case 6:
                         String dayTimeTwo = afterDay()+" 17:00:00";
                         Log.d("sdfafs",dayTimeTwo);
                         mWeiboDialog = WeiboDialogUtils.createLoadingDialog(SetRemindActivity.this, "正在设置");
                         setMind(dayTimeTwo);
                         break;
                 }
            }
        });
    }

    public  String afterTime(String begin_time,int afterTime){
        long time = afterTime*60*1000;
        try {
            Date da = fmt.parse(begin_time);
            Date afterDate = new Date(da.getTime()+time);
            return fmt.format(afterDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return begin_time;
        }
    }

    public  String afterDay(){
        Date date=new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
        date=calendar.getTime(); //这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        return dateString;
    }

    private void initDate() {
       timeList.add("10分钟后提醒");
       timeList.add("30分钟后提醒");
       timeList.add("1小时后提醒");
       timeList.add("3小时后提醒");
       timeList.add("5小时后提醒");
       timeList.add("明天9:00提醒");
       timeList.add("明天17:00提醒");
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        timeListView = (ListView) findViewById(R.id.timeListView);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    private void setMind(String remind_time){
        OkGo.post(URL.BUSS_SET_REMIND)
                .tag(this)
                .params("USER_ID", MyApplication.getUserVo().getUSER_ID())
                .params("MESSAGE", "商机定时提醒-"+MESSAGE)
                .params("BUSS_ID", BUSS_ID)
                .params("BUSS_NAME", BUSS_NAME)
                .params("NAME", MyApplication.getUserVo().getNAME())
                .params("BUSS_INFO_ID", BUSS_INFO_ID)
                .params("REMIND_TIME", remind_time)
                .cacheKey("catchKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                        JSONObject jsonObject  = null;
                        try {
                            jsonObject = new JSONObject(s);
                            if(jsonObject.getBoolean("success")){
                                Toast.makeText(SetRemindActivity.this,jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(SetRemindActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(SetRemindActivity.this, "设置失败", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                        super.onError(call, response, e);
                        Toast.makeText(SetRemindActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }



}
