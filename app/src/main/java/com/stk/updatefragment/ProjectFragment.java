package com.stk.updatefragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.stk.adapter.ProUpdateAdapter;
import com.stk.efflog.MyApplication;
import com.stk.efflog.R;
import com.stk.model.ProUpdateVo;
import com.stk.utils.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectFragment extends Fragment implements View.OnClickListener{
    private View view;
    private ListView proListView;
    private RelativeLayout bottom_layout;
    private List<ProUpdateVo> proUpdateVoList;
    private List<ProUpdateVo> updateVoList  = new ArrayList<>();

    private ProUpdateAdapter proUpdateAdapter;
    private Map<Integer,Boolean> isSelect;
    private StringBuffer sb = new StringBuffer();
    private TextView updatePro;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.fragment_project2, container, false);
        initView();
        queryProDay();
        return view;
    }

    private void initView() {
        updatePro = (TextView) view.findViewById(R.id.updatePro);
        proListView = (ListView) view.findViewById(R.id.proListView);
        bottom_layout = (RelativeLayout) view.findViewById(R.id.bottom_layout);
        bottom_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bottom_layout:
                  if(updatePro.getText().toString().equals("点击跳转")){
                      changePage.setCurPage(0);
                  }else{
                      isSelect = proUpdateAdapter.getIsSelect();
                      updateVoList.clear();
                      for (int i = 0; i < isSelect.size(); i++) {
                          if(isSelect.get(i)){
                              sb.append(proUpdateVoList.get(i).getPRO_ITEM_ID()+"&-&");
                          }
                      }
                      updateProDay(sb);
                  }
                break;
        }
    }

    private void queryProDay(){
        OkGo.post(URL.QUERY_PROJECT_DAY)
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
                                proUpdateVoList =  new Gson().fromJson(jsonObject.getString("data"),new TypeToken<List<ProUpdateVo>>(){}.getType());
                                proUpdateAdapter = new ProUpdateAdapter(proUpdateVoList,getActivity());
                                proListView.setAdapter(proUpdateAdapter);
                                if(proUpdateVoList.size()==0){
                                    updatePro.setText("点击跳转");
                                }
                            }else{
                                Toast.makeText(getActivity(), "查询失败", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "查询失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Toast.makeText(getActivity(), "网络请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateProDay(StringBuffer id_array){
        OkGo.post(URL.UPDATE_PROJECT_DAY)
                .tag(this)
                .params("PRO_ITEM", id_array.toString())
                .cacheKey("catchKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(s);
                            if(jsonObject.getBoolean("success")){
                                Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_SHORT).show();

                                updateVoList.clear();
                                for (int i = 0; i < proUpdateVoList.size(); i++) {
                                    if(!isSelect.get(i)){
                                        updateVoList.add(proUpdateVoList.get(i));
                                    }else{
                                        isSelect.put(i,false);
                                    }
                                }
                                proUpdateVoList.clear();
                                proUpdateVoList.addAll(updateVoList);

                                proUpdateAdapter.notifyDataSetChanged();

                                if(proUpdateVoList.size()==0){
                                    updatePro.setText("点击跳转");
                                }
                            }else{
                                Toast.makeText(getActivity(), "更新失败", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "更新失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Toast.makeText(getActivity(), "服务器请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public interface ChangePage{
        public void setCurPage(int page);
    }

    private ChangePage changePage;

    public ChangePage getChangePage() {
        return changePage;
    }

    public void setChangePage(ChangePage changePage) {
        this.changePage = changePage;
    }
}
