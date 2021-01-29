package com.example.myapplication.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class CustomFlowLayout extends ViewGroup  {

    public CustomFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
            int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
            int measureWidthMode = MeasureSpec.getMode(widthMeasureSpec);
            int measureHeightMode = MeasureSpec.getMode(heightMeasureSpec);

            int height = 0;
            int width = 0;
            int count = getChildCount();
            for (int i=0;i<count;i++) {
                //测量子控件
                View child = getChildAt(i);
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                //获得子控件的高度和宽度
                int childHeight = child.getMeasuredHeight();
                int childWidth = child.getMeasuredWidth();
                //得到最大宽度，并且累加高度
                height += childHeight;
                width = Math.max(childWidth, width);
            }
            setMeasuredDimension((measureWidthMode == MeasureSpec.EXACTLY) ? measureWidth: width, (measureHeightMode == MeasureSpec.EXACTLY) ? measureHeight: height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
