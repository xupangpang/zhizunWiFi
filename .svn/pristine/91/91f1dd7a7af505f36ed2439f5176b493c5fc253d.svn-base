package com.zhizun.zhizunwifi.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author xupp
 * @date 2017/4/18
 */

public class RouterRadarView extends View {
    private int w, h;// 获取控件宽高
    private Paint mPaintLine;// 画雷达圆线
    private Paint mPaintSolid;// 画雷达渐变实心圆
    private Matrix matrix;
    private int degrees;
    private Handler mHandler = new Handler();
    private int phase;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            degrees++;
            matrix.postRotate(degrees, w / 2, h / 2);//旋转矩阵
            RouterRadarView.this.invalidate();// 重绘
            mHandler.postDelayed(mRunnable, 55);
        }
    };

    public RouterRadarView(Context context) {
        this(context, null);
    }

    public RouterRadarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RouterRadarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        mHandler.postDelayed(mRunnable,500);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        w = getMeasuredWidth();//获取view的宽度
        h = getMeasuredHeight();//获取view的高度
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mPaintLine = new Paint();
        mPaintLine.setColor(Color.parseColor("#CCA1A1A1"));// 设置画笔
        mPaintLine.setStrokeWidth(2);// 设置画笔宽度
        mPaintLine.setAntiAlias(true);// 消除锯齿
        mPaintLine.setStyle(Paint.Style.STROKE);// 设置空心

        mPaintSolid = new Paint();
        mPaintSolid.setAntiAlias(true);// 消除锯齿
        mPaintSolid.setStyle(Paint.Style.FILL);//实心圆
        matrix = new Matrix();//创建组件
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Path path = new Path();
        path.addCircle(0, 0, 6, Path.Direction.CCW);
        PathEffect pathEffect = new PathDashPathEffect(path,60, phase, PathDashPathEffect.Style.ROTATE);
        mPaintLine.setPathEffect(pathEffect);
        canvas.drawLine(60, 40, 100, 40, mPaintLine);// 画线


        matrix.reset();//重置矩阵，避免累加，越转越快
        super.onDraw(canvas);
    }
}
