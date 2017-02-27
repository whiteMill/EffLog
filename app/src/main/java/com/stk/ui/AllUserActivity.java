
package com.stk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.stk.adapter.UserAdapter;
import com.stk.efflog.R;
import com.stk.model.ContactUser;
import com.stk.utils.HandVew;

import java.io.Serializable;
import java.util.List;


public class AllUserActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView back;
    private ListView sListView;
    private TextView sTextView;
    private HandVew sHandView;
    private UserAdapter userAdapter;
    private List<ContactUser> userArrayList = null;

    private TextView save;
    private final static int USER_CODE = 324234;
    private final static int USER_DELETE = 324343234;

    private final static int ADD_MANA = 5454;
    private final static int DELETE_MANA = 3535;

    private Intent priIntent;
    private String flag;
    private TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user);
        initView();
        priIntent = getIntent();

        userArrayList = (List<ContactUser>)priIntent.getSerializableExtra("userList");

        flag = priIntent.getStringExtra("flag");

        if(flag.equals("delete")||flag.equals("deleteMana")){
            titleText.setText("移除成员");
            save.setText("移除");
            for (int i = 0; i < userArrayList.size(); i++) {
                userArrayList.get(i).setCheck(false);
            }
        }

        initDate();
        sHandView.setmTextView(sTextView);
        sHandView.setOnTouchCharacterListener(new HandVew.onTouchCharacterListener() {
            @Override
            public void touchCharacterListener(String s) {
                int position = userAdapter.getSelection(s);
                Log.d("DDD",position+"");
                if(position!=-1){
                    sListView.setSelection(position);
                }
            }
        });
    }

    private void initDate(){
        userAdapter  =  new UserAdapter(AllUserActivity.this,userArrayList);
        sListView.setAdapter(userAdapter);
    }

    private void initView() {
        titleText = (TextView) findViewById(R.id.titleText);
        back = (ImageView) findViewById(R.id.back);
        sListView = (ListView) findViewById(R.id.sListView);
        sTextView = (TextView) findViewById(R.id.sTextView);
        sHandView = (HandVew) findViewById(R.id.sHandView);
        save = (TextView) findViewById(R.id.save);
        save.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.save:
                   if(flag.equals("add")){
                       for (int i = 0; i < userArrayList.size(); i++) {
                           if(userAdapter.getIsSelected().get(i)){
                               userArrayList.get(i).setCheck(true);
                           }else{
                               userArrayList.get(i).setCheck(false);
                           }
                       }
                       Intent intent = new Intent();
                       intent.putExtra("list", (Serializable)userArrayList);
                       setResult(USER_CODE,intent);
                       finish();
                   }else if(flag.equals("delete")){
                       for (int i = 0; i < userArrayList.size(); i++) {
                           if(userAdapter.getIsSelected().get(i)){
                               userArrayList.get(i).setCheck(true);
                           }else{
                               userArrayList.get(i).setCheck(false);
                           }
                       }
                       Intent intent = new Intent();
                       intent.putExtra("list", (Serializable)userArrayList);
                       setResult(USER_DELETE,intent);
                       finish();
                   }else if(flag.equals("addMana")){
                       for (int i = 0; i < userArrayList.size(); i++) {
                           if(userAdapter.getIsSelected().get(i)){
                               userArrayList.get(i).setCheck(true);
                           }else{
                               userArrayList.get(i).setCheck(false);
                           }
                       }
                       Intent intent = new Intent();
                       intent.putExtra("list", (Serializable)userArrayList);
                       setResult(ADD_MANA,intent);
                       finish();
                   }else{
                       for (int i = 0; i < userArrayList.size(); i++) {
                           if(userAdapter.getIsSelected().get(i)){
                               userArrayList.get(i).setCheck(true);
                           }else{
                               userArrayList.get(i).setCheck(false);
                           }
                       }
                       Intent intent = new Intent();
                       intent.putExtra("list", (Serializable)userArrayList);
                       setResult(DELETE_MANA,intent);
                       finish();
                   }

                break;
        }
    }

}
