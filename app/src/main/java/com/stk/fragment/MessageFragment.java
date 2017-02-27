package com.stk.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.stk.efflog.MyApplication;
import com.stk.efflog.R;
import com.stk.model.BussPushVo;
import com.stk.model.LogPushVo;
import com.stk.model.LogStateVo;
import com.stk.model.ProPushVo;
import com.stk.ui.BussMessActivity;
import com.stk.ui.LogMessActivity;
import com.stk.ui.PlanMessActivity;
import com.stk.ui.ProMessActivity;
import com.stk.utils.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment implements View.OnClickListener{

    private View view;
    private RelativeLayout log_layout;
    private RelativeLayout buss_layout;
    private RelativeLayout pro_layout;
    private RelativeLayout plan_layout;
    private List<LogPushVo> logPushVoList;
    private List<LogStateVo> logStateVoList;
    private List<BussPushVo> bussPushVoList;
    private List<ProPushVo> proPushVoList;

    private TextView plan_mess;
    private TextView plan_time;

    private TextView log_mess;
    private TextView log_time;

    private TextView buss_time;
    private TextView buss_mess;

    private TextView pro_time;
    private TextView pro_mess;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message, container, false);
        initView();
        getPushLogRecord();
        getPushLogState();
        getPushBussState();
        getPushProState();
        return view;
    }

    private void initView() {
        pro_time = (TextView) view.findViewById(R.id.pro_time);
        pro_mess = (TextView) view.findViewById(R.id.pro_mess);

        buss_time = (TextView) view.findViewById(R.id.buss_time);
        buss_mess = (TextView) view.findViewById(R.id.buss_mess);

        plan_time = (TextView) view.findViewById(R.id.plan_time);
        plan_mess = (TextView) view.findViewById(R.id.plan_mess);

        log_mess = (TextView) view.findViewById(R.id.log_mess);
        log_time = (TextView) view.findViewById(R.id.log_time);

        log_layout = (RelativeLayout) view.findViewById(R.id.log_layout);
        buss_layout = (RelativeLayout) view.findViewById(R.id.buss_layout);
        pro_layout = (RelativeLayout) view.findViewById(R.id.pro_layout);
        plan_layout = (RelativeLayout) view.findViewById(R.id.plan_layout);

        log_layout.setOnClickListener(this);
        buss_layout.setOnClickListener(this);
        pro_layout.setOnClickListener(this);
        plan_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.plan_layout:
                Intent planIntent = new Intent(getActivity(), PlanMessActivity.class);
                startActivity(planIntent);
                break;
            case R.id.log_layout:
                Intent logIntent = new Intent(getActivity(), LogMessActivity.class);
                startActivity(logIntent);
                break;
            case R.id.buss_layout:
                Intent bussIntent = new Intent(getActivity(), BussMessActivity.class);
                startActivity(bussIntent);
                break;
            case R.id.pro_layout:
                Intent proIntent = new Intent(getActivity(), ProMessActivity.class);
                startActivity(proIntent);
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
                                if(logPushVoList.size()!=0){
                                     LogPushVo logPushVo = logPushVoList.get(0);
                                     plan_mess.setVisibility(View.VISIBLE);
                                     plan_time.setVisibility(View.VISIBLE);
                                     plan_mess.setText(logPushVo.getLOG_CONTENT());
                                     String logIme =logPushVo.getLOG_TIME();

                                    int point  = isYesterday(logIme);
                                    if(point==0){
                                        plan_time.setText("今天 "+logIme.substring(logIme.length()-5,logIme.length()));
                                    }else if(point==1){
                                        plan_time.setText(logIme.substring(5));
                                    }else{
                                        plan_time.setText(logIme.substring(5));
                                    }
                                }else{
                                    plan_mess.setVisibility(View.INVISIBLE);
                                    plan_time.setVisibility(View.INVISIBLE);
                                }
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

    private void getPushLogState(){
        OkGo.post(URL.QUERY_LOG_PUSH_STATE)
                .tag(this)
                .params("USER_ID", MyApplication.getUserVo().getUSER_ID())
                .params("BEGIN_INDEX", "1")
                .cacheKey("catchKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(s);
                            if(jsonObject.getBoolean("success")){
                                logStateVoList = new Gson().fromJson(jsonObject.getString("data"),new TypeToken<List<LogStateVo>>(){}.getType());
                                if(logStateVoList.size()!=0){
                                    LogStateVo logStateVo = logStateVoList.get(0);
                                    log_mess.setVisibility(View.VISIBLE);
                                    log_time.setVisibility(View.VISIBLE);
                                    log_mess.setText(logStateVo.getMESSAGE());
                                    String logIme =logStateVo.getLOG_TIME();

                                    int point  = isYesterday(logIme);
                                    if(point==0){
                                        log_time.setText("今天 "+logIme.substring(logIme.length()-5,logIme.length()));
                                    }else if(point==1){
                                        log_time.setText(logIme.substring(5));
                                    }else{
                                        log_time.setText(logIme.substring(5));
                                    }
                                }else{
                                    log_mess.setVisibility(View.INVISIBLE);
                                    log_time.setVisibility(View.INVISIBLE);
                                }
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


    private void getPushBussState(){
        OkGo.post(URL.QUERY_BUSS_PUSH_STATE)
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
                                bussPushVoList = new Gson().fromJson(jsonObject.getString("data"),new TypeToken<List<BussPushVo>>(){}.getType());
                                if(bussPushVoList.size()!=0){
                                    BussPushVo BussPushVo = bussPushVoList.get(0);
                                    buss_mess.setVisibility(View.VISIBLE);
                                    buss_time.setVisibility(View.VISIBLE);
                                    buss_mess.setText(BussPushVo.getMESSAGE());
                                    String logIme =BussPushVo.getBUSS_TIME();

                                    int point  = isYesterday(logIme);
                                    if(point==0){
                                        buss_time.setText("今天 "+logIme.substring(logIme.length()-5,logIme.length()));
                                    }else if(point==1){
                                        buss_time.setText(logIme.substring(5));
                                    }else{
                                        buss_time.setText(logIme.substring(5));
                                    }
                                }else{
                                    buss_mess.setVisibility(View.INVISIBLE);
                                    buss_time.setVisibility(View.INVISIBLE);
                                }
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

    private void getPushProState(){
        OkGo.post(URL.QUERY_PRO_PUSH_STATE)
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
                                proPushVoList = new Gson().fromJson(jsonObject.getString("data"),new TypeToken<List<ProPushVo>>(){}.getType());
                                if(proPushVoList.size()!=0){
                                    ProPushVo proPushVo = proPushVoList.get(0);
                                    pro_mess.setVisibility(View.VISIBLE);
                                    pro_time.setVisibility(View.VISIBLE);
                                    pro_mess.setText(proPushVo.getMESSAGE());
                                    String logIme =proPushVo.getPRO_TIME();

                                    int point  = isYesterday(logIme);
                                    if(point==0){
                                        pro_time.setText("今天 "+logIme.substring(logIme.length()-5,logIme.length()));
                                    }else if(point==1){
                                        pro_time.setText(logIme.substring(5));
                                    }else{
                                        pro_time.setText(logIme.substring(5));
                                    }
                                }else{
                                    pro_mess.setVisibility(View.INVISIBLE);
                                    pro_time.setVisibility(View.INVISIBLE);
                                }
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


    public void pushMessage(String message){
        JSONObject jsonObject = null;
        String flag = null;
        try {
            jsonObject = new JSONObject(message);
            flag= jsonObject.getString("FLAG");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(flag!=null){
            if(flag.equals("LOG_PUSH")){
                LogPushVo reMindVo = new Gson().fromJson(message,LogPushVo.class);
                plan_mess.setVisibility(View.VISIBLE);
                plan_time.setVisibility(View.VISIBLE);

                plan_mess.setText(reMindVo.getLOG_CONTENT());

                String logIme =reMindVo.getLOG_TIME();
                int point  = isYesterday(logIme);
                if(point==0){
                    plan_time.setText("今天 "+logIme.substring(logIme.length()-5,logIme.length()));
                }else if(point==1){
                    plan_time.setText(logIme.substring(5));
                }else{
                    plan_time.setText(logIme.substring(5));
                }
            }else if (flag.equals("LOG_STATE")){
                LogStateVo logStateVo = new Gson().fromJson(message,LogStateVo.class);
                log_mess.setVisibility(View.VISIBLE);
                log_time.setVisibility(View.VISIBLE);

                log_mess.setText(logStateVo.getMESSAGE());

                String logIme =logStateVo.getLOG_TIME();
                int point  = isYesterday(logIme);
                if(point==0){
                    log_time.setText("今天 "+logIme.substring(logIme.length()-5,logIme.length()));
                }else if(point==1){
                    log_time.setText(logIme.substring(5));
                }else{
                    log_time.setText(logIme.substring(5));
                }
            }else if(flag.equals("BUSS_PUSH")){
                BussPushVo bussPushVo = new Gson().fromJson(message,BussPushVo.class);
                buss_mess.setVisibility(View.VISIBLE);
                buss_time.setVisibility(View.VISIBLE);

                buss_mess.setText(bussPushVo.getMESSAGE());

                String logIme =bussPushVo.getBUSS_TIME();
                int point  = isYesterday(logIme);
                if(point==0){
                    buss_time.setText("今天 "+logIme.substring(logIme.length()-5,logIme.length()));
                }else if(point==1){
                    buss_time.setText(logIme.substring(5));
                }else{
                    buss_time.setText(logIme.substring(5));
                }
            }else if(flag.equals("PRO_PUSH")){
                ProPushVo proPushVo = new Gson().fromJson(message,ProPushVo.class);
                pro_mess.setVisibility(View.VISIBLE);
                pro_time.setVisibility(View.VISIBLE);

                pro_mess.setText(proPushVo.getMESSAGE());

                String logIme =proPushVo.getPRO_TIME();
                int point  = isYesterday(logIme);
                if(point==0){
                    pro_time.setText("今天 "+logIme.substring(logIme.length()-5,logIme.length()));
                }else if(point==1){
                    pro_time.setText(logIme.substring(5));
                }else{
                    pro_time.setText(logIme.substring(5));
                }
            }
        }

    }
    public int isYesterday(String date){
        int day =0;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d1  = new Date();//当前时间
            Date d2 = sdf.parse(date);//传进的时间
            long cha = d2.getTime() - d1.getTime();
            day = new Long(cha/(1000*60*60*24)).intValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day;
    }


}
