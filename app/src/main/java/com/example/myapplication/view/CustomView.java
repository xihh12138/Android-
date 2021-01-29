package com.example.myapplication.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CustomView extends View implements View.OnClickListener {

    private int mX;
    private int mY;

    private Paint paint;

    private Rect rect;

    private int count;

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(50);
        rect=new Rect();
        setOnClickListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mWidth=MeasureSpec.getSize(widthMeasureSpec);
        int mHeight=MeasureSpec.getSize(heightMeasureSpec);
        if (MeasureSpec.getMode(widthMeasureSpec)==MeasureSpec.AT_MOST){//wrap content
            String text="点了我"+count+"次";
            paint.getTextBounds(text,0,text.length(),rect);
            mWidth=rect.width()+10;
        }
        if (MeasureSpec.getMode(heightMeasureSpec)==MeasureSpec.AT_MOST){//wrap content
            String text="点了我"+count+"次";
            paint.getTextBounds(text,0,text.length(),rect);
            mHeight=rect.height()+10;
        }
        setMeasuredDimension(mWidth,mHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mX=left;
        mY=top;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.GRAY);
        canvas.drawRect(0,0,getWidth(),getHeight(),paint);
        paint.setColor(Color.CYAN);
        String text="点了我"+count+"次";
        paint.getTextBounds(text,0,text.length(),rect);
        int textHeight;
        if (getWidth()-10<=rect.width()){ textHeight=rect.height(); }
        else { textHeight=(getHeight()-rect.height())/2; }
        canvas.drawText("点了我"+count+"次",(getWidth()-rect.width())/2,textHeight,paint);
    }

    @Override
    public void onClick(View v) {
        count++;
        invalidate();
    }
}
