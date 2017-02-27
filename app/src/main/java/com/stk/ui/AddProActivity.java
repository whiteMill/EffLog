package com.stk.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.stk.adapter.JoinAdapter;
import com.stk.efflog.MyApplication;
import com.stk.efflog.R;
import com.stk.model.AllUserVo;
import com.stk.model.ContactUser;
import com.stk.utils.CharacterUtils;
import com.stk.utils.LogListView;
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

public class AddProActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView cancel;
    private TextView save;

    private SimpleDateFormat sdf;
    private EditText bussName;
    //private EditText bussRes;
    private EditText bussDetail;
    private LogListView joinListView;
    private TextView beginTime;
    private TextView endTime;
    private TextView addPeo;
    private TextView deletePeo;

    private TextView joinAmount;
    private TextView joinName;

    private TextView manaAmount;
    private TextView manaName;
    private LogListView manaListView;
    private TextView addManaPeo;
    private TextView deleteManaPeo;


    private final static int REQUEST_CODE = 1321;

    private final static int USER_CODE = 324234;
    private final static int USER_DELETE = 324343234;

    private final static int ADD_MANA = 5454;
    private final static int DELETE_MANA = 3535;

    private List<ContactUser> contactUsers = new ArrayList<>();

    private List<AllUserVo> stuContactVoArrayList= null;

    private List<ContactUser> deleteListUser = new ArrayList<>();

    private List<ContactUser> userArrayList = null;

    private JoinAdapter joinAdapter;

    private List<ContactUser> manaContactUsers = new ArrayList<>();
    private List<ContactUser> manaDeleList = new ArrayList<>();
    private List<ContactUser> manaArrayList = new ArrayList<>();

    TimePickerView begTime;
    TimePickerView endpvTime;

    private StringBuffer joinPeo = new StringBuffer();

    private List<ContactUser> joinList = new ArrayList<>();
    private List<ContactUser> manaList = new ArrayList<>();

    private int flag = 0;

    private SimpleDateFormat timesFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Dialog mWeiboDialog;

    private Intent intent;
    private String buss_name;

    private List<ContactUser> delteJoinList = new ArrayList<>();
    private List<ContactUser> delteManaList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pro);
        initView();
        allUser();
        getCurrentTime();
        intent = getIntent();
        buss_name = intent.getStringExtra("pro_name");
        bussName.setText(buss_name);
    }

    private void initView() {
        manaAmount = (TextView) findViewById(R.id.manaAmount);
        manaName = (TextView) findViewById(R.id.manaName);
        manaListView = (LogListView) findViewById(R.id.manaListView);

        addManaPeo = (TextView) findViewById(R.id.addManaPeo);
        deleteManaPeo = (TextView) findViewById(R.id.deleteManaPeo);

        addManaPeo.setOnClickListener(this);
        deleteManaPeo.setOnClickListener(this);

        joinAmount = (TextView) findViewById(R.id.joinAmount);
        joinName = (TextView) findViewById(R.id.joinName);

        addPeo = (TextView) findViewById(R.id.addPeo);
        deletePeo = (TextView) findViewById(R.id.deletePeo);
        addPeo.setOnClickListener(this);
        deletePeo.setOnClickListener(this);

        cancel = (TextView) findViewById(R.id.cancel);
        save  = (TextView) findViewById(R.id.save);
        beginTime = (TextView) findViewById(R.id.beginTime);
        endTime = (TextView) findViewById(R.id.endTime);

        joinListView = (LogListView) findViewById(R.id.joinListView);

        bussName = (EditText) findViewById(R.id.buss_name);
      //  bussRes = (EditText) findViewById(R.id.buss_resource);
        bussDetail  = (EditText) findViewById(R.id.buss_detail);

        joinName.setText("1---"+ MyApplication.getUserVo().getNAME());
        manaName.setText("1---"+ MyApplication.getUserVo().getNAME());

        beginTime.setOnClickListener(this);
        endTime.setOnClickListener(this);
        cancel.setOnClickListener(this);
        save.setOnClickListener(this);

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
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        beginTime.setText(sdf.format(new Date()));
        endTime.setText(sdf.format(new Date()));
    }


    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }

    private void initDate(){
        userArrayList = new ArrayList<>();
        for(int i=0;i<stuContactVoArrayList.size();i++){
            if(!stuContactVoArrayList.get(i).getUSER_ID().equals(MyApplication.getUserVo().getUSER_ID())){
                ContactUser contactUser = new ContactUser();
                contactUser.setCheck(false);
                contactUser.setFirstCharacter(CharacterUtils.getFirstSpell(stuContactVoArrayList.get(i).getNAME()).toUpperCase());
                contactUser.setPhone(stuContactVoArrayList.get(i).getPHONE());
                contactUser.setUser_id(stuContactVoArrayList.get(i).getUSER_ID());
                contactUser.setUsername(stuContactVoArrayList.get(i).getNAME());
                userArrayList.add(contactUser);
                manaArrayList.add(contactUser);
            }
        }
        Collections.sort(userArrayList, new Comparator<ContactUser>() {
            @Override
            public int compare(ContactUser userOne, ContactUser userTwo) {
                return userOne.getFirstCharacter().compareTo(userTwo.getFirstCharacter());
            }
        });

        Collections.sort(manaArrayList, new Comparator<ContactUser>() {
            @Override
            public int compare(ContactUser userOne, ContactUser userTwo) {
                return userOne.getFirstCharacter().compareTo(userTwo.getFirstCharacter());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==REQUEST_CODE){
            switch (resultCode){
                case USER_CODE:
                    userArrayList = (List<ContactUser>)data.getSerializableExtra("list");
                    contactUsers.clear();
                    delteJoinList.clear();
                    for (int i = 0; i < userArrayList.size(); i++) {
                        if(userArrayList.get(i).getCheck()){
                            contactUsers.add(userArrayList.get(i));
                            delteJoinList.add(userArrayList.get(i));
                        }
                    }
                    joinAmount.setText("项目参与人("+(1+contactUsers.size())+")");
                    joinList.clear();
                    joinList.addAll(contactUsers);
                    joinAdapter = new JoinAdapter(AddProActivity.this,contactUsers);
                    joinListView.setAdapter(joinAdapter);
                    break;
                case USER_DELETE:
                    contactUsers = (List<ContactUser>)data.getSerializableExtra("list");
                    deleteListUser.clear();
                    delteJoinList.clear();
                    for(ContactUser contactUser:contactUsers){
                        if(!contactUser.getCheck()){
                            deleteListUser.add(contactUser);
                            delteJoinList.add(contactUser);
                        }else{
                            for (int i = 0; i < userArrayList.size(); i++) {
                                if(contactUser.getUser_id().equals(userArrayList.get(i).getUser_id())){
                                    userArrayList.get(i).setCheck(false);
                                }
                            }
                        }
                    }
                    joinAmount.setText("项目参与人("+(1+deleteListUser.size())+")");
                    joinList.clear();
                    joinList.addAll(deleteListUser);
                    joinAdapter = new JoinAdapter(AddProActivity.this,deleteListUser);
                    joinListView.setAdapter(joinAdapter);
                    break;
                case ADD_MANA:
                    manaArrayList = (List<ContactUser>)data.getSerializableExtra("list");
                    manaContactUsers.clear();
                    delteManaList.clear();
                    for (int i = 0; i < manaArrayList.size(); i++) {
                        if(manaArrayList.get(i).getCheck()){
                            manaContactUsers.add(manaArrayList.get(i));
                            delteManaList.add(manaArrayList.get(i));
                        }
                    }
                    manaAmount.setText("项目管理员("+(1+manaContactUsers.size())+")");
                    manaList.clear();
                    manaList.addAll(manaContactUsers);

                    joinAdapter = new JoinAdapter(AddProActivity.this,manaContactUsers);
                    manaListView.setAdapter(joinAdapter);
                    break;
                case DELETE_MANA:
                    manaContactUsers = (List<ContactUser>)data.getSerializableExtra("list");
                    manaDeleList.clear();
                    delteManaList.clear();
                    for(ContactUser contactUser:manaContactUsers){
                        if(!contactUser.getCheck()){
                            manaDeleList.add(contactUser);
                            delteManaList.add(contactUser);
                        }else{
                            for (int i = 0; i < manaArrayList.size(); i++) {
                                if(contactUser.getUser_id().equals(manaArrayList.get(i).getUser_id())){
                                    manaArrayList.get(i).setCheck(false);
                                }
                            }
                        }
                    }

                    manaAmount.setText("项目管理员("+(1+manaDeleList.size())+")");
                    manaList.clear();
                    manaList.addAll(manaDeleList);
                    joinAdapter = new JoinAdapter(AddProActivity.this,manaDeleList);
                    manaListView.setAdapter(joinAdapter);
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                finish();
                break;
            case R.id.save:
                if(bussName.getText().toString().trim().isEmpty()){
                    Toast.makeText(this, "请输入项目名称zzz", Toast.LENGTH_SHORT).show();
                }else{
                    mWeiboDialog = WeiboDialogUtils.createLoadingDialog(AddProActivity.this, "正在提交");
                    addBuss();
                }
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

            case R.id.addPeo:
                Intent addIntent = new Intent(AddProActivity.this,AllUserActivity.class);
                addIntent.putExtra("userList", (Serializable)userArrayList);
                addIntent.putExtra("flag","add");
                startActivityForResult(addIntent,REQUEST_CODE);
                break;
            case R.id.deletePeo:
                Intent deleteIntent = new Intent(AddProActivity.this,AllUserActivity.class);
                deleteIntent.putExtra("userList", (Serializable)delteJoinList);
                deleteIntent.putExtra("flag","delete");
                startActivityForResult(deleteIntent,REQUEST_CODE);
                break;

            case R.id.addManaPeo:
                Intent addManaIntent = new Intent(AddProActivity.this,AllUserActivity.class);
                addManaIntent.putExtra("userList", (Serializable)manaArrayList);
                addManaIntent.putExtra("flag","addMana");
                startActivityForResult(addManaIntent,REQUEST_CODE);
                break;

            case R.id.deleteManaPeo:
                Intent deleteManaIntent = new Intent(AddProActivity.this,AllUserActivity.class);
                deleteManaIntent.putExtra("userList", (Serializable)delteManaList);
                deleteManaIntent.putExtra("flag","deleteMana");
                startActivityForResult(deleteManaIntent,REQUEST_CODE);
                break;
        }
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
                        JSONObject jsonObject  = null;
                        try {
                            jsonObject = new JSONObject(s);
                            if(jsonObject.getBoolean("success")){
                                stuContactVoArrayList  = new Gson().fromJson(jsonObject.getString("data"),new TypeToken<List<AllUserVo>>(){}.getType());
                                initDate();
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

    private void addBuss(){
        joinPeo.setLength(0);
        joinPeo.append(MyApplication.getUserVo().getUSER_ID()+"-"+0+"&-&");

        for(ContactUser contactUser:joinList){
            contactUser.setPeo_type("2");
        }
        for(ContactUser contactUser:manaList){
            contactUser.setPeo_type("1");
        }

        for (int i = 0; i < joinList.size(); i++) {
            for (int j = 0; j < manaList.size(); j++) {
                if(joinList.get(i).getUser_id().equals(manaList.get(j).getUser_id())){
                    joinList.get(i).setPeo_type("0");
                    manaList.remove(manaList.get(j));
                    break;
                }
            }
        }

        joinList.addAll(manaList);

        for (int i = 0; i < joinList.size(); i++) {
            joinPeo.append(joinList.get(i).getUser_id()+"-"+joinList.get(i).getPeo_type()+"&-&");
        }
        Log.d("hfduhrbb",joinPeo.toString());
        OkGo.post(URL.ADD_PROJECT)
                .tag(this)
                .params("USER_ID",MyApplication.getUserVo().getUSER_ID())
                .params("PRO_NAME",bussName.getText().toString().trim())
                .params("PRO_INFO",bussDetail.getText().toString().trim())
                .params("PRO_BEGIN_TIME",beginTime.getText().toString())
                .params("PRO_END_TIME",endTime.getText().toString())
                .params("PRO_MANA",joinPeo.toString())
                .params("PRO_TIME",timesFormat.format(new Date()))
                .params("NAME",MyApplication.getUserVo().getNAME())
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
                                Toast.makeText(AddProActivity.this, "项目添加成功", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(AddProActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

    public Date afterTime(Date da){
        long time = 20*60*1000;
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date afterDate = new Date(da.getTime()+time);
        return afterDate;
    }
}
