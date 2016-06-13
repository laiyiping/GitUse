package com.strong.googleplay.utils;

import android.os.Environment;
import android.provider.DocumentsContract;

import java.io.File;

/**
 * Created by Administrator on 2016/5/29.
 */
public class FileUtils {

    public static final String ROOT = "GooglePlay";
    public static final String CACHE = "cache";
    private static final String ICON = "image";
    private static final String FILE = "file";

    public static File getDir(String string) {
        StringBuilder path = new StringBuilder();

        if (isSdAvailable()) {//SD卡可用保存在sd卡
            path.append(Environment.getExternalStorageDirectory().getAbsolutePath());
            path.append(File.separator);
            path.append(ROOT);
            path.append(File.separator);
            path.append(string);
        } else { //保存在手机内部存储
            File cacheDir = UiUtils.getContext().getCacheDir();
            path.append(cacheDir.getAbsolutePath());
            path.append(File.separator);
            path.append(string);
        }

        File file = new File(path.toString());
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();//创建文件夹
        }
        return file;
    }

    public static File getCacheDir() {
        return getDir(CACHE);
    }

    public static File getImageDir() {
        return getDir(ICON);
    }




    public static boolean isSdAvailable() {

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    public static File getFileDir() {
        return getDir(FILE);
    }
}
