/**
  * Generated by smali2java 1.0.0.558
  * Copyright (C) 2013 Hensence.com
  */

package com.zhizun.zhizunwifi.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.view.View;
import com.zhizun.zhizunwifi.R;

@SuppressLint("DrawAllocation") 
public class SpeedTestPoint extends View {
    private float a;
    private int b;
    private Paint c;
    private Bitmap d;
    
    public SpeedTestPoint(Context p1) {
        super(p1);
    }
    
    public SpeedTestPoint(Context p1, AttributeSet p2) {
        super(p1, p2);
    }
    
    public SpeedTestPoint(Context p1, AttributeSet p2, int p3) {
        super(p1, p2, p3);
    }
    
    public final void a(float p1) {
        a = p1;
        postInvalidate();
    }
    
    protected void onDraw(Canvas p1) {
        super.onDraw(p1);
//        getMeasuredWidth() = getMeasuredWidth() / 0x2;
        if(c == null) {
            c = new Paint();
            c.setAntiAlias(true);
        }
        /*for(; d == null;){}
        if(d.isRecycled()){
        		
        }*/
        		
        d = BitmapFactory.decodeResource(getResources(), R.drawable.speed_pointer);
//        p1.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.speed_pointer), 0.0f, 0.0f, c);
        p1.save();
        p1.setDrawFilter(new PaintFlagsDrawFilter(0x0, 0x3));
//        p1.rotate(a, (float)getMeasuredWidth()/2, (float)getMeasuredWidth()/2);
//        p1.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.speed_pointer), 0.0f, 0.0f, c);
        
        int x = d.getWidth();
        int y = d.getHeight();
        p1.translate(x, y);
        p1.rotate(55);
        p1.translate(-y, x-y);
        p1.drawBitmap( d , 0, 0,c);
        
        p1.restore();
    }
    
    public final void a(int p1) {
        b = p1;
    }
}
