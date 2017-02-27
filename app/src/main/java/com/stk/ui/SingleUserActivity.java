package com.stk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.stk.adapter.SingleUserAdapter;
import com.stk.efflog.R;
import com.stk.model.ManPeoContact;
import com.stk.utils.HandVew;

import java.util.List;

public class SingleUserActivity extends AppCompatActivity implements View.OnClickListener{

    private List<ManPeoContact> manPeoContactList = null;
    private Intent intent;
    private ImageView back;
    private ListView sListView;
    private TextView sTextView;
    private HandVew sHandView;
    private SingleUserAdapter userAdapter;
    private final static  int RESULT_CODE = 8569;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_user);
        initView();
        intent = getIntent();
        manPeoContactList = (List<ManPeoContact>) intent.getSerializableExtra("list");

        userAdapter  =  new SingleUserAdapter(SingleUserActivity.this,manPeoContactList);
        sListView.setAdapter(userAdapter);
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
        sListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent();
                    intent.putExtra("USER_ID",manPeoContactList.get(position).getUSER_ID());
                    intent.putExtra("USRE_NAME",manPeoContactList.get(position).getNAME());
                    Log.d("msdsmsmmaa",manPeoContactList.get(position).toString());
                    setResult(RESULT_CODE,intent);
                    finish();
            }
        });
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        sListView = (ListView) findViewById(R.id.sListView);
        sTextView = (TextView) findViewById(R.id.sTextView);
        sHandView = (HandVew) findViewById(R.id.sHandView);
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
}
