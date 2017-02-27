package com.stk.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.stk.efflog.R;


/**
 * Created by admin on 2016/11/2.
 */
public class HandVew extends View {

    private Paint textPaint = new Paint();

    private String[] textString = new String[]{
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};
    //鼠标点击、滑动时选择的字母
    private int selectPosition = -1;

    //当前文字显示
    private TextView mTextView;

    public HandVew(Context context) {
        super(context);
    }

    public HandVew(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HandVew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initPaint() {
        textPaint.setTextSize(20);
        textPaint.setColor(Color.BLACK);
        textPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawText(canvas);
    }

    private void drawText(Canvas canvas) {
        int width = getWidth();
        int heigh = getHeight();

        int singleHeigh = heigh / textString.length;

        for (int i = 0; i < textString.length; i++) {
            initPaint();
            if (selectPosition == i) {
                textPaint.setColor(getResources().getColor(R.color.colorPrimaryDark));
            }
            //计算每个字母的坐标
            float X = (width - textPaint.measureText(textString[i])) / 2;
            float Y = (i + 1) * singleHeigh;
            canvas.drawText(textString[i], X, Y, textPaint);
            textPaint.reset();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //计算选中字母
        int index = (int) (event.getY()/getHeight()*textString.length);
        Log.d("当前选中字母",index+"");
        if(index>=textString.length-1){
            index = textString.length-1;
        }else if(index<0){
            index=0;
        }
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                setBackgroundColor(getResources().getColor(R.color.layoutBackground));
                selectPosition=index;
                mTextView.setVisibility(View.VISIBLE);
                mTextView.setText(textString[selectPosition]);
                if(onTouchCharacterListener!=null){
                    onTouchCharacterListener.touchCharacterListener(textString[selectPosition]);
                }
                //重新绘制
                invalidate();
                break;
            default:
                setBackgroundColor(getResources().getColor(R.color.backgroundColor));
                selectPosition=-1;
                mTextView.setVisibility(View.GONE);
                invalidate();
                break;
        }
        return true;
    }

    private onTouchCharacterListener onTouchCharacterListener;

    public void setOnTouchCharacterListener(HandVew.onTouchCharacterListener onTouchCharacterListener) {
        this.onTouchCharacterListener = onTouchCharacterListener;
    }

    public interface  onTouchCharacterListener{
        void touchCharacterListener(String s);
    }

    public void setmTextView(TextView mTextView) {
        this.mTextView = mTextView;
    }
}
