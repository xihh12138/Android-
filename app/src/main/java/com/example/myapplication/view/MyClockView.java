package com.example.myapplication.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.myapplication.R;

import java.util.Timer;
import java.util.TimerTask;

public class MyClockView extends View {

    private static final String TAG="MyClockView";

    private final int PS=1,PM=2,PH=3;

    private float clockRingWidth,
            clockDefaultScaleWidth,clockDefaultScaleHeight,clockSpecialScaleWidth, clockSpecialScaleHeight,
            clockHWidth,clockMWidth,clockSWidth;

    private int clockRingColor,clockScaleColor,clockNumberColor,
            clockHColor,clockMColor,clockSColor;

    //时钟半径,时钟宽度
    private float clockRadius;
    private int clockWidth;//相当于是时钟的圆心

    //时,分,秒
    private int cH,cM,cS;

    //拖动标识
    private boolean isDragging =false;
    //当前拖动指针标识，开始拖动时的角度（用于）
    private int draggingPointer,lastDraggingDegree=1;

    /**
     * 定时器
     */
    private Timer mTimer=new Timer();
    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            if (draggingPointer==0){
                maintainTime();
                cS++;
                //子线程用postInvalidate
                postInvalidate();
            }
        }
    };
    public MyClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttribute(context,attrs);
    }

    private void initAttribute(Context context, AttributeSet attributeSet){
        TypedArray typedArray=context.obtainStyledAttributes(attributeSet, R.styleable.MyClockView);
        clockRingWidth=typedArray.getDimension(R.styleable.MyClockView_clockRingWidth,5);
        clockDefaultScaleWidth=typedArray.getDimension(R.styleable.MyClockView_clockDefaultScaleWidth,2);
        clockDefaultScaleHeight=typedArray.getDimension(R.styleable.MyClockView_clockDefaultScaleHeight,10);
        clockSpecialScaleWidth=typedArray.getDimension(R.styleable.MyClockView_clockSpecialScaleWidth,4);
        clockSpecialScaleHeight=typedArray.getDimension(R.styleable.MyClockView_clockSpecialScaleHeight,15);
        clockHWidth=typedArray.getDimension(R.styleable.MyClockView_clockHWidth,10);
        clockMWidth=typedArray.getDimension(R.styleable.MyClockView_clockMWidth,5);
        clockSWidth=typedArray.getDimension(R.styleable.MyClockView_clockSWidth,2);

        clockRingColor=typedArray.getColor(R.styleable.MyClockView_clockRingColor, Color.DKGRAY);
        clockScaleColor=typedArray.getColor(R.styleable.MyClockView_clockScaleColor, Color.LTGRAY);
        clockNumberColor=typedArray.getColor(R.styleable.MyClockView_clockNumberColor, Color.BLACK);
        clockHColor=typedArray.getColor(R.styleable.MyClockView_clockHColor, Color.BLACK);
        clockMColor=typedArray.getColor(R.styleable.MyClockView_clockMColor, Color.GRAY);
        clockSColor=typedArray.getColor(R.styleable.MyClockView_clockSColor, Color.RED);

        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        final int measureWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int measureHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        switch (measureWidthMode) {
            case MeasureSpec.UNSPECIFIED:
                measureWidth = getSuggestedMinimumWidth();
                break;
            case MeasureSpec.AT_MOST:
                measureWidth = Math.min(measureWidth,clockWidth);
                break;
            default:
        }
        switch (measureHeightMode) {
            case MeasureSpec.UNSPECIFIED:
                measureHeight = getSuggestedMinimumWidth();
                break;
            case MeasureSpec.AT_MOST:
                measureHeight = Math.min(measureHeight, clockWidth);
                break;
            default:
        }
        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        clockWidth= w/2;
        clockRingWidth=clockWidth/45;
        clockDefaultScaleWidth=clockWidth/95;
        clockDefaultScaleHeight=clockWidth/20;
        clockSpecialScaleWidth=clockWidth/60;
        clockSpecialScaleHeight=clockWidth/15;
        clockHWidth=clockWidth/20;
        clockMWidth=clockWidth/45;
        clockSWidth=clockWidth/70;
        clockRadius= (float)(clockWidth*0.95);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(clockWidth, clockWidth);
        drawCircle(canvas);
        drawNumber(canvas);
        drawPointer(canvas);
    }

    /**
     * 画时钟和刻度
     * @param canvas
     */
    private void drawCircle(Canvas canvas){
        //时钟的画笔
        Paint clockCirclePaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        clockCirclePaint.setStyle(Paint.Style.STROKE);
        clockCirclePaint.setColor(clockRingColor);
        clockCirclePaint.setStrokeWidth(clockRingWidth);
        canvas.drawCircle(0,0, clockRadius,clockCirclePaint);
        clockCirclePaint.setColor(clockScaleColor);
        for (int i = 0; i < 60; i++) {
            if (i%5==0){//整点刻度
                clockCirclePaint.setStrokeWidth(clockSpecialScaleWidth);
                canvas.drawLine(0,-clockRadius+clockRingWidth/2,0,-clockRadius+clockRingWidth/2+clockSpecialScaleHeight,clockCirclePaint);
            }else {
                clockCirclePaint.setStrokeWidth(clockDefaultScaleWidth);
                canvas.drawLine(0,-clockRadius+clockRingWidth/2,0,-clockRadius+clockRingWidth/2+clockDefaultScaleHeight,clockCirclePaint);
            }
            canvas.rotate(6);
        }
    }

    /**
     * 画刻度上的数字
     * @param canvas
     */
    private void drawNumber(Canvas canvas){
        //数字的画笔
        int angle=0;
        Paint clockNumberPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        clockNumberPaint.setStyle(Paint.Style.FILL);
        clockNumberPaint.setTextSize(clockWidth/10);
        clockNumberPaint.setColor(clockNumberColor);
        Rect rect=new Rect();
        clockNumberPaint.getTextBounds("12",0,2,rect);
        canvas.drawText("12",-rect.width()/2-5,-clockRadius+clockRingWidth/2+clockSpecialScaleHeight+rect.height()+10,clockNumberPaint);
        for (int i = 1; i <= 11; i++) {
            angle+=30;
            canvas.rotate(30);
            final String number=String.valueOf(i);
            clockNumberPaint.getTextBounds(number,0,number.length(),rect);
            canvas.drawText(number,-rect.width()/2,-clockRadius+clockRingWidth/2+clockSpecialScaleHeight+rect.height()+10,clockNumberPaint);
        }
        canvas.rotate(30);
    }

    /**
     * 画指针
     * @param canvas
     */
    private void drawPointer(Canvas canvas){
        //指针的画笔
        Paint clockPointerPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        clockPointerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        clockPointerPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.save();
        clockPointerPaint.setColor(clockHColor);
        clockPointerPaint.setStrokeWidth(clockHWidth);
        canvas.rotate(cH*30+cM/2+cS/60/10);
        canvas.drawLine(0,0,0, -(float) (clockRadius*0.5),clockPointerPaint);
        canvas.restore();
        canvas.save();
        clockPointerPaint.setColor(clockMColor);
        clockPointerPaint.setStrokeWidth(clockMWidth);
        canvas.rotate(cM*6+cS/10);
        canvas.drawLine(0,0,0, -(float) (clockRadius*0.7),clockPointerPaint);
        canvas.restore();
        canvas.save();
        clockPointerPaint.setColor(clockSColor);
        clockPointerPaint.setStrokeWidth(clockSWidth);
        canvas.rotate(cS*6);
        canvas.drawLine(0,0,0, -(float) (clockRadius*0.8),clockPointerPaint);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float x=event.getX(),y=event.getY();
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            final int degree=getDegreeBetweenLines(x,y);
            if (!isDragging){//开始拖动时判断当前操作的指针，抬起手指清除标志
                isDragging =true;
                final int distance= (int) Math.pow(Math.pow(clockWidth-x,2)+Math.pow(clockWidth-y,2),0.5);//到圆心的距离
                if (distance<clockRadius*0.8){//可能是操作指针
                    int hDegree,mDegree,sDegree;
                    if (distance>clockRadius*0.7){//可能是秒针
                        sDegree=(cS*6);
                        if (Math.abs(sDegree-degree)<=10){//允许触碰误差在10°以内
                            draggingPointer=PS;
                        }else {//不是拖动指针
                            draggingPointer=0;
                        }
                    }else if (distance>clockRadius*0.5){//可能是分针或秒针
                        sDegree=(cS*6);
                        mDegree=(cM*6+cS/10);
                        final int sDiff=Math.abs(sDegree-degree);
                        final int mDiff=Math.abs(mDegree-degree);
                        final int min=Math.min(sDiff,mDiff);
                        if (min==sDiff&&sDiff<=10){//是秒针
                            draggingPointer=PS;
                        }else if (min==mDiff&&mDiff<=10){//是分针
                            draggingPointer=PM;
                        }else {//不是拖动指针
                            draggingPointer=0;
                        }
                    }else {//可能是任意指针
                        sDegree=(cS*6);
                        mDegree=(cM*6+cS/10);
                        hDegree=(cH*30+cM/2+cS/60/10);
                        final int sDiff=Math.abs(sDegree-degree);
                        final int mDiff=Math.abs(mDegree-degree);
                        final int hDiff=Math.abs(hDegree-degree);
                        final int min=Math.min(sDiff,Math.min(mDiff,hDiff));
                        if (min==sDiff&&sDiff<=10){//是秒针
                            draggingPointer=PS;
                        }else if (min==mDiff&&mDiff<=10){//是分针
                            draggingPointer=PM;
                        }else if (min==hDiff&&hDiff<=10){//是时针
                            draggingPointer=PH;
                        }else {//不是拖动指针
                            draggingPointer=0;
                        }
                    }
                }
            }
            if (isDragging){//如果正在拖动，根据拖动指针标志改变时间
                switch (draggingPointer){
                    case PS:
                        cS=degree/6;
                        break;
                    case PM:
                        cM=(degree-cS/10)/6;
                        cS=10*(degree-6*cM);
                        break;
                    case PH:
                        cH=(degree-cM/2+cS/60/10)/30;
                        cM=(5*(degree-degree/60-30*cH))/3;
                        cS=600*(degree-30*cH-cM/2);
                        break;
                    default:
                }
                if (lastDraggingDegree!=-1){
                    if (degree<=45&&lastDraggingDegree>=315){//从0°左滑到右
                        switch (draggingPointer){
                            case PS: cM++;break;
                            case PM: cH++;break;
                            default:
                        }
                    }else if(degree>=315&&lastDraggingDegree<=45) {//从0°右滑到左
                        switch (draggingPointer){
                            case PS: cM--;break;
                            case PM: cH--;break;
                            default:
                        }
                    }
                }
                maintainTime();
                lastDraggingDegree=degree;
                if (draggingPointer!=0){
                    postInvalidate();
                }
                Log.d(TAG, "onTouchEvent: cH="+cH+"     cM="+cM+"     cS="+cS);
            }
        }else if (event.getAction() == MotionEvent.ACTION_UP){
            isDragging =false;
            draggingPointer=0;
            lastDraggingDegree=-1;
        }
        return true;
    }

    public void start(){
        mTimer.schedule(task,0,1000);
    }

    private void maintainTime(){
        if (cS >= 60) {
            cS = 0;
            cM++;
        }
        if (cM >= 60){
            cM = 0;
            cH++;
        }
        if (cH >= 12){
            cH = 0;
        }
    }
    private int getDegreeBetweenLines(float x, float y){
        double degree = 0;
        if (x>clockWidth&&y<clockWidth){//第一象限
            degree=Math.atan((clockWidth-y)/(x-clockWidth));
            degree=degree*180/Math.PI;
            degree=90-degree;
        }else if (x<clockWidth&&y<clockWidth){//第二象限
            degree=Math.atan((clockWidth-y)/(clockWidth-x));
            degree=degree*180/Math.PI;
            degree=270+degree;
        }else if (x<clockWidth&&y>clockWidth){//第三象限
            degree=Math.atan((y-clockWidth)/(clockWidth-x));
            degree=degree*180/Math.PI;
            degree=270-degree;
        }else if (x>clockWidth&&y>clockWidth){//第四象限
            degree=Math.atan((y-clockWidth)/(x-clockWidth));
            degree=degree*180/Math.PI;
            degree=90+degree;
        }
        return (int) degree;
    }
}
