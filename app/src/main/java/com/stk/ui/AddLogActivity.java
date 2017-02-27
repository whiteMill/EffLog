package com.stk.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.stk.efflog.MyApplication;
import com.stk.efflog.R;
import com.stk.model.CommitLogVo;
import com.stk.utils.SharedPreferencesUtils;
import com.stk.utils.URL;
import com.stk.utils.WeiboDialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Response;

public class AddLogActivity extends AppCompatActivity implements View.OnClickListener{
    private Intent intent;
    private int logClass;
    private SimpleDateFormat sdf;

    private Button sureLog;
    private EditText log_item;
    private ImageView back;
    private LinearLayout timeLayout;
    private TextView time_begin;
    private TextView time_end;

    private String user_promise;

    private String log_index;
    private String log_time;
    private String log_type;
    private String log_sumamry;
    private String weather;

    private final int LOG_DETAIL =100001;

    private final int A_RESULT_CODE = 1001;
    private final int B_RESULT_CODE = 1002;
    private final int C_RESULT_CODE = 1003;
    private SimpleDateFormat ssf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    private ArrayList<CommitLogVo> logVoArrayList = new ArrayList<>();

    TimePickerView beginpvTime;
    TimePickerView endpvTime;
    private Dialog mWeiboDialog;

    private String ADD_TIME;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_log);
        intent =  getIntent();
        logClass =  intent.getIntExtra("logLevel",1001);
        log_index = intent.getStringExtra("log_index");
        log_time  = intent.getStringExtra("log_time");
        log_type = intent.getStringExtra("log_type");
        weather  = intent.getStringExtra("weather");
        log_sumamry = intent.getStringExtra("log_summary");
        ADD_TIME = intent.getStringExtra("add_time");


        Log.d("fjhdsfsdhgfsd",ADD_TIME);
        initView();
        getCurrentTime();
    }

    private void initView() {
        user_promise = (String) SharedPreferencesUtils.getParam(AddLogActivity.this,"PROMISE","");


        sureLog = (Button) findViewById(R.id.sure_log);
        log_item = (EditText) findViewById(R.id.log_item);
        log_item.setHint("我的承诺："+user_promise);
        back = (ImageView) findViewById(R.id.back);
        timeLayout = (LinearLayout) findViewById(R.id.time_layout);
        time_begin = (TextView) findViewById(R.id.time_begin);
        time_end = (TextView) findViewById(R.id.time_end);
        time_begin.setOnClickListener(this);
        time_end.setOnClickListener(this);
        back.setOnClickListener(this);
        sureLog.setOnClickListener(this);

        beginpvTime = new TimePickerView(this, TimePickerView.Type.ALL);
        beginpvTime.setTime(new Date());
        beginpvTime.setCyclic(true);
        beginpvTime.setCancelable(true);
        beginpvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                time_begin.setText(getTime(date));
                time_end.setText(getTime(afterTime(date)));
                endpvTime.setTime(afterTime(date));
            }
        });

        endpvTime = new TimePickerView(this, TimePickerView.Type.ALL);
        endpvTime.setTime(new Date());
        endpvTime.setCyclic(true);
        endpvTime.setCancelable(true);
        endpvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                time_end.setText(getTime(date));
            }
        });

    }

    private void getCurrentTime() {
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        time_begin.setText(sdf.format(new Date()));
        time_end.setText(sdf.format(new Date()));
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }




    private void conmmitLog(ArrayList<CommitLogVo> commitLogVos){
        OkGo.post(URL.LOG_COMMIT)
                .tag(this)
                .params("LogVos","{"+"\"data\":"+commitLogVos.toString()+"}")
                .params("USER_ID",MyApplication.getUserVo().getUSER_ID())
                .params("NAME",MyApplication.getUserVo().getNAME())
                .params("LEADER_ID",MyApplication.getUserVo().getLEADER_ID())
                .cacheKey("catchKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(s);
                            Boolean isSuccess = jsonObject.getBoolean("success");
                            if(isSuccess){
                                Toast.makeText(AddLogActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                setResult(LOG_DETAIL);
                                finish();
                            }else{
                                Toast.makeText(AddLogActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(AddLogActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Toast.makeText(AddLogActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                    }
                });
    }

    private CommitLogVo commitLogVoGet(String logContent, String timeBegin, String timeEnd, String level){
        CommitLogVo commitLogVo = new CommitLogVo();
        commitLogVo.setBEGIN_TIME(timeBegin);
        commitLogVo.setLOG_INDEX(log_index);
        commitLogVo.setROLE_ID(MyApplication.getUserVo().getROLE_ID());
        commitLogVo.setEND_TIME(timeEnd);
        commitLogVo.setLOG_CONTENT(logContent);
        commitLogVo.setLOG_FLAG("1");
        commitLogVo.setLOG_ID("123");
        commitLogVo.setUSER_ID(MyApplication.getUserVo().getUSER_ID());
        commitLogVo.setLOG_TIME(ADD_TIME);

        commitLogVo.setLOG_LEVEL(level);
        commitLogVo.setPRIOR_ID("null");
        commitLogVo.setPRIOR_INDEX("null");
        commitLogVo.setWEATHER(weather);
        commitLogVo.setUPDATE_TIME(ssf.format(new Date()));
        commitLogVo.setLOG_MIND("null");
        commitLogVo.setLOG_SUMMARY(log_sumamry);
        commitLogVo.setLOG_TYPE(log_type);
        return commitLogVo;
    }

    @Override
    public void onClick(View v) {
      switch (v.getId()){
          case R.id.sure_log:
              if(log_item.getText().toString().trim().isEmpty()){
                  Toast.makeText(this, "请输入相关内容", Toast.LENGTH_SHORT).show();
              }else{
                  switch (logClass){
                      case A_RESULT_CODE:
                          String alogContent = log_item.getText().toString().trim();
                          String alog_begin = time_begin.getText().toString();
                          String alog_end =time_end.getText().toString();
                          CommitLogVo aLogVo =  commitLogVoGet(alogContent,alog_begin,alog_end,"A");
                          logVoArrayList.clear();
                          logVoArrayList.add(aLogVo);
                          mWeiboDialog = WeiboDialogUtils.createLoadingDialog(AddLogActivity.this, "正在上传");
                          conmmitLog(logVoArrayList);
                          break;
                      case B_RESULT_CODE:
                          String blogContent = log_item.getText().toString().trim();
                          String blog_begin = time_begin.getText().toString();
                          String blog_end =time_end.getText().toString();
                          CommitLogVo bLogVo =  commitLogVoGet(blogContent,blog_begin,blog_end,"B");
                          logVoArrayList.clear();
                          logVoArrayList.add(bLogVo);
                          mWeiboDialog = WeiboDialogUtils.createLoadingDialog(AddLogActivity.this, "正在上传");
                          conmmitLog(logVoArrayList);
                          break;
                      case C_RESULT_CODE:
                          String clogContent = log_item.getText().toString().trim();
                          String clog_begin = time_begin.getText().toString();
                          String clog_end =time_end.getText().toString();
                          CommitLogVo cLogVo =  commitLogVoGet(clogContent,clog_begin,clog_end,"C");
                          logVoArrayList.clear();
                          logVoArrayList.add(cLogVo);
                          mWeiboDialog = WeiboDialogUtils.createLoadingDialog(AddLogActivity.this, "正在上传");
                          conmmitLog(logVoArrayList);
                          break;
                  }
              }
               break;
          case R.id.back:
              finish();
              break;

          case R.id.time_begin:
              try {
                  beginpvTime.setTime(sdf.parse(time_begin.getText().toString()));
              } catch (ParseException e) {
                  e.printStackTrace();
              }
              beginpvTime.show();

              break;
          case R.id.time_end:
              try {
                  endpvTime.setTime(sdf.parse(time_end.getText().toString()));
              } catch (ParseException e) {
                  e.printStackTrace();
              }
              endpvTime.show();
              break;
      }
    }

    public Date afterTime(Date da){
        long time = 20*60*1000;
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date afterDate = new Date(da.getTime()+time);
        return afterDate;
    }
}
