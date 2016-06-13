package com.strong.googleplay.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.strong.googleplay.utils.LogUtils;

/**
 * Created by Administrator on 2016/6/8.
 */
public class CircleProgress extends View {
    public CircleProgress(Context context) {
        super(context);
    }

    public CircleProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    float progress=0;
    int mCircleWidth=8;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        Paint paint = new Paint();
        paint.setAntiAlias(true);  //消除锯齿

        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(height/3);

        paint.setStrokeWidth(2); //设置圆环的宽度
        paint.setColor(Color.GRAY);
        float center=width/2f;
        canvas.drawCircle(center,center,center-1,paint);
        canvas.drawCircle(center,center,center-mCircleWidth+1,paint);

        paint.setStrokeWidth(mCircleWidth); //设置圆环的宽度
        paint.setColor(Color.GREEN);
        RectF rectF=new RectF(mCircleWidth/2f,mCircleWidth/2f,width-mCircleWidth/2f,height-mCircleWidth/2f);
        canvas.drawArc(rectF,270,progress*360,false,paint);

        paint.setStrokeWidth(mCircleWidth);
        canvas.drawLine(width/5*2,height/7*2.5f,width/5*2,height/7*4.5f,paint);
        canvas.drawLine(width/5*3,height/7*2.5f,width/5*3,height/7*4.5f,paint);

    }

    public void setProgress(float progress) {
        this.progress = progress;
        postInvalidate();

    }
}
