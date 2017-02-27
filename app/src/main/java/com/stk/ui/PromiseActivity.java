package com.stk.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.stk.efflog.MainActivity;
import com.stk.efflog.MyApplication;
import com.stk.efflog.R;
import com.stk.utils.SharedPreferencesUtils;
import com.stk.utils.URL;
import com.stk.utils.WeiboDialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

public class PromiseActivity extends AppCompatActivity implements View.OnClickListener{


    private EditText editText;
    private Button button;
    private String mPromise;
    private Dialog mWeiboDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_mise);
        mPromise = (String) SharedPreferencesUtils.getParam(PromiseActivity.this,"PROMISE","");
        initView();
    }


    private void initView() {
        editText = (EditText) findViewById(R.id.pro_text);
        if(mPromise.equals("null")){
            editText.setText("");
        }else{
            editText.setText(mPromise);
        }

        button = (Button) findViewById(R.id.sure_promission);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sure_promission:
                if(editText.getText().toString().trim().equals("")){
                    Toast.makeText(this, "请填写承诺内容zzz~", Toast.LENGTH_SHORT).show();
                }else{
                    mWeiboDialog = WeiboDialogUtils.createLoadingDialog(PromiseActivity.this, "正在跳转");
                    addPromise();
                }
                break;
        }
    }


    private void addPromise(){
        OkGo.post(URL.ADD_PROMISE)
                .tag(this)
                .params("USER_ID", MyApplication.getUserVo().getUSER_ID())
                .params("PROMISE",editText.getText().toString())
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
                                SharedPreferencesUtils.setParam(PromiseActivity.this,"PROMISE",editText.getText().toString().trim());
                                Intent intent = new Intent(PromiseActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            //  Toast.makeText(MyPromissActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
