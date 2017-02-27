
package com.stk.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.stk.adapter.SpinerAdapter;
import com.stk.efflog.MyApplication;
import com.stk.efflog.R;
import com.stk.model.AllUserVo;
import com.stk.utils.SpinerPopWindow;
import com.stk.utils.URL;
import com.stk.utils.WeiboDialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class ChooseLogActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView pass_back;
    private List<AllUserVo> stuContactVoArrayList= new ArrayList<>();

    private List<AllUserVo> userVoList = new ArrayList<>();

    private SpinerPopWindow userSpinerPopWindow;
    private SpinerAdapter spinerAdapter;
    private List<String> mListType = new ArrayList<>();
    private EditText search_key;
    private TextView chooseTime;
    private TextView log_type;
    private TextView searchUser;
    private RelativeLayout manaLayout;
    private Button chooseButton;
    private SpinerPopWindow typePopWindow;
    private SpinerAdapter logSpinerAdapter;
    private List<String> logListType = new ArrayList<>();
    private int year, month, day;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Dialog mWeiboDialog;

    private String USER_ID;
    private String LOG_TYPE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_log);
        initView();
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(ChooseLogActivity.this, "请稍后...");
        spinerAdapter = new SpinerAdapter(this, mListType);
        spinerAdapter.refreshData(mListType, 0);
        userSpinerPopWindow = new SpinerPopWindow(this);
        typePopWindow = new SpinerPopWindow(this);
        userSpinerPopWindow.setAdatper(spinerAdapter);
        USER_ID = MyApplication.getUserVo().getUSER_ID();
        LOG_TYPE = "01";

        userSpinerPopWindow.setItemListener(new SpinerAdapter.IOnItemSelectListener() {
            @Override
            public void onItemClick(int pos) {
                searchUser.setText(mListType.get(pos));
                if(MyApplication.getUserVo().getPERMISSION_ID().equals("01")){
                    Log.d("jkojhfjfg",stuContactVoArrayList.get(pos).getUSER_ID());
                    USER_ID = stuContactVoArrayList.get(pos).getUSER_ID();
                }else{
                    Log.d("jkojhfjfg",userVoList.get(pos).getUSER_ID());
                    USER_ID = userVoList.get(pos).getUSER_ID();
                }
            }
        });

        if(MyApplication.getUserVo().getPERMISSION_ID().equals("01")){
            allUser();
        }else{
            allUserByLeader();
        }

        logSpinerAdapter = new SpinerAdapter(this,logListType);
        logSpinerAdapter.refreshData(logListType, 0);
        typePopWindow = new SpinerPopWindow(this);
        typePopWindow.setAdatper(logSpinerAdapter);

        typePopWindow.setItemListener(new SpinerAdapter.IOnItemSelectListener() {
            @Override
            public void onItemClick(int pos) {
                Log.d("jkojhfjfg",pos+"");
                log_type.setText(logListType.get(pos));
                switch (pos){
                    case 0:
                        LOG_TYPE = "01";
                    break;

                    case 1:
                        LOG_TYPE = "02";
                    break;

                    case 2:
                        LOG_TYPE = "03";
                    break;

                    case 3:
                        LOG_TYPE = "04";
                    break;

                    case 4:
                        LOG_TYPE = "05";
                    break;
                }
            }
        });
        getCurrentTime();
    }

    private void initView() {
        pass_back = (ImageView) findViewById(R.id.pass_back);
        manaLayout = (RelativeLayout) findViewById(R.id.manaLayout);
        search_key = (EditText) findViewById(R.id.search_key);
        chooseTime = (TextView) findViewById(R.id.chooseTime);
        log_type = (TextView) findViewById(R.id.log_type);
        searchUser = (TextView) findViewById(R.id.searchUser);
        chooseButton = (Button) findViewById(R.id.chooseButton);
        searchUser.setText(MyApplication.getUserVo().getNAME());

        chooseButton.setOnClickListener(this);
        log_type.setOnClickListener(this);
        chooseTime.setOnClickListener(this);
        pass_back.setOnClickListener(this);
        searchUser.setOnClickListener(this);

        String[] types = getResources().getStringArray(R.array.logType);
        for (int i = 0; i < types.length; i++) {
            logListType.add(types[i]);
        }
    }

    public void showTime() {
        DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String monthStr = "";
                String dayStr = "";
                if(monthOfYear<9){
                    monthStr = "0"+(monthOfYear+1);
                }else{
                    monthStr = (monthOfYear+1)+"";
                }
                if(dayOfMonth<10){
                    dayStr = "0"+dayOfMonth;
                }else{
                    dayStr = dayOfMonth+"";
                }
                chooseTime.setText(year + "-" + monthStr + "-" + dayStr);
            }
        }, year, month - 1, day);
        datePicker.show();
    }

    private void getCurrentTime() {
        Calendar now = Calendar.getInstance();
        year = now.get(Calendar.YEAR);
        month = now.get(Calendar.MONTH) + 1;
        day = now.get(Calendar.DAY_OF_MONTH);
        now.get(Calendar.MINUTE);
        chooseTime.setText(simpleDateFormat.format(new Date()));
    }

    private void showSpinWindow() {
        Log.e("", "showSpinWindow");
        userSpinerPopWindow.setWidth(searchUser.getWidth()/2);
        userSpinerPopWindow.showAsDropDown(searchUser);
    }

    private void showLogSpinWindow() {
        Log.e("", "showSpinWindow");
        typePopWindow.setWidth(log_type.getWidth()/2);
        typePopWindow.showAsDropDown(log_type);
    }

    @Override
    public void onClick(View v) {
         switch (v.getId()){
             case R.id.pass_back:
                 finish();
                 break;
             case R.id.chooseButton:
                 Intent intent = new Intent(ChooseLogActivity.this,SortLogActivity.class);
                 intent.putExtra("user_id",USER_ID);
                 intent.putExtra("log_type",LOG_TYPE);
                 intent.putExtra("log_time",chooseTime.getText().toString());

                 Log.d("jkojhfjfg",chooseTime.getText().toString());

                 if(search_key.getText().toString().trim().toString().equals("")){
                     intent.putExtra("log_key","null");
                 }else{
                     intent.putExtra("log_key",search_key.getText().toString().trim());
                 }
                 startActivity(intent);
                 finish();
                 break;
             case R.id.log_type:
                 showLogSpinWindow();
                 break;
             case R.id.chooseTime:
                 showTime();
                 break;
             case R.id.searchUser:
                 showSpinWindow();
                 break;
         }
    }

    private AllUserVo initUser(){
        AllUserVo allUserVo = new AllUserVo();
        allUserVo.setPHONE(MyApplication.getUserVo().getPHONE());
        allUserVo.setNAME(MyApplication.getUserVo().getNAME());
        allUserVo.setPERMISSION_ID(MyApplication.getUserVo().getPERMISSION_ID());
        allUserVo.setPERMISSION_NAME(MyApplication.getUserVo().getPERMISSION_NAME());
        allUserVo.setROLE_ID(MyApplication.getUserVo().getROLE_ID());
        allUserVo.setROLE_NAME(MyApplication.getUserVo().getROLE_NAME());
        allUserVo.setUSER_ID(MyApplication.getUserVo().getUSER_ID());
        allUserVo.setUSER_NAME(MyApplication.getUserVo().getUSER_NAME());
        return allUserVo;
    }

    private void allUser(){
        OkGo.post(URL.ALL_USER)
                .tag(this)
                .params("USER_ID", MyApplication.getUserVo().getUSER_ID())
                .cacheKey("catchKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                        JSONObject jsonObject  = null;
                        try {
                            jsonObject = new JSONObject(s);
                            if(jsonObject.getBoolean("success")){
                                stuContactVoArrayList  = new Gson().fromJson(jsonObject.getString("data"),new TypeToken<List<AllUserVo>>(){}.getType());
                                for (int i = 0; i < stuContactVoArrayList.size(); i++) {
                                    mListType.add(stuContactVoArrayList.get(i).getNAME());
                                }
                                spinerAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                        super.onError(call, response, e);
                    }
                });
    }

    private void allUserByLeader(){
        OkGo.post(URL.UQERY_USER_BY_LEADER)
                .tag(this)
                .params("USER_ID", MyApplication.getUserVo().getUSER_ID())
                .cacheKey("catchKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                        JSONObject jsonObject  = null;
                        try {
                            jsonObject = new JSONObject(s);
                            if(jsonObject.getBoolean("success")){
                                userVoList  = new Gson().fromJson(jsonObject.getString("data"),new TypeToken<List<AllUserVo>>(){}.getType());
                                userVoList.add(initUser());
                                for (int i = 0; i < userVoList.size(); i++) {
                                    mListType.add(userVoList.get(i).getNAME());
                                }
                                spinerAdapter.notifyDataSetChanged();
                                if(userVoList.size()==1){
                                    manaLayout.setVisibility(View.GONE);
                                }else{
                                    manaLayout.setVisibility(View.VISIBLE);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                        super.onError(call, response, e);
                    }
                });
    }
}
