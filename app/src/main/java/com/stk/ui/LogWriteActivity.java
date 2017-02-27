package com.stk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.stk.efflog.R;
import com.stk.utils.SharedPreferencesUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LogWriteActivity extends AppCompatActivity implements View.OnClickListener{

    private Intent intent;
    private int logClass;
    private SimpleDateFormat sdf;

    private Button sureLog;
    private EditText log_item;
    private String itemContent;
    private ImageView back;
    private LinearLayout timeLayout;
    private TextView time_begin;
    private TextView time_end;
    private String user_promise;

    private final int A_RESULT_CODE = 1001;
    private final int B_RESULT_CODE = 1002;
    private final int C_RESULT_CODE = 1003;
    private int groupPosition;

    TimePickerView beginpvTime;
    TimePickerView endpvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_write);
        intent =  getIntent();
        logClass =  intent.getIntExtra("logLevel",1001);
        groupPosition = intent.getIntExtra("groupPosition",0);
        initView();
        getCurrentTime();
    }

    private void initView(){
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        user_promise = (String) SharedPreferencesUtils.getParam(LogWriteActivity.this,"PROMISE","");
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

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }

    private void getCurrentTime() {
        Calendar now = Calendar.getInstance();
        now.get(Calendar.MINUTE);
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        time_begin.setText(sdf.format(new Date()));
        time_end.setText(sdf.format(new Date()));
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
                            itemContent = log_item.getText().toString().trim();
                            Intent aIntent=new Intent();
                            aIntent.putExtra("Log_content", itemContent);
                            aIntent.putExtra("begin_time",time_begin.getText().toString());
                            aIntent.putExtra("end_time",time_end.getText().toString());
                            aIntent.putExtra("position",groupPosition);
                            setResult(A_RESULT_CODE, aIntent);
                            finish();
                            break;
                        case B_RESULT_CODE:
                            itemContent = log_item.getText().toString().trim();
                            Intent bIntent=new Intent();
                            bIntent.putExtra("Log_content", itemContent);
                            bIntent.putExtra("begin_time",time_begin.getText().toString());
                            bIntent.putExtra("end_time",time_end.getText().toString());
                            bIntent.putExtra("position",groupPosition);
                            setResult(B_RESULT_CODE, bIntent);
                            finish();
                            break;
                        case C_RESULT_CODE:
                            itemContent = log_item.getText().toString().trim();
                            Intent cIntent=new Intent();
                            cIntent.putExtra("Log_content", itemContent);
                            cIntent.putExtra("begin_time",time_begin.getText().toString());
                            cIntent.putExtra("end_time",time_end.getText().toString());
                            cIntent.putExtra("position",groupPosition);
                            setResult(C_RESULT_CODE, cIntent);
                            finish();
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
