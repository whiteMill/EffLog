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
import com.stk.adapter.BussPushAdapter;
import com.stk.efflog.MyApplication;
import com.stk.efflog.R;
import com.stk.model.BussPushVo;
import com.stk.model.BussinessVo;
import com.stk.utils.URL;
import com.stk.utils.WeiboDialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class BussMessActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView back;
    private ListView remindListView;
    private List<BussPushVo> bussPushVoList = new ArrayList<>();
    private BussPushAdapter bussPushAdapter ;
    private Dialog mWeiboDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buss_mess);
        initView();
        getPushLogState();
        remindListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                querySingleBuss(bussPushVoList.get(position).getBUSS_ID());
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(BussMessActivity.this, "请稍后。。。");
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
                                bussPushAdapter = new BussPushAdapter(BussMessActivity.this,bussPushVoList);
                                remindListView.setAdapter(bussPushAdapter);
                            }else{
                                Toast.makeText(BussMessActivity.this, "数据查询失败", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(BussMessActivity.this, "数据查询失败", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Toast.makeText(BussMessActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                        super.onError(call, response, e);
                    }
                });
    }


    private void querySingleBuss(String BUSS_ID){
        OkGo.post(URL.MESS_BUSS_SINGLE)
                .tag(this)
                .params("BUSS_ID", BUSS_ID)
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
                                BussinessVo bussinessVo = new Gson().fromJson(jsonObject.getString("data"), BussinessVo.class);
                                Intent intent = new Intent(BussMessActivity.this, BussinessActivity.class);
                                Bundle mBundle = new Bundle();
                                mBundle.putSerializable("bussList",bussinessVo);
                                intent.putExtras(mBundle);
                                startActivity(intent);
                            }else{
                                Toast.makeText(BussMessActivity.this, "商机查询失败", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(BussMessActivity.this, "商机查询失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                        Toast.makeText(BussMessActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                        super.onError(call, response, e);
                    }
                });
    }

}
