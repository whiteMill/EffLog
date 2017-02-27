package com.stk.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.stk.adapter.BussInfoAdapter;
import com.stk.efflog.MyApplication;
import com.stk.efflog.R;
import com.stk.model.AllUserVo;
import com.stk.model.BussInfoVo;
import com.stk.model.BussinessVo;
import com.stk.model.ManaPeo;
import com.stk.utils.URL;
import com.stk.utils.WeiboDialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class BussinessActivity extends AppCompatActivity  implements View.OnClickListener{

    private Intent intent;
    private String buss_name;
    private String buss_id;
    private ImageView back;
    private TextView bussName;
    private TextView bussEdit;

    private List<ManaPeo> manaPeoList;
    private List<ManaPeo> joinList = new ArrayList<>();
    private List<ManaPeo> manList = new ArrayList<>();
    private TextView add_item;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<BussInfoVo> bussInfoVoList;

    private List<BussInfoVo> comPleteList = new ArrayList<>();
    private List<BussInfoVo> unComPleteList = new ArrayList<>();

    private List<BussInfoVo> bussInfoList = new ArrayList<>();

    private RelativeLayout in_buss_layout;
    private RelativeLayout out_buss_layout;
    private TextView unComInfo;
    private TextView compleInfo;
    private ImageView unComInfoIm;
    private ImageView compleInfoIm;

    private ListView bussInfoListView;
    private BussInfoAdapter bussInfoAdapter;

    private int one_flag = 0;
    private int two_flag = 1;

    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    private Dialog mWeiboDialog;

    private BussinessVo bussinessVo;

    private List<AllUserVo> stuContactVoArrayList= null;
    private StringBuffer BUSS_MANA = new StringBuffer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buss_item);
        initView();
        intent = getIntent();
        bussinessVo = (BussinessVo) getIntent().getSerializableExtra("bussList");
        buss_name = bussinessVo.getBUSS_NAME();
        buss_id = bussinessVo.getBUSS_ID();
        manaPeoList = bussinessVo.getMANA_PEO();

        initDate();
        allUser();
        bussName.setText(buss_name);
        swipeRefreshLayout.setRefreshing(true);
        bussInfoAdapter = new BussInfoAdapter(BussinessActivity.this,bussInfoList);
        bussInfoListView.setAdapter(bussInfoAdapter);
        QueryBussInfo();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        QueryBussInfo();
                    }
                },1500);
            }
        });
        bussInfoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BussinessActivity.this,AddBussItemActivity.class);
                intent.putExtra("FLAG","UPDATE");
                intent.putExtra("manaPeoList", (Serializable) manaPeoList);
                intent.putExtra("manList", (Serializable) manList);
                intent.putExtra("BUSS_ID",buss_id);
                Log.d("sfdhffffffffffffff",buss_name);
                intent.putExtra("BUSS_NAME",buss_name);
                intent.putExtra("POSITION",position+"");
                intent.putExtra("list", (Serializable) bussInfoList);
                startActivity(intent);


            }
        });
        bussInfoListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                builder = new AlertDialog.Builder(BussinessActivity.this);
                builder.setItems(getResources().getStringArray(R.array.bussItem), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        switch (arg1) {
                            case 0:
                                alertDialog = new AlertDialog.Builder(BussinessActivity.this).setTitle("提醒").setMessage("确定要删除吗？")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                alertDialog.dismiss();
                                                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(BussinessActivity.this, "正在删除...");
                                                delteBussInfo(bussInfoList.get(position).getBUSS_INFO_ID());

                                            }
                                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                return;
                                            }
                                        }).create(); // 创建对话框
                                alertDialog.show(); // 显示对话框
                                break;
                            case 1:
                               Intent intent = new Intent(BussinessActivity.this,SetRemindActivity.class);
                                intent.putExtra("MESSAGE",bussInfoList.get(position).getBUSS_MESS());
                                intent.putExtra("BUSS_ID",buss_id);
                                intent.putExtra("BUSS_NAME",buss_name);
                                intent.putExtra("BUSS_INFO_ID",bussInfoList.get(position).getBUSS_INFO_ID());
                                startActivity(intent);
                                break;
                        }
                        arg0.dismiss();
                    }
                });
                builder.show();
                return true;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        swipeRefreshLayout.setRefreshing(true);
        QueryBussInfo();
    }

    private void  initDate(){
        for (int i = 0; i < manaPeoList.size(); i++) {
            if(manaPeoList.get(i).getPEO_TYPE().equals("0")){
                ManaPeo joinPeo = new ManaPeo();
                joinPeo.setPEO_TYPE(manaPeoList.get(i).getPEO_TYPE());
                joinPeo.setUSER_ID(manaPeoList.get(i).getUSER_ID());
                joinPeo.setNAME(manaPeoList.get(i).getNAME());
                joinPeo.setPHONE(manaPeoList.get(i).getPHONE());

                ManaPeo manaPeo = new ManaPeo();
                manaPeo.setPEO_TYPE(manaPeoList.get(i).getPEO_TYPE());
                manaPeo.setUSER_ID(manaPeoList.get(i).getUSER_ID());
                manaPeo.setNAME(manaPeoList.get(i).getNAME());
                manaPeo.setPHONE(manaPeoList.get(i).getPHONE());

                joinList.add(joinPeo);
                manList.add(manaPeo);
            }else if(manaPeoList.get(i).getPEO_TYPE().equals("2")){
                ManaPeo joinPeoJ = new ManaPeo();
                joinPeoJ.setPEO_TYPE(manaPeoList.get(i).getPEO_TYPE());
                joinPeoJ.setUSER_ID(manaPeoList.get(i).getUSER_ID());
                joinPeoJ.setNAME(manaPeoList.get(i).getNAME());
                joinPeoJ.setPHONE(manaPeoList.get(i).getPHONE());
                joinList.add(joinPeoJ);
            }else{
                ManaPeo manaPeoM = new ManaPeo();
                manaPeoM.setPEO_TYPE(manaPeoList.get(i).getPEO_TYPE());
                manaPeoM.setUSER_ID(manaPeoList.get(i).getUSER_ID());
                manaPeoM.setNAME(manaPeoList.get(i).getNAME());
                manaPeoM.setPHONE(manaPeoList.get(i).getPHONE());
                manList.add(manaPeoM);
            }
        }

        for (ManaPeo manaPeo:joinList) {
              manaPeo.setPEO_TYPE("2");
        }
        for(ManaPeo manaPeo:manList){
            manaPeo.setPEO_TYPE("1");
        }
    }

    private void initView() {
        bussInfoListView = (ListView) findViewById(R.id.bussInfoListView);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.daySwipeRefresh);
        // 设置下拉进度的背景颜色，默认就是白色的
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        // 设置下拉进度的主题颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.textdefault, R.color.colorPrimary, R.color.colorPrimaryDark);
        add_item = (TextView) findViewById(R.id.add_item);
        back = (ImageView) findViewById(R.id.back);
        bussName  = (TextView) findViewById(R.id.bussName);
        bussEdit = (TextView) findViewById(R.id.bussEdit);

        in_buss_layout = (RelativeLayout) findViewById(R.id.in_buss_layout);
        out_buss_layout = (RelativeLayout) findViewById(R.id.out_buss_layout);

        unComInfo = (TextView) findViewById(R.id.unComInfo);
        unComInfoIm = (ImageView) findViewById(R.id.unComInfoIm);

        compleInfo = (TextView) findViewById(R.id.compleInfo);
        compleInfoIm = (ImageView) findViewById(R.id.compleInfoIm);

        in_buss_layout.setOnClickListener(this);
        out_buss_layout.setOnClickListener(this);

        add_item.setOnClickListener(this);
        back.setOnClickListener(this);
        bussEdit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                 finish();
                 break;
            case R.id.bussEdit:
                Intent editBussIntent = new Intent(BussinessActivity.this,BussEditActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("bussVo",bussinessVo);
                editBussIntent.putExtras(mBundle);
                editBussIntent.putExtra("joinList", (Serializable) joinList);
                Log.d("joinList_size",joinList.size()+"");
                editBussIntent.putExtra("manList", (Serializable) manList);
                Log.d("manList_size",manList.size()+"");
                editBussIntent.putExtra("stuContactVoArrayList", (Serializable) stuContactVoArrayList);
                startActivity(editBussIntent);
                finish();
                 break;

            case R.id.add_item:
                 Intent intent  = new Intent(BussinessActivity.this,AddBussItemActivity.class);
                 intent.putExtra("FLAG","ADD");
                 intent.putExtra("manaPeoList", (Serializable) manaPeoList);
                 intent.putExtra("manList", (Serializable) manList);
                Log.d("sfdhffffffffffffff",buss_name);
                intent.putExtra("BUSS_NAME",buss_name);
                 intent.putExtra("BUSS_ID",buss_id);
                 startActivity(intent);
                 break;
            case R.id.in_buss_layout:
                unComInfo.setTextColor(getResources().getColor(R.color.tabColor));
                compleInfo.setTextColor(getResources().getColor(R.color.black));

                unComInfoIm.setVisibility(View.VISIBLE);
                compleInfoIm.setVisibility(View.INVISIBLE);

                unComInfoIm.setBackgroundColor(getResources().getColor(R.color.tabColor));
                compleInfoIm.setBackgroundColor(getResources().getColor(R.color.white));

                one_flag = 0;
                two_flag = 1;

                bussInfoList.clear();
                bussInfoList.addAll(unComPleteList);
                bussInfoAdapter.notifyDataSetChanged();
                break;

            case R.id.out_buss_layout:
                compleInfo.setTextColor(getResources().getColor(R.color.tabColor));
                unComInfo.setTextColor(getResources().getColor(R.color.black));

                compleInfoIm.setVisibility(View.VISIBLE);
                unComInfoIm.setVisibility(View.INVISIBLE);

                compleInfoIm.setBackgroundColor(getResources().getColor(R.color.tabColor));
                unComInfoIm.setBackgroundColor(getResources().getColor(R.color.white));

                one_flag = 1;
                two_flag = 0;

                bussInfoList.clear();
                bussInfoList.addAll(comPleteList);
                bussInfoAdapter.notifyDataSetChanged();
                break;
        }
    }

    public void QueryBussInfo(){
        OkGo.post(URL.QUERY_BUSS_INFO)
                .tag(this)
                .params("BUSS_ID",buss_id)
                .cacheKey("catchKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        swipeRefreshLayout.setRefreshing(false);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(s);
                            if(jsonObject.getBoolean("success")){
                                bussInfoVoList =  new Gson().fromJson(jsonObject.getString("data"),new TypeToken<List<BussInfoVo>>(){}.getType());
                                manaData(bussInfoVoList);
                            }else{
                                Toast.makeText(BussinessActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(BussinessActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(BussinessActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void manaData(List<BussInfoVo> bussInfoVoList) {

        comPleteList.clear();
        unComPleteList.clear();

        for (int i = 0; i < bussInfoVoList.size(); i++) {
            if(bussInfoVoList.get(i).getINFO_FLAG().equals("0")){
                comPleteList.add(bussInfoVoList.get(i));
            }else{
                unComPleteList.add(bussInfoVoList.get(i));
            }
        }

        if(one_flag==0&&two_flag==1){
            bussInfoList.clear();
            bussInfoList.addAll(unComPleteList);
            bussInfoAdapter.notifyDataSetChanged();
        }else if(one_flag==1&&two_flag==0){
            bussInfoList.clear();
            bussInfoList.addAll(comPleteList);
            bussInfoAdapter.notifyDataSetChanged();
        }
    }

    private void delteBussInfo(String buss_info_id){
          BUSS_MANA.setLength(0);

        for (int i=0;i<manaPeoList.size();i++){
          BUSS_MANA.append(manaPeoList.get(i).getUSER_ID()+"&-&");
        }
        OkGo.post(URL.DELTE_BUSS_INFO)
                .tag(this)
                .params("BUSS_INFO_ID",buss_info_id)
                .params("USER_ID",MyApplication.getUserVo().getUSER_ID())
                .params("BUSS_ID",bussinessVo.getBUSS_ID())
                .params("BUSS_NAME",bussinessVo.getBUSS_NAME())
                .params("BUSS_MANA",BUSS_MANA.toString())
                .params("NAME",MyApplication.getUserVo().getNAME())
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

                            }
                            Toast.makeText(BussinessActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(BussinessActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                        Toast.makeText(BussinessActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                    }
                });

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
}
