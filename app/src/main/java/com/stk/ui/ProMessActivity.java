package com.stk.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.stk.adapter.ProPushAdapter;
import com.stk.efflog.MyApplication;
import com.stk.efflog.R;
import com.stk.model.ProPushVo;
import com.stk.model.ProjectVo;
import com.stk.utils.URL;
import com.stk.utils.WeiboDialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class ProMessActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView back;
    private ListView remindListView;
    private List<ProPushVo> proPushVoList = new ArrayList<>();
    private ProPushAdapter proPushAdapter ;
    private Dialog mWeiboDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_mess); initView();
        getPushLogState();
        remindListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                querySinglePro(proPushVoList.get(position).getPROJECT_ID());
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(ProMessActivity.this, "请稍后。。。");
            }
        });
    }
    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        remindListView= (ListView) findViewById(R.id.remindListView);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    private void getPushLogState(){
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
                                proPushAdapter = new ProPushAdapter(ProMessActivity.this,proPushVoList);
                                remindListView.setAdapter(proPushAdapter);
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

    private void querySinglePro(String PROJECT_ID){
        OkGo.post(URL.MESS_PRO_SINGLE)
                .tag(this)
                .params("PROJECT_ID", PROJECT_ID)
                .cacheKey("catchKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                        Log.d("lomsmsfnas",s);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(s);
                            if(jsonObject.getBoolean("success")){
                                ProjectVo projectVo = new Gson().fromJson(jsonObject.getString("data"), ProjectVo.class);
                                Intent intent = new Intent(ProMessActivity.this, ProjectActivity.class);
                                Bundle mBundle = new Bundle();
                                mBundle.putSerializable("proList",projectVo);
                                intent.putExtras(mBundle);
                                startActivity(intent);
                            }else{
                                Toast.makeText(ProMessActivity.this, "项目查询失败", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ProMessActivity.this, "项目查询失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                        Toast.makeText(ProMessActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                        super.onError(call, response, e);
                    }
                });
    }
}
