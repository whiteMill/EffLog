package com.stk.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stk.efflog.R;
import com.stk.model.LogVo;

import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;

/**
 * Created by admin on 2016/12/30.
 */

public class LogUpdateAdapter extends BaseAdapter {
    private Context context;
    private List<LogVo> logVoArrayList;

    private HashMap<Integer,Boolean> isSelected;

    private  HashMap<Integer,String> logMindMap;

    private int mTouchItemPosition = -1;

    public HashMap<Integer, String> getLogMindMap() {
        return logMindMap;
    }

    public void setLogMindMap(HashMap<Integer, String> logMindMap) {
        this.logMindMap = logMindMap;
    }

    public HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        this.isSelected = isSelected;
    }

    public LogUpdateAdapter(Context context, List<LogVo> logVoArrayList) {
        this.context = context;
        this.logVoArrayList = logVoArrayList;
        isSelected = new HashMap<>();
        logMindMap = new HashMap<>();
        initCheckBox();
    }

    private void initCheckBox(){
        for (int i = 0; i < logVoArrayList.size(); i++) {
            if(logVoArrayList.get(i).getLOG_FLAG().equals("0")){
                isSelected.put(i,true);
            }else{
                isSelected.put(i,false);
            }
           logMindMap.put(i,logVoArrayList.get(i).getLOG_MIND());
        }
    }

    @Override
    public int getCount() {
        return logVoArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return logVoArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LogViewHolder logViewHolder = null;
        if(convertView==null){
            logViewHolder = new LogViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.update_layout,null);
            logViewHolder.content  = (TextView) convertView.findViewById(R.id.log_content);
            logViewHolder.tv_firstCharacter = (TextView) convertView.findViewById(R.id.tv_firstCharacter);
            logViewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.isComplete);
            logViewHolder.mindEditText  = (EditText) convertView.findViewById(R.id.log_mind_edit);
            logViewHolder.edit_layout = (RelativeLayout) convertView.findViewById(R.id.edit_layout);
            logViewHolder.checkBox.setVisibility(View.VISIBLE);

            logViewHolder.mindEditText.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    //注意，此处必须使用getTag的方式，不能将position定义为final，写成mTouchItemPosition = position
                    mTouchItemPosition = (Integer) view.getTag();

                    //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
                    if ((view.getId() == R.id.log_mind_edit && canVerticalScroll((EditText)view))) {
                        view.getParent().requestDisallowInterceptTouchEvent(true);
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            view.getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }
                    return false;
                }
            });

            // 让ViewHolder持有一个TextWathcer，动态更新position来防治数据错乱；不能将position定义成final直接使用，必须动态更新
            logViewHolder.mTextWatcher = new MyTextWatcher();
            logViewHolder.mindEditText.addTextChangedListener(logViewHolder.mTextWatcher);
            logViewHolder.updatePosition(position);


            convertView.setTag(logViewHolder);
        }else{
            logViewHolder = (LogViewHolder) convertView.getTag();
            //动态更新TextWathcer的position
            logViewHolder.updatePosition(position);
        }

        logViewHolder.edit_layout.setVisibility(View.VISIBLE);

        if(!logMindMap.get(position).equals("null")&&logMindMap.get(position).length()!=0){
            logViewHolder.mindEditText.setText(logMindMap.get(position));
        }else{
            logViewHolder.mindEditText.setText("");
        }

        logViewHolder.mindEditText.setTag(position);



        if (mTouchItemPosition == position) {
            logViewHolder.mindEditText.requestFocus();
            logViewHolder.mindEditText.setSelection(logViewHolder.mindEditText.getText().length());
        } else {
            logViewHolder.mindEditText.clearFocus();
        }


        if(position==0){
            logViewHolder.tv_firstCharacter.setVisibility(View.VISIBLE);
            equalLevel(position,logViewHolder);
            logViewHolder.content.setText(logVoArrayList.get(position).getLOG_CONTENT());
        }else{
            if(logVoArrayList.get(position).getLOG_LEVEL().compareTo(logVoArrayList.get(position-1).getLOG_LEVEL())>0){
                logViewHolder.tv_firstCharacter.setVisibility(View.VISIBLE);
                equalLevel(position,logViewHolder);
                logViewHolder.content.setText(logVoArrayList.get(position).getLOG_CONTENT());
            }else{
                logViewHolder.tv_firstCharacter.setVisibility(GONE);
                logViewHolder.content.setText(logVoArrayList.get(position).getLOG_CONTENT());
            }
        }



        logViewHolder.checkBox.setChecked(isSelected.get(position));

        logViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    isSelected.put(position,true);
                }else{
                    isSelected.put(position,false);
                }
            }
        });
        return convertView;
    }



    /**
     * EditText竖直方向是否可以滚动
     * @param editText 需要判断的EditText
     * @return true：可以滚动 false：不可以滚动
     */
    private boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() -editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if(scrollDifference == 0) {
            return false;
        }

        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }


    static final class  LogViewHolder{
        private TextView content;
        private TextView tv_firstCharacter;
        private CheckBox checkBox;
        private RelativeLayout edit_layout;
        private EditText mindEditText;
        MyTextWatcher mTextWatcher;

        //动态更新TextWathcer的position
        public void updatePosition(int position) {
            mTextWatcher.updatePosition(position);
        }
    }

    class MyTextWatcher implements TextWatcher {
        //由于TextWatcher的afterTextChanged中拿不到对应的position值，所以自己创建一个子类
        private int mPosition;

        public void updatePosition(int position) {
            mPosition = position;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            logMindMap.put(mPosition, s.toString());
        }
    }

    private void equalLevel(int position,LogViewHolder logViewHolder){
        switch (logVoArrayList.get(position).getLOG_LEVEL()){
            case "A":
                logViewHolder.tv_firstCharacter.setText("A(最重要-自己做)");
                break;
            case "B":
                logViewHolder.tv_firstCharacter.setText("B(重要-压缩做)");
                break;
            case "C":
                logViewHolder.tv_firstCharacter.setText("C(次重要-授权别人做)");
                break;
        }

    }
}


