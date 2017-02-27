package com.stk.logfragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.stk.adapter.WorkAdapter;
import com.stk.efflog.MyApplication;
import com.stk.efflog.R;
import com.stk.model.LogVo;
import com.stk.model.WorkLogVo;
import com.stk.ui.LogDetailActivity;
import com.stk.utils.SwipeRefreshView;
import com.stk.utils.URL;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SeasonLogFragment extends Fragment {
    private View view;
    private List<List<LogVo>> logList = new ArrayList<>();
    private List<List<LogVo>> queryList = null;
    private ListView seasonListView;
    private SwipeRefreshView swipeRefreshLayout;
    private int i=1;
    private WorkAdapter workAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.fragment_season_log, container, false);
        initView();
        swipeRefreshLayout.setRefreshing(true);
        workAdapter = new WorkAdapter(logList,getActivity());
        seasonListView.setAdapter(workAdapter);

        logList.clear();
        chooseUser(MyApplication.getUserVo().getUSER_ID(),1+"");
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        logList.clear();
                        i = 1;
                        chooseUser(MyApplication.getUserVo().getUSER_ID(),1+"");
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
                        chooseUser(MyApplication.getUserVo().getUSER_ID(),(++i)+"");


                        // 加载完数据设置为不加载状态，将加载进度收起来
                        swipeRefreshLayout.setLoading(false);
                    }
                }, 1200);
            }
        });
        return view;
    }

    private void initView(){
        swipeRefreshLayout = (SwipeRefreshView) view.findViewById(R.id.seasonSwipeRefresh);
        // 设置下拉进度的背景颜色，默认就是白色的
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        // 设置下拉进度的主题颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.textdefault, R.color.colorPrimary, R.color.colorPrimaryDark);
        seasonListView = (ListView) view.findViewById(R.id.seasonListView);
        seasonListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent  = new Intent(getActivity(), LogDetailActivity.class);
                intent.putExtra("LOG_TYPE","04");
                intent.putExtra("list", (Serializable) logList.get(position));
                startActivity(intent);

            }
        });
    }
    public void  freshData(){
        logList.clear();
        chooseUser(MyApplication.getUserVo().getUSER_ID(),1+"");
    }

    private void chooseUser(String user_id,String BEGIN_INDEX){
        OkGo.post(URL.LOG_QUERY)
                .tag(this)
                .params("USER_ID",user_id)
                .params("LOG_TYPE","04")
                .params("BEGIN_INDEX",BEGIN_INDEX)
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
                                if(queryList.size()==0&&i!=1){
                                    i = i-1;
                                    Toast.makeText(getActivity(), "没有更多数据了", Toast.LENGTH_SHORT).show();
                                }
                                logList.addAll(queryList);
                                workAdapter.notifyDataSetChanged();
                            }else{
                                Toast.makeText(getActivity(),workLogVo.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            Toast.makeText(getActivity(),"日志获取失败", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(),"服务器请求失败", Toast.LENGTH_SHORT).show();
                        super.onError(call, response, e);
                    }
                });
    }

}
