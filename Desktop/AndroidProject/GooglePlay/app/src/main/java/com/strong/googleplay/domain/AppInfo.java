package com.strong.googleplay.domain;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/30.
 */
public class AppInfo {

    private long id;
    private String name;
    private String packageName;
    private String iconUrl;
    private float stars;
    private long size;
    private String downloadUrl;
    private String des;


    //---------------------------------------------
    private String downloadNum;
    private String version;
    private String date;
    private String author;
    private ArrayList<String> screen;
    private ArrayList<String> safeUrl;
    private ArrayList<String> safeDesUrl;
    private ArrayList<String> safeDes;
    private ArrayList<Integer> safeDesColor;


    public AppInfo(long id, String name, String iconUrl, String packageName, float stars, long size, String downloadUrl, String des) {
        this.id = id;
        this.name = name;
        this.iconUrl = iconUrl;
        this.packageName = packageName;
        this.stars = stars;
        this.size = size;
        this.downloadUrl = downloadUrl;
        this.des = des;
    }

    public AppInfo(long id, String name, String packageName, String iconUrl, float stars, long size, String downloadUrl, String des, String downloadNum, String version, String date, String author, ArrayList<String> screen, ArrayList<String> safeUrl, ArrayList<String> safeDesUrl, ArrayList<String> safeDes, ArrayList<Integer> safeDesColor) {
        this.id = id;
        this.name = name;
        this.packageName = packageName;
        this.iconUrl = iconUrl;
        this.stars = stars;
        this.size = size;
        this.downloadUrl = downloadUrl;
        this.des = des;
        this.downloadNum = downloadNum;
        this.version = version;
        this.date = date;
        this.author = author;
        this.screen = screen;
        this.safeUrl = safeUrl;
        this.safeDesUrl = safeDesUrl;
        this.safeDes = safeDes;
        this.safeDesColor = safeDesColor;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getPackageName() {
        return packageName;
    }

    public float getStars() {
        return stars;
    }

    public long getSize() {
        return size;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getDes() {
        return des;
    }

    public String getDownloadNum() {
        return downloadNum;
    }

    public String getVersion() {
        return version;
    }

    public String getDate() {
        return date;
    }

    public String getAuthor() {
        return author;
    }

    public ArrayList<String> getScreen() {
        return screen;
    }

    public ArrayList<String> getSafeUrl() {
        return safeUrl;
    }

    public ArrayList<String> getSafeDesUrl() {
        return safeDesUrl;
    }

    public ArrayList<String> getSafeDes() {
        return safeDes;
    }

    public ArrayList<Integer> getSafeDesColor() {
        return safeDesColor;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", packageName='" + packageName + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", stars=" + stars +
                ", size=" + size +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", des='" + des + '\'' +
                '}';
    }
}
