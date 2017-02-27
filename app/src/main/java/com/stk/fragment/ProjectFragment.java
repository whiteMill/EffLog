package com.stk.fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stk.efflog.R;
import com.stk.profragment.BussFragment;
import com.stk.profragment.ProFragment;
import com.stk.ui.AddBussActivity;
import com.stk.ui.AddProActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectFragment extends Fragment implements View.OnClickListener{

    private TextView bussTex;
    private TextView proTex;
    private ViewPager proViewPager;
    private BussFragment bussFragment;
    private ProFragment proFragment;
    private LinearLayout mLayout;
    private View view;
    private ImageView addPro;
    private List<Fragment> fragmentList = new ArrayList<>();
    private AlertDialog.Builder builder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_project, container, false);
        initView();
        return view;
    }

    private void initView() {
        addPro = (ImageView) view.findViewById(R.id.addPro);
        bussTex = (TextView) view.findViewById(R.id.bussTex);
        proTex = (TextView) view.findViewById(R.id.proTex);
        proViewPager = (ViewPager) view.findViewById(R.id.proViewPager);
        mLayout = (LinearLayout) view.findViewById(R.id.mLayout);
        bussTex.setOnClickListener(this);
        proTex.setOnClickListener(this);
        addPro.setOnClickListener(this);
        bussFragment = new BussFragment();
        proFragment = new ProFragment();
        fragmentList.add(bussFragment);
        fragmentList.add(proFragment);

        proViewPager.setAdapter(new MyViewPagerAdapter(getActivity().getSupportFragmentManager()));
        proViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    public void change(int index){
        switch (index) {
            case 0:
                bussTex.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                bussTex.setBackgroundColor(getResources().getColor(R.color.layoutBackground));
                proTex.setTextColor(getResources().getColor(R.color.layoutBackground));
                proTex.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
               // mLayout.setBackground(getResources().getDrawable(drawable.contact_shape));
                break;
            case 1:
                bussTex.setTextColor(getResources().getColor(R.color.layoutBackground));
                bussTex.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                proTex.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                proTex.setBackgroundColor(getResources().getColor(R.color.layoutBackground));
               // mLayout.setBackground(getResources().getDrawable(drawable.contact_shape));
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



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bussTex:
                proViewPager.setCurrentItem(0);
                change(0);
                break;
            case R.id.proTex:
                proViewPager.setCurrentItem(1);
                change(1);
                break;
            case R.id.addPro:
                builder = new AlertDialog.Builder(getActivity());
                builder.setItems(getResources().getStringArray(R.array.proItem), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        switch (arg1) {
                            case 0:
                                Intent intent = new Intent(getActivity(), AddProActivity.class);
                                startActivity(intent);
                                break;
                            case 1:
                                Intent intent1 = new Intent(getActivity(), AddBussActivity.class);
                                startActivity(intent1);
                                break;
                        }
                        arg0.dismiss();
                    }
                });
                builder.show();

                break;
        }
    }
}
