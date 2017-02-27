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
import com.stk.adapter.ProInfoAdapter;
import com.stk.efflog.MyApplication;
import com.stk.efflog.R;
import com.stk.model.AllUserVo;
import com.stk.model.ManaPeo;
import com.stk.model.ProInfoVo;
import com.stk.model.ProjectVo;
import com.stk.utils.URL;
import com.stk.utils.WeiboDialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


public class ProjectActivity extends AppCompatActivity implements View.OnClickListener{

    private Intent intent;
    private String pro_name;
    private String pro_id;
    private ImageView back;
    private TextView proName;
    private TextView proEdit;

    private List<ManaPeo> manaPeoList;
    private List<ManaPeo> joinList = new ArrayList<>();
    private List<ManaPeo> manList = new ArrayList<>();

    private TextView add_item;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<ProInfoVo> proInfoVoList;

    private List<ProInfoVo> unBeginList = new ArrayList<>();
    private List<ProInfoVo> beginList = new ArrayList<>();
    private List<ProInfoVo> completeList = new ArrayList<>();

    private List<ProInfoVo> proInfoList = new ArrayList<>();

    private RelativeLayout un_begin_layout;
    private RelativeLayout begin_layout;
    private RelativeLayout comolete_layout;

    private ListView proInfoListView;
    private ProInfoAdapter proInfoAdapter;

    private int one_flag = 0;
    private int two_flag = 1;
    private int three_flag = 1;

    private TextView unBeginInfo;
    private TextView unComInfo;
    private TextView compleInfo;

    private ImageView unBeginInfofoIm;
    private ImageView unComInfofoIm;
    private ImageView compleInfoIm;

    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    private Dialog mWeiboDialog;

    private ProjectVo projectVo;

    private List<AllUserVo> stuContactVoArrayList= null;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private StringBuffer stringBuffer  = new StringBuffer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        initView();
        intent = getIntent();

        projectVo = (ProjectVo) getIntent().getSerializableExtra("proList");
        pro_name = projectVo.getPROJECT_NAME();
        pro_id = projectVo.getPROJECT_ID();
        manaPeoList = projectVo.getMANA_PEO();

        initDate();
        allUser();
        proName.setText(pro_name);
        swipeRefreshLayout.setRefreshing(true);
        proInfoAdapter = new ProInfoAdapter(ProjectActivity.this,proInfoList);
        proInfoListView.setAdapter(proInfoAdapter);
        QueryProInfo();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        QueryProInfo();
                    }
                },1500);
            }
        });

        proInfoListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                builder = new AlertDialog.Builder(ProjectActivity.this);
                builder.setItems(getResources().getStringArray(R.array.bussItem), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        switch (arg1) {
                            case 0:
                                alertDialog = new AlertDialog.Builder(ProjectActivity.this).setTitle("提醒").setMessage("确定要删除吗？")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                alertDialog.dismiss();
                                                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(ProjectActivity.this, "正在删除...");
                                                delteProInfo(proInfoList.get(position).getPRO_ITEM_ID());

                                            }
                                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                return;
                                            }
                                        }).create(); // 创建对话框
                                alertDialog.show(); // 显示对话框
                                break;
                            case 1:
                                Intent intent = new Intent(ProjectActivity.this,SetProRemindActivity.class);
                                intent.putExtra("MESSAGE",proInfoList.get(position).getITEM_MESS());
                                intent.putExtra("PRO_ID",pro_id);
                                intent.putExtra("PRO_NAME",pro_name);
                                intent.putExtra("PRO_ITEM_ID",proInfoList.get(position).getPRO_ITEM_ID());
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
        proInfoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ProjectActivity.this,AddProItemActivity.class);
                intent.putExtra("FLAG","UPDATE");
                intent.putExtra("manaPeoList", (Serializable) manaPeoList);
                intent.putExtra("manList", (Serializable) manList);
                intent.putExtra("PROJECT_ID",pro_id);
                intent.putExtra("PROJECT_NAME",projectVo.getPROJECT_NAME());
                intent.putExtra("POSITION",position+"");
                intent.putExtra("list", (Serializable)proInfoList);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        swipeRefreshLayout.setRefreshing(true);
        QueryProInfo();
    }

    private void initView() {
        proInfoListView = (ListView) findViewById(R.id.proInfoListView);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.proSwipeRefresh);
        // 设置下拉进度的背景颜色，默认就是白色的
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        // 设置下拉进度的主题颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.textdefault, R.color.colorPrimary, R.color.colorPrimaryDark);
        add_item = (TextView) findViewById(R.id.add_item);
        back = (ImageView) findViewById(R.id.back);
        proName  = (TextView) findViewById(R.id.proName);
        proEdit = (TextView) findViewById(R.id.proEdit);

        un_begin_layout = (RelativeLayout) findViewById(R.id.one_pro_layout);
        begin_layout = (RelativeLayout) findViewById(R.id.two_pro_layout);
        comolete_layout = (RelativeLayout) findViewById(R.id.three_pro_layout);

        unBeginInfo = (TextView) findViewById(R.id.unBegInfo);
        unBeginInfofoIm = (ImageView) findViewById(R.id.unBegInfoIm);

        unComInfo = (TextView) findViewById(R.id.beginInfo);
        unComInfofoIm = (ImageView) findViewById(R.id.beginInfofoIm);

        compleInfo = (TextView) findViewById(R.id.compleInfo);
        compleInfoIm = (ImageView) findViewById(R.id.compleInfoIm);

        un_begin_layout.setOnClickListener(this);
        begin_layout.setOnClickListener(this);
        comolete_layout.setOnClickListener(this);

        add_item.setOnClickListener(this);
        back.setOnClickListener(this);
        proEdit.setOnClickListener(this);
    }

    public void QueryProInfo(){
        OkGo.post(URL.QUERY_PRO_INFO)
                .tag(this)
                .params("PROJECT_ID",pro_id)
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
                                proInfoVoList =  new Gson().fromJson(jsonObject.getString("data"),new TypeToken<List<ProInfoVo>>(){}.getType());
                                manaData(proInfoVoList);
                            }else{
                                Toast.makeText(ProjectActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ProjectActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(ProjectActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
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

    private void manaData(List<ProInfoVo> proInfoVoList) {

        unBeginList.clear();
        beginList.clear();
        completeList.clear();

        try{
            for (int i = 0; i < proInfoVoList.size(); i++) {
                if(proInfoVoList.get(i).getITEM_FLAG().equals("0")){
                    completeList.add(proInfoVoList.get(i));
                }else{
                    if(simpleDateFormat.parse(proInfoVoList.get(i).getITEM_BEGIN_TIME()).before(new Date())){
                        beginList.add(proInfoVoList.get(i));
                    }else{
                        unBeginList.add(proInfoVoList.get(i));
                    }
                }
            }
        }catch (Exception e){

        }

        if(one_flag==0&&two_flag==1&&three_flag==1){
            proInfoList.clear();
            proInfoList.addAll(unBeginList);
            proInfoAdapter.notifyDataSetChanged();
        }else if(one_flag==1&&two_flag==0&&three_flag==1){
            proInfoList.clear();
            proInfoList.addAll(beginList);
            proInfoAdapter.notifyDataSetChanged();
        }else if(one_flag==1&&two_flag==1&&three_flag==0){
            proInfoList.clear();
            proInfoList.addAll(completeList);
            proInfoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.proEdit:
                Intent editProIntent = new Intent(ProjectActivity.this,ProjectEditActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("proVo",projectVo);
                editProIntent.putExtras(mBundle);
                editProIntent.putExtra("joinList", (Serializable) joinList);
                Log.d("joinList_size",joinList.size()+"");
                editProIntent.putExtra("manList", (Serializable) manList);
                Log.d("manList_size",manList.size()+"");
                editProIntent.putExtra("stuContactVoArrayList", (Serializable) stuContactVoArrayList);
                startActivity(editProIntent);
                finish();
                break;
            case R.id.add_item:
                Intent intent  = new Intent(ProjectActivity.this,AddProItemActivity.class);
                intent.putExtra("FLAG","ADD");
                intent.putExtra("manaPeoList", (Serializable) manaPeoList);
                intent.putExtra("manList", (Serializable) manList);
                intent.putExtra("PROJECT_NAME",projectVo.getPROJECT_NAME());
                intent.putExtra("PROJECT_ID",pro_id);
                startActivity(intent);
                break;
            case R.id.one_pro_layout:
                unBeginInfo.setTextColor(getResources().getColor(R.color.tabColor));
                unComInfo.setTextColor(getResources().getColor(R.color.black));
                compleInfo.setTextColor(getResources().getColor(R.color.black));

                unBeginInfofoIm.setVisibility(View.VISIBLE);
                unComInfofoIm.setVisibility(View.INVISIBLE);
                compleInfoIm.setVisibility(View.INVISIBLE);

                unBeginInfofoIm.setBackgroundColor(getResources().getColor(R.color.tabColor));
                unComInfofoIm.setBackgroundColor(getResources().getColor(R.color.white));
                compleInfoIm.setBackgroundColor(getResources().getColor(R.color.white));

                one_flag = 0;
                two_flag = 1;
                three_flag = 1;

                proInfoList.clear();
                proInfoList.addAll(unBeginList);
                proInfoAdapter.notifyDataSetChanged();
                break;

            case R.id.two_pro_layout:
                unBeginInfo.setTextColor(getResources().getColor(R.color.black));
                unComInfo.setTextColor(getResources().getColor(R.color.tabColor));
                compleInfo.setTextColor(getResources().getColor(R.color.black));

                unBeginInfofoIm.setVisibility(View.INVISIBLE);
                unComInfofoIm.setVisibility(View.VISIBLE);
                compleInfoIm.setVisibility(View.INVISIBLE);

                unBeginInfofoIm.setBackgroundColor(getResources().getColor(R.color.white));
                unComInfofoIm.setBackgroundColor(getResources().getColor(R.color.tabColor));
                compleInfoIm.setBackgroundColor(getResources().getColor(R.color.white));

                one_flag = 1;
                two_flag = 0;
                three_flag = 1;

                proInfoList.clear();
                proInfoList.addAll(beginList);
                proInfoAdapter.notifyDataSetChanged();
                break;

            case R.id.three_pro_layout:
                unBeginInfo.setTextColor(getResources().getColor(R.color.black));
                unComInfo.setTextColor(getResources().getColor(R.color.black));
                compleInfo.setTextColor(getResources().getColor(R.color.tabColor));

                unBeginInfofoIm.setVisibility(View.INVISIBLE);
                unComInfofoIm.setVisibility(View.INVISIBLE);
                compleInfoIm.setVisibility(View.VISIBLE);

                unBeginInfofoIm.setBackgroundColor(getResources().getColor(R.color.white));
                unComInfofoIm.setBackgroundColor(getResources().getColor(R.color.white));
                compleInfoIm.setBackgroundColor(getResources().getColor(R.color.tabColor));

                one_flag = 1;
                two_flag = 1;
                three_flag = 0;

                proInfoList.clear();
                proInfoList.addAll(completeList);
                proInfoAdapter.notifyDataSetChanged();
                break;
        }
    }


    private void delteProInfo(String pro_item_id){
        stringBuffer.setLength(0);
        for (int i = 0; i < manaPeoList.size(); i++) {
            stringBuffer.append(manaPeoList.get(i).getUSER_ID()+"&-&");
        }
        
        OkGo.post(URL.DELTE_PRO_ITEM)
                .tag(this)
                .params("PRO_ITEM_ID",pro_item_id)
                .params("USER_ID",MyApplication.getUserVo().getUSER_ID())
                .params("PRO_ID",projectVo.getPROJECT_ID())
                .params("PRO_NAME",projectVo.getPROJECT_NAME())
                .params("PRO_MANA",stringBuffer.toString())
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
                                swipeRefreshLayout.setRefreshing(true);
                                QueryProInfo();
                            }
                            Toast.makeText(ProjectActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ProjectActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                        Toast.makeText(ProjectActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
