package com.stk.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stk.efflog.R;
import com.stk.logfragment.DayLogFragment;
import com.stk.logfragment.MonthLogFragment;
import com.stk.logfragment.SeasonLogFragment;
import com.stk.logfragment.WeekLogFragment;
import com.stk.logfragment.YearLogFragment;
import com.stk.ui.ChooseLogActivity;
import com.stk.ui.LogUpdateActivity;
import com.stk.utils.LogPopwindow;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkFragment extends Fragment implements View.OnClickListener{

    private View view;
    private LogPopwindow logPopwindow;
    private TextView addLog;
    private ViewPager mViewPager;

    private RelativeLayout dayLog;
    private RelativeLayout weekLog;
    private RelativeLayout monthLog;
    private RelativeLayout seasonLog;
    private RelativeLayout yearLog;

    private TextView day;
    private TextView week;
    private TextView month;
    private TextView season;
    private TextView year;

    private ImageView dayIm;
    private ImageView weekIm;
    private ImageView monthIm;
    private ImageView seasonIm;
    private ImageView yearIm;

    private DayLogFragment dayFragment;
    private WeekLogFragment weekFragment;
    private MonthLogFragment monthFragment;
    private SeasonLogFragment seasonFragment;
    private YearLogFragment yearFragment;

    private List<Fragment> fragmentList = new ArrayList<>();

    private TextView chooseText;

    private int currentPage=0;

    public static final String UPDATE_ACTION = "update_action";


    public WorkFragment() {

    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 111:
                    queryIsExist("01");
                    break;
                case 222:
                    queryIsExist("02");
                    break;
                case 333:
                    queryIsExist("03");
                    break;
                case 444:
                    queryIsExist("04");
                    break;
                case 555:
                    queryIsExist("05");
                    break;
                case 666:
                    logPopwindow.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_work, container, false);
        initView();
        IntentFilter filter = new IntentFilter(UPDATE_ACTION);
        getActivity().registerReceiver(updateReceiver, filter);
        return view;
    }

    BroadcastReceiver updateReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("sdfasfasfvvv","jhajjjaj");
            switch (currentPage){
                case 0:
                    dayFragment.freshData();
                    break;
                case 1:
                    weekFragment.freshData();
                    break;
                case 2:
                    monthFragment.freshData();
                    break;
                case 3:
                    seasonFragment.freshData();
                    break;
                case 4:
                    yearFragment.freshData();
                    break;
            }
        }
    };


    private void initView() {
        dayFragment = new DayLogFragment();
        weekFragment = new WeekLogFragment();
        monthFragment = new MonthLogFragment();
        seasonFragment = new SeasonLogFragment();
        yearFragment = new YearLogFragment();

        fragmentList.add(dayFragment);
        fragmentList.add(weekFragment);
        fragmentList.add(monthFragment);
        fragmentList.add(seasonFragment);
        fragmentList.add(yearFragment);

        dayLog = (RelativeLayout) view.findViewById(R.id.dayLog);
        weekLog = (RelativeLayout) view.findViewById(R.id.weekLog);
        monthLog = (RelativeLayout) view.findViewById(R.id.monthLog);
        seasonLog = (RelativeLayout) view.findViewById(R.id.seasonLog);
        yearLog = (RelativeLayout) view.findViewById(R.id.yearLog);
        chooseText = (TextView) view.findViewById(R.id.chooseText);

        chooseText.setOnClickListener(this);
        dayLog.setOnClickListener(this);
        weekLog.setOnClickListener(this);
        monthLog.setOnClickListener(this);
        seasonLog.setOnClickListener(this);
        yearLog.setOnClickListener(this);

        day = (TextView) view.findViewById(R.id.day);
        week = (TextView) view.findViewById(R.id.week);
        month = (TextView) view.findViewById(R.id.month);
        season = (TextView) view.findViewById(R.id.season);
        year = (TextView) view.findViewById(R.id.year);

        dayIm = (ImageView) view.findViewById(R.id.dayIm);
        weekIm = (ImageView) view.findViewById(R.id.weekIm);
        monthIm = (ImageView) view.findViewById(R.id.monthIm);
        seasonIm = (ImageView) view.findViewById(R.id.seasonIm);
        yearIm = (ImageView) view.findViewById(R.id.yearIm);

        mViewPager = (ViewPager) view.findViewById(R.id.mViewPager);
        addLog = (TextView) view.findViewById(R.id.addLog);
        logPopwindow = new LogPopwindow(getActivity(),mHandler);
        addLog.setOnClickListener(this);

        mViewPager.setAdapter(new MyViewPagerAdapter(getActivity().getSupportFragmentManager()));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Log.d("dasdasffffff",position+"");
                currentPage  = position;
                change(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    /*@Override
    public void onResume() {
        super.onResume();
        switch (currentPage){
            case 0:
                dayFragment.freshData();
                break;
            case 1:
                weekFragment.freshData();
                break;
            case 2:
                monthFragment.freshData();
                break;
            case 3:
                seasonFragment.freshData();
                break;
            case 4:
                yearFragment.freshData();
                break;
        }


    }*/

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

    public void change(int index){
        switch (index) {
            case 0:
                day.setTextColor(getActivity().getResources().getColor(R.color.tabColor));
                week.setTextColor(getActivity().getResources().getColor(R.color.black));
                month.setTextColor(getActivity().getResources().getColor(R.color.black));
                season.setTextColor(getActivity().getResources().getColor(R.color.black));
                year.setTextColor(getActivity().getResources().getColor(R.color.black));

                dayIm.setVisibility(View.VISIBLE);
                weekIm.setVisibility(View.INVISIBLE);
                monthIm.setVisibility(View.INVISIBLE);
                seasonIm.setVisibility(View.INVISIBLE);
                yearIm.setVisibility(View.INVISIBLE);

                dayIm.setBackgroundColor(getActivity().getResources().getColor(R.color.tabColor));
                weekIm.setBackgroundColor(getActivity().getResources().getColor(R.color.black));
                monthIm.setBackgroundColor(getActivity().getResources().getColor(R.color.black));
                seasonIm.setBackgroundColor(getActivity().getResources().getColor(R.color.black));
                yearIm.setBackgroundColor(getActivity().getResources().getColor(R.color.black));
                break;

            case 1:

                day.setTextColor(getActivity().getResources().getColor(R.color.black));
                week.setTextColor(getActivity().getResources().getColor(R.color.tabColor));
                month.setTextColor(getActivity().getResources().getColor(R.color.black));
                season.setTextColor(getActivity().getResources().getColor(R.color.black));
                year.setTextColor(getActivity().getResources().getColor(R.color.black));

                dayIm.setVisibility(View.INVISIBLE);
                weekIm.setVisibility(View.VISIBLE);
                monthIm.setVisibility(View.INVISIBLE);
                seasonIm.setVisibility(View.INVISIBLE);
                yearIm.setVisibility(View.INVISIBLE);

                dayIm.setBackgroundColor(getActivity().getResources().getColor(R.color.black));
                weekIm.setBackgroundColor(getActivity().getResources().getColor(R.color.tabColor));
                monthIm.setBackgroundColor(getActivity().getResources().getColor(R.color.black));
                seasonIm.setBackgroundColor(getActivity().getResources().getColor(R.color.black));
                yearIm.setBackgroundColor(getActivity().getResources().getColor(R.color.black));
                break;
            case 2:
                day.setTextColor(getActivity().getResources().getColor(R.color.black));
                week.setTextColor(getActivity().getResources().getColor(R.color.black));
                month.setTextColor(getActivity().getResources().getColor(R.color.tabColor));
                season.setTextColor(getActivity().getResources().getColor(R.color.black));
                year.setTextColor(getActivity().getResources().getColor(R.color.black));

                dayIm.setVisibility(View.INVISIBLE);
                weekIm.setVisibility(View.INVISIBLE);
                monthIm.setVisibility(View.VISIBLE);
                seasonIm.setVisibility(View.INVISIBLE);
                yearIm.setVisibility(View.INVISIBLE);

                dayIm.setBackgroundColor(getActivity().getResources().getColor(R.color.black));
                weekIm.setBackgroundColor(getActivity().getResources().getColor(R.color.black));
                monthIm.setBackgroundColor(getActivity().getResources().getColor(R.color.tabColor));
                seasonIm.setBackgroundColor(getActivity().getResources().getColor(R.color.black));
                yearIm.setBackgroundColor(getActivity().getResources().getColor(R.color.black));
                break;
            case 3:

                day.setTextColor(getActivity().getResources().getColor(R.color.black));
                week.setTextColor(getActivity().getResources().getColor(R.color.black));
                month.setTextColor(getActivity().getResources().getColor(R.color.black));
                season.setTextColor(getActivity().getResources().getColor(R.color.tabColor));
                year.setTextColor(getActivity().getResources().getColor(R.color.black));

                dayIm.setVisibility(View.INVISIBLE);
                weekIm.setVisibility(View.INVISIBLE);
                monthIm.setVisibility(View.INVISIBLE);
                seasonIm.setVisibility(View.VISIBLE);
                yearIm.setVisibility(View.INVISIBLE);

                dayIm.setBackgroundColor(getActivity().getResources().getColor(R.color.black));
                weekIm.setBackgroundColor(getActivity().getResources().getColor(R.color.black));
                monthIm.setBackgroundColor(getActivity().getResources().getColor(R.color.black));
                seasonIm.setBackgroundColor(getActivity().getResources().getColor(R.color.tabColor));
                yearIm.setBackgroundColor(getActivity().getResources().getColor(R.color.black));
                break;
            case 4:
                day.setTextColor(getActivity().getResources().getColor(R.color.black));
                week.setTextColor(getActivity().getResources().getColor(R.color.black));
                month.setTextColor(getActivity().getResources().getColor(R.color.black));
                season.setTextColor(getActivity().getResources().getColor(R.color.black));
                year.setTextColor(getActivity().getResources().getColor(R.color.tabColor));

                dayIm.setVisibility(View.INVISIBLE);
                weekIm.setVisibility(View.INVISIBLE);
                monthIm.setVisibility(View.INVISIBLE);
                seasonIm.setVisibility(View.INVISIBLE);
                yearIm.setVisibility(View.VISIBLE);

                dayIm.setBackgroundColor(getActivity().getResources().getColor(R.color.black));
                weekIm.setBackgroundColor(getActivity().getResources().getColor(R.color.black));
                monthIm.setBackgroundColor(getActivity().getResources().getColor(R.color.black));
                seasonIm.setBackgroundColor(getActivity().getResources().getColor(R.color.black));
                yearIm.setBackgroundColor(getActivity().getResources().getColor(R.color.tabColor));
                break;
        }
    }

    private void queryIsExist(final String type){
        Intent intent = new Intent(getActivity(), LogUpdateActivity.class);
        intent.putExtra("log_type",type);
        startActivity(intent);
        logPopwindow.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addLog:
                if (logPopwindow != null) {
                    if (!logPopwindow.isShowing()) {
                        logPopwindow.showAtLocation(view.findViewById(R.id.addLog),
                                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    } else {
                        logPopwindow.dismiss();
                    }
                }
                break;
            case R.id.dayLog:
                mViewPager.setCurrentItem(0);
                change(0);
                break;
            case R.id.weekLog:
                mViewPager.setCurrentItem(1);
                change(1);
                break;
            case R.id.monthLog:
                mViewPager.setCurrentItem(2);
                change(2);
                break;
            case R.id.seasonLog:
                mViewPager.setCurrentItem(3);
                change(3);
                break;
            case R.id.yearLog:
                mViewPager.setCurrentItem(4);
                change(4);
                break;
            case R.id.chooseText:
                Intent intent = new Intent(getActivity(), ChooseLogActivity.class);
                startActivity(intent);
                break;
        }
    }
}
