package com.strong.googleplay.domain;

/**
 * Created by Administrator on 2016/5/30.
 */
public class SubjectInfo {

    private String des;
    private String url;

    public SubjectInfo(String des, String url) {
        this.des = des;
        this.url = url;
    }

    public String getDes() {
        return des;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "SubjectInfo{" +
                "des='" + des + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
