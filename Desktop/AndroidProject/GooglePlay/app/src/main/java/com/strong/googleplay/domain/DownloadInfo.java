package com.strong.googleplay.domain;

/**
 * Created by Administrator on 2016/6/6.
 */
public class DownloadInfo {

    private long id;
    private String packageName;
    private long appSize;
    private long currentSize;
    private String downloadUrl;



    private String path;
    private int state;

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setAppSize(long appSize) {
        this.appSize = appSize;
    }

    public void setCurrentSize(long currentSize) {
        this.currentSize = currentSize;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getId() {
        return id;
    }

    public String getPackageName() {
        return packageName;
    }

    public long getAppSize() {
        return appSize;
    }

    public long getCurrentSize() {
        return currentSize;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public int getState() {
        return state;
    }
}
