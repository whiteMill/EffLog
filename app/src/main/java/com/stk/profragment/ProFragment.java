package com.stk.profragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.stk.adapter.ProjectAdapter;
import com.stk.efflog.MyApplication;
import com.stk.efflog.R;
import com.stk.model.ProjectVo;
import com.stk.ui.ProjectActivity;
import com.stk.utils.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProFragment extends Fragment {

    private GridView proGridView;
    private View view;
    private List<ProjectVo> projectVoList  = null;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pro_show, container, false);
        initView();
        swipeRefreshLayout.setRefreshing(true);
        getAllPro();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getAllPro();
                    }
                },1500);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllPro();
    }

    private void initView() {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.proSwipeRefresh);
        // 设置下拉进度的背景颜色，默认就是白色的
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        // 设置下拉进度的主题颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.textdefault, R.color.colorPrimary, R.color.colorPrimaryDark);
        proGridView  = (GridView) view.findViewById(R.id.proGridView);

        proGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ProjectActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("proList", projectVoList.get(position));
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });
    }

    private void getAllPro(){
        OkGo.post(URL.QUERY_PROJECT)
                .tag(this)
                .params("USER_ID", MyApplication.getUserVo().getUSER_ID())
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
                                projectVoList = new Gson().fromJson(jsonObject.getString("data"),new TypeToken<List<ProjectVo>>(){}.getType());
                                proGridView.setAdapter(new ProjectAdapter(projectVoList,getActivity()));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        swipeRefreshLayout.setRefreshing(false);
                        super.onError(call, response, e);
                    }
                });


    }

}
