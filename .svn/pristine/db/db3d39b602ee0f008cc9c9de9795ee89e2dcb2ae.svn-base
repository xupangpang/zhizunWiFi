package com.zhizun.zhizunwifi.utils.router;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;

/**
 * @author xupp
 * @date 2017/4/19
 */

public class RouterBallPulseIndicator extends RouterIndicator {
    public static final float SCALE=1.0f;

    //scale x ,y
    private float[] scaleFloats=new float[]{SCALE,
            SCALE,
            SCALE,
            SCALE,SCALE,SCALE,SCALE,SCALE,SCALE,SCALE,SCALE};



    @Override
    public void draw(Canvas canvas, Paint paint) {
        float circleSpacing= (float)(getWidth() / 3.5) ;
        float radius=(Math.min(getWidth(),getHeight())-circleSpacing*2)/6;
        float x = getWidth()/ 2-(radius*2+circleSpacing);
        float y=getHeight() / 2;
        for (int i = 0; i < 11; i++) {
            canvas.save();
            float translateX = x + (radius * 2) * i + circleSpacing * i ;
            canvas.translate(translateX, y);
            canvas.scale(scaleFloats[i], scaleFloats[i]);
            canvas.drawCircle(0, 0, radius, paint);
            canvas.restore();
        }
    }

    @Override
    public ArrayList<ValueAnimator> onCreateAnimators() {
        ArrayList<ValueAnimator> animators=new ArrayList<>();
        int[] delays=new int[]{120,240,360,480,600,720,840,960,1080,1200,1320};
        for (int i = 0; i < 11; i++) {
            final int index=i;

            ValueAnimator scaleAnim= ValueAnimator.ofFloat(1,0.3f,1);

            scaleAnim.setDuration(750);
            scaleAnim.setRepeatCount(-1);
            scaleAnim.setStartDelay(delays[i]);

            addUpdateListener(scaleAnim,new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    scaleFloats[index] = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            animators.add(scaleAnim);
        }
        return animators;
    }

}
