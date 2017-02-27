package com.stk.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.stk.adapter.JoinAdapter;
import com.stk.efflog.MyApplication;
import com.stk.efflog.R;
import com.stk.model.AllUserVo;
import com.stk.model.BussinessVo;
import com.stk.model.ContactUser;
import com.stk.model.ManaPeo;
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


public class BussEditActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView back;
    private BussinessVo bussinessVo;
    private Intent intent;
    private EditText buss_name;
    private EditText buss_resource;
    private EditText buss_detail;
    private TextView beginTime;
    private TextView endTime;
    private TextView joinName;
    private TextView manaName;

    private Dialog mWeiboDialog;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private SimpleDateFormat timesFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    TimePickerView begTime;
    TimePickerView endpvTime;

    private LogListView joinListView;
    private LogListView manaListView;

    private List<ManaPeo> joinList =null;
    private List<ManaPeo> manaList = null;

    private List<ContactUser> joinContactUserList =new ArrayList<>();
    private List<ContactUser> manaContactUserList =new ArrayList<>();

    private List<ContactUser> deleteListUser = new ArrayList<>();

    private List<AllUserVo> stuContactVoArrayList;

    private List<ContactUser> userArrayList = null;
    private List<ContactUser> userMaArrayList = null;

    private List<ContactUser> manaDeleList = new ArrayList<>();

    private TextView addPeo;
    private TextView deletePeo;
    private TextView addManaPeo;
    private TextView deleteManaPeo;

    private TextView joinAmount;
    private TextView manaAmount;

    private final static int REQUEST_CODE = 1321;
    private final static int USER_CODE = 324234;
    private final static int USER_DELETE = 324343234;
    private final static int ADD_MANA = 5454;
    private final static int DELETE_MANA = 3535;

    private JoinAdapter joinAdapter;

    private List<ContactUser> contactUsers = new ArrayList<>();
    private List<ContactUser> manaContactUsers = new ArrayList<>();

    private Boolean isMana = false;

    private StringBuffer joinPeo = new StringBuffer();
    private TextView update;

    private TextView buss_to_pro;
    private TextView deleteBuss;

    private List<ContactUser> delteJoinList = new ArrayList<>();
    private List<ContactUser> delteManaList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buss_edit);
        initView();
        intent = getIntent();
        bussinessVo = (BussinessVo) getIntent().getSerializableExtra("bussVo");

        stuContactVoArrayList = (List<AllUserVo>) intent.getSerializableExtra("stuContactVoArrayList");

        joinList  = (List<ManaPeo>) intent.getSerializableExtra("joinList");
        manaList  = (List<ManaPeo>) intent.getSerializableExtra("manList");

        buss_name.setText(bussinessVo.getBUSS_NAME());
        buss_resource.setText(bussinessVo.getBUSS_RESOURCE());
        buss_detail.setText(bussinessVo.getBUSS_INFO());
        beginTime.setText(bussinessVo.getBUSS_BEGIN_TIME());
        endTime.setText(bussinessVo.getBUSS_END_TIME());

        joinName.setText("1---"+bussinessVo.getNAME());

        manaName.setText("1---"+bussinessVo.getNAME());

        initDate();
        setData();

        if(!isMana){
            buss_name.setKeyListener(null);
            buss_resource.setKeyListener(null);
            buss_detail.setKeyListener(null);
            beginTime.setEnabled(false);
            endTime.setEnabled(false);
            addPeo.setVisibility(View.INVISIBLE);
            deletePeo.setVisibility(View.INVISIBLE);
            addManaPeo.setVisibility(View.INVISIBLE);
            deleteManaPeo.setVisibility(View.INVISIBLE);
            update.setVisibility(View.INVISIBLE);
            buss_to_pro.setVisibility(View.INVISIBLE);
            deleteBuss.setVisibility(View.INVISIBLE);
        }

        joinListView.setAdapter(new JoinAdapter(BussEditActivity.this,joinContactUserList));
        manaListView.setAdapter(new JoinAdapter(BussEditActivity.this,manaContactUserList));
    }

    private void setData(){
        for (int i = 0; i < joinList.size(); i++) {
            if(!joinList.get(i).getUSER_ID().equals(bussinessVo.getUSER_ID())){
                ContactUser contactUser = new ContactUser();
                contactUser.setCheck(false);
                contactUser.setFirstCharacter(CharacterUtils.getFirstSpell(joinList.get(i).getNAME()).toUpperCase());
                contactUser.setPhone(joinList.get(i).getPHONE());
                contactUser.setUser_id(joinList.get(i).getUSER_ID());
                contactUser.setUsername(joinList.get(i).getNAME());
                joinContactUserList.add(contactUser);
            }

        }

        for (int i = 0; i < manaList.size(); i++) {
            if(!manaList.get(i).getUSER_ID().equals(bussinessVo.getUSER_ID())){
                ContactUser contactUser = new ContactUser();
                contactUser.setCheck(false);
                contactUser.setFirstCharacter(CharacterUtils.getFirstSpell(manaList.get(i).getNAME()).toUpperCase());
                contactUser.setPhone(manaList.get(i).getPHONE());
                contactUser.setUser_id(manaList.get(i).getUSER_ID());
                contactUser.setUsername(manaList.get(i).getNAME());
                manaContactUserList.add(contactUser);
            }
        }

        for (int i = 0; i < manaList.size(); i++) {
            if(MyApplication.getUserVo().getUSER_ID().equals(manaList.get(i).getUSER_ID())){
                isMana = true;
                break;
            }
        }


        joinAmount.setText("商机参与人("+(1+joinContactUserList.size())+")");
        manaAmount.setText("商机管理员("+(1+manaContactUserList.size())+")");

    }

    private void initDate(){
        userArrayList = new ArrayList<>();
        userMaArrayList = new ArrayList<>();
        for(int i=0;i<stuContactVoArrayList.size();i++){
            if(!stuContactVoArrayList.get(i).getUSER_ID().equals(bussinessVo.getUSER_ID())){
                ContactUser contactUser = new ContactUser();
                contactUser.setCheck(false);
                contactUser.setFirstCharacter(CharacterUtils.getFirstSpell(stuContactVoArrayList.get(i).getNAME()).toUpperCase());
                contactUser.setPhone(stuContactVoArrayList.get(i).getPHONE());
                contactUser.setUser_id(stuContactVoArrayList.get(i).getUSER_ID());
                contactUser.setUsername(stuContactVoArrayList.get(i).getNAME());

                ContactUser conMantactUser = new ContactUser();
                conMantactUser.setCheck(false);
                conMantactUser.setFirstCharacter(CharacterUtils.getFirstSpell(stuContactVoArrayList.get(i).getNAME()).toUpperCase());
                conMantactUser.setPhone(stuContactVoArrayList.get(i).getPHONE());
                conMantactUser.setUser_id(stuContactVoArrayList.get(i).getUSER_ID());
                conMantactUser.setUsername(stuContactVoArrayList.get(i).getNAME());

                userArrayList.add(contactUser);
                userMaArrayList.add(conMantactUser);
            }
        }
        Collections.sort(userArrayList, new Comparator<ContactUser>() {
            @Override
            public int compare(ContactUser userOne, ContactUser userTwo) {
                return userOne.getFirstCharacter().compareTo(userTwo.getFirstCharacter());
            }
        });
        Collections.sort(userMaArrayList, new Comparator<ContactUser>() {
            @Override
            public int compare(ContactUser userOne, ContactUser userTwo) {
                return userOne.getFirstCharacter().compareTo(userTwo.getFirstCharacter());
            }
        });

        for (int i = 0; i < userArrayList.size(); i++) {
            for (int j = 0; j < joinList.size(); j++) {
                if(userArrayList.get(i).getUser_id().equals(joinList.get(j).getUSER_ID())){
                    userArrayList.get(i).setCheck(true);
                    break;
                }
            }
        }

        for (int i = 0; i < userMaArrayList.size(); i++) {
            for (int j = 0; j < manaList.size(); j++) {
                if(userMaArrayList.get(i).getUser_id().equals(manaList.get(j).getUSER_ID())){
                    userMaArrayList.get(i).setCheck(true);
                    break;
                }
            }
        }

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
                    joinAmount.setText("商机参与人("+(1+contactUsers.size())+")");
                    joinContactUserList.clear();
                    joinContactUserList.addAll(contactUsers);
                    joinAdapter = new JoinAdapter(BussEditActivity.this,contactUsers);
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
                    joinAmount.setText("商机参与人("+(1+deleteListUser.size())+")");
                    joinContactUserList.clear();
                    joinContactUserList.addAll(deleteListUser);
                    joinAdapter = new JoinAdapter(BussEditActivity.this,deleteListUser);
                    joinListView.setAdapter(joinAdapter);
                    break;

                case ADD_MANA:
                    userMaArrayList = (List<ContactUser>)data.getSerializableExtra("list");
                    manaContactUsers.clear();
                    delteManaList.clear();
                    for (int i = 0; i < userMaArrayList.size(); i++) {
                        if(userMaArrayList.get(i).getCheck()){
                            manaContactUsers.add(userMaArrayList.get(i));
                            delteManaList.add(userArrayList.get(i));
                        }
                    }
                    manaAmount.setText("商机管理员("+(1+manaContactUsers.size())+")");
                    manaContactUserList.clear();
                    manaContactUserList.addAll(manaContactUsers);
                    joinAdapter = new JoinAdapter(BussEditActivity.this,manaContactUsers);
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
                            for (int i = 0; i < userMaArrayList.size(); i++) {
                                if(contactUser.getUser_id().equals(userMaArrayList.get(i).getUser_id())){
                                    userMaArrayList.get(i).setCheck(false);
                                }
                            }
                        }
                    }
                    manaAmount.setText("商机管理员("+(1+manaDeleList.size())+")");
                    manaContactUserList.clear();
                    manaContactUserList.addAll(manaDeleList);
                    joinAdapter = new JoinAdapter(BussEditActivity.this,manaDeleList);
                    manaListView.setAdapter(joinAdapter);
                    break;
            }
        }


    }

    private void initView() {
        buss_to_pro = (TextView) findViewById(R.id.buss_to_pro);
        deleteBuss = (TextView) findViewById(R.id.deleteBuss);

        buss_to_pro.setOnClickListener(this);
        deleteBuss.setOnClickListener(this);

        update = (TextView) findViewById(R.id.save);

        joinAmount = (TextView) findViewById(R.id.joinAmount);
        manaAmount = (TextView) findViewById(R.id.manaAmount);

        addPeo = (TextView) findViewById(R.id.addPeo);
        deletePeo = (TextView) findViewById(R.id.deletePeo);
        addPeo.setOnClickListener(this);
        deletePeo.setOnClickListener(this);

        addManaPeo = (TextView) findViewById(R.id.addManaPeo);
        deleteManaPeo = (TextView) findViewById(R.id.deleteManaPeo);
        addManaPeo.setOnClickListener(this);
        deleteManaPeo.setOnClickListener(this);

        joinName = (TextView) findViewById(R.id.joinName);
        manaName = (TextView) findViewById(R.id.manaName);
        back = (ImageView) findViewById(R.id.back);
        buss_name = (EditText) findViewById(R.id.buss_name);
        buss_resource = (EditText) findViewById(R.id.buss_resource);
        buss_detail = (EditText) findViewById(R.id.buss_detail);
        beginTime = (TextView) findViewById(R.id.beginTime);
        endTime = (TextView) findViewById(R.id.endTime);

        joinListView = (LogListView) findViewById(R.id.joinListView);
        manaListView = (LogListView) findViewById(R.id.manaListView);

        back.setOnClickListener(this);
        beginTime.setOnClickListener(this);
        endTime.setOnClickListener(this);
        update.setOnClickListener(this);

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


    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.save:
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(BussEditActivity.this, "正在更新...");
                updateBuss();
                break;
            case R.id.deleteBuss:
                    mWeiboDialog = WeiboDialogUtils.createLoadingDialog(BussEditActivity.this, "正在删除...");
                    delteBuss();
                break;
            case R.id.buss_to_pro:
                    mWeiboDialog = WeiboDialogUtils.createLoadingDialog(BussEditActivity.this, "正在操作...");
                    bussToPro();
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
                Intent addIntent = new Intent(BussEditActivity.this,AllUserActivity.class);
                addIntent.putExtra("userList", (Serializable)userArrayList);
                addIntent.putExtra("flag","add");
                startActivityForResult(addIntent,REQUEST_CODE);
                break;

            case R.id.deletePeo:
                Intent deleteIntent = new Intent(BussEditActivity.this,AllUserActivity.class);

                if(joinContactUserList.size()!=0){
                    deleteIntent.putExtra("userList", (Serializable)joinContactUserList);
                }else{
                    deleteIntent.putExtra("userList", (Serializable)delteJoinList);
                }

                deleteIntent.putExtra("flag","delete");
                startActivityForResult(deleteIntent,REQUEST_CODE);
                break;

            case R.id.addManaPeo:
                Intent addManaIntent = new Intent(BussEditActivity.this,AllUserActivity.class);
                addManaIntent.putExtra("userList", (Serializable)userMaArrayList);
                addManaIntent.putExtra("flag","addMana");
                startActivityForResult(addManaIntent,REQUEST_CODE);
                break;

            case R.id.deleteManaPeo:
                Intent deleteManaIntent = new Intent(BussEditActivity.this,AllUserActivity.class);

                if(manaContactUserList.size()!=0){
                    deleteManaIntent.putExtra("userList", (Serializable)manaContactUserList);
                }else{
                    deleteManaIntent.putExtra("userList", (Serializable)delteManaList);
                }
                deleteManaIntent.putExtra("flag","deleteMana");
                startActivityForResult(deleteManaIntent,REQUEST_CODE);
                break;
        }
    }

    private void updateBuss(){
        joinPeo.setLength(0);
        joinPeo.append(bussinessVo.getUSER_ID()+"-"+0+"&-&");

        for(ContactUser contactUser:joinContactUserList){
            contactUser.setPeo_type("2");
        }
        for(ContactUser contactUser:manaContactUserList){
            contactUser.setPeo_type("1");
        }

        for (int i = 0; i < joinContactUserList.size(); i++) {
            for (int j = 0; j < manaContactUserList.size(); j++) {
                if(joinContactUserList.get(i).getUser_id().equals(manaContactUserList.get(j).getUser_id())){
                    joinContactUserList.get(i).setPeo_type("0");
                    manaContactUserList.remove(manaContactUserList.get(j));
                    break;
                }
            }
        }

        joinContactUserList.addAll(manaContactUserList);

        for (int i = 0; i < joinContactUserList.size(); i++) {
            joinPeo.append(joinContactUserList.get(i).getUser_id()+"-"+joinContactUserList.get(i).getPeo_type()+"&-&");
        }
        Log.d("hfssssdererfduhrbb",joinPeo.toString());
        OkGo.post(URL.UPDATE_BUSS)
                .tag(this)
                .params("BUSS_ID",bussinessVo.getBUSS_ID())
                .params("BUSS_NAME",buss_name.getText().toString().trim())
                .params("BUSS_RESOURCE",buss_resource.getText().toString().trim())
                .params("BUSS_INFO",buss_detail.getText().toString().trim())
                .params("BUSS_BEGIN_TIME",beginTime.getText().toString())
                .params("BUSS_END_TIME",endTime.getText().toString())
                .params("BUSS_MANA",joinPeo.toString())
                .params("BUSS_TIME",timesFormat.format(new Date()))
                .params("USER_ID",MyApplication.getUserVo().getUSER_ID())
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
                                Toast.makeText(BussEditActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(BussEditActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                        super.onError(call, response, e);
                        Toast.makeText(BussEditActivity.this, "网路请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void delteBuss(){
        joinPeo.setLength(0);
        joinPeo.append(bussinessVo.getUSER_ID()+"&-&");

        for(ContactUser contactUser:joinContactUserList){
            contactUser.setPeo_type("2");
        }
        for(ContactUser contactUser:manaContactUserList){
            contactUser.setPeo_type("1");
        }

        for (int i = 0; i < joinContactUserList.size(); i++) {
            for (int j = 0; j < manaContactUserList.size(); j++) {
                if(joinContactUserList.get(i).getUser_id().equals(manaContactUserList.get(j).getUser_id())){
                    joinContactUserList.get(i).setPeo_type("0");
                    manaContactUserList.remove(manaContactUserList.get(j));
                    break;
                }
            }
        }

        joinContactUserList.addAll(manaContactUserList);

        for (int i = 0; i < joinContactUserList.size(); i++) {
            joinPeo.append(joinContactUserList.get(i).getUser_id()+"&-&");
        }



        OkGo.post(URL.DELETE_BUSS)
                .tag(this)
                .params("BUSS_ID",bussinessVo.getBUSS_ID())
                .params("USER_ID",MyApplication.getUserVo().getUSER_ID())
                .params("BUSS_NAME",buss_name.getText().toString())
                .params("BUSS_MANA",joinPeo.toString())
                .params("NAME",MyApplication.getUserVo().getNAME())
                .cacheKey("catchKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                        JSONObject  jsonObject = null;
                        try {
                            jsonObject = new JSONObject(s);
                            if(jsonObject.getBoolean("success")){
                                Toast.makeText(BussEditActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(BussEditActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(BussEditActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                        super.onError(call, response, e);
                        Toast.makeText(BussEditActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void bussToPro(){

        joinPeo.setLength(0);
        joinPeo.append(bussinessVo.getUSER_ID()+"&-&");

        for(ContactUser contactUser:joinContactUserList){
            contactUser.setPeo_type("2");
        }
        for(ContactUser contactUser:manaContactUserList){
            contactUser.setPeo_type("1");
        }

        for (int i = 0; i < joinContactUserList.size(); i++) {
            for (int j = 0; j < manaContactUserList.size(); j++) {
                if(joinContactUserList.get(i).getUser_id().equals(manaContactUserList.get(j).getUser_id())){
                    joinContactUserList.get(i).setPeo_type("0");
                    manaContactUserList.remove(manaContactUserList.get(j));
                    break;
                }
            }
        }

        joinContactUserList.addAll(manaContactUserList);

        for (int i = 0; i < joinContactUserList.size(); i++) {
            joinPeo.append(joinContactUserList.get(i).getUser_id()+"&-&");
        }

        OkGo.post(URL.BUSS_TO_PRO)
                .tag(this)
                .params("BUSS_ID",bussinessVo.getBUSS_ID())
                .params("USER_ID",MyApplication.getUserVo().getUSER_ID())
                .params("BUSS_NAME",buss_name.getText().toString())
                .params("BUSS_MANA",joinPeo.toString())
                .params("NAME",MyApplication.getUserVo().getNAME())
                .cacheKey("catchKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                        JSONObject  jsonObject = null;
                        try {
                            jsonObject = new JSONObject(s);
                            if(jsonObject.getBoolean("success")){
                                Toast.makeText(BussEditActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                                Intent intent  = new Intent(BussEditActivity.this,AddProActivity.class);
                                intent.putExtra("pro_name",buss_name.getText().toString());
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(BussEditActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(BussEditActivity.this, "转项目失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                        super.onError(call, response, e);
                        Toast.makeText(BussEditActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
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
