package com.stk.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.stk.efflog.MyApplication;
import com.stk.efflog.R;
import com.stk.model.UserVo;
import com.stk.utils.SharedPreferencesUtils;
import com.stk.utils.URL;
import com.stk.utils.WeiboDialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.Call;
import okhttp3.Response;

import static com.stk.efflog.MyApplication.userVo;
import static com.stk.utils.SharedPreferencesUtils.getParam;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText userName;
    private EditText password;
    private Button login;
    private String  user_name;
    private String user_password;
    private String old_user;
    private Dialog mWeiboDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        initView();
        String default_mess= (String) getParam(LoginActivity.this,"USER_MESS","");
        String default_password = (String) getParam(LoginActivity.this,"USER_PASSWORD","");
        userName.setText(default_mess);
        password.setText(default_password);
    }

    private void initView(){
        old_user  = (String) getParam(getApplicationContext(),"USER_ID","");
        userName = (EditText) findViewById(R.id.user_name);
        password = (EditText) findViewById(R.id.user_password);
        login = (Button) findViewById(R.id.loginBtn);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginBtn:
                user_name = userName.getText().toString().trim();
                user_password = password.getText().toString().trim();
                if(user_name.isEmpty()||user_password.isEmpty()){
                    Toast.makeText(this,"请把信息填写完整",Toast.LENGTH_SHORT).show();
                }else{
                    mWeiboDialog = WeiboDialogUtils.createLoadingDialog(LoginActivity.this, "正在登陆");
                    login();
                }
                break;
        }
    }

    private void login(){
        OkGo.post(URL.LOGIN)
                .tag(this)
                .params("USER_MESS",user_name)
                .params("PASSWORD",user_password)
                .cacheKey("catchKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(s);
                            Boolean isFlag = jsonObject.getBoolean("success");
                            if(isFlag){
                                userVo = new Gson().fromJson(jsonObject.getString("data"), UserVo.class);
                                SharedPreferencesUtils.setParam(LoginActivity.this,"USER_ID",userVo.getUSER_ID());
                                SharedPreferencesUtils.setParam(LoginActivity.this,"USER_NAME",userVo.getUSER_NAME());
                                SharedPreferencesUtils.setParam(LoginActivity.this,"NAME",userVo.getNAME());
                                SharedPreferencesUtils.setParam(LoginActivity.this,"ROLE_ID",userVo.getROLE_ID());
                                SharedPreferencesUtils.setParam(LoginActivity.this,"PHONE",userVo.getPHONE());
                                SharedPreferencesUtils.setParam(LoginActivity.this,"STATUS",userVo.getSTATUS());
                                SharedPreferencesUtils.setParam(LoginActivity.this,"ROLE_NAME",userVo.getROLE_NAME());
                                SharedPreferencesUtils.setParam(LoginActivity.this,"PERMISSION_ID",userVo.getPERMISSION_ID());
                                SharedPreferencesUtils.setParam(LoginActivity.this,"PERMISSION_NAME",userVo.getPERMISSION_NAME());
                                SharedPreferencesUtils.setParam(LoginActivity.this,"PROMISE",userVo.getPROMISE());
                                SharedPreferencesUtils.setParam(LoginActivity.this,"LEADER_ID",userVo.getLEADER_ID());
                                MyApplication.setUserVo(userVo);
                                setAlias();
                            }else{
                                WeiboDialogUtils.closeDialog(mWeiboDialog);
                                Toast.makeText(LoginActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            WeiboDialogUtils.closeDialog(mWeiboDialog);
                            Toast.makeText(LoginActivity.this,"网络请求失败~",Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                        super.onError(call, response, e);
                        Toast.makeText(LoginActivity.this,"服务器请求失败~",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void setAlias(){
        int alias_flag = (int) getParam(getApplicationContext(),"alies",0);
        if(alias_flag == 1&&old_user.equals(userVo.getUSER_ID())){
            WeiboDialogUtils.closeDialog(mWeiboDialog);
            Intent intent =new Intent(LoginActivity.this, PromiseActivity.class);
            startActivity(intent);
            SharedPreferencesUtils.setParam(LoginActivity.this,"USER_MESS",user_name);
            SharedPreferencesUtils.setParam(LoginActivity.this,"USER_PASSWORD",user_password);
            finish();
        }else{
            JPushInterface.setAlias(getApplicationContext(), MyApplication.getUserVo().getUSER_ID(), new TagAliasCallback() {
                @Override
                public void gotResult(int i, String s, Set<String> set) {
                    if(i==0){
                        SharedPreferencesUtils.setParam(getApplicationContext(),"alies",1);
                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                        Intent intent =new Intent(LoginActivity.this, PromiseActivity.class);
                        startActivity(intent);
                        SharedPreferencesUtils.setParam(LoginActivity.this,"USER_MESS",user_name);
                        SharedPreferencesUtils.setParam(LoginActivity.this,"USER_PASSWORD",user_password);
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this,"网络请求超时,请重新登陆！",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }



    }
}
