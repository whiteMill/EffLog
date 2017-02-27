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
import com.stk.adapter.BussUpdateAdapter;
import com.stk.efflog.MyApplication;
import com.stk.efflog.R;
import com.stk.model.BussUpdateVo;
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
public class BussFragment extends Fragment implements View.OnClickListener{
    private View view;
    private ListView bussListView;
    private RelativeLayout bottom_layout;
    private List<BussUpdateVo> bussUpdateVoList;
    private List<BussUpdateVo> bussUpdateVos = new ArrayList<>();

    private BussUpdateAdapter bussUpdateAdapter;
    private Map<Integer,Boolean> isSelect;
    private StringBuffer sb = new StringBuffer();
    private TextView updateBuss;
    private ProjectFragment.ChangePage changePage;

    public ProjectFragment.ChangePage getChangePage() {
        return changePage;
    }

    public void setChangePage(ProjectFragment.ChangePage changePage) {
        this.changePage = changePage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_buss, container, false);
        initView();
        queryBussDay();
        return view;
    }

    private void initView() {
        updateBuss = (TextView) view.findViewById(R.id.updateBuss);
        bussListView = (ListView) view.findViewById(R.id.bussListView);
        bottom_layout = (RelativeLayout) view.findViewById(R.id.bottom_layout);
        bottom_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bottom_layout:
                if(updateBuss.getText().toString().equals("点击跳转")){
                    changePage.setCurPage(0);
                }else{
                    isSelect = bussUpdateAdapter.getIsSelect();
                    for (int i = 0; i < isSelect.size(); i++) {
                        if(isSelect.get(i)){
                            sb.append(bussUpdateVoList.get(i).getBUSS_INFO_ID()+"&-&");
                        }
                    }
                    updateBussDay(sb);
                }
                break;

        }
    }

    private void queryBussDay(){
        OkGo.post(URL.QUERY_BUSS_DAY)
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
                                bussUpdateVoList =  new Gson().fromJson(jsonObject.getString("data"),new TypeToken<List<BussUpdateVo>>(){}.getType());
                                bussUpdateAdapter = new BussUpdateAdapter(getActivity(),bussUpdateVoList);
                                bussListView.setAdapter(bussUpdateAdapter);
                                if(bussUpdateVoList.size()==0){
                                    updateBuss.setText("点击跳转");
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


    private void updateBussDay(StringBuffer id_array){
        OkGo.post(URL.UPDATE_BUSS_DAY)
                .tag(this)
                .params("BUSS_ITEM", id_array.toString())
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

                                bussUpdateVos.clear();
                                for (int i = 0; i < bussUpdateVoList.size(); i++) {
                                    if(!isSelect.get(i)){
                                        bussUpdateVos.add(bussUpdateVoList.get(i));
                                    }else{
                                        isSelect.put(i,false);
                                    }
                                }
                                bussUpdateVoList.clear();
                                bussUpdateVoList.addAll(bussUpdateVos);

                                bussUpdateAdapter.notifyDataSetChanged();

                                if(bussUpdateVoList.size()==0){
                                    updateBuss.setText("点击跳转");
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
}
