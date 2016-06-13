package com.strong.googleplay.utils;

import com.lidroid.xutils.BitmapUtils;

/**
 * Created by Administrator on 2016/5/30.
 */
public class BatmapHelper {

    public static BitmapUtils bitmapUtils;

    public static BitmapUtils getBitmapUtils() {
        if (bitmapUtils == null) {
            bitmapUtils = new BitmapUtils(UiUtils.getContext(), FileUtils.getImageDir().getAbsolutePath(), 0.3f);
//            bitmapUtils = new BitmapUtils(UiUtils.getContext());
        }
        return bitmapUtils;
    }
}
