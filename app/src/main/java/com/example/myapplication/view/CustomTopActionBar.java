package com.example.myapplication.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapplication.R;

public class CustomTopActionBar extends RelativeLayout {

    private ImageView iv_top_left;
    private TextView tv_top_title;
    private TextView tv_top_right;

    public CustomTopActionBar(Context context) {
        super(context);
    }

    public CustomTopActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.custom_top_actionbar,this);
        iv_top_left=findViewById(R.id.iv_top_left);
        tv_top_title=findViewById(R.id.tv_top_title);
        tv_top_right=findViewById(R.id.tv_top_right);
    }

    public String getTitle(){
        return tv_top_title.getText().toString();
    }

    public void setTitle(String title){
        tv_top_title.setText(title);
    }

    public void setRight(String right){
        tv_top_right.setText(right);
    }

    public void setOnClickLeftListener(OnClickListener listener){
        iv_top_left.setOnClickListener(listener);
    }

    public void setOnClickTitleListener(OnClickListener listener){
        tv_top_title.setOnClickListener(listener);
    }

    public void setOnClickRightListener(OnClickListener listener){
        tv_top_right.setOnClickListener(listener);
    }
}
