package com.stk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stk.efflog.R;
import com.stk.updatefragment.BussFragment;
import com.stk.updatefragment.ProjectFragment;
import com.stk.updatefragment.UpdateLogFragment;

import java.util.ArrayList;
import java.util.List;

import static com.stk.efflog.R.id.updateViewPager;

public class LogUpdateActivity extends AppCompatActivity implements View.OnClickListener{

    private ViewPager viewPager;
    private UpdateLogFragment updateLogFragment;
    private ProjectFragment projectFragment;
    private BussFragment bussFragment;
    private List<Fragment> fragmentList = new ArrayList<>();

    private RelativeLayout logLayout;
    private RelativeLayout proLayout;
    private RelativeLayout bussLayout;

    private TextView logText;
    private TextView proText;
    private TextView bussText;

    private ImageView logIm;
    private ImageView proIm;
    private ImageView bussIm;

    private String log_type;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_update);
        initView();
        intent = getIntent();
        log_type = intent.getStringExtra("log_type");
        updateLogFragment.getType(log_type);

        projectFragment.setChangePage(new ProjectFragment.ChangePage() {
            @Override
            public void setCurPage(int page) {
                viewPager.setCurrentItem(page);
            }
        });

        bussFragment.setChangePage(new ProjectFragment.ChangePage() {
            @Override
            public void setCurPage(int page) {
                viewPager.setCurrentItem(page);
            }
        });
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(updateViewPager);

        updateLogFragment  = new UpdateLogFragment();
        projectFragment = new ProjectFragment();
        bussFragment = new BussFragment();

        fragmentList.add(updateLogFragment);
        fragmentList.add(projectFragment);
        fragmentList.add(bussFragment);

        logLayout = (RelativeLayout) findViewById(R.id.logLayout);
        proLayout = (RelativeLayout) findViewById(R.id.proLayout);
        bussLayout = (RelativeLayout) findViewById(R.id.bussLayout);

        logLayout.setOnClickListener(this);
        proLayout.setOnClickListener(this);
        bussLayout.setOnClickListener(this);

        logText = (TextView) findViewById(R.id.logText);
        proText = (TextView) findViewById(R.id.proText);
        bussText = (TextView) findViewById(R.id.bussText);

        logIm = (ImageView) findViewById(R.id.logIm);
        proIm = (ImageView) findViewById(R.id.proIm);
        bussIm = (ImageView) findViewById(R.id.bussIm);


        viewPager.setAdapter(new MyViewPagerAdapter(this.getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                change(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void change(int position) {
       switch (position){
           case 0:
               logText.setTextColor(getResources().getColor(R.color.tabColor));
               proText.setTextColor(getResources().getColor(R.color.black));
               bussText.setTextColor(getResources().getColor(R.color.black));

               logIm.setVisibility(View.VISIBLE);
               proIm.setVisibility(View.INVISIBLE);
               bussIm.setVisibility(View.INVISIBLE);

               logIm.setBackgroundColor(getResources().getColor(R.color.tabColor));
               proIm.setBackgroundColor(getResources().getColor(R.color.white));
               bussIm.setBackgroundColor(getResources().getColor(R.color.white));
               break;

           case 1:
               logText.setTextColor(getResources().getColor(R.color.black));
               proText.setTextColor(getResources().getColor(R.color.tabColor));
               bussText.setTextColor(getResources().getColor(R.color.black));

               logIm.setVisibility(View.INVISIBLE);
               proIm.setVisibility(View.VISIBLE);
               bussIm.setVisibility(View.INVISIBLE);

               logIm.setBackgroundColor(getResources().getColor(R.color.white));
               proIm.setBackgroundColor(getResources().getColor(R.color.tabColor));
               bussIm.setBackgroundColor(getResources().getColor(R.color.white));
               break;

           case 2:
               logText.setTextColor(getResources().getColor(R.color.black));
               proText.setTextColor(getResources().getColor(R.color.black));
               bussText.setTextColor(getResources().getColor(R.color.tabColor));

               logIm.setVisibility(View.INVISIBLE);
               proIm.setVisibility(View.INVISIBLE);
               bussIm.setVisibility(View.VISIBLE);

               logIm.setBackgroundColor(getResources().getColor(R.color.white));
               proIm.setBackgroundColor(getResources().getColor(R.color.white));
               bussIm.setBackgroundColor(getResources().getColor(R.color.tabColor));
               break;
       }

    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.logLayout:
               viewPager.setCurrentItem(0);
               change(0);
               break;

           case R.id.proLayout:
               viewPager.setCurrentItem(1);
               change(1);
               break;

           case R.id.bussLayout:
               viewPager.setCurrentItem(2);
               change(2);
               break;
       }
    }


    private class MyViewPagerAdapter extends FragmentPagerAdapter {

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }


}
