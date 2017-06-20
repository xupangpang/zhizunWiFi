package com.zhizun.zhizunwifi.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.zhizun.zhizunwifi.R;

/**
 * @author xupp
 * @date 2017/4/20
 */

public class CircleViewPhy extends View {
    /**
     * 第一种颜色
     */
    private int mFirstColor;
    /**
     * 第二种颜色
     */
    private int mSecondColor;
    /**
     * 圆弧的宽度
     */
    private int mCircleWidth;
    /**
     * 画笔
     */
    private Paint mPaint;

    private Paint wPaint;

    private Paint TPaint;
    /**
     * 圆弧的度数
     */
    private int mProgress;

    /**
     * 圆弧的度数
     */
    private int TProgress;
    /**
     * 圆弧绘制的速度
     */
    private int mSpeed;
    /**
     * 是不是开始绘制下一个圆弧
     */
    private boolean isNext = false;

    private int phase;

    private Rect mSrcRect, mDestRect;


    private float mStartAngle;
    private float mSweepAngle = 0;
    private static final int MIN_ANGLE_SWEEP = 3;
    private static final int MAX_ANGLE_SWEEP = 155;
    private int mAngleIncrement = 3;
    Bitmap bmp;
    private ImageView imageCircle;
    private Context mContext;
    private boolean isFinsh = false;
    private boolean autoRun = false;

    public CircleViewPhy(Context context) {
        this(context, null);
    }

    public CircleViewPhy(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public void setImageCircle(ImageView imageCircle) {
       this.imageCircle = imageCircle;

    }

    public void setfinsh(boolean finsh){
      this.isFinsh = finsh;
    }

    public void setautoRun(boolean run){
        this.autoRun = run;
    }


    /**
     * 获取自定义控件的一些值
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public CircleViewPhy(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleViewPhy, defStyleAttr, 0);

        mContext = context;

        for (int i = 0; i < a.getIndexCount(); i++) {

            switch (a.getIndex(i)) {
                case R.styleable.CircleViewPhy_firstColor:
                    mFirstColor = a.getColor(a.getIndex(i), Color.WHITE);
                    break;
                case R.styleable.CircleViewPhy_secondColor:
                    mSecondColor = a.getColor(a.getIndex(i), Color.RED);
                    break;
                case R.styleable.CircleViewPhy_speed:
                    mSpeed = a.getInt(a.getIndex(i), 20);
                    break;
                case R.styleable.CircleViewPhy_circleWidth:
                    mCircleWidth = a.getDimensionPixelOffset(a.getIndex(i), (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_PX, 20, getResources().getDisplayMetrics()));
                    break;
            }
        }
        a.recycle();
        mPaint = new Paint();
        wPaint = new Paint();
        TPaint = new Paint();


            //绘图线程
            new Thread() {
                @Override
                public void run() {
                    while (true) {
                        if (autoRun){
                            if (isFinsh){
                                TProgress ++;
                                if (TProgress == 360){
                                    TProgress = 360;
                                }
                            }

                            if (mProgress != 360) {
                                mProgress++;
                            }else {

                                final float angle = 5;
                                mStartAngle += angle;

                                if (mStartAngle > 360) {
                                    mStartAngle -= 360;
                                }



                                if (mSweepAngle > MAX_ANGLE_SWEEP) {
                                    mAngleIncrement = -mAngleIncrement;
                                } else if (mSweepAngle < MIN_ANGLE_SWEEP) {
                                    mSweepAngle = MIN_ANGLE_SWEEP;
                                } else if (mSweepAngle == MIN_ANGLE_SWEEP) {
                                    mAngleIncrement = -mAngleIncrement;
                                }

                                mSweepAngle += mAngleIncrement;
                            }

                        }
                        postInvalidate();
                        try {
                            if (mProgress == 360 && !isFinsh){

                                Thread.sleep(mSpeed + 5); //通过传递过来的速度参数来决定线程休眠的时间从而达到绘制速度的快慢
                            }else {

                                Thread.sleep(mSpeed); //通过传递过来的速度参数来决定线程休眠的时间从而达到绘制速度的快慢
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        int center = getWidth() / 2;
        int radius = center - mCircleWidth / 2;
        
        mPaint.setStrokeWidth(mCircleWidth); // 设置圆环的宽度
        wPaint.setStrokeWidth(mCircleWidth);
        TPaint.setStrokeWidth(mCircleWidth + 8);

        mPaint.setAntiAlias(true); // 消除锯齿
        wPaint.setAntiAlias(true); // 消除锯齿
        TPaint.setAntiAlias(true);

        mPaint.setStyle(Paint.Style.STROKE); // 设置空心
        wPaint.setStyle(Paint.Style.STROKE);
        TPaint.setStyle(Paint.Style.STROKE);

        RectF oval = new RectF(center - radius + 5, center - radius + 5, center + radius - 5, center + radius - 5); // 用于定义的圆弧的形状和大小的界限
        RectF oval0 = new RectF(center - radius + 50, center - radius + 50, center + radius - 50, center + radius - 50); // 用于定义的圆弧的形状和大小的界限




        if (!isFinsh){
            Path path = new Path();
            path.addCircle(0, 0, 5, Path.Direction.CCW);
            PathEffect pathEffect = new PathDashPathEffect(path,30, phase, PathDashPathEffect.Style.ROTATE);
            mPaint.setPathEffect(pathEffect);
            mPaint.setColor(mFirstColor); // 设置圆环的颜色
            canvas.drawArc(oval, -90, mProgress, false, mPaint); // 根据进度画圆弧

            if (mProgress == 360){
                wPaint.setColor(getResources().getColor(R.color.white_alpha_144)); // 设置圆环的颜色
                canvas.drawArc(oval0, -90, 360, false, wPaint);

                TPaint.setColor(Color.WHITE);
                canvas.drawArc(oval, mStartAngle, mSweepAngle, false, TPaint);

            }
        }else {
            wPaint.setColor(getResources().getColor(R.color.white)); // 设置圆环的颜色
            canvas.drawArc(oval0, -90, TProgress, false, wPaint);
        }


        }



}
