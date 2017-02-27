package com.stk.efflog;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.stk.fragment.MessageFragment;
import com.stk.fragment.ProjectFragment;
import com.stk.fragment.UserFragment;
import com.stk.fragment.WorkFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private FragmentManager fragmentManager;
    private MessageFragment messageFragment;
    private WorkFragment workFragment;
    private UserFragment userFragment;
    private ProjectFragment projectFragment;

    private static final int MESSAGE_CENTER = 0;
    private static final int WORK_CENTER = 1;
    private static final int PROJECT_CENTER = 2;
    private static final int USER_CENTER = 3;
    private RadioButton messageBtn;
    private RadioButton workBtn;
    private RadioButton proBtn;
    private RadioButton userBtn;

    private int position = 1;

    private PushReceive pushReceive;
    public static final String PUSH_MESSAGE = "com.wl.action.PUSH_MESSAGE";

    // 要申请的权限
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        initView();
        setTabSelection(MESSAGE_CENTER);
        initBroad();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                      // 检查该权限是否已经获取
                       int i = ContextCompat.checkSelfPermission(this, permissions[0]);
                        // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                       if (i != PackageManager.PERMISSION_GRANTED) {
                               // 如果没有授予该权限，就去提示用户请求
                              showDialogTipUserRequestPermission();
                           }
                   }

     }

    private void showDialogTipUserRequestPermission() {
        new AlertDialog.Builder(this)
                       .setTitle("存储权限不可用")
                       .setMessage("由于效率日志需要获取存储空间，为你存储个人信息；\n否则，您将无法正常使用效率日志")
                       .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                              @Override
                             public void onClick(DialogInterface dialog, int which) {
                                        startRequestPermission();
                                 }
                             })
                         .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                             @Override
                                public void onClick(DialogInterface dialog, int which) {
                                         finish();
                                    }
                             }).setCancelable(false).show();
             }


                // 开始提交请求权限
                 private void startRequestPermission() {
               ActivityCompat.requestPermissions(this, permissions, 321);
             }


    @Override
         public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
               super.onRequestPermissionsResult(requestCode, permissions, grantResults);

                 if (requestCode == 321) {
                     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                                        // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                                       boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                                       if (!b) {
                                             // 用户还是想用我的 APP 的
                                                // 提示用户去应用设置界面手动开启权限
                                               showDialogTipUserGoToAppSettting();
                                        } else
                                               finish();
                                     } else {
                                        Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                                     }
                            }
                    }
            }


    // 提示用户去应用设置界面手动开启权限
                 private void showDialogTipUserGoToAppSettting() {

                 dialog = new AlertDialog.Builder(this)
                       .setTitle("存储权限不可用")
                         .setMessage("请在-应用设置-权限-中，允许效率日志使用存储权限来保存用户数据")
                        .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                               @Override
                                public void onClick(DialogInterface dialog, int which) {
                                      // 跳转到应用设置界面
                                       goToAppSetting();
                                   }
                            })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                   }
                            }).setCancelable(false).show();
             }

    // 跳转到当前应用的设置界面
    private void goToAppSetting() {
        Intent intent = new Intent();

        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);

        startActivityForResult(intent, 123);
    }

    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 检查该权限是否已经获取
                int i = ContextCompat.checkSelfPermission(this, permissions[0]);
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (i != PackageManager.PERMISSION_GRANTED) {
                    // 提示用户应该去应用设置界面手动开启权限
                    showDialogTipUserGoToAppSettting();
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private void initView(){
        messageBtn = (RadioButton) findViewById(R.id.MessageCenter);
        workBtn = (RadioButton) findViewById(R.id.WorkCenter);
        userBtn = (RadioButton) findViewById(R.id.UserCenter);
        proBtn = (RadioButton) findViewById(R.id.ProCenter);
        proBtn.setOnClickListener(this);
        messageBtn.setOnClickListener(this);
        workBtn.setOnClickListener(this);
        userBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.MessageCenter:
                setTabSelection(MESSAGE_CENTER);
                break;
            case R.id.WorkCenter:
                setTabSelection(WORK_CENTER);
                break;
            case R.id.ProCenter:
                setTabSelection(PROJECT_CENTER);
                break;
            case R.id.UserCenter:
                setTabSelection(USER_CENTER);
                break;
        }
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (messageFragment != null) {
            transaction.hide(messageFragment);
        }
        if (workFragment != null) {
            transaction.hide(workFragment);
        }
        if (projectFragment != null) {
            transaction.hide(projectFragment);
        }
        if (userFragment != null) {
            transaction.hide(userFragment);
        }
    }

    private void setTabSelection(int position) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (position) {
            case MESSAGE_CENTER:
                change(MESSAGE_CENTER);
                if (messageFragment == null) {
                    messageFragment = new MessageFragment();
                    transaction.add(R.id.changeFragment, messageFragment);
                } else {
                    transaction.show(messageFragment);
                }
                break;
            case WORK_CENTER:
                change(WORK_CENTER);
                // 是否添加过frgament
                if (workFragment == null) {
                    workFragment = new WorkFragment();
                    transaction.add(R.id.changeFragment, workFragment);
                } else {
                    transaction.show(workFragment);
                }
                break;
            case PROJECT_CENTER:
                change(PROJECT_CENTER);
                // 是否添加过frgament
                if (projectFragment == null) {
                    projectFragment = new ProjectFragment();
                    transaction.add(R.id.changeFragment, projectFragment);
                } else {
                    transaction.show(projectFragment);
                }
                break;
            case USER_CENTER:
                change(USER_CENTER);
                if (userFragment == null) {
                    userFragment = new UserFragment();
                    transaction.add(R.id.changeFragment, userFragment);
                } else {
                    transaction.show(userFragment);
                }
                break;
        }
        transaction.commit();
    }

    private void initBroad(){
        pushReceive  = new PushReceive();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(PUSH_MESSAGE);
        registerReceiver(pushReceive,intentFilter);
    }

    private class PushReceive extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            messageFragment.pushMessage(intent.getStringExtra("message"));
        }
    }

    private void change(int index) {
        switch (index) {
            case 0:
                messageBtn.setChecked(true);
                workBtn.setChecked(false);
                proBtn.setChecked(false);
                userBtn.setChecked(false);
                messageBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.message_pressed), null, null);
                workBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.log_normal), null, null);
                proBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.pro_normal), null, null);
                userBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.user_normal), null, null);
                messageBtn.setTextColor(getResources().getColor(R.color.tabColor));
                workBtn.setTextColor(getResources().getColor(R.color.tabTex));
                proBtn.setTextColor(getResources().getColor(R.color.tabTex));
                userBtn.setTextColor(getResources().getColor(R.color.tabTex));
                break;

            case 1:
                messageBtn.setChecked(false);
                workBtn.setChecked(true);
                proBtn.setChecked(false);
                userBtn.setChecked(false);
                messageBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.message_normal), null, null);
                workBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.log_pressed), null, null);
                proBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.pro_normal), null, null);
                userBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.user_normal), null, null);
                messageBtn.setTextColor(getResources().getColor(R.color.tabTex));
                workBtn.setTextColor(getResources().getColor(R.color.tabColor));
                proBtn.setTextColor(getResources().getColor(R.color.tabTex));
                userBtn.setTextColor(getResources().getColor(R.color.tabTex));
                break;
                case 2:
                messageBtn.setChecked(false);
                workBtn.setChecked(false);
                proBtn.setChecked(true);
                userBtn.setChecked(false);
                messageBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.message_normal), null, null);
                workBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.log_normal), null, null);
                proBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.pro_pressed), null, null);
                userBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.user_normal), null, null);
                messageBtn.setTextColor(getResources().getColor(R.color.tabTex));
                workBtn.setTextColor(getResources().getColor(R.color.tabTex));
                proBtn.setTextColor(getResources().getColor(R.color.tabColor));
                userBtn.setTextColor(getResources().getColor(R.color.tabTex));
                break;
            case 3:
                messageBtn.setChecked(false);
                workBtn.setChecked(false);
                proBtn.setChecked(false);
                userBtn.setChecked(true);
                messageBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.message_normal), null, null);
                workBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.log_normal), null, null);
                proBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.pro_normal), null, null);
                userBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.user_pressed), null, null);
                messageBtn.setTextColor(getResources().getColor(R.color.tabTex));
                workBtn.setTextColor(getResources().getColor(R.color.tabTex));
                proBtn.setTextColor(getResources().getColor(R.color.tabTex));
                userBtn.setTextColor(getResources().getColor(R.color.tabColor));
                break;
        }
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        position = savedInstanceState.getInt("position");
        setTabSelection(position);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // 记录当前的position
        outState.putInt("position", position);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 注意
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}
