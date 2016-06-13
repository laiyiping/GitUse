package com.strong.googleplay.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2016/6/3.
 */
public class RatioLayout extends FrameLayout {
    public RatioLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        ratio=attrs.getAttributeFloatValue("http://schemas.android.com/apk/res-auto","ratio",2.43f);
    }

    public RatioLayout(Context context) {
        super(context);
    }

    float ratio=1f;  //宽高比例
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);  //测量宽度的模式
        int widthSize = MeasureSpec.getSize(widthMeasureSpec); //宽度大小
        int width = widthSize - getPaddingLeft() - getPaddingRight(); //去掉左右padding

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);  //测量高度的模式
        int heightSize = MeasureSpec.getSize(heightMeasureSpec); //高度大小
        int height = heightSize - getPaddingTop() - getPaddingBottom(); //去掉左右padding

        if (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY) {//高度宽修正高
            height= (int) (width/ratio+0.5f);
        } else if (widthMode != MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {//根据高修正宽
            width= (int) (height*ratio+0.5f);
        }

        width += (getPaddingLeft()+getPaddingRight());
        height += (getPaddingTop()+getPaddingBottom());

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
