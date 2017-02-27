package com.stk.utils;


import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.stk.efflog.R;


public class LogPopwindow extends PopupWindow implements OnClickListener {

    private Context mContext;
    private Handler mHandle;

    public LogPopwindow(Context mContext, Handler mHandle) {
        super();
        this.mContext = mContext;
        this.mHandle = mHandle;
        View view = LayoutInflater.from(mContext).inflate(R.layout.popwindow_log, new LinearLayout(mContext),
                false);
        setContentView(view);
        TextView dayLog = (TextView) view.findViewById(R.id.dayLog);
        TextView weekLog = (TextView) view.findViewById(R.id.weekLog);
        TextView monthLog = (TextView) view.findViewById(R.id.monthLog);
        TextView seasonLog = (TextView) view.findViewById(R.id.seasonLog);
        TextView yearLog = (TextView) view.findViewById(R.id.yearLog);

        TextView btnCancle = (TextView) view.findViewById(R.id.btnCancle);
        dayLog.setOnClickListener(this);
        weekLog.setOnClickListener(this);
        monthLog.setOnClickListener(this);
        seasonLog.setOnClickListener(this);
        yearLog.setOnClickListener(this);
        btnCancle.setOnClickListener(this);

        setWidth(ScreenUtils.getDeviceWidth(mContext));
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new BitmapDrawable());
        setOutsideTouchable(false);
        setFocusable(true);
        setAnimationStyle(R.style.take_photo_anim);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dayLog:
                mHandle.sendEmptyMessage(111);
                break;
            case R.id.weekLog:
                mHandle.sendEmptyMessage(222);
                break;
            case R.id.monthLog:
                mHandle.sendEmptyMessage(333);
                break;
            case R.id.seasonLog:
                mHandle.sendEmptyMessage(444);
                break;
            case R.id.yearLog:
                mHandle.sendEmptyMessage(555);
                break;
            case R.id.btnCancle:
                mHandle.sendEmptyMessage(666);
                break;

            default:
                break;
        }
    }

}
