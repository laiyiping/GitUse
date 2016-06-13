package com.strong.googleplay.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;

import com.strong.googleplay.BaseApplication;
import com.strong.googleplay.R;
import com.strong.googleplay.holder.HomePictureHolder;

/**
 * Created by Administrator on 2016/5/28.
 */
public class UiUtils {

    /**
     * 获取资源数组
     * @param id 数组id
     */

    public static String[] getStringArray(int id) {
        return getResources().getStringArray(id);
    }

    public static Resources getResources() {
        return BaseApplication.getApplication().getResources();
    }

    public static Context getContext() {
        return BaseApplication.getApplication();
    }

    public static int dip2px(int dip) {
        final float scale=getResources().getDisplayMetrics().density;
        return (int) (dip*scale+0.5f);
    }

    public static int px2dip(int px) {
        final float scale=getResources().getDisplayMetrics().density;
        return (int) (px/scale+0.5f);
    }


    /**
     * 将runnable提交到主线程运行
     * @param runnable 解决
     */
    public static void runOnUiThread(Runnable runnable) {
        if (android.os.Process.myTid()==BaseApplication.getMainTid()) {
            runnable.run();
        } else {
            BaseApplication.getHandler().post(runnable);
        }
    }

    public static Drawable getDrawalbe(int id) {
        return getResources().getDrawable(id);
    }

    public static int getDimens(int dat) {
        return (int) getResources().getDimension(dat);
    }

    public static void postDelayed(Runnable runTask, int time) {
        BaseApplication.getHandler().postDelayed(runTask, time);
    }

    public static void cancelDelayed(Runnable  runTask) {
        BaseApplication.getHandler().removeCallbacks(runTask);
    }

    public static View inflate(int id) {
        return View.inflate(getContext(),id,null);
    }
}
