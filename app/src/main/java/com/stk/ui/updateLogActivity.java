package com.stk.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.stk.efflog.MyApplication;
import com.stk.efflog.R;
import com.stk.model.LogVo;
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

import okhttp3.Call;
import okhttp3.Response;

import static com.stk.efflog.R.id.endTime;

public class updateLogActivity extends AppCompatActivity implements View.OnClickListener{

    private Intent intent;
    private String position;
    private ArrayList<LogVo> logVoArrayList = new ArrayList<>();

    private Button update_log;
    private Button delete_log;
    private TextView time_begin;
    private TextView time_end;
    private EditText log_item;
    private ImageView back;
    private final int DELETE_CODE = 1213;
    private final int UPDATE_CODE = 1212;
    private String currentTime;
    private int Year, Month, Day,Hour,Sec;
    private SimpleDateFormat sdf;
    private String log_type;

    private String user_promise;
    private TextView complete_state;
    private LogVo logVo;
    private AlertDialog alertDialog;
    private Dialog mWeiboDialog;
    private final int LOG_DETAIL =100001;
    private int fff=0;
    private EditText log_mind;
    TimePickerView beginpvTime;
    TimePickerView endpvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_log);
        intent  = getIntent();
        logVo= (LogVo)intent.getSerializableExtra("log");
        log_type  = intent.getStringExtra("log_type");


        initView();
        getCurrentTime();
        log_item.setText(logVo.getLOG_CONTENT());
        time_begin.setText(logVo.getBEGIN_TIME());
        time_end.setText(logVo.getEND_TIME());
        if(logVo.getLOG_MIND().equals("null")){
            log_mind.setText("");
        }else{
            log_mind.setText(logVo.getLOG_MIND());
        }
    }

    private void initView(){
        log_mind = (EditText) findViewById(R.id.log_mind);
        complete_state = (TextView) findViewById(R.id.complete_state);
        if(logVo.getLOG_FLAG().equals("1")){
            complete_state.setText("未完成");
        }else{
            complete_state.setText("已完成");
        }

        user_promise = (String) SharedPreferencesUtils.getParam(updateLogActivity.this,"PROMISE","");


        update_log = (Button) findViewById(R.id.update_log);
        delete_log = (Button) findViewById(R.id.delete_log);
        time_begin = (TextView) findViewById(R.id.time_begin);
        time_end = (TextView) findViewById(R.id.time_end);
        log_item = (EditText) findViewById(R.id.log_item);
        log_item.setHint("我的承诺："+user_promise);
        back = (ImageView) findViewById(R.id.back);
        update_log.setOnClickListener(this);
        delete_log.setOnClickListener(this);
        time_begin.setOnClickListener(this);
        time_end.setOnClickListener(this);
        back.setOnClickListener(this);
        complete_state.setOnClickListener(this);

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

    public Date afterTime(Date da){
        long time = 20*60*1000;
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date afterDate = new Date(da.getTime()+time);
        return afterDate;
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }


    private void getCurrentTime() {
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        time_begin.setText(sdf.format(new Date()));
        time_end.setText(sdf.format(new Date()));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
            case R.id.complete_state:
                if(logVo.getLOG_FLAG().equals("1")){
                    if(fff==0){
                        complete_state.setText("已完成");
                        fff=1;
                    }else{
                        complete_state.setText("未完成");
                        fff=0;
                    }
                }else{
                    if(fff==0){
                        complete_state.setText("未完成");
                        fff=1;
                    }else{
                        complete_state.setText("已完成");
                        fff=0;
                    }
                }

                break;

            case R.id.update_log:
                updateLog();
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(updateLogActivity.this, "正在更新");
                break;
            case R.id.delete_log:
                alertDialog = new AlertDialog.Builder(updateLogActivity.this).setTitle("提醒").setMessage("确定要删除吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.dismiss();
                                deleteLog();
                                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(updateLogActivity.this, "正在删除");
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        }).create(); // 创建对话框
                alertDialog.show(); // 显示对话框
                break;
        }
    }

    private void deleteLog(){
        OkGo.post(URL.DELETE_SINGLE_LOG)
                .tag(this)
                .params("LOG_ID",logVo.getLOG_ID())
                .params("LOG_INDEX",logVo.getLOG_INDEX())
                .params("USER_ID",logVo.getUSER_ID())
                .params("LOG_TYPE",log_type)
                .params("NAME", MyApplication.getUserVo().getNAME())
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
                            if(jsonObject.getBoolean("success")){
                                Toast.makeText(updateLogActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(updateLogActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        setResult(LOG_DETAIL);
                        finish();

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                        super.onError(call, response, e);
                        setResult(LOG_DETAIL);
                        finish();
                    }
                });

    }

    private void updateLog(){
        OkGo.post(URL.UPDATE_SINGLE_LOG)
                .tag(this)
                .params("LOG_ID",logVo.getLOG_ID())
                .params("LOG_CONTENT",log_item.getText().toString().trim())
                .params("BEGIN_TIME",time_begin.getText().toString())
                .params("END_TIME",time_end.getText().toString())
                .params("LOG_FLAG",complete_state.getText().toString().equals("未完成")?"1":"0")
                .params("LOG_MIND",log_mind.getText().toString().trim().equals("")?"null":log_mind.getText().toString().trim())
                .params("LOG_INDEX",logVo.getLOG_INDEX())
                .params("USER_ID",logVo.getUSER_ID())
                .params("LOG_TYPE",log_type)
                .params("NAME", MyApplication.getUserVo().getNAME())
                .params("LEADER_ID",MyApplication.getUserVo().getLEADER_ID())
                .cacheKey("catchKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                        setResult(LOG_DETAIL);
                        finish();
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(s);
                            Toast.makeText(updateLogActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        setResult(LOG_DETAIL);
                        finish();
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                        super.onError(call, response, e);
                        setResult(LOG_DETAIL);
                        finish();
                    }

                });
    }
}
