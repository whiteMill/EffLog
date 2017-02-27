package com.stk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.stk.efflog.R;
import com.stk.model.CommitLogVo;
import com.stk.utils.SharedPreferencesUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.stk.efflog.R.id.endTime;

public class SingleLogActivity extends AppCompatActivity implements View.OnClickListener{

    private Intent intent;
    private String position;
    private String groupPosition;
    private ArrayList<CommitLogVo> logVoArrayList = new ArrayList<>();
    private Button update_log;
    private Button delete_log;
    private TextView time_begin;
    private TextView time_end;
    private EditText log_item;
    private ImageView back;
    private final int DELETE_CODE = 1213;
    private final int UPDATE_CODE = 1212;
    private CommitLogVo logVo = null;
    private SimpleDateFormat  sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private String user_promise;
    private RelativeLayout time_wheel;
    TimePickerView beginpvTime;
    TimePickerView endpvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_log);
        initView();
        intent = getIntent();
        position = intent.getStringExtra("position");
        groupPosition = intent.getStringExtra("groupPosition");
        logVoArrayList = (ArrayList<CommitLogVo>) intent.getSerializableExtra("list");

        logVo = logVoArrayList.get(Integer.valueOf(position));
        log_item.setText(logVo.getLOG_CONTENT());
        time_begin.setText(logVo.getBEGIN_TIME());
        time_end.setText(logVo.getEND_TIME());
    }

    private void initView(){
        user_promise = (String) SharedPreferencesUtils.getParam(SingleLogActivity.this,"PROMISE","");
        time_wheel = (RelativeLayout) findViewById(R.id.time_wheel);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
            case R.id.update_log:
                if(log_item.getText().toString().trim().isEmpty()){
                    Toast.makeText(this, "请输入相关内容", Toast.LENGTH_SHORT).show();
                }else{
                    Intent update=new Intent();
                    update.putExtra("update_position",position);
                    update.putExtra("update_groupPosition",groupPosition);
                    update.putExtra("update_content",log_item.getText().toString().trim());
                    update.putExtra("begin_time",time_begin.getText().toString());
                    update.putExtra("end_time",time_end.getText().toString());
                    setResult(UPDATE_CODE, update);
                    finish();
                }
                break;
            case R.id.delete_log:
                Intent delete=new Intent();
                delete.putExtra("delete_position",position);
                delete.putExtra("delete_groupPosition",groupPosition);
                setResult(DELETE_CODE, delete);
                finish();
                break;

        }
    }
}
