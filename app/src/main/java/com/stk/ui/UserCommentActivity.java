package com.stk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.stk.efflog.MyApplication;
import com.stk.efflog.R;
import com.stk.utils.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Response;


public class UserCommentActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView back;
    private EditText comment;
    private Button commit;
    private Intent intent;
    private String log_index;
    private String to_id;
    private SimpleDateFormat simpleDateFormat;
    private SimpleDateFormat dateFormat;
    private String to_name;
    private final int LOG_COMMENT =100002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_comment);
        intent = getIntent();
        log_index =  intent.getStringExtra("log_index");
        to_id = intent.getStringExtra("to_id");
        to_name = intent.getStringExtra("to_name");
        initView();
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        comment = (EditText) findViewById(R.id.comment);
        commit = (Button) findViewById(R.id.commit);
        back.setOnClickListener(this);
        commit.setOnClickListener(this);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.commit:
                if(comment.getText().toString().trim().isEmpty()){
                    Toast.makeText(UserCommentActivity.this, "请输入评论内容", Toast.LENGTH_SHORT).show();
                }else{
                    commitComment();
                }

                break;
        }
    }

    private void commitComment(){
        OkGo.post(URL.COMMIT_COMMENT)
                .tag(this)
                .params("LOG_INDEX",log_index)
                .params("COMMENT_CONTENT",comment.getText().toString().trim())
                .params("FROM_ID", MyApplication.getUserVo().getUSER_ID())
                .params("TO_ID",to_id)
                .params("COMMENT_TIME",simpleDateFormat.format(new Date()))
                .params("FROM_NAME",MyApplication.getUserVo().getNAME())
                .params("TO_NAME",to_name)
                .cacheKey("catchKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(s);
                            Boolean isSuccess = jsonObject.getBoolean("success");
                            if(isSuccess){
                                Toast.makeText(UserCommentActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                setResult(LOG_COMMENT);
                                finish();
                            }else{
                                Toast.makeText(UserCommentActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

   /* private void push(){
        OkGo.post(URL.PUSH)
                .tag(this)
                .params("FROM_ID", MyApplication.getUserVo().getUSER_ID())
                .params("TO_ID", to_id)
                .params("FROM_NAME",MyApplication.getUserVo().getNAME())
                .params("TO_NAME",to_name )
                .params("MESS_CONTENT",comment.getText().toString().trim())
                .params("TIME", dateFormat.format(new Date()))
                .params("LOG_INDEX",log_index)
                .cacheKey("catchKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {


                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                    }
                });
    }*/
}
