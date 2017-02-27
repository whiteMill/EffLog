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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.stk.adapter.WorkAdapter;
import com.stk.efflog.R;
import com.stk.model.LogVo;
import com.stk.model.WorkLogVo;
import com.stk.utils.SwipeRefreshView;
import com.stk.utils.URL;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class SortLogActivity extends AppCompatActivity implements View.OnClickListener{
    private Intent intent;
    private String USER_ID;
    private String LOG_TYPE;
    private String LOG_TIME;
    private String LOG_KEY;

    private List<List<LogVo>> logList = new ArrayList<>();
    private List<List<LogVo>> queryList = null;
    private ListView sortLogListView;
    private SwipeRefreshView swipeRefreshLayout;
    private int i=1;
    private WorkAdapter workAdapter;
    private ImageView pass_back;
    private TextView emptyLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_log);
        initView();

        intent  = getIntent();
        USER_ID = intent.getStringExtra("user_id");
        LOG_TYPE = intent.getStringExtra("log_type");
        LOG_TIME = intent.getStringExtra("log_time");
        LOG_KEY = intent.getStringExtra("log_key");

        swipeRefreshLayout.setRefreshing(true);
        workAdapter = new WorkAdapter(logList,SortLogActivity.this);
        sortLogListView.setAdapter(workAdapter);
        logList.clear();
        chooseUser(1+"");
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        logList.clear();
                        i = 1;
                        chooseUser(1+"");
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
                        chooseUser((++i)+"");


                        // 加载完数据设置为不加载状态，将加载进度收起来
                        swipeRefreshLayout.setLoading(false);
                    }
                }, 1200);
            }
        });

    }

    private void initView(){
        emptyLogo = (TextView) findViewById(R.id.emptyLogo);

        pass_back = (ImageView)findViewById(R.id.pass_back);
        pass_back.setOnClickListener(this);
        swipeRefreshLayout = (SwipeRefreshView)findViewById(R.id.daySwipeRefresh);
        // 设置下拉进度的背景颜色，默认就是白色的
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        // 设置下拉进度的主题颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.textdefault, R.color.colorPrimary, R.color.colorPrimaryDark);
        sortLogListView = (ListView)findViewById(R.id.sortLogListView);
        sortLogListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent  = new Intent(SortLogActivity.this, LogDetailActivity.class);
                intent.putExtra("LOG_TYPE","01");
                intent.putExtra("list", (Serializable) logList.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pass_back:
                finish();
                break;
        }
    }

    private void chooseUser(String BEGIN_INDEX){
        OkGo.post(URL.SORT_LOG)
                .tag(this)
                .params("USER_ID",USER_ID)
                .params("LOG_TYPE",LOG_TYPE)
                .params("BEGIN_INDEX",BEGIN_INDEX)
                .params("LOG_KEY",LOG_KEY)
                .params("LOG_TIME",LOG_TIME)
                .cacheKey("catchKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        swipeRefreshLayout.setRefreshing(false);
                        WorkLogVo workLogVo = new Gson().fromJson(s, WorkLogVo.class);
                        try{
                            if(workLogVo.isSuccess()){
                                queryList =  workLogVo.getData();

                                if(queryList.size()==0&&i==1){
                                   swipeRefreshLayout.setVisibility(View.GONE);
                                   emptyLogo.setVisibility(View.VISIBLE);
                                }else{
                                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                                    emptyLogo.setVisibility(View.GONE);
                                }

                                if(queryList.size()==0&&i!=1){
                                    i = i-1;
                                    Toast.makeText(SortLogActivity.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
                                }
                                logList.addAll(queryList);
                                workAdapter.notifyDataSetChanged();
                            }else{
                                Toast.makeText(SortLogActivity.this,workLogVo.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            Toast.makeText(SortLogActivity.this,"日志获取失败", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(SortLogActivity.this,"网络请求失败", Toast.LENGTH_SHORT).show();
                        super.onError(call, response, e);
                    }
                });
    }
}
