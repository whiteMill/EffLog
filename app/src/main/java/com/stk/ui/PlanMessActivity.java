package com.stk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.stk.adapter.LogPushAdapter;
import com.stk.efflog.MyApplication;
import com.stk.efflog.R;
import com.stk.model.LogPushVo;
import com.stk.utils.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


public class PlanMessActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView back;
    private ListView remindListView;
    private List<LogPushVo> logPushVoList = new ArrayList<>();
    private LogPushAdapter logPushAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        initView();
        getPushLogRecord();
        remindListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PlanMessActivity.this,PlanDetailActivty.class);
                intent.putExtra("logPushVo",logPushVoList.get(position));
                startActivity(intent);
            }
        });
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        remindListView= (ListView) findViewById(R.id.remindListView);
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

    private void getPushLogRecord(){
        OkGo.post(URL.QUERY_LOG_PUSH)
                .tag(this)
                .params("USER_ID", MyApplication.getUserVo().getUSER_ID())
                .cacheKey("catchKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(s);
                            if(jsonObject.getBoolean("success")){
                                logPushVoList = new Gson().fromJson(jsonObject.getString("data"),new TypeToken<List<LogPushVo>>(){}.getType());
                                logPushAdapter = new LogPushAdapter(PlanMessActivity.this,logPushVoList);
                                remindListView.setAdapter(logPushAdapter);
                            }else{

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


}
