package com.strong.googleplay.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.strong.googleplay.utils.LogUtils;
import com.strong.googleplay.utils.UiUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/5.
 */
public class MyFlowlayout extends ViewGroup {
    private int horizontalSpace= UiUtils.dip2px(13);
    private int useWidth;
    private int verticalSpace=UiUtils.dip2px(13);

    public MyFlowlayout(Context context) {
        super(context);
    }

    public MyFlowlayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    ArrayList<Line> mLines = new ArrayList<>();
    Line currentLine;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);//获取当前控件的测量模式
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);//获取当前控件的宽高

        width -= (getPaddingLeft() + getPaddingRight());//子控件实际摆放的可用宽度
        int childWidthMode;
        int childHeightMode;
        childWidthMode = widthMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : widthMode;
        childHeightMode = heightMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : heightMode;

        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width, childWidthMode);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, childHeightMode);

        mLines.clear();
        currentLine=new Line();
        useWidth = 0; //当前行已经宽度已经使用的值
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            int measureWidth = child.getMeasuredWidth();//获得子view的宽度
            useWidth +=measureWidth;
            if (useWidth <= width) {//宽度还没超父控件
                currentLine.addChild(child);
                useWidth +=horizontalSpace;//加上child之间的间隔
                if (useWidth > width) {
                    newLine();//换行
                }

            } else {
                   //换行
                if (currentLine.getChildCount() == 0) {//一个child一行放不下的情况
                    currentLine.addChild(child);
                } else {
                    i--;//当前这个child还没添加
                }
                newLine();
            }
        }
        if ((!mLines.contains(currentLine))) {
            mLines.add(currentLine);
        }

        int totalHeight=0;
        for (Line line :
                mLines) {
            totalHeight += line.getHeight();
        }
        totalHeight+= verticalSpace *(mLines.size()-1)+getPaddingTop()+getPaddingBottom();


//        LogUtils.e("高"+totalHeight+"   宽"+width+"   行数"+mLines.size()+"   子个数"+getChildCount());

        setMeasuredDimension(width+getPaddingLeft()+getPaddingRight(), resolveSize(totalHeight, heightMeasureSpec));
    }

    //保存当前行，新建另一行
    private void newLine() {
        mLines.add(currentLine);
        currentLine=new Line();
        useWidth=0;
    }

    private class Line {
        ArrayList<View> children = new ArrayList<>();
        int height=0;
        public void addChild(View child) {
            children.add(child);
            height = height > child.getMeasuredHeight() ? height : child.getMeasuredHeight();
        }

        public int getChildCount() {
            return children.size();
        }

        public int getHeight() {
            return height;
        }

        public void layout(int l, int t) {
            //计算出剩余空间
            //将剩余空间平分给每个child

            int lineWidth=0;
            for (int i = 0; i < children.size(); i++) {
                lineWidth+=children.get(i).getMeasuredWidth();
            }
            lineWidth += (children.size() - 1) * horizontalSpace;
            lineWidth+=getPaddingLeft()+getPaddingRight();
            int surplus=getMeasuredWidth()-lineWidth;//剩余
            surplus/=children.size();

            for (int i = 0; i < children.size(); i++) {
                View child = children.get(i);
                child.layout(l,t,l+child.getMeasuredWidth()+surplus,t+child.getMeasuredHeight());
                l+=child.getMeasuredWidth()+horizontalSpace+surplus;
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        l += getPaddingLeft();
        t += getPaddingTop();
        for (int i = 0; i < mLines.size(); i++) {
            Line line = mLines.get(i);
            line.layout(l, t);
            t+=line.getHeight()+verticalSpace;
        }

        for (int i = 0; i < getChildCount(); i++) {
            TextView textView = (TextView) getChildAt(i);
            textView.setGravity(Gravity.CENTER);
        }
    }
}
