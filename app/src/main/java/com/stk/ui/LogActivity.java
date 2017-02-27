package com.stk.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.stk.adapter.FreeLogAdapter;
import com.stk.efflog.MyApplication;
import com.stk.efflog.R;
import com.stk.model.CommitLogVo;
import com.stk.model.LogVo;
import com.stk.model.WeatherVo;
import com.stk.utils.LogExpandView;
import com.stk.utils.LogListView;
import com.stk.utils.URL;
import com.stk.utils.WeiboDialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

import static com.stk.fragment.WorkFragment.UPDATE_ACTION;

public class LogActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView log_commit;
    private LogExpandView LogListView;
    private Map<LogVo, ArrayList<CommitLogVo>> dataset = new HashMap<>();
    private MyExpandableListViewAdapter adapter;
    private  final  static int REQUEST_CODE = 1001;
    private AlertDialog.Builder builder;
    private final int A_RESULT_CODE = 1001;
    private final int B_RESULT_CODE = 1002;
    private final int C_RESULT_CODE = 1003;
    private String log_type;
    private Intent intent;
    private SimpleDateFormat ssf;
  //  private int logClass;
    private List<CommitLogVo> commitLogVoList = new ArrayList<>();
    private String weather;
    private final int DELETE_CODE = 1213;
    private final int UPDATE_CODE = 1212;
    private ImageView back;
    private ImageView fen_image;
    private LogListView freeListView;
    private Button mAddLog;
    private ArrayList<CommitLogVo> commitLogVos = new ArrayList<>();
    private FreeLogAdapter freeLogAdapter;
    private List<LogVo> logVoList = new ArrayList<>();
    private String prior_type;
    private List<LogVo> unCompleteList;
    private int point =0;
    private Dialog mWeiboDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        initView();
        getWeather();
        intent =  getIntent();
        //logClass =  intent.getIntExtra("logLevel",1001);
        log_type = intent.getStringExtra("log_type");
        unCompleteList = (List<LogVo>)intent.getSerializableExtra("unComLogVoList");
        Log.d("ssssssssssssssss",log_type);
        getRecentLog();
        freeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent =  new Intent(LogActivity.this,SingleLogActivity.class);
                intent.putExtra("position",position+"");
                intent.putExtra("groupPosition","-1");
                intent.putExtra("list", (Serializable)commitLogVos);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
    }




    private void initView() {
        fen_image = (ImageView) findViewById(R.id.fen_image);
        mAddLog = (Button) findViewById(R.id.mAddLog);
        mAddLog.setOnClickListener(this);
        freeListView = (LogListView) findViewById(R.id.freeListView);
        back = (ImageView) findViewById(R.id.back);
        ssf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log_commit = (TextView) findViewById(R.id.log_commit);
        LogListView = (LogExpandView) findViewById(R.id.logListView);
        log_commit.setOnClickListener(this);
        back.setOnClickListener(this);

        LogListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent =  new Intent(LogActivity.this,SingleLogActivity.class);
                intent.putExtra("position",childPosition+"");
                intent.putExtra("groupPosition",groupPosition+"");
                intent.putExtra("list", (Serializable)dataset.get(logVoList.get(groupPosition)));
                startActivityForResult(intent,REQUEST_CODE);
                return true;
            }
        });
    }

    private void initDate(List<LogVo> priorList){
        for (int i = 0; i < priorList.size(); i++) {
            dataset.put(priorList.get(i),new ArrayList<CommitLogVo>());
        }

        for (int i = 0; i < unCompleteList.size(); i++) {
               point = 0;
               LogVo logModel =  unCompleteList.get(i);
               CommitLogVo commitLogVo = new CommitLogVo();

               commitLogVo.setBEGIN_TIME(logModel.getBEGIN_TIME());
               commitLogVo.setLOG_ID(logModel.getLOG_ID());
               commitLogVo.setLOG_INDEX("123");
               commitLogVo.setLOG_FLAG(logModel.getLOG_FLAG());
               commitLogVo.setLOG_TYPE(log_type);
               commitLogVo.setPRIOR_ID(logModel.getPRIOR_ID());
               commitLogVo.setPRIOR_INDEX(logModel.getPRIOR_ID());
               commitLogVo.setROLE_ID(logModel.getROLE_ID());
               commitLogVo.setEND_TIME(logModel.getEND_TIME());
               commitLogVo.setLOG_CONTENT(logModel.getLOG_CONTENT());
               commitLogVo.setLOG_LEVEL(logModel.getLOG_LEVEL());
               commitLogVo.setLOG_MIND(logModel.getLOG_MIND());
               commitLogVo.setLOG_SUMMARY("null");
               commitLogVo.setUSER_ID(MyApplication.getUserVo().getUSER_ID());
               commitLogVo.setUPDATE_TIME(ssf.format(new Date()));
               commitLogVo.setWEATHER(logModel.getWEATHER());
               commitLogVo.setLOG_TIME(ssf.format(new Date()));

               for(LogVo logVo:dataset.keySet()){
                   if(logModel.getPRIOR_ID().equals(logVo.getLOG_ID())&&logModel.getPRIOR_INDEX().equals(logVo.getLOG_INDEX())){
                       point = 1;
                       dataset.get(logVo).add(commitLogVo);
                       break;
                   }
               }
               if(point!=1){
                commitLogVos.add(commitLogVo);
               }

        }
        adapter = new MyExpandableListViewAdapter();
        LogListView.setAdapter(adapter);

        int groupCount = LogListView.getCount();
        for (int i=0; i<groupCount; i++) {
            LogListView.expandGroup(i);
        };

        freeLogAdapter = new FreeLogAdapter(LogActivity.this,commitLogVos);
        freeListView.setAdapter(freeLogAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.log_commit:
                for (int i = 0; i < dataset.size(); i++) {
                    for (int j = 0; j <dataset.get(logVoList.get(i)).size(); j++) {
                        commitLogVoList.add(dataset.get(logVoList.get(i)).get(j));
                    }
                }
                for (int i = 0; i < commitLogVos.size(); i++) {
                         commitLogVoList.add(commitLogVos.get(i));
                }

                if(commitLogVoList.size()==0){
                    Toast.makeText(this, "请先编写日志~", Toast.LENGTH_SHORT).show();
                }else{
                    mWeiboDialog = WeiboDialogUtils.createLoadingDialog(LogActivity.this, "正在上传...");
                    conmmitLog();
                }
                break;
            case R.id.back:
                finish();
                break;

            case R.id.mAddLog:
                alterDialog(-1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            switch (resultCode) {
                case A_RESULT_CODE:
                    String alogContent = data.getStringExtra("Log_content");
                    String alog_begin = data.getStringExtra("begin_time");
                    String alog_end = data.getStringExtra("end_time");
                    int a_position  = data.getIntExtra("position",0);
                    CommitLogVo aLogVo =  commitLogVoGet(alogContent,alog_begin,alog_end,"A");
                    if(a_position==-1){
                        commitLogVos.add(aLogVo);
                        CollectionDate(commitLogVos);
                        freeLogAdapter.notifyDataSetChanged();
                    }else{
                        aLogVo.setPRIOR_ID(logVoList.get(a_position).getLOG_ID());
                        aLogVo.setPRIOR_INDEX(logVoList.get(a_position).getLOG_INDEX());
                        dataset.get(logVoList.get(a_position)).add(aLogVo);
                        CollectionDate(dataset.get(logVoList.get(a_position)));
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case B_RESULT_CODE:
                    String blogContent = data.getStringExtra("Log_content");
                    String blog_begin = data.getStringExtra("begin_time");
                    String blog_end = data.getStringExtra("end_time");
                    int b_position  = data.getIntExtra("position",0);
                    CommitLogVo bLogVo =  commitLogVoGet(blogContent,blog_begin,blog_end,"B");
                    if(b_position==-1){
                        commitLogVos.add(bLogVo);
                        CollectionDate(commitLogVos);
                        freeLogAdapter.notifyDataSetChanged();
                    }else{
                        bLogVo.setPRIOR_ID(logVoList.get(b_position).getLOG_ID());
                        bLogVo.setPRIOR_INDEX(logVoList.get(b_position).getLOG_INDEX());
                        dataset.get(logVoList.get(b_position)).add(bLogVo);
                        CollectionDate(dataset.get(logVoList.get(b_position)));
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case C_RESULT_CODE:
                    String clogContent = data.getStringExtra("Log_content");
                    String clog_begin = data.getStringExtra("begin_time");
                    String clog_end = data.getStringExtra("end_time");
                    int c_position  = data.getIntExtra("position",0);
                    CommitLogVo cLogVo =  commitLogVoGet(clogContent,clog_begin,clog_end,"C");
                    if(c_position==-1){
                        commitLogVos.add(cLogVo);
                        CollectionDate(commitLogVos);
                        freeLogAdapter.notifyDataSetChanged();
                    }else{
                        cLogVo.setPRIOR_ID(logVoList.get(c_position).getLOG_ID());
                        cLogVo.setPRIOR_INDEX(logVoList.get(c_position).getLOG_INDEX());
                        dataset.get(logVoList.get(c_position)).add(cLogVo);
                        CollectionDate(dataset.get(logVoList.get(c_position)));
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case UPDATE_CODE:
                    String update_positon =  data.getStringExtra("update_position");
                    String update_groupPosition  = data.getStringExtra("update_groupPosition");
                    String update_content = data.getStringExtra("update_content");
                    String begin_time = data.getStringExtra("begin_time");
                    String end_time = data.getStringExtra("end_time");
                    if(update_groupPosition.equals("-1")){
                       CommitLogVo updatelogVo = commitLogVos.get(Integer.valueOf(update_positon));
                       updatelogVo.setLOG_CONTENT(update_content);
                       updatelogVo.setBEGIN_TIME(begin_time);
                       updatelogVo.setEND_TIME(end_time);
                       CollectionDate(commitLogVos);
                       freeLogAdapter.notifyDataSetChanged();
                    }else{
                        CommitLogVo updateCommitLogVo  = dataset.get(logVoList.get(Integer.valueOf(update_groupPosition))).get(Integer.valueOf(update_positon));
                        updateCommitLogVo.setLOG_CONTENT(update_content);
                        updateCommitLogVo.setBEGIN_TIME(begin_time);
                        updateCommitLogVo.setEND_TIME(end_time);
                        CollectionDate(dataset.get(logVoList.get(Integer.valueOf(update_groupPosition))));
                        adapter.notifyDataSetChanged();
                    }
                    break;

                case DELETE_CODE:
                    String delete_position =  data.getStringExtra("delete_position");
                    String delete_groupPosition  = data.getStringExtra("delete_groupPosition");
                    if(delete_groupPosition.equals("-1")){
                       commitLogVos.remove(commitLogVos.get(Integer.valueOf(delete_position)));
                        CollectionDate(commitLogVos);
                        freeLogAdapter.notifyDataSetChanged();
                    }else{
                        for (int i = 0; i < dataset.get(logVoList.get(Integer.valueOf(delete_groupPosition))).size(); i++) {
                            if(i==Integer.valueOf(delete_position)){
                                dataset.get(logVoList.get(Integer.valueOf(delete_groupPosition))).remove(dataset.get(logVoList.get(Integer.valueOf(delete_groupPosition))).get(i));
                            }
                        }
                        CollectionDate(dataset.get(logVoList.get(Integer.valueOf(delete_groupPosition))));
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }

    private CommitLogVo commitLogVoGet(String logContent,String timeBegin,String timeEnd,String level){
        CommitLogVo commitLogVo = new CommitLogVo();
        commitLogVo.setBEGIN_TIME(timeBegin);
        commitLogVo.setLOG_INDEX("123");
        commitLogVo.setROLE_ID(MyApplication.getUserVo().getROLE_ID());
        commitLogVo.setEND_TIME(timeEnd);
        commitLogVo.setLOG_CONTENT(logContent);
        commitLogVo.setLOG_FLAG("1");
        commitLogVo.setLOG_ID("123");
        commitLogVo.setUSER_ID(MyApplication.getUserVo().getUSER_ID());
        commitLogVo.setLOG_TIME(ssf.format(new Date()));

        commitLogVo.setLOG_LEVEL(level);
        commitLogVo.setPRIOR_ID("null");
        commitLogVo.setPRIOR_INDEX("null");
        commitLogVo.setWEATHER(weather);
        commitLogVo.setUPDATE_TIME(ssf.format(new Date()));
        commitLogVo.setLOG_MIND("null");
        commitLogVo.setLOG_SUMMARY("null");
        commitLogVo.setLOG_TYPE(log_type);
        return commitLogVo;
    }

    private class MyExpandableListViewAdapter extends BaseExpandableListAdapter {

        //获得父项的数量
        @Override
        public int getGroupCount() {
            return dataset.size();
        }

        //获取某个父项的子项数量
        @Override
        public int getChildrenCount(int groupPosition) {
            return dataset.get(logVoList.get(groupPosition)).size();
        }

        //获得某个父项
        @Override
        public Object getGroup(int groupPosition) {
            return dataset.get(logVoList.get(groupPosition));
        }

        //  获得某个父项的某个子项
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return dataset.get(logVoList.get(groupPosition)).get(childPosition );
        }

        //获取groupId
        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        //获取某个父项的子项id
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        //  按函数的名字来理解应该是是否具有稳定的id，这个方法目前一直都是返回false，没有去改动过
        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) LogActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.parent_layout, null);
            }
            convertView.setTag(R.layout.parent_layout, groupPosition);
            convertView.setTag(R.layout.child_layout, -1);
            TextView text = (TextView) convertView.findViewById(R.id.parent_title);
            TextView textView = (TextView) convertView.findViewById(R.id.parent_content);
            text.setText(logVoList.get(groupPosition).getLOG_LEVEL());
            textView.setText(logVoList.get(groupPosition).getLOG_CONTENT());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 /* //Toast.makeText(LogActivity.this, weekLogVoList.get(groupPosition).getContent(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LogActivity.this,LogWriteActivity.class);
                    startActivityForResult(intent,REQUEST_CODE);*/
                    alterDialog(groupPosition);
                }
            });
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) LogActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.child_layout, null);
            }

            convertView.setTag(R.layout.parent_layout, groupPosition);
            convertView.setTag(R.layout.child_layout, childPosition);

            TextView text = (TextView) convertView.findViewById(R.id.child_title);
            TextView textView = (TextView) convertView.findViewById(R.id.child_content);
            TextView time = (TextView) convertView.findViewById(R.id.time);

            String start = dataset.get(logVoList.get(groupPosition)).get(childPosition).getBEGIN_TIME();
            String end  = dataset.get(logVoList.get(groupPosition)).get(childPosition).getEND_TIME();

            if(isYesterday(dataset.get(logVoList.get(groupPosition)).get(childPosition).getBEGIN_TIME())==0){
                start = start.substring(start.length()-5,start.length());
            }else{
                start = start.substring(5);
            }

            if(isYesterday(dataset.get(logVoList.get(groupPosition)).get(childPosition).getEND_TIME())==0){
                end = end.substring(end.length()-5,end.length());
            }else{
                end = end.substring(5);
            }
            Log.d("sssssssiiiiii",dataset.get(logVoList.get(groupPosition)).get(childPosition).getBEGIN_TIME());

            text.setText(dataset.get(logVoList.get(groupPosition)).get(childPosition).getLOG_LEVEL());
            textView.setText(dataset.get(logVoList.get(groupPosition)).get(childPosition).getLOG_CONTENT());
            time.setText("起： "+start);
            //myViewHolder.time.setText("起： "+start+"         止： "+end);
           // time.setText("起： "+dataset.get(logVoList.get(groupPosition)).get(childPosition).getBEGIN_TIME());
            return convertView;
        }

        //  子项是否可选中，如果需要设置子项的点击事件，需要返回true
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    private void alterDialog(final int groupPosition){
        builder = new AlertDialog.Builder(this);
        builder.setItems(getResources().getStringArray(R.array.ItemEqual), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                switch (arg1) {
                    case 0:
                        Intent aIntent = new Intent(LogActivity.this, LogWriteActivity.class);
                        aIntent.putExtra("logLevel",A_RESULT_CODE );
                        aIntent.putExtra("log_type",log_type);
                        aIntent.putExtra("intent_type","123");
                        aIntent.putExtra("groupPosition",groupPosition);
                        startActivityForResult(aIntent, REQUEST_CODE);
                        break;
                    case 1:
                        Intent bIntent = new Intent(LogActivity.this, LogWriteActivity.class);
                        bIntent.putExtra("logLevel", B_RESULT_CODE);
                        bIntent.putExtra("log_type",log_type);
                        bIntent.putExtra("intent_type","123");
                        bIntent.putExtra("groupPosition",groupPosition);
                        startActivityForResult(bIntent, REQUEST_CODE);
                        break;
                    case 2:
                        Intent cIntent = new Intent(LogActivity.this, LogWriteActivity.class);
                        cIntent.putExtra("logLevel", C_RESULT_CODE);
                        cIntent.putExtra("log_type",log_type);
                        cIntent.putExtra("intent_type","123");
                        cIntent.putExtra("groupPosition",groupPosition);
                        startActivityForResult(cIntent, REQUEST_CODE);
                        break;
                }
                arg0.dismiss();
            }
        });
        builder.show();
    }


    private void conmmitLog(){
        OkGo.post(URL.LOG_COMMIT)
                .tag(this)
                .params("LogVos","{"+"\"data\":"+commitLogVoList.toString()+"}")
                .params("USER_ID",MyApplication.getUserVo().getUSER_ID())
                .params("NAME",MyApplication.getUserVo().getNAME())
                .params("LEADER_ID",MyApplication.getUserVo().getLEADER_ID())
                .cacheKey("catchKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(s);
                            Boolean isSuccess = jsonObject.getBoolean("success");
                            if(isSuccess){
                                Toast.makeText(LogActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                finish();
                                sendBroadcast(new Intent(UPDATE_ACTION));
                            }else{
                                Toast.makeText(LogActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Toast.makeText(LogActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                    }
                });
    }

    private void getWeather(){
           OkGo.get("http://apicloud.mob.com/v1/weather/query")
                   .tag(this)
                   .params("key","1a4b99c9e7c4f")
                   .params("city","杭州")
                   .params("province","浙江")
                   .cacheKey("catchKey")
                   .cacheMode(CacheMode.DEFAULT)
                   .execute(new StringCallback() {
                       @Override
                       public void onSuccess(String s, Call call, Response response) {
                           try{
                               WeatherVo  weatherVo =  new Gson().fromJson(s,WeatherVo.class);
                               weather = weatherVo.getResult().get(0).getWeather();
                           }catch (Exception e){
                               weather = "null";
                           }
                       }
                       @Override
                       public void onError(Call call, Response response, Exception e) {
                           super.onError(call, response, e);
                           weather = "null";
                       }
                   });
    }

    private void CollectionDate(ArrayList<CommitLogVo> list){
        Collections.sort(list, new Comparator<CommitLogVo>() {
            @Override
            public int compare(CommitLogVo logOne, CommitLogVo logTwo) {
                return logOne.getLOG_LEVEL().compareTo(logTwo.getLOG_LEVEL());
            }
        });
    }

    private void getRecentLog(){
        if(!log_type.equals("05")){
            prior_type = Integer.valueOf(log_type)+1+"";
            prior_type = "0"+prior_type;
        }else{
            prior_type = "05";
        }
        OkGo.post(URL.RECENT_LOG)
                .tag(this)
                .params("LOG_TYPE",prior_type)
                .params("USER_ID",MyApplication.getUserVo().getUSER_ID())
                .cacheKey("catchKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(s);
                            if(jsonObject.getBoolean("success")){
                                logVoList = new Gson().fromJson(jsonObject.getString("data"),new TypeToken<List<LogVo>>() {}.getType());
                                if(logVoList==null){
                                    logVoList = new ArrayList<LogVo>();
                                    fen_image.setVisibility(View.GONE);
                                    LogListView.setVisibility(View.GONE);
                                }else{
                                    fen_image.setVisibility(View.VISIBLE);
                                    LogListView.setVisibility(View.VISIBLE);
                                }
                                collectionLevel(logVoList);
                              /*  fen_image.setVisibility(View.GONE);
                                LogListView.setVisibility(View.GONE);*/
                                initDate(logVoList);
                            }else{

                            }
                        }catch (JSONException e){

                        }
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                    }
                });
    }

    private void collectionLevel(List<LogVo> logs){
        Collections.sort(logs, new Comparator<LogVo>() {
            @Override
            public int compare(LogVo logOne, LogVo logTwo) {
                return logOne.getLOG_LEVEL().compareTo(logTwo.getLOG_LEVEL());
            }
        });
    }

    public int isYesterday(String date){
        int day =0;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d1  = new Date();//当前时间
            Date d2 = sdf.parse(date);//传进的时间
            long cha = d2.getTime() - d1.getTime();
            day = new Long(cha/(1000*60*60*24)).intValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day;
    }


}
