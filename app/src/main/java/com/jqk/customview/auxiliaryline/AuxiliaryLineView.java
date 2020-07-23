package com.jqk.customview.auxiliaryline;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.jqk.customview.R;

/**
 * ---------------------------------------------------------
 * \                                                       \
 * \                                                       \
 * \                                                       \
 * \                                                       \
 * \                                                       \
 * \ (x1, y1)    (x2, y2)             (x3, y3)     (x4, y4)\
 * -----`-----------`---------------------`------------`----
 */
public class AuxiliaryLineView extends View {
    private int screenWidth = 1024;
    private int screenHeight = 600;

    private int yellowX1 = 0;
    private int yellowX2 = 0;
    private int yellowX3 = 0;
    private int yellowX4 = 0;

    private int yellowY1 = 0;
    private int yellowY2 = 0;
    private int yellowY3 = 0;
    private int yellowY4 = 0;

    private int redX1 = 0;
    private int redX2 = 0;
    private int redX3 = 0;
    private int redX4 = 0;

    private int redY1 = 0;
    private int redY2 = 0;
    private int redY3 = 0;
    private int redY4 = 0;

    private int bottomX1 = 0;
    private int bottomX2 = 0;
    private int bottomX3 = 0;
    private int bottomX4 = 0;

    private int bottomY1 = 0;
    private int bottomY2 = 0;
    private int bottomY3 = 0;
    private int bottomY4 = 0;

    private int greenX1 = 0;
    private int greenX2 = 0;
    private int greenX3 = 0;
    private int greenX4 = 0;

    private int greenY1 = 0;
    private int greenY2 = 0;
    private int greenY3 = 0;
    private int greenY4 = 0;

    private Paint redLine;
    private Paint yellowLine;
    private Paint greenLine;

    private Path redPath;
    private Path yellowPath1;
    private Path yellowPath2;
    private Path greenPath1;
    private Path greenPath2;

    private int differX = 0;
    private int yellowDifferX = 0;
    private int redDifferX = 0;

    private int lineSpace = 0;
    // 可调参数
    private int bottomWidth = 1000;
    private int redHeight = 80;
    private int yellowHeight = 240;
    private int greenHeight = 350;
    private int slope = 50;

    public AuxiliaryLineView(Context context) {
        super(context);
        init();
    }

    public AuxiliaryLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AuxiliaryLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        differX = (int) (greenHeight / Math.tan(Math.toRadians(slope)));
        yellowDifferX = (int) (differX * (yellowHeight / (double) greenHeight));
        redDifferX = (int) (differX * (redHeight / (double) greenHeight));

        L.d("differX = " + differX);
        L.d("differX / 2 = " + differX / 2);
        L.d("yellowDifferX = " + yellowDifferX);

        lineSpace = differX / 2;

        bottomX1 = (screenWidth - bottomWidth) / 2;
        bottomX2 = bottomX1 + lineSpace;
        bottomX4 = screenWidth - bottomX1;
        bottomX3 = bottomX4 - lineSpace;

        bottomY1 = screenHeight;
        bottomY2 = bottomY1;
        bottomY3 = bottomY1;
        bottomY4 = bottomY1;

        greenX1 = bottomX1 + differX;
        greenX2 = greenX1 + lineSpace / 2;
        greenX4 = bottomX4 - differX;
        greenX3 = greenX4 - lineSpace / 2;

        greenY1 = screenHeight - greenHeight;
        greenY2 = greenY1;
        greenY3 = greenY1;
        greenY4 = greenY2;

        yellowX1 = bottomX1 + yellowDifferX;
        yellowX2 = bottomX2 + yellowDifferX;
        yellowX3 = bottomX3 - yellowDifferX;
        yellowX4 = bottomX4 - yellowDifferX;

        yellowY1 = screenHeight - yellowHeight;
        yellowY2 = yellowY1;
        yellowY3 = yellowY1;
        yellowY4 = yellowY1;

        redX1 = bottomX1 + redDifferX;
        redX2 = bottomX2 + redDifferX;
        redX3 = bottomX3 - redDifferX;
        redX4 = bottomX4 - redDifferX;

        redY1 = screenHeight - redHeight;
        redY2 = redY1;
        redY3 = redY1;
        redY4 = redY1;

        redLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        redLine.setStyle(Paint.Style.STROKE);
        redLine.setColor(getResources().getColor(R.color.lineRed));
        redLine.setStrokeWidth(10);

        yellowLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        yellowLine.setStyle(Paint.Style.STROKE);
        yellowLine.setColor(getResources().getColor(R.color.lineYellow));
        yellowLine.setStrokeWidth(10);

        greenLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        greenLine.setStyle(Paint.Style.STROKE);
        greenLine.setColor(getResources().getColor(R.color.lineGreen));
        greenLine.setStrokeWidth(10);

        redPath = new Path();
        redPath.moveTo(bottomX1, bottomY1);
        redPath.lineTo(redX1, redY1);
        redPath.lineTo(redX4, redY4);
        redPath.lineTo(bottomX4, bottomY4);

        yellowPath1 = new Path();
        yellowPath1.moveTo(redX1, redY1);
        yellowPath1.lineTo(yellowX1, yellowY1);
        yellowPath1.lineTo(yellowX2, yellowY2);

        yellowPath2 = new Path();
        yellowPath2.moveTo(yellowX3, yellowY3);
        yellowPath2.lineTo(yellowX4, yellowY4);
        yellowPath2.lineTo(redX4, redY4);

        greenPath1 = new Path();
        greenPath1.moveTo(yellowX1, yellowY1);
        greenPath1.lineTo(greenX1, greenY1);
        greenPath1.lineTo(greenX2, greenY2);

        greenPath2 = new Path();
        greenPath2.moveTo(greenX3, greenY3);
        greenPath2.lineTo(greenX4, greenY4);
        greenPath2.lineTo(yellowX4, yellowY4);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画绿线
        canvas.drawPath(greenPath1, greenLine);
        canvas.drawPath(greenPath2, greenLine);
        // 画黄线
        canvas.drawPath(yellowPath1, yellowLine);
        canvas.drawPath(yellowPath2, yellowLine);
        // 画红线
        canvas.drawPath(redPath, redLine);

    }
}
