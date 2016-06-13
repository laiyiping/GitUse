package com.strong.googleplay.utils;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

/**
 * Created by Administrator on 2016/6/4.
 */
public class DrawableUtils {
    public static GradientDrawable creatShape(int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(UiUtils.dip2px(10));
        return drawable;
    }

    public static StateListDrawable creatStateListDrawable(Drawable pressed, Drawable normal) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressed);
        stateListDrawable.addState(new int[]{}, normal);
        return stateListDrawable;
    }
}
