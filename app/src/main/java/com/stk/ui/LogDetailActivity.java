package com.stk.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.stk.adapter.CommentAdapter;
import com.stk.adapter.WorkItemAdapter;
import com.stk.efflog.MyApplication;
import com.stk.efflog.R;
import com.stk.model.CommentVo;
import com.stk.model.LogVo;
import com.stk.utils.LogListView;
import com.stk.utils.URL;
import com.stk.utils.WeiboDialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Response;

import static com.stk.fragment.WorkFragment.UPDATE_ACTION;

public class LogDetailActivity extends AppCompatActivity implements View.OnClickListener,PopupMenu.OnMenuItemClickListener{

    private ImageView back;
    private TextView opera;
    private TextView commentText;
    private TextView user_name;
    private TextView log_time;
    private TextView log_weather;
    private LogListView commentListView;

    private String log_type;
    private List<LogVo> logVoList;
    private Intent intent;

    private LogListView outTimeListView;
    private LogListView comListView;
    private LogListView unComListView;

    private TextView outTimeText;
    private TextView unComText;
    private TextView comText;

    private List<LogVo> outTimeList = new ArrayList<>();
    private List<LogVo> comList = new ArrayList<>();
    private List<LogVo> unComList = new ArrayList<>();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);

    private LogVo logVo;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    private final int A_RESULT_CODE = 1001;
    private final int B_RESULT_CODE = 1002;
    private final int C_RESULT_CODE = 1003;
    private final int REQUEST_CODE = 102;
    private Dialog mWeiboDialog;
    private final int LOG_DETAIL =100001;
    private final int LOG_COMMENT =100002;
    private List<List<LogVo>> logList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_detail);
        initView();
        intent  = getIntent();
        log_type = intent.getStringExtra("LOG_TYPE");
        logVoList = (List<LogVo>)intent.getSerializableExtra("list");
        logVo = logVoList.get(0);
        user_name.setText(logVo.getNAME());
        log_time .setText(logVo.getLOG_TIME());
        log_weather.setText(logVo.getWEATHER());
        initDate(logVoList);
        outTimeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 Intent intent = new Intent(LogDetailActivity.this,updateLogActivity.class);
                 intent.putExtra("log_type",log_type);
                 intent.putExtra("log",outTimeList.get(position));
                 startActivityForResult(intent,REQUEST_CODE);
            }
        });
        unComListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LogDetailActivity.this,updateLogActivity.class);
                intent.putExtra("log_type",log_type);
                intent.putExtra("log",unComList.get(position));
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
        comListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LogDetailActivity.this,updateLogActivity.class);
                intent.putExtra("log_type",log_type);
                intent.putExtra("log",comList.get(position));
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
        QueryComment();

    }

    @Override
    public void onBackPressed() {
        sendBroadcast(new Intent(UPDATE_ACTION));
        super.onBackPressed();
    }

    private void  initDate(List<LogVo> mList){
        try{
            comList.clear();
            outTimeList.clear();
            unComList.clear();
            for (int i = 0; i < mList.size(); i++) {
                if(mList.get(i).getLOG_FLAG().equals("0")){
                    comList.add(mList.get(i));
                    collectionLevel(comList);
                }else if(mList.get(i).getLOG_FLAG().equals("1")&&sdf.parse(mList.get(i).getEND_TIME()).before(new Date())){
                    outTimeList.add(mList.get(i));
                    collectionLevel(outTimeList);
                }else{
                    unComList.add(mList.get(i));
                    collectionLevel(unComList);
                }
            }
        }catch (Exception e){

        }

        if(comList.size()==0){
            comText.setVisibility(View.GONE);
        }else{
            comText.setVisibility(View.VISIBLE);
        }

        if(outTimeList.size()==0){
            outTimeText.setVisibility(View.GONE);
        }else{
            outTimeText.setVisibility(View.VISIBLE);
        }

        if(unComList.size()==0){
            unComText.setVisibility(View.GONE);
        }else{
            unComText.setVisibility(View.VISIBLE);
        }
        outTimeListView.setAdapter(new WorkItemAdapter(LogDetailActivity.this,outTimeList));
        unComListView.setAdapter(new WorkItemAdapter(LogDetailActivity.this,unComList));
        comListView.setAdapter(new WorkItemAdapter(LogDetailActivity.this,comList));
    }

    private void initView() {
        commentListView = (LogListView) findViewById(R.id.commentListView);
        back = (ImageView) findViewById(R.id.back);
        opera = (TextView) findViewById(R.id.opera);
        commentText = (TextView) findViewById(R.id.commentText);
        user_name = (TextView) findViewById(R.id.user_name);
        log_time = (TextView) findViewById(R.id.log_time);
        log_weather = (TextView) findViewById(R.id.log_weather);

        back.setOnClickListener(this);
        commentText.setOnClickListener(this);
        opera.setOnClickListener(this);
        outTimeListView = (LogListView) findViewById(R.id.outTimeListView);
        comListView = (LogListView) findViewById(R.id.comListView);
        unComListView = (LogListView) findViewById(R.id.unComListView);

        outTimeText = (TextView) findViewById(R.id.outTimeText);
        unComText = (TextView) findViewById(R.id.unComText);
        comText = (TextView) findViewById(R.id.comText);
    }

    private void collectionLevel(List<LogVo> logs){
        Collections.sort(logs, new Comparator<LogVo>() {
            @Override
            public int compare(LogVo logOne, LogVo logTwo) {
                return logOne.getLOG_LEVEL().compareTo(logTwo.getLOG_LEVEL());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                sendBroadcast(new Intent(UPDATE_ACTION));
                finish();
                break;
            case R.id.opera:
                PopupMenu pop = new PopupMenu(this, v);
                pop.getMenuInflater().inflate(R.menu.main, pop.getMenu());
                pop.show();
                pop.setOnMenuItemClickListener(this);
                break;
            case R.id.commentText:
                Intent intent = new Intent(this,UserCommentActivity.class);
                intent.putExtra("log_index",logVo.getLOG_INDEX());
                intent.putExtra("to_id",logVo.getUSER_ID());
                intent.putExtra("to_name",logVo.getNAME());
                startActivityForResult(intent,REQUEST_CODE);
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add:
                Intent intent = new Intent(LogDetailActivity.this,LogDetailAddActivity.class);
                intent.putExtra("log_type",log_type);
                intent.putExtra("log_time",logVo.getLOG_TIME());
                intent.putExtra("weather",logVo.getWEATHER());
                intent.putExtra("log_summary",logVo.getLOG_SUMMARY());
                intent.putExtra("add_time",logVo.getADD_TIME());
                intent.putExtra("log_index",logVo.getLOG_INDEX());
                startActivityForResult(intent,REQUEST_CODE);

                /*
                builder = new AlertDialog.Builder(this);
                builder.setItems(getResources().getStringArray(R.array.ItemEqual), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        switch (arg1) {
                            case 0:
                                Intent aIntent = new Intent(LogDetailActivity.this, AddLogActivity.class);
                                aIntent.putExtra("logLevel",A_RESULT_CODE );
                                aIntent.putExtra("log_type",log_type);
                                aIntent.putExtra("log_index",logVo.getLOG_INDEX());
                                aIntent.putExtra("log_time",logVo.getLOG_TIME());
                                aIntent.putExtra("weather",logVo.getWEATHER());
                                aIntent.putExtra("log_summary",logVo.getLOG_SUMMARY());
                                aIntent.putExtra("add_time",logVo.getADD_TIME());
                                startActivityForResult(aIntent,REQUEST_CODE);
                                break;
                            case 1:
                                Intent bIntent = new Intent(LogDetailActivity.this, AddLogActivity.class);
                                bIntent.putExtra("logLevel", B_RESULT_CODE);
                                bIntent.putExtra("log_type",log_type);
                                bIntent.putExtra("log_index",logVo.getLOG_INDEX());
                                bIntent.putExtra("log_time",logVo.getLOG_TIME());
                                bIntent.putExtra("weather",logVo.getWEATHER());
                                bIntent.putExtra("log_summary",logVo.getLOG_SUMMARY());
                                bIntent.putExtra("add_time",logVo.getADD_TIME());
                                startActivityForResult(bIntent,REQUEST_CODE);
                                break;
                            case 2:
                                Intent cIntent = new Intent(LogDetailActivity.this, AddLogActivity.class);
                                cIntent.putExtra("logLevel", C_RESULT_CODE);
                                cIntent.putExtra("log_type",log_type);
                                cIntent.putExtra("log_index",logVo.getLOG_INDEX());
                                cIntent.putExtra("log_time",logVo.getLOG_TIME());
                                cIntent.putExtra("weather",logVo.getWEATHER());
                                cIntent.putExtra("log_summary",logVo.getLOG_SUMMARY());
                                cIntent.putExtra("add_time",logVo.getADD_TIME());
                                startActivityForResult(cIntent,REQUEST_CODE);
                                break;
                        }
                        arg0.dismiss();
                    }
                });
                builder.show();*/
                break;
            case R.id.delete:
                alertDialog = new AlertDialog.Builder(LogDetailActivity.this).setTitle("提醒").setMessage("确定要删除吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.dismiss();
                                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(LogDetailActivity.this, "正在加载");
                                deleteLog();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        }).create(); // 创建对话框
                alertDialog.show(); // 显示对话框
                break;
        }

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            switch (resultCode){
                case LOG_DETAIL:
                    queryLog();
                    break;
                case LOG_COMMENT:
                   // QueryComment();
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        QueryComment();
    }

    private void QueryComment(){
        OkGo.post(URL.QUERY_COMMENT)
                .tag(this)
                .params("LOG_INDEX",logVo.getLOG_INDEX())
                .cacheKey("catchKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(s);
                            Boolean isSuccess = jsonObject.getBoolean("success");
                            if(isSuccess){
                                List<CommentVo> commentVoList = new Gson().fromJson(jsonObject.getString("data"),new TypeToken<List<CommentVo>>() {}.getType());
                                commentListView.setAdapter(new CommentAdapter(LogDetailActivity.this,commentVoList));

                            }else{
                                Toast.makeText(LogDetailActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                    }
                });
    }



    private void queryLog() {
        OkGo.post(URL.LOG_QUERY_LOG_INDEX)
                .tag(this)
                .params("USER_ID",logVo.getUSER_ID())
                .params("LOG_TYPE",log_type)
                .params("LOG_INDEX",logVo.getLOG_INDEX())
                .cacheKey("catchKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("backSds",s);
                        JSONObject jsonObject =null;
                        try {
                            jsonObject = new JSONObject(s);
                            List<LogVo> voList = new Gson().fromJson(jsonObject.getString("data"),new TypeToken<List<LogVo>>(){}.getType());
                            initDate(voList);
                        } catch (JSONException e) {
                            Toast.makeText(LogDetailActivity.this,"日志获取失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Toast.makeText(LogDetailActivity.this,"服务器请求失败", Toast.LENGTH_SHORT).show();
                        super.onError(call, response, e);
                    }
                });
    }

    private void deleteLog() {
        OkGo.post(URL.DELETE_LOG)
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
                            jsonObject  = new JSONObject(s);
                            if(jsonObject.getBoolean("success")){
                                Toast.makeText(LogDetailActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                sendBroadcast(new Intent(UPDATE_ACTION));
                                finish();
                            }else{
                                Toast.makeText(LogDetailActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(LogDetailActivity.this, "服务器请求失败~", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                        super.onError(call, response, e);
                        Toast.makeText(LogDetailActivity.this, "服务器请求失败~", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
