package com.strong.googleplay.domain;

/**
 * Created by Administrator on 2016/6/3.
 */
public class CategoryInfo {

    private String title;
    private String name1;
    private String name2;
    private String name3;
    private String url1;
    private String url2;
    private String url3;
    private boolean isTitle;



    public CategoryInfo(String title,boolean isTitle) {
        this.title = title;
        this.isTitle = isTitle;
    }

    public CategoryInfo(String title, String name1, String name2, String name3, String url1, String url2, String url3,boolean isTitle) {
        this.title = title;
        this.name1 = name1;
        this.name2 = name2;
        this.name3 = name3;
        this.url1 = url1;
        this.url2 = url2;
        this.url3 = url3;
    }


//    public void setIsTitle(boolean isTitle) {
//        this.isTitle = isTitle;
//    }

    public boolean getIsTitle() {

        return isTitle;
    }
    public String getTitle() {
        return title;
    }

    public String getName1() {
        return name1;
    }

    public String getName2() {
        return name2;
    }

    public String getName3() {
        return name3;
    }

    public String getUrl1() {
        return url1;
    }

    public String getUrl2() {
        return url2;
    }

    public String getUrl3() {
        return url3;
    }
}
