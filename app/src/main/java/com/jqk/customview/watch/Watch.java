package com.jqk.customview.watch;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.jqk.customview.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Watch extends View {
    private static final String TAG = "watch";

    private int dialColor;
    private int circleColor;
    private int scaleColor;
    private int centerColor;

    private Paint dialPaint; // 表盘
    private Paint circlePaint; // 白圈
    private Paint centerPaint; // 中心红点
    private Paint LongScalePaint; // 长刻度
    private Paint shortScalePaint; // 短刻度
    private Paint textPaint; // 数字

    private float dialLeft, dialTop, dialRight, dialBottom;

    private int dialWidth, dialHeight;
    private int circleCenterX, circleCenterY;
    private int dialPointWidth = 5;
    private int hourPointWidth = 20;
    private int minutePointWidth = 13;
    private int secondPointWidth = 6;
    private int rotateAngle = 6;

    private Paint hourHandPaint, minuteHandPaint, secondHandPaint;
    private int hourHandColor, minuteHandColor, secondHandColor;
    private int hour, minute, second;


    RectF dialRectf;
    Rect textBound = new Rect();

    public Watch(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.watch,
                0, 0);

        try {
            dialColor = a.getColor(R.styleable.watch_dialColor, getResources().getColor(android.R.color.black));
            circleColor = a.getColor(R.styleable.watch_dialColor, getResources().getColor(android.R.color.white));
            scaleColor = a.getColor(R.styleable.watch_dialColor, getResources().getColor(android.R.color.white));
            hourHandColor = a.getColor(R.styleable.watch_hourHandColor, getResources().getColor(R.color.colorHour));
            minuteHandColor = a.getColor(R.styleable.watch_minuteHandColor, getResources().getColor(R.color.colorMinute));
            secondHandColor = a.getColor(R.styleable.watch_secondHandColor, getResources().getColor(R.color.colorSecond));
            centerColor = a.getColor(R.styleable.watch_secondHandColor, getResources().getColor(R.color.colorSecond));
        } finally {
            a.recycle();
        }

        init();
    }

    public void init() {
        // 表盘
        dialPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dialPaint.setStyle(Paint.Style.FILL);
        dialPaint.setColor(dialColor);
        dialPaint.setStrokeWidth(dialPointWidth);
        // 白色边缘
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setColor(circleColor);
        circlePaint.setStrokeWidth(dialPointWidth);
        // 中心原点
        centerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        centerPaint.setStyle(Paint.Style.FILL);
        centerPaint.setStrokeCap(Paint.Cap.ROUND);
        centerPaint.setStrokeWidth(30);
        centerPaint.setColor(centerColor);
        // 长刻度
        LongScalePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        LongScalePaint.setStrokeCap(Paint.Cap.SQUARE);
        LongScalePaint.setStrokeWidth(15);
        LongScalePaint.setColor(scaleColor);
        // 短刻度
        shortScalePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        shortScalePaint.setStrokeCap(Paint.Cap.ROUND);
        shortScalePaint.setStrokeWidth(5);
        shortScalePaint.setColor(scaleColor);
        // 数字
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(scaleColor);
        // 时针
        hourHandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        hourHandPaint.setStrokeCap(Paint.Cap.ROUND);
        hourHandPaint.setStrokeWidth(hourPointWidth);
        hourHandPaint.setColor(hourHandColor);
        // 分针
        minuteHandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        minuteHandPaint.setStrokeCap(Paint.Cap.ROUND);
        minuteHandPaint.setStrokeWidth(minutePointWidth);
        minuteHandPaint.setColor(minuteHandColor);
        // 秒针
        secondHandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        secondHandPaint.setStrokeCap(Paint.Cap.ROUND);
        secondHandPaint.setStrokeWidth(secondPointWidth);
        secondHandPaint.setColor(secondHandColor);
    }

    public void setHourHandColor(int hourHandColor) {
        this.hourHandColor = hourHandColor;
        hourHandPaint.setColor(hourHandColor);
        invalidate(); // 重新绘制
        requestLayout();  // 重新布局
    }

    public void setMinuteHandColor(int minuteHandColor) {
        this.minuteHandColor = minuteHandColor;
        minuteHandPaint.setColor(minuteHandColor);
        invalidate();
        requestLayout();
    }

    public void setSecondHandColor(int secondHandColor) {
        this.secondHandColor = secondHandColor;
        secondHandPaint.setColor(secondHandColor);
        invalidate();
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int WidthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        switch (widthSpecMode) {
            case MeasureSpec.UNSPECIFIED:
                dialWidth = 20;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                dialWidth = WidthSpecSize;
                break;
        }
        switch (heightSpecMode) {
            case MeasureSpec.UNSPECIFIED:
                dialHeight = 20;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                dialHeight = heightSpecSize;
                break;
        }

        dialLeft = dialPointWidth;
        dialRight = dialWidth - dialPointWidth;
        dialTop = dialPointWidth;
        dialBottom = dialHeight - dialPointWidth;
        circleCenterX = dialWidth / 2;
        circleCenterY = dialHeight / 2;

        textPaint.setTextSize(circleCenterX / 6);

        setMeasuredDimension(dialWidth, dialHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 获取时间
        getTime();
        setTime(hour, minute, second);
        // 绘制表盘
        dialRectf = new RectF(dialLeft, dialTop, dialRight, dialBottom);
        canvas.drawOval(dialRectf, dialPaint);
        // 绘制表盘框
        dialRectf = new RectF(dialLeft + dialPointWidth, dialTop + dialPointWidth, dialRight - dialPointWidth, dialBottom - dialPointWidth);
        canvas.drawOval(dialRectf, circlePaint);

        // 绘制刻度
        for (int i = 1; i <= 60; i++) {
            canvas.rotate(rotateAngle, circleCenterX, circleCenterY); // 旋转画布
            if (i % 5 == 0) {
                canvas.drawLine(circleCenterX, circleCenterX / 8 - 10, circleCenterX, circleCenterX / 8 + 10, LongScalePaint); // 绘制长刻度线

            } else {
                canvas.drawLine(circleCenterX, circleCenterX / 8, circleCenterX, circleCenterX / 8 + 5, shortScalePaint); // 绘制短刻度线
            }
        }
        // 绘制数字
        for (int i = 1; i <= 60; i++) {
            if (i % 5 == 0) {
                Double sinx = Math.sin(Math.toRadians(i * rotateAngle));
                Double cosy = Math.cos(Math.toRadians(i * rotateAngle));

                float x3 = (float) (circleCenterX + (circleCenterX * 7 / 10 + 20) * sinx);
                float y3 = (float) (circleCenterX - (circleCenterX * 7 / 10 + 20) * cosy);

                String number = i / 5 + "";
                textPaint.getTextBounds(number, 0, number.length(), textBound);

                canvas.drawText(i / 5 + "", x3 - textBound.width() / 2, y3 + textBound.height() / 2, textPaint);
            }
        }
        // 绘制时针
        drawHourHand(hour, canvas);
        // 绘制分针
        drawMinuteHand(minute, canvas);
        // 绘制秒针
        drawSecondHand(second, canvas);
        // 绘制圆点
        canvas.drawPoint(circleCenterX, circleCenterY, centerPaint);

        postInvalidateDelayed(1000);//每秒刷新一次
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void drawHourHand(int hour, Canvas canvas) {
        int angle1 = minute / 2;
        int angle = hour * rotateAngle * 5 + angle1;
        canvas.rotate(angle, circleCenterX, circleCenterY);
        canvas.drawLine(circleCenterX, circleCenterY + 30, circleCenterX, circleCenterY / 2, hourHandPaint);
        canvas.rotate(360 - angle, circleCenterX, circleCenterY); // 将画布旋转到初始位置
    }

    private void drawMinuteHand(int minute, Canvas canvas) {
        int angle = minute * rotateAngle;
        canvas.rotate(angle, circleCenterX, circleCenterY);
        canvas.drawLine(circleCenterX, circleCenterY + 30, circleCenterX, circleCenterY / 3, minuteHandPaint);
        canvas.rotate(360 - angle, circleCenterX, circleCenterY); // 将画布旋转到初始位置
    }

    private void drawSecondHand(int second, Canvas canvas) {
        int angle = second * rotateAngle;
        canvas.rotate(angle, circleCenterX, circleCenterY);
        canvas.drawLine(circleCenterX, circleCenterY + 30, circleCenterX, dialTop + 20, secondHandPaint);
        canvas.rotate(360 - angle, circleCenterX, circleCenterY); // 将画布旋转到初始位置
    }

    public void setTime(int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public void getTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH");
        hour = Integer.parseInt(dateFormat.format(date));
        dateFormat = new SimpleDateFormat("mm");
        minute = Integer.parseInt(dateFormat.format(date));
        dateFormat = new SimpleDateFormat("ss");
        second = Integer.parseInt(dateFormat.format(date));
    }
}
