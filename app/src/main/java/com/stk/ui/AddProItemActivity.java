package com.stk.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.stk.efflog.MyApplication;
import com.stk.efflog.R;
import com.stk.model.ManPeoContact;
import com.stk.model.ManaPeo;
import com.stk.model.ProInfoVo;
import com.stk.utils.CharacterUtils;
import com.stk.utils.URL;
import com.stk.utils.WeiboDialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class AddProItemActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView cancel;
    private TextView save;
    private EditText itemEdit;
    private TextView pro_name;
    private RelativeLayout pro_mana;
    private List<ManaPeo> manaPeoList = null;
    private List<ManaPeo>  manList = null;
    private Intent intent;
    private int flag = 0;
    private List<ManPeoContact> manPeoContactList = new ArrayList<>();
    private final static int REQUEST_CODE = 189;
    private final static  int RESULT_CODE = 8569;
    TimePickerView begTime;
    TimePickerView endpvTime;
    private TextView beginTime;
    private TextView endTime;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private SimpleDateFormat timesFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String PROJECT_ID;
    private String userId;
    private String userName;

    private Dialog mWeiboDialog;

    private String point;

    private String position;
    private List<ProInfoVo> proInfoVoList;

    private ProInfoVo proInfoVo;
    private RelativeLayout info_flag_layout;
    private RelativeLayout marker_layout;
    private TextView isComplete;
    private TextView isMarker;

    private int fff=0;
    private int ggg=0;

    private String isFinish;
    private String projectName;

    private StringBuffer stringBuffer =new StringBuffer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pro_item);
        initView();
        intent = getIntent();
        manaPeoList = (List<ManaPeo>) intent.getSerializableExtra("manaPeoList");
        manList = (List<ManaPeo>) intent.getSerializableExtra("manList");
        PROJECT_ID = intent.getStringExtra("PROJECT_ID");
        projectName = intent.getStringExtra("PROJECT_NAME");
        point = intent.getStringExtra("FLAG");
        if(point.equals("ADD")){
            getCurrentTime();
        }else if(point.equals("UPDATE")){
            save.setText("更新");
            position = intent.getStringExtra("POSITION");
            proInfoVoList = (List<ProInfoVo>) intent.getSerializableExtra("list");
            proInfoVo = proInfoVoList.get(Integer.valueOf(position));
            itemEdit.setText(proInfoVo.getITEM_MESS());
            beginTime.setText(proInfoVo.getITEM_BEGIN_TIME());
            endTime.setText(proInfoVo.getITEM_END_TIME());
            info_flag_layout.setVisibility(View.VISIBLE);
            if(proInfoVo.getITEM_FLAG().equals("0")){
                isComplete.setText("已完成");
            }else{
                isComplete.setText("未完成");
            }
            if(proInfoVo.getITEM_MARKER().equals("0")){
                isMarker.setText("里程碑条目");
            }else{
                isMarker.setText("非里程碑条目");
            }
        }
        initDate();
        initUser();
    }

    private void initDate(){
        for (int i = 0; i < manList.size(); i++) {
            if(manList.get(i).getUSER_ID().equals(MyApplication.getUserVo().getUSER_ID())){
                flag = 1;
                break;
            }
        }
        if(flag == 0){
            pro_name.setText(MyApplication.getUserVo().getNAME());
            userId = MyApplication.getUserVo().getUSER_ID();
            if(point.equals("UPDATE")){
                pro_name.setText(proInfoVo.getNAME());
                userId = proInfoVo.getUSER_ID();
            }
        }else{
            if(point.equals("UPDATE")){
                pro_name.setText(proInfoVo.getNAME());
                userId = proInfoVo.getUSER_ID();
            }
            pro_mana.setOnClickListener(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode  ==REQUEST_CODE){
            switch (resultCode){
                case  RESULT_CODE:
                    userId =  data.getStringExtra("USER_ID");
                    userName = data.getStringExtra("USRE_NAME");
                    pro_name.setText(userName);
                    break;
            }
        }
    }

    private void initView() {
        isMarker = (TextView) findViewById(R.id.isMarker);
        marker_layout = (RelativeLayout) findViewById(R.id.marker_layout);
        isComplete = (TextView) findViewById(R.id.isComplete);
        info_flag_layout = (RelativeLayout) findViewById(R.id.info_flag_layout);
        beginTime = (TextView) findViewById(R.id.beginTime);
        endTime = (TextView) findViewById(R.id.endTime);
        pro_mana  = (RelativeLayout) findViewById(R.id.buss_mana);
        pro_name  = (TextView) findViewById(R.id.buss_name);
        cancel = (TextView) findViewById(R.id.cancel);
        save = (TextView) findViewById(R.id.save);
        itemEdit = (EditText) findViewById(R.id.itemEdit);
        info_flag_layout.setOnClickListener(this);
        cancel.setOnClickListener(this);
        save.setOnClickListener(this);
        beginTime.setOnClickListener(this);
        endTime.setOnClickListener(this);
        marker_layout.setOnClickListener(this);
        begTime = new TimePickerView(this, TimePickerView.Type.ALL);
        begTime.setTime(new Date());
        begTime.setCyclic(true);
        begTime.setCancelable(true);
        begTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                beginTime.setText(getTime(date));
                endTime.setText(getTime(afterTime(date)));
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
                endTime.setText(getTime(date));
            }
        });
    }

    private void getCurrentTime() {
        beginTime.setText(sdf.format(new Date()));
        endTime.setText(sdf.format(new Date()));
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                finish();
                break;
            case R.id.save:
                if(itemEdit.getText().toString().trim().isEmpty()||pro_name.getText().toString().isEmpty()){
                    Toast.makeText(this, "请输入相关信息zzz", Toast.LENGTH_SHORT).show();
                }else{
                    if(point.equals("ADD")){
                        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(AddProItemActivity.this, "提交中...");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                addProItem();
                            }
                        },500);
                    }else{
                        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(AddProItemActivity.this, "更新中...");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                updatePro();
                            }
                        },500);

                    }


                }
                break;
            case R.id.buss_mana:
                Intent intent = new Intent(AddProItemActivity.this,SingleUserActivity.class);
                intent.putExtra("list", (Serializable) manPeoContactList);
                startActivityForResult(intent,REQUEST_CODE);
                break;
            case R.id.beginTime:
                try {
                    begTime.setTime(sdf.parse(beginTime.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                begTime.show();
                break;

            case R.id.endTime:
                try {
                    endpvTime.setTime(sdf.parse(endTime.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                endpvTime.show();
                break;
            case R.id.info_flag_layout:
                if(proInfoVo.getITEM_FLAG().equals("1")){
                    if(fff==0){
                        isComplete.setText("已完成");
                        fff=1;
                    }else{
                        isComplete.setText("未完成");
                        fff=0;
                    }
                }else{
                    if(fff==0){
                        isComplete.setText("未完成");
                        fff=1;
                    }else{
                        isComplete.setText("已完成");
                        fff=0;
                    }
                }
                break;
            case R.id.marker_layout:
                if(point.equals("ADD")){
                    if(ggg==0){
                        isMarker.setText("里程碑条目");
                        ggg=1;
                    }else{
                        isMarker.setText("非里程碑条目");
                        ggg=0;
                    }
                }else{
                    if(proInfoVo.getITEM_MARKER().equals("1")){
                        if(ggg==0){
                            isMarker.setText("里程碑条目");
                            ggg=1;
                        }else{
                            isMarker.setText("非里程碑条目");
                            ggg=0;
                        }
                    }else{
                        if(ggg==0){
                            isMarker.setText("非里程碑条目");
                            ggg=1;
                        }else{
                            isMarker.setText("里程碑条目");
                            ggg=0;
                        }
                    }
                }

                break;
        }
    }

    private void addProItem(){
        stringBuffer.setLength(0);
        for (int i = 0; i < manaPeoList.size(); i++) {
            stringBuffer.append(manaPeoList.get(i).getUSER_ID()+"&-&");
        }

        OkGo.post(URL.ADD_PRO_INFO)
                .tag(this)
                .params("PROJECT_ID",PROJECT_ID)
                .params("USER_ID",userId)
                .params("ITEM_MESS",itemEdit.getText().toString().trim())
                .params("ITEM_TIME",timesFormat.format(new Date()))
                .params("ITEM_BEGIN_TIME",beginTime.getText().toString())
                .params("ITEM_END_TIME",endTime.getText().toString())
                .params("ITEM_MARKER",isMarker.getText().toString().equals("里程碑条目")?"0":"1")
                .params("PRO_NAME",projectName)
                .params("PRO_MANA",stringBuffer.toString())
                .params("NAME",MyApplication.getUserVo().getNAME())
                .params("MESS_ID",MyApplication.getUserVo().getUSER_ID())
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(s);
                            if(jsonObject.getBoolean("success")){
                                finish();
                            }else{
                                Toast.makeText(AddProItemActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AddProItemActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                        super.onError(call, response, e);
                    }
                });



    }

    private void initUser(){
        for(int i=0;i<manaPeoList.size();i++){
            ManPeoContact manPeoContact = new ManPeoContact();
            manPeoContact.setChecked(false);
            manPeoContact.setFirstCharacter(CharacterUtils.getFirstSpell(manaPeoList.get(i).getNAME()).toUpperCase());
            manPeoContact.setNAME(manaPeoList.get(i).getNAME());
            manPeoContact.setUSER_ID(manaPeoList.get(i).getUSER_ID());
            manPeoContactList.add(manPeoContact);

        }
        Collections.sort(manPeoContactList, new Comparator<ManPeoContact>() {
            @Override
            public int compare(ManPeoContact userOne, ManPeoContact userTwo) {
                return userOne.getFirstCharacter().compareTo(userTwo.getFirstCharacter());
            }
        });

    }

    private void updatePro(){
        if(isComplete.getText().toString().equals("未完成")){
            isFinish = "1";
        }else{
            isFinish = "0";
        }

        stringBuffer.setLength(0);
        for (int i = 0; i < manaPeoList.size(); i++) {
            stringBuffer.append(manaPeoList.get(i).getUSER_ID()+"&-&");
        }

        OkGo.post(URL.UPDATE_PRO_ITEM)
                .tag(this)
                .params("PRO_ITEM_ID",proInfoVo.getPRO_ITEM_ID())
                .params("ITEM_MESS",itemEdit.getText().toString().trim())
                .params("ITEM_TIME",timesFormat.format(new Date()))
                .params("USER_ID",userId)
                .params("ITEM_FLAG",isFinish)
                .params("ITEM_BEGIN_TIME",beginTime.getText().toString())
                .params("ITEM_END_TIME",endTime.getText().toString())
                .params("ITEM_MARKER",isMarker.getText().toString().equals("里程碑条目")?"0":"1")
                .params("NAME",MyApplication.getUserVo().getNAME())
                .params("PRO_NAME",projectName)
                .params("PRO_ID",PROJECT_ID)
                .params("MESS_ID",MyApplication.getUserVo().getUSER_ID())
                .params("PRO_MANA",stringBuffer.toString())
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(s);
                            if(jsonObject.getBoolean("success")){
                                Toast.makeText(AddProItemActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(AddProItemActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AddProItemActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                        Toast.makeText(AddProItemActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public Date afterTime(Date da){
        long time = 20*60*1000;
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date afterDate = new Date(da.getTime()+time);
        return afterDate;
    }

}
