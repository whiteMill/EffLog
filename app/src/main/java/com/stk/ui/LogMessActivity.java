package com.stk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.stk.adapter.LogStateAdapter;
import com.stk.efflog.MyApplication;
import com.stk.efflog.R;
import com.stk.model.LogStateVo;
import com.stk.utils.SwipeRefreshView;
import com.stk.utils.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class LogMessActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView back;
    private ListView remindListView;
    private List<LogStateVo> logStateVoList = new ArrayList<>();
    private List<LogStateVo> queryLogStateVoList = new ArrayList<>();
    private LogStateAdapter logStateAdapter ;
    private SwipeRefreshView swipeRefreshLayout;
    private int i=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_mess);
        initView();

        logStateAdapter = new LogStateAdapter(LogMessActivity.this,logStateVoList);
        remindListView.setAdapter(logStateAdapter);
        logStateVoList.clear();
        getPushLogState(1+"");

        remindListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LogMessActivity.this,LogMessDetailActivity.class);
                intent.putExtra("LogStateVo",logStateVoList.get(position));
                startActivity(intent);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        logStateVoList.clear();
                        i = 1;
                        getPushLogState(1+"");
                    }
                },1500);
            }
        });

        swipeRefreshLayout.setOnLoadListener(new SwipeRefreshView.OnLoadListener() {
            @Override
            public void onLoad() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 添加数据
                     /*   for (int i = 30; i < 35; i++) {
                         *//*   mList.add("我是天才" + i+ "号");
                            // 这里要放在里面刷新，放在外面会导致刷新的进度条卡住
                            mAdapter.notifyDataSetChanged();*//*
                        }*/
                        getPushLogState((++i)+"");


                        // 加载完数据设置为不加载状态，将加载进度收起来
                        swipeRefreshLayout.setLoading(false);
                    }
                }, 1200);
            }
        });

    }

    private void initView() {
        swipeRefreshLayout = (SwipeRefreshView)findViewById(R.id.logSwipeRefresh);
        // 设置下拉进度的背景颜色，默认就是白色的
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        // 设置下拉进度的主题颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.textdefault, R.color.colorPrimary, R.color.colorPrimaryDark);
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

    private void getPushLogState(String index){
        OkGo.post(URL.QUERY_LOG_PUSH_STATE)
                .tag(this)
                .params("USER_ID", MyApplication.getUserVo().getUSER_ID())
                .params("BEGIN_INDEX", index)
                .cacheKey("catchKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        swipeRefreshLayout.setRefreshing(false);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(s);
                            if(jsonObject.getBoolean("success")){
                                queryLogStateVoList = new Gson().fromJson(jsonObject.getString("data"),new TypeToken<List<LogStateVo>>(){}.getType());
                                if(queryLogStateVoList.size()==0&&i!=1){
                                    i = i-1;
                                    Toast.makeText(LogMessActivity.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
                                }
                                logStateVoList.addAll(queryLogStateVoList);
                                logStateAdapter.notifyDataSetChanged();
                            }else{
                                Toast.makeText(LogMessActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LogMessActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(LogMessActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                        super.onError(call, response, e);
                    }
                });
    }
}
