package com.stk.fragment;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.stk.efflog.MyApplication;
import com.stk.efflog.R;
import com.stk.model.UpdataInfo;
import com.stk.ui.EditPassActivity;
import com.stk.ui.EditPhoneActivity;
import com.stk.utils.ApkUtils;
import com.stk.utils.CustomDialog;
import com.stk.utils.URL;
import com.stk.utils.WeiboDialogUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment implements View.OnClickListener{
    private View view;
    private RelativeLayout editPass;
    private RelativeLayout editPhone;
    private RelativeLayout updateApp;
    private TextView user_name;
    private TextView user_phone;
    private int flag=1;
    private Dialog mWeiboDialog;
    private CustomDialog.Builder builder;
    private UpdataInfo updataInfo;
    private View appView;
    private ProgressBar progressBar;
    private TextView curr_pro;
    private TextView tips;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user, container, false);
        initView();
        return view;
    }

    private void initView(){
        editPass = (RelativeLayout) view.findViewById(R.id.editPass);
        editPhone = (RelativeLayout) view.findViewById(R.id.editPhone);
        updateApp = (RelativeLayout) view.findViewById(R.id.update_app);
        user_name = (TextView) view.findViewById(R.id.user_name);
        user_phone = (TextView) view.findViewById(R.id.user_phone);
        builder = new CustomDialog.Builder(getActivity());
        editPass.setOnClickListener(this);
        editPhone.setOnClickListener(this);
        updateApp.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        user_name.setText(MyApplication.getUserVo().getNAME());
        user_phone.setText(MyApplication.getUserVo().getPHONE());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editPass:
                Intent editPassIn = new Intent(getActivity(), EditPassActivity.class);
                startActivity(editPassIn);
                break;
            case R.id.editPhone:
                Intent editPhoneIn = new Intent(getActivity(), EditPhoneActivity.class);
                startActivity(editPhoneIn);
                break;
            case R.id.update_app:
                if (flag == 0) {
                    Toast.makeText(getActivity(), "正在更新.请稍后", Toast.LENGTH_SHORT).show();
                }else{
                    mWeiboDialog = WeiboDialogUtils.createLoadingDialog(getActivity(), "请稍后...");
                    OkGo.get(URL.APP_DOWNLOAD)//
                            .tag(getActivity())//
                            .execute(new FileCallback() {
                                @Override
                                public void onSuccess(File file, Call call, Response response) {

                                }

                                @Override
                                public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                                    //这里回调下载进度(该回调在主线程,可以直接更新ui)
                                    if (currentSize / totalSize == 1) {
                                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                                        try {
                                            InputStream in1 = new FileInputStream(Environment.getExternalStorageDirectory().getPath() + File.separator + "Download" + File.separator + "update.xml");
                                            BufferedInputStream in2 = new BufferedInputStream(in1);
                                            updataInfo = ApkUtils.getUpdataInfo(in2);
                                            Log.d("info", updataInfo.toString());
                                            if (!updataInfo.getVersion().equals(getVersionName())) {
                                                appView = LayoutInflater.from(getActivity()).inflate(R.layout.update_app_pop, null);
                                                progressBar = (ProgressBar) appView.findViewById(R.id.up_progress);
                                                curr_pro = (TextView) appView.findViewById(R.id.curr_pro);
                                                tips = (TextView) appView.findViewById(R.id.tips);
                                                builder.setContentView(appView);
                                                builder.setTitle("提醒");
                                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    public void onClick(final DialogInterface dialog, int which) {
                                                        tips.setVisibility(View.GONE);
                                                        progressBar.setVisibility(View.VISIBLE);
                                                        curr_pro.setVisibility(View.VISIBLE);
                                                        OkGo.get(updataInfo.getUrl())
                                                                .tag(getActivity())
                                                                .execute(new FileCallback() {  //文件下载时，可以指定下载的文件目录和文件名
                                                                    @Override
                                                                    public void onSuccess(File file, Call call, Response response) {

                                                                    }

                                                                    @Override
                                                                    public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                                                                        flag = 0;
                                                                        //这里回调下载进度(该回调在主线程,可以直接更新ui)
                                                                        progressBar.setMax((int) totalSize);
                                                                        progressBar.setProgress((int) currentSize);
                                                                        curr_pro.setText("当前进度：" + (Math.round(progress * 10000) * 1.0f / 100) + "%");
                                                                        if (currentSize / totalSize == 1) {
                                                                            flag = 1;
                                                                            dialog.dismiss();
                                                                            ApkUtils.install(getActivity(), new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "Download" + File.separator + "update.apk"));
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onError(Call call, Response response, Exception e) {
                                                                        dialog.dismiss();
                                                                        super.onError(call, response, e);
                                                                        Toast.makeText(getActivity(), "网络请求失败", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                    }
                                                });
                                                builder.setNegativeButton("取消",
                                                        new android.content.DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                            }
                                                        });
                                                builder.create().show();
                                            } else {
                                                Toast.makeText(getActivity(), "当前已是最新版本", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onError(Call call, Response response, Exception e) {
                                    WeiboDialogUtils.closeDialog(mWeiboDialog);
                                    Toast.makeText(getActivity(), "网络请求失败", Toast.LENGTH_SHORT).show();
                                    super.onError(call, response, e);
                                }
                            });


                }
                break;
        }
    }

    private String getVersionName() throws Exception {
        PackageManager packageManager = getActivity().getPackageManager();
        PackageInfo packInfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);
        return packInfo.versionName;
    }

}
