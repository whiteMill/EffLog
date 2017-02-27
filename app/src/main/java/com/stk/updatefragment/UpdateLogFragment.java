package com.stk.updatefragment;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.stk.adapter.LogUpdateAdapter;
import com.stk.efflog.MyApplication;
import com.stk.efflog.R;
import com.stk.model.LogVo;
import com.stk.model.UpdateLogVo;
import com.stk.ui.LogActivity;
import com.stk.utils.URL;
import com.stk.utils.WeiboDialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateLogFragment extends Fragment implements View.OnClickListener{
    private View view;
    private String log_type=null;
    private List<LogVo> logVoList = new ArrayList<>();
    private ListView updateListView;
    private EditText log_summary;
    private TextView updateLog;
    private LogUpdateAdapter logUpdateAdapter;
    private ArrayList<LogVo> comLogVoList = new ArrayList<>();
    private ArrayList<LogVo> unComLogVoList = new ArrayList<>();
    private List<UpdateLogVo> updateLogVoList  = new ArrayList<>();
    private SimpleDateFormat ssf;
    private int flag = 0;
    private LinearLayout summary_layout;
    private Dialog mWeiboDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_update_log, container, false);
        initView();
        recentLog();
        return view;
    }

    private void initView() {
        ssf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        updateListView = (ListView) view.findViewById(R.id.updateListView);
        updateLog = (TextView) view.findViewById(R.id.updateLog);

        summary_layout  = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.list_foot,null);
        log_summary = (EditText) summary_layout.findViewById(R.id.log_summary);

        updateLog.setOnClickListener(this);
    }

    public void getType(String type){
            log_type = type;
            MyApplication.setLog_type(log_type);
    }

    private void recentLog(){
        OkGo.post(URL.RECENT_LOG)
                .tag(this)
                .params("LOG_TYPE",MyApplication.getLog_type())
                .params("USER_ID",MyApplication.getUserVo().getUSER_ID())
                .cacheKey("catchKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(s);
                            if(jsonObject.getBoolean("success")){
                                logVoList = new Gson().fromJson(jsonObject.getString("data"),new TypeToken<List<LogVo>>() {
                                }.getType());
                                if(logVoList==null){
                                    log_summary.setVisibility(View.INVISIBLE);
                                    summary_layout.setVisibility(View.INVISIBLE);
                                    updateListView.setVisibility(View.INVISIBLE);
                                    updateLog.setText("点击跳转");
                                    flag = 1;
                                    logVoList = new ArrayList<LogVo>();
                                }else{
                                    summary_layout.setVisibility(View.VISIBLE);
                                    updateListView.setVisibility(View.VISIBLE);
                                }
                                collectionLevel(logVoList);
                                logUpdateAdapter  = new LogUpdateAdapter(getActivity(),logVoList);
                                if(logVoList!=null){
                                    updateListView.addFooterView(summary_layout);
                                }
                                updateListView.setAdapter(logUpdateAdapter);
                            }
                        }catch (JSONException e){

                        }
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                    }
                });
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
            case R.id.updateLog:
                if(flag==1){
                    Intent intent = new Intent(getActivity(), LogActivity.class);
                    intent.putExtra("log_type",MyApplication.getLog_type());
                    intent.putExtra("unComLogVoList", (Serializable)unComLogVoList);
                    startActivity(intent);
                    getActivity().finish();
                }else{
                    if(log_summary.getText().toString().trim().isEmpty()){
                        Toast.makeText(getActivity(), "请填写日志总结zzz~", Toast.LENGTH_SHORT).show();
                    }else{
                        for (int i = 0; i < logVoList.size(); i++) {
                            if(logUpdateAdapter.getIsSelected().get(i)){
                                logVoList.get(i).setLOG_FLAG("0");
                                comLogVoList.add(logVoList.get(i));
                            }else{
                                logVoList.get(i).setLOG_FLAG("1");
                                unComLogVoList.add(logVoList.get(i));
                            }
                        }

                        for(int i=0;i<logVoList.size();i++){
                            Log.d("ddinssdifn",logUpdateAdapter.getLogMindMap().get(i));
                            if(logUpdateAdapter.getLogMindMap().get(i)==null){
                                logVoList.get(i).setLOG_MIND("null");
                            }else{
                                logVoList.get(i).setLOG_MIND(logUpdateAdapter.getLogMindMap().get(i));
                            }
                            logVoList.get(i).setLOG_SUMMARY(log_summary.getText().toString().trim());
                        }
                        for (int i = 0; i < logVoList.size(); i++) {
                            UpdateLogVo updateLogVo = new UpdateLogVo();
                            updateLogVo.setLOG_ID(logVoList.get(i).getLOG_ID());
                            updateLogVo.setLOG_FLAG(logVoList.get(i).getLOG_FLAG());
                            updateLogVo.setLOG_INDEX(logVoList.get(i).getLOG_INDEX());
                            updateLogVo.setLOG_MIND(logVoList.get(i).getLOG_MIND());
                            updateLogVo.setLOG_SUMMARY(logVoList.get(i).getLOG_SUMMARY());
                            updateLogVo.setUPDATE_TIME(ssf.format(new Date()));
                            updateLogVo.setLOG_TYPE(log_type);
                            updateLogVoList.add(updateLogVo);
                        }
                        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(getActivity(), "正在更新");
                        updateLog();
                    }
                }
                break;
        }
    }

    private void updateLog() {
        Log.d("sdagsssssssafffffdsgf",updateLogVoList.toString());
        OkGo.post(URL.UPDATE_LOG)
                .tag(this)
                .params("RECENT_LOG","{"+"\"data\":"+updateLogVoList.toString()+"}")
                .params("USER_ID", MyApplication.getUserVo().getUSER_ID())
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

                                logVoList.clear();
                                logUpdateAdapter.notifyDataSetChanged();

                                log_summary.setVisibility(View.INVISIBLE);
                                summary_layout.setVisibility(View.INVISIBLE);
                                updateListView.setVisibility(View.INVISIBLE);
                                flag = 1;
                                updateLog.setText("点击跳转");
                                Toast.makeText(getActivity(), "日志更新成功", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity(), "日志更新失败", Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException e){
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                        super.onError(call, response, e);
                        Toast.makeText(getActivity(), "服务器请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
